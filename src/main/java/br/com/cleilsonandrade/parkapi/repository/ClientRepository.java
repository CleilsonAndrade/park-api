package br.com.cleilsonandrade.parkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cleilsonandrade.parkapi.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
