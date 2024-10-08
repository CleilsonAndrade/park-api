package br.com.cleilsonandrade.parkapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.exception.EntityNotFoundException;
import br.com.cleilsonandrade.parkapi.repository.ClientParkingRepository;
import br.com.cleilsonandrade.parkapi.repository.projection.ClientParkingProjection;
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
            "Receipt '%s' not found in the system or checkout already carried out".formatted(receipt)));
  }

  @Transactional(readOnly = true)
  public long getTotalTimesLotParkingComplete(String cpf) {
    return repository.countByClientCpfAndDateDepartureIsNotNull(cpf);
  }

  @Transactional(readOnly = true)
  public Page<ClientParkingProjection> searchAllByClientCpf(String cpf, Pageable pageable) {
    return repository.findAllByClientCpf(cpf, pageable);
  }

  @Transactional(readOnly = true)
  public Page<ClientParkingProjection> searchAllByClientCpf(Long id, Pageable pageable) {
    return repository.findAllByClientUserId(id, pageable);
  }
}
