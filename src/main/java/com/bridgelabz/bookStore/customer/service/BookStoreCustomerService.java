package com.bridgelabz.bookStore.customer.service;

import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.customer.modle.*;
import com.bridgelabz.bookStore.customer.repository.*;
import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.admin.repository.IBookRepository;
import com.bridgelabz.bookStore.admin.repository.ICartRepository;
import com.bridgelabz.bookStore.utility.MailService;
import com.bridgelabz.bookStore.utility.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookStoreCustomerService implements IBookStoreCustomerService {

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

    @Override
    public String registerUser(UserDTO userDTO) {
        if (iCustomerRepository.findByEmail(userDTO.getEmail()).isPresent())
            throw new BookStoreException("Account with this email is already present");
        Customer customer = new Customer();
        customer.setFullName(userDTO.getFullName());
        customer.setEmail(userDTO.getEmail());
        customer.setMobileNo(userDTO.getMobileNo());
        customer.setPassword(userDTO.getPassword());
        customer.setVerified(false);
        Customer user = iCustomerRepository.save(customer);
        mail.sendCustomerMailWithTokenURL(userDTO.getEmail(), Token.getToken(user.getId()));
        return "Successfully Registered";
    }

    @Override
    public String verifyUser(String token) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(token));
        if (customer.isPresent()){
            customer.get().setVerified(true);
            iCustomerRepository.save(customer.get());
            return "Your emailId is verified please click on the following given link \n" +
                    "<link>http://localhost:3000/login</link>";
        }
        throw new BookStoreException("Please Please re-register for verification");
    }

    @Override
    public CustomerDTO loginUser(UserDTO userDTO) {
        Optional<Customer> customer = iCustomerRepository.findByEmail(userDTO.getEmail());
        if (customer.isPresent()){
            if (customer.get().getVerified()){
                if (customer.get().getPassword().equals(userDTO.getPassword())){
                    CustomerDTO customerDTO = new CustomerDTO();
                    customerDTO.setToken(Token.getToken(customer.get().getId()));
                    customerDTO.setFullName(customer.get().getFullName());
                    customerDTO.setEmail(customer.get().getEmail());
                    customerDTO.setMobileNo(customer.get().getMobileNo());
                    customerDTO.setAddressDetail(customer.get().getAddressDetail());
                    customerDTO.setUserCart(customer.get().getUserCart());
                    customerDTO.setMyOrders(customer.get().getMyOrders());
                    return customerDTO;
                }
            }
            throw new BookStoreException("Please Verify your emailId at the provided Mail");
        }
        throw new BookStoreException("This email not registered");
    }

    @Override
    public String forgotPassword(UserDTO userDTO) {
        Optional<Customer> customer = iCustomerRepository.findByEmail(userDTO.getEmail());
        mail.sendResetMessage(userDTO.getEmail(), Token.getToken(customer.get().getId()));
        return "Check your email for reset password link";
    }

    @Override
    public String resetPassword(ResetPassword resetPassword, String token) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(token));
        if (resetPassword.getConformPassword().equals(resetPassword.getNewPassword())){
            if (customer.isPresent()){
                customer.get().setPassword(resetPassword.getNewPassword());
                iCustomerRepository.save(customer.get());
                return "The password has been successfully reset please login again";
            }
            throw new BookStoreException("Please follow the reset method again this link haas expired");
        }
        throw new BookStoreException("the new and confirm password are not matching");
    }

    @Override
    public Customer editUser(String userToken, UserDTO userDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()){
            customer.get().setFullName(userDTO.getFullName());
            customer.get().setEmail(userDTO.getEmail());
            customer.get().setMobileNo(userDTO.getMobileNo());
            customer.get().setPassword(userDTO.getPassword());
            return iCustomerRepository.save(customer.get());
        }
        throw new BookStoreException("Login session time out");
    }



    @Override
    public Customer addAddress(String userToken, AddressDTO addressDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()) {
            List<AddressDetail> addressDetails = customer.get().getAddressDetail();
            AddressDetail addressDetail  = new AddressDetail();
            addressDetail.setAddress(addressDTO.getAddress());
            addressDetail.setCity(addressDTO.getCity());
            addressDetail.setLandmark(addressDTO.getLandmark());
            addressDetail.setLocality(addressDTO.getLocality());
            addressDetail.setPinCode(addressDTO.getPinCode());
            addressDetail.setType(addressDTO.getType());
            AddressDetail address = iAddressRepository.save(addressDetail);
            addressDetails.add(address);
            customer.get().setAddressDetail(addressDetails);
            return iCustomerRepository.save(customer.get());
        }
        throw new BookStoreException("Login session time out");
    }

    @Override
    public Customer editAddress(String userToken, AddressDTO addressDTO) {
        Optional<Customer> customer = iCustomerRepository.findById(Token.decodeJWT(userToken));
        if (customer.isPresent()) {
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
            return iCustomerRepository.save(customer.get());
        }
        throw new BookStoreException("Login session time out");
    }
}