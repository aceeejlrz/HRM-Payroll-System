package com.hrmpayroll.controller;

import com.hrmpayroll.Main;
import com.hrmpayroll.service.AuthenticationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Login controller with authentication and security
 */
public class LoginController {

    @FXML private VBox loginContainer;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    @FXML private Hyperlink forgotPasswordLink;

    private final AuthenticationService authService;
    private int loginAttempts;

    public LoginController() {
        this.authService = AuthenticationService.getInstance();
        this.loginAttempts = 0;
    }

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        loginButton.setDefaultButton(true);

        // Add listeners for real-time validation
        usernameField.textProperty().addListener((obs, old, newVal) -> validateInputs());
        passwordField.textProperty().addListener((obs, old, newVal) -> validateInputs());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }

        try {
            if (authService.login(username, password)) {
                // Login successful
                loginAttempts = 0;
                Main.switchToDashboard();
            } else {
                loginAttempts++;
                showError("Invalid credentials. Attempt " + loginAttempts + "/3");

                if (loginAttempts >= 3) {
                    loginButton.setDisable(true);
                    showError("Account locked. Contact administrator.");
                }
            }
        } catch (Exception e) {
            showError("System error: " + e.getMessage());
        }
    }

    private void validateInputs() {
        boolean isValid = !usernameField.getText().trim().isEmpty() &&
                !passwordField.getText().isEmpty();
        loginButton.setDisable(!isValid);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #e74c3c;");
    }

    @FXML
    private void handleForgotPassword() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Reset");
        alert.setHeaderText("Contact System Administrator");
        alert.setContentText("To reset your password, please contact the HR department at ext. 5500");
        alert.showAndWait();
    }
}