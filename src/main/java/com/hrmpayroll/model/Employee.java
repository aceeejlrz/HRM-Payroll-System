package com.hrmpayroll.model;

import com.hrmpayroll.model.enums.DepartmentType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

/**
 * Employee model with comprehensive attributes
 */
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private String employeeId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private DepartmentType department;
    private String position;
    private double hourlyRate;
    private double annualSalary;
    private boolean isFullTime;
    private String sinNumber; // Social Insurance Number
    private LocalDate dateOfBirth;
    private String emergencyContact;

    public Employee(String firstName, String lastName, String email, DepartmentType department) {
        this.employeeId = generateEmployeeId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.hireDate = LocalDate.now();
        this.isFullTime = true;
    }

    private String generateEmployeeId() {
        return "EMP" + System.currentTimeMillis() % 100000;
    }

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public DepartmentType getDepartment() { return department; }
    public void setDepartment(DepartmentType department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public double getAnnualSalary() { return annualSalary; }
    public void setAnnualSalary(double annualSalary) { this.annualSalary = annualSalary; }
    public boolean isFullTime() { return isFullTime; }
    public void setFullTime(boolean fullTime) { isFullTime = fullTime; }
    public String getSinNumber() { return sinNumber; }
    public void setSinNumber(String sinNumber) { this.sinNumber = sinNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
}