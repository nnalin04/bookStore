package com.bridgelabz.bookStore.customer.dto;

import com.bridgelabz.bookStore.customer.modle.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private String token;
    private Integer quantity;
    private Book book;
}
