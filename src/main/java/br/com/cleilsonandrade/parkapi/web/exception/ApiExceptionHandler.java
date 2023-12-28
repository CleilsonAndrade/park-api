package br.com.cleilsonandrade.parkapi.web.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.cleilsonandrade.parkapi.exception.CodeUniqueViolationException;
import br.com.cleilsonandrade.parkapi.exception.CpfUniqueViolationException;
import br.com.cleilsonandrade.parkapi.exception.EntityNotFoundException;
import br.com.cleilsonandrade.parkapi.exception.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorMessage> entityNotFoundException(RuntimeException ex,
      HttpServletRequest request) {
    log.error("Api Error: ", ex);

    return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
        .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage(), null));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorMessage> accessDeniedException(RuntimeException ex,
      HttpServletRequest request) {
    log.error("Api Error: ", ex);

    return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON)
        .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage(), null));
  }

  @ExceptionHandler({ UsernameUniqueViolationException.class, CpfUniqueViolationException.class,
      CodeUniqueViolationException.class })
  public ResponseEntity<ErrorMessage> uniqueViolationException(RuntimeException ex,
      HttpServletRequest request) {
    log.error("Api Error: ", ex);

    return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
        .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage(), null));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex,
      HttpServletRequest request, BindingResult result) {
    log.error("Api Error: ", ex);

    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).contentType(MediaType.APPLICATION_JSON)
        .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid fields", result));
  }
}
