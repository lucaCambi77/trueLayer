package it.cambi.trueLayer.redis;

import it.cambi.trueLayer.config.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@TestConfiguration
public class EmbeddedRedis {

  private RedisServer redisServer;

  public EmbeddedRedis(RedisProperties redisProperties) throws IOException {
    this.redisServer = new RedisServer(redisProperties.getRedisPort());
  }

  @PostConstruct
  public void postConstruct() {
    redisServer.start();
  }

  @PreDestroy
  public void preDestroy() {
    redisServer.stop();
  }
}
