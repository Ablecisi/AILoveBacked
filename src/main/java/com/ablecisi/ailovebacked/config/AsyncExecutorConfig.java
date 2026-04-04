package com.ablecisi.ailovebacked.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncExecutorConfig {

    @Bean(name = "dialogSseExecutor")
    public Executor dialogSseExecutor() {
        ThreadPoolTaskExecutor ex = new ThreadPoolTaskExecutor();
        ex.setCorePoolSize(4);
        ex.setMaxPoolSize(32);
        ex.setQueueCapacity(200);
        ex.setThreadNamePrefix("dialog-sse-");
        ex.initialize();
        return ex;
    }
}
