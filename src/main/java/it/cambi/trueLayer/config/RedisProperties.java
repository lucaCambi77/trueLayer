package it.cambi.trueLayer.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SocketUtils;

@Configuration
@Getter
public class RedisProperties {
  private int redisPort;
  private String redisHost;
  private String redisPassword;

  public RedisProperties(
      @Value("${spring.redis.port}") int redisPort,
      @Value("${spring.redis.host}") String redisHost,
      @Value("${spring.redis.password}") String redisPassword,
      @Value("${spring.redis.embedded}") boolean embedded) {
    this.redisPort = embedded ? SocketUtils.findAvailableTcpPort() : redisPort;
    this.redisHost = redisHost;
    this.redisPassword = redisPassword;
  }
}
