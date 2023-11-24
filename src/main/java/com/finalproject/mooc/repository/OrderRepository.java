package com.finalproject.mooc.repository;

import com.finalproject.mooc.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
