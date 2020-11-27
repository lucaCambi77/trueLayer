package it.cambi.trueLayer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@Import(value = RedisProperties.class)
public class CacheConfig {

  @Value("${truelayer.pokemonVersionCache}")
  private Duration pokemonVersionCache;

  @Value("${truelayer.pokemonNameCache}")
  private Duration pokemonNameCache;

  @Bean
  public RedisTemplate<?, ?> redisTemplate(RedisProperties redisProperties) {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory(redisProperties));
    return template;
  }

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

    redisStandaloneConfiguration.setHostName(redisProperties.getRedisHost());
    redisStandaloneConfiguration.setPort(redisProperties.getRedisPort());
    redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getRedisPassword()));

    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .withInitialCacheConfigurations(cacheExpiries())
        .build();
  }

  private Map<String, RedisCacheConfiguration> cacheExpiries() {
    Map<String, RedisCacheConfiguration> result = new HashMap<>();

    result.put("pokemonVersionCache", ttlCacheConfiguration(pokemonVersionCache));
    result.put("pokemonNameCache", ttlCacheConfiguration(pokemonNameCache));

    return result;
  }

  private RedisCacheConfiguration ttlCacheConfiguration(Duration ttl) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
        .entryTtl(ttl)
        .disableCachingNullValues();
  }
}
