package com.dhruvijain.deliveryplanner.model;

public class User {

    public enum Role {
        ADMIN,
        MANAGER,
        DRIVER
    }

    private final String username;
    private final String passwordHash;
    private final Role role;

    public User(String username, String passwordHash, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{username='" + username + "', role=" + role + "}";
    }
}
