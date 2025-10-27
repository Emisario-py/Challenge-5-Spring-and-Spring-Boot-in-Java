package org.digitalnao.meli.repository;

import org.digitalnao.meli.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Order entity data access operations.
 * This interface extends JpaRepository to provide standard CRUD operations
 * and query methods for Order entities. It serves as the data access layer
 * for order-related database operations, leveraging Spring Data JPA to
 * automatically generate implementation code for database interactions.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

public interface OrderRepository extends JpaRepository<Order, Long> { }