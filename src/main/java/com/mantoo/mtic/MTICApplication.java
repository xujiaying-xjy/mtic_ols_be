package com.mantoo.mtic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.nio.charset.StandardCharsets;

@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = {"com.mantoo.mtic.module.system.mapper", "com.mantoo.mtic.module.system.exMapper"})
public class MTICApplication {

    public static void main(String[] args) {
        SpringApplication.run(MTICApplication.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
