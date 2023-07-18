package com.hust.quiz.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChoiceBoxController implements Initializable {
    private static double totalGrade = 0;
    private static int countChoice = 0;
    private final String[] gradeList = {"None", "100%", "83.333333%", "80%", "75%", "70%", "66,66667%", "60%", "50%", "40%",
            "33,33333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "11.11111%", "10%", "5%", "0%"};
    @FXML
    private Label choiceNumberLabel;
    @FXML
    private TextArea choiceTextField;
    @FXML
    private ComboBox<String> cbGrade;
    @FXML
    private Button btn_image_choice;
    @FXML
    private ImageView image_choice;
    private String imagePath = null;

    public static int getTotalGrade() {
        return (int) totalGrade;
    }

    public static int getCountChoice() {
        return countChoice;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // do not call reset because it will reset totalGrade to 0
        choiceTextField.setText("");
        cbGrade.setDisable(true);
        cbGrade.setValue("None");
        cbGrade.getItems().addAll(gradeList);

        choiceTextField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            cbGrade.setDisable(newValue.equals(""));
            if (oldValue.equals("") && !newValue.equals("")) {
                countChoice++;
            } else if (!oldValue.equals("") && newValue.equals("")) {
                countChoice--;
            }
        });

        cbGrade.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            totalGrade = totalGrade + getGrade(newValue) - getGrade(oldValue);
            if (totalGrade < 0) {
                totalGrade = 0;
            }
        });

        // configure choice image
        btn_image_choice.setText("Choose image");
        btn_image_choice.setOnAction(event -> {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Choose Image");
            File selectedFile = filechooser.showOpenDialog(null);
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                if (imagePath.endsWith(".jpg") || imagePath.endsWith(".png") || imagePath.endsWith(".gif")) {
                    btn_image_choice.setText("Image is selected");
                    image_choice.setImage(new Image("file:///" + imagePath));
                } else {
                    btn_image_choice.setText("Image must be .jpg or .png or .gif ");
                    imagePath = null;
                }
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
        return getGrade(cbGrade.getValue());
    }

    public void setNumberChoice(int num) {
        choiceNumberLabel.setText("Choice " + num);
    }

    public String getChoiceText() {
        return choiceTextField.getText();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setInfo(String choiceText, double grade, String imagePath) {
        choiceTextField.setText(choiceText);
        cbGrade.setValue(grade + "%");
        this.imagePath = imagePath;
        if (imagePath != null) {
            btn_image_choice.setText("Image is selected");
            image_choice.setImage(new Image("file:///" + imagePath));
        }
    }

    public void reset() {
        totalGrade = 0;
        choiceTextField.setText("");
        cbGrade.setDisable(true);
        cbGrade.setValue("None");
        imagePath = null;
        btn_image_choice.setText("Choose image");
        image_choice.setImage(null);
        countChoice = 0;
    }
}