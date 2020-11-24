package com.bridgelabz.bookStore.dto;

import com.bridgelabz.bookStore.modle.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private String token;
    private Book book;
}
