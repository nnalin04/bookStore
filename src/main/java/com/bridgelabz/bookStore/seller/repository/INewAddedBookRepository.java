package com.bridgelabz.bookStore.seller.repository;

import com.bridgelabz.bookStore.seller.model.NewAddedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface INewAddedBookRepository extends JpaRepository<NewAddedBook, Integer> {
}
