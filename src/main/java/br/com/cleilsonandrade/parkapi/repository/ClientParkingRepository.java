package br.com.cleilsonandrade.parkapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;

public interface ClientParkingRepository extends JpaRepository<ClientParking, Long> {

  Optional<ClientParking> findByReceiptAndDateDepartureIsNull(String receipt);

}
