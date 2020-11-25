package com.bridgelabz.bookStore.customer.service;

import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.customer.modle.Cart;
import com.bridgelabz.bookStore.customer.modle.Customer;

public interface IBookStoreService {
    String registerUser(UserDTO userDTO);
    String loginUser(UserDTO userDTO);
    String verifyUser(String token);
    String forgotPassword(UserDTO userDTO);
    String resetPassword(ResetPassword resetPassword, String token);
    StoreDTO getStore();
    Integer addToCart(BookDTO bookDTO, String token);
    Cart removeFromCart(String userToken, String bookToken);
    Cart editCart(String userToken, BookDTO bookDTO);
    Customer addAddress(String userToken, AddressDTO addressDTO);
    Customer editUser(String userToken, UserDTO userDTO);
}
