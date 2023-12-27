package br.com.cleilsonandrade.parkapi.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDTO {
  @NotBlank
  @Size(min = 3, max = 100)
  private String name;

  @Size(min = 11, max = 11)
  @CPF
  private String cpf;
}
