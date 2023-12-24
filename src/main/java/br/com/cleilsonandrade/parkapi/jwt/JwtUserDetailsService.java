package br.com.cleilsonandrade.parkapi.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.cleilsonandrade.parkapi.entity.User;
import br.com.cleilsonandrade.parkapi.service.UserService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.searchByUsername(username);
    return new JwtUserDetails(user);
  }

  public JwtToken getTokenAuthenticated(String username) {
    User.Role role = userService.searchRoleByUsername(username);
    return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
  }
}
