package com.bridgelabz.bookStore.customer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Integer noOfItems;
    private List<BookDTO> booksDTO;
}
