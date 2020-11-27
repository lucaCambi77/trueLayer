package it.cambi.trueLayer.cache;

import it.cambi.trueLayer.model.pokemon.Pokemon;
import it.cambi.trueLayer.model.pokemon.PokemonVersion;
import it.cambi.trueLayer.redis.EmbeddedRedisConfig;
import it.cambi.trueLayer.repository.PokemonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = {EmbeddedRedisConfig.class},
        properties = {"spring.redis.embedded=true"})
public class PokemonCacheTest {

    @Autowired
    private PokemonRepository pokemonRepository;
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    public void setUp() {
        invalidateCache();
    }

    @Test
    public void shouldUseCacheToFindVersion() {
        when(restTemplate.getForObject(anyString(), any(), (Object) any()))
                .thenReturn(PokemonVersion.builder().count(1).build());

        PokemonVersion pokemonVersion = pokemonRepository.getPokemonVersion();
        assertNotNull(pokemonVersion);
        assertEquals(1, pokemonVersion.getCount());

        verify(restTemplate).getForObject(any(), any(), (Object) any());

        reset(restTemplate);

        PokemonVersion pokemonVersion1 = pokemonRepository.getPokemonVersion();

        assertEquals(pokemonVersion, pokemonVersion1);
        verify(restTemplate, times(0)).getForObject(any(), any(), (Object) any());
    }

    @Test
    public void getPokemonVersionShouldNotUseCacheAfterInvalidate() {
        when(restTemplate.getForObject(anyString(), any(), (Object) any()))
                .thenReturn(PokemonVersion.builder().count(1).build());

        PokemonVersion pokemonVersion = pokemonRepository.getPokemonVersion();
        assertNotNull(pokemonVersion);
        assertEquals(1, pokemonVersion.getCount());

        verify(restTemplate).getForObject(any(), any(), (Object) any());

        reset(restTemplate);

        when(restTemplate.getForObject(anyString(), any(), (Object) any()))
                .thenReturn(PokemonVersion.builder().count(2).build());

        invalidateCache();

        PokemonVersion pokemonVersion1 = pokemonRepository.getPokemonVersion();

        assertNotEquals(pokemonVersion, pokemonVersion1);
        verify(restTemplate).getForObject(any(), any(), (Object) any());
    }

    @Test
    public void shouldUseCacheToFindPokemon() {
        when(restTemplate.getForObject(anyString(), any(), (Object) any()))
                .thenReturn(Pokemon.builder().id(1).build());

        Pokemon pokemon = pokemonRepository.getPokemonByName("charizard");

        assertNotNull(pokemon);
        assertEquals(1, pokemon.getId());

        verify(restTemplate).getForObject(any(), any(), (Object) any());

        reset(restTemplate);

        Pokemon pokemon1 = pokemonRepository.getPokemonByName("charizard");

        assertEquals(pokemon, pokemon1);
        verify(restTemplate, times(0)).getForObject(any(), any(), (Object) any());
    }

    @Test
    public void getPokemonByNameShouldNotUseCacheAfterInvalidate() {
        when(restTemplate.getForObject(anyString(), any(), (Object) any()))
                .thenReturn(Pokemon.builder().id(1).build());

        Pokemon pokemon = pokemonRepository.getPokemonByName("charizard");

        assertNotNull(pokemon);
        assertEquals(1, pokemon.getId());

        verify(restTemplate).getForObject(any(), any(), (Object) any());

        reset(restTemplate);

        when(restTemplate.getForObject(anyString(), any(), (Object) any()))
                .thenReturn(Pokemon.builder().id(2).build());

        invalidateCache();

        Pokemon pokemon1 = pokemonRepository.getPokemonByName("charizard");

        assertNotEquals(pokemon, pokemon1);
        verify(restTemplate).getForObject(any(), any(), (Object) any());
    }

    private void invalidateCache() {
        cacheManager.getCacheNames()
                .parallelStream()
                .forEach(n -> Objects.requireNonNull(cacheManager.getCache(n)).clear());
    }
}
