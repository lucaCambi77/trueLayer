package it.cambi.trueLayer.model.pokemon;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@Getter
@EqualsAndHashCode(of = "id")
public class Pokemon {

    Integer id;
    String name;
    List<FlavorText> flavor_text_entries;

}
