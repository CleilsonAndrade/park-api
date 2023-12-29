package br.com.cleilsonandrade.parkapi.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.Client;
import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.entity.Parking;
import br.com.cleilsonandrade.parkapi.util.LotParkingUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LotParkingService {
  private final ClientParkingService clientParkingService;

  private final ClientService clientService;

  private final ParkingService parkingService;

  @Transactional
  public ClientParking checkIn(ClientParking clientParking) {
    Client client = clientService.searchByCpf(clientParking.getClient().getCpf());
    clientParking.setClient(client);

    Parking parking = parkingService.searchByParkingAvailable();

    parking.setStatus(Parking.StatusParking.BUSY);

    clientParking.setParking(parking);
    clientParking.setDateEntry(LocalDateTime.now());
    clientParking.setReceipt(LotParkingUtils.generatedReceipt());

    return clientParkingService.create(clientParking);
  }
}
