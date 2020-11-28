package com.bridgelabz.bookStore.customer.service;

import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.model.Cart;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.model.Book;

public interface IBookStoreCustomerService {
    String registerUser(UserDTO userDTO);
    String loginUser(UserDTO userDTO);
    String verifyUser(String token);
    String forgotPassword(UserDTO userDTO);
    String resetPassword(ResetPassword resetPassword, String token);
    Customer addAddress(String userToken, AddressDTO addressDTO);
    Customer editUser(String userToken, UserDTO userDTO);
    Customer editAddress(String userToken, AddressDTO addressDTO);
}
