package com.hrmpayroll.model.enums;

/**
 * User roles for role-based access control
 */
public enum UserRole {
    ADMIN("System Administrator"),
    HR_MANAGER("HR Manager"),
    HR_STAFF("HR Staff");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}