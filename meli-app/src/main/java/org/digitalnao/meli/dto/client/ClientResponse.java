package org.digitalnao.meli.dto.client;

import lombok.Data;
import org.digitalnao.meli.dto.order.OrderResponse;

import java.util.List;

@Data
public class ClientResponse {
    private Long id;
    private String clientName;
    private String email;
    private String address;
    private Integer age;
    private List<OrderResponse> orders;
}
