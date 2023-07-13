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
    private Label label_quiz_name_IT;
    @FXML
    private Label label_quiz_name_view;
    @FXML
    private Label label_quiz_description;
    @FXML
    private Label timeFormatLabel;
    @FXML
    private Label timeLimitLabel;
    @FXML
    private Label timeFormatLabel1;
    @FXML
    private Label timeLimitLabel1;
    @FXML
    private Label timeLimitLabel2;
    @FXML
    private ImageView btn_edit_quiz; // click jump to EditQuizView - NOT done
    @FXML
    private Button btn_preview_quiz;
    @FXML
    private ImageView btn_menu_return; // return to homeView

    @FXML
    private AnchorPane first_pane;
    @FXML
    private AnchorPane second_pane;
    @FXML
    private AnchorPane blur_pane;
    @FXML
    private ImageView btn_close_confirm;
    @FXML
    private Button btn_start_attempt;
    @FXML
    private Button btn_cancel_attempt;
    @FXML
    private Label lb_timelimit_confirm;

    public void displayInfo(String quizName) {
        Quiz quiz = QuizService.getQuiz(quizName);
        label_quiz_name_IT.setText(quiz.getQuiz_name());
        label_quiz_name_view.setText(quiz.getQuiz_name());
        label_quiz_description.setText(quiz.getQuiz_description());
        if(quiz.getTimeLimit()  > 0){
            timeLimitLabel.setText(String.valueOf(quiz.getTimeLimit()));
        }else{
            timeLimitLabel.setText(" Unlimit!");
        }
//        timeFormatLabel.setText(timeFormat);
        if(quiz.getTimeLimit()  > 0){
            timeLimitLabel1.setText(String.valueOf(quiz.getTimeLimit()));
        }else{
            timeLimitLabel1.setText(" Unlimit!");
        }
//        timeFormatLabel1.setText(timeFormat);
        if(quiz.getTimeLimit()  > 0){
            timeLimitLabel2.setText(String.valueOf(quiz.getTimeLimit()));
        }else{
            timeLimitLabel2.setText(" Unlimit!");
        }
//        timeFormatLabel1.setText(timeFormat);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        second_pane.setVisible(false);
        blur_pane.setVisible(false);

        btn_menu_return.setOnMouseClicked(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        btn_edit_quiz.setOnMouseClicked(event -> {
            // TODO: Update quiz
            ViewFactory.getInstance().updateEditQuizView(label_quiz_name_IT.getText());
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
        btn_start_attempt.setOnAction(event -> {
            blur_pane.setVisible(false);
            second_pane.setVisible(false);
            ViewFactory.getInstance().updateQuestionQuiz(label_quiz_name_view.getText());
            ViewFactory.getInstance().routes(ViewFactory.SCENES.START_QUIZ);
        });
    }
}
