package com.bridgelabz.bookStore.model;

import com.opencsv.bean.CsvBindByName;

public class BookStoreCSV {

    @CsvBindByName(column = "id", required = true)
    public Integer Id;

    @CsvBindByName(column = "author", required = true)
    public String authorName;

    @CsvBindByName(column = "title", required = true)
    public String bookName;

    @CsvBindByName(column = "image", required = true)
    public String imgURL;

    @CsvBindByName(column = "price", required = true)
    public Integer price;

    @CsvBindByName(column = "description", required = true)
    public String bookDetail;
}
