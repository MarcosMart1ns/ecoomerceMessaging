package br.com.shop.domain;

import java.math.BigDecimal;

public class Order {
    private String orderId;
    private BigDecimal ammount;

    private String email;

    public Order(String orderId, BigDecimal ammount, String email) {
        this.orderId = orderId;
        this.ammount = ammount;
        this.email = email;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmmount() {
        return ammount;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }


    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", ammount=" + ammount +
                ", email='" + email + '\'' +
                '}';
    }
}
