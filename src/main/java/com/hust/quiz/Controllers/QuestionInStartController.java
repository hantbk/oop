package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Services.ChoiceService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//hiển thị câu hỏi trong bài test
public class QuestionInStartController implements Initializable {
    private final List<Choice> listChoiceInBox = new ArrayList<>();
    @FXML
    private Label label_questionContent;
    @FXML
    private ImageView image_question;
    @FXML
    private Label label_questionNum;
    @FXML
    private RadioButton rButton_A, rButton_B, rButton_C, rButton_D, rButton_E;
    @FXML
    private VBox vBox_question;
    private Question questionInBox; //cau hoi chua trong box

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setInforQuestion(Question question, int questionNum) {
        this.questionInBox = question;
        this.label_questionNum.setText(String.valueOf(questionNum));
        this.label_questionContent.setText(question.getQuestion_text());
        if (question.getQuestionImage() != null) {
            image_question.setImage(new Image("file:///" + question.getQuestionImage()));
            image_question.setFitHeight(150);

        } else {
            vBox_question.getChildren().remove(image_question);
        }

        List<Choice> listChoice = new ArrayList<>(ChoiceService.getChoice(question.getQuestion_id()));
        listChoiceInBox.addAll(listChoice);
        int numChoice = listChoice.size();
        if (!listChoice.isEmpty()) {
            if (numChoice == 2) {
                rButton_A.setText("A: " + listChoice.get(0).getContent());
                rButton_B.setText("B: " + listChoice.get(1).getContent());
                rButton_C.setVisible(false);
                rButton_D.setVisible(false);
                rButton_E.setVisible(false);
            } else if (numChoice == 3) {
                rButton_A.setText("A: " + listChoice.get(0).getContent());
                rButton_B.setText("B: " + listChoice.get(1).getContent());
                rButton_C.setText("C: " + listChoice.get(2).getContent());
                rButton_D.setVisible(false);
                rButton_E.setVisible(false);
            } else if (numChoice == 4) {
                rButton_A.setText("A: " + listChoice.get(0).getContent());
                rButton_B.setText("B: " + listChoice.get(1).getContent());
                rButton_C.setText("C: " + listChoice.get(2).getContent());
                rButton_D.setText("D: " + listChoice.get(3).getContent());
                rButton_E.setVisible(false);
            } else if (numChoice == 5) {
                rButton_A.setText("A: " + listChoice.get(0).getContent());
                rButton_B.setText("B: " + listChoice.get(1).getContent());
                rButton_C.setText("C: " + listChoice.get(2).getContent());
                rButton_D.setText("D: " + listChoice.get(3).getContent());
                rButton_E.setText("E: " + listChoice.get(4).getContent());
            }
        }
    }

    //lay grade cua question
    public double getGrade() {
        if (rButton_A.isSelected()) {
            return listChoiceInBox.get(0).getChoiceGrade() / 100;
        } else if (rButton_B.isSelected()) {
            return listChoiceInBox.get(1).getChoiceGrade() / 100;
        } else if (rButton_C.isSelected()) {
            return listChoiceInBox.get(2).getChoiceGrade() / 100;
        } else if (rButton_D.isSelected()) {
            return listChoiceInBox.get(3).getChoiceGrade() / 100;
        } else if (rButton_E.isSelected()) {
            return listChoiceInBox.get(4).getChoiceGrade() / 100;
        } else {
            return 0;
        }
    }

    public int getSelected() {
        if (rButton_A.isSelected()) {
            return 0;
        } else if (rButton_B.isSelected()) {
            return 1;
        } else if (rButton_C.isSelected()) {
            return 2;
        } else if (rButton_D.isSelected()) {
            return 3;
        } else if (rButton_E.isSelected()) {
            return 4;
        } else {
            return -1;
        }
    }
}