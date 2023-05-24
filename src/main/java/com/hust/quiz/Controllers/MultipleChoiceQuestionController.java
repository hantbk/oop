package com.hust.quiz.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MultipleChoiceQuestionController  implements Initializable {
    @FXML
    private Button btn_blankchoice;
    @FXML
    private GridPane choiceGrid;
    int row = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_blankchoice.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                choiceGrid.addRow(row++, root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
