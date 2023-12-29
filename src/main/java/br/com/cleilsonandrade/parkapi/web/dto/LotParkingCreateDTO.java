package br.com.cleilsonandrade.parkapi.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotParkingCreateDTO {
  @NotBlank
  @Size(min = 8, max = 8)
  @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "The vehicle license plate must follow the standard 'XXX-0000'")
  private String plate;

  @NotBlank
  private String brand;

  @NotBlank
  private String model;

  @NotBlank
  private String color;

  @NotBlank
  @Size(min = 11, max = 11)
  @CPF
  private String clientCpf;
}
