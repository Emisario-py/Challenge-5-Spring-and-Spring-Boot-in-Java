package org.digitalnao.meli.controller;

import lombok.RequiredArgsConstructor;
import org.digitalnao.meli.domain.Item;
import org.digitalnao.meli.dto.item.ItemResponse;
import org.digitalnao.meli.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing item resources through HTTP endpoints.
 * Provides five main operations mapped to specific HTTP methods and paths:
 * <ul>
 *   <li>GET /api/items - Retrieves all items across all orders</li>
 *   <li>GET /api/items/{id} - Retrieves a single item by ID</li>
 *   <li>POST /api/items/order/{orderId} - Creates a new item linked to a specific order</li>
 *   <li>PUT /api/items/{id} - Updates an existing item's product details</li>
 *   <li>DELETE /api/items/{id} - Removes an item from its order</li>
 * </ul>
 *
 * All successful operations return HTTP 200 OK with ItemResponse DTOs, except
 * delete operations which return HTTP 204 No Content. The controller requires
 * an orderId when creating items to establish the item-order relationship.
 * Uses constructor-based dependency injection through Lombok's @RequiredArgsConstructor.
 *
 * @author Emiliano Osuna
 * @version 1.0
 * @see ItemService
 * @see ItemResponse
 */

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     * Retrieves all items from the database across all orders.
     * @return ResponseEntity containing List of ItemResponse with HTTP 200 OK status
     */
    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    /**
     * Retrieves a specific item by its unique identifier.
     * @param id the Long identifier of the item to retrieve from path variable
     * @return ResponseEntity containing ItemResponse with HTTP 200 OK status
     * @throws ItemNotFoundException if no item exists with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    /**
     * Creates a new item and associates it with an existing order.
     * @param item the Item entity from request body with product details and pricing
     * @param orderId the Long identifier of the order from path variable to associate the item with
     * @return ResponseEntity containing the created ItemResponse with HTTP 200 OK status
     * @throws OrderNotFoundException if no order exists with the specified orderId
     */
    @PostMapping("/order/{orderId}")
    public ResponseEntity<ItemResponse> createItem(
            @RequestBody Item item,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(itemService.createItem(item, orderId));
    }

    /**
     * Updates an existing item's product information in the database.
     * @param id the Long identifier of the item to update from path variable
     * @param itemDetails the Item entity from request body with updated information
     * @return ResponseEntity containing the updated ItemResponse with HTTP 200 OK status
     * @throws ItemNotFoundException if no item exists with the specified ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(
            @PathVariable Long id,
            @RequestBody Item itemDetails) {
        return ResponseEntity.ok(itemService.updateItem(id, itemDetails));
    }

    /**
     * Deletes an item from the database and its parent order.
     * @param id the Long identifier of the item to delete from path variable
     * @return ResponseEntity with no body and HTTP 204 No Content status
     * @throws ItemNotFoundException if no item exists with the specified ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
