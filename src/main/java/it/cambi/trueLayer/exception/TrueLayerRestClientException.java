package it.cambi.trueLayer.exception;

import org.springframework.web.client.RestClientException;

public class TrueLayerRestClientException extends RuntimeException {
  public TrueLayerRestClientException(RestClientException exceptionMessage) {
    super(exceptionMessage);
  }
}
