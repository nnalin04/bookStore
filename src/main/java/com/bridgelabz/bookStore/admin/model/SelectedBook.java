package com.bridgelabz.bookStore.admin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class SelectedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantityInCart;
    private Integer quantityForOrder;
    private Date orderedDate;
    private Date deliveredDate;
    @OneToOne
    private Book book;
}
