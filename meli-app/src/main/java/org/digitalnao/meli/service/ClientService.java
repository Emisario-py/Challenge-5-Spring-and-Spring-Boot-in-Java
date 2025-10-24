package org.digitalnao.meli.service;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.dto.client.ClientResponse;
import org.digitalnao.meli.mapper.ClientMapper;
import org.digitalnao.meli.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;

    @Transactional(readOnly = true)
    public List<ClientResponse> getAllClients() {
        return repository.findAll().stream()
                .map(ClientMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse getClientById(Long id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return ClientMapper.toResponse(client);
    }

    public ClientResponse createClient(Client client) {
        if (client.getId() != null && repository.existsById(client.getId())) {
            throw new RuntimeException("Client with ID " + client.getId() + " already exists.");
        }
        Client saved = repository.save(client);
        return ClientMapper.toResponse(saved);
    }

    public ClientResponse updateClient(Long id, Client clientDetails) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setClientName(clientDetails.getClientName());
        client.setEmail(clientDetails.getEmail());
        client.setAddress(clientDetails.getAddress());
        client.setAge(clientDetails.getAge());

        Client updated = repository.save(client);
        return ClientMapper.toResponse(updated);
    }

    public void deleteClient(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Client not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
