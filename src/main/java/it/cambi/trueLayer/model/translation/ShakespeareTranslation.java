package it.cambi.trueLayer.model.translation;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@Jacksonized
public class ShakespeareTranslation {

    ShakespeareTranslationContent shakespeareTranslationContent;
    ShakespeareSuccess shakespeareSuccess;
}
