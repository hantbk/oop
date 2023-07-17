package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class QuizViewController implements Initializable {
    @FXML
    private Label label_quiz_name_IT, label_quiz_name_view, label_quiz_description;
    @FXML
    private Label timeFormatLabel, timeLimitLabel; // second pane
    @FXML
    private Label timeFormatLabel1, timeLimitLabel1; // first pane
    @FXML
    private ImageView btn_edit_quiz; // click jump to EditQuizView - NOT done
    @FXML
    private Button btn_preview_quiz;
    @FXML
    private ImageView btn_menu_return; // return to homeView
    @FXML
    private AnchorPane second_pane, blur_pane;
    @FXML
    private ImageView btn_close_confirm;
    @FXML
    private Button btn_start_attempt, btn_cancel_attempt;
    private Quiz quiz;

    public void displayInfo(String quizName) {
        quiz = QuizService.getQuiz(quizName);
        if (quiz == null) {
            System.out.println("No quiz found with name: " + quizName);
            return;
        }
        label_quiz_name_IT.setText(quiz.getQuiz_name());
        label_quiz_name_view.setText(quiz.getQuiz_name());
        label_quiz_description.setText(quiz.getQuiz_description());

        if (quiz.getTimeLimit() > 0 && quiz.getTimeFormat() != null) {
            timeLimitLabel.setText(quiz.getTimeLimit() + " ");
            timeFormatLabel.setText(quiz.getTimeFormat());
            timeLimitLabel1.setText(quiz.getTimeLimit() + " ");
            timeFormatLabel1.setText(quiz.getTimeFormat());
        } else {
            timeLimitLabel.setText("No time limit");
            timeFormatLabel.setText("");
            timeLimitLabel1.setText("No");
            timeFormatLabel1.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        second_pane.setVisible(false);
        blur_pane.setVisible(false);

        btn_menu_return.setOnMouseClicked(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        btn_edit_quiz.setOnMouseClicked(event -> {
            ViewFactory.getInstance().updateEditQuizView(quiz);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.EDIT_QUIZ);
        });

        // configure confirm attempt window
        btn_preview_quiz.setOnAction(event -> {
            blur_pane.setVisible(true);
            second_pane.setVisible(true);
        });

        btn_close_confirm.setOnMouseClicked(event -> {
            blur_pane.setVisible(false);
            second_pane.setVisible(false);
        });

        btn_cancel_attempt.setOnAction(event -> {
            blur_pane.setVisible(false);
            second_pane.setVisible(false);
        });

        // start quiz
        btn_start_attempt.setOnAction(event -> {
            blur_pane.setVisible(false);
            second_pane.setVisible(false);
            ViewFactory.getInstance().updateStartQuiz(quiz);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.START_QUIZ);
        });
    }
}
