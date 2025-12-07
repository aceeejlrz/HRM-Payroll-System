package com.hrmpayroll.model;

import com.hrmpayroll.model.enums.UserRole;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User model for authentication and role-based access
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private transient String password; // Not serialized for security
    private String passwordHash;
    private UserRole role;
    private String fullName;
    private LocalDateTime createdAt;
    private boolean isActive;

    public User(String username, String password, UserRole role, String fullName) {
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
        this.fullName = fullName;
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    private String hashPassword(String password) {
        // Simple hash for demo - in production use BCrypt
        return Integer.toHexString(password.hashCode());
    }

    public boolean verifyPassword(String password) {
        return hashPassword(password).equals(passwordHash);
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getPasswordHash() { return passwordHash; }
}