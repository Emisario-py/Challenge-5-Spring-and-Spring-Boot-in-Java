package org.digitalnao.meli.dto.order;

import lombok.Data;
import org.digitalnao.meli.dto.item.ItemResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing the response structure for order data.
 * This class encapsulates order information returned to API consumers, including
 * order identification, associated client, creation timestamp, and a list of
 * items within the order. It provides a complete view of order data in a format
 * suitable for external consumption, decoupled from the internal domain model.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Data
public class OrderResponse {
    private Long id;
    private Long clientId;
    private Instant createdAt;
    private List<ItemResponse> items;
}
