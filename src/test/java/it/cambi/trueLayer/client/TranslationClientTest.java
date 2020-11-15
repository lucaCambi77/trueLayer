package it.cambi.trueLayer.client;

import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import it.cambi.trueLayer.model.translation.ShakespeareSuccess;
import it.cambi.trueLayer.model.translation.ShakespeareTranslation;
import it.cambi.trueLayer.model.translation.ShakespeareTranslationContent;
import it.cambi.trueLayer.repository.FunTranslationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranslationClientTest {

    @InjectMocks
    private TranslationClient translationClient;

    @Mock
    private FunTranslationRepository translationRepository;

    @Test
    public void shouldTranslateShakespeareFromInput() {
        String input = "input";
        String translatedInput = "input";

        when(translationRepository.getShakespeareTranslationBy(input)).thenReturn(ShakespeareTranslation.builder()
                .success(ShakespeareSuccess.builder()
                        .total(1).build())
                .contents(ShakespeareTranslationContent.builder()
                        .translation("shakespeare")
                        .translated(translatedInput)
                        .text(input).build()).build());

        ShakespeareTranslation shakespeareTranslation = translationClient.getShakespeareTranslation(input);

        assertEquals(input, shakespeareTranslation.getContents().getText());
        assertEquals(translatedInput, shakespeareTranslation.getContents().getTranslated());
        assertEquals(1, shakespeareTranslation.getSuccess().getTotal());
        verify(translationRepository).getShakespeareTranslationBy(input);
    }

    @Test
    public void shouldThrowWhenNoTranslationFound() {
        String input = "input";

        when(translationRepository.getShakespeareTranslationBy(input)).thenReturn(null);

        assertThrows(DataNotFoundException.class
                , () -> translationClient.getShakespeareTranslation(input)
                , "Should throw DataNotFoundException translation not found");

        verify(translationRepository).getShakespeareTranslationBy(input);
    }

    @Test
    public void shouldThrowWhenTranslationNotSucceeded() {
        String input = "input";

        when(translationRepository.getShakespeareTranslationBy(input)).thenReturn(ShakespeareTranslation.builder()
                .success(ShakespeareSuccess.builder().total(0)
                        .build()).build());

        assertThrows(DataNotFoundException.class
                , () -> translationClient.getShakespeareTranslation(input)
                , "Should throw DataNotFoundException translation not found");

        verify(translationRepository).getShakespeareTranslationBy(input);
    }


    @Test
    public void shouldThrowWhenRestClientException() {
        String input = "input";
        when(translationRepository.getShakespeareTranslationBy(input)).thenThrow(new TrueLayerRestClientException(new RestClientException("RestClientException")));

        assertThrows(TrueLayerRestClientException.class
                , () -> translationClient.getShakespeareTranslation(input)
                , "Should throw TrueLayerRestClientException when rest client throws exception");

        verify(translationRepository).getShakespeareTranslationBy(input);
    }
}
