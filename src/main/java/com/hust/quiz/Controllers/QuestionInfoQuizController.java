package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Question;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class QuestionInfoQuizController {

    @FXML
    private CheckBox checkbox_delete;

    @FXML
    private Label lb_numQues;

    @FXML
    private Label lb_quesName;

    private Question questionOfBox;

    public void setInfor(Question question, int numQues){
        this.questionOfBox = question;
        lb_quesName.setText(question.getQuestion_name() + ": " + question.getQuestion_text());
        this.lb_numQues.setText(String.valueOf(numQues));
    }
    public boolean isSelectDelete(){
        return this.checkbox_delete.isSelected();
    }

    public Question getQuestion(){ return this.questionOfBox; }

}
