package org.digitalnao.meli.dto.order;

import lombok.Data;
import org.digitalnao.meli.dto.item.ItemResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long clientId;
    private Instant createdAt;
    private List<ItemResponse> items;
}
