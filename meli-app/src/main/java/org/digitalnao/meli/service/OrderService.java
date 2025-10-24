package org.digitalnao.meli.service;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.dto.order.CreateOrderRequest;
import org.digitalnao.meli.dto.order.OrderResponse;
import org.digitalnao.meli.mapper.OrderMapper;
import org.digitalnao.meli.repository.ClientRepository;
import org.digitalnao.meli.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    // GET /api/orders
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    // GET /api/orders/{id}
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return OrderMapper.toResponse(order);
    }

    // POST /api/orders
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("Order ID must be provided manually.");
        }

        if (orderRepository.existsById(request.getId())) {
            throw new RuntimeException("Order with ID " + request.getId() + " already exists.");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + request.getClientId()));

        Order order = new Order();
        order.setId(request.getId()); // ✅ Copiamos el ID manualmente
        order.setClient(client);

        // Mapear los items
        if (request.getItems() != null) {
            order.setItems(
                    request.getItems().stream().map(dto -> {
                        Item item = new Item();
                        item.setId(dto.getId()); // ✅ si los manejas manualmente
                        item.setProductId(dto.getProductId());
                        item.setName(dto.getName());
                        item.setQuantity(dto.getQuantity());
                        item.setUnitPrice(dto.getUnitPrice());
                        item.setOrder(order);
                        return item;
                    }).collect(Collectors.toList())
            );
        }

        Order saved = orderRepository.save(order);

        return OrderMapper.toResponse(saved);
    }


    // PUT /api/orders/{id}
    @Transactional
    public OrderResponse updateOrder(Long id, Order orderDetails) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Actualizar cliente si se envía uno nuevo
        if (orderDetails.getClient() != null) {
            existing.setClient(orderDetails.getClient());
        }

        // Reemplazar items si mandan nuevos
        if (orderDetails.getItems() != null && !orderDetails.getItems().isEmpty()) {

            // Limpiar los actuales (orphanRemoval = true borra en cascada)
            existing.getItems().clear();

            // Inicializar nuevo total
            BigDecimal newTotal = BigDecimal.ZERO;

            // Agregar nuevos items a la orden
            for (Item newItem : orderDetails.getItems()) {
                newItem.setOrder(existing);
                existing.getItems().add(newItem); // ✅ Aquí está el paso que faltaba
                newTotal = newTotal.add(
                        newItem.getUnitPrice().multiply(BigDecimal.valueOf(newItem.getQuantity()))
                );
            }

        }

        existing.setCreatedAt(orderDetails.getCreatedAt() != null
                ? orderDetails.getCreatedAt()
                : existing.getCreatedAt());

        Order updated = orderRepository.save(existing);
        return OrderMapper.toResponse(updated);
    }


    // DELETE /api/orders/{id}
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
