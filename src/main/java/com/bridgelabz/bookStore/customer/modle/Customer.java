package com.bridgelabz.bookStore.customer.modle;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String fullName;
    private String email;
    private Boolean verified;
    private String mobileNo;
    private String password;
    @OneToMany
    private List<AddressDetail> addressDetail;

    {
        addressDetail = new ArrayList<>();
    }

    @OneToOne
    private Cart userCart;
    @OneToMany
    private List<MyOrder> myOrders;

    {
        myOrders = new ArrayList<>();
    }
}
