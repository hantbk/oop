package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
    private Spinner<Integer> spinner_time_format;
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
    @FXML
    private CheckBox checkbox_enable_close;

    @FXML
    private CheckBox checkbox_enable_open;

    @FXML
    private CheckBox checkbox_enable_time_limit;

    private Scene quizView;
    private Stage stage;
    private Parent root;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set cac setting ban dau cho cac thanh phan
        setInit();

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
                ViewFactory.getInstance().routes(ViewFactory.SCENES.QUIZ_VIEW);
            }
        });

        //tick enable để có thể set open date
        checkbox_enable_open.setOnAction(event -> {
            if(checkbox_enable_open.isSelected()){
                date_open.setDisable(false);
            }else{
                date_open.setDisable(true);
            }
        });

        //tick enable để có thể set close date
        checkbox_enable_close.setOnAction(event -> {
            if(checkbox_enable_close.isSelected()){
                date_close.setDisable(false);
            }else{
                date_close.setDisable(true);
            }
        });

        //tick enable để có thể set time limit
        checkbox_enable_time_limit.setOnAction(event -> {
            if(checkbox_enable_time_limit.isSelected()){
                text_time_limit.setDisable(false);
                spinner_time_format.setDisable(false);
            }else{
                text_time_limit.setDisable(true);
                spinner_time_format.setDisable(true);
            }
        });

        // configure btn_menu_return - back to home
        btn_menu_return.setOnMouseClicked(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        // configure btn_cancel - back to home
        btn_cancel.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));
    }
public void setInit(){
        //ban đầu các thành phần này sẽ không điền được nếu không chọn enable
        date_open.setDisable(true);
        date_close.setDisable(true);
        text_time_limit.setDisable(true);
        spinner_time_format.setDisable(true);

        //set gia tri ban dau chon spiner chon minute
        spinner_time_format.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 5));
        // default time expire - fixed - no change
        spinner_time_expire.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList("Open attempts are submitted automatically")));

        }
}
