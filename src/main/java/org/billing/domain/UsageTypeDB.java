package org.billing.domain;

public class UsageTypeDB {
    private int id;
    private String name;

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }

    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
}
