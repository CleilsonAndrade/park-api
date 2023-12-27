package br.com.cleilsonandrade.parkapi.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.Client;
import br.com.cleilsonandrade.parkapi.exception.CpfUniqueViolationException;
import br.com.cleilsonandrade.parkapi.exception.EntityNotFoundException;
import br.com.cleilsonandrade.parkapi.repository.ClientRepository;
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
        () -> new EntityNotFoundException(String.format("Client id=%s not found in the system", id)));
  }

}
