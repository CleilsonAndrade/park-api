package br.cleilsonandrade.parkapi.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.cleilsonandrade.parkapi.entity.User;
import br.cleilsonandrade.parkapi.service.UserService;
import br.cleilsonandrade.parkapi.web.dto.UserCreateDTO;
import br.cleilsonandrade.parkapi.web.dto.UserPassDTO;
import br.cleilsonandrade.parkapi.web.dto.UserResponseDTO;
import br.cleilsonandrade.parkapi.web.dto.mapper.UserMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Users", description = "Contains all operations related to resources for registering, editing and reading a user")
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {
  private final UserService userService;

  @Operation(summary="Criar um novo usuário",description="Recurso para criar um novo usuário",responses={@ApiResponse(responseCode="201",description="Recurso criado com sucesso",content=@Content(mediaType="application/json",schema=@Schema(implementation=UserResponseDTO.class))
  })

  @PostMapping
  public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
    User newUser = this.userService.save(UserMapper.toUser(userCreateDTO));
    return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(newUser));
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id) {
    User user = this.userService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDTO(user));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updatedPassword(@PathVariable Long id, @Valid @RequestBody UserPassDTO userPassDTO) {
    this.userService.editPassword(id, userPassDTO.getCurrentPassword(), userPassDTO.getNewPassword(),
        userPassDTO.getConfirmPassword());
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAll() {
    List<User> users = this.userService.getAll();
    return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toListDTO(users));
  }
}
