package com.hust.quiz.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChoiceBoxController implements Initializable {
    private static double totalGrade = 0;
    private final String[] gradeList = {"None", "100%", "83.333333%", "80%", "75%", "70%", "66,66667%", "60%", "50%", "40%",
            "33,33333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "11.11111%", "10%", "5%", "0%"};
    @FXML
    private Label choiceNumberLabel;
    @FXML
    private TextField choiceTextField;
    @FXML
    private ComboBox<String> cbGrade;

    public static int getTotalGrade() {
        return (int) totalGrade;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // do not call reset because it will reset totalGrade to 0
        choiceTextField.setText("");
        cbGrade.setDisable(true);
        cbGrade.setValue("None");
        cbGrade.getItems().addAll(gradeList);

        choiceTextField.textProperty().addListener((observableValue, oldValue, newValue)
                -> cbGrade.setDisable(newValue.equals("")));

        cbGrade.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            totalGrade = totalGrade + getGrade(newValue) - getGrade(oldValue);
            if (totalGrade < 0) {
                totalGrade = 0;
            }
        });
    }

    private double getGrade(String grade) {
        if (grade == null || Objects.equals(grade, "None")) {
            return 0;
        } else {
            // remove % from string
            grade = grade.substring(0, grade.length() - 1);
            grade = grade.replace(",", ".");
            return Double.parseDouble(grade);
        }
    }

    public double getGrade() {
        if (Objects.equals(cbGrade.getValue(), "None")) {
            return 0;
        } else {
            // remove % from string
            String grade = cbGrade.getValue().substring(0, cbGrade.getValue().length() - 1);
            grade = grade.replace(",", ".");
            return Double.parseDouble(grade);
        }
    }

    public void setNumberChoice(int num) {
        choiceNumberLabel.setText("Choice " + num);
    }

    public String getChoiceText() {
        return choiceTextField.getText();
    }

    public void setInfo(String choiceText, double grade) {
        choiceTextField.setText(choiceText);
        cbGrade.setValue(grade + "%");
    }

    public void reset() {
        totalGrade = 0;
        choiceTextField.setText("");
        cbGrade.setDisable(true);
        cbGrade.setValue("None");
    }
}