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

  @GetMapping(
      value = "/{name}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ShakespeareTranslationDto getShakespeareDescriptionByPokemon(
      @PathVariable String name,
      @RequestParam(required = false, value = "version") Integer version) {
    return trueLayerService.getShakespeareTranslationByPokemon(name, version);
  }
}
