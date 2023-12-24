package br.com.cleilsonandrade.parkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.cleilsonandrade.parkapi.web.dto.UserCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {
  @Autowired
  WebTestClient testClient;

  @Test
  public void createUser_WithUsernameAndPasswordValid_ReturnUserCreatedWithStatus201() {
    UserResponseDTO responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("tody@email.com", "123456"))
        .exchange()
        .expectStatus().isCreated()
        .expectBody(UserResponseDTO.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");
    org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
  }
}
