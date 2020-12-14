package com.bridgelabz.bookStore.admin.controller;

import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.dto.Store;
import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.CartItem;
import com.bridgelabz.bookStore.admin.service.IBookStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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

    @GetMapping("/getStore/{currentPage}")
    public ResponseEntity<Store> getBookStore(@PathVariable Integer currentPage){
        Store books = iBookStoreService.getBooks(currentPage);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/addToCart")
    public ResponseEntity<List<CartItem>> addToCart(@RequestBody CartDTO cartDTO) {
        List<CartItem> noOfItems = iBookStoreService.addToCart(cartDTO);
        return new ResponseEntity<>(noOfItems, HttpStatus.OK);
    }

    @PutMapping("/editCart")
    public ResponseEntity<Cart> editCart(@RequestBody CartDTO cartDTO) {
        Cart cart = iBookStoreService.editCart(cartDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody CartDTO cartDTO) {
        Cart cart = iBookStoreService.removeFromCart(cartDTO);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/bookInDisplay")
    public ResponseEntity<Book> bookInDisplay(@RequestBody Book book) {
        Book bookInDisplay = iBookStoreService.bookInDisplay(book);
        return new ResponseEntity<>(bookInDisplay, HttpStatus.OK);
    }

    @GetMapping("/getBookToDisplay")
    public ResponseEntity<Book> getBookToDisplay() {
        Book book = iBookStoreService.getBookToDisplay();
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
