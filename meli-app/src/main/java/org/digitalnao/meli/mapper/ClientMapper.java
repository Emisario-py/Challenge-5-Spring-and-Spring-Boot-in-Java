package org.digitalnao.meli.mapper;

import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.dto.client.ClientResponse;
import java.util.stream.Collectors;

/**
 * Utility class for converting Client entities to ClientResponse DTOs.
 * Provides a single static method toResponse() that handles the complete transformation
 * from persistence layer to presentation layer. The conversion process includes:
 * <ol>
 *   <li>Null safety check - returns null if input client is null</li>
 *   <li>Creates new ClientResponse DTO instance</li>
 *   <li>Copies all scalar fields: id, clientName, email, address, age</li>
 *   <li>Recursively maps orders collection using OrderMapper.toResponse() via Stream API</li>
 *   <li>Collects mapped orders into a List and sets on the DTO</li>
 * </ol>
 *
 * This mapper prevents circular reference issues in JSON serialization by breaking
 * the bidirectional relationship chain and ensures clean separation between domain
 * entities and API response structures.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see ClientResponse
 * @see OrderMapper
 */

public class ClientMapper {

    /**
     * Converts a Client domain entity into a ClientResponse DTO.
     * @param client the Client entity to convert, may be null
     * @return ClientResponse DTO with all client data and mapped orders, or null if input is null
     */
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
