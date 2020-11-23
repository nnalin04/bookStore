package com.bridgelabz.bookStore.service;

import com.bridgelabz.bookStore.dto.UserDTO;
import com.bridgelabz.bookStore.modle.Customer;
import com.bridgelabz.bookStore.repository.ICustomerRepository;
import com.bridgelabz.bookStore.utility.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookStoreService implements IBookStoreService{

    @Autowired
    ICustomerRepository customerRepository;

    @Override
    public String registerUser(UserDTO userDTO) {
        if (customerRepository.findByEmail(userDTO.getEmail()).isPresent())
            return "Account with this email is already present";
        Customer customer = new Customer();
        customer.setFullName(userDTO.getFullName());
        customer.setEmail(userDTO.getEmail());
        customer.setMobileNo(userDTO.getMobileNo());
        customer.setPassword(userDTO.getPassword());
        customerRepository.save(customer);
        return "Successfully Registered";
    }

    @Override
    public String verifyUser(String token) {
        Optional<Customer> customer = customerRepository.findById(Token.decodeJWT(token));
        customer.ifPresent(value -> value.setVerified(true));
        customerRepository.save(customer.get());
        return "Your emailId is verified";
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
}
