module com.example.inventoryui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.inventoryui to javafx.fxml;
    exports com.inventoryui;
    exports com.inventory.DTO;
    exports com.inventory.DataSource;
}