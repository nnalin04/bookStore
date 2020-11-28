package com.bridgelabz.bookStore.model;

import com.bridgelabz.bookStore.model.Book;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class SelectedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantityInCart;
    private Integer quantityForOrder;
    private LocalDate orderedDate;
    private LocalDate deliveredDate;
    @OneToOne
    private Book book;
}
