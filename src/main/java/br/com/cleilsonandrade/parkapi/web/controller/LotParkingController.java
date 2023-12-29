package br.com.cleilsonandrade.parkapi.web.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
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
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-lots")
public class LotParkingController {
  private final LotParkingService lotParkingService;

  @Operation(summary = "Create a new check-in in parking lot", description = "Resource for entering a vehicle into the parking lot"
      +
      "Request requires use of a 'Bearer token'. Restricted access to Role='ADMIN'", security = @SecurityRequirement(name = "security"), responses = {
          @ApiResponse(responseCode = "201", description = "Resource created successfully", headers = @Header(name = HttpHeaders.LOCATION, description = "URl access to the created resource"), content = @Content(mediaType = "application/json", schema = @Schema(implementation = LotParkingResponseDTO.class))),
          @ApiResponse(responseCode = "403", description = "Feature not allowed for profile ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
          @ApiResponse(responseCode = "404", description = "Possible causes: <br/>"
              + "- Customer CPF not registered in the system; </br>"
              + "- No free parkings were found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
          @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      })
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