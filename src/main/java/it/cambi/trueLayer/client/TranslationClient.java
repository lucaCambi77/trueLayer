package it.cambi.trueLayer.client;

import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.model.translation.ShakespeareTranslation;
import it.cambi.trueLayer.repository.FunTranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TranslationClient {

    private final FunTranslationRepository funTranslationRepository;

    public ShakespeareTranslation getShakespeareTranslation(String input) {
        ShakespeareTranslation shakespeareTranslation = funTranslationRepository.getShakespeareTranslationBy(input);

       return Optional.ofNullable(shakespeareTranslation).filter(st -> st.getShakespeareSuccess().getTotal() > 0)
               .orElseThrow(() -> new DataNotFoundException("Unable to translate input " + input));
    }
}
