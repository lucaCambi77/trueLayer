package it.cambi.trueLayer.model.pokemon;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class FlavourText {

    String flavour_text;
    FlavourTextVersion language;
    FlavourTextVersion version;
}
