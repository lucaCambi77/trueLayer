package it.cambi.trueLayer.controller;

import it.cambi.trueLayer.dto.ShakespeareTranslationDto;
import it.cambi.trueLayer.service.TrueLayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokemon")
@RequiredArgsConstructor
public class TrueLayerController {

    private final TrueLayerService trueLayerService;

    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ShakespeareTranslationDto getShakespeareDescriptionByPokemon(@PathVariable String name, @RequestParam(required = false) Integer version) {
        return trueLayerService.getShakespeareTranslationByPokemon(name, version);
    }
}