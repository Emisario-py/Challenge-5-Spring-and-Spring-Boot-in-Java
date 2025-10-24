package org.digitalnao.meli.service;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.dto.item.ItemResponse;
import org.digitalnao.meli.mapper.ItemMapper;
import org.digitalnao.meli.repository.ItemRepository;
import org.digitalnao.meli.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
        return ItemMapper.toResponse(item);
    }

    @Transactional
    public ItemResponse createItem(Item item, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        item.setOrder(order);
        Item saved = itemRepository.save(item);

        orderRepository.save(order);

        return ItemMapper.toResponse(saved);
    }

    @Transactional
    public ItemResponse updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        item.setProductId(itemDetails.getProductId());
        item.setName(itemDetails.getName());
        item.setQuantity(itemDetails.getQuantity());
        item.setUnitPrice(itemDetails.getUnitPrice());
        // ❌ ELIMINADO: lineTotal se calcula automáticamente con @PrePersist/@PreUpdate
        // item.setLineTotal(itemDetails.getLineTotal());

        Item updated = itemRepository.save(item);

        // ✅ Recalcular el total de la orden después de actualizar el item
        Order order = item.getOrder();
        orderRepository.save(order);

        return ItemMapper.toResponse(updated);
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));

        Order order = item.getOrder();

        itemRepository.deleteById(id);

        orderRepository.save(order);
    }
}