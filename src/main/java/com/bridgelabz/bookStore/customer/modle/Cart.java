package com.bridgelabz.bookStore.customer.modle;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Integer noOfItems;
    @OneToMany
    private List<SelectedBook> selectedBooks;

    {
        selectedBooks = new ArrayList<>();
    }
}