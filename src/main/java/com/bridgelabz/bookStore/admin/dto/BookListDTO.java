package com.bridgelabz.bookStore.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookListDTO {
    private Integer pageNo;
    private String sortingMethod;
    private String searchQuarry;
}
