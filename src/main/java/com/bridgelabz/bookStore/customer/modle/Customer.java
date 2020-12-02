package com.bridgelabz.bookStore.customer.modle;

import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.Orders;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Pattern(regexp = "^[A-Z]{1}[a-z]{2,}([ ][A-Z]{1}[a-z]{2,})*",
            message="The name must be greater then 3 character")
    private String fullName;

    @Pattern(regexp="^([a-zA-Z]{3,}([.|_|+|-]?[a-zA-Z0-9]+)?[@][a-zA-Z0-9]+[.][a-zA-Z]{2,3}([.]?[a-zA-Z]{2,3})?)$",
            message="Enter the correct email")
    private String email;

    private Boolean verified;

    @Pattern(regexp="^[5-9]{1}[0-9]{9}$",
            message="Mobile no. should be greater then 10 digit")
    private String mobileNo;

    @Pattern(regexp="^(?=.*[A-Z].*)(?=.*[0-9].*)(?=.*[!@#$%^&*+].*)[0-9a-zA-Z!@#$%^&*+]{8,}$",
            message="the password must be 8 character and contain an upperCase, lowerCase, specialCharacter & number")
    private String password;

    @OneToMany
    private List<AddressDetail> addressDetail;

    @OneToOne
    private Orders myOrders;

    {
        addressDetail = new ArrayList<>();
    }

    @OneToOne
    private Cart userCart;
}
