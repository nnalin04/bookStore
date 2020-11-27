package com.bridgelabz.bookStore.customer.modle;

import com.bridgelabz.bookStore.model.Book;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class SelectedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Integer quantity;
    @OneToOne
    private Book book;
}
