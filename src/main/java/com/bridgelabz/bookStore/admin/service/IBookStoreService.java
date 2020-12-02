package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.Cart;

import java.util.List;

public interface IBookStoreService {
    void createBookStore();

    List<List<Book>> getBooks();

    Integer addToCart(CartDTO cartDTO, String userToken);

    Cart editCart(String userToken, CartDTO cartDTO);

    Cart removeFromCart(String userToken, CartDTO cartDTO);
}
