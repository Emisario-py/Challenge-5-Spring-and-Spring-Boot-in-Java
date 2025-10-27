package org.digitalnao.meli.service;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Client;
import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.dto.order.CreateOrderRequest;
import org.digitalnao.meli.dto.order.OrderResponse;
import org.digitalnao.meli.exception.ClientNotFoundException;
import org.digitalnao.meli.exception.OrderNotFoundException;
import org.digitalnao.meli.mapper.OrderMapper;
import org.digitalnao.meli.repository.ClientRepository;
import org.digitalnao.meli.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer component managing all business logic for order operations.
 * Handles five main operations with complex orchestration:
 * <ul>
 *   <li>getAllOrders() - Uses @Transactional(readOnly=true), retrieves all orders via
 *       orderRepository.findAll(), streams results, maps each to OrderResponse using
 *       OrderMapper (which recursively maps items), collects into List</li>
 *   <li>getOrderById() - Queries orderRepository.findById(), throws RuntimeException
 *       (not OrderNotFoundException) if empty, maps to OrderResponse with items</li>
 *   <li>createOrder() - Validates order ID not null, checks uniqueness, verifies client
 *       exists, creates Order entity, maps items from CreateItemRequest DTOs establishing
 *       bidirectional relationships, persists entire graph, returns mapped DTO</li>
 *   <li>updateOrder() - Retrieves existing order, updates client if provided, replaces
 *       items collection (clears existing, adds new with relationships), calculates total,
 *       updates or preserves createdAt timestamp, persists changes</li>
 *   <li>deleteOrder() - Validates existence, calls orderRepository.deleteById() which
 *       cascades to items due to CascadeType.ALL configuration</li>
 * </ul>
 *
 * Uses constructor injection via @RequiredArgsConstructor for OrderRepository and
 * ClientRepository dependencies. Coordinates complex operations across multiple entities.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see OrderRepository
 * @see ClientRepository
 * @see OrderMapper
 * @see OrderNotFoundException
 * @see ClientNotFoundException
 */

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    /**
     * Retrieves all orders from the database and converts them to DTOs.
     * @return List of OrderResponse DTOs containing all orders with client IDs and items
     */
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single order by ID and converts to DTO.
     *
     * @param id the Long identifier of the order to retrieve
     * @return OrderResponse DTO containing order data, client ID, creation timestamp, and items
     * @throws RuntimeException if no order exists with the specified ID
     */
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return OrderMapper.toResponse(order);
    }

    /**
     * Creates a new order with associated client and items.
     * @param request the CreateOrderRequest containing order ID, client ID, and list of item DTOs
     * @return OrderResponse DTO of the newly created order with all items
     * @throws RuntimeException if order ID is null or order ID already exists in database
     * @throws ClientNotFoundException if no client exists with the specified client ID
     */
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        if (request.getId() == null) {
            throw new RuntimeException("Order ID must be provided manually.");
        }

        if (orderRepository.existsById(request.getId())) {
            throw new RuntimeException("Order with ID " + request.getId() + " already exists.");
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ClientNotFoundException(request.getClientId()));

        Order order = new Order();
        order.setId(request.getId());
        order.setClient(client);

        if (request.getItems() != null) {
            order.setItems(
                    request.getItems().stream().map(dto -> {
                        Item item = new Item();
                        item.setId(dto.getId());
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

    /**
     * Updates an existing order's information in the database.
     * @param id the Long identifier of the order to update
     * @param orderDetails the Order entity containing new values including client, items, and timestamp
     * @return OrderResponse DTO of the updated order with all current items
     * @throws OrderNotFoundException if no order exists with the specified ID
     */
    @Transactional
    public OrderResponse updateOrder(Long id, Order orderDetails) {
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (orderDetails.getClient() != null) {
            existing.setClient(orderDetails.getClient());
        }

        if (orderDetails.getItems() != null && !orderDetails.getItems().isEmpty()) {

            existing.getItems().clear();

            BigDecimal newTotal = BigDecimal.ZERO;

            for (Item newItem : orderDetails.getItems()) {
                newItem.setOrder(existing);
                existing.getItems().add(newItem);
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

    /**
     * Deletes an order from the database after validating existence.
     *
     * @param id the Long identifier of the order to delete
     * @throws OrderNotFoundException if no order exists with the specified ID
     */
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }
}
