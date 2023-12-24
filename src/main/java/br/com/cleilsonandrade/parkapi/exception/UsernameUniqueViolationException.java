package br.com.cleilsonandrade.parkapi.exception;

public class UsernameUniqueViolationException extends RuntimeException {
  public UsernameUniqueViolationException(String message) {
    super(message);
  }
}
