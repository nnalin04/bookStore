package com.bridgelabz.bookStore.admin.dto;

import com.bridgelabz.bookStore.admin.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WishListDTO {
    private String token;
    private List<Book> books;
    private Book book;
}
