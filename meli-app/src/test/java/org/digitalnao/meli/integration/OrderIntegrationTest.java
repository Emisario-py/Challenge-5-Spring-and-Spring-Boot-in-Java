package org.digitalnao.meli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.dto.item.CreateItemRequest;
import org.digitalnao.meli.dto.order.CreateOrderRequest;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Order API Integration Tests")
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Client testClient;

    @BeforeEach
    void setup() {
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        clientRepository.deleteAll();

        // Crear cliente de prueba
        testClient = new Client();
        testClient.setId(1L);
        testClient.setClientName("Test Client");
        testClient.setAddress("Test Address");
        testClient.setAge(30);
        testClient.setEmail("test@example.com");
        testClient = clientRepository.save(testClient);
    }

    @Test
    @DisplayName("GET /api/orders - Debe obtener todas las órdenes")
    void testGetAllOrders() throws Exception {

        createAndSaveOrder(testClient, 1L);
        createAndSaveOrder(testClient, 2L);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("GET /api/orders/{id} - Debe obtener una orden por ID")
    void testGetOrderById() throws Exception {
        Order order = createAndSaveOrder(testClient, 10L);

        mockMvc.perform(get("/api/orders/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.clientId").value(testClient.getId()));
    }

    @Test
    @DisplayName("POST /api/orders - Debe crear una orden con items")
    void testCreateOrder() throws Exception {

        CreateItemRequest item1 = new CreateItemRequest();
        item1.setId(1001L);
        item1.setProductId("PROD-001");
        item1.setName("Laptop");
        item1.setQuantity(1);
        item1.setUnitPrice(new BigDecimal("999.99"));

        CreateItemRequest item2 = new CreateItemRequest();
        item2.setId(1002L);
        item2.setProductId("PROD-002");
        item2.setName("Mouse");
        item2.setQuantity(2);
        item2.setUnitPrice(new BigDecimal("29.99"));

        CreateOrderRequest request = new CreateOrderRequest();
        request.setId(200L);
        request.setClientId(testClient.getId());
        request.setItems(Arrays.asList(item1, item2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.clientId").value(testClient.getId()))
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items[*].productId", containsInAnyOrder("PROD-001", "PROD-002")));
    }

    @Test
    @DisplayName("PUT /api/orders/{id} - Debe actualizar una orden")
    void testUpdateOrder() throws Exception {
        Order order = createAndSaveOrder(testClient, 20L);

        Order updateData = new Order();
        updateData.setCreatedAt(Instant.now());

        mockMvc.perform(put("/api/orders/{id}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()));
    }

    @Test
    @DisplayName("DELETE /api/orders/{id} - Debe eliminar una orden")
    void testDeleteOrder() throws Exception {
        Order order = createAndSaveOrder(testClient, 30L);
        Long orderId = order.getId();

        mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isNoContent());

        assertThat(orderRepository.findById(orderId)).isEmpty();
    }


    private Order createAndSaveOrder(Client client, Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setClient(client);
        order.setCreatedAt(Instant.now());
        return orderRepository.save(order);
    }
}