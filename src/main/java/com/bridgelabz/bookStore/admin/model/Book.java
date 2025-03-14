package com.bridgelabz.bookStore.admin.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Book {
    @Id
    private Integer id;
    private String bookName;
    private String authorName;
    private String imgURL;
    private Integer price;
    @Column(length = 2048)
    private String bookDetail;
    private boolean inCart;
    private boolean inWishList;
    private Integer quantityInStock;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(bookName, book.bookName) &&
                Objects.equals(authorName, book.authorName) &&
                Objects.equals(imgURL, book.imgURL) &&
                Objects.equals(price, book.price) &&
                Objects.equals(bookDetail, book.bookDetail);
    }
}