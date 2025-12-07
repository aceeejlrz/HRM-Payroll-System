package com.hrmpayroll.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Attendance tracking for employees
 */
public class AttendanceRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private String recordId;
    private String employeeId;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private String status; // PRESENT, LATE, ABSENT

    public AttendanceRecord(String employeeId) {
        this.recordId = "ATT" + System.currentTimeMillis() % 100000;
        this.employeeId = employeeId;
        this.clockInTime = LocalDateTime.now();
        this.status = "PRESENT";
    }

    public long getHoursWorked() {
        if (clockInTime == null || clockOutTime == null) return 0;
        return java.time.Duration.between(clockInTime, clockOutTime).toHours();
    }

    // Getters and Setters
    public String getRecordId() { return recordId; }
    public String getEmployeeId() { return employeeId; }
    public LocalDateTime getClockInTime() { return clockInTime; }
    public LocalDateTime getClockOutTime() { return clockOutTime; }
    public void setClockOutTime(LocalDateTime clockOutTime) { this.clockOutTime = clockOutTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}