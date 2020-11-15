package it.cambi.trueLayer.client;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.model.pokemon.FlavourText;
import it.cambi.trueLayer.model.pokemon.Pokemon;
import it.cambi.trueLayer.model.pokemon.PokemonVersion;
import it.cambi.trueLayer.repository.PokemonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PokemonClient {

    private PokemonRepository pokemonRepository;

    public FlavourText getPokemonFlavorText(String name, Integer inputVersion) {
        PokemonVersion pokemonVersion = pokemonRepository.getPokemonVersion();
        if (inputVersion != null && (inputVersion < 1 || inputVersion > pokemonVersion.getCount()))
            throw new IllegalArgumentException("Version must be greater than zero and less or equal to " + pokemonVersion.getCount());

        String version = inputVersion == null ? Integer.toString(pokemonVersion.getCount()) : inputVersion.toString();

        Pokemon pokemon = pokemonRepository.getPokemonByName(name);
        Optional<Pokemon> optPokemon = Optional.ofNullable(pokemon).filter(p -> p.getId() != null).filter(p -> p.getFlavor_text_entries() != null);

        return optPokemon.map(p -> optPokemon.get().getFlavor_text_entries()
                .stream()
                .filter(f -> f.getLanguage() != null && f.getLanguage().getName().equals(TrueLayerConstant.DEFAULT_LANGUAGE))
                .filter(f -> f.getVersion() != null && f.getVersion().getUrl().indexOf(version) > 0)
                .findFirst().orElseThrow(() -> new DataNotFoundException("No description found for pokemon -> " + name + " version -> " + version)))
                .orElseThrow(() -> new DataNotFoundException("Invalid search for pokemon -> " + name + " version -> " + version));
    }
}
