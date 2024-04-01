package br.com.cleilsonandrade.parkapi.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import br.com.cleilsonandrade.parkapi.web.dto.PageableDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientPageableMapper {
  public static PageableDTO toDto(Page<?> page) {
    return new ModelMapper().map(page, PageableDTO.class);
  }
}
