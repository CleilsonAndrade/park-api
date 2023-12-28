package br.com.cleilsonandrade.parkapi.exception;

public class CodeUniqueViolationException extends RuntimeException {
  public CodeUniqueViolationException(String message) {
    super(message);
  }
}
