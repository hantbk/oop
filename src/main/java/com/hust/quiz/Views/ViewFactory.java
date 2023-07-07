package com.hust.quiz.Views;

import com.hust.quiz.Controllers.*;
import com.hust.quiz.Models.Question;
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
    private Scene quizViewScene;
    private Scene editQuizScene;
    private Scene startQuizScene;
    private QuestionBankController questionBankController;
    private QuizViewController quizViewController;
    private EditQuizController editQuizController;
    private AddQuestionController addQuestionController;
    private StartViewController startViewController;
    // singleton design pattern
    private ViewFactory() {
        stage = new Stage();

        FXMLLoader home = new FXMLLoader(getClass().getResource("/Fxml/HomeView.fxml"));
        FXMLLoader addQuestionView = new FXMLLoader(getClass().getResource("/Fxml/AddQuestionView.fxml"));
        FXMLLoader quizView = new FXMLLoader(getClass().getResource("/Fxml/QuizView.fxml"));
        FXMLLoader addQuizView = new FXMLLoader(getClass().getResource("/Fxml/AddQuizView.fxml"));
        FXMLLoader editQuizView = new FXMLLoader(getClass().getResource("/Fxml/EditQuizView.fxml"));
        FXMLLoader startQuizView = new FXMLLoader(getClass().getResource("/Fxml/StartView.fxml"));
        try {
            homeScene = new Scene(home.load());

            // because we need to access to QuestionBankController in HomeController to set TabPane
            HomeController homeController = home.getController();
            questionBankController = homeController.getQuestionBankController();
            questionBankScene = new Scene(homeController.getQuestionBankView());

            addQuestion = new Scene(addQuestionView.load());
            addQuestionController = addQuestionView.getController();

            quizViewScene = new Scene(quizView.load());
            quizViewController = quizView.getController();

            addQuizScene = new Scene(addQuizView.load());

            editQuizScene = new Scene(editQuizView.load());
            editQuizController = editQuizView.getController();

            startQuizScene = new Scene(startQuizView.load());
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

    public void updateQuizView(String quizName, String quizInfo) {
        quizViewController.displayInfo(quizName, quizInfo);
    }

    public void updateEditQuizView(String quizName) {
        editQuizController.editQuizDisplayInfo(quizName);
    }

    //ham nay de dien thong tin question vao cac o khi bam vao edit ow questionBank
    public void updateInforEditQuestion(Question question, String category_name){ addQuestionController.setInfor(question, category_name);}

    public void routes(SCENES scene) {
        switch (scene) {
            case HOME: {
                stage.setScene(homeScene);
                break;
            }
            case QUESTION_BANK: {
                questionBankController.load();
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
            case QUIZ_VIEW: {
                stage.setScene(quizViewScene);
                break;
            }
            case EDIT_QUIZ: {
                stage.setScene(editQuizScene);
                break;
            }
            case START_QUIZ: {
                stage.setScene(startQuizScene);
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
        ADD_QUIZ,
        QUIZ_VIEW,
        EDIT_QUIZ,
        START_QUIZ
    }
}