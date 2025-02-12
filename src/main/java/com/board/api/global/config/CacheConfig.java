package com.board.api.global.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@EnableCaching
@Configuration
public class CacheConfig {

    private static final String BOARD_CACHE_NAME = "board";

    @Bean
    public CacheManager cacheManager() {
        return createConcurrentMapCacheManager();
    }

    private ConcurrentMapCacheManager createConcurrentMapCacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setAllowNullValues(false);
        cacheManager.setCacheNames(List.of(BOARD_CACHE_NAME));
        return cacheManager;
    }
}