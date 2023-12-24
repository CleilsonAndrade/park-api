package br.com.cleilsonandrade.parkapi.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import br.com.cleilsonandrade.parkapi.entity.User;
import br.com.cleilsonandrade.parkapi.web.dto.UserCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserResponseDTO;

public class UserMapper {
  public static User toUser(UserCreateDTO userCreateDTO) {
    return new ModelMapper().map(userCreateDTO, User.class);
  }

  public static UserResponseDTO toDTO(User user) {
    String role = user.getRole().name().substring("ROLE_".length());
    PropertyMap<User, UserResponseDTO> props = new PropertyMap<User, UserResponseDTO>() {
      @Override
      protected void configure() {
        map().setRole(role);
      }
    };

    ModelMapper mapper = new ModelMapper();
    mapper.addMappings((props));
    return mapper.map(user, UserResponseDTO.class);
  }

  public static List<UserResponseDTO> toListDTO(List<User> users) {
    return users.stream().map(user -> toDTO(user)).collect(Collectors.toList());
  }
}
