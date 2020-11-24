package com.bridgelabz.bookStore.controller;

import com.bridgelabz.bookStore.dto.ResetPassword;
import com.bridgelabz.bookStore.dto.StoreDTO;
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

    @PutMapping("/{token}")
    public ResponseEntity VerifyUser(@PathVariable String token) {
        String message = service.verifyUser(token);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity loginUser(@RequestBody UserDTO userDTO) {
        String token = service.loginUser(userDTO);
        return new ResponseEntity(token, HttpStatus.OK);
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity forgotPassword(@RequestBody UserDTO userDTO) {
        String message = service.forgotPassword(userDTO);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PutMapping("/{token}")
    public ResponseEntity resetPassword(@RequestBody ResetPassword resetPassword, @PathVariable String token) {
        String message = service.resetPassword(resetPassword, token);
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getBookStore(){
        StoreDTO store = service.getStore();
        return new ResponseEntity(store, HttpStatus.OK);
    }
}
