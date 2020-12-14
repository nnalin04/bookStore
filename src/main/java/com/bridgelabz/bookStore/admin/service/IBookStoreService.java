package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.dto.Store;
import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.CartItem;

import java.util.List;

public interface IBookStoreService {
    void createBookStore();

    Store getBooks(Integer currentPage);

    List<CartItem> addToCart(CartDTO cartDTO);

    Cart editCart(CartDTO cartDTO);

    Cart removeFromCart(CartDTO cartDTO);

    Book bookInDisplay(Book book);

    Book getBookToDisplay();
}
