package it.cambi.trueLayer.client;

import it.cambi.trueLayer.constant.TrueLayerConstant;
import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import it.cambi.trueLayer.model.pokemon.FlavorText;
import it.cambi.trueLayer.model.pokemon.FlavourTextVersion;
import it.cambi.trueLayer.model.pokemon.Pokemon;
import it.cambi.trueLayer.model.pokemon.PokemonVersion;
import it.cambi.trueLayer.repository.PokemonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonClientTest {

    @InjectMocks
    private PokemonClient pokemonClient;

    @Mock
    private PokemonRepository pokemonRepository;

    private final Integer pokemonVerCount = 100;
    private final String flavourText = "flavour_text";
    private final String pokemon = "pokemon";

    @BeforeEach
    public void setUp() {
        Mockito.lenient().when(pokemonRepository.getPokemonVersion()).thenReturn(PokemonVersion.builder().count(pokemonVerCount).build());
    }

    @Test
    public void shouldGetPokemonFlavourTextWithDefaults() {

        when(pokemonRepository.getPokemonByName(pokemon))
                .thenReturn(Pokemon.builder().id(1).name("pokemon")
                        .flavor_text_entries(Collections.singletonList(FlavorText.builder()
                                .flavor_text(flavourText)
                                .language(FlavourTextVersion.builder()
                                        .name(TrueLayerConstant.DEFAULT_LANGUAGE)
                                        .url("languageUrl").build())
                                .version(FlavourTextVersion.builder()
                                        .name("versionName").url("versionUrl/" + pokemonVerCount)
                                        .build()).build()))
                        .build());

        FlavorText pokemonFlavourText = pokemonClient.getPokemonFlavorText(pokemon, null);

        assertEquals(flavourText, pokemonFlavourText.getFlavor_text());

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository).getPokemonByName(pokemon);
    }

    @Test
    public void shouldGetPokemonFlavourTextWithDifferentVersion() {
        Integer differentVersion = 10;

        when(pokemonRepository.getPokemonByName(pokemon))
                .thenReturn(Pokemon.builder().id(1).name("pokemon")
                        .flavor_text_entries(Collections.singletonList(FlavorText.builder()
                                .flavor_text(flavourText)
                                .language(FlavourTextVersion.builder()
                                        .name(TrueLayerConstant.DEFAULT_LANGUAGE)
                                        .url("languageUrl").build())
                                .version(FlavourTextVersion.builder()
                                        .name("versionName").url("versionUrl/" + differentVersion)
                                        .build()).build()))
                        .build());

        FlavorText pokemonFlavourText = pokemonClient.getPokemonFlavorText(pokemon, differentVersion);

        assertEquals(flavourText, pokemonFlavourText.getFlavor_text());
        assertTrue(pokemonFlavourText.getVersion().getUrl().indexOf(differentVersion.toString()) > 0);

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository).getPokemonByName(pokemon);
    }

    @Test
    public void shouldThrowNegativeVersion() {
        Integer negativeVersion = -1;

        assertThrows(IllegalArgumentException.class
                , () -> pokemonClient.getPokemonFlavorText(pokemon, negativeVersion)
                , "Should throw IllegalArgumentException when negative version");

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository, times(0)).getPokemonByName(anyString());
    }

    @Test
    public void shouldThrowNotYesExistingVersion() {

        Integer notYetExistingVersion = pokemonVerCount + 1;

        assertThrows(IllegalArgumentException.class
                , () -> pokemonClient.getPokemonFlavorText(pokemon, notYetExistingVersion)
                , "Should throw IllegalArgumentException when version greater than current");

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository, times(0)).getPokemonByName(anyString());
    }

    @Test
    public void shouldThrowWhenGetPokemonMissingFlavourTextEntries() {

        when(pokemonRepository.getPokemonByName(pokemon))
                .thenReturn(Pokemon.builder().id(1).name("pokemon")
                        .build());

        assertThrows(DataNotFoundException.class
                , () -> pokemonClient.getPokemonFlavorText(pokemon, null)
                , "Should throw DataNotFoundException when missing FlavourText entries");

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository).getPokemonByName(pokemon);
    }

    @Test
    public void shouldThrowWhenGetNullPokemon() {

        when(pokemonRepository.getPokemonByName(pokemon))
                .thenReturn(null);

        assertThrows(DataNotFoundException.class
                , () -> pokemonClient.getPokemonFlavorText(pokemon, null)
                , "Should throw DataNotFoundException when missing FlavourText entries");

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository).getPokemonByName(pokemon);
    }

    @Test
    public void shouldThrowWhenEmptyFlavourTextNotFound() {

        when(pokemonRepository.getPokemonByName(pokemon))
                .thenReturn(Pokemon.builder().id(1).name("pokemon")
                        .flavor_text_entries(Collections.singletonList(FlavorText.builder()
                                .build())).build());

        assertThrows(DataNotFoundException.class
                , () -> pokemonClient.getPokemonFlavorText(pokemon, null)
                , "Should throw DataNotFoundException when FlavourText not found");

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository).getPokemonByName(pokemon);
    }

    @Test
    public void shouldThrowWhenRestClientException() {

        when(pokemonRepository.getPokemonByName(pokemon)).thenThrow(new TrueLayerRestClientException(new RestClientException("RestClientException")));

        assertThrows(TrueLayerRestClientException.class
                , () -> pokemonClient.getPokemonFlavorText(pokemon, null)
                , "Should throw TrueLayerRestClientException when rest client throws exception");

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository).getPokemonByName(pokemon);

        reset(pokemonRepository);

        when(pokemonRepository.getPokemonVersion()).thenThrow(new TrueLayerRestClientException(new RestClientException("RestClientException")));

        assertThrows(TrueLayerRestClientException.class
                , () -> pokemonClient.getPokemonFlavorText(pokemon, null)
                , "Should throw TrueLayerRestClientException when rest client throws exception");

        verify(pokemonRepository).getPokemonVersion();
        verify(pokemonRepository, times(0)).getPokemonByName(pokemon);
    }
}
