package it.cambi.trueLayer.model.pokemon;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Pokemon {

    Integer id;
    String name;
    List<FlavourText> flavor_text_entries;
}
