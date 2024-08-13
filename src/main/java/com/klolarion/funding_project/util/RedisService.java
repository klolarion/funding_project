package com.klolarion.funding_project.util;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void saveCsrfToken(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    //redis 데이터 저장
    public void setData(String key, String value, long time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    //redis 데이터 조회
    public String getData(String key){
        return redisTemplate.opsForValue().get(key);
    }

    //redis 데이터 삭제
    public void deleteData(String key){redisTemplate.delete(key);}


}
