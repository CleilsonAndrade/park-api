package br.com.cleilsonandrade.parkapi.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cleilsonandrade.parkapi.entity.User;
import br.com.cleilsonandrade.parkapi.service.UserService;
import br.com.cleilsonandrade.parkapi.web.dto.UserCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserPassDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserResponseDTO;
import br.com.cleilsonandrade.parkapi.web.dto.mapper.UserMapper;
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

@Tag(name = "Users", description = "Contains all operations related to resources for registering, editing and reading a user")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @Operation(summary = "Create a new user", description = "Feature to create a new user", responses = {
      @ApiResponse(responseCode = "201", description = "Resource created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "409", description = "Email user already registered in the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
    User newUser = this.userService.create(UserMapper.toUser(userCreateDTO));
    return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(newUser));
  }

  @Operation(summary = "Retrieve a user by ID", description = "Feature to retrieve a user by ID, restricted access to 'ADMIN' or 'CLIENT'", security = @SecurityRequirement(name = "security"), parameters = {
      @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the user to be retrieved in Integer format", required = true)
  }, responses = {
      @ApiResponse(responseCode = "200", description = "Resource retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
      @ApiResponse(responseCode = "403", description = "User without permission to access this resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENT') AND #id == authentication.principal.id)")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
    User user = this.userService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDTO(user));
  }

  @Operation(summary = "Update password", description = "Feature to update password, restricted access to 'ADMIN' or 'CLIENT'", security = @SecurityRequirement(name = "security"), parameters = {
      @Parameter(in = ParameterIn.PATH, name = "id", description = "ID of the user to be retrieved in Integer format", required = true)
  }, responses = {
      @ApiResponse(responseCode = "204", description = "Password updated successfully"),
      @ApiResponse(responseCode = "400", description = "Password does not match", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "403", description = "User without permission to access this resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
      @ApiResponse(responseCode = "422", description = "Invalid or poorly formatted fields", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT') AND (#id == authentication.principal.id)")
  public ResponseEntity<Void> updatedPassword(@PathVariable Long id, @Valid @RequestBody UserPassDTO userPassDTO) {
    this.userService.editPassword(id, userPassDTO.getCurrentPassword(), userPassDTO.getNewPassword(),
        userPassDTO.getConfirmPassword());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "List all users", security = @SecurityRequirement(name = "security"), description = "Feature to list all registered users, restricted access to 'ADMIN'", responses = {
      @ApiResponse(responseCode = "200", description = "List of all registered users", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))),
      @ApiResponse(responseCode = "403", description = "User without permission to access this resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
  })
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponseDTO>> getAll() {
    List<User> users = this.userService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toListDTO(users));
  }
}
