package com.bridgelabz.bookStore.utility;

import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.admin.model.Book;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class CSVReader {

    private static final String BOOK_CSV_FILE_PATH = "./src/main/resources/books_data.csv";

    public List<Book> loadCensusData() throws BookStoreException{
        List<Book> books = new ArrayList<>();
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOK_CSV_FILE_PATH))) {
            while((line = reader.readLine()) != null){
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase("id"))
                    continue;
                Book book = new Book();
                book.setId(Integer.valueOf(data[0]));
                book.setAuthorName(data[1]);
                book.setBookName(data[2]);
                book.setImgURL(data[3]);
                book.setPrice(Integer.valueOf(data[4]));
                StringBuilder description = new StringBuilder();
                for (int i = 5; i < data.length ; i++) {
                    description.append(data[i]);
                }
                book.setBookDetail(String.valueOf(description));
                books.add(book);
            }
        } catch (IOException e) {
            throw new BookStoreException("Book Store data has faults");
        }
        return books;
    }
}
