package br.com.cleilsonandrade.parkapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.cleilsonandrade.parkapi.web.dto.UserCreateDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserPassDTO;
import br.com.cleilsonandrade.parkapi.web.dto.UserResponseDTO;
import br.com.cleilsonandrade.parkapi.web.exception.ErrorMessage;

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

  @Test
  public void createUser_WithUsernameInvalid_ReturnErrorMessageWithStatus422() {
    ErrorMessage responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("", "123456"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("tody@", "123456"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("tody@email.", "123456"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
  }

  @Test
  public void createUser_WithPasswordInvalid_ReturnErrorMessageWithStatus422() {
    ErrorMessage responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("tody@email.com", ""))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("tody@email.com", "123"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("tody@email.com", "123456789"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
  }

  @Test
  public void createUser_WithUsernameExisting_ReturnErrorMessageWithStatus409() {
    ErrorMessage responseBody = testClient
        .post()
        .uri("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserCreateDTO("ana@email.com", "123456"))
        .exchange()
        .expectStatus().isEqualTo(409)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);
  }

  @Test
  public void findUser_IdUserExisting_ReturnUserWithStatus200() {
    UserResponseDTO responseBody = testClient
        .get()
        .uri("/users/100")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserResponseDTO.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);
    org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");
    org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");

    testClient
        .get()
        .uri("/users/101")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserResponseDTO.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
    org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
    org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");

    testClient
        .get()
        .uri("/users/101")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserResponseDTO.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);
    org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");
    org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");

  }

  @Test
  public void findUser_IdUserNonexistent_ReturnErrorMessageWithStatus404() {
    ErrorMessage responseBody = testClient
        .get()
        .uri("/users/0")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
  }

  @Test
  public void findUser_WithTheRoleClientUserLookingForAnotherClient_ReturnErrorMessageWithStatus403() {
    ErrorMessage responseBody = testClient
        .get()
        .uri("/users/102")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
  }

  @Test
  public void updatePassword_WithDataValid_ReturnWithStatus204() {
    testClient
        .patch()
        .uri("/users/100")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("123456", "123456", "123456"))
        .exchange()
        .expectStatus().isNoContent();

    testClient
        .patch()
        .uri("/users/101")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("123456", "123456", "123456"))
        .exchange()
        .expectStatus().isNoContent();
  }

  @Test
  public void updatePassword_WithDifferentUsers_ReturnErrorMessageWithStatus403() {
    ErrorMessage responseBody = testClient
        .patch()
        .uri("/users/0")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("123456", "123456", "123456"))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    responseBody = testClient
        .patch()
        .uri("/users/0")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("123456", "123456", "123456"))
        .exchange()
        .expectStatus().isForbidden()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
  }

  @Test
  public void updatePassword_WithDataInvalid_ReturnErrorMessageWithStatus422() {
    ErrorMessage responseBody = testClient
        .patch()
        .uri("/users/100")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("", "", ""))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .patch()
        .uri("/users/100")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("12345", "12345", "12345"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    responseBody = testClient
        .patch()
        .uri("/users/100")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("12345678", "12345678", "12345678"))
        .exchange()
        .expectStatus().isEqualTo(422)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
  }

  @Test
  public void updatePassword_WithPassInvalid_ReturnErrorMessageWithStatus400() {
    ErrorMessage responseBody = testClient
        .patch()
        .uri("/users/100")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("123456", "123456", "000000"))
        .exchange()
        .expectStatus().isEqualTo(400)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

    responseBody = testClient
        .patch()
        .uri("/users/100")
        .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new UserPassDTO("000000", "12345", "12345"))
        .exchange()
        .expectStatus().isEqualTo(400)
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
    org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
  }
}
