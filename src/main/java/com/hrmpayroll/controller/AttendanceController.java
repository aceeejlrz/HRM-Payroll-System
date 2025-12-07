package com.hrmpayroll.controller;

import com.hrmpayroll.model.AttendanceRecord;
import com.hrmpayroll.model.Employee;
import com.hrmpayroll.model.LeaveRequest;
import com.hrmpayroll.model.enums.LeaveType;
import com.hrmpayroll.service.DataStorageService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.time.LocalDateTime; // ADDED MISSING IMPORT

/**
 * Attendance and leave management controller
 */
public class AttendanceController {

    @FXML private TabPane attendanceTabs;
    @FXML private ComboBox<Employee> employeeCombo;
    @FXML private Button clockInBtn;
    @FXML private Button clockOutBtn;

    @FXML private TableView<AttendanceRecord> attendanceTable;
    @FXML private TableColumn<AttendanceRecord, String> dateCol;
    @FXML private TableColumn<AttendanceRecord, String> hoursCol;

    @FXML private ComboBox<Employee> leaveEmployeeCombo;
    @FXML private ComboBox<LeaveType> leaveTypeCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextArea reasonArea;
    @FXML private Button requestLeaveBtn;

    @FXML private TableView<LeaveRequest> leaveTable;
    @FXML private TableColumn<LeaveRequest, String> leaveIdCol;
    @FXML private TableColumn<LeaveRequest, String> leaveEmpCol;
    @FXML private TableColumn<LeaveRequest, String> leaveTypeCol;
    @FXML private TableColumn<LeaveRequest, String> leaveStatusCol;

    private final DataStorageService storage;

    public AttendanceController() {
        this.storage = DataStorageService.getInstance();
    }

    @FXML
    public void initialize() {
        setupAttendanceTab();
        setupLeaveTab();
    }

    private void setupAttendanceTab() {
        employeeCombo.setItems(FXCollections.observableArrayList(storage.getAllEmployees()));

        clockInBtn.setOnAction(e -> handleClockIn());
        clockOutBtn.setOnAction(e -> handleClockOut());

        dateCol.setCellValueFactory(new PropertyValueFactory<>("clockInTime"));
        hoursCol.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
    }

    private void handleClockIn() {
        Employee emp = employeeCombo.getValue();
        if (emp == null) {
            showAlert("Select an employee first");
            return;
        }

        AttendanceRecord record = new AttendanceRecord(emp.getEmployeeId());
        storage.saveAttendanceRecord(record);
        showAlert("Clock-in successful at " + record.getClockInTime());
        loadAttendanceData(emp);
    }

    private void handleClockOut() {
        var records = storage.getAttendanceByEmployee(employeeCombo.getValue().getEmployeeId());
        if (!records.isEmpty() && records.get(0).getClockOutTime() == null) {
            var record = records.get(0);
            // FIXED: Removed fully qualified name, using imported LocalDateTime
            record.setClockOutTime(LocalDateTime.now());
            storage.saveAttendanceRecord(record);
            showAlert("Clock-out successful. Hours: " + record.getHoursWorked());
        }
    }

    private void loadAttendanceData(Employee emp) {
        attendanceTable.setItems(FXCollections.observableArrayList(
                storage.getAttendanceByEmployee(emp.getEmployeeId())
        ));
    }

    private void setupLeaveTab() {
        leaveEmployeeCombo.setItems(FXCollections.observableArrayList(storage.getAllEmployees()));
        leaveTypeCombo.setItems(FXCollections.observableArrayList(LeaveType.values()));
        requestLeaveBtn.setOnAction(e -> handleLeaveRequest());

        leaveIdCol.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        leaveEmpCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        leaveTypeCol.setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        leaveStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadLeaveRequests();
    }

    private void handleLeaveRequest() {
        Employee emp = leaveEmployeeCombo.getValue();
        LeaveType type = leaveTypeCombo.getValue();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (emp == null || type == null || start == null || end == null) {
            showAlert("Please fill all required fields");
            return;
        }

        LeaveRequest request = new LeaveRequest(emp.getEmployeeId(), type, start, end);
        request.setReason(reasonArea.getText());
        storage.saveLeaveRequest(request);

        showAlert("Leave request submitted successfully");
        clearLeaveForm();
        loadLeaveRequests();
    }

    private void loadLeaveRequests() {
        leaveTable.setItems(FXCollections.observableArrayList(
                storage.getLeaveRequests()
        ));
    }

    private void clearLeaveForm() {
        leaveEmployeeCombo.getSelectionModel().clearSelection();
        leaveTypeCombo.getSelectionModel().clearSelection();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        reasonArea.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}