package com.bridgelabz.bookStore.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Book {

    @Id
    private Integer id;
    private String bookName;
    private String authorName;
    private String imgURL;
    private Integer price;
    private String bookDetail;
    private Integer quantityInStock;

    public Book(BookStoreCSV bookStoreCSV) {
        this.id = bookStoreCSV.Id;
        this.bookName = bookStoreCSV.bookName;
        this.authorName = bookStoreCSV.authorName;
        this.imgURL = bookStoreCSV.imgURL;
        this.price = bookStoreCSV.price;
        this.bookDetail = bookStoreCSV.bookDetail;
    }

    public Book() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(bookName, book.bookName) &&
                Objects.equals(authorName, book.authorName) &&
                Objects.equals(price, book.price) &&
                Objects.equals(bookDetail, book.bookDetail) &&
                Objects.equals(quantityInStock, book.quantityInStock);
    }
}