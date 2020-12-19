package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.CartDTO;
import com.bridgelabz.bookStore.admin.dto.Store;
import com.bridgelabz.bookStore.admin.dto.BookListDTO;
import com.bridgelabz.bookStore.admin.dto.WishListDTO;
import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.CartItem;

import java.util.List;

public interface IBookStoreService {
    void createBookStore();

    Store getBooks(BookListDTO bookListDTO);

    List<CartItem> addToCart(CartDTO cartDTO);

    Cart editCart(CartDTO cartDTO);

    Cart removeFromCart(CartDTO cartDTO);

    Book bookInDisplay(Book book);

    Book getBookToDisplay();

    List<Book> findBooksBySearch(String name);

    List<Book> addBookToWishList(WishListDTO wishListDTO);

    List<Book> deleteBookToWishList(WishListDTO wishListDTO);
}
