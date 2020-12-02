package com.bridgelabz.bookStore.admin.repository;

import com.bridgelabz.bookStore.admin.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {
}
