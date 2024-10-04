package com.fernandocanabarro.blog_app.config;

import java.util.Objects;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisCacheConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;
    private RedissonClient redissonClient;

    @Bean
    public RedissonClient redissonClient(){
        if (Objects.isNull(redissonClient)) {
            Config config = new Config();
            config.useSingleServer()
                .setAddress(String.format("redis://%s:6379", redisHost));
            redissonClient = Redisson.create(config);
        }
        return redissonClient;
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient){
        return new RedissonSpringCacheManager(redissonClient);
    }
}
