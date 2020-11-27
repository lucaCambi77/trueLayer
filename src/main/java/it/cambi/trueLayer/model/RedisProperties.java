package it.cambi.trueLayer.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RedisProperties {

    private int redisPort;
    private String redisHost;
    private String redisPassword;

}
