package com.bridgelabz.bookStore.seller.controller;

import com.bridgelabz.bookStore.customer.dto.AddressDTO;
import com.bridgelabz.bookStore.customer.dto.ResetPassword;
import com.bridgelabz.bookStore.customer.dto.UserDTO;
import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.seller.model.Seller;
import com.bridgelabz.bookStore.seller.service.IBookStoreSeller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/bookstoreSeller")
public class BookStoreSellerController {

    @Autowired
    private IBookStoreSeller iBookStoreSellerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        String message = iBookStoreSellerService.registerUser(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/verify/{userToken}")
    public ResponseEntity<String> VerifyUser(@PathVariable String userToken) {
        String message = iBookStoreSellerService.verifyUser(userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        String token = iBookStoreSellerService.loginUser(userDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody UserDTO userDTO) {
        String message = iBookStoreSellerService.forgotPassword(userDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/reset/{userToken}")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPassword resetPassword,
                                                @PathVariable String userToken) {
        String message = iBookStoreSellerService.resetPassword(resetPassword, userToken);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/editUser/{userToken}")
    public ResponseEntity<Seller> editUser(@PathVariable String userToken,
                                             @RequestBody UserDTO userDTO) {
        Seller customer = iBookStoreSellerService.editUser(userToken, userDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/addAddress/{userToken}")
    public ResponseEntity<Seller> addAddress(@PathVariable String userToken,
                                               @RequestBody AddressDTO addressDTO) {
        Seller customer = iBookStoreSellerService.addAddress(userToken, addressDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PutMapping("/editAddress/{userToken}")
    public ResponseEntity<Seller> editAddress(@PathVariable String userToken,
                                      @RequestBody AddressDTO addressDTO) {
        Seller customer = iBookStoreSellerService.editAddress(userToken, addressDTO);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/sendBookDetail/{userToken}")
    public ResponseEntity<String> sendBookTOSell(@PathVariable String userToken,
                                                 @RequestBody BookDTO bookDTO) throws MessagingException {
        String message = iBookStoreSellerService.sendBookTOAdmin(userToken, bookDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/rejected/{userToken}")
    public ResponseEntity<String> rejectedBook(@PathVariable String userToken, @RequestBody BookDTO bookDTO) {
        String message = iBookStoreSellerService.rejectedBook(userToken, bookDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/accepted/{userToken}")
    public ResponseEntity<String> acceptedBook(@PathVariable String userToken, @RequestBody BookDTO bookDTO) {
        String message = iBookStoreSellerService.acceptedBook(userToken, bookDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
