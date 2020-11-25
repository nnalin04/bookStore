package com.bridgelabz.bookStore.customer.repository;

import com.bridgelabz.bookStore.customer.modle.AddressDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAddressRepository extends JpaRepository<AddressDetail, Integer> {
}
