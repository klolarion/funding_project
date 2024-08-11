package com.klolarion.funding_project.infrastructure.persistence;

import com.klolarion.funding_project.application.port.out.CacheRepository;
import com.klolarion.funding_project.domain.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RedisCacheRepositoryImpl implements CacheRepository {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setData(Member member) {


    }

    @Override
    public Member getData() {
        return null;
    }

    @Override
    public boolean removeCache() {
        return false;
    }
}
