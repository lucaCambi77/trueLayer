package it.cambi.trueLayer.service;

import it.cambi.trueLayer.client.PokemonClient;
import it.cambi.trueLayer.client.TranslationClient;
import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.dto.ShakespeareTranslationDto;
import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import it.cambi.trueLayer.model.pokemon.FlavourText;
import it.cambi.trueLayer.model.pokemon.FlavourTextVersion;
import it.cambi.trueLayer.model.translation.ShakespeareSuccess;
import it.cambi.trueLayer.model.translation.ShakespeareTranslation;
import it.cambi.trueLayer.model.translation.ShakespeareTranslationContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrueLayerServiceTest {

    @InjectMocks
    private TrueLayerService trueLayerService;

    @Mock
    private PokemonClient pokemonClient;

    @Mock
    private TranslationClient translationClient;

    @Test
    public void shouldFindShakespeareTranslation() {
        String name = "pokemon";
        String flavourText = "flavourText";
        String flavourTranslated = "flavour_translated";

        FlavourText flavourTextResponse =  FlavourText.builder()
                .flavour_text(flavourText)
                .language(FlavourTextVersion.builder()
                        .name(TrueLayerConstant.DEFAULT_LANGUAGE)
                        .url("languageUrl").build())
                .version(FlavourTextVersion.builder()
                        .name("versionName").url("versionUrl/" + 100)
                        .build()).build();

        when(pokemonClient.getPokemonFlavorText(name, null)).thenReturn(flavourTextResponse);

        when(translationClient.getShakespeareTranslation(flavourTextResponse.getFlavour_text())).thenReturn(ShakespeareTranslation.builder()
                .success(ShakespeareSuccess.builder()
                        .total(1).build())
                .contents(ShakespeareTranslationContent.builder()
                        .translation("shakespeare")
                        .translated(flavourTranslated)
                        .text(flavourTextResponse.getFlavour_text()).build()).build());

        ShakespeareTranslationDto shakespeareTranslationDto = trueLayerService.getShakespeareTranslationByPokemon(name, null);

        assertNotNull(shakespeareTranslationDto);
        assertEquals(flavourTranslated, shakespeareTranslationDto.getDescription());
        assertEquals(name, shakespeareTranslationDto.getName());

        verify(pokemonClient).getPokemonFlavorText(name, null);
        verify(translationClient).getShakespeareTranslation(flavourText);
    }

    @Test
    public void shouldThrowWhenDataNotFound() {
        String name = "pokemon";
        when(pokemonClient.getPokemonFlavorText(name, null)).thenThrow(new DataNotFoundException("Data not Found"));

        assertThrows(DataNotFoundException.class
                , () -> trueLayerService.getShakespeareTranslationByPokemon(name, null)
                , "Should throw when DataNotFoundException");

        verify(pokemonClient).getPokemonFlavorText(name, null);
        verify(translationClient, times(0)).getShakespeareTranslation(anyString());

    }

    @Test
    public void shouldThrowWhenPokemonClientRestClientException() {
        String name = "pokemon";
        when(pokemonClient.getPokemonFlavorText(name, null)).thenThrow(new TrueLayerRestClientException(new RestClientException("Rest client error")));

        assertThrows(TrueLayerRestClientException.class
                , () -> trueLayerService.getShakespeareTranslationByPokemon(name, null)
                , "Should throw when TrueLayerRestClientException");

        verify(pokemonClient).getPokemonFlavorText(name, null);
        verify(translationClient, times(0)).getShakespeareTranslation(anyString());
    }

   @Test
    public void shouldThrowWhenTranslationClientRestClientException() {
       String name = "pokemon";
       String flavourText = "flavourText";

       FlavourText flavourTextResponse =  FlavourText.builder()
               .flavour_text(flavourText)
               .language(FlavourTextVersion.builder()
                       .name(TrueLayerConstant.DEFAULT_LANGUAGE)
                       .url("languageUrl").build())
               .version(FlavourTextVersion.builder()
                       .name("versionName").url("versionUrl/" + 100)
                       .build()).build();

       when(pokemonClient.getPokemonFlavorText(name, null)).thenReturn(flavourTextResponse);

       when(translationClient.getShakespeareTranslation(flavourText)).thenThrow(new TrueLayerRestClientException(new RestClientException("Rest client error")));

       assertThrows(TrueLayerRestClientException.class
                , () -> trueLayerService.getShakespeareTranslationByPokemon(name, null)
                , "Should throw when TrueLayerRestClientException");

        verify(pokemonClient).getPokemonFlavorText(name, null);
        verify(translationClient).getShakespeareTranslation(anyString());
    }

    @Test
    public void shouldThrowWhenInputIsNull() {
        String name = "pokemon";

        assertThrows(IllegalArgumentException.class
                , () -> trueLayerService.getShakespeareTranslationByPokemon(null, null)
                , "Should throw when IllegalArgumentException");

        verify(pokemonClient, times(0)).getPokemonFlavorText(name, null);
        verify(translationClient, times(0)).getShakespeareTranslation(anyString());
    }

    @Test
    public void shouldThrowWhenInputIsBlank() {
        String name = "pokemon";

        assertThrows(IllegalArgumentException.class
                , () -> trueLayerService.getShakespeareTranslationByPokemon("    ", null)
                , "Should throw when IllegalArgumentException");

        verify(pokemonClient, times(0)).getPokemonFlavorText(name, null);
        verify(translationClient, times(0)).getShakespeareTranslation(anyString());
    }
}
