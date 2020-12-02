package com.bridgelabz.bookStore.seller.repository;

import com.bridgelabz.bookStore.seller.model.AcceptedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAcceptedRepository extends JpaRepository<AcceptedBook, Integer> {
}
