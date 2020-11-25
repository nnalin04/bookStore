package com.bridgelabz.bookStore.customer.controller;

import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.customer.modle.Cart;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.service.IBookStoreService;
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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        String message = service.registerUser(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/verify/{userToken}")
    public ResponseEntity<String> VerifyUser(@PathVariable String userToken) {
        String message = service.verifyUser(userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        String token = service.loginUser(userDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody UserDTO userDTO) {
        String message = service.forgotPassword(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/reset/{userToken}")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword, @PathVariable String userToken) {
        String message = service.resetPassword(resetPassword, userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/editUser/{userToken}")
    public ResponseEntity<Customer> editUser(@PathVariable String userToken, @RequestBody UserDTO userDTO) {
        Customer customer = service.editUser(userToken, userDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/getStore")
    public ResponseEntity<StoreDTO> getBookStore(){
        StoreDTO store = service.getStore();
        return new ResponseEntity<>(store, HttpStatus.OK);
    }

    @PutMapping("/addToCart/{userToken}")
    public ResponseEntity<Integer> addToCart(@PathVariable String userToken, @RequestBody BookDTO bookDTO) {
        Integer noOfItems = service.addToCart(bookDTO, userToken);
        return new ResponseEntity<>(noOfItems, HttpStatus.OK);
    }

    @PutMapping("/editCart/{userToken}")
    public ResponseEntity<Cart> editCart(@PathVariable String userToken, @RequestBody BookDTO bookDTO) {
        Cart cart = service.editCart(userToken, bookDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/removeFromCart/{userToken}/{bookToken}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable String userToken, @PathVariable String bookToken) {
        Cart cart = service.removeFromCart(userToken, bookToken);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/addAddress/{userToken}")
    public ResponseEntity<Customer> addAddress(@PathVariable String userToken, @RequestBody AddressDTO addressDTO) {
        Customer customer = service.addAddress(userToken, addressDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}
