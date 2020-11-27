package com.bridgelabz.bookStore.customer.controller;

import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.customer.modle.Cart;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.service.IBookStoreService;
import com.bridgelabz.bookStore.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/bookstore")
public class BookStoreController {

    @Autowired
    IBookStoreService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        String message = service.registerUser(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/verify/{userToken}")
    public ResponseEntity<String> VerifyUser(@PathVariable String userToken, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        String message = service.verifyUser(userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        String token = service.loginUser(userDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        String message = service.forgotPassword(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/reset/{userToken}")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword,
                                                @PathVariable String userToken, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        String message = service.resetPassword(resetPassword, userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/editUser/{userToken}")
    public ResponseEntity<Customer> editUser(@PathVariable String userToken,
                                             @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Customer customer = service.editUser(userToken, userDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/getStore")
    public ResponseEntity<List<Book>> getBookStore(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        List<Book> books = service.getBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/addToCart/{userToken}")
    public ResponseEntity<Integer> addToCart(@PathVariable String userToken,
                                             @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Integer noOfItems = service.addToCart(bookDTO, userToken);
        return new ResponseEntity<>(noOfItems, HttpStatus.OK);
    }

    @PutMapping("/editCart/{userToken}")
    public ResponseEntity<Cart> editCart(@PathVariable String userToken,
                                         @RequestBody BookDTO bookDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Cart cart = service.editCart(userToken, bookDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/removeFromCart/{userToken}/{bookToken}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable String userToken,
                                               @PathVariable String bookToken, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Cart cart = service.removeFromCart(userToken, bookToken);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/addAddress/{userToken}")
    public ResponseEntity<Customer> addAddress(@PathVariable String userToken,
                                               @RequestBody AddressDTO addressDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Customer customer = service.addAddress(userToken, addressDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/editAdddress/{userToken}")
    public ResponseEntity editAddress(@PathVariable String userToken,
                                      @RequestBody AddressDTO addressDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Customer customer = service.editAddress(userToken, addressDTO);
        return new ResponseEntity(customer, HttpStatus.OK);
    }
}
