package com.hrmpayroll.model;

import com.hrmpayroll.model.enums.LeaveType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Leave request management
 */
public class LeaveRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String employeeId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED
    private String reviewedBy;

    public LeaveRequest(String employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate) {
        this.requestId = "LV" + System.currentTimeMillis() % 100000;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "PENDING";
    }

    public long getDurationDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public String getEmployeeId() { return employeeId; }
    public LeaveType getLeaveType() { return leaveType; }
    public void setLeaveType(LeaveType leaveType) { this.leaveType = leaveType; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
}