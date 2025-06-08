package com.boardgameenjoyers.boardgamebuddy;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public interface WebMvcConfigurer {
    void addResourceHandlers(ResourceHandlerRegistry registry);
}
