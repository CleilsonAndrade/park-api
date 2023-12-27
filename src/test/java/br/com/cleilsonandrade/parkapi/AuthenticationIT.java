package br.com.cleilsonandrade.parkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.cleilsonandrade.parkapi.jwt.JwtToken;
import br.com.cleilsonandrade.parkapi.web.dto.UserLoginDTO;
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationIT {
  @Autowired
  WebTestClient testClient;

  @Test
  public void authenticate_WithValidatedCredentials_ReturnTokenWithStatus200() {
    JwtToken responseBody = testClient
        .post()
        .uri("/auth")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserLoginDTO("ana@email.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(JwtToken.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
  }

  @Test
  public void authenticate_WithInvalidatedCredentials_ReturnErrorMessageWithStatus400() {
    ErrorMessage responseBody = testClient
        .post()
        .uri("/auth")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserLoginDTO("invalid@email.com", "123456"))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    responseBody = testClient
        .post()
        .uri("/auth")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserLoginDTO("ana@email.com", "000000"))
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
  }
}
