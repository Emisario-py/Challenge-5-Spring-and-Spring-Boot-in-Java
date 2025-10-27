package org.digitalnao.meli.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.domain.Order;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Item API Integration Tests")
class ItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Order testOrder;

    @BeforeEach
    void setup() {
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        clientRepository.deleteAll();

        // Crear cliente
        Client client = new Client();
        client.setId(1L);
        client.setClientName("Test Client");
        client.setAddress("Test Address");
        client.setAge(30);
        client.setEmail("test@example.com");
        client = clientRepository.save(client);

        // Crear orden
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setClient(client);
        testOrder.setCreatedAt(Instant.now());
        testOrder = orderRepository.save(testOrder);
    }

    @Test
    @DisplayName("GET /api/items - Debe obtener todos los items")
    void testGetAllItems() throws Exception {
        // Crear items de prueba
        createAndSaveItem(testOrder, 10L, "PROD-001", "Laptop");
        createAndSaveItem(testOrder, 20L, "PROD-002", "Mouse");

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Laptop", "Mouse")))
                .andExpect(jsonPath("$[*].productId", containsInAnyOrder("PROD-001", "PROD-002")));
    }

    @Test
    @DisplayName("GET /api/items/{id} - Debe obtener un item por ID")
    void testGetItemById() throws Exception {
        Item item = createAndSaveItem(testOrder, 100L, "PROD-100", "Laptop");

        mockMvc.perform(get("/api/items/{id}", item.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.productId").value("PROD-100"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.unitPrice").value(99.99))
                .andExpect(jsonPath("$.orderId").value(testOrder.getId()));
    }

    @Test
    @DisplayName("POST /api/items/order/{orderId} - Debe crear un item")
    void testCreateItem() throws Exception {
        Item newItem = new Item();
        newItem.setId(200L);
        newItem.setProductId("PROD-200");
        newItem.setName("Keyboard");
        newItem.setQuantity(1);
        newItem.setUnitPrice(new BigDecimal("49.99"));

        mockMvc.perform(post("/api/items/order/{orderId}", testOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.productId").value("PROD-200"))
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.unitPrice").value(49.99))
                .andExpect(jsonPath("$.orderId").value(testOrder.getId()));

        // Verificar en BD
        assertThat(itemRepository.findById(200L)).isPresent();
    }

    @Test
    @DisplayName("PUT /api/items/{id} - Debe actualizar un item")
    void testUpdateItem() throws Exception {
        Item item = createAndSaveItem(testOrder, 300L, "PROD-300", "Original Name");

        Item updateData = new Item();
        updateData.setProductId("PROD-300-UPD");
        updateData.setName("Updated Name");
        updateData.setQuantity(5);
        updateData.setUnitPrice(new BigDecimal("199.99"));

        mockMvc.perform(put("/api/items/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(300))
                .andExpect(jsonPath("$.productId").value("PROD-300-UPD"))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.quantity").value(5))
                .andExpect(jsonPath("$.unitPrice").value(199.99));

        // Verificar en BD
        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getName()).isEqualTo("Updated Name");
        assertThat(updatedItem.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("DELETE /api/items/{id} - Debe eliminar un item")
    void testDeleteItem() throws Exception {
        Item item = createAndSaveItem(testOrder, 400L, "PROD-400", "To Delete");
        Long itemId = item.getId();

        mockMvc.perform(delete("/api/items/{id}", itemId))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        assertThat(itemRepository.findById(itemId)).isEmpty();
    }


    // Helper method
    private Item createAndSaveItem(Order order, Long itemId, String productId, String name) {
        Item item = new Item();
        item.setId(itemId);
        item.setProductId(productId);
        item.setName(name);
        item.setQuantity(1);
        item.setUnitPrice(new BigDecimal("99.99"));
        item.setOrder(order);
        return itemRepository.save(item);
    }
}