package org.digitalnao.meli.dto;

import lombok.Data;
import org.digitalnao.meli.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class CreateOrderResponse {

    public record Item(
            Long id,
            String productId,
            String name,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}

    private Long id;
    private String customerName;
    private String customerEmail;
    private OrderStatus status;
    private BigDecimal total;
    private Instant createdAt;
    private List<Item> items;
}
