package com.bridgelabz.bookStore.controller;

import com.bridgelabz.bookStore.dto.CartDTO;
import com.bridgelabz.bookStore.model.Book;
import com.bridgelabz.bookStore.model.Cart;
import com.bridgelabz.bookStore.service.IBookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/bookstoreCart")
public class BookStoreCartController {

    @Autowired
    private IBookStoreService iBookStoreService;

    @PostConstruct
    public void init() {
        iBookStoreService.createBookStore();
    }

    @GetMapping("/getStore")
    public ResponseEntity<List<Book>> getBookStore(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        List<Book> books = iBookStoreService.getBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/addToCart/{userToken}")
    public ResponseEntity<Integer> addToCart(@PathVariable String userToken,
                                             @RequestBody CartDTO cartDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Integer noOfItems = iBookStoreService.addToCart(cartDTO, userToken);
        return new ResponseEntity<>(noOfItems, HttpStatus.OK);
    }

    @PutMapping("/editCart/{userToken}")
    public ResponseEntity<Cart> editCart(@PathVariable String userToken,
                                         @RequestBody CartDTO cartDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Cart cart = iBookStoreService.editCart(userToken, cartDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/removeFromCart/{userToken}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable String userToken,
                                               @RequestBody CartDTO cartDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity(bindingResult.getAllErrors().get(0).getDefaultMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        Cart cart = iBookStoreService.removeFromCart(userToken, cartDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
