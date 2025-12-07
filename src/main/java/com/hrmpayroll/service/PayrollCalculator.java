package com.hrmpayroll.service;

import com.hrmpayroll.model.Employee;
import com.hrmpayroll.model.Payroll;
import com.hrmpayroll.service.ReportGenerator;
import java.time.YearMonth;

/**
 * Comprehensive payroll calculation engine
 * Handles taxes, deductions, overtime, and benefits
 */
public class PayrollCalculator {

    // 2025 Tax rates for Canada (Ontario)
    private static final double FEDERAL_TAX_RATE = 0.15; // 15% on first $53,359
    private static final double PROVINCIAL_TAX_RATE = 0.0505; // 5.05% Ontario
    private static final double CPP_RATE = 0.0595; // 5.95% up to max $3,754.45
    private static final double EI_RATE = 0.0164; // 1.64% up to max $1,077.48
    private static final double CPP_MAX = 3754.45;
    private static final double EI_MAX = 1077.48;

    private static final double OVERTIME_RATE = 1.5; // 1.5x after 40 hours
    private static final double STANDARD_HOURS = 40.0;

    public Payroll calculatePayroll(Employee employee, double hoursWorked, double bonusAmount, YearMonth period) {
        Payroll payroll = new Payroll(employee.getEmployeeId(), period);

        // Calculate earnings
        double hourlyRate = employee.isFullTime() ?
                employee.getAnnualSalary() / (52 * 40) : employee.getHourlyRate();

        double regularHours = Math.min(hoursWorked, STANDARD_HOURS);
        double overtimeHours = Math.max(0, hoursWorked - STANDARD_HOURS);

        double regularPay = regularHours * hourlyRate;
        double overtimePay = overtimeHours * hourlyRate * OVERTIME_RATE;
        double grossPay = regularPay + overtimePay + bonusAmount;

        // Calculate deductions
        double cpp = Math.min(grossPay * CPP_RATE, CPP_MAX / 12);
        double ei = Math.min(grossPay * EI_RATE, EI_MAX / 12);
        double federalTax = (grossPay - cpp - ei) * FEDERAL_TAX_RATE;
        double provincialTax = (grossPay - cpp - ei) * PROVINCIAL_TAX_RATE;
        double totalDeductions = federalTax + provincialTax + cpp + ei;

        double netPay = grossPay - totalDeductions;

        // Set payroll values
        payroll.setRegularHours(regularHours);
        payroll.setOvertimeHours(overtimeHours);
        payroll.setRegularPay(regularPay);
        payroll.setOvertimePay(overtimePay);
        payroll.setBonusAmount(bonusAmount);
        payroll.setGrossPay(grossPay);
        payroll.setFederalTax(federalTax);
        payroll.setProvincialTax(provincialTax);
        payroll.setCppContribution(cpp);
        payroll.setEiContribution(ei);
        payroll.setTotalDeductions(totalDeductions);
        payroll.setNetPay(netPay);

        return payroll;
    }

    public double calculateYearToDate(String employeeId) {
        DataStorageService storage = DataStorageService.getInstance();
        return storage.getPayrollsByEmployee(employeeId).stream()
                .mapToDouble(Payroll::getNetPay)
                .sum();
    }
}