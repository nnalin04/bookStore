package com.bridgelabz.bookStore.seller.service;

import com.bridgelabz.bookStore.customer.dto.AddressDTO;
import com.bridgelabz.bookStore.customer.dto.ResetPassword;
import com.bridgelabz.bookStore.customer.dto.UserDTO;
import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.seller.model.Seller;

public interface IBookStoreSeller {
    String registerUser(UserDTO userDTO);

    String verifyUser(String userToken);

    String loginUser(UserDTO userDTO);

    String forgotPassword(UserDTO userDTO);

    String resetPassword(ResetPassword resetPassword, String userToken);

    Seller editUser(String userToken, UserDTO userDTO);

    Seller addAddress(String userToken, AddressDTO addressDTO);

    Seller editAddress(String userToken, AddressDTO addressDTO);

    String sendBookTOAdmin(String userToken, BookDTO bookDTO);

    String rejectedBook(String userToken, BookDTO bookDTO);

    String acceptedBook(String userToken, BookDTO bookDTO);
}
