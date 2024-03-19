package br.com.shop.domain;

public class Message<T> {


    private final CorrelationId id;
    private final T payload;

    public Message(CorrelationId correlationId, T payload) {
        this.id = correlationId;
        this.payload = payload;
    }

    public CorrelationId getId() {
        return id;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message {" +
                "id=" + id +
                ", payload=" + payload +
                '}';
    }
}
