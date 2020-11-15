package it.cambi.trueLayer.repository;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.model.translation.ShakespeareTranslation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FunTranslationRepository {

    @Value("${funtranslations.client.endpoint}")
    private String funTranslationsClientEndpoint;

    RestTemplate restTemplate;

    public ShakespeareTranslation getShakespeareTranslationBy(String input) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(funTranslationsClientEndpoint + TrueLayerConstant.TRANSLATION_SHAKESPEARE_PATH)
                .queryParam("text", input).build();

        return restTemplate.getForObject(uriComponents.toString(),
                ShakespeareTranslation.class);
    }
}
