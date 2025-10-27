package org.digitalnao.meli.dto.order;

import lombok.Data;
import org.digitalnao.meli.dto.item.CreateItemRequest;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the request payload for creating new orders.
 * This class encapsulates all information needed to create an order, including
 * the order identifier, associated client ID, and a collection of items to be
 * included in the order. It serves as the input structure for order creation
 * endpoints, allowing clients to submit complete order information in a single request.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Data
public class CreateOrderRequest {
    private Long id;
    private Long clientId;
    private List<CreateItemRequest> items;
}
