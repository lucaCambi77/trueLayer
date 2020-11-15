package it.cambi.trueLayer.service;

import it.cambi.trueLayer.client.PokemonClient;
import it.cambi.trueLayer.client.TranslationClient;
import it.cambi.trueLayer.dto.ShakespeareTranslationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrueLayerService {

    private final PokemonClient pokemonClient;
    private final TranslationClient translationClient;

    public ShakespeareTranslationDto findShakespeareTranslationByPokemon(String name, Integer version) {
        return Optional.ofNullable(name).filter(n -> !n.isBlank())
                .map(n -> pokemonClient.getPokemonFlavorText(name, version))
                .map(f -> translationClient.getShakespeareTranslation(f.getFlavour_text()))
                .map(t -> ShakespeareTranslationDto.builder()
                        .description(t.getContents().getTranslated())
                        .name(name)
                        .build())
                .orElseThrow(() -> new IllegalArgumentException("Pokemon input name must not be empty"));
    }
}
