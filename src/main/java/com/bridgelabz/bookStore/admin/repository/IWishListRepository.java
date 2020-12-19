package com.bridgelabz.bookStore.admin.repository;

import com.bridgelabz.bookStore.admin.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Integer> {
}
