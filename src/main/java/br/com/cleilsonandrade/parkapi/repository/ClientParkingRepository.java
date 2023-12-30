package br.com.cleilsonandrade.parkapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.repository.projection.ClientParkingProjection;

public interface ClientParkingRepository extends JpaRepository<ClientParking, Long> {

  Optional<ClientParking> findByReceiptAndDateDepartureIsNull(String receipt);

  long countByClientCpfAndDateDepartureIsNotNull(String cpf);

  Page<ClientParkingProjection> findAllByClientCpf(String cpf, Pageable pageable);

}
