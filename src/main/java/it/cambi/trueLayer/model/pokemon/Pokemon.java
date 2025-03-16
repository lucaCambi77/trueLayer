package it.cambi.trueLayer.model.pokemon;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Getter
@EqualsAndHashCode(of = "id")
public class Pokemon {

    Integer id;
    String name;
    List<FlavorText> flavor_text_entries;

}
