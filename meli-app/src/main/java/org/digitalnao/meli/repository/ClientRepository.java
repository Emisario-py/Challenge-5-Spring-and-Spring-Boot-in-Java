package org.digitalnao.meli.repository;

import org.digitalnao.meli.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Client entity data access operations.
 * This interface extends JpaRepository to provide standard CRUD operations
 * and query methods for Client entities. It serves as the data access layer
 * for client-related database operations, leveraging Spring Data JPA's
 * automatic implementation generation for common database interactions.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

public interface ClientRepository extends JpaRepository<Client, Long> { }