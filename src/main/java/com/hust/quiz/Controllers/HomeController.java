package com.hust.quiz.Controllers;

import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button btn_add, btn_back, btn_question, btn_category, btn_import, btn_export, btn_turn_editing_on;
    @FXML
    private AnchorPane second_pane, first_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        // configure btn_turn_editing_on
        btn_turn_editing_on.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.ADD_QUIZ));

        // configure btn_question, btn_category, btn_import, btn_export
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionBankView.fxml"));
        try {
            Parent root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        QuestionBankController controller = loader.getController();

        btn_question.setOnAction(event -> {
            controller.setTabPane(0);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
        });

        btn_category.setOnAction(event -> {
            controller.setTabPane(1);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
        });

        btn_import.setOnAction(event -> {
            controller.setTabPane(2);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
        });

        btn_export.setOnAction(event -> {
            controller.setTabPane(3);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
        });
    }
}