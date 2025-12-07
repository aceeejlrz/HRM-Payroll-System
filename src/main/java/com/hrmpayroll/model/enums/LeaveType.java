package com.hrmpayroll.model.enums;

/**
 * Types of leave requests
 */
public enum LeaveType {
    ANNUAL("Annual Leave"),
    SICK("Sick Leave"),
    MATERNITY("Maternity Leave"),
    PATERNITY("Paternity Leave"),
    UNPAID("Unpaid Leave");

    private final String displayName;

    LeaveType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}