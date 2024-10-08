package br.com.cleilsonandrade.parkapi.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cleilsonandrade.parkapi.entity.User;
import br.com.cleilsonandrade.parkapi.entity.User.Role;
import br.com.cleilsonandrade.parkapi.exception.EntityNotFoundException;
import br.com.cleilsonandrade.parkapi.exception.UsernameUniqueViolationException;
import br.com.cleilsonandrade.parkapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public User create(User user) {
    try {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      return this.userRepository.save(user);
    } catch (org.springframework.dao.DataIntegrityViolationException ex) {
      throw new UsernameUniqueViolationException(String.format("Username '%s' already registered", user.getUsername()));
    }
  }

  @Transactional(readOnly = true)
  public User getById(Long id) {
    return this.userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User id=%s not found".formatted(id)));
  }

  @Transactional
  public User editPassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
    if (!newPassword.equals(confirmPassword)) {
      throw new RuntimeException("New password is not the same as confirmation");
    }

    User findUser = getById(id);

    if (!passwordEncoder.matches(currentPassword, findUser.getPassword())) {
      throw new RuntimeException("The password entered does not match the registered one");
    }

    findUser.setPassword(passwordEncoder.encode(newPassword));

    return findUser;
  }

  @Transactional(readOnly = true)
  public List<User> getAll() {
    return this.userRepository.findAll();
  }

  @Transactional(readOnly = true)
  public User searchByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User %s not found".formatted(username)));
  }

  @Transactional(readOnly = true)
  public Role searchRoleByUsername(String username) {
    return userRepository.findRoleByUsername(username);
  }

}
