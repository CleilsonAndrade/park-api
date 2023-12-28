package br.com.cleilsonandrade.parkapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.cleilsonandrade.parkapi.entity.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Long> {

  Optional<Parking> findByCode(String code);

}
