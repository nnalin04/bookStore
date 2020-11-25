package com.bridgelabz.bookStore.customer.repository;

import com.bridgelabz.bookStore.customer.modle.SelectedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISelectedRepository extends JpaRepository<SelectedBook, Integer> {
}
