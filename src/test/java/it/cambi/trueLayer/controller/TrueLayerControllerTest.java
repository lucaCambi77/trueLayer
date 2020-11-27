package it.cambi.trueLayer.controller;

import it.cambi.trueLayer.dto.ShakespeareTranslationDto;
import it.cambi.trueLayer.exception.DataNotFoundException;
import it.cambi.trueLayer.exception.TrueLayerRestClientException;
import it.cambi.trueLayer.redis.EmbeddedRedis;
import it.cambi.trueLayer.service.TrueLayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = EmbeddedRedis.class,
        properties = {"spring.redis.embedded=true"})@AutoConfigureMockMvc
public class TrueLayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrueLayerService trueLayerService;

    private final MediaType mediaType = MediaType.APPLICATION_JSON;
    private final String name = "name";
    private final String shakeSpeareTranslation = "contentTranslated";

    @Test
    public void shouldGetPokemonDescriptionTranslated() throws Exception {

        ShakespeareTranslationDto shakespeareTranslationDto = ShakespeareTranslationDto.builder()
                .description(shakeSpeareTranslation)
                .name(name).build();

        when(trueLayerService.getShakespeareTranslationByPokemon(name, null)).thenReturn(shakespeareTranslationDto);

        mockMvc.perform(get("/pokemon/" + name)
                .contentType(mediaType)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.description", is(shakeSpeareTranslation)))
                .andReturn();
        verify(trueLayerService).getShakespeareTranslationByPokemon(name, null);
    }

    @Test
    public void shouldGetPokemonDescriptionTranslatedWithVersion() throws Exception {

        ShakespeareTranslationDto shakespeareTranslationDto = ShakespeareTranslationDto.builder()
                .description(shakeSpeareTranslation)
                .name(name).build();

        when(trueLayerService.getShakespeareTranslationByPokemon(name, 100)).thenReturn(shakespeareTranslationDto);

        mockMvc.perform(get("/pokemon/" + name).param("version", "100")
                .contentType(mediaType)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.description", is(shakeSpeareTranslation)))
                .andReturn();
        verify(trueLayerService).getShakespeareTranslationByPokemon(name, 100);
    }

    @Test
    public void shouldNotFoundErrorWhenDataNotFoundException() throws Exception {

        when(trueLayerService.getShakespeareTranslationByPokemon(name, null))
                .thenThrow(new DataNotFoundException("Data not found"));

        mockMvc.perform(get("/pokemon/" + name)
                .contentType(mediaType)).andExpect(status().isNotFound())
                .andReturn();
        verify(trueLayerService).getShakespeareTranslationByPokemon(name, null);
    }

    @Test
    public void shouldGetBadRequestWhenIllegalArgumentException() throws Exception {

        when(trueLayerService.getShakespeareTranslationByPokemon(name, null))
                .thenThrow(new IllegalArgumentException("Input not valid"));

        mockMvc.perform(get("/pokemon/" + name)
                .contentType(mediaType)).andExpect(status().isBadRequest())
                .andReturn();
        verify(trueLayerService).getShakespeareTranslationByPokemon(name, null);
    }

    @Test
    public void shouldGetInternalServerErrorHttpServerException() throws Exception {

        when(trueLayerService.getShakespeareTranslationByPokemon(name, null))
                .thenThrow(new TrueLayerRestClientException(new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE)));

        mockMvc.perform(get("/pokemon/" + name)
                .contentType(mediaType)).andExpect(status().is5xxServerError())
                .andReturn();
        verify(trueLayerService).getShakespeareTranslationByPokemon(name, null);
    }


    @Test
    public void shouldGetInternalClientErrorHttpServerException() throws Exception {

        when(trueLayerService.getShakespeareTranslationByPokemon(name, null))
                .thenThrow(new TrueLayerRestClientException(new HttpClientErrorException(HttpStatus.BAD_REQUEST)));

        mockMvc.perform(get("/pokemon/" + name)
                .contentType(mediaType)).andExpect(status().is4xxClientError())
                .andReturn();
        verify(trueLayerService).getShakespeareTranslationByPokemon(name, null);
    }


    @Test
    public void shouldGetInternalServerErrorRestClientException() throws Exception {

        when(trueLayerService.getShakespeareTranslationByPokemon(name, null))
                .thenThrow(new TrueLayerRestClientException(new RestClientException("Some rest client error")));

        mockMvc.perform(get("/pokemon/" + name)
                .contentType(mediaType)).andExpect(status().isInternalServerError())
                .andReturn();
        verify(trueLayerService).getShakespeareTranslationByPokemon(name, null);
    }
}
