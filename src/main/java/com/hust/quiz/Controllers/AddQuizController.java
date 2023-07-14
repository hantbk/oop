package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Services.Utils;
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
    private TextField text_quiz_name, text_quiz_description; // Quiz Name, Quiz Description
    @FXML
    private CheckBox check_display_description; // Check display description or not
    @FXML
    private DatePicker date_open, date_close; // Date open quiz, close quiz
    @FXML
    private TextField text_time_limit; // Time limit
    @FXML
    private Spinner<String> spinner_time_format, spinner_time_expire;
    @FXML
    private Button btn_create, btn_cancel; // Save quiz, Cancel create quiz
    @FXML
    private ImageView btn_menu_return; // Return to Home
    @FXML
    private Label alert_missing_name;
    @FXML
    private CheckBox checkbox_enable_close, checkbox_enable_open, checkbox_enable_time_limit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set cac setting ban dau cho cac thanh phan
        setInit();

        // configure btn_menu_return - back to home
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().updateQuizHome();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        // configure btn_save - save quiz
        btn_create.setOnAction(event -> {
            String quizName = text_quiz_name.getText();

            if (quizName.isEmpty()) {
                // alert pop-up if didn't fill name box
                alert_missing_name.setVisible(true);
            } else {
                // turn off alert
                alert_missing_name.setVisible(false);

                String quizDescription = "";
                int timeLimit = 0;
                String time_format = null;

                if (check_display_description.isSelected()) {
                    quizDescription = text_quiz_description.getText().trim();
                }

                if (checkbox_enable_time_limit.isSelected()) {
                    timeLimit = Utils.StringToInt(text_time_limit.getText());
                    time_format = spinner_time_format.getValue();
                }
                QuizService.addQuiz(new Quiz(quizName, quizDescription, timeLimit, time_format, date_open.getValue(), date_close.getValue()));

                ViewFactory.getInstance().updateQuizView(quizName);
                this.reset();
                ViewFactory.getInstance().routes(ViewFactory.SCENES.QUIZ_VIEW);
            }
        });

        //tick enable để có thể set open date
        checkbox_enable_open.setOnAction(event -> date_open.setDisable(!checkbox_enable_open.isSelected()));

        //tick enable để có thể set close date
        checkbox_enable_close.setOnAction(event -> date_close.setDisable(!checkbox_enable_close.isSelected()));

        //tick enable để có thể set time limit
        checkbox_enable_time_limit.setOnAction(event -> {
            if (checkbox_enable_time_limit.isSelected()) {
                text_time_limit.setDisable(false);
                spinner_time_format.setDisable(false);
            } else {
                text_time_limit.clear();
                text_time_limit.setDisable(true);
                spinner_time_format.setDisable(true);
            }
        });

        // configure btn_cancel - back to home
        btn_cancel.setOnAction(event -> {
            this.reset();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
    }

    private void setInit() {
        //set gia tri ban dau chon spiner chon minute
        spinner_time_format.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("minutes", "hours", "days", "seconds", "weeks")));
        // default time expire - fixed - no change
        spinner_time_expire.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Open attempts are submitted automatically")));
    }

    private void reset() {
        text_quiz_name.setText(null);
        text_quiz_description.setText(null);
        date_open.setValue(null);
        date_close.setValue(null);
        text_time_limit.setText(null);
        spinner_time_format.getValueFactory().setValue("minutes");
        //ban đầu các thành phần này sẽ không điền được nếu không chọn enable
        date_open.setDisable(true);
        date_close.setDisable(true);
        text_time_limit.setDisable(true);
        spinner_time_format.setDisable(true);

        checkbox_enable_open.setSelected(false);
        checkbox_enable_close.setSelected(false);
        checkbox_enable_time_limit.setSelected(false);
    }
}
