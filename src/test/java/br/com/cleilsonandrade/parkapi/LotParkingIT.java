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
        .jsonPath("brand").isEqualTo("FIAT")
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
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
        .bodyValue(createDTO)
        .exchange()
        .expectStatus().isForbidden()
        .expectBody()
        .jsonPath("status").isEqualTo(403)
        .jsonPath("path").isEqualTo("/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
  }

  @Test
  public void createCheckIn_WithCpfNonexistent_ReturnErrorStatus404() {
    LotParkingCreateDTO createDTO = LotParkingCreateDTO.builder()
        .plate("WER-1111")
        .brand("brand")
        .model("PALIO 1.0")
        .color("AZUL")
        .clientCpf("03394550040")
        .build();

    testClient
        .post()
        .uri("/parking-lots/check-in")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .bodyValue(createDTO)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody()
        .jsonPath("status").isEqualTo(404)
        .jsonPath("path").isEqualTo("/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
  }

  @Test
  @Sql(scripts = "/sql/lot-parkings/lot-parkings-parking-busy-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/sql/lot-parkings/lot-parkings-parking-busy-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void createCheckIn_WithParkingBusy_ReturnErrorStatus404() {
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
        .expectStatus().isNotFound()
        .expectBody()
        .jsonPath("status").isEqualTo(404)
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
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
        .bodyValue(createDTO)
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody()
        .jsonPath("status").isEqualTo(422)
        .jsonPath("path").isEqualTo("/parking-lots/check-in")
        .jsonPath("method").isEqualTo("POST");
  }

  @Test
  public void searchCheckIn_WithRoleAdmin_ReturnDataWithStatus200() {
    testClient
        .get()
        .uri("/parking-lots/check-in/{receipt}", "20230313-101300")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("plate").isEqualTo("FIT-1020")
        .jsonPath("brand").isEqualTo("FIAT")
        .jsonPath("model").isEqualTo("PALIO")
        .jsonPath("color").isEqualTo("VERDE")
        .jsonPath("clientCpf").isEqualTo("24122251095")
        .jsonPath("receipt").isEqualTo("20230313-101300")
        .jsonPath("parkingCode").isEqualTo("A-01")
        .jsonPath("dateEntry").isEqualTo("2023-03-13 10:15:00");
  }

  @Test
  public void searchCheckIn_WithRoleClient_ReturnDataWithStatus200() {
    testClient
        .get()
        .uri("/parking-lots/check-in/{receipt}", "20230313-101300")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("plate").isEqualTo("FIT-1020")
        .jsonPath("brand").isEqualTo("FIAT")
        .jsonPath("model").isEqualTo("PALIO")
        .jsonPath("color").isEqualTo("VERDE")
        .jsonPath("clientCpf").isEqualTo("24122251095")
        .jsonPath("receipt").isEqualTo("20230313-101300")
        .jsonPath("parkingCode").isEqualTo("A-01")
        .jsonPath("dateEntry").isEqualTo("2023-03-13 10:15:00");
  }

  @Test
  public void searchCheckIn_WithReceiptNonexistent_ReturnErrorWithStatus404() {
    testClient
        .get()
        .uri("/parking-lots/check-in/{receipt}", "20230313-000000")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody()
        .jsonPath("status").isEqualTo(404)
        .jsonPath("path").isEqualTo("/parking-lots/check-in/20230313-000000")
        .jsonPath("method").isEqualTo("GET");
  }
}
