package com.bridgelabz.bookStore.service;

import com.bridgelabz.bookStore.dto.CartDTO;
import com.bridgelabz.bookStore.model.Book;
import com.bridgelabz.bookStore.model.Cart;

import java.util.List;

public interface IBookStoreService {
    void createBookStore();

    List<Book> getBooks();

    Integer addToCart(CartDTO cartDTO, String userToken);

    Cart editCart(String userToken, CartDTO cartDTO);

    Cart removeFromCart(String userToken, CartDTO cartDTO);
}
