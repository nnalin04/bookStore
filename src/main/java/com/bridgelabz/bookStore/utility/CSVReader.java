package com.bridgelabz.bookStore.utility;

import com.bridgelabz.bookStore.exception.BookStoreException;
import com.bridgelabz.bookStore.model.Book;
import com.bridgelabz.bookStore.model.BookStoreCSV;
import csvbuilder.CSVBuilderException;
import csvbuilder.CSVBuilderFactory;
import csvbuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class CSVReader {

    public List<Book> loadCensusData(Class<BookStoreCSV> bookStoreCSV, String filePath)
            throws BookStoreException {
        List<Book> books = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator csvFileIterator = csvBuilder.getCSVFileIterator(reader, bookStoreCSV);
            Iterable<BookStoreCSV> csvIterable = () -> csvFileIterator;
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(BookStoreCSV.class::cast)
                        .forEach(bookCSV ->
                                books.add(new Book(bookCSV))
                        );
            return books;

        } catch (IOException e) {
            throw new BookStoreException("Book Store data has faults");
        } catch (CSVBuilderException e) {
            throw new BookStoreException("Error occurred in loding the book store data");
        }
    }
}
