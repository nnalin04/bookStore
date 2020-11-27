package com.bridgelabz.bookStore.customer.dto;

import com.bridgelabz.bookStore.model.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Integer quantity;
    private Book book;
}
