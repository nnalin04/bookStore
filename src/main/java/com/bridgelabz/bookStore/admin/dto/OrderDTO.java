package com.bridgelabz.bookStore.admin.dto;

import com.bridgelabz.bookStore.admin.model.SelectedBook;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Integer noOfItemsOrdered;
    @OneToMany
    private List<SelectedBook> bookList;
    {
        bookList = new ArrayList<>();
    }
}
