package com.miw.gildedrose;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@SpringBootApplication
public class GildedRoseApplication {

    public static void main(String[] args) {
        SpringApplication.run(GildedRoseApplication.class, args);
    }

//    @Configuration
//    static class OktaOAuth2WebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .authorizeRequests().anyRequest().authenticated()
//                    .and().oauth2ResourceServer().jwt();
//        }
//    }
}
