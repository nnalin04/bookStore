package com.bridgelabz.bookStore.customer.repository;

import com.bridgelabz.bookStore.model.SelectedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISelectedRepository extends JpaRepository<SelectedBook, Integer> {
}
