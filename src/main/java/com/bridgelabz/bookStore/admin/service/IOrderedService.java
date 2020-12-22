package com.bridgelabz.bookStore.admin.service;

import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.admin.dto.OrderDTO;
import com.bridgelabz.bookStore.admin.dto.ReturnOrder;
import com.bridgelabz.bookStore.admin.model.Orders;

import javax.mail.MessagingException;

public interface IOrderedService {

    Orders getMyOrders(String userToken);
    ReturnOrder checkOut(String userToken, OrderDTO orderDTO) throws MessagingException;
    String orderDelivery(String userToken, BookDTO bookDTO) throws MessagingException;
}
