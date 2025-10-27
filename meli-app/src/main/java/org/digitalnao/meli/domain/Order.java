package org.digitalnao.meli.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing an order in the system.
 * This domain model manages order data including creation timestamp,
 * associated client, and a collection of items. Each order belongs to
 * a single client and can contain multiple items through a one-to-many
 * relationship. The creation timestamp is automatically set and immutable.
 * The entity is mapped to the "orders" table in the database.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    private Long id;


    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();
}