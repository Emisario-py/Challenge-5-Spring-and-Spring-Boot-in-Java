package org.digitalnao.meli.repository;

import org.digitalnao.meli.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Item entity data access operations.
 * This interface extends JpaRepository to provide standard CRUD operations
 * and query methods for Item entities. It acts as the persistence layer
 * for item-related database operations, utilizing Spring Data JPA's
 * repository abstraction to simplify data access implementation.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

public interface ItemRepository extends JpaRepository<Item, Long> { }