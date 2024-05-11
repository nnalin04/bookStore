package com.bridgelabz.bookStore.admin.dto;

import com.bridgelabz.bookStore.admin.model.Cart;
import com.bridgelabz.bookStore.admin.model.OrderedBook;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnOrder {
    private Cart cart;
    private OrderedBook orderedBook;
}