package br.com.cleilsonandrade.parkapi.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.Client;
import br.com.cleilsonandrade.parkapi.exception.CpfUniqueViolationException;
import br.com.cleilsonandrade.parkapi.exception.EntityNotFoundException;
import br.com.cleilsonandrade.parkapi.repository.ClientRepository;
import br.com.cleilsonandrade.parkapi.repository.projection.ClientProjection;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {
  private final ClientRepository clientRepository;

  @Transactional
  public Client create(Client client) {
    try {
      return clientRepository.save(client);
    } catch (DataIntegrityViolationException e) {
      throw new CpfUniqueViolationException(
          String.format("CPF '%s' cannot be registered, it already exists in the system", client.getCpf()));
    }
  }

  @Transactional(readOnly = true)
  public Client searchById(Long id) {
    return clientRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Client id=%s not found in the system".formatted(id)));
  }

  @Transactional(readOnly = true)
  public Page<ClientProjection> searchAll(Pageable pageable) {
    return clientRepository.searchAll(pageable);
  }

  @Transactional(readOnly = true)
  public Client searchByUserId(Long id) {
    return clientRepository.findByUserId(id);
  }

  @Transactional(readOnly = true)
  public Client searchByCpf(String cpf) {
    return clientRepository.findByCpf(cpf).orElseThrow(
        () -> new EntityNotFoundException("Customer with the CPF '%s' not found".formatted(cpf)));
  }

}
