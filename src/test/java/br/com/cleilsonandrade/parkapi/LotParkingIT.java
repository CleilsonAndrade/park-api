package br.com.cleilsonandrade.parkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.cleilsonandrade.parkapi.web.dto.LotParkingCreateDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/lot-parkings/lot-parkings-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/lot-parkings/lot-parkings-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class LotParkingIT {
  @Autowired
  WebTestClient testClient;

  @Test
  public void createCheckIn_WithDataValid_ReturnCreatedAndLocation() {
    LotParkingCreateDTO createDTO = LotParkingCreateDTO.builder()
        .plate("WER-1111")
        .brand("brand")
        .model("PALIO 1.0")
        .color("AZUL")
        .clientCpf("22381495037")
        .build();

    testClient
        .post()
        .uri("/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .bodyValue(createDTO)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists(HttpHeaders.LOCATION)
        .expectBody()
        .jsonPath("plate").isEqualTo("WER-1111")
        .jsonPath("brand").isEqualTo("brand")
        .jsonPath("model").isEqualTo("PALIO 1.0")
        .jsonPath("color").isEqualTo("AZUL1")
        .jsonPath("clientCpf").isEqualTo("22381495037")
        .jsonPath("receipt").exists()
        .jsonPath("parkingCode").exists()
        .jsonPath("dateEntry").exists();
  }

  @Test
  public void createCheckIn_WithRoleClient_ReturnErrorStatus403() {
    LotParkingCreateDTO createDTO = LotParkingCreateDTO.builder()
        .plate("WER-1111")
        .brand("brand")
        .model("PALIO 1.0")
        .color("AZUL")
        .clientCpf("22381495037")
        .build();

    testClient
        .post()
        .uri("/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "biaa@email.com", "123456"))
        .bodyValue(createDTO)
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .jsonPath("status").isEqualTo(403)
        .jsonPath("path").isEqualTo("/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
  }

  @Test
  public void createCheckIn_WithDataInvalid_ReturnErrorStatus422() {
    LotParkingCreateDTO createDTO = LotParkingCreateDTO.builder()
        .plate("")
        .brand("")
        .model("")
        .color("")
        .clientCpf("")
        .build();

    testClient
        .post()
        .uri("/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "biaa@email.com", "123456"))
        .bodyValue(createDTO)
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody()
        .jsonPath("status").isEqualTo(422)
        .jsonPath("path").isEqualTo("/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
  }
}
