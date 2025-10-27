package org.digitalnao.meli.mapper;

import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.dto.item.ItemResponse;

/**
 * Utility class for converting Item entities to ItemResponse DTOs.
 * Provides a single static method toResponse() that handles the complete transformation
 * from persistence layer to presentation layer. The conversion process includes:
 * <ol>
 *   <li>Null safety check - returns null if input item is null</li>
 *   <li>Creates new ItemResponse DTO instance</li>
 *   <li>Copies all scalar fields: id, productId, name, quantity, unitPrice</li>
 *   <li>Extracts order ID from the parent Order relationship if order is not null</li>
 *   <li>Sets orderId on the DTO for reference purposes</li>
 * </ol>
 *
 * This mapper breaks the bidirectional Item-Order relationship by only including
 * the order ID rather than the full Order entity, preventing circular references
 * in JSON serialization and maintaining clean API boundaries.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see ItemResponse
 */

public class ItemMapper {

    /**
     * Converts an Item domain entity into an ItemResponse DTO.
     * @param item the Item entity to convert, may be null
     * @return ItemResponse DTO with all item data and order ID reference, or null if input is null
     */
    public static ItemResponse toResponse(Item item) {
        if (item == null) return null;

        ItemResponse dto = new ItemResponse();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setName(item.getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());

        if (item.getOrder() != null) {
            dto.setOrderId(item.getOrder().getId());
        }

        return dto;
    }
}