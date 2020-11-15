package it.cambi.trueLayer.exception.advice;

import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.exception.ErrorResponse;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class TrueLayerControllerAdvice {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> datNotFoundException(DataNotFoundException dataNotFoundException) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

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
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> restClientServerException(TrueLayerRestClientException trueLayerRestClientException) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .error(httpStatus.getReasonPhrase())
                .errorMessage(trueLayerRestClientException.getMessage())
                .status(httpStatus.value())
                .timmeStamp(new Date())
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
