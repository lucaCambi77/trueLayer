package it.cambi.trueLayer.model.pokemon;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class FlavourText {

    String flavour_text;
    FlavourTextVersion language;
    FlavourTextVersion version;
}
