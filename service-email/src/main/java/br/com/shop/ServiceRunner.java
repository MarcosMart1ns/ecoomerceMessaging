package br.com.shop;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceRunner<T> {

    private final ServiceProvider<T> serviceProvider;

    public ServiceRunner(ServiceFactory<T> serviceFactory) {
        this.serviceProvider = new ServiceProvider<>(serviceFactory);
    }

    public void start(int threadCount){
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i <threadCount; i++) {
            pool.submit(serviceProvider);
        }

    }
}
