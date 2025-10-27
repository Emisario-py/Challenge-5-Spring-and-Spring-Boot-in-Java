package org.digitalnao.meli.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the request payload for creating new items.
 * This class encapsulates all necessary information required to create an item,
 * including product identification, name, quantity, and pricing. It includes
 * validation constraints to ensure data integrity before processing the request.
 * Used as input for item creation endpoints.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */
@Data
public class CreateItemRequest {
    private Long id;

    @NotBlank(message = "Product ID is required")
    private String productId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
}