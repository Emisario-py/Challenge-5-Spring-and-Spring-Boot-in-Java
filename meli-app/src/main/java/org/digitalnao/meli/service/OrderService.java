package org.digitalnao.meli.service;

import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.domain.OrderItem;
import org.digitalnao.meli.dto.CreateOrderRequest;
import org.digitalnao.meli.dto.CreateOrderRequestItem;
import org.digitalnao.meli.dto.CreateOrderResponse;
import org.digitalnao.meli.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {
    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }



    /**
     * Creates a new order computing totals from the incoming request and persists it.
     */
    public CreateOrderResponse createOrder(CreateOrderRequest req) {
        Order order = new Order();
        order.setCustomerName(req.getCustomerName());
        order.setCustomerEmail(req.getCustomerEmail());


        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;


        for (CreateOrderRequestItem it : req.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(it.getProductId());
            item.setName(it.getName());
            item.setQuantity(it.getQuantity());
            item.setUnitPrice(it.getUnitPrice());
            BigDecimal lineTotal = it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
            item.setLineTotal(lineTotal);
            items.add(item);
            total = total.add(lineTotal);
        }


        order.setItems(items);
        order.setTotal(total);


        Order saved = orderRepository.save(order);


// map to response
        CreateOrderResponse res = new CreateOrderResponse();
        res.setId(saved.getId());
        res.setCustomerName(saved.getCustomerName());
        res.setCustomerEmail(saved.getCustomerEmail());
        res.setStatus(saved.getStatus());
        res.setTotal(saved.getTotal());
        res.setCreatedAt(saved.getCreatedAt());


        List<CreateOrderResponse.Item> respItems = saved.getItems().stream()
                .map(i -> new CreateOrderResponse.Item(
                        i.getId(), i.getProductId(), i.getName(), i.getQuantity(), i.getUnitPrice(), i.getLineTotal()
                )).toList();
        res.setItems(respItems);


        return res;
    }

    // Obtener todas las órdenes
    public List<CreateOrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Obtener una orden por ID
    public CreateOrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    // Mapeo reutilizable
    private CreateOrderResponse mapToResponse(Order saved) {
        CreateOrderResponse res = new CreateOrderResponse();
        res.setId(saved.getId());
        res.setCustomerName(saved.getCustomerName());
        res.setCustomerEmail(saved.getCustomerEmail());
        res.setStatus(saved.getStatus());
        res.setTotal(saved.getTotal());
        res.setCreatedAt(saved.getCreatedAt());
        res.setItems(saved.getItems().stream()
                .map(i -> new CreateOrderResponse.Item(
                        i.getId(), i.getProductId(), i.getName(), i.getQuantity(), i.getUnitPrice(), i.getLineTotal()
                )).toList());
        return res;
    }

    // Actualizar una orden existente
    public CreateOrderResponse updateOrder(Long id, CreateOrderRequest req) {
        // Buscar la orden existente
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        // Actualizar datos del cliente
        existing.setCustomerName(req.getCustomerName());
        existing.setCustomerEmail(req.getCustomerEmail());

        // Limpiar los ítems previos de la misma lista (sin reemplazarla)
        List<OrderItem> items = existing.getItems();
        items.clear(); // gracias a orphanRemoval=true, Hibernate eliminará los registros previos

        BigDecimal total = BigDecimal.ZERO;

        // Crear y agregar los nuevos ítems
        for (CreateOrderRequestItem it : req.getItems()) {
            OrderItem item = new OrderItem();
            item.setProductId(it.getProductId());
            item.setName(it.getName());
            item.setQuantity(it.getQuantity());
            item.setUnitPrice(it.getUnitPrice());

            BigDecimal lineTotal = it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
            item.setLineTotal(lineTotal);

            items.add(item);
            total = total.add(lineTotal);
        }

        // Actualizar el total de la orden
        existing.setTotal(total);

        // Guardar la orden actualizada
        Order saved = orderRepository.save(existing);

        // Retornar respuesta mapeada
        return mapToResponse(saved);
    }


    // Eliminar una orden por ID
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        orderRepository.deleteById(id);
    }

}


