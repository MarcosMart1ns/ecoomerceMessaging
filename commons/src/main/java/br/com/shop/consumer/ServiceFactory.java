package br.com.shop.consumer;

public interface ServiceFactory<T> {

    ConsumerService<T> create();
}
