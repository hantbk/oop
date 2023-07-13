package com.hust.quiz.Controllers;

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
    private ImageView btn_edit_quiz; // click jump to EditQuizView - NOT done
    @FXML
    private Button btn_preview_quiz;
    @FXML
    private ImageView btn_menu_return; // return to homeView

    @FXML
    private AnchorPane anchor_blur;
    @FXML
    private AnchorPane anchor_confirm;
    @FXML
    private ImageView btn_close_confirm;
    @FXML
    private Button btn_start_attempt;
    @FXML
    private Button btn_cancel_attempt;
    @FXML
    private Label lb_timelimit_confirm;


    public void displayInfo(String quizName, String quizInfo) {
        label_quiz_name_IT.setText(quizName);
        label_quiz_name_view.setText(quizName);
        label_quiz_description.setText(quizInfo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        anchor_blur.setVisible(false);
        anchor_confirm.setVisible(false);

        btn_menu_return.setOnMouseClicked(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        btn_edit_quiz.setOnMouseClicked(event -> {
            // TODO: Update quiz
            ViewFactory.getInstance().updateEditQuizView(label_quiz_name_IT.getText());
            ViewFactory.getInstance().routes(ViewFactory.SCENES.EDIT_QUIZ);
        });

        // configure confirm attempt window
        btn_preview_quiz.setOnAction(event -> {
            anchor_blur.setVisible(true);
            anchor_confirm.setVisible(true);
        });
        btn_close_confirm.setOnMouseClicked(event -> {
            anchor_blur.setVisible(false);
            anchor_confirm.setVisible(false);
        });
        btn_cancel_attempt.setOnAction(event -> {
            anchor_blur.setVisible(false);
            anchor_confirm.setVisible(false);
        });
        btn_start_attempt.setOnAction(event -> {
            anchor_blur.setVisible(false);
            anchor_confirm.setVisible(false);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.START_QUIZ);
        });
    }
}
