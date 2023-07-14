package com.hust.quiz.Controllers;

import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class EndQuizController implements Initializable {
    @FXML
    private Button btn_finish_review;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_finish_review.setOnMouseClicked(event -> {
            ViewFactory.getInstance().updateQuizHome();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
            System.out.println("Finish review");
        });
    }

}
