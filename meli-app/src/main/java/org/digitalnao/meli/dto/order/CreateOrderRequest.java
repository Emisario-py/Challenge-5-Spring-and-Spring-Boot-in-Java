package org.digitalnao.meli.dto.order;

import lombok.Data;
import org.digitalnao.meli.dto.item.CreateItemRequest;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long id;
    private Long clientId;
    private List<CreateItemRequest> items;
}
