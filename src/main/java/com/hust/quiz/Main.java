package com.hust.quiz;

import com.hust.quiz.Views.ViewFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/MultipleChoiceQuestion.fxml"));
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.setResizable(false);
//        stage.show();
        ViewFactory view = ViewFactory.getInstance();
    }


}
