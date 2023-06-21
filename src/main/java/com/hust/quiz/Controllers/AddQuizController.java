package com.hust.quiz.Controllers;

import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddQuizController implements Initializable {
    @FXML
    private ImageView btn_menu_return;
    @FXML
    private Button btn_cancel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // configure btn_menu_return - back to home
        btn_menu_return.setOnMouseClicked(event -> {
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
        // configure btn_cancel - back to home
        btn_cancel.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));
    }
}
