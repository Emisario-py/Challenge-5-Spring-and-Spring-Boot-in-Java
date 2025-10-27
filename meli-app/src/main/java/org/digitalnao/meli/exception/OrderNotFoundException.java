package org.digitalnao.meli.exception;

/**
 * Custom runtime exception thrown when a requested order cannot be found in the system.
 * This exception is triggered during order retrieval, update, or deletion operations
 * when the specified order ID does not exist in the database. It provides clear error
 * messages that include the order identifier or a custom message, enabling better
 * exception handling throughout the application.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Order not found with id: " + id);
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}