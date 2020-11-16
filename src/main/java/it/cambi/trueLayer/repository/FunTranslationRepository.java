package it.cambi.trueLayer.repository;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.model.translation.ShakespeareTranslation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@RequiredArgsConstructor
public class FunTranslationRepository extends AbstractRepository {

    @Value("${funtranslations.client.endpoint}")
    private String funTranslationsClientEndpoint;

    private final RestTemplate restTemplate;

    public ShakespeareTranslation getShakespeareTranslationBy(String input) {

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(funTranslationsClientEndpoint + TrueLayerConstant.TRANSLATION_SHAKESPEARE_PATH)
                .queryParam("text", input.replaceAll("\\n", "").replaceAll("\\r", "")).build();

        return getForObject(() -> restTemplate.getForObject(uriComponents.toString(),
                ShakespeareTranslation.class));
    }
}
