module com.example.assig3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.assig3 to javafx.fxml;
    exports com.example.assig3;
    exports Connection;
    opens Connection to javafx.fxml;
    exports Model;
    opens Model to javafx.fxml;
    exports Business;
    opens Business to javafx.fxml;
    exports Dao;
    opens Dao to javafx.fxml;
    exports Sstart;
    opens Sstart to javafx.fxml;
    exports Presentation;
    opens Presentation to javafx.fxml;
}