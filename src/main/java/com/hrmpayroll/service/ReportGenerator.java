package com.hrmpayroll.service;

import com.hrmpayroll.model.*;
import com.hrmpayroll.model.enums.DepartmentType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates comprehensive payroll and HR reports
 */
public class ReportGenerator {

    private final DataStorageService storage;
    private final PayrollCalculator calculator;

    public ReportGenerator() {
        this.storage = DataStorageService.getInstance();
        this.calculator = new PayrollCalculator();
    }

    public ObservableList<Employee> getDepartmentEmployees(DepartmentType dept) {
        Department department = storage.getDepartment(dept);
        return FXCollections.observableArrayList(
                department.getEmployeeIds().stream()
                        .map(storage::getEmployee) // FIXED: Now works with added method
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    public double getDepartmentPayrollTotal(DepartmentType dept, YearMonth period) {
        Department department = storage.getDepartment(dept);
        return department.getEmployeeIds().stream()
                .flatMap(id -> storage.getPayrollsByEmployee(id).stream())
                .filter(p -> p.getPayPeriod().equals(period))
                .mapToDouble(Payroll::getNetPay)
                .sum();
    }

    public Map<String, Double> getPayrollSummaryByDepartment(YearMonth period) {
        Map<String, Double> summary = new HashMap<>();
        for (DepartmentType dept : DepartmentType.values()) {
            double total = getDepartmentPayrollTotal(dept, period);
            summary.put(dept.getDisplayName(), total);
        }
        return summary;
    }

    public String generateEmployeePayrollReport(String employeeId, YearMonth period) {
        // FIXED: Now works with added method
        Employee emp = storage.getEmployee(employeeId);
        List<Payroll> payrolls = storage.getPayrollsByEmployee(employeeId).stream()
                .filter(p -> p.getPayPeriod().equals(period))
                .collect(Collectors.toList());

        if (payrolls.isEmpty() || emp == null) return "No payroll data found";

        Payroll payroll = payrolls.get(0);
        StringBuilder report = new StringBuilder();

        report.append("===================================================\n");
        report.append("         PAYROLL REPORT FOR EMPLOYEE\n");
        report.append("===================================================\n");
        report.append(String.format("Employee: %s (ID: %s)\n", emp.getFullName(), emp.getEmployeeId()));
        report.append(String.format("Department: %s\n", emp.getDepartment().getDisplayName()));
        report.append(String.format("Pay Period: %s\n", period));
        report.append(String.format("Pay Date: %s\n\n", payroll.getPayDate()));

        report.append("EARNINGS:\n");
        report.append(String.format("  Regular Hours: %.2f @ $%.2f/hr = $%.2f\n",
                payroll.getRegularHours(), payroll.getRegularPay()/payroll.getRegularHours(), payroll.getRegularPay()));
        report.append(String.format("  Overtime Hours: %.2f = $%.2f\n", payroll.getOvertimeHours(), payroll.getOvertimePay()));
        report.append(String.format("  Bonus: $%.2f\n", payroll.getBonusAmount()));
        report.append(String.format("  Gross Pay: $%.2f\n\n", payroll.getGrossPay()));

        report.append("DEDUCTIONS:\n");
        report.append(String.format("  Federal Tax: $%.2f\n", payroll.getFederalTax()));
        report.append(String.format("  Provincial Tax: $%.2f\n", payroll.getProvincialTax()));
        report.append(String.format("  CPP: $%.2f\n", payroll.getCppContribution()));
        report.append(String.format("  EI: $%.2f\n", payroll.getEiContribution()));
        report.append(String.format("  Total Deductions: $%.2f\n\n", payroll.getTotalDeductions()));

        report.append(String.format("NET PAY: $%.2f\n", payroll.getNetPay()));
        report.append("===================================================\n");

        return report.toString();
    }
}