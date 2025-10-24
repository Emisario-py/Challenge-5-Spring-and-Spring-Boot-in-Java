package org.digitalnao.meli.dto.item;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemResponse {
    private Long id;
    private String productId;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Long orderId;
}
