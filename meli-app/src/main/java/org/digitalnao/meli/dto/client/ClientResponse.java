package org.digitalnao.meli.dto.client;

import lombok.Data;
import org.digitalnao.meli.dto.order.OrderResponse;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing the response structure for client data.
 * This class encapsulates client information that is sent to API consumers,
 * including basic client details and a list of associated orders.
 * It serves as the output format for client-related endpoints, decoupling
 * the internal domain model from the external API representation.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Data
public class ClientResponse {
    private Long id;
    private String clientName;
    private String email;
    private String address;
    private Integer age;
    private List<OrderResponse> orders;
}
