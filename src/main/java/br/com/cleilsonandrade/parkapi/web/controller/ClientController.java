package br.com.cleilsonandrade.parkapi.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cleilsonandrade.parkapi.entity.Client;
import br.com.cleilsonandrade.parkapi.jwt.JwtUserDetails;
import br.com.cleilsonandrade.parkapi.service.ClientService;
import br.com.cleilsonandrade.parkapi.service.UserService;
import br.com.cleilsonandrade.parkapi.web.dto.ClientCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.ClientResponseDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserResponseDTO;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.ClientMapper;
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Client", description = "Contains all operations related to resources for registering, editing and reading a client")
@RequiredArgsConstructor
@RestController
@RequestMapping("/clients")
public class ClientController {
  private final ClientService clientService;

  private final UserService userService;

  @Operation(summary = "Create a new client", description = "Feature to create a new client, linked to an already registered user"
      +
      "Request requires use of a 'Bearer token'. Restricted access toRole='CLIENT'", responses = {
          @ApiResponse(responseCode = "201", description = "Resource created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
          @ApiResponse(responseCode = "403", description = "Feature not allowed for profile ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
          @ApiResponse(responseCode = "409", description = "CPF client is already registered in the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
          @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      })
  @PostMapping
  @PreAuthorize("hasRole('CLIENT')")
  public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientCreateDTO dto,
      @AuthenticationPrincipal JwtUserDetails userDetails) {
    Client client = ClientMapper.toClient(dto);
    client.setUser(userService.getById(userDetails.getId()));
    clientService.create(client);
    return ResponseEntity.status(201).body(ClientMapper.toDto(client));
  }

  @Operation(summary = "Localize client", description = "Feature to localize client, get by id"
      +
      "Request requires use of a 'Bearer token'. Restricted access toRole='ADMIN'", responses = {
          @ApiResponse(responseCode = "200", description = "Resource localized successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
          @ApiResponse(responseCode = "403", description = "Feature not allowed for profileCLIENT", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
          @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      })
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id) {
    Client client = clientService.searchById(id);
    return ResponseEntity.ok(ClientMapper.toDto(client));
  }
}
