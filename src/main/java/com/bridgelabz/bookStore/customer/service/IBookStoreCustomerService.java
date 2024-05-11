package com.bridgelabz.bookStore.customer.service;

import com.bridgelabz.bookStore.admin.model.CartItem;
import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.customer.modle.Customer;

import java.util.List;

public interface IBookStoreCustomerService {
    String registerUser(UserDTO userDTO);
    CustomerDTO loginUser(UserDTO userDTO);
    String verifyUser(String token);
    String forgotPassword(UserDTO userDTO);
    String resetPassword(ResetPassword resetPassword, String token);
    Customer addAddress(String userToken, AddressDTO addressDTO);
    Customer editUser(String userToken, UserDTO userDTO);
    Customer editAddress(String userToken, AddressDTO addressDTO);
    List<CartItem> getCustomerCart(String token);
}
