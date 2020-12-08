package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.dto.Store;
import com.bridgelabz.bookStore.admin.model.Cart;

public interface IBookStoreService {
    void createBookStore();

    Store getBooks(Integer currentPage);

    Cart addToCart(CartDTO cartDTO);

    Cart editCart(CartDTO cartDTO);

    Cart removeFromCart(CartDTO cartDTO);

    void beforeServerClosing();
}
