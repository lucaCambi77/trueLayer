package it.cambi.trueLayer.repository;

import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import org.springframework.web.client.RestClientException;

import java.util.function.Supplier;

public class AbstractRepository {

    public <T> T getForObject(Supplier<T> supplier) {
        try {

            return supplier.get();

        } catch (RestClientException restClientException) {
            throw new TrueLayerRestClientException(restClientException);
        }
    }
}
