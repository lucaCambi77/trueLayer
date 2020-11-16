package it.cambi.trueLayer.exception.advice;

import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.exception.ErrorResponse;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;

@ControllerAdvice
public class TrueLayerControllerAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> datNotFoundException(DataNotFoundException dataNotFoundException) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(httpStatus.getReasonPhrase())
                .errorMessage(dataNotFoundException.getMessage())
                .status(httpStatus.value())
                .timmeStamp(new Date())
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> illegalArgumentBadRequest(IllegalArgumentException illegalArgumentException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(httpStatus.getReasonPhrase())
                .errorMessage(illegalArgumentException.getMessage())
                .status(httpStatus.value())
                .timmeStamp(new Date())
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ExceptionHandler(TrueLayerRestClientException.class)
    public ResponseEntity<ErrorResponse> restClientServerException(TrueLayerRestClientException restClientException) {
        HttpStatus httpStatus;

        if (restClientException.getCause() instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) restClientException.getCause();
            httpStatus = httpClientErrorException.getStatusCode();
        } else if (restClientException.getCause() instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientServerException = (HttpClientErrorException) restClientException.getCause();
            httpStatus = httpClientServerException.getStatusCode();
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(httpStatus.getReasonPhrase())
                .errorMessage(restClientException.getMessage())
                .status(httpStatus.value())
                .timmeStamp(new Date())
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
