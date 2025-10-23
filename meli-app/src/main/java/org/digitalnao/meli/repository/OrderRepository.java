package org.digitalnao.meli.repository;

import org.digitalnao.meli.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> { }