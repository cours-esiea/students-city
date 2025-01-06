package com.example.studentscity.data.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorProvider {
    private static volatile ExecutorProvider INSTANCE;
    private final ExecutorService databaseExecutor;

    private ExecutorProvider() {
        databaseExecutor = Executors.newSingleThreadExecutor();
    }

    public static ExecutorProvider getInstance() {
        if (INSTANCE == null) {
            synchronized (ExecutorProvider.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ExecutorProvider();
                }
            }
        }
        return INSTANCE;
    }

    public ExecutorService getDatabaseExecutor() {
        return databaseExecutor;
    }

    public void shutdown() {
        databaseExecutor.shutdown();
    }
} 