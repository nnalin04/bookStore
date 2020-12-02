package com.bridgelabz.bookStore.admin.controller;

import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.service.IBookStoreService;
import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.exception.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
    public ResponseEntity<List<List<Book>>> getBookStore(){
        List<List<Book>> books = iBookStoreService.getBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/addToCart/{userToken}")
    public ResponseEntity<Integer> addToCart(@PathVariable String userToken,
                                             @RequestBody CartDTO cartDTO) {
        Integer noOfItems = iBookStoreService.addToCart(cartDTO, userToken);
        return new ResponseEntity<>(noOfItems, HttpStatus.OK);
    }

    @PutMapping("/editCart/{userToken}")
    public ResponseEntity<Cart> editCart(@PathVariable String userToken,
                                         @RequestBody CartDTO cartDTO) {
        Cart cart = iBookStoreService.editCart(userToken, cartDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/removeFromCart/{userToken}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable String userToken,
                                               @RequestBody CartDTO cartDTO) {
        Cart cart = iBookStoreService.removeFromCart(userToken, cartDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BookStoreException.class)
    public ErrorMessage handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
