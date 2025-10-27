package org.digitalnao.meli.service;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.domain.Order;
import org.digitalnao.meli.dto.item.ItemResponse;
import org.digitalnao.meli.exception.ItemNotFoundException;
import org.digitalnao.meli.exception.OrderNotFoundException;
import org.digitalnao.meli.mapper.ItemMapper;
import org.digitalnao.meli.repository.ItemRepository;
import org.digitalnao.meli.repository.OrderRepository;
import org.hibernate.metamodel.mapping.ordering.ast.OrderByComplianceViolation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer component managing all business logic for item operations.
 * Handles five main operations with specific implementation details:
 * <ul>
 *   <li>getAllItems() - Retrieves all items via itemRepository.findAll(), streams results,
 *       maps each to ItemResponse using ItemMapper, and collects into a List</li>
 *   <li>getItemById() - Queries itemRepository.findById(), throws ItemNotFoundException
 *       if Optional is empty, and maps found entity to DTO</li>
 *   <li>createItem() - Validates order existence using orderRepository.findById(), sets
 *       bidirectional relationship with item.setOrder(), saves item, saves order for
 *       consistency, and returns mapped DTO. Annotated @Transactional for atomicity</li>
 *   <li>updateItem() - Retrieves existing item or throws exception, updates four fields
 *       (productId, name, quantity, unitPrice), saves item, saves parent order, returns DTO</li>
 *   <li>deleteItem() - Retrieves item or throws exception, gets order reference, calls
 *       itemRepository.deleteById(), saves order for consistency. All @Transactional</li>
 * </ul>
 *
 * Uses constructor injection via @RequiredArgsConstructor for ItemRepository and OrderRepository.
 * Coordinates between items and orders to maintain referential integrity.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see ItemRepository
 * @see OrderRepository
 * @see ItemMapper
 * @see ItemNotFoundException
 * @see OrderNotFoundException
 */

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    /**
     * Retrieves all items from the database and converts them to DTOs.
     * @return List of ItemResponse DTOs containing all items with their order references
     */
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single item by ID and converts to DTO.
     * @param id the Long identifier of the item to retrieve
     * @return ItemResponse DTO containing item data and parent order ID
     * @throws ItemNotFoundException if no item exists with the specified ID
     */
    public ItemResponse getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        return ItemMapper.toResponse(item);
    }

    /**
     * Creates a new item and associates it with an existing order.
     * @param item the Item entity containing product information, quantity, and pricing
     * @param orderId the Long identifier of the order to associate the item with
     * @return ItemResponse DTO of the newly created item with order reference
     * @throws OrderNotFoundException if no order exists with the specified orderId
     */
    @Transactional
    public ItemResponse createItem(Item item, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        item.setOrder(order);
        Item saved = itemRepository.save(item);

        orderRepository.save(order);

        return ItemMapper.toResponse(saved);
    }

    /**
     * Updates an existing item's product information in the database.
     * @param id the Long identifier of the item to update
     * @param itemDetails the Item entity containing new values for fields to update
     * @return ItemResponse DTO of the updated item with order reference
     * @throws ItemNotFoundException if no item exists with the specified ID
     */
    @Transactional
    public ItemResponse updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        item.setProductId(itemDetails.getProductId());
        item.setName(itemDetails.getName());
        item.setQuantity(itemDetails.getQuantity());
        item.setUnitPrice(itemDetails.getUnitPrice());

        Item updated = itemRepository.save(item);

        Order order = item.getOrder();
        orderRepository.save(order);

        return ItemMapper.toResponse(updated);
    }

    /**
     * Deletes an item from the database and updates the parent order.
     * @param id the Long identifier of the item to delete
     * @throws ItemNotFoundException if no item exists with the specified ID
     */
    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        Order order = item.getOrder();

        itemRepository.deleteById(id);

        orderRepository.save(order);
    }
}