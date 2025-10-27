package org.digitalnao.meli.controller;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.dto.client.ClientResponse;
import org.digitalnao.meli.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * REST controller for managing client resources through HTTP endpoints.
 * Provides five main operations mapped to specific HTTP methods and paths:
 * <ul>
 *   <li>GET /api/clients - Retrieves all clients with their associated orders</li>
 *   <li>GET /api/clients/{id} - Retrieves a single client by ID</li>
 *   <li>POST /api/clients - Creates a new client with provided data</li>
 *   <li>PUT /api/clients/{id} - Updates an existing client's information</li>
 *   <li>DELETE /api/clients/{id} - Removes a client from the system</li>
 * </ul>
 *
 * All successful operations return HTTP 200 OK with ClientResponse DTOs, except
 * delete operations which return HTTP 204 No Content. The controller delegates
 * all business logic to ClientService and uses constructor-based dependency injection
 * through Lombok's @RequiredArgsConstructor annotation.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see ClientService
 * @see ClientResponse
 */

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    /**
     * Retrieves all clients from the database.
     * @return ResponseEntity containing List of ClientResponse with HTTP 200 OK status
     */
    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    /**
     * Retrieves a specific client by their unique identifier.
     * @param id the Long identifier of the client to retrieve from path variable
     * @return ResponseEntity containing ClientResponse with HTTP 200 OK status
     * @throws ClientNotFoundException if no client exists with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    /**
     * Creates a new client in the database.
     * @param client the Client entity from request body containing all client information
     * @return ResponseEntity containing the created ClientResponse with HTTP 200 OK status
     * @throws RuntimeException if a client with the same ID already exists in the database
     */
    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    /**
     * Updates an existing client's information in the database.
     * @param id the Long identifier of the client to update from path variable
     * @param clientDetails the Client entity from request body with updated information
     * @return ResponseEntity containing the updated ClientResponse with HTTP 200 OK status
     * @throws ClientNotFoundException if no client exists with the specified ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id,
            @RequestBody Client clientDetails) {
        return ResponseEntity.ok(clientService.updateClient(id, clientDetails));
    }

    /**
     * Deletes a client from the database.
     * @param id the Long identifier of the client to delete from path variable
     * @return ResponseEntity with no body and HTTP 204 No Content status
     * @throws ClientNotFoundException if no client exists with the specified ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
