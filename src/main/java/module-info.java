module com.hust.quiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.hust.quiz to javafx.fxml;
    exports com.hust.quiz;
    exports com.hust.quiz.Controllers;
    opens com.hust.quiz.Controllers to javafx.fxml;
}