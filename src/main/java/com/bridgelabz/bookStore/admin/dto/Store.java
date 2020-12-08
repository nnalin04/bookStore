package com.bridgelabz.bookStore.admin.dto;

import com.bridgelabz.bookStore.admin.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Store {

    public List<Book> books;
    public Integer totalBooks;
    public Integer noOfPage;

    public Store(List<Book> books, Integer totalBooks) {
    }

    public Store() {}
}
