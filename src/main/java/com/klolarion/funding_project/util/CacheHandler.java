package com.klolarion.funding_project.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheHandler {
    //사용자정보 캐싱 -> 토큰에 ID와 role을 넣어 대체(대부분 사용자 ID로 조회)
    //기타 정보 캐싱 -> 금액 등 일관성이 중요한 데이터가 대부분이라 딱히 캐싱할 데이터가 없음.
}
