package it.cambi.trueLayer.exception.advice;

import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import it.cambi.trueLayer.exception.model.ErrorResponse;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class TrueLayerControllerAdvice {

  @ExceptionHandler(DataNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> datNotFoundException(
      DataNotFoundException dataNotFoundException) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .error(httpStatus.getReasonPhrase())
            .errorMessage(dataNotFoundException.getMessage())
            .status(httpStatus.value())
            .timmeStamp(new Date())
            .build();

    return ResponseEntity.status(httpStatus).body(errorResponse);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> illegalArgumentBadRequest(
      IllegalArgumentException illegalArgumentException) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .error(httpStatus.getReasonPhrase())
            .errorMessage(illegalArgumentException.getMessage())
            .status(httpStatus.value())
            .timmeStamp(new Date())
            .build();

    return ResponseEntity.status(httpStatus).body(errorResponse);
  }

  @ExceptionHandler(TrueLayerRestClientException.class)
  public ResponseEntity<ErrorResponse> restClientServerException(
      TrueLayerRestClientException restClientException) {
    HttpStatusCode httpStatus;
    String exceptionMessage;

    Throwable cause = restClientException.getCause();

    if (cause instanceof HttpServerErrorException ex) {
      httpStatus = ex.getStatusCode();
      exceptionMessage = ex.getMessage();
    } else if (cause instanceof HttpClientErrorException ex) {
      httpStatus = ex.getStatusCode();
      exceptionMessage = ex.getMessage();
    } else {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      exceptionMessage = restClientException.getMessage();
    }

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .errorMessage(exceptionMessage)
            .status(httpStatus.value())
            .timmeStamp(new Date())
            .build();

    return ResponseEntity.status(httpStatus).body(errorResponse);
  }
}
