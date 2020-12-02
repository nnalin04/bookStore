package com.bridgelabz.bookStore.seller.model;

import com.bridgelabz.bookStore.admin.model.Book;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class RejectedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer noOfBookInStock;
    @Column(length = 2048)
    private String reasonForRejection;
    @OneToOne
    private Book book;
}
