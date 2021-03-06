package it.cambi.trueLayer.repository;

import it.cambi.trueLayer.model.translation.ShakespeareTranslation;
import it.cambi.trueLayer.redis.EmbeddedRedisConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
    classes = EmbeddedRedisConfig.class,
    properties = {"spring.redis.embedded=true"})
public class FunTranslationRepositoryTest {

  @Autowired private FunTranslationRepository funTranslationRepository;

  @Test
  public void shouldFindTranslation() {

    ShakespeareTranslation shakespeareTranslation =
        funTranslationRepository.getShakespeareTranslationBy("It is a beautiful day");
    assertNotNull(shakespeareTranslation);
    assertEquals(1, shakespeareTranslation.getSuccess().getTotal());
    assertNotNull(shakespeareTranslation.getContents().getTranslated());
  }
}
