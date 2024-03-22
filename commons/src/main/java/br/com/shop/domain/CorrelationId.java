package br.com.shop.domain;

import java.util.UUID;

public class CorrelationId {

    private final String id;

    public CorrelationId() {
        this.id = UUID.randomUUID().toString();
    }

    public CorrelationId(String name) {
        this.id = name + "(" + UUID.randomUUID().toString()+")";
    }

    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }

    public static CorrelationId build(){
        return new CorrelationId();
    }

    public CorrelationId continueWith(String correlationId) {
        return new CorrelationId(id + "-" + correlationId);
    }
}
