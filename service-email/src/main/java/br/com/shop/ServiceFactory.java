package br.com.shop;

public interface ServiceFactory<T> {

    ConsumerService<T> create();
}
