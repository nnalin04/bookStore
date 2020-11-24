package com.bridgelabz.bookStore.modle;

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
