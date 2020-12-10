package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.Store;
import com.bridgelabz.bookStore.admin.model.BookInDisplay;
import com.bridgelabz.bookStore.admin.repository.IBookInDisplayRepository;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.admin.model.SelectedBook;
import com.bridgelabz.bookStore.customer.repository.*;
import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.repository.IBookRepository;
import com.bridgelabz.bookStore.admin.repository.ICartRepository;
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
    private ISelectedRepository iSelectedRepository;

    @Autowired
    private ICartRepository iCartRepository;

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
        store.setBooks(bookList.get(currentPage-1));
        return store;
    }

    @Override
    public List<Book> addToCart(CartDTO cartDTO) {
        List<Book> booksInCart = cartDTO.getBooks();
        Optional<Book> book = iBookRepository.findById(cartDTO.getBookDTO().getBook().getId());
        if (cartDTO.getUserToken().length() > 0) {
            Optional<Customer> customer = iCustomerRepository.findById(Token
                                                             .decodeJWT(cartDTO.getUserToken()));
            if (customer.get().getUserCart() == null){
                customer.get().setUserCart(iCartRepository.save(new Cart()));
            }
            if (booksInCart.size() == 0){
                booksInCart.add(book.get());
            }
            for (int i = 0; i < booksInCart.size(); i++){
                if (customer.get().getUserCart().getBooks().size() == 0) {
                    booksInCart.get(i).setInCart(true);
                    customer.get().getUserCart().getBooks().add(booksInCart.get(i));
                    customer.get().getUserCart().setNoOfItems(+1);
                    iCartRepository.save(customer.get().getUserCart());
                    iCustomerRepository.save(customer.get());
                    booksInCart = customer.get().getUserCart().getBooks();
                    this.inCart = true;
                } else {
                    for (int j = 0; j < customer.get().getUserCart().getBooks().size(); j++){
                        if (!booksInCart.get(i).equals(
                                customer.get().getUserCart().getBooks().get(j))) {
                            customer.get().getUserCart().getBooks().add(booksInCart.get(i));
                            iCartRepository.save(customer.get().getUserCart());
                            iCustomerRepository.save(customer.get());
                        }
                        if (book.get().equals(
                                customer.get().getUserCart().getBooks().get(j))){
                            this.inCart = true;
                        }
                    }
                }
                if (!inCart) {
                    book.get().setInCart(true);
                    customer.get().getUserCart().getBooks().add(iBookRepository.save(book.get()));
                    customer.get().getUserCart().setNoOfItems(customer.get().getUserCart().getNoOfItems() + 1);
                    iCartRepository.save(customer.get().getUserCart());
                    booksInCart = iCustomerRepository.save(customer.get()).getUserCart().getBooks();
                }
            }
        } else {
            for (int i = 0; i < booksInCart.size(); i++) {
                if (!booksInCart.get(i).equals(book.get())) {
                    book.get().setInCart(true);
                    booksInCart.add(book.get());
                }
            }
        }
        return booksInCart;
    }

    private Integer noOfItemsInCart(List<Book> Books) {
        Integer counter = 0;
        for (Book selectedBook : Books) {
            counter += selectedBook.getQuantityInCart();
        }
        return counter;
    }

    @Override
    public Cart editCart(CartDTO cartDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(cartDTO.getUserToken()));
        List<Book> selectedBooks = cartDTO.getBooks();
        Optional<Book> book = iBookRepository.findById(cartDTO.getBookDTO().getBook().getId());
        int i = 0;
        while (i < selectedBooks.size()) {
            if (selectedBooks.get(i).equals(book.get())) {
                if (cartDTO.getBookDTO().getUserSelectedQuantity() == 0) {
                    this.count -= selectedBooks.get(i).getQuantityInCart();
                    selectedBooks.remove(selectedBooks.get(i));
                } else if (cartDTO.getBookDTO().getUserSelectedQuantity() >= 1) {
                    this.count = selectedBooks.get(i).getQuantityInCart();
                    selectedBooks.get(i).setQuantityInCart(cartDTO.getBookDTO().getUserSelectedQuantity());
                    this.count = selectedBooks.get(i).getQuantityInCart() - this.count;
                }
                break;
            }
            i++;
        }

        return getCart(cartDTO, customer, selectedBooks);
    }

    @Override
    public Cart removeFromCart(CartDTO cartDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(cartDTO.getUserToken()));
        Optional<Book> book = iBookRepository.findById(cartDTO.getBookDTO().getBook().getId());
        List<Book> selectedBooks = cartDTO.getBooks();
        for (int i = 0; i < selectedBooks.size() ; i++) {
            if (selectedBooks.get(i).getBookName().equals(book.get().getBookName())) {
                this.count = selectedBooks.get(i).getQuantityInCart();
                selectedBooks.remove(selectedBooks.get(i));
                break;
            }
        }
        return getCart(cartDTO, customer, selectedBooks);
    }

    @NotNull
    private Cart getCart(CartDTO cartDTO, Optional<Customer> customer, List<Book> selectedBooks) {
        Cart cart = new Cart();
        cart.setNoOfItems(cartDTO.getNoOfItems() + this.count);
        cart.setBooks(selectedBooks);
        if (customer.isPresent()) {
            if (customer.get().getUserCart() == null){
                iCartRepository.save(cart);
            }
            customer.get().getUserCart().setNoOfItems(cart.getNoOfItems());
            customer.get().getUserCart().setBooks(cart.getBooks());
            iCustomerRepository.save(customer.get());
        }
        return cart;
    }

    @Override
    public void beforeServerClosing() {
        List<Book> booksTOSave = iBookRepository.findAll();
        for (int i = 0; i < booksTOSave.size(); i++) {
            booksTOSave.get(i).setInCart(false);
        }
        iBookRepository.saveAll(booksTOSave);
        Optional<BookInDisplay> inDisplay = inDisplayRepository.findById(1);
        inDisplay.get().setBook(null);
        inDisplayRepository.save(inDisplay.get());
    }

    @Override
    public Book bookInDisplay(Book book) {
        Book bookToDisplay;
        if(!inDisplayRepository.existsById(1)) {
            BookInDisplay inDisplay = new BookInDisplay();
            inDisplay.setId(1);
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
