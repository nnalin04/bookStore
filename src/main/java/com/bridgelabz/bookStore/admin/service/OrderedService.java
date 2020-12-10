package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.model.Book;
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
            List<Book> bookList = customer.get().getMyOrders().getBookList();
            for (int i = 0; i < orderDTO.getBookList().size() ; i++) {
                SelectedBook selectedBook = new SelectedBook();
                selectedBook.setQuantityForOrder(orderDTO.getBookList().get(i)
                                                .getSelectedBook().getQuantityForOrder());
                selectedBook.setOrderedDate(Calendar.getInstance().getTime());
                iSelectedRepository.save(selectedBook);
                mail.sendSimpleMessage(customer.get().getEmail(), "Book Store Order Status",
                        "Order was successfully Placed on "+ selectedBook.getDeliveredDate()+"");
                this.removeOrderedFromCart(customer.get(), orderDTO.getBookList().get(i));
            }
            customer.get().getMyOrders().setBookList(bookList);
            return "Order was successfully Placed";
        }
        throw new BookStoreException("Login to checkout your orders");
    }

    private void removeOrderedFromCart(Customer customer, Book book) {
        List<Book> books = customer.getUserCart().getBooks();
        for (int i = 0; i < books.size(); i++ ) {
            if (books.get(i).getBookName().equals(book.getBookName())) {
                books.get(i).setQuantityInCart(books.get(i).getQuantityInCart() -
                        book.getSelectedBook().getQuantityForOrder());
            }
            if (books.get(i).getQuantityInCart() == 0) {
                books.remove(books.get(i));
            }
        }
    }

    @Override
    public String orderDelivery(String userToken, BookDTO bookDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        List<Book> bookList = customer.get().getMyOrders().getBookList();
        for (int i = 0; i < bookList.size(); i++) {
            Book selectedBook = bookList.get(i);
            if (selectedBook.equals(bookDTO.getBook())) {
                selectedBook.getSelectedBook().setDeliveredDate(Calendar.getInstance().getTime());
            }
            iSelectedRepository.save(selectedBook.getSelectedBook());
            mail.sendSimpleMessage(customer.get().getEmail(), "Book Store Delivery Status",
                    "Order was successfully delivered on "+ selectedBook
                                            .getSelectedBook().getDeliveredDate()+"");
            return "Order was successfully delivered";
        }
        throw new BookStoreException("Book will not be able to delivered due to some service problem payment" +
                " will be returned in 2 to 3 working days");
    }

}
