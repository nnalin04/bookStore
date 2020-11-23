package com.bridgelabz.bookStore.service;

import com.bridgelabz.bookStore.dto.UserDTO;
import com.bridgelabz.bookStore.modle.Customer;
import com.bridgelabz.bookStore.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
