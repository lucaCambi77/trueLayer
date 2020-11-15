package it.cambi.trueLayer.model.pokemon;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@Getter
public class Pokemon {

    Integer id;
    String name;
    List<FlavourText> flavor_text_entries;

}
