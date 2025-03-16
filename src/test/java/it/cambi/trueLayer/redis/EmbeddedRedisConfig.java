package it.cambi.trueLayer.redis;

import it.cambi.trueLayer.model.RedisProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @PostConstruct
    public void postConstruct() throws IOException {
        getRedisServer().start();
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        getRedisServer().stop();
    }

    @Bean
    @DependsOn(value = "RedisProperties")
    public RedisServer getRedisServer() throws IOException {
        return new RedisServer(redisProperties.getRedisPort());
    }
}
