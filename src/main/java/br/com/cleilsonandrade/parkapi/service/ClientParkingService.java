package br.com.cleilsonandrade.parkapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.repository.ClientParkingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientParkingService {
  private final ClientParkingRepository repository;

  @Transactional
  public ClientParking create(ClientParking clientParking) {
    return repository.save(clientParking);
  }
}
