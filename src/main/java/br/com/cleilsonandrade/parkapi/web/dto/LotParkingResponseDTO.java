package br.com.cleilsonandrade.parkapi.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LotParkingResponseDTO {

  private String plate;

  private String brand;

  private String model;

  private String color;

  private String clientCpf;

  private String receipt;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm: ss")
  private LocalDateTime dateEntry;

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm: ss")
  private LocalDateTime dateDeparture;

  private String parkingCode;

  private BigDecimal value;

  private BigDecimal discount;
}
