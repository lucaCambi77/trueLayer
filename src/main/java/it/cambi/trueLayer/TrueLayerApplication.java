package it.cambi.trueLayer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.trueLayer.config.CacheConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Import(value = CacheConfig.class)
public class TrueLayerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TrueLayerApplication.class, args);
  }

  @Bean
  public RestTemplate getRestTemplate() {

    return new RestTemplate();
  }

  @Bean
  public ObjectMapper getObjectMapper() {

    return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}
