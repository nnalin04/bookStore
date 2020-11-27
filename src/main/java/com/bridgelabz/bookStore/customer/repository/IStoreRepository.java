package com.bridgelabz.bookStore.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Integer> {
}
