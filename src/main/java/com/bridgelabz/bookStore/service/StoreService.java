package com.bridgelabz.bookStore.service;

import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.model.SelectedBook;
import com.bridgelabz.bookStore.customer.repository.*;
import com.bridgelabz.bookStore.dto.CartDTO;
import com.bridgelabz.bookStore.model.Book;
import com.bridgelabz.bookStore.model.Cart;
import com.bridgelabz.bookStore.repository.IBookRepository;
import com.bridgelabz.bookStore.repository.ICartRepository;
import com.bridgelabz.bookStore.utility.CSVReader;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private List<Book> books;

    @Override
    public void createBookStore() {
        this.books = new CSVReader().loadCensusData();
        this.books.forEach(book -> iBookRepository.save(book));
    }

    @Override
    public List<Book> getBooks() {
        this.books = this.iBookRepository.findAll();
        return this.books;
    }

    @Override
    public Integer addToCart(CartDTO cartDTO, String token) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(token));
        List<SelectedBook> selectedBooks = cartDTO.getSelectedBooks();
        Optional<Book> book = iBookRepository.findById(cartDTO.getBookDTO().getBook().getId());

        SelectedBook selectedBook = new SelectedBook();
        selectedBook.setBook(book.get());
        selectedBook.setQuantityInCart(cartDTO.getBookDTO().getUserSelectedQuantity());
        SelectedBook selected = iSelectedRepository.save(selectedBook);
        selectedBooks.add(selected);

        Cart cart = new Cart();
        cart.setNoOfItems(cartDTO.getNoOfItems() + cartDTO.getBookDTO().getUserSelectedQuantity());
        cart.setSelectedBooks(selectedBooks);

        if (customer.isPresent()) {
            customer.get().setUserCart(iCartRepository.save(cart));
            iCustomerRepository.save(customer.get());
        }
        return cart.getNoOfItems();
    }

    @Override
    public Cart editCart(String userToken, CartDTO cartDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
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
    public Cart removeFromCart(String userToken, CartDTO cartDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
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
}
