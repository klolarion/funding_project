package com.klolarion.funding_project.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomAccountGenerator {
    public static String generateRandomAccount() {
        Random random = new Random();

        // 3자리 숫자 생성 (000 - 999)
        String part1 = String.format("%03d", random.nextInt(1000));

        // 5자리 숫자 생성 (00000 - 99999)
        String part2 = String.format("%05d", random.nextInt(100000));

        // 2자리 숫자 생성 (00 - 99)
        String part3 = String.format("%02d", random.nextInt(100));

        // 결합하여 000-00000-00 형태의 문자열 생성
        return part1 + "-" + part2 + "-" + part3;

    }
}
