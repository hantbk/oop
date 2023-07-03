package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

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
    private Button btn_create; // Save quiz
    @FXML
    private ImageView btn_menu_return; // Return to Home
    @FXML
    private Button btn_cancel; // Cancel create quiz
    @FXML
    private Label alert_missing_name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // default time expire - fixed - no change
        spinner_time_expire.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Open attempts are submitted automatically")));
        // default counter format is minute - fixed - no change
        spinner_time_format.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Minutes")));

        // configure btn_menu_return - back to home
        btn_menu_return.setOnMouseClicked(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        // configure btn_cancel - back to home
        btn_cancel.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        // configure btn_save - save quiz
        btn_create.setOnAction(event -> {
            String quizName = text_quiz_name.getText();
            String quizDescription = text_quiz_description.getText();
            if (quizDescription.isEmpty()) {
                quizDescription = "No description";
            }
            
            if (quizName.isEmpty()) {
                // alert pop-up if didn't fill name box
                alert_missing_name.setVisible(true);
            } else {
                // turn off alert
                alert_missing_name.setVisible(false);

                QuizService.addQuiz(new Quiz(quizName, quizDescription));

                ViewFactory.getInstance().updateQuizView(quizName, quizDescription);
                // ViewFactory.getInstance().updateEditQuizView(quizName);
                ViewFactory.getInstance().routes(ViewFactory.SCENES.QUIZ_VIEW);
            }
        });
    }
}
