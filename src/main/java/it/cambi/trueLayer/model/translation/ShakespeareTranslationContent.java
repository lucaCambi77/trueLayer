package it.cambi.trueLayer.model.translation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ShakespeareTranslationContent {

    String translated;
    String text;
    String translation;
}
