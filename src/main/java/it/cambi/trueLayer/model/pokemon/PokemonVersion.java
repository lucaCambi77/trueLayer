package it.cambi.trueLayer.model.pokemon;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
@EqualsAndHashCode
public class PokemonVersion {
    int count;
}
