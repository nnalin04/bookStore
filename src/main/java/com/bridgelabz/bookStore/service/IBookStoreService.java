package com.bridgelabz.bookStore.service;

import com.bridgelabz.bookStore.dto.UserDTO;

public interface IBookStoreService {
    String registerUser(UserDTO userDTO);
    String loginUser(UserDTO userDTO);
    String verifyUser(String token);
}
