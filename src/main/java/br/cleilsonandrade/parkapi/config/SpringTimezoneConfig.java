package br.cleilsonandrade.parkapi.config;

import java.util.TimeZone;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class SpringTimezoneConfig {

  @PostConstruct
  public void timezone() {
    TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
  }
}
