package com.hrmpayroll.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Payroll record for employee compensation
 */
public class Payroll implements Serializable {
    private static final long serialVersionUID = 1L;

    private String payrollId;
    private String employeeId;
    private YearMonth payPeriod;
    private LocalDate payDate;

    // Earnings
    private double regularHours;
    private double overtimeHours;
    private double regularPay;
    private double overtimePay;
    private double bonusAmount;
    private double grossPay;

    // Deductions
    private double federalTax;
    private double provincialTax;
    private double cppContribution;
    private double eiContribution;
    private double totalDeductions;

    // Net Pay
    private double netPay;

    public Payroll(String employeeId, YearMonth payPeriod) {
        this.payrollId = "PR" + System.currentTimeMillis() % 100000;
        this.employeeId = employeeId;
        this.payPeriod = payPeriod;
        this.payDate = LocalDate.now().plusDays(14); // Pay 2 weeks after period
    }

    // Getters and Setters
    public String getPayrollId() { return payrollId; }
    public String getEmployeeId() { return employeeId; }
    public YearMonth getPayPeriod() { return payPeriod; }
    public LocalDate getPayDate() { return payDate; }
    public void setPayDate(LocalDate payDate) { this.payDate = payDate; }

    public double getRegularHours() { return regularHours; }
    public void setRegularHours(double regularHours) { this.regularHours = regularHours; }
    public double getOvertimeHours() { return overtimeHours; }
    public void setOvertimeHours(double overtimeHours) { this.overtimeHours = overtimeHours; }
    public double getRegularPay() { return regularPay; }
    public void setRegularPay(double regularPay) { this.regularPay = regularPay; }
    public double getOvertimePay() { return overtimePay; }
    public void setOvertimePay(double overtimePay) { this.overtimePay = overtimePay; }
    public double getBonusAmount() { return bonusAmount; }
    public void setBonusAmount(double bonusAmount) { this.bonusAmount = bonusAmount; }
    public double getGrossPay() { return grossPay; }
    public void setGrossPay(double grossPay) { this.grossPay = grossPay; }

    public double getFederalTax() { return federalTax; }
    public void setFederalTax(double federalTax) { this.federalTax = federalTax; }
    public double getProvincialTax() { return provincialTax; }
    public void setProvincialTax(double provincialTax) { this.provincialTax = provincialTax; }
    public double getCppContribution() { return cppContribution; }
    public void setCppContribution(double cppContribution) { this.cppContribution = cppContribution; }
    public double getEiContribution() { return eiContribution; }
    public void setEiContribution(double eiContribution) { this.eiContribution = eiContribution; }
    public double getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(double totalDeductions) { this.totalDeductions = totalDeductions; }

    public double getNetPay() { return netPay; }
    public void setNetPay(double netPay) { this.netPay = netPay; }
}