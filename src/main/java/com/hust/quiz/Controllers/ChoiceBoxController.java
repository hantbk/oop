package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Choice;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChoiceBoxController implements Initializable {
    @FXML
    private Label choiceNumberLabel;
    @FXML
    private TextField choiceTextField;
    @FXML
    private ComboBox<String> cbGrade;

    public void setNumberChoice(int num) {
        choiceNumberLabel.setText("Choice " + num);
    }

    public String getChoiceText() {
        return choiceTextField.getText();
    }

    public void reset() {
        choiceTextField.setText("");
        cbGrade.setValue("None");
    }

    public int getGrade() {
        if (Objects.equals(cbGrade.getValue(), "None"))
            return 0;
        else {
            String grade = this.cbGrade.getValue();
            int intGrade =  Integer.valueOf( grade.substring(0, grade.length() - 1) );
            return intGrade;
        }
    }

    public void setChoice(Choice choice){
        this.choiceTextField.setText(choice.getContent());
        this.cbGrade.setValue(choice.getChoiceGrade() +"%");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addGrade();
    }

    public void addGrade() {
        String[] grade = {"None", "100%", "83.333333%", "80%", "75%", "70%", "66,66667%", "60%", "50%", "40%",
                "33,33333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "11.11111%", "10%", "5%", "0%"};
        cbGrade.getItems().addAll(grade);
    }
}