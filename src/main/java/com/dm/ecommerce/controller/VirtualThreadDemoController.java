package com.dm.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Demo controller to showcase virtual threads in action.
 * This controller provides endpoints to test virtual threads performance
 * with simulated I/O bound operations.
 */
@RestController
@RequestMapping("/api/demo/virtual-threads")
public class VirtualThreadDemoController {

    private static final Logger logger = LoggerFactory.getLogger(VirtualThreadDemoController.class);

    /**
     * Gets information about the current thread.
     * Shows if the request is being handled by a virtual thread.
     * 
     * @return Thread information
     */
    @GetMapping("/info")
    public Map<String, Object> getThreadInfo() {
        Thread currentThread = Thread.currentThread();
        return Map.of(
            "threadName", currentThread.getName(),
            "threadId", currentThread.threadId(),
            "isVirtual", currentThread.isVirtual(),
            "threadCount", Thread.activeCount(),
            "timestamp", System.currentTimeMillis()
        );
    }

    /**
     * Simulates multiple concurrent I/O-bound operations.
     * Tests how the application handles many concurrent operations with virtual threads.
     * 
     * @param tasks Number of simulated I/O operations to execute in parallel
     * @param delayMs Simulated I/O delay in milliseconds per operation
     * @return Results of the simulation
     */
    @GetMapping("/simulate")
    public Map<String, Object> simulateConcurrentTasks(
            @RequestParam(defaultValue = "100") int tasks,
            @RequestParam(defaultValue = "200") int delayMs) throws ExecutionException, InterruptedException {
        
        logger.info("Starting simulation with {} tasks, {}ms delay each", tasks, delayMs);
        Instant start = Instant.now();
        
        // Create a list of future tasks
        List<CompletableFuture<String>> futures = new ArrayList<>();
        
        // Submit all tasks to be executed in parallel
        for (int i = 0; i < tasks; i++) {
            int taskId = i;
            futures.add(CompletableFuture.supplyAsync(() -> {
                try {
                    // Simulate I/O bound operation
                    Thread thread = Thread.currentThread();
                    logger.debug("Task {} running on thread {}, virtual: {}", 
                            taskId, thread.getName(), thread.isVirtual());
                    Thread.sleep(delayMs);
                    return "Task " + taskId + " completed on " + (thread.isVirtual() ? "virtual" : "platform") + 
                           " thread " + thread.getName();
                } catch (InterruptedException e) {
                    return "Task " + taskId + " was interrupted";
                }
            }));
        }
        
        // Wait for all tasks to complete
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.get(); // Wait for all to complete
        
        // Get results
        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .limit(5) // Just show first 5 results
                .toList();
        
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        
        return Map.of(
            "totalTasks", tasks,
            "delayPerTask", delayMs + "ms",
            "totalExecutionTime", duration.toMillis() + "ms",
            "theoreticalSequentialTime", (tasks * delayMs) + "ms",
            "threadType", Thread.currentThread().isVirtual() ? "Virtual" : "Platform",
            "requestId", UUID.randomUUID().toString(),
            "sampleResults", results
        );
    }
} 