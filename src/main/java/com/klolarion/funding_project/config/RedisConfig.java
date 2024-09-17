package com.klolarion.funding_project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

//        객체나 구조화된 데이터 저장시 사용.
//        redisTemplate.setKeySerializer(new StringRedisSerializer()); // 키 직렬화
//        redisTemplate.setValueSerializer(new StringRedisSerializer()); // 값 직렬화
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer()); // 해시 키 직렬화
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer()); // 해시 값 직렬화
//        redisTemplate.afterPropertiesSet(); // 설정 완료 후 초기화

        return redisTemplate;
    }

}
