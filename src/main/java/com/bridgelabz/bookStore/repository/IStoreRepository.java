package com.bridgelabz.bookStore.repository;

import com.bridgelabz.bookStore.modle.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Integer> {
}
