package com.bridgelabz.bookStore.customer.dto;

import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.Orders;
import com.bridgelabz.bookStore.customer.modle.AddressDetail;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;
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
