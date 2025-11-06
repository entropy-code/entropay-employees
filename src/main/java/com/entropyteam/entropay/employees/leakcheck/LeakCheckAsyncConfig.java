package com.entropyteam.entropay.employees.leakcheck;

import java.time.Duration;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;

@Configuration
@EnableAsync
class LeakCheckAsyncConfig {

    @Value("${leakcheck.rate-limit.permits-per-second:3}")
    private int permitsPerSecond;

    @Value("${leakcheck.executor.core-pool-size:1}")
    private int corePoolSize;

    @Value("${leakcheck.executor.max-pool-size:1}")
    private int maxPoolSize;

    @Value("${leakcheck.executor.queue-capacity:500}")
    private int queueCapacity;

    @Bean(name = "leakCheckExecutor")
    public Executor leakCheckExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("leak-check-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    @Bean(name = "leakCheckRateLimiter")
    public RateLimiter leakCheckRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(permitsPerSecond)
                .timeoutDuration(Duration.ofMinutes(5))
                .build();

        return RateLimiter.of("leakCheckApi", config);
    }
}
