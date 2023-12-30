package br.com.cleilsonandrade.parkapi.web.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
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
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Parking", description = "Contains all operations related to resources for registering, editing and reading a parking")
@RestController
@RequestMapping("/parkings")
@RequiredArgsConstructor
public class ParkingController {
  private final ParkingService parkingService;

  @Operation(summary = "Create a new parking", description = "Feature to create a new parking", security = @SecurityRequirement(name = "security"), responses = {
      @ApiResponse(responseCode = "201", description = "Resource created successfully", headers = @Header(name = HttpHeaders.LOCATION, description = "URL of the created resource")),
      @ApiResponse(responseCode = "403", description = "Resource not allowed for the profile 'CLIENT'", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "409", description = "Vacancy already registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
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

  @Operation(summary = "Localizar uma vaga", description = "Resource to return a parking using the code"
      + "Request requires use of a 'Bearer token'. Restricted access to Role='ADMIN'", security = @SecurityRequirement(name = "security"), responses = {
          @ApiResponse(responseCode = "200", description = "The parking data is returned using its code", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingResponseDTO.class))),
          @ApiResponse(responseCode = "404", description = "Parking not located", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
          @ApiResponse(responseCode = "403", description = "Resource not allowed for the profile 'CLIENT'", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      })
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ParkingResponseDTO> getByCode(@PathVariable String code) {
    Parking parking = parkingService.findByCode(code);
    return ResponseEntity.ok(ParkingMapper.toDto(parking));
  }
}
