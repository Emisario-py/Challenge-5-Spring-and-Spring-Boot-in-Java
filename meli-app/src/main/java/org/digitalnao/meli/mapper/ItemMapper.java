package org.digitalnao.meli.mapper;

import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.dto.item.ItemResponse;

public class ItemMapper {

    public static ItemResponse toResponse(Item item) {
        if (item == null) return null;

        ItemResponse dto = new ItemResponse();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setName(item.getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());

        // ✅ Agregar orderId para referencia (opcional pero útil)
        if (item.getOrder() != null) {
            dto.setOrderId(item.getOrder().getId());
        }

        return dto;
    }
}