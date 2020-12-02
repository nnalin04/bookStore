package com.bridgelabz.bookStore.admin.controller;

import com.bridgelabz.bookStore.admin.dto.BookDTO;
import com.bridgelabz.bookStore.admin.dto.OrderDTO;
import com.bridgelabz.bookStore.admin.model.Orders;
import com.bridgelabz.bookStore.admin.service.IOrderedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping(path = "/bookstoreOrders")
public class BookStoreOrdersController {

    @Autowired
    private IOrderedService iOrderedService;

    @GetMapping("/{userToken}")
    public ResponseEntity<Orders> getOrders(@PathVariable String userToken) {
        Orders orders = iOrderedService.getMyOrders(userToken);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/{userToken}")
    public ResponseEntity<String> checkOut(@PathVariable String userToken,
                                           @RequestBody OrderDTO orderDTO) {
        String message = iOrderedService.checkOut(userToken, orderDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/{userToken}")
    public ResponseEntity<String> orderDelivery(@PathVariable String userToken,
                                                @RequestBody BookDTO bookDTO){
        String message = iOrderedService.orderDelivery(userToken, bookDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

