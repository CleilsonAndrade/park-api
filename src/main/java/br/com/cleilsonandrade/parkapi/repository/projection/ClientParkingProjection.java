package br.com.cleilsonandrade.parkapi.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClientParkingProjection {
  String getPlate();

  String getBrand();

  String getModel();

  String getColor();

  String getClientCpf();

  String getReceipt();

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm: ss")
  LocalDateTime getDateEntry();

  @JsonFormat(pattern = "yyyy-MM-dd hh:mm: ss")
  LocalDateTime getDateDeparture();

  String getParkingCode();

  BigDecimal getValue();

  BigDecimal getDiscount();

}
