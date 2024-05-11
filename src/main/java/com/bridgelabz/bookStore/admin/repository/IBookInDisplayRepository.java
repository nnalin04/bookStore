package com.bridgelabz.bookStore.admin.repository;

import com.bridgelabz.bookStore.admin.model.BookInDisplay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookInDisplayRepository extends JpaRepository<BookInDisplay, Integer> {
}
