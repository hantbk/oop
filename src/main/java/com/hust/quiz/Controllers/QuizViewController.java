package com.hust.quiz.Controllers;

import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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

    public void displayInfo(String quizName, String quizInfo) {
        label_quiz_name_IT.setText(quizName);
        label_quiz_name_view.setText(quizName);
        label_quiz_description.setText(quizInfo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: update quiz_name, description
        btn_menu_return.setOnMouseClicked(event -> {
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
    }
}
