package br.com.cleilsonandrade.parkapi.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.cleilsonandrade.parkapi.entity.Parking;
import br.com.cleilsonandrade.parkapi.service.ParkingService;
import br.com.cleilsonandrade.parkapi.web.dto.ParkingCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.ParkingResponseDTO;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.ParkingMapper;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parkings")
@RequiredArgsConstructor
public class ParkingController {
  private final ParkingService parkingService;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> create(@RequestBody @Valid ParkingCreateDTO dto) {
    Parking parking = ParkingMapper.toParking(dto);
    parkingService.create(parking);

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{code}")
        .buildAndExpand(parking.getCode())
        .toUri();

    return ResponseEntity.created(location).build();
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ParkingResponseDTO> getByCode(@PathVariable String code) {
    Parking parking = parkingService.findByCode(code);
    return ResponseEntity.ok(ParkingMapper.toDto(parking));
  }
}
