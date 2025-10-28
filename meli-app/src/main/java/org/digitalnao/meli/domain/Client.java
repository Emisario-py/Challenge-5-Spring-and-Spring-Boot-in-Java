package org.digitalnao.meli.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a client in the system.
 * This domain model stores client information including personal details
 * such as name, address, age, and email. Each client can have multiple
 * orders associated with them through a one-to-many relationship.
 * The entity is mapped to the "clients" table in the database.
 *
 * @author Emiliano Osuna
 * @version 1.0
 */

@Entity
@Table(name = "clients")
@Data
public class Client {

    @Id
    @Column(name = "id_client")
    private Long id;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();
}