package com.entropyteam.entropay.employees.payroll;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
class PayrollAsyncConfig {

    @Value("${payroll.executor.core-pool-size:4}")
    private int corePoolSize;

    @Value("${payroll.executor.max-pool-size:8}")
    private int maxPoolSize;

    @Value("${payroll.executor.queue-capacity:2000}")
    private int queueCapacity;

    @Value("${payroll.dispatch-executor.core-pool-size:2}")
    private int dispatchCorePoolSize;

    @Value("${payroll.dispatch-executor.max-pool-size:2}")
    private int dispatchMaxPoolSize;

    @Value("${payroll.dispatch-executor.queue-capacity:10}")
    private int dispatchQueueCapacity;

    /**
     * Pool that runs the per-employee calculation fan-out. Sized for real headcount; a
     * {@link ThreadPoolExecutor.CallerRunsPolicy} rejection handler is the safety net so an
     * oversized company can never overflow the queue into a {@code RejectedExecutionException}.
     */
    @Bean(name = "payrollExecutor")
    public Executor payrollExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("payroll-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    /**
     * Small dedicated pool for the {@code @Async} run-dispatch entrypoint. Kept separate from
     * {@code payrollExecutor} so the dispatch thread can never starve or contend with the
     * calculation fan-out it submits and joins on. One thread per concurrently-allowed run.
     */
    @Bean(name = "payrollDispatchExecutor")
    public Executor payrollDispatchExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(dispatchCorePoolSize);
        executor.setMaxPoolSize(dispatchMaxPoolSize);
        executor.setQueueCapacity(dispatchQueueCapacity);
        executor.setThreadNamePrefix("payroll-dispatch-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(120);
        executor.initialize();
        return executor;
    }
}
