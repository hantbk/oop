package com.hust.quiz.Views;

import com.hust.quiz.Controllers.HomeController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private static ViewFactory instance;
    private final Stage stage;
    private Scene homeScene;
    private Scene questionBankScene;
    private Scene addQuestion;
    private Scene addQuizScene;

    // singleton design pattern
    private ViewFactory() {
        stage = new Stage();

        FXMLLoader home = new FXMLLoader(getClass().getResource("/Fxml/HomeView.fxml"));
        FXMLLoader multipleChoiceView = new FXMLLoader(getClass().getResource("/Fxml/AddQuestionView.fxml"));
        FXMLLoader addQuizView = new FXMLLoader(getClass().getResource("/Fxml/AddQuizView.fxml"));

        try {
            homeScene = new Scene(home.load());
            // because we need to access to QuestionBankController in HomeController to set TabPane
            HomeController homeController = home.getController();
            questionBankScene = new Scene(homeController.getQuestionBankView());
            addQuestion = new Scene(multipleChoiceView.load());
            addQuizScene = new Scene(addQuizView.load());

        } catch (IOException e) {
            System.out.println("Error to load fxml");
            e.printStackTrace();
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
            case ADD_QUESTION: {
                stage.setScene(addQuestion);
                break;
            }
            case ADD_QUIZ: {
                stage.setScene(addQuizScene);
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
        ADD_QUESTION,
        ADD_QUIZ
    }
}