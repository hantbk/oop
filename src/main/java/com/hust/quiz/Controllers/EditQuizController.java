package com.hust.quiz.Controllers;

import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;



public class EditQuizController implements Initializable {
    @FXML
    private ImageView btn_menu_return;
    @FXML
    private Label label_quiz_name_IT;
    @FXML
    private Label label_quiz_name_edit;
    @FXML
    private Label number_of_questions;
    @FXML
    private AnchorPane add_question_option;
    @FXML
    private ImageView arrow_add;

    public void editQuizDisplayInfo(String quizName) {
        // TODO: Update num
        label_quiz_name_IT.setText(quizName);
        label_quiz_name_edit.setText(quizName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_menu_return.setOnMouseClicked(event -> {
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
        add_question_option.setVisible(false);

        arrow_add.setOnMouseClicked(event -> {
            add_question_option.setVisible(true);
        });
    }
}
