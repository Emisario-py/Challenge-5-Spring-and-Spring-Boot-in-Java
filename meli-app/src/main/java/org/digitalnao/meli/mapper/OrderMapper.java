package org.digitalnao.meli.mapper;

import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.dto.order.OrderResponse;
import java.util.stream.Collectors;

/**
 * Utility class for converting Order entities to OrderResponse DTOs.
 * Provides a single static method toResponse() that handles the complete transformation
 * from persistence layer to presentation layer. The conversion process includes:
 * <ol>
 *   <li>Null safety check - returns null if input order is null</li>
 *   <li>Creates new OrderResponse DTO instance</li>
 *   <li>Copies order ID and creation timestamp (Instant)</li>
 *   <li>Extracts client ID from the Client relationship if client is not null</li>
 *   <li>Recursively maps items collection using ItemMapper.toResponse() via Stream API</li>
 *   <li>Collects mapped items into a List and sets on the DTO</li>
 * </ol>
 *
 * This mapper prevents circular references by using client ID instead of full Client
 * entity, while still recursively converting the items collection for complete order
 * representation in API responses.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see OrderResponse
 * @see ItemMapper
 */

public class OrderMapper {

    /**
     * Converts an Order domain entity into an OrderResponse DTO.
     * @param order the Order entity to convert, may be null
     * @return OrderResponse DTO with order data, client ID, and mapped items, or null if input is null
     */
    public static OrderResponse toResponse(Order order) {
        if (order == null) return null;

        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setCreatedAt(order.getCreatedAt());

        if (order.getClient() != null) {
            dto.setClientId(order.getClient().getId());
        }

        if (order.getItems() != null)
            dto.setItems(order.getItems().stream()
                    .map(ItemMapper::toResponse)
                    .collect(Collectors.toList()));

        return dto;
    }
}