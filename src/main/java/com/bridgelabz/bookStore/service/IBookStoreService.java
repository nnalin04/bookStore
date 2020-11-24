package com.bridgelabz.bookStore.service;

import com.bridgelabz.bookStore.dto.ResetPassword;
import com.bridgelabz.bookStore.dto.StoreDTO;
import com.bridgelabz.bookStore.dto.UserDTO;

public interface IBookStoreService {
    String registerUser(UserDTO userDTO);
    String loginUser(UserDTO userDTO);
    String verifyUser(String token);
    String forgotPassword(UserDTO userDTO);
    String resetPassword(ResetPassword resetPassword, String token);
    StoreDTO getStore();
}
