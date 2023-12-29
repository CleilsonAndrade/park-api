package br.com.cleilsonandrade.parkapi.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.service.LotParkingService;
import br.com.cleilsonandrade.parkapi.web.dto.LotParkingCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.LotParkingResponseDTO;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.ClientParkingMapper;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-lots")
public class LotParkingController {
  private final LotParkingService lotParkingService;

  @PostMapping("/check-in")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<LotParkingResponseDTO> checkIn(@RequestBody @Valid LotParkingCreateDTO dto) {
    ClientParking clientParking = ClientParkingMapper.toClientParking(dto);
    lotParkingService.checkIn(clientParking);

    LotParkingResponseDTO responseDTO = ClientParkingMapper.toDto(clientParking);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{receipt}")
        .buildAndExpand(clientParking.getReceipt())
        .toUri();

    return ResponseEntity.created(location).body(responseDTO);
  }

}
