package org.digitalnao.meli.mapper;

import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.dto.order.OrderResponse;
import java.util.stream.Collectors;

public class OrderMapper {

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