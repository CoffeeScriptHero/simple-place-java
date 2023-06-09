package com.kozarenko.exception;

import com.kozarenko.exception.custom.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.kozarenko.util.Constants.Exception.NO_REQUEST_PARAMETER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.CONFLICT;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
          MissingServletRequestParameterException ex,
          HttpHeaders headers,
          HttpStatus status,
          WebRequest request) {

    return buildResponseEntity(new ApiError(
            BAD_REQUEST,
            NO_REQUEST_PARAMETER
    ));
  }

  @ExceptionHandler({
          NoUserWithSuchUsernameException.class,
          NoUserWithSuchIdException.class,
          NoPostWithSuchIdException.class,
          NoCommentWithSuchIdException.class
  })
  public ResponseEntity<Object> handleNotFoundExceptions(Exception ex) {
    return buildResponseEntity(new ApiError(NOT_FOUND, ex));
  }

  @ExceptionHandler(UsernameTakenException.class)
  public ResponseEntity<Object> handleUsernameTaken(UsernameTakenException ex) {
    return buildResponseEntity(new ApiError(CONFLICT, ex));
  }

  @ExceptionHandler(PasswordVerificationException.class)
  public ResponseEntity<Object> handlePasswordVerification(PasswordVerificationException ex) {
    return buildResponseEntity(new ApiError(UNAUTHORIZED, ex));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
    return buildResponseEntity(new ApiError(FORBIDDEN, ex));
  }

  private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
