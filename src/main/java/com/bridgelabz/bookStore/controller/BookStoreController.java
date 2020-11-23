package com.bridgelabz.bookStore.controller;

import com.bridgelabz.bookStore.dto.UserDTO;
import com.bridgelabz.bookStore.service.IBookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/bookstore")
public class BookStoreController {

    @Autowired
    IBookStoreService service;

    @PostMapping
    public ResponseEntity registerUser(@RequestBody UserDTO userDTO) {
        String message = service.registerUser(userDTO);
        return new ResponseEntity(message, HttpStatus.OK);
    }
}
