package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.Store;
import com.bridgelabz.bookStore.admin.model.BookInDisplay;
import com.bridgelabz.bookStore.admin.model.CartItem;
import com.bridgelabz.bookStore.admin.repository.*;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.repository.*;
import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.utility.CSVReader;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoreService implements IBookStoreService {

    @Autowired
    private ICustomerRepository iCustomerRepository;

    @Autowired
    private IBookRepository iBookRepository;

    @Autowired
    private IAddressRepository iAddressRepository;

    @Autowired
    private IOrderedBookRepository iOrderedBookRepository;

    @Autowired
    private ICartRepository iCartRepository;

    @Autowired
    ICartItemRepository iCartItemRepository;

    @Autowired
    IBookInDisplayRepository inDisplayRepository;

    @Autowired
    MailService mail;

    private Boolean inCart = false;

    int count = 0;

    private List<Book> books;

    @Override
    public void createBookStore() {
        this.books = new CSVReader().loadCensusData();
        this.books.forEach(book -> iBookRepository.save(book));
    }

    @Override
    public Store getBooks(Integer currentPage) {
        this.books = this.iBookRepository.findAll();
        List<List<Book>> bookList = new ArrayList<>();
        List<Book> innerBook = new ArrayList<>();
        int count = 0;
        for (Book book : this.books) {
            if (count == 8) {
                bookList.add(innerBook);
                innerBook = new ArrayList<>();
                count = 0;
            }
            count++;
            innerBook.add(book);
        }
        Store store = new Store();
        store.setTotalBooks(this.books.size());
        store.setNoOfPage(bookList.size());
        store.setBooks(bookList.get(currentPage - 1));
        return store;
    }

    @Override
    public List<CartItem> addToCart(CartDTO cartDTO) {
        List<CartItem> booksInCart = cartDTO.getCartItems();
        Optional<Book> book = iBookRepository.findById(cartDTO.getBookDTO().getBook().getId());

        CartItem cartItem = new CartItem();
        cartItem.setBook(cartDTO.getBookDTO().getBook());
        cartItem.setNoOfItems(cartDTO.getBookDTO().getUserSelectedQuantity());

        if (cartDTO.getUserToken() != null) {

            Optional<Customer> customer = iCustomerRepository.findById(Token
                    .decodeJWT(cartDTO.getUserToken()));

            if (customer.isPresent()) {

                if (customer.get().getUserCart().getCartItems().size() == 0) {                    ;
                    customer.get()
                            .getUserCart()
                            .setCartItems(iCartItemRepository.saveAll(booksInCart));
                }

                List<CartItem> books = customer.get().getUserCart().getCartItems();
                books.stream().map(currentBook -> {
                    if (currentBook.getBook().getId().equals(book.get().getId())) {
                        books.remove(currentBook);
                    }
                    return currentBook;
                });
                books.add(iCartItemRepository.save(cartItem));
                customer.get().getUserCart().setCartItems(books);
                booksInCart = iCustomerRepository.save(customer.get()).getUserCart().getCartItems();
            }
        } else {
            booksInCart.add(cartItem);
        }
        return booksInCart;
    }

    @Override
    public Cart editCart(CartDTO cartDTO) {
        Customer customer = null;
        if (cartDTO.getUserToken() != null) {
            customer = iCustomerRepository.findById(Token.decodeJWT(cartDTO.getUserToken())).get();
        }
        List<CartItem> selectedBooks = cartDTO.getCartItems();
        Book book = cartDTO.getBookDTO().getBook();
        int i = 0;
        while (i < selectedBooks.size()) {
            if (selectedBooks.get(i).getBook().getId().equals(book.getId())) {
                selectedBooks.get(i).setNoOfItems(cartDTO.getBookDTO().getUserSelectedQuantity());
                break;
            }
            i++;
        }

        return getCart(cartDTO, customer, selectedBooks);
    }

    @Override
    public Cart removeFromCart(CartDTO cartDTO) {
        Customer customer = null;
        if (cartDTO.getUserToken() != null) {
            customer = iCustomerRepository.findById(Token.decodeJWT(cartDTO.getUserToken())).get();
        }
        Book book = cartDTO.getBookDTO().getBook();
        List<CartItem> selectedBooks = cartDTO.getCartItems();
        for (int i = 0; i < selectedBooks.size(); i++) {
            if (selectedBooks.get(i).getBook().getId().equals(book.getId())) {
                selectedBooks.remove(selectedBooks.get(i));
                break;
            }
        }
        return getCart(cartDTO, customer, selectedBooks);
    }

    @NotNull
    private Cart getCart(CartDTO cartDTO, Customer customer, List<CartItem> selectedBooks) {
        Cart cart = new Cart();
        System.out.println(cartDTO.getNoOfItems());
        cart.setNoOfItems(cartDTO.getNoOfItems());
        cart.setCartItems(selectedBooks);
        if (customer != null) {
            customer.getUserCart().setNoOfItems(cart.getNoOfItems());
            customer.getUserCart().setCartItems(cart.getCartItems());
            iCustomerRepository.save(customer);
        }
        return cart;
    }

    @Override
    public Book bookInDisplay(Book book) {
        Book bookToDisplay;
        if (inDisplayRepository.existsById(1)) {
            BookInDisplay inDisplay = new BookInDisplay();
            inDisplay.setBook(book);
            BookInDisplay saveDisplay = inDisplayRepository.save(inDisplay);
            bookToDisplay = saveDisplay.getBook();
        } else {
            Optional<BookInDisplay> bookInDisplay = inDisplayRepository.findById(1);
            bookInDisplay.get().setBook(book);
            bookToDisplay = inDisplayRepository.save(bookInDisplay.get()).getBook();
        }
        return bookToDisplay;
    }

    @Override
    public Book getBookToDisplay() {
        return inDisplayRepository.findById(1).get().getBook();
    }
}
