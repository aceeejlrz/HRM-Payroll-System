package com.hrmpayroll.controller;

import com.hrmpayroll.Main;
import com.hrmpayroll.model.enums.UserRole;
import com.hrmpayroll.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

/**
 * Main dashboard controller with role-based navigation
 */
public class DashboardController {

    @FXML private BorderPane mainContainer;
    @FXML private Label welcomeLabel;
    @FXML private Label companyLabel;
    @FXML private VBox navMenu;
    @FXML private Button employeesBtn;
    @FXML private Button payrollBtn;
    @FXML private Button reportsBtn;
    @FXML private Button attendanceBtn;
    @FXML private Button logoutBtn;

    private final AuthenticationService authService;

    public DashboardController() {
        this.authService = AuthenticationService.getInstance();
    }

    @FXML
    public void initialize() {
        setupNavigation();
        updateWelcomeMessage();
        applyRoleBasedAccess();
    }

    private void setupNavigation() {
        employeesBtn.setOnAction(e -> loadView("/com/hrmpayroll/view/EmployeeManagement.fxml", "Employee Management"));
        payrollBtn.setOnAction(e -> loadView("/com/hrmpayroll/view/PayrollProcessing.fxml", "Payroll Processing"));
        reportsBtn.setOnAction(e -> loadView("/com/hrmpayroll/view/Reporting.fxml", "Reports & Analytics"));
        attendanceBtn.setOnAction(e -> loadView("/com/hrmpayroll/view/Attendance.fxml", "Attendance & Leave"));
        logoutBtn.setOnAction(e -> handleLogout());
    }

    private void updateWelcomeMessage() {
        var user = authService.getCurrentUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getFullName() + " (" + user.getRole().getDisplayName() + ")");
        }
    }

    private void applyRoleBasedAccess() {
        var user = authService.getCurrentUser();
        if (user == null) return;

        // HR_STAFF has limited access
        if (user.getRole() == UserRole.HR_STAFF) {
            reportsBtn.setDisable(true);
            reportsBtn.setStyle("-fx-opacity: 0.5;");
        }
    }

    private void loadView(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node node = loader.load();

            mainContainer.setCenter(node);
            mainContainer.setTop(createHeader(title));

        } catch (IOException e) {
            showError("Could not load view: " + e.getMessage());
        }
    }

    private TitledPane createHeader(String title) {
        TitledPane header = new TitledPane();
        header.setText(title);
        header.setCollapsible(false);
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return header;
    }

    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    authService.logout();
                    Main.loadLoginScene();
                } catch (Exception e) {
                    showError("Logout failed: " + e.getMessage());
                }
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}