package org.digitalnao.meli.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String productId;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private Integer quantity;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;


    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal lineTotal;


    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getLineTotal() { return lineTotal; }
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}
