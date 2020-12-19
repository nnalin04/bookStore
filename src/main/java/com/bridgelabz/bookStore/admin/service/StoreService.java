package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.dto.Store;
import com.bridgelabz.bookStore.admin.dto.BookListDTO;
import com.bridgelabz.bookStore.admin.dto.WishListDTO;
import com.bridgelabz.bookStore.admin.model.*;
import com.bridgelabz.bookStore.admin.repository.*;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.repository.IAddressRepository;
import com.bridgelabz.bookStore.customer.repository.ICustomerRepository;
import com.bridgelabz.bookStore.utility.CSVReader;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    IWishListRepository iWishListRepository;

//    @Autowired
//    IElasticBookRepository iElasticBookRepository;

    @Autowired
    ElasticSearchService eService;

    @Autowired
    MailService mail;

    private Boolean inCart = false;

    private List<Book> books;

    @Override
    public void createBookStore() {
        this.books = new CSVReader().loadCensusData();
        this.books.forEach(book -> {
            book.setQuantityInStock(2);
            book.setInCart(false);
            book.setInWishList(false);
            iBookRepository.save(book);
            try {
                eService.addNewBook(book);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public Store getBooks(BookListDTO bookListDTO) {
        if ( bookListDTO.getSearchQuarry() != null && bookListDTO.getSearchQuarry().length() > 0){
            this.books = this.findBooksBySearch(bookListDTO.getSearchQuarry());
        } else {
            this.books = this.iBookRepository.findAll();
        }
        List<List<Book>> bookList = new ArrayList<>();
        List<Book> books = this.sortBookStore(bookListDTO.getSortingMethod());
        List<Book> innerBook = new ArrayList<>();
        int count = 0;
        for (Book book : books) {
            if (count == 8) {
                bookList.add(innerBook);
                innerBook = new ArrayList<>();
                count = 0;
            }
            count++;
            innerBook.add(book);
        }
        if (count > 0) {
            bookList.add(innerBook);
        }
        Store store = new Store();
        store.setTotalBooks(books.size());
        store.setNoOfPage(bookList.size());;
        store.setBooks(bookList.get(bookListDTO.getPageNo() - 1));
        return store;
    }

    private List<Book> sortBookStore(String sortingData) {
        Comparator<Book> comparator;
        if (sortingData == null) sortingData = "";

        switch (sortingData) {
            case "HighToLow" :
                comparator = Comparator.comparing(Book::getPrice).reversed();
                break;
            case "LowToHigh" :
                comparator = Comparator.comparing(Book::getPrice);
                break;
            case "NewestArrivals" :
                comparator = Comparator.comparing(Book::getId).reversed();
                break;
            default:
                comparator = Comparator.comparing(Book::getId);
                break;
        }

        return this.books.stream()
                .sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Book> findBooksBySearch(String name) {
        List<Book> books = eService.searchData(name);
        return books;
    }

    @Override
    public List<Book> addBookToWishList(WishListDTO wishListDTO) {
        List<Book> dtoBooks;
        if (wishListDTO.getBooks() != null)
            dtoBooks = wishListDTO.getBooks();
        else dtoBooks = new ArrayList<>();
        Optional<Book> book = iBookRepository.findById(wishListDTO.getBook().getId());
        book.get().setInWishList(true);
        if (wishListDTO.getToken() != null) {

            Optional<Customer> customer = iCustomerRepository.findById(Token
                    .decodeJWT(wishListDTO.getToken()));

            if (customer.isPresent()) {

                if (customer.get().getMyWishList().getBooks().size() == 0) {                    ;
                    customer.get().getMyWishList().getBooks().add(book.get());
                }

                List<Book> books = customer.get().getMyWishList().getBooks();
                books.stream().map(currentBook -> {
                    if (currentBook.getId().equals(book.get().getId()) ) {
                        books.remove(currentBook);
                    }
                    return currentBook;
                });
                books.add(book.get());
                customer.get().getMyWishList().setBooks(books);
                dtoBooks = iCustomerRepository.save(customer.get()).getMyWishList().getBooks();
            }
        } else {
            dtoBooks.add(book.get());
        }
        return dtoBooks;
    }

    @Override
    public List<Book> deleteBookToWishList(WishListDTO wishListDTO) {
        boolean inList = false;
        Book bookToAdd = null;
        List<Book> books = wishListDTO.getBooks();
        books.remove(wishListDTO.getBook());

        iBookRepository.save(wishListDTO.getBook());
        if (wishListDTO.getToken() != null && wishListDTO.getToken().length() > 0) {
            Optional<Customer> customer = iCustomerRepository.findById(Token
                    .decodeJWT(wishListDTO.getToken()));
            List<Book> booksList = customer.get().getMyWishList().getBooks();
            for (Book book : booksList) {
                inList = false;
                for (Book dtoBook : books) {
                    bookToAdd = dtoBook;
                    if (book.getId().equals(dtoBook.getId()))
                        inList = true;
                }
                if (!inList) {
                    booksList.add(bookToAdd);
                }
                if (book.getId().equals(wishListDTO.getBook().getId())){
                    booksList.remove(book);
                }
            }
            customer.get().getMyWishList().setBooks(booksList);
            books = iCustomerRepository.save(customer.get()).getMyWishList().getBooks();

        }
        return books;
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
                    if (currentBook.getBook().getId().equals(book.get().getId()) ) {
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
            if (selectedBooks.get(i).getBook().getId().equals(book.getId())
                    && cartDTO.getBookDTO().getUserSelectedQuantity() <= book.getQuantityInStock()) {
                selectedBooks.get(i).getBook().setQuantityInStock
                        (selectedBooks.get(i).getBook().getQuantityInStock()
                      - cartDTO.getBookDTO().getUserSelectedQuantity());
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
        if (!inDisplayRepository.existsById(1)) {
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
