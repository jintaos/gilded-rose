package com.miw.gildedrose.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("gilded-rose")
                .packagesToScan("com.miw.gildedrose.controllers")
                .pathsToMatch("/**")
                .build();
    }
}