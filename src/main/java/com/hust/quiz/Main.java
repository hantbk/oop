package com.hust.quiz;

import com.hust.quiz.Models.Model;
import com.hust.quiz.Views.ViewFactory;
import javafx.application.Application;

import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ViewFactory view = ViewFactory.getInstance();
        Model module = Model.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }
}
