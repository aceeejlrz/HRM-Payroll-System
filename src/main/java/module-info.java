module hrm.payroll.system {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.base;

    opens com.hrmpayroll.controller to javafx.fxml;
    opens com.hrmpayroll.model to javafx.base;
    opens com.hrmpayroll.model.enums to javafx.base;

    exports com.hrmpayroll;
}