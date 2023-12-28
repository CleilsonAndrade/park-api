package br.com.cleilsonandrade.parkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;

import br.com.cleilsonandrade.parkapi.entity.Parking;
import br.com.cleilsonandrade.parkapi.web.dto.ParkingCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.ParkingResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingMapper {
  public static Parking toParking(ParkingCreateDTO dto) {
    return new ModelMapper().map(dto, Parking.class);
  }

  public static ParkingResponseDTO toDto(Parking parking) {
    return new ModelMapper().map(parking, ParkingResponseDTO.class);
  }
}
