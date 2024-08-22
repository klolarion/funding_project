package com.klolarion.funding_project.util;


import com.klolarion.funding_project.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String MEMBER_KEY_PREFIX = "member:";

    public void saveCsrfToken(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    //redis 데이터 저장
    public void setData(String key, Object value, long time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    //redis 데이터 조회
    public Object getData(String key){
        return redisTemplate.opsForValue().get(key);
    }

    //redis 데이터 삭제
    public void deleteData(String key){redisTemplate.delete(key);}


}
