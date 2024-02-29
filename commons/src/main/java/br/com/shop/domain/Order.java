package br.com.shop.domain;

import java.math.BigDecimal;

public class Order {
    private String userId;
    private String orderId;
    private BigDecimal ammount;

    public Order(String userId, String orderId, BigDecimal value) {
        this.userId = userId;
        this.orderId = orderId;
        this.ammount = value;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
