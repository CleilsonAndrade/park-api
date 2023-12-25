package br.com.cleilsonandrade.parkapi;

import java.util.function.Consumer;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import br.com.cleilsonandrade.parkapi.jwt.JwtToken;
import br.com.cleilsonandrade.parkapi.web.dto.UserLoginDTO;

public class JwtAuthentication {
  public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
    String token = client
        .post()
        .uri("/auth")
        .bodyValue(new UserLoginDTO(username, password))
        .exchange()
        .expectStatus().isOk()
        .expectBody(JwtToken.class)
        .returnResult().getResponseBody().getToken();

    return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
  }
}
