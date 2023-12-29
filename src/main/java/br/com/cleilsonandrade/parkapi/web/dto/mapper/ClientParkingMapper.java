package br.com.cleilsonandrade.parkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;

import br.com.cleilsonandrade.parkapi.entity.ClientParking;
import br.com.cleilsonandrade.parkapi.web.dto.LotParkingCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.LotParkingResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientParkingMapper {
  public static ClientParking toClientParking(LotParkingCreateDTO dto) {
    return new ModelMapper().map(dto, ClientParking.class);
  }

  public static LotParkingResponseDTO toDto(ClientParking clientParking) {
    return new ModelMapper().map(clientParking, LotParkingResponseDTO.class);
  }
}
