module com.hust.quiz {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    requires org.apache.poi.ooxml;

//    requires poi.ooxml;

    requires poi.ooxml;
    requires org.apache.pdfbox;
    requires java.desktop;
    requires javafx.swing;


    opens com.hust.quiz to javafx.fxml;
    exports com.hust.quiz;
    exports com.hust.quiz.Controllers;
    exports com.hust.quiz.Models;
    opens com.hust.quiz.Controllers to javafx.fxml;
}