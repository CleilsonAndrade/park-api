package br.com.cleilsonandrade.parkapi.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import br.com.cleilsonandrade.parkapi.web.dto.mapper.ClientMapper;
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

  @PostMapping
  @PreAuthorize("hasRole('CLIENT')")
  public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientCreateDTO dto,
      @AuthenticationPrincipal JwtUserDetails userDetails) {
    Client client = ClientMapper.toClient(dto);
    client.setUser(userService.getById(userDetails.getId()));
    clientService.create(client);
    return ResponseEntity.status(201).body(ClientMapper.toDto(client));
  }
}
