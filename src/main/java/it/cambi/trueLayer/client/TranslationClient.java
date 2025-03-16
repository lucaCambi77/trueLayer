package it.cambi.trueLayer.client;

import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.model.translation.ShakespeareTranslation;
import it.cambi.trueLayer.repository.FunTranslationRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TranslationClient {

  private final FunTranslationRepository funTranslationRepository;

  @Cacheable(value = "shakespeareCache", sync = true)
  public ShakespeareTranslation getShakespeareTranslation(String input) {
    ShakespeareTranslation shakespeareTranslation =
        Optional.ofNullable(input)
            .filter(i -> !i.isBlank())
            .map(funTranslationRepository::getShakespeareTranslationBy)
            .orElseThrow(
                () -> new DataNotFoundException("Unable to translate empty or null input"));

    return Optional.ofNullable(shakespeareTranslation)
        .filter(st -> st.getSuccess().getTotal() > 0)
        .orElseThrow(() -> new DataNotFoundException("Unable to translate input " + input));
  }
}
