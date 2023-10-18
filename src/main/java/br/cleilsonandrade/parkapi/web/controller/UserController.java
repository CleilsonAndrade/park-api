package br.cleilsonandrade.parkapi.web.controller;

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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("usuarios")
public class UserController {
  private final UserService userService;

  @PostMapping
  public ResponseEntity<User> create(@RequestBody UserCreateDTO createDTO) {
    User newUser = userService.save(createDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getById(@PathVariable Long id) {
    User user = userService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<User> updatedPassword(@PathVariable Long id, @RequestBody User user) {
    User findUser = userService.getById(id);
    return ResponseEntity.status(HttpStatus.OK).body(findUser);
  }
}
