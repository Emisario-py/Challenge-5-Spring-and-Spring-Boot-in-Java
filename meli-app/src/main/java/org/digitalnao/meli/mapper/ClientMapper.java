package org.digitalnao.meli.mapper;

import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.dto.client.ClientResponse;
import java.util.stream.Collectors;

public class ClientMapper {

    public static ClientResponse toResponse(Client client) {
        if (client == null) return null;

        ClientResponse dto = new ClientResponse();
        dto.setId(client.getId());
        dto.setClientName(client.getClientName());
        dto.setEmail(client.getEmail());
        dto.setAddress(client.getAddress());
        dto.setAge(client.getAge());

        if (client.getOrders() != null)
            dto.setOrders(client.getOrders().stream()
                    .map(OrderMapper::toResponse)
                    .collect(Collectors.toList()));

        return dto;
    }
}
