package it.cambi.trueLayer.exception;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String exceptionMessage) {
        super((exceptionMessage));
    }
}
