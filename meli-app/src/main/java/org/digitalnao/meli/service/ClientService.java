package org.digitalnao.meli.service;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.dto.client.ClientResponse;
import org.digitalnao.meli.exception.ClientNotFoundException;
import org.digitalnao.meli.mapper.ClientMapper;
import org.digitalnao.meli.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer component managing all business logic for client operations.
 * Handles five main operations with specific implementation details:
 * <ul>
 *   <li>getAllClients() - Uses @Transactional(readOnly=true) for optimized reads,
 *       retrieves all clients via repository.findAll(), streams results, maps each
 *       to ClientResponse using ClientMapper, and collects into a List</li>
 *   <li>getClientById() - Queries repository.findById(), throws ClientNotFoundException
 *       if Optional is empty using orElseThrow(), and maps found entity to DTO</li>
 *   <li>createClient() - Validates ID uniqueness using repository.existsById(), throws
 *       RuntimeException if ID exists, calls repository.save(), and maps result</li>
 *   <li>updateClient() - Retrieves existing client or throws exception, updates four
 *       fields (clientName, email, address, age), persists via repository.save()</li>
 *   <li>deleteClient() - Validates existence using repository.existsById(), throws
 *       exception if not found, calls repository.deleteById() which cascades to orders</li>
 * </ul>
 *
 * Uses constructor injection via @RequiredArgsConstructor for ClientRepository dependency.
 * All mapper conversions use ClientMapper.toResponse() for consistent DTO transformation.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see ClientRepository
 * @see ClientMapper
 * @see ClientNotFoundException
 */

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    /**
     * Retrieves all clients from the database and converts them to DTOs.
     * @return List of ClientResponse DTOs containing all clients with their orders
     */
    @Transactional(readOnly = true)
    public List<ClientResponse> getAllClients() {
        return repository.findAll().stream()
                .map(ClientMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single client by ID and converts to DTO.
     * @param id the Long identifier of the client to retrieve
     * @return ClientResponse DTO containing client data and associated orders
     * @throws ClientNotFoundException if no client exists with the specified ID
     */
    public ClientResponse getClientById(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return ClientMapper.toResponse(client);
    }

    /**
     * Creates a new client in the database after validating ID uniqueness.
     * @param client the Client entity containing all information to persist
     * @return ClientResponse DTO of the newly created client
     * @throws RuntimeException if a client with the same ID already exists in the database
     */
    public ClientResponse createClient(Client client) {
        if (client.getId() != null && repository.existsById(client.getId())) {
            throw new RuntimeException("Client with ID " + client.getId() + " already exists.");
        }
        Client saved = repository.save(client);
        return ClientMapper.toResponse(saved);
    }

    /**
     * Updates an existing client's information in the database.
     * @param id the Long identifier of the client to update
     * @param clientDetails the Client entity containing new values for fields to update
     * @return ClientResponse DTO of the updated client
     * @throws ClientNotFoundException if no client exists with the specified ID
     */
    public ClientResponse updateClient(Long id, Client clientDetails) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        client.setClientName(clientDetails.getClientName());
        client.setEmail(clientDetails.getEmail());
        client.setAddress(clientDetails.getAddress());
        client.setAge(clientDetails.getAge());

        Client updated = repository.save(client);
        return ClientMapper.toResponse(updated);
    }

    /**
     * Deletes a client from the database after validating existence.
     * @param id the Long identifier of the client to delete
     * @throws ClientNotFoundException if no client exists with the specified ID
     */
    public void deleteClient(Long id) {
        if (!repository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
