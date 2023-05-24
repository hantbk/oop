package com.hust.quiz.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private static ViewFactory instance;
    private final Stage stage;
    private Scene homeScene;
    private Scene questionBankScene;
    private Scene multipleChoiceScene;
    //private Scene choiceQuestionView;

    // singleton design pattern
    private ViewFactory() {
        stage = new Stage();

        FXMLLoader home = new FXMLLoader(getClass().getResource("/Fxml/HomeView.fxml"));
        FXMLLoader questionBankView = new FXMLLoader(getClass().getResource("/Fxml/QuestionBankView.fxml"));
        FXMLLoader multipleChoiceView = new FXMLLoader(getClass().getResource("/Fxml/MultipleChoiceQuestion.fxml"));

        try {
            homeScene = new Scene(home.load());
            questionBankScene = new Scene(questionBankView.load());
            multipleChoiceScene = new Scene(multipleChoiceView.load());

        } catch (IOException e) {
            System.out.println("Error to load fxml");
            e.printStackTrace();
        }

        //stage.setScene(multipleChoiceQuestionScene);
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
            case MULTI_CHOICE: {
                stage.setScene(multipleChoiceScene);
                break;
            }
            default: {
                stage.setScene(homeScene);
            }
        }
    }

    public enum SCENES {
        HOME,
        QUESTION_BANK,
        MULTI_CHOICE
    }
}