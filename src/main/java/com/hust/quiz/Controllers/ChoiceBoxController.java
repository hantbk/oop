package com.hust.quiz.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChoiceBoxController implements Initializable {
    @FXML
    private Label choiceNumberLabel;
    @FXML
    private TextField choiceTextField;
    public void setNumberChoice(int num){
        choiceNumberLabel.setText("Choice " + num);
    }
    public String getChoiceText(){
        return choiceTextField.getText();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}