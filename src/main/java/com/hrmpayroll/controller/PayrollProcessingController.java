package com.hrmpayroll.controller;

import com.hrmpayroll.model.Employee;
import com.hrmpayroll.model.Payroll;
import com.hrmpayroll.service.DataStorageService;
import com.hrmpayroll.service.PayrollCalculator;
import com.hrmpayroll.service.ReportGenerator; // ADDED MISSING IMPORT
import java.time.YearMonth;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.scene.control.ListCell;

/**
 * Payroll processing and calculation controller
 */
public class PayrollProcessingController {

    @FXML private ComboBox<Employee> employeeCombo;
    @FXML private ComboBox<YearMonth> periodCombo;
    @FXML private TextField hoursField;
    @FXML private TextField bonusField;
    @FXML private Button calculateBtn;
    @FXML private Button processBtn;
    @FXML private TextArea resultArea;

    private final DataStorageService storage;
    private final PayrollCalculator calculator;
    private Payroll currentPayroll;

    public PayrollProcessingController() {
        this.storage = DataStorageService.getInstance();
        this.calculator = new PayrollCalculator();
    }

    @FXML
    public void initialize() {
        setupComboBoxes();
        setupButtons();
        resultArea.setEditable(false);
        resultArea.setStyle("-fx-font-family: monospace;");
    }

    private void setupComboBoxes() {
        // Load employees
        employeeCombo.setItems(FXCollections.observableArrayList(storage.getAllEmployees()));
        employeeCombo.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>() {
            @Override
            public ListCell<Employee> call(ListView<Employee> listView) {
                return new ListCell<Employee>() {
                    @Override
                    protected void updateItem(Employee emp, boolean empty) {
                        super.updateItem(emp, empty);
                        setText(empty || emp == null ? null : emp.getFullName() + " (" + emp.getEmployeeId() + ")");
                    }
                };
            }
        });

        // Load recent pay periods
        YearMonth current = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            periodCombo.getItems().add(current.minusMonths(i));
        }
        periodCombo.setValue(current);
    }

    private void setupButtons() {
        calculateBtn.setOnAction(e -> calculatePayroll());
        processBtn.setOnAction(e -> processPayroll());
        processBtn.setDisable(true);
    }

    private void calculatePayroll() {
        if (!validateInputs()) return;

        Employee employee = employeeCombo.getValue();
        YearMonth period = periodCombo.getValue();
        double hours = Double.parseDouble(hoursField.getText());
        double bonus = bonusField.getText().isEmpty() ? 0 : Double.parseDouble(bonusField.getText());

        currentPayroll = calculator.calculatePayroll(employee, hours, bonus, period);

        // FIXED: Removed fully qualified class name
        String report = new ReportGenerator()
                .generateEmployeePayrollReport(employee.getEmployeeId(), period);

        resultArea.setText(report);
        processBtn.setDisable(false);
    }

    private void processPayroll() {
        if (currentPayroll == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Process Payroll");
        alert.setHeaderText("Confirm Payroll Processing");
        alert.setContentText("Process payment of $" + String.format("%.2f", currentPayroll.getNetPay()) + " ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                storage.savePayroll(currentPayroll);
                showInfo("Payroll processed successfully!\nPayroll ID: " + currentPayroll.getPayrollId());
                processBtn.setDisable(true);
                clearFields();
            }
        });
    }

    private boolean validateInputs() {
        if (employeeCombo.getValue() == null) {
            showError("Please select an employee");
            return false;
        }
        if (periodCombo.getValue() == null) {
            showError("Please select a pay period");
            return false;
        }
        try {
            double hours = Double.parseDouble(hoursField.getText());
            if (hours < 0 || hours > 168) { // Max hours in a week
                showError("Hours must be between 0 and 168");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter valid hours worked");
            return false;
        }
        return true;
    }

    private void clearFields() {
        hoursField.clear();
        bonusField.clear();
        resultArea.clear();
        currentPayroll = null;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }
}