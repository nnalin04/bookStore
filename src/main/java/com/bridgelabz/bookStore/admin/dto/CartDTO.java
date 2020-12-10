package com.bridgelabz.bookStore.admin.dto;

import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.admin.model.SelectedBook;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CartDTO {
    private Integer id;
    private String userToken;
    private Integer noOfItems;
    private BookDTO bookDTO;
    private List<Book> books = new ArrayList<>();
}
