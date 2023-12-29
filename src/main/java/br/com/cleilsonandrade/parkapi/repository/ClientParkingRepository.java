package br.com.cleilsonandrade.parkapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;

public interface ClientParkingRepository extends JpaRepository<ClientParking, Long> {

}
