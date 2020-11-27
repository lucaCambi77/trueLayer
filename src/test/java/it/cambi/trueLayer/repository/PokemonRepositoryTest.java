package it.cambi.trueLayer.repository;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import it.cambi.trueLayer.model.pokemon.Pokemon;
import it.cambi.trueLayer.model.pokemon.PokemonVersion;
import it.cambi.trueLayer.redis.EmbeddedRedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    classes = EmbeddedRedisConfig.class,
    properties = {"spring.redis.embedded=true"})
public class PokemonRepositoryTest {

  @Autowired private PokemonRepository pokemonRepository;

  @Test
  public void shouldFindPokemon() {
    Pokemon pokemon = pokemonRepository.getPokemonByName("charizard");

    assertNotNull(pokemon);
    assertNotNull(pokemon.getName());
    assertNotNull(pokemon.getFlavor_text_entries());
    assertTrue(
        pokemon.getFlavor_text_entries().stream()
            .anyMatch(fe -> fe.getLanguage().getName().equals(TrueLayerConstant.DEFAULT_LANGUAGE)));
    pokemon.getFlavor_text_entries().stream()
        .filter(fe -> fe.getLanguage().getName().equals(TrueLayerConstant.DEFAULT_LANGUAGE))
        .findAny()
        .ifPresent(p -> assertNotNull(p.getFlavor_text()));
  }

  @Test
  public void shouldNotFindPokemon() {
    assertThrows(
        TrueLayerRestClientException.class,
        () -> pokemonRepository.getPokemonByName("chorizo"),
        "Should throw client exception when pokemon not exists");
  }

  @Test
  public void shouldFindVersion() {
    PokemonVersion pokemonVersion = pokemonRepository.getPokemonVersion();
    assertNotNull(pokemonVersion);
  }
}
