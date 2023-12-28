package br.com.cleilsonandrade.parkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.cleilsonandrade.parkapi.web.dto.ParkingCreateDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/parkings/parkings-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/parkings/parkings-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ParkingIT {
  @Autowired
  WebTestClient testClient;

  @Test
  public void createParking_WithDataValid_ReturnLocationWithStatus201() {
    testClient
        .post()
        .uri("/parkings")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .bodyValue(new ParkingCreateDTO("A-05", "AVAILABLE"))
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists(HttpHeaders.LOCATION);
  }

  @Test
  public void createParking_WithCodeExisting_ReturnErrorMessageWithStatus409() {
    testClient
        .post()
        .uri("/parkings")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .bodyValue(new ParkingCreateDTO("A-01", "AVAILABLE"))
        .exchange()
        .expectStatus().isEqualTo(409)
        .expectBody()
        .jsonPath("status").isEqualTo(409)
        .jsonPath("method").isEqualTo("POST")
        .jsonPath("path").isEqualTo("/parkings");
  }

  @Test
  public void createParking_WithDataInvalid_ReturnErrorMessageWithStatus422() {
    testClient
        .post()
        .uri("/parkings")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .bodyValue(new ParkingCreateDTO("", ""))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody()
        .jsonPath("status").isEqualTo(422)
        .jsonPath("method").isEqualTo("POST")
        .jsonPath("path").isEqualTo("/parkings");

    testClient
        .post()
        .uri("/parkings")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .bodyValue(new ParkingCreateDTO("A-501", "OCUPE"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody()
        .jsonPath("status").isEqualTo(422)
        .jsonPath("method").isEqualTo("POST")
        .jsonPath("path").isEqualTo("/parkings");
  }
}
