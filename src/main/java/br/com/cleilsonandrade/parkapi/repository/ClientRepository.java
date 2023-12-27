package br.com.cleilsonandrade.parkapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.cleilsonandrade.parkapi.entity.Client;
import br.com.cleilsonandrade.parkapi.repository.projection.ClientProjection;

public interface ClientRepository extends JpaRepository<Client, Long> {
  @Query("SELECT c from Client c")
  Page<ClientProjection> searchAll(Pageable pageable);
}
