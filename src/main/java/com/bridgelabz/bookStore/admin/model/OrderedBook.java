package com.bridgelabz.bookStore.admin.model;

import com.bridgelabz.bookStore.customer.dto.AddressDTO;
import com.bridgelabz.bookStore.customer.modle.AddressDetail;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class OrderedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    private Book book;
    private Integer quantityForOrder;
    private String orderedDate;
    private String deliveredDate;
    @OneToOne
    private AddressDetail deliveryAddress;
}
