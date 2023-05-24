package com.hust.quiz.Controllers;

import com.hust.quiz.Views.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button btn_add, btn_back, btn_question;
    @FXML
    private AnchorPane second_pane, first_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_question.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK));

        // configure btn_add, btn_back
        btn_add.setOnAction(e -> {
            second_pane.setVisible(true);
            first_pane.setOpacity(0.1);
            first_pane.setDisable(true);
        });

        btn_back.setOnAction(e -> {
            second_pane.setVisible(false);
            first_pane.setVisible(true);
            first_pane.setOpacity(1);
            first_pane.setDisable(false);
        });


    }
}