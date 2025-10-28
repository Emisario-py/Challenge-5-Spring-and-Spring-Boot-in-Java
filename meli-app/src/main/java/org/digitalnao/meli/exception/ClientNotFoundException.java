package org.digitalnao.meli.exception;

/**
 * Custom runtime exception thrown when a requested client cannot be found in the system.
 * This exception is typically thrown during client retrieval operations when the
 * specified client ID does not exist in the database. It provides meaningful error
 * messages that include the client identifier or a custom message, facilitating
 * better error handling and user feedback in the application.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long id) {
        super("Client not found with id: " + id);
    }

    public ClientNotFoundException(String message) {
        super(message);
    }
}