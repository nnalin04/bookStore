package com.bridgelabz.bookStore.repository;

import com.bridgelabz.bookStore.modle.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {
}
