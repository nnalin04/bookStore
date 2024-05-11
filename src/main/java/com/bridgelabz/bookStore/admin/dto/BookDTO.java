package com.bridgelabz.bookStore.admin.dto;

import com.bridgelabz.bookStore.admin.model.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Integer userSelectedQuantity;
    private String reason;
    private Book book;
}
