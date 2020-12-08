package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.Store;
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
    MailService mail;

    int count = 0;

    private boolean isPresent = false;

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
    public Cart addToCart(CartDTO cartDTO) {
        List<SelectedBook> selectedBooks = cartDTO.getSelectedBooks();
        Optional<Book> book = iBookRepository.findById(cartDTO.getBookDTO().getBook().getId());
        this.isPresent = false;
        for (int i = 0; i <  selectedBooks.size(); i++) {
            if (selectedBooks.get(i).getBook().getId().equals(book.get().getId())) {
                isPresent = true;
                selectedBooks.get(i).getBook().setInCart(true);
                selectedBooks.get(i).setQuantityInCart(selectedBooks.get(i).getQuantityInCart() + 1);
            }
        }

        if (!isPresent) {
            SelectedBook selectedBook = new SelectedBook();
            selectedBook.setBook(book.get());
            selectedBook.setQuantityInCart(cartDTO.getBookDTO().getUserSelectedQuantity());
            selectedBooks.add(selectedBook);
        }
        Cart cart = new Cart();
        cart.setNoOfItems(cartDTO.getNoOfItems() + cartDTO.getBookDTO().getUserSelectedQuantity());
        cart.setSelectedBooks(selectedBooks);
        if (cartDTO.getUserToken().length() > 0) {
            Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(cartDTO.getUserToken()));
            if (customer.isPresent()) {
                iSelectedRepository.saveAll(selectedBooks);
                customer.get().setUserCart(iCartRepository.save(cart));
                iSelectedRepository.saveAll(selectedBooks);
                iCustomerRepository.save(customer.get());
                cart = customer.get().getUserCart();
            }
        }

        return cart;
    }

    @Override
    public Cart editCart(CartDTO cartDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(cartDTO.getUserToken()));
        List<SelectedBook> selectedBooks = cartDTO.getSelectedBooks();
        Optional<Book> book = iBookRepository.findById(cartDTO.getBookDTO().getBook().getId());
        int i = 0;
        while (i < selectedBooks.size()) {
            if (selectedBooks.get(i).getBook().equals(book.get())) {
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
        List<SelectedBook> selectedBooks = cartDTO.getSelectedBooks();
        for (int i = 0; i < selectedBooks.size() ; i++) {
            if (selectedBooks.get(i).getBook().equals(book.get())) {
                this.count = selectedBooks.get(i).getQuantityInCart();
                selectedBooks.remove(selectedBooks.get(i));
                break;
            }
        }
        return getCart(cartDTO, customer, selectedBooks);
    }

    @NotNull
    private Cart getCart(CartDTO cartDTO, Optional<Customer> customer, List<SelectedBook> selectedBooks) {
        Cart cart = new Cart();
        cart.setNoOfItems(cartDTO.getNoOfItems() + this.count);
        cart.setSelectedBooks(selectedBooks);
        if (customer.isPresent()) {
            customer.get().getUserCart().setNoOfItems(cart.getNoOfItems());
            customer.get().getUserCart().setSelectedBooks(cart.getSelectedBooks());
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
    }
}
