package org.digitalnao.meli.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Entity class representing an item within an order.
 * This domain model captures product information including product ID,
 * name, quantity, and unit price. Each item belongs to a single order
 * through a many-to-one relationship. Items represent individual products
 * that are part of a customer's order.
 * The entity is mapped to the "items" table in the database.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Entity
@Table(name = "items")
@Data
public class Item {

    @Id
    private Long id;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}