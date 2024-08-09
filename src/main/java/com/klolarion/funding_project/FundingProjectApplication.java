package com.klolarion.funding_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FundingProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundingProjectApplication.class, args);
    }

}
