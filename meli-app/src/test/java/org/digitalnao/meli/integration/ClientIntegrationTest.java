package org.digitalnao.meli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.repository.ClientRepository;
import org.digitalnao.meli.repository.ItemRepository;
import org.digitalnao.meli.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Client API Integration Tests")
class ClientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/clients - Debe obtener todos los clientes")
    void testGetAllClients() throws Exception {
        // Crear clientes de prueba
        Client client1 = createAndSaveClient("Juan Pérez", "juan@example.com");
        Client client2 = createAndSaveClient("María López", "maria@example.com");

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].clientName", containsInAnyOrder("Juan Pérez", "María López")));
    }

    @Test
    @DisplayName("POST /api/clients - Debe crear un cliente correctamente")
    void testCreateClient() throws Exception {
        Client newClient = new Client();
        newClient.setId(100L);
        newClient.setClientName("Nuevo Cliente");
        newClient.setAddress("Calle Nueva 456");
        newClient.setAge(25);
        newClient.setEmail("nuevo@example.com");

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.clientName").value("Nuevo Cliente"))
                .andExpect(jsonPath("$.email").value("nuevo@example.com"));

        assertThat(clientRepository.findById(100L)).isPresent();
    }

    @Test
    @DisplayName("GET /api/clients/{id} - Debe obtener un cliente por ID")
    void testGetClientById() throws Exception {
        Client client = createAndSaveClient("Test Client", "test@example.com");

        mockMvc.perform(get("/api/clients/{id}", client.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(client.getId()))
                .andExpect(jsonPath("$.clientName").value("Test Client"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    @DisplayName("GET /api/clients/{id} - Debe retornar error cuando no existe")
    void testGetClientByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/clients/{id}", 9999L))
                .andExpect(status().is5xxServerError());
    }


    @Test
    @DisplayName("PUT /api/clients/{id} - Debe actualizar un cliente")
    void testUpdateClient() throws Exception {
        Client client = createAndSaveClient("Original Name", "original@example.com");

        Client updateData = new Client();
        updateData.setClientName("Updated Name");
        updateData.setAddress("New Address 789");
        updateData.setAge(35);
        updateData.setEmail("updated@example.com");

        mockMvc.perform(put("/api/clients/{id}", client.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.age").value(35));

        Client updatedClient = clientRepository.findById(client.getId()).orElseThrow();
        assertThat(updatedClient.getClientName()).isEqualTo("Updated Name");
    }

    @Test
    @DisplayName("DELETE /api/clients/{id} - Debe eliminar un cliente")
    void testDeleteClient() throws Exception {
        Client client = createAndSaveClient("To Delete", "delete@example.com");
        Long clientId = client.getId();

        mockMvc.perform(delete("/api/clients/{id}", clientId))
                .andExpect(status().isNoContent());

        assertThat(clientRepository.findById(clientId)).isEmpty();
    }

    private Client createAndSaveClient(String name, String email) {
        Client client = new Client();
        client.setId(System.currentTimeMillis());
        client.setClientName(name);
        client.setAddress("Test Address 123");
        client.setAge(30);
        client.setEmail(email);
        return clientRepository.save(client);
    }
}