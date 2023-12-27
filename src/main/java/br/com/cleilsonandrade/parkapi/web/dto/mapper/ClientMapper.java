package br.com.cleilsonandrade.parkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;

import br.com.cleilsonandrade.parkapi.entity.Client;
import br.com.cleilsonandrade.parkapi.web.dto.ClientCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.ClientResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {
  public static Client toClient(ClientCreateDTO dto) {
    return new ModelMapper().map(dto, Client.class);
  }

  public static ClientResponseDTO toDto(Client client) {
    return new ModelMapper().map(client, ClientResponseDTO.class);
  }
}
