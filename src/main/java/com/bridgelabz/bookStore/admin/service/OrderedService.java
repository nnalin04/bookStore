package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.model.*;
import com.bridgelabz.bookStore.admin.repository.IOrderedBookRepository;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.repository.IAddressRepository;
import com.bridgelabz.bookStore.customer.repository.ICustomerRepository;
import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.admin.dto.OrderDTO;
import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.admin.repository.IBookRepository;
import com.bridgelabz.bookStore.admin.repository.ICartRepository;
import com.bridgelabz.bookStore.admin.repository.IOrdersRepository;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.hibernate.boot.model.source.spi.Orderable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderedService implements IOrderedService{

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
    private IOrdersRepository iOrdersRepository;

    @Autowired
    MailService mail;

    @Override
    public Orders getMyOrders(String userToken) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()) {
            return customer.get().getMyOrders();
        }
        throw new BookStoreException("Login to view your orders");
    }

    @Override
    public Cart checkOut(String userToken, OrderDTO orderDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()) {
            if (customer.get().getMyOrders() == null) {
                customer.get().setMyOrders(new Orders());
                iOrdersRepository.save(customer.get().getMyOrders());
                iCustomerRepository.save(customer.get());
            }
            List<OrderedBook> bookList = customer.get().getMyOrders().getOrderedBook();
            OrderedBook orderedBook = new OrderedBook();
            orderedBook.setQuantityForOrder(orderDTO.getNoOfItemsOrdered());
            Formatter fmt = new Formatter();
            Calendar cal = Calendar.getInstance();
            fmt = new Formatter();
            fmt.format("%tB %tm", cal, cal);
            orderedBook.setOrderedDate(fmt+"");
            orderedBook.setDeliveryAddress(iAddressRepository.save(orderDTO.getDeliveryAddress()));
            orderedBook.setBook(orderDTO.getBook());
            iOrderedBookRepository.save(orderedBook);
            bookList.add(orderedBook);
            customer.get().getMyOrders().setOrderedBook(bookList);
            iCustomerRepository.save(customer.get());
            mail.sendSimpleMessage(customer.get().getEmail(), "Book Store Order Status",
                    "Order was successfully Placed on "+ orderedBook.getDeliveredDate()+"");
            return this.removeOrderedFromCart(customer.get(), orderDTO.getBook());
        }
        throw new BookStoreException("Login to checkout your orders");
    }

    private Cart removeOrderedFromCart(Customer customer, Book book) {
        List<CartItem> books = customer.getUserCart().getCartItems();
        for (int i = 0; i < books.size(); i++ ) {
            if (books.get(i).getBook().equals(book)) {
                books.remove(books.get(i));
            }
        }
        customer.getUserCart().setCartItems(books);
        return iCustomerRepository.save(customer).getUserCart();
    }

    @Override
    public String orderDelivery(String userToken, BookDTO bookDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        List<OrderedBook> bookList = customer.get().getMyOrders().getOrderedBook();
        for (int i = 0; i < bookList.size(); i++) {
            OrderedBook selectedBook = bookList.get(i);
            if (selectedBook.equals(bookDTO.getBook())) {
                Formatter fmt = new Formatter();
                Calendar cal = Calendar.getInstance();
                fmt = new Formatter();
                fmt.format("%tB %tm", cal, cal);
                selectedBook.setDeliveredDate(fmt+"");
            }
            iOrderedBookRepository.save(selectedBook);
            mail.sendSimpleMessage(customer.get().getEmail(), "Book Store Delivery Status",
                    "Order was successfully delivered on "+ selectedBook.getDeliveredDate()+"");
            return "Order was successfully delivered";
        }
        throw new BookStoreException("Book will not be able to delivered due to some service problem payment" +
                " will be returned in 2 to 3 working days");
    }

}
