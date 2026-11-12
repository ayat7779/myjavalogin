package com.mylogin.model;

/**
 * Model class representing a user in the application.
 */
public class User {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    private String fullName;
    private String username;
    private String email;
    private String password;
    private String status;

    public User(String fullName, String username, String email, String password) {
        this(fullName, username, email, password, STATUS_PENDING);
    }

    public User(String fullName, String username, String email, String password, String status) {
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status != null ? status : STATUS_PENDING;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isApproved() {
        return STATUS_APPROVED.equalsIgnoreCase(this.status);
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.username);
    }
}
