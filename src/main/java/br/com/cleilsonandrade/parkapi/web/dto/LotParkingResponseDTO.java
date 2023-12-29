package br.com.cleilsonandrade.parkapi.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotParkingResponseDTO {

  private String plate;

  private String brand;

  private String model;

  private String color;

  private String clientCpf;

  private String receipt;

  private LocalDateTime dateEntry;

  private LocalDateTime dateDeparture;

  private String parkingCode;

  private BigDecimal value;

  private BigDecimal discount;
}
