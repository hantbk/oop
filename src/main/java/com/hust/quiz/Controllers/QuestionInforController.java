package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Question;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionInforController implements Initializable {
    @FXML
    private Button edit_button;

    @FXML
    private Label inforQuestion_label;

    @FXML
    private CheckBox select_checkbox;

    private Question question;
    private String category_name;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        edit_button.setOnAction(event -> {
            ViewFactory.getInstance().updateInforEditQuestion(this.question, this.category_name);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.ADD_QUESTION);
        });
    }
    public void updateInforQuestion(Question question, String category_name){
        inforQuestion_label.setText(question.getQuestionContent());
        this.question = question;
        this.category_name = category_name;
    }
}
