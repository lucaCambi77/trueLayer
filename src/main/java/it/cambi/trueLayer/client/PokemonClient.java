package it.cambi.trueLayer.client;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.model.pokemon.FlavorText;
import it.cambi.trueLayer.model.pokemon.Pokemon;
import it.cambi.trueLayer.model.pokemon.PokemonVersion;
import it.cambi.trueLayer.repository.PokemonRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PokemonClient {

  private final PokemonRepository pokemonRepository;

  @Cacheable(value = "pokemonCache", sync = true)
  public FlavorText getPokemonFlavorText(String name, Integer inputVersion) {
    PokemonVersion pokemonVersion = pokemonRepository.getPokemonVersion();

    int version =
        Optional.of(Optional.ofNullable(inputVersion).orElseGet(pokemonVersion::getCount))
            .filter(v -> !(v < 1 || v > pokemonVersion.getCount()))
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Version must be greater than zero and less or equal to "
                            + pokemonVersion.getCount()));

    Pokemon pokemon = pokemonRepository.getPokemonByName(name);
    Optional<Pokemon> optPokemon =
        Optional.ofNullable(pokemon)
            .filter(p -> p.getId() != null)
            .filter(p -> p.getFlavor_text_entries() != null);

    return optPokemon
        .map(
            p ->
                optPokemon.get().getFlavor_text_entries().stream()
                    .filter(
                        f ->
                            f.getLanguage() != null
                                && f.getLanguage()
                                    .getName()
                                    .equals(TrueLayerConstant.DEFAULT_LANGUAGE))
                    .filter(
                        f ->
                            f.getVersion() != null
                                && f.getVersion().getUrl().indexOf(Integer.toString(version)) > 0)
                    .findFirst()
                    .orElseThrow(
                        () ->
                            new DataNotFoundException(
                                "No description found for pokemon -> "
                                    + name
                                    + " version -> "
                                    + version)))
        .orElseThrow(
            () ->
                new DataNotFoundException(
                    "Invalid search for pokemon -> " + name + " version -> " + version));
  }
}
