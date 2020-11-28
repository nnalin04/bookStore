package com.bridgelabz.bookStore.controller;

import com.bridgelabz.bookStore.service.IOrderedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/bookstoreOrders")
public class BookStoreOrdersController {

    @Autowired
    private IOrderedService iOrderedService;
}

