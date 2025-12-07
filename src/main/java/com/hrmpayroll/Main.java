package com.hrmpayroll;

import com.hrmpayroll.model.User;
import com.hrmpayroll.model.enums.UserRole;
import com.hrmpayroll.service.AuthenticationService;
import com.hrmpayroll.service.DataStorageService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for HRM Payroll System
 * Group Members: Amran Hawaga, Jezrel Dela Cruz, Jared Duldulao
 * Roles:
 * - Lead Developer & Architect: Amran Hawaga
 * - UI/UX Designer & QA Engineer: Jezrel Dela Cruz
 * - Database Manager & Technical Writer: Jared Duldulao
 */
public class Main extends Application {

    public static final String APP_TITLE = "HRM & Payroll System v2.0";
    public static final String VERSION = "2.0";
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        initializeApplication();
        loadInitialLoginScene();
    }

    /**
     * Initializes application data and creates default admin user if first run
     */
    private void initializeApplication() {
        try {
            DataStorageService storage = DataStorageService.getInstance();

            // Create default admin user if no users exist
            if (storage.getAllUsers().isEmpty()) {
                User admin = new User(
                        "admin",
                        "admin123",
                        UserRole.ADMIN,
                        "System Administrator"
                );
                storage.saveUser(admin);
                System.out.println("Default admin created: username='admin', password='admin123'");
            }
        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadInitialLoginScene() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/hrmpayroll/view/Login.fxml"));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setTitle(APP_TITLE + " - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Switches scene to main dashboard
     */
    public static void switchToDashboard() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/hrmpayroll/view/Dashboard.fxml"));
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());

        primaryStage.setTitle(APP_TITLE + " - Dashboard");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(true);
    }

    /**
     * Switches scene back to login (static method for controllers)
     */
    public static void loadLoginScene() throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("/com/hrmpayroll/view/Login.fxml"));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(Main.class.getResource("/style.css").toExternalForm());

        primaryStage.setTitle(APP_TITLE + " - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}