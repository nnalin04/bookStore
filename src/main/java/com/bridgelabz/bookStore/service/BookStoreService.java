package com.bridgelabz.bookStore.service;

import com.bridgelabz.bookStore.dto.ResetPassword;
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

    @Autowired
    MailService mail;

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
}
