package com.bridgelabz.bookStore.admin.dto;

import com.bridgelabz.bookStore.admin.model.Book;
import com.bridgelabz.bookStore.customer.modle.AddressDetail;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToOne;

@Getter
@Setter
public class OrderDTO {
    private Integer noOfItemsOrdered;
    private AddressDetail deliveryAddress;
    private Book book;
}
