package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.repository.IAddressRepository;
import com.bridgelabz.bookStore.customer.repository.ICustomerRepository;
import com.bridgelabz.bookStore.customer.repository.ISelectedRepository;
import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.admin.dto.OrderDTO;
import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.admin.model.Orders;
import com.bridgelabz.bookStore.admin.model.SelectedBook;
import com.bridgelabz.bookStore.admin.repository.IBookRepository;
import com.bridgelabz.bookStore.admin.repository.ICartRepository;
import com.bridgelabz.bookStore.admin.repository.IOrdersRepository;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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
    private ISelectedRepository iSelectedRepository;

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
    public String checkOut(String userToken, OrderDTO orderDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()) {
            if (customer.get().getMyOrders() == null) {
                customer.get().setMyOrders(new Orders());
                iOrdersRepository.save(customer.get().getMyOrders());
                iCustomerRepository.save(customer.get());
            }
            List<SelectedBook> bookList = customer.get().getMyOrders().getBookList();
            for (int i = 0; i < orderDTO.getBookList().size() ; i++) {
                SelectedBook selectedBook = new SelectedBook();
                selectedBook.setBook(orderDTO.getBookList().get(i).getBook());
                selectedBook.setQuantityForOrder(orderDTO.getBookList().get(i).getQuantityForOrder());
                selectedBook.setOrderedDate(Calendar.getInstance().getTime());
                selectedBook.setQuantityInCart(selectedBook
                        .getQuantityInCart() - selectedBook.getQuantityForOrder());
                SelectedBook savedSelectedBook = iSelectedRepository.save(selectedBook);
                mail.sendSimpleMessage(customer.get().getEmail(), "Book Store Order Status",
                        "Order was successfully Placed on "+ selectedBook.getDeliveredDate()+"");
                bookList.add(savedSelectedBook);
            }
            customer.get().getMyOrders().setBookList(bookList);
            return "Order was successfully Placed";
        }
        throw new BookStoreException("Login to checkout your orders");
    }

    @Override
    public String orderDelivery(String userToken, BookDTO bookDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        List<SelectedBook> bookList = customer.get().getMyOrders().getBookList();
        for (int i = 0; i < bookList.size(); i++) {
            SelectedBook selectedBook = bookList.get(i);
            if (selectedBook.getBook().equals(bookDTO.getBook())) {
                selectedBook.setDeliveredDate(Calendar.getInstance().getTime());
            }
            iSelectedRepository.save(selectedBook);
            mail.sendSimpleMessage(customer.get().getEmail(), "Book Store Delivery Status",
                    "Order was successfully delivered on "+ selectedBook.getDeliveredDate()+"");
            return "Order was successfully delivered";
        }
        throw new BookStoreException("Book will not be able to delivered due to some service problem payment" +
                " will be returned in 2 to 3 working days");
    }

}
