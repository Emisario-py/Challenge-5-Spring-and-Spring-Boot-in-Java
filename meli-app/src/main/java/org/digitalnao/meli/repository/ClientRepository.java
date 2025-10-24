package org.digitalnao.meli.repository;

import org.digitalnao.meli.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long> { }