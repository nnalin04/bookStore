package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.admin.dto.OrderDTO;
import com.bridgelabz.bookStore.admin.model.Orders;

public interface IOrderedService {

    Orders getMyOrders(String userToken);
    String checkOut(String userToken, OrderDTO orderDTO);
    String orderDelivery(String userToken, BookDTO bookDTO);
}
