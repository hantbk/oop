package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Services.ChoiceService;
import com.hust.quiz.Services.ImageService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class QuestionReviewController {
    @FXML
    private Label label_questionContent;

    @FXML
    private Label label_questionNum;

    @FXML
    private Label lbChoiceA;

    @FXML
    private Label lbChoiceB;

    @FXML
    private Label lbChoiceC;

    @FXML
    private Label lbChoiceD;

    @FXML
    private Label lbChoiceE;

    @FXML
    private RadioButton rButton_A;

    @FXML
    private RadioButton rButton_B;

    @FXML
    private RadioButton rButton_C;

    @FXML
    private RadioButton rButton_D;

    @FXML
    private RadioButton rButton_E;
    @FXML
    private Label lbCorrectAnswer;
    @FXML
    private VBox correctPlace;
    @FXML
    private ImageView img_ques;

    public void setQuestion(Question question, int questionNum){
        this.label_questionNum.setText(String.valueOf(questionNum));
        this.label_questionContent.setText(question.getQuestion_text());
        List<Choice> listChoice = new ArrayList<>(ChoiceService.getChoice(question.getQuestion_id()));
        this.setCorrectAnswer(listChoice);
        int numChoice = listChoice.size();
        if (!listChoice.isEmpty()) {
            if( numChoice == 2){
                lbChoiceA.setText("A: " + listChoice.get(0).getContent());
                lbChoiceB.setText("B: "+ listChoice.get(1).getContent());
                lbChoiceC.setVisible(false);    rButton_C.setVisible(false);
                lbChoiceD.setVisible(false);    rButton_D.setVisible(false);
                lbChoiceE.setVisible(false);    rButton_D.setVisible(false);
            }else if(numChoice == 3){
                lbChoiceA.setText("A: " + listChoice.get(0).getContent());
                lbChoiceB.setText("B: "+ listChoice.get(1).getContent());
                lbChoiceC.setText("C: " + listChoice.get(2).getContent());
                rButton_D.setVisible(false);    lbChoiceD.setVisible(false);
                rButton_E.setVisible(false);    lbChoiceE.setVisible(false);
            }else if(numChoice == 4){
                lbChoiceA.setText("A: " + listChoice.get(0).getContent());
                lbChoiceB.setText("B: "+ listChoice.get(1).getContent());
                lbChoiceC.setText("C: " + listChoice.get(2).getContent());
                lbChoiceD.setText("D: " + listChoice.get(3).getContent());
                rButton_E.setVisible(false);    lbChoiceE.setVisible(false);
            }else if(numChoice == 5){
                lbChoiceA.setText("A: " + listChoice.get(0).getContent());
                lbChoiceB.setText("B: "+ listChoice.get(1).getContent());
                lbChoiceC.setText("C: " + listChoice.get(2).getContent());
                lbChoiceD.setText("D: " + listChoice.get(3).getContent());
                lbChoiceE.setText("E: " + listChoice.get(4).getContent());
            }
        }

        int question_id = question.getQuestion_id();
        if (ImageService.getImage(question_id) != null) { // Kiểm tra xem câu hỏi có ảnh không
            if (ImageService.getImage(question_id).toLowerCase().endsWith(".png")) {
                Image image = new Image(ImageService.getImage(question_id));
                img_ques.setImage(image);
            }
        }
        else {
            img_ques.setVisible(false); // Ẩn đối tượng ImageView nếu không có ảnh
        }
    }

    //tick vào đáp án đã chọn
    public void setSelected(int selected){
        if(selected == 0){
            rButton_A.setSelected(true);
        }else if(selected == 1){
            rButton_B.setSelected(true);
        }else if(selected == 2){
            rButton_C.setSelected(true);
        }else if(selected == 3){
            rButton_D.setSelected(true);
        }else if(selected == 4){
            rButton_E.setSelected(true);
        }
    }

    //hiển thị đáp án đúng
    public void setCorrectAnswer(List<Choice> listChoice){
        for(Choice choice : listChoice){
            if(choice.getChoiceGrade() != 0)
                lbCorrectAnswer.setText("The correct answer is: " + choice.getContent());
        }
    }

    //nếu trả lời đúng thì màu xanh, sai thì màu dỏ
    public void checkCorrect(boolean isCorrect){
        if(isCorrect){
            this.correctPlace.setStyle("-fx-background-color: #7ef19b");
        }else{
            this.correctPlace.setStyle("-fx-background-color: #e86b6b");
        }
    }
}
