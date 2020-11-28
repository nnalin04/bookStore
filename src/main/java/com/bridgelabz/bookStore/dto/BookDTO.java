package com.bridgelabz.bookStore.dto;

import com.bridgelabz.bookStore.model.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Integer userSelectedQuantity;
    private Book book;
}
