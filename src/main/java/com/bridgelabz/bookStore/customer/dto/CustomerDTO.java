package com.bridgelabz.bookStore.customer.dto;

import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.Orders;
import com.bridgelabz.bookStore.admin.model.WishList;
import com.bridgelabz.bookStore.customer.modle.AddressDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomerDTO {

    private String token;
    private String fullName;
    private String email;
    private Boolean verified;
    private String mobileNo;
    private List<AddressDetail> addressDetail;
    private Orders myOrders;
    private WishList wishList;
    {
        addressDetail = new ArrayList<>();
    }
    private Cart userCart;
}
