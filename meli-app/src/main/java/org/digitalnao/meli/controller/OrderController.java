package org.digitalnao.meli.controller;

import org.digitalnao.meli.dto.CreateOrderRequest;
import org.digitalnao.meli.dto.CreateOrderResponse;
import org.digitalnao.meli.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    /**
     * Create a new order.
     * @return HTTP 201 with the created order payload.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public List<CreateOrderResponse> getAll() {
        return orderService.getAllOrders();
    }

    // Obtener una orden específica por ID
    @GetMapping("/{id}")
    public CreateOrderResponse getById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    // Actualizar una orden existente (solo nombre, email o ítems)
    @PutMapping("/{id}")
    public CreateOrderResponse updateOrder(@PathVariable Long id, @Valid @RequestBody CreateOrderRequest request) {
        return orderService.updateOrder(id, request);
    }

    // Eliminar una orden por ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }
}
