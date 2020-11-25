package com.bridgelabz.bookStore.customer.service;

import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.customer.modle.*;
import com.bridgelabz.bookStore.customer.repository.*;
import com.bridgelabz.bookStore.utility.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStoreService implements IBookStoreService{

    @Autowired
    ICustomerRepository customerRepository;

    @Autowired
    IStoreRepository storeRepository;

    @Autowired
    IBookRepository bookRepository;

    @Autowired
    IAddressRepository addressRepository;

    @Autowired
    ISelectedRepository selectedRepository;

    @Autowired
    MailService mail;

    int count = 0;
    boolean inCart = false;

    @Override
    public String registerUser(UserDTO userDTO) {
        if (customerRepository.findByEmail(userDTO.getEmail()).isPresent())
            return "Account with this email is already present";
        Customer customer = new Customer();
        customer.setFullName(userDTO.getFullName());
        customer.setEmail(userDTO.getEmail());
        customer.setMobileNo(userDTO.getMobileNo());
        customer.setPassword(userDTO.getPassword());
        Customer user = customerRepository.save(customer);
        mail.sendMailWithTokenURL(userDTO.getEmail(), Token.getToken(user.getId()));
        return "Successfully Registered";
    }

    @Override
    public String verifyUser(String token) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(token));
        if (customer.isPresent()){
            customer.get().setVerified(true);
            customerRepository.save(customer.get());
            return "Your emailId is verified";
        }
        return "Please Please re-register for verification";
    }

    @Override
    public String loginUser(UserDTO userDTO) {
        Optional<Customer> customer = customerRepository.findByEmail(userDTO.getEmail());
        if (customer.isPresent()){
            if (customer.get().getVerified()){
                if (customer.get().getPassword().equals(userDTO.getPassword())){
                    return Token.getToken(customer.get().getId());
                }
            }
            return "Please Verify your emailId through your provided Mail";
        }
        return "This email not registered";
    }

    @Override
    public String forgotPassword(UserDTO userDTO) {
        Optional<Customer> customer = customerRepository.findByEmail(userDTO.getEmail());
        mail.sendSimpleMessage(userDTO, Token.getToken(customer.get().getId()));
        return "Check your email for reset password link";
    }

    @Override
    public String resetPassword(ResetPassword resetPassword, String token) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(token));
        if (resetPassword.getConformPassword().equals(resetPassword.getNewPassword())){
            if (customer.isPresent()){
                customer.get().setPassword(resetPassword.getNewPassword());
                customerRepository.save(customer.get());
                return "The password has been successfully reset";
            }
            return "Please follow the reset method again this link haas expired";
        }
        return "the new and confirm password are not matching";
    }

    @Override
    public Customer editUser(String userToken, UserDTO userDTO) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(userToken));
        customer.get().setFullName(userDTO.getFullName());
        customer.get().setEmail(userDTO.getEmail());
        customer.get().setMobileNo(userDTO.getMobileNo());
        customer.get().setPassword(userDTO.getPassword());
        return customerRepository.save(customer.get());
    }

    @Override
    public StoreDTO getStore() {
        Optional<Store> store = storeRepository.findById(1);
        List<Book> books = store.get().getBooks();
        StoreDTO storeDTO = new StoreDTO();
        List<BookDTO> booksList = storeDTO.getBooks();
        books.forEach(book -> {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setToken(Token.getToken(book.getId()));
            bookDTO.setBook(book);
            booksList.add(bookDTO);
        });
        storeDTO.setBooks(booksList);
        return storeDTO;
    }

    @Override
    public Integer addToCart(BookDTO bookDTO, String token) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(token));
        List<SelectedBook> selectedBooks = customer.get().getUserCart().getSelectedBooks();
        Optional<Book> book = bookRepository.findById(Token.decodeJWT(bookDTO.getToken()));
        SelectedBook selectedBook = new SelectedBook();
        selectedBook.setBook(book.get());
        selectedBook.setQuantity(1);
        SelectedBook selected = selectedRepository.save(selectedBook);
        selectedBooks.add(selected);
        customer.get().getUserCart().setNoOfItems(customer.get().getUserCart().getNoOfItems() + 1);
        customer.get().getUserCart().setSelectedBooks(selectedBooks);
        Customer user = customerRepository.save(customer.get());
        return user.getUserCart().getSelectedBooks().size();
    }

    @Override
    public Cart editCart(String userToken, BookDTO bookDTO) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(userToken));
        List<SelectedBook> selectedBooks = customer.get().getUserCart().getSelectedBooks();
        Optional<Book> book = bookRepository.findById(Token.decodeJWT(bookDTO.getToken()));
        int i = 0;
        while (i < selectedBooks.size()) {
            if (selectedBooks.get(i).getBook().equals(book.get())) {
                if (bookDTO.getQuantity() == 0) {
                    this.count -= selectedBooks.get(i).getQuantity();
                    selectedBooks.remove(selectedBooks.get(i));
                } else if (bookDTO.getQuantity() >= 1) {
                    this.count = selectedBooks.get(i).getQuantity();
                    selectedBooks.get(i).setQuantity(bookDTO.getQuantity());
                    this.count = selectedBooks.get(i).getQuantity() - this.count;
                }
                break;
            }
            i++;
        }
        customer.get().getUserCart().setNoOfItems(customer.get().getUserCart().getNoOfItems() + this.count);
        customer.get().getUserCart().setSelectedBooks(selectedBooks);
        Customer user = customerRepository.save(customer.get());
        return user.getUserCart();
    }

    @Override
    public Cart removeFromCart(String userToken, String bookToken) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(userToken));
        Optional<Book> book = bookRepository.findById(Token.decodeJWT(bookToken));
        List<SelectedBook> selectedBooks = customer.get().getUserCart().getSelectedBooks();
        for (int i = 0; i < selectedBooks.size() ; i++) {
            if (selectedBooks.get(i).getBook().equals(book.get())) {
                this.count = selectedBooks.get(i).getQuantity();
                selectedBooks.remove(selectedBooks.get(i));
                break;
            }
        }
        customer.get().getUserCart().setNoOfItems(customer.get().getUserCart().getNoOfItems() - this.count);
        customer.get().getUserCart().setSelectedBooks(selectedBooks);
        Customer user = customerRepository.save(customer.get());
        return user.getUserCart();
    }

    @Override
    public Customer addAddress(String userToken, AddressDTO addressDTO) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(userToken));
        List<AddressDetail> addressDetails = customer.get().getAddressDetail();
        AddressDetail addressDetail  = new AddressDetail();
        addressDetail.setAddress(addressDTO.getAddress());
        addressDetail.setCity(addressDTO.getCity());
        addressDetail.setLandmark(addressDTO.getLandmark());
        addressDetail.setLocality(addressDTO.getLocality());
        addressDetail.setPinCode(addressDTO.getPinCode());
        addressDetail.setType(addressDTO.getType());
        AddressDetail address = addressRepository.save(addressDetail);
        addressDetails.add(address);
        customer.get().setAddressDetail(addressDetails);
        return customerRepository.save(customer.get());
    }

    @Override
    public Customer editAddress(String userToken, AddressDTO addressDTO) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(userToken));
        List<AddressDetail> addressDetails = customer.get().getAddressDetail();
        addressDetails.stream()
                .filter(addressDetail -> addressDetail.getId().equals(addressDTO.getId()))
                .forEach(addressDetail -> {
                                            addressDetail.setAddress(addressDTO.getAddress());
                                            addressDetail.setCity(addressDTO.getCity());
                                            addressDetail.setLandmark(addressDTO.getLandmark());
                                            addressDetail.setLocality(addressDTO.getLocality());
                                            addressDetail.setPinCode(addressDTO.getPinCode());
                                            addressDetail.setType(addressDTO.getType());
                                        });
        customer.get().setAddressDetail(addressDetails);
        return customerRepository.save(customer.get());
    }
}