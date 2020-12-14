package com.bridgelabz.bookStore.admin.repository;

import com.bridgelabz.bookStore.admin.model.OrderedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderedBookRepository extends JpaRepository<OrderedBook, Integer> {
}
