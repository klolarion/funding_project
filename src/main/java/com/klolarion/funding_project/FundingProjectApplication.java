package com.klolarion.funding_project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "펀딩 프로젝트", version = "1.0", description = "펀딩 프로젝트 API 목록"))
public class FundingProjectApplication {

    @PostConstruct
    public void init() {
        // 애플리케이션 루트 디렉토리 하위에 log 폴더 생성
        File logDir = new File("log");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(FundingProjectApplication.class, args);
    }

}
