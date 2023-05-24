package com.hust.quiz.Views;

import com.hust.quiz.Controllers.ChoiceQuestionController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ViewFactory {
    public enum SCENES {
        HOME,
        QUESTION_BANK,
        MULTIPLE_CHOICE_QUESTION
    }
    private final Stage stage;
    private Scene homeScene;
    private Scene questionBankScene;
    private Scene multipleChoiceQuestionScene;
    //private Scene choiceQuestionView;

    private static ViewFactory instance;
    // singleton design pattern
    private ViewFactory() {
        stage = new Stage();

        FXMLLoader home = new FXMLLoader(getClass().getResource("/Fxml/HomeView.fxml"));
        FXMLLoader questionBankView = new FXMLLoader(getClass().getResource("/Fxml/QuestionBankView.fxml"));
        FXMLLoader multipleChoiceQuestionView = new FXMLLoader(getClass().getResource("/Fxml/MultipleChoiceQuestionView.fxml"));
        //FXMLLoader choiceQuestionView = new FXMLLoader(getClass().getResource("/Fxml/ChoiceQuestionView.fxml"));

        try {
            homeScene = new Scene(home.load());
            questionBankScene = new Scene(questionBankView.load());
            multipleChoiceQuestionScene = new Scene(multipleChoiceQuestionView.load());

        } catch (IOException e) {
            System.out.println("Error to load fxml");
            System.out.println(e.getMessage());
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
            case MULTIPLE_CHOICE_QUESTION: {
                stage.setScene(multipleChoiceQuestionScene);
                break;
            }
            default: {
                stage.setScene(homeScene);
            }
        }
    }
}
