package br.com.cleilsonandrade.parkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.cleilsonandrade.parkapi.web.dto.ClientCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.ClientResponseDTO;
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clients/clients-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clients/clients-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClientIT {
  @Autowired
  WebTestClient testClient;

  @Test
  public void createClient_WithDataValid_ReturnClientWithStatus201() {
    ClientResponseDTO responseBody = testClient
        .post()
        .uri("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
        .bodyValue(new ClientCreateDTO("Tobias Ferreira", "51460558073"))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(ClientResponseDTO.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getName()).isEqualTo("Tobias Ferreira");
    org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("51460558073");
  }

  @Test
  public void createClient_WithUserNotAllowed_ReturnErrorMessageStatus403() {
    ErrorMessage responseBody = testClient
        .post()
        .uri("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .bodyValue(new ClientCreateDTO("Tobias Ferreira", "24122251095"))
        .exchange()
        .expectStatus().isEqualTo(403)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
  }

  @Test
  public void createClient_WithRegisteredCPF_ReturnErrorMessageStatus409() {
    ErrorMessage responseBody = testClient
        .post()
        .uri("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
        .bodyValue(new ClientCreateDTO("Tobias Ferreira", "24122251095"))
        .exchange()
        .expectStatus().isEqualTo(409)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
  }

  @Test
  public void createClient_WithDataInvalid_ReturnErrorMessageStatus422() {
    ErrorMessage responseBody = testClient
        .post()
        .uri("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
        .bodyValue(new ClientCreateDTO("", ""))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .post()
        .uri("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
        .bodyValue(new ClientCreateDTO("To", "00000000000"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .post()
        .uri("/clients")
        .contentType(MediaType.APPLICATION_JSON)
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
        .bodyValue(new ClientCreateDTO("To", "000.000.000-00"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
  }

  @Test
  public void searchClient_WithIdExistingForAdmin_ReturnClientWithStatus200() {
    ClientResponseDTO responseBody = testClient
        .get()
        .uri("/clients/10")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(ClientResponseDTO.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);
  }

  @Test
  public void searchClient_WithIdExistingForClient_ReturnErrorMessageWithStatus403() {
    ErrorMessage responseBody = testClient
        .get()
        .uri("/clients/0")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
  }

  @Test
  public void searchClient_WithIdNonexistingForAdmin_ReturnErrorMessageWithStatus404() {
    ErrorMessage responseBody = testClient
        .get()
        .uri("/clients/0")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
  }

  @Test
  public void searchClient_WithIdExistingForClient_ReturnErrorMessageWithStatus404() {
    ErrorMessage responseBody = testClient
        .get()
        .uri("/clients/0")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
  }

}
