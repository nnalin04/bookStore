package com.bridgelabz.bookStore.customer.dto;

import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String fullName;
    private String email;
    private String mobileNo;
    private String password;
    private File image;
    private List<CartItem> cartItemList;
    private List<Book> booksInWishList;
}
