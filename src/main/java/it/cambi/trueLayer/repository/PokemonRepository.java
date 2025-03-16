package it.cambi.trueLayer.repository;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.model.pokemon.Pokemon;
import it.cambi.trueLayer.model.pokemon.PokemonVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
@RequiredArgsConstructor
public class PokemonRepository extends AbstractRepository {

  private final RestTemplate restTemplate;

  @Value("${pokemon.client.endpoint}")
  private String pokemonClientEndpoint;

  public Pokemon getPokemonByName(String name) {

    return getForObject(
        () ->
            restTemplate.getForObject(
                pokemonClientEndpoint + TrueLayerConstant.POKEMON_SPECIES_PATH + name,
                Pokemon.class));
  }

  public PokemonVersion getPokemonVersion() {

    return getForObject(
        () ->
            restTemplate.getForObject(
                pokemonClientEndpoint + TrueLayerConstant.POKEMON_VERSION_PATH,
                PokemonVersion.class));
  }
}
