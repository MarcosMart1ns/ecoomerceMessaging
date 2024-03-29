package br.com.shop.domain;

public class User {

    private final String uuid;

    public User(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getReportPath() {
        return "target/" + uuid + "-report.txt";
    }
}
