package com.boardgameenjoyers.boardgamebuddy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class Application implements CommandLineRunner, WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/manifest.json")
                .addResourceLocations("classpath:/static/manifest.json");
        registry.addResourceHandler("/service-worker.js")
                .addResourceLocations("classpath:/static/service-worker.js");
    }

    @Override
    public void run(String... args) throws Exception {

    }

}
