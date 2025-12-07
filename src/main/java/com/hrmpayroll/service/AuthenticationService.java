package com.hrmpayroll.service;

import com.hrmpayroll.model.User;
import com.hrmpayroll.model.enums.UserRole;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user authentication and session management
 */
public class AuthenticationService {
    private static AuthenticationService instance;
    private User currentUser;
    private final Map<String, UserSession> activeSessions;

    private AuthenticationService() {
        activeSessions = new HashMap<>();
    }

    public static synchronized AuthenticationService getInstance() {
        if (instance == null) {
            instance = new AuthenticationService();
        }
        return instance;
    }

    public boolean login(String username, String password) {
        DataStorageService storage = DataStorageService.getInstance();
        User user = storage.getUser(username);

        if (user != null && user.isActive() && user.verifyPassword(password)) {
            this.currentUser = user;
            activeSessions.put(username, new UserSession(user));
            logAudit("LOGIN_SUCCESS", username);
            return true;
        }

        logAudit("LOGIN_FAILED", username);
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            activeSessions.remove(currentUser.getUsername());
            logAudit("LOGOUT", currentUser.getUsername());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean hasPermission(UserRole requiredRole) {
        if (currentUser == null) return false;

        switch (requiredRole) {
            case ADMIN:
                return currentUser.getRole() == UserRole.ADMIN;
            case HR_MANAGER:
                return currentUser.getRole() == UserRole.ADMIN ||
                        currentUser.getRole() == UserRole.HR_MANAGER;
            case HR_STAFF:
                return currentUser.getRole() == UserRole.ADMIN ||
                        currentUser.getRole() == UserRole.HR_MANAGER ||
                        currentUser.getRole() == UserRole.HR_STAFF;
            default:
                return false;
        }
    }

    private void logAudit(String action, String username) {
        System.out.println(String.format("[AUDIT] %s - Action: %s, User: %s, Time: %s",
                LocalDateTime.now(), action, username, System.currentTimeMillis()));
    }

    private static class UserSession {
        User user;
        long loginTime;

        UserSession(User user) {
            this.user = user;
            this.loginTime = System.currentTimeMillis();
        }
    }
}