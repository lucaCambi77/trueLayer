package it.cambi.trueLayer.repository;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import it.cambi.trueLayer.model.pokemon.Pokemon;
import it.cambi.trueLayer.model.pokemon.PokemonVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PokemonRepository {

    @Value("${pokemon.client.endpoint}")
    private String pokemonClientEndpoint;

    RestTemplate restTemplate;

    public Pokemon getPokemonByName(String name) {
        try {

            return restTemplate.getForObject(pokemonClientEndpoint + TrueLayerConstant.POKEMON_SPECIES_PATH + name,
                    Pokemon.class);
        } catch (RestClientException restClientException) {
            throw new TrueLayerRestClientException(restClientException);
        }
    }

    public PokemonVersion getPokemonVersion() {
        try {
            return restTemplate.getForObject(pokemonClientEndpoint + TrueLayerConstant.POKEMON_VERSION_PATH, PokemonVersion.class);
        } catch (RestClientException restClientException) {
            throw new TrueLayerRestClientException(restClientException);
        }
    }
}
