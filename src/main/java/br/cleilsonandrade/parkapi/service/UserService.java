package br.cleilsonandrade.parkapi.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cleilsonandrade.parkapi.entity.User;
import br.cleilsonandrade.parkapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public User save(User user) {
    return this.userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public User getById(Long id) {
    return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
  }

  @Transactional
  public User editPassword(Long id, String password) {
    User user = getById(id);
    user.setPassword(password);
    return user;
  }

}
