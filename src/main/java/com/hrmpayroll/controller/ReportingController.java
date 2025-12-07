package com.hrmpayroll.controller;

import com.hrmpayroll.model.enums.DepartmentType;
import com.hrmpayroll.service.DataStorageService;
import com.hrmpayroll.service.ReportGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.time.YearMonth;
import java.util.Map;
import java.util.Objects;

/**
 * Reports and analytics controller with data visualization
 */
public class ReportingController {

    @FXML private ComboBox<DepartmentType> deptCombo;
    @FXML private ComboBox<YearMonth> periodCombo;
    @FXML private Button generateBtn;
    @FXML private TabPane reportTabs;
    @FXML private TextArea summaryText;
    @FXML private AnchorPane chartPane;

    private final DataStorageService storage;
    private final ReportGenerator generator;

    public ReportingController() {
        this.storage = DataStorageService.getInstance();
        this.generator = new ReportGenerator();
    }

    @FXML
    public void initialize() {
        setupControls();
        generateBtn.setOnAction(e -> generateReport());
    }

    private void setupControls() {
        deptCombo.setItems(FXCollections.observableArrayList(DepartmentType.values()));
        deptCombo.getItems().add(0, null); // All departments option
        deptCombo.getSelectionModel().select(0);

        // Load recent periods
        YearMonth current = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            periodCombo.getItems().add(current.minusMonths(i));
        }
        periodCombo.setValue(current);
    }

    private void generateReport() {
        DepartmentType dept = deptCombo.getValue();
        YearMonth period = periodCombo.getValue();

        // Generate summary text
        if (dept == null) {
            generateCompanyWideReport(period);
        } else {
            generateDepartmentReport(dept, period);
        }

        // Generate charts
        generatePayrollChart();
    }

    private void generateCompanyWideReport(YearMonth period) {
        StringBuilder sb = new StringBuilder();
        sb.append("COMPANY-WIDE PAYROLL SUMMARY\n");
        sb.append("Period: ").append(period).append("\n\n");

        Map<String, Double> summary = generator.getPayrollSummaryByDepartment(period);
        double total = 0;
        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            sb.append(String.format("%-20s: $%,12.2f\n", entry.getKey(), entry.getValue()));
            total += entry.getValue();
        }
        sb.append(String.format("\n%-20s: $%,12.2f\n", "TOTAL", total));

        summaryText.setText(sb.toString());
    }

    private void generateDepartmentReport(DepartmentType dept, YearMonth period) {
        var employees = generator.getDepartmentEmployees(dept);
        double total = generator.getDepartmentPayrollTotal(dept, period);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("DEPARTMENT REPORT: %s\n", dept.getDisplayName()));
        sb.append("Period: ").append(period).append("\n\n");
        sb.append(String.format("Total Employees: %d\n", employees.size()));
        sb.append(String.format("Total Payroll: $%,.2f\n\n", total));

        sb.append("EMPLOYEE BREAKDOWN:\n");
        for (var emp : employees) {
            var payrolls = storage.getPayrollsByEmployee(emp.getEmployeeId()).stream()
                    .filter(p -> p.getPayPeriod().equals(period))
                    .findFirst();
            payrolls.ifPresent(p ->
                    sb.append(String.format("  %-25s: $%,8.2f\n", emp.getFullName(), p.getNetPay()))
            );
        }

        summaryText.setText(sb.toString());
    }

    private void generatePayrollChart() {
        // Clear previous chart
        chartPane.getChildren().clear();

        // Create PieChart
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Payroll Distribution by Department");

        Map<String, Double> data = generator.getPayrollSummaryByDepartment(periodCombo.getValue());
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            if (entry.getValue() > 0) {
                pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }

        pieChart.setPrefSize(600, 400);
        pieChart.setLegendVisible(true);

        AnchorPane.setTopAnchor(pieChart, 10.0);
        AnchorPane.setBottomAnchor(pieChart, 10.0);
        AnchorPane.setLeftAnchor(pieChart, 10.0);
        AnchorPane.setRightAnchor(pieChart, 10.0);

        chartPane.getChildren().add(pieChart);
    }
}