package it.cambi.trueLayer.config;

import it.cambi.trueLayer.model.RedisProperties;
import java.io.IOException;
import java.net.ServerSocket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisPropertiesConfig {

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.password}")
  private String redisPassword;

  @Value("${spring.redis.embedded}")
  private boolean redisEmbedded;

  @Bean(name = "RedisProperties")
  public RedisProperties getRedisProperties() {
    return RedisProperties.builder()
        .redisHost(redisEmbedded ? "localhost" : redisHost)
        .redisPassword(redisPassword)
        .redisPort(redisEmbedded ? findAvailablePort() : redisPort)
        .build();
  }

  private int findAvailablePort() {
    try (ServerSocket socket = new ServerSocket(0)) {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new IllegalStateException("Could not find an available port", e);
    }
  }
}
