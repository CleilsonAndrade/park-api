package br.com.cleilsonandrade.parkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import br.com.cleilsonandrade.parkapi.web.dto.ClientPageableDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientPageableMapper {
  public static ClientPageableDTO toDto(Page page) {
    return new ModelMapper().map(page, ClientPageableDTO.class);
  }
}
