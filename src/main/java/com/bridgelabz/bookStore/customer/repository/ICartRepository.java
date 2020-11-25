package com.bridgelabz.bookStore.customer.repository;

import com.bridgelabz.bookStore.customer.modle.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends JpaRepository<Cart, Integer> {
}
