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
  public RedisTemplate<?, ?> getRedisTemplate(RedisProperties redisProperties) {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(getRedisConnectionFactory(redisProperties));
    return template;
  }

  @Bean
  public LettuceConnectionFactory getRedisConnectionFactory(RedisProperties redisProperties) {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

    redisStandaloneConfiguration.setHostName(redisProperties.getRedisHost());
    redisStandaloneConfiguration.setPort(redisProperties.getRedisPort());
    redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getRedisPassword()));

    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public CacheManager getCacheManager(RedisConnectionFactory redisConnectionFactory) {
    return RedisCacheManager.builder(redisConnectionFactory)
        .withInitialCacheConfigurations(getCacheExpiresMap())
        .build();
  }

  private Map<String, RedisCacheConfiguration> getCacheExpiresMap() {
    Map<String, RedisCacheConfiguration> result = new HashMap<>();

    result.put("pokemonVersionCache", getTtlCacheConfiguration(pokemonVersionCache));
    result.put("pokemonNameCache", getTtlCacheConfiguration(pokemonNameCache));

    return result;
  }

  private RedisCacheConfiguration getTtlCacheConfiguration(Duration ttl) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
        .entryTtl(ttl)
        .disableCachingNullValues();
  }
}
