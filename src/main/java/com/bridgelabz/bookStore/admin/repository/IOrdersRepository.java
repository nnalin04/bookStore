package com.bridgelabz.bookStore.admin.repository;

import com.bridgelabz.bookStore.admin.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrdersRepository extends JpaRepository<Orders, Integer> {
}
