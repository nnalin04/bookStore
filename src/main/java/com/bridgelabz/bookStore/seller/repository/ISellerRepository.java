package com.bridgelabz.bookStore.seller.repository;

import com.bridgelabz.bookStore.customer.modle.Customer;
import com.bridgelabz.bookStore.seller.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISellerRepository extends JpaRepository<Seller, Integer> {
    Optional<Seller> findByEmail(String email);
}
