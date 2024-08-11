package com.klolarion.funding_project.application.port.out;

import com.klolarion.funding_project.domain.entity.Member;

public interface CacheRepository {
    void setData(Member member);
    Member getData();
    boolean removeCache();

}
