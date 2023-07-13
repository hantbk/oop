package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Question;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class QuestionInfoFromBankController {
    @FXML
    private CheckBox check_box_question_name;
    @FXML
    private Label question_content;

    private Question question;
    private String category_name;

    public void updateQuestionInfo(Question question, String category_name) {
        check_box_question_name.setText(question.getQuestionContent());
        question_content.setText(question.getQuestionContent());
        this.question = question;
        this.category_name = category_name;
    }
}
