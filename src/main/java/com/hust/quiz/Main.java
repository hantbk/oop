package com.hust.quiz;

import com.hust.quiz.Views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        ViewFactory.getInstance();
    }
}