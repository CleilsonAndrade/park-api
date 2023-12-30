package br.com.cleilsonandrade.parkapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.exception.EntityNotFoundException;
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

  @Transactional(readOnly = true)
  public ClientParking searchByReceipt(String receipt) {
    return repository.findByReceiptAndDateDepartureIsNull(receipt).orElseThrow(
        () -> new EntityNotFoundException(
            String.format("Receipt '%s' not found in the system or checkout already carried out", receipt)));
  }

  @Transactional(readOnly = true)
  public long getTotalTimesLotParkingComplete(String cpf) {
    return repository.countByClientCpfAndDateDepartureIsNotNull(cpf);
  }
}
