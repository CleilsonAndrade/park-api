package br.com.cleilsonandrade.parkapi.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import br.com.cleilsonandrade.parkapi.repository.projection.ClientProjection;
import br.com.cleilsonandrade.parkapi.service.ClientService;
import br.com.cleilsonandrade.parkapi.service.UserService;
import br.com.cleilsonandrade.parkapi.web.dto.ClientCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.ClientResponseDTO;
import br.com.cleilsonandrade.parkapi.web.dto.PageableDTO;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.ClientMapper;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.ClientPageableMapper;
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
      "Request requires use of a 'Bearer token'. Restricted access to Role='CLIENT'", security = @SecurityRequirement(name = "security"), responses = {
          @ApiResponse(responseCode = "201", description = "Resource created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
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

  @Operation(summary = "Localize client", description = "Feature to localize client, get by id. "
      +
      "Request requires use of a 'Bearer token'. Restricted access to Role='ADMIN'", parameters = {
          @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the user to be retrieved in Integer format", required = true)
      }, security = @SecurityRequirement(name = "security"), responses = {
          @ApiResponse(responseCode = "200", description = "Resource localized successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
          @ApiResponse(responseCode = "403", description = "Feature not allowed for profile CLIENT", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
          @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      })
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id) {
    Client client = clientService.searchById(id);
    return ResponseEntity.ok(ClientMapper.toDto(client));
  }

  @Operation(summary = "Retrieve clients list", description = "Request requires use of 'Bearer token'. Restricted access to Role='ADMIN'", security = @SecurityRequirement(name = "security"), parameters = {
      @Parameter(in = ParameterIn.QUERY, name = "page", content = @Content(schema = @Schema(type = "integer", defaultValue = "0")), description = "Represents page returned"),
      @Parameter(in = ParameterIn.QUERY, name = "size", content = @Content(schema = @Schema(type = "integer", defaultValue = "20")), description = "Represents the total number of elements per page"),
      @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true, array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")), description = "Represents the ordering of results. Accepts multiple sorting criteria are supported"),
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Resource localized successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
      @ApiResponse(responseCode = "403", description = "Feature not allowed for profile CLIENT", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @GetMapping()
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<PageableDTO> getAll(
      @Parameter(hidden = true) @PageableDefault(size = 5, sort = { "name" }) Pageable pageable) {
    Page<ClientProjection> clients = clientService.searchAll(pageable);
    return ResponseEntity.ok(ClientPageableMapper.toDto(clients));
  }

  @Operation(summary = "Retrieve authenticated client data", description = "Request requires use of a 'Bearer token'. Restricted access to Role='CLIENT'", security = @SecurityRequirement(name = "security"), responses = {
      @ApiResponse(responseCode = "200", description = "Resource localized successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientResponseDTO.class))),
      @ApiResponse(responseCode = "403", description = "Feature not allowed for profile ADMIN", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
  })
  @GetMapping("/details")
  @PreAuthorize("hasRole('CLIENT')")
  public ResponseEntity<ClientResponseDTO> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
    Client client = clientService.searchByUserId(userDetails.getId());
    return ResponseEntity.ok(ClientMapper.toDto(client));
  }
}
