package com.hrmpayroll.controller;

import com.hrmpayroll.model.Employee;
import com.hrmpayroll.model.enums.DepartmentType;
import com.hrmpayroll.service.DataStorageService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Employee CRUD operations controller
 */
public class EmployeeManagementController {

    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> idCol;
    @FXML private TableColumn<Employee, String> nameCol;
    @FXML private TableColumn<Employee, String> deptCol;
    @FXML private TableColumn<Employee, String> emailCol;
    @FXML private TableColumn<Employee, String> phoneCol;

    @FXML private TextField searchField;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button refreshBtn;

    private final DataStorageService storage;

    public EmployeeManagementController() {
        this.storage = DataStorageService.getInstance();
    }

    @FXML
    public void initialize() {
        setupTable();
        loadEmployees();
        setupSearch();
        setupButtons();
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void loadEmployees() {
        employeeTable.setItems(FXCollections.observableArrayList(storage.getAllEmployees()));
    }

    private void setupSearch() {
        searchField.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                loadEmployees();
            } else {
                var filtered = storage.getAllEmployees().stream()
                        .filter(emp -> emp.getFullName().toLowerCase().contains(newVal.toLowerCase()) ||
                                emp.getEmployeeId().toLowerCase().contains(newVal.toLowerCase()))
                        .collect(Collectors.toList());
                employeeTable.setItems(FXCollections.observableArrayList(filtered));
            }
        });
    }

    private void setupButtons() {
        addBtn.setOnAction(e -> showAddEditDialog(null));
        editBtn.setOnAction(e -> {
            Employee selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) showAddEditDialog(selected);
        });
        deleteBtn.setOnAction(e -> handleDelete());
        refreshBtn.setOnAction(e -> loadEmployees());
    }

    private void showAddEditDialog(Employee employee) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle(employee == null ? "Add New Employee" : "Edit Employee");

        // Form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 20px;");

        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        ComboBox<DepartmentType> deptCombo = new ComboBox<>();
        deptCombo.setItems(FXCollections.observableArrayList(DepartmentType.values()));
        TextField positionField = new TextField();
        TextField sinField = new TextField();
        DatePicker dobPicker = new DatePicker();
        TextField emergencyField = new TextField();

        // Populate for edit
        if (employee != null) {
            firstNameField.setText(employee.getFirstName());
            lastNameField.setText(employee.getLastName());
            emailField.setText(employee.getEmail());
            phoneField.setText(employee.getPhone());
            deptCombo.setValue(employee.getDepartment());
            positionField.setText(employee.getPosition());
            sinField.setText(employee.getSinNumber());
            dobPicker.setValue(employee.getDateOfBirth());
            emergencyField.setText(employee.getEmergencyContact());
        }

        grid.add(new Label("First Name:*"), 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(new Label("Last Name:*"), 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(new Label("Email:*"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Department:*"), 0, 3);
        grid.add(deptCombo, 1, 3);
        grid.add(new Label("Position:"), 0, 4);
        grid.add(positionField, 1, 4);
        grid.add(new Label("Phone:"), 0, 5);
        grid.add(phoneField, 1, 5);
        grid.add(new Label("SIN:"), 0, 6);
        grid.add(sinField, 1, 6);
        grid.add(new Label("Date of Birth:"), 0, 7);
        grid.add(dobPicker, 1, 7);
        grid.add(new Label("Emergency Contact:"), 0, 8);
        grid.add(emergencyField, 1, 8);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Convert result to employee
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                Employee emp = employee != null ? employee :
                        new Employee(firstNameField.getText(), lastNameField.getText(),
                                emailField.getText(), deptCombo.getValue());

                emp.setFirstName(firstNameField.getText());
                emp.setLastName(lastNameField.getText());
                emp.setEmail(emailField.getText());
                emp.setDepartment(deptCombo.getValue());
                emp.setPosition(positionField.getText());
                emp.setPhone(phoneField.getText());
                emp.setSinNumber(sinField.getText());
                emp.setDateOfBirth(dobPicker.getValue());
                emp.setEmergencyContact(emergencyField.getText());

                return emp;
            }
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();
        result.ifPresent(emp -> {
            storage.saveEmployee(emp);
            loadEmployees();
            showInfo(employee == null ? "Employee added successfully" : "Employee updated successfully");
        });
    }

    private void handleDelete() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showInfo("Please select an employee to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Employee");
        alert.setContentText("Are you sure you want to delete " + selected.getFullName() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                storage.deleteEmployee(selected.getEmployeeId());
                loadEmployees();
                showInfo("Employee deleted successfully");
            }
        });
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}