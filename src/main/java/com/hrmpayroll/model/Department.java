package com.hrmpayroll.model;

import com.hrmpayroll.model.enums.DepartmentType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Department model containing employees
 */
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    private DepartmentType type;
    private List<String> employeeIds; // Store IDs for serialization
    private double budget;

    public Department(DepartmentType type) {
        this.type = type;
        this.employeeIds = new ArrayList<>();
        this.budget = 500000.0; // Default budget
    }

    public void addEmployee(String employeeId) {
        if (!employeeIds.contains(employeeId)) {
            employeeIds.add(employeeId);
        }
    }

    public void removeEmployee(String employeeId) {
        employeeIds.remove(employeeId);
    }

    // Getters and Setters
    public DepartmentType getType() { return type; }
    public List<String> getEmployeeIds() { return new ArrayList<>(employeeIds); }
    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = budget; }
}