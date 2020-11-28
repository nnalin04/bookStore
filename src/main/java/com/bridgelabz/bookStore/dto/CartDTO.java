package com.bridgelabz.bookStore.dto;

import com.bridgelabz.bookStore.model.SelectedBook;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Integer id;
    private Integer noOfItems;
    private BookDTO bookDTO;
    @OneToMany
    private List<SelectedBook> selectedBooks;

    {
        selectedBooks = new ArrayList<>();
    }
}
