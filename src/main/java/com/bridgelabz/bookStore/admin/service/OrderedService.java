package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.admin.dto.OrderDTO;
import com.bridgelabz.bookStore.admin.dto.ReturnOrder;
import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.CartItem;
import com.bridgelabz.bookStore.admin.model.OrderedBook;
import com.bridgelabz.bookStore.admin.model.Orders;
import com.bridgelabz.bookStore.admin.repository.IBookRepository;
import com.bridgelabz.bookStore.admin.repository.ICartRepository;
import com.bridgelabz.bookStore.admin.repository.IOrderedBookRepository;
import com.bridgelabz.bookStore.admin.repository.IOrdersRepository;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.repository.IAddressRepository;
import com.bridgelabz.bookStore.customer.repository.ICustomerRepository;
import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.util.*;

@Service
public class    OrderedService implements IOrderedService{

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
    private TemplateEngine templateEngine;

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
    public ReturnOrder checkOut(String userToken, OrderDTO orderDTO) throws MessagingException {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        ReturnOrder returnOrder = new ReturnOrder();
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
            fmt.format("%tB %td", cal, cal);
            orderedBook.setOrderedDate(fmt+"");
            orderedBook.setDeliveryAddress(iAddressRepository.save(orderDTO.getDeliveryAddress()));
            orderedBook.setBook(orderDTO.getBook());
            OrderedBook savedOrderedBook = iOrderedBookRepository.save(orderedBook);
            returnOrder.setOrderedBook(savedOrderedBook);
            bookList.add(savedOrderedBook);
            customer.get().getMyOrders().setOrderedBook(bookList);
            iCustomerRepository.save(customer.get());
            mail.sendSimpleMessage(customer.get().getEmail(),"Book Store Order Status", this.messageBody(orderDTO, savedOrderedBook, customer));
            returnOrder.setCart(this.removeOrderedFromCart(customer.get(), orderDTO));
            return returnOrder;
        }
        throw new BookStoreException("Login to checkout your orders");
    }

    private Cart removeOrderedFromCart(Customer customer, OrderDTO book) {
        List<CartItem> items = new ArrayList<>();
        List<CartItem> books = customer.getUserCart().getCartItems();
        for (int i = 0; i < books.size(); i++ ) {
            if (books.get(i).getBook().getId().equals(book.getBook().getId())) {
                book.getBook().setQuantityInStock(
                        book.getBook().getQuantityInStock() - book.getNoOfItemsOrdered());
                iBookRepository.save(book.getBook());
            } else {
                items.add(books.get(i));
            }
        }
        customer.getUserCart().setCartItems(items);
        return iCustomerRepository.save(customer).getUserCart();
    }

    @Override
    public String orderDelivery(String userToken, BookDTO bookDTO) throws MessagingException {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        List<OrderedBook> bookList = customer.get().getMyOrders().getOrderedBook();
        for (int i = 0; i < bookList.size(); i++) {
            OrderedBook selectedBook = bookList.get(i);
            if (selectedBook.getBook().equals(bookDTO.getBook())) {
                Formatter fmt = new Formatter();
                Calendar cal = Calendar.getInstance();
                fmt.format("%tB %tm", cal, cal);
                selectedBook.setDeliveredDate(fmt+"");
            }
            iOrderedBookRepository.save(selectedBook);
            mail.sendSimpleMessage(customer.get().getEmail(), "Book Store Delivery Status",
                    "Order was successfully delivered on "+ selectedBook.getDeliveredDate()+".");
            return "Order was successfully delivered";
        }
        throw new BookStoreException("Book will not be able to delivered due to some service problem payment" +
                " will be returned in 2 to 3 working days");
    }

    public String messageBody(OrderDTO orderDTO, OrderedBook savedOrderedBook, Optional<Customer> customer) {
        Context context = new Context();
        context.setVariable("user", customer.get());
        context.setVariable("order", savedOrderedBook);
        context.setVariable("orderDetail", orderDTO.getBook());
        context.setVariable("id", String.format("%04d", savedOrderedBook.getId()));
        return templateEngine.process("SuccessOrderEmail", context);
    }

}
