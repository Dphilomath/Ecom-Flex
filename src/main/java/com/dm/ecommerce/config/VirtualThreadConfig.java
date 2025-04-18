package com.dm.ecommerce.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

/**
 * Configuration class for virtual threads support.
 * This class provides configuration beans to enable virtual threads
 * for both Spring's task executor and Tomcat's connector.
 */
@Configuration
public class VirtualThreadConfig {

    /**
     * Creates a task executor that uses virtual threads.
     * This replaces Spring Boot's default task executor with one that creates a new
     * virtual thread for each task.
     *
     * @return AsyncTaskExecutor that delegates to virtual threads
     */
    @Bean(name = TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * Customizes Tomcat's ProtocolHandler to use virtual threads.
     * This allows incoming HTTP requests to be processed by virtual threads instead of
     * platform threads from a thread pool.
     *
     * @return A TomcatProtocolHandlerCustomizer that configures the connector to use virtual threads
     */
    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
} 