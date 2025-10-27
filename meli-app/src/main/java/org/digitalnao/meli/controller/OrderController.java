package org.digitalnao.meli.controller;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.dto.order.CreateOrderRequest;
import org.digitalnao.meli.dto.order.OrderResponse;
import org.digitalnao.meli.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing order resources through HTTP endpoints.
 * Provides five main operations mapped to specific HTTP methods and paths:
 * <ul>
 *   <li>GET /api/orders - Retrieves all orders with their items and client info</li>
 *   <li>GET /api/orders/{id} - Retrieves a single order with all details</li>
 *   <li>POST /api/orders - Creates a new order with client and items</li>
 *   <li>PUT /api/orders/{id} - Updates an existing order's data</li>
 *   <li>DELETE /api/orders/{id} - Removes an order and its items</li>
 * </ul>
 *
 * All successful operations return HTTP 200 OK with OrderResponse DTOs, except
 * delete operations which return HTTP 204 No Content. Order creation requires
 * a CreateOrderRequest which includes order ID, client ID, and items list.
 * Uses constructor-based dependency injection through Lombok's @RequiredArgsConstructor.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see OrderService
 * @see OrderResponse
 * @see CreateOrderRequest
 */

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Retrieves all orders from the database.
     * @return ResponseEntity containing List of OrderResponse with HTTP 200 OK status
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    /**
     * Retrieves a specific order by its unique identifier.
     * @param id the Long identifier of the order to retrieve from path variable
     * @return ResponseEntity containing OrderResponse with HTTP 200 OK status
     * @throws RuntimeException if no order exists with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * Creates a new order with associated client and items.
     * @param request the CreateOrderRequest from request body with order, client, and items data
     * @return ResponseEntity containing the created OrderResponse with HTTP 200 OK status
     * @throws RuntimeException if order ID is null or order ID already exists
     * @throws ClientNotFoundException if no client exists with the specified client ID
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    /**
     * Updates an existing order's information in the database.
     * @param id the Long identifier of the order to update from path variable
     * @param orderDetails the Order entity from request body with updated information
     * @return ResponseEntity containing the updated OrderResponse with HTTP 200 OK status
     * @throws OrderNotFoundException if no order exists with the specified ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long id,
            @RequestBody org.digitalnao.meli.domain.Order orderDetails
    ) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderDetails));
    }

    /**
     * Deletes an order from the database.
     * @param id the Long identifier of the order to delete from path variable
     * @return ResponseEntity with no body and HTTP 204 No Content status
     * @throws OrderNotFoundException if no order exists with the specified ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
