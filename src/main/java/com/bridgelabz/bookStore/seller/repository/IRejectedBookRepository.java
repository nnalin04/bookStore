package com.bridgelabz.bookStore.seller.repository;

import com.bridgelabz.bookStore.seller.model.RejectedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRejectedBookRepository extends JpaRepository<RejectedBook, Integer> {
}
