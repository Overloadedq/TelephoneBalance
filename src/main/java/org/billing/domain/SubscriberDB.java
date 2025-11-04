package org.billing.domain;

import java.math.BigDecimal;

public class SubscriberDB {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private String role;
    private boolean blocked;





    // Геттеры
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isBlocked() { return blocked; }

    // Сеттеры
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }
}
