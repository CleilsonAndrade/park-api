package br.com.cleilsonandrade.parkapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingResponseDTO {
  private Long id;

  private String code;

  private String status;
}
