package com.bridgelabz.bookStore.admin.repository;

import com.bridgelabz.bookStore.admin.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Integer> {
}
