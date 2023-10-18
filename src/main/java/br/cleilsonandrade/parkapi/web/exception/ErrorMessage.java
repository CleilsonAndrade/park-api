package br.cleilsonandrade.parkapi.web.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorMessage {
  private String path;

  private String method;

  private int status;

  private String statusText;

  private String message;

  private Map<String, String> errors;

  public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult result) {
    this.path = request.getRequestURI();
    this.method = request.getMethod();
    this.status = status.value();
    this.statusText = status.getReasonPhrase();
    this.message = message;
    addErrors(result);
  }

  private void addErrors(BindingResult result) {
    this.errors = new HashMap<>();

    for (FieldError fieldError : result.getFieldErrors()) {
      this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
  }
}
