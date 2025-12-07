package com.hrmpayroll.model.enums;

/**
 * Department types in the organization
 */
public enum DepartmentType {
    HR("Human Resources"),
    IT("Information Technology"),
    FINANCE("Finance"),
    MARKETING("Marketing"),
    OPERATIONS("Operations"),
    SALES("Sales");

    private final String displayName;

    DepartmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}