package com.bridgelabz.bookStore.admin.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer noOfItemsOrdered;
    @OneToMany
    private List<OrderedBook> orderedBook;
    {
        orderedBook = new ArrayList<>();
    }
}
