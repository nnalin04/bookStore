package com.bridgelabz.bookStore.customer.controller;
import com.bridgelabz.bookStore.admin.model.CartItem;
import com.bridgelabz.bookStore.customer.dto.*;
import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.customer.service.IBookStoreCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/bookstoreCustomer")
public class BookStoreCustomerController {

    @Autowired
    private IBookStoreCustomerService iBookStoreCustomerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser( @RequestBody UserDTO userDTO) {

        String message = iBookStoreCustomerService.registerUser(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/verify/{userToken}")
    public ResponseEntity<String> VerifyUser(@PathVariable String userToken) {
        String message = iBookStoreCustomerService.verifyUser(userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerDTO> loginUser(@RequestBody UserDTO userDTO) {
        CustomerDTO customerDTO = iBookStoreCustomerService.loginUser(userDTO);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody UserDTO userDTO) {
        String message = iBookStoreCustomerService.forgotPassword(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/reset/{userToken}")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword,
                                                @PathVariable String userToken) {
        String message = iBookStoreCustomerService.resetPassword(resetPassword, userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/editUser/{userToken}")
    public ResponseEntity<Customer> editUser(@PathVariable String userToken,
                                             @RequestBody UserDTO userDTO) {
        Customer customer = iBookStoreCustomerService.editUser(userToken, userDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/addAddress/{userToken}")
    public ResponseEntity<Customer> addAddress(@PathVariable String userToken,
                                               @RequestBody AddressDTO addressDTO) {
        Customer customer = iBookStoreCustomerService.addAddress(userToken, addressDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/getCustomerCart/{token}")
    public ResponseEntity<List<CartItem>> getCustomerCart(@PathVariable String token){
        List<CartItem> books  = iBookStoreCustomerService.getCustomerCart(token);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/editAddress/{userToken}")
    public ResponseEntity editAddress(@PathVariable String userToken,
                                      @RequestBody AddressDTO addressDTO) {
        Customer customer = iBookStoreCustomerService.editAddress(userToken, addressDTO);
        return new ResponseEntity(customer, HttpStatus.OK);
    }
}
