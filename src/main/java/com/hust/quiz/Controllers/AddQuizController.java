package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddQuizController implements Initializable {
    @FXML
    private TextField text_quiz_name; // Quiz Name
    @FXML
    private TextField text_quiz_description; // Quiz Description
    @FXML
    private CheckBox check_display_description; // Check display description or not
    @FXML
    private DatePicker date_open; // Date open quiz
    @FXML
    private DatePicker date_close; // Date close quiz
    @FXML
    private TextField text_time_limit; // Time limit
    @FXML
    private Spinner<String> spinner_time_format;
    @FXML
    private Spinner<String> spinner_time_expire;
    @FXML
    private Button btn_save; // Save quiz
    @FXML
    private ImageView btn_menu_return; // Return to Home
    @FXML
    private Button btn_cancel; // Cancel create quiz
    @FXML
    private Label alert_missing_name;

    private Scene quizView;
    private Stage stage;
    private Parent root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // default time expire - fixed - no change
        spinner_time_expire.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Open attempts are submitted automatically")));
        // default counter format is minute - fixed - no change
        spinner_time_format.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Minutes")));

        // configure btn_menu_return - back to home
        btn_menu_return.setOnMouseClicked(event -> {
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        // configure btn_cancel - back to home
        btn_cancel.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        // configure btn_save - save quiz
        btn_save.setOnAction(event -> {
            String quizName = text_quiz_name.getText();
            String quizDescription = text_quiz_description.getText();
            if (quizName.isEmpty()) {
                // alert pop-up if didn't fill name box
                alert_missing_name.setVisible(true);
            } else {
                // turn off alert
                alert_missing_name.setVisible(false);

                // update info to quizViewScene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuizView.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                QuizViewController quizViewController = loader.getController();
                quizViewController.displayInfo(quizName, quizDescription);

                // TODO: have not push on database yet
                // ViewFactory.getInstance().routes(ViewFactory.SCENES.QUIZ_VIEW);
                stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                quizView = new Scene(root);
                stage.setScene(quizView);
                stage.show();
            }
        });
    }
}
