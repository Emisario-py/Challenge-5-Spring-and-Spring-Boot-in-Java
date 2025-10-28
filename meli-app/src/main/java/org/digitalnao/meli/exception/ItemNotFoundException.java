package org.digitalnao.meli.exception;

/**
 * Custom runtime exception thrown when a requested item cannot be found in the system.
 * This exception is raised during item retrieval or update operations when the
 * specified item ID does not exist in the database. It provides descriptive error
 * messages containing the item identifier or a custom message to improve error
 * traceability and debugging.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(Long id) {
        super("Item not found with id: " + id);
    }

    public ItemNotFoundException(String message) {
        super(message);
    }
}