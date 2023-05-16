package com.hust.quiz.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    public enum SCENES {
        HOME,
        QUESTION_BANK
    }
    private final Stage stage;
    private Scene homeScene;
    private Scene questionBankScene;

    private static ViewFactory instance;
    // singleton design pattern
    private ViewFactory() {
        stage = new Stage();

        FXMLLoader home = new FXMLLoader(getClass().getResource("/Fxml/home-view.fxml"));
        FXMLLoader questionBankView = new FXMLLoader(getClass().getResource("/Fxml/question-bank.fxml"));

        try {
            homeScene = new Scene(home.load());
            questionBankScene = new Scene(questionBankView.load());

        } catch (IOException e) {
            System.out.println("Error to load fxml");
            System.out.println(e.getMessage());
        }

        stage.setScene(homeScene);
        stage.setResizable(false);
        stage.setTitle("Quiz App");
        stage.show();
    }
    public static ViewFactory getInstance() {
        if (instance == null) {
            instance = new ViewFactory();
        }
        return instance;
    }

    public void routes(SCENES scene) {
        switch (scene) {
            case HOME: {
                stage.setScene(homeScene);
                break;
            }
            case QUESTION_BANK: {
                stage.setScene(questionBankScene);
                break;
            }
            default: {
                stage.setScene(homeScene);
            }
        }
    }
}
