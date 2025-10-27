package org.digitalnao.meli.dto.item;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the response structure for item data.
 * This class encapsulates item information sent to API consumers, including
 * product details, quantity, pricing, and the associated order identifier.
 * It serves as the output format for item-related endpoints, providing a
 * simplified view of item data separate from the internal domain model.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Data
public class ItemResponse {
    private Long id;
    private String productId;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Long orderId;
}
