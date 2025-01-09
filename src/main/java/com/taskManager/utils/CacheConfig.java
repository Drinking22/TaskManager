package com.taskManager.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Конфигурация кэширования с использованием Redis.
 * Этот класс настраивает менеджер кэша, который использует Redis в качестве
 * хранилища кэша. Он определяет настройки, такие как время жизни кэша и
 * сериализацию значений.
 */
@Configuration
public class CacheConfig {

    /**
     * Создает и настраивает экземпляр {@link RedisCacheManager}.
     *
     * @param redisConnectionFactory фабрика для подключения к Redis
     * @return настроенный экземпляр {@link RedisCacheManager}
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .build();
    }
}
