package com.bridgelabz.bookStore.customer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private Integer id;
    private String type;
    private Long pinCode;
    private String locality;
    private String address;
    private String city;
    private String landmark;
}
