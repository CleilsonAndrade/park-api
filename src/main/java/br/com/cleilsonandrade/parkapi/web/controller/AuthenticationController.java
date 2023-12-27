package br.com.cleilsonandrade.parkapi.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cleilsonandrade.parkapi.jwt.JwtToken;
import br.com.cleilsonandrade.parkapi.jwt.JwtUserDetailsService;
import br.com.cleilsonandrade.parkapi.web.dto.UserLoginDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserResponseDTO;
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Authentication", description = "Contains all operations related to resources for authentication")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
  private final JwtUserDetailsService detailsService;
  private final AuthenticationManager authenticationManager;

  @Operation(summary = "Authenticate to API", description = "Authentication feature", responses = {
      @ApiResponse(responseCode = "200", description = "Authentication carried out successfully and return 'Bearer token'", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Disabled by credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Invalid field(s)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @PostMapping
  public ResponseEntity<?> authenticate(@RequestBody @Valid UserLoginDTO dto, HttpServlet request) {
    log.info("Login authentication process {}", dto.getUsername());

    try {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          dto.getUsername(), dto.getPassword());

      authenticationManager.authenticate(authenticationToken);
      JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());

      return ResponseEntity.ok(token);

    } catch (AuthenticationException e) {
      log.warn("Bad credentials from username '{}'", dto.getUsername());
    }

    return ResponseEntity.badRequest()
        .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credentials Invalid"));

  }
}
