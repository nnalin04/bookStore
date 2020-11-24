package com.bridgelabz.bookStore.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StoreDTO {
    private List<BookDTO> books = new ArrayList<>();
}
