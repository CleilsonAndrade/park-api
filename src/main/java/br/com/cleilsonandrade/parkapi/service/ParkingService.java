package br.com.cleilsonandrade.parkapi.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.Parking;
import br.com.cleilsonandrade.parkapi.exception.CodeUniqueViolationException;
import br.com.cleilsonandrade.parkapi.exception.EntityNotFoundException;
import br.com.cleilsonandrade.parkapi.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingService {
  private final ParkingRepository parkingRepository;

  @Transactional
  public Parking create(Parking parking) {
    try {
      return parkingRepository.save(parking);
    } catch (DataIntegrityViolationException e) {
      throw new CodeUniqueViolationException(String.format("Parking with code '%s' already exists", parking.getCode()));
    }
  }

  @Transactional(readOnly = true)
  public Parking findByCode(String code) {
    return parkingRepository.findByCode(code).orElseThrow(
        () -> new EntityNotFoundException(String.format("Parking with code '%s' not found", code)));
  }

  @Transactional(readOnly = true)
  public Parking searchByParkingAvailable() {
    return parkingRepository.findFirstByStatus(Parking.StatusParking.AVAILABLE).orElseThrow(
        () -> new EntityNotFoundException("No free parkings were found"));
  }
}
