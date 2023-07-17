package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.CountdownTimer;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartQuizController implements Initializable {
    private final List<QuestionInStartController> listController = new ArrayList<>();
    private CountdownTimer countdownTimer;
    private int sec;
    private String timeStartQuiz, timeCompleteQuiz;
    private LocalTime start, end;
    private Quiz quiz;
    @FXML
    private AnchorPane quiz_pane, attempt_pane;
    @FXML
    private Label timerLabel;
    @FXML
    private ImageView btn_menu_return;
    @FXML
    private Label label_quiz_name_1, label_quiz_name_2;
    @FXML
    private VBox vbox_question;
    @FXML
    private GridPane grid_num_question;
    @FXML
    private ScrollPane scrollPane_quizView;
    @FXML
    private Button btn_cancel_finish, btn_submit_quiz, btn_finish_attempt;

    private void runTimer() {
        if (quiz.getTimeFormat() == null || quiz.getTimeLimit() == 0) {
            timerLabel.setText("No time limit");
        } else {
            if (Objects.equals(quiz.getTimeFormat(), "hours")) {
                sec = quiz.getTimeLimit() * 3600;
            } else if (Objects.equals(quiz.getTimeFormat(), "minutes")) {
                sec = quiz.getTimeLimit() * 60;
            } else if (Objects.equals(quiz.getTimeFormat(), "seconds")) {
                sec = quiz.getTimeLimit();
            }
            countdownTimer.setTimeAndRun(sec);
        }
    }

    //update cac cau hoi trong quiz vao vBox
    public void updateStartQuiz(Quiz quiz) {
        this.quiz = quiz;
        this.label_quiz_name_1.setText(quiz.getQuiz_name());
        this.label_quiz_name_2.setText(quiz.getQuiz_name());

        // update vbox
        int quiz_id = quiz.getQuiz_id();
        List<Question> listQuestion = QuizService.getQuestionQuiz(quiz_id);
        int i = 1;
        for (Question question : listQuestion) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/QuestionInStart.fxml"));
            try {
                Parent root = loader.load();
                QuestionInStartController controller = loader.getController();
                listController.add(controller);
                controller.setInforQuestion(question, i);
                vbox_question.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("At " + this.getClass());
            }
            i++;
        }

        // update gridPane
        for (int j = 0; j < listQuestion.size(); j++) {
            final int question_index = j + 1;
            Button button = new Button(String.valueOf(question_index));
            button.setPrefSize(40, 30);
            button.setOnAction(event -> {
                //vị trí của câu hỏi i trong VBox
                double place = vbox_question.getChildren().get(Integer.parseInt(button.getText()) - 1).getLayoutY() +
                        (Integer.parseInt(button.getText()) - 1) * 10 - 10;
                //nhảy đến vị trí câu hỏi i
                scrollPane_quizView.setVvalue(place / vbox_question.getHeight());
            });
            grid_num_question.add(button, j % 5, j / 5);
        }

        // update timer
        runTimer();

        // update time start quiz
        LocalDate now = LocalDate.now();
        this.start = LocalTime.now();
        DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
        this.timeStartQuiz = now.format(day) + ", " + now.format(date) + ", " + this.start.format(time);
    }

    public void endQuiz() {
        //quiz_pane.setDisable(true);
        System.out.println("Grade: " + this.getGradeQuiz());
        LocalDate now = LocalDate.now();
        this.end = LocalTime.now();
        DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE");
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
        this.timeCompleteQuiz = now.format(day) + ", " + now.format(date) + ", " + this.end.format(time);
        Duration timeTaken = Duration.between(start, end);

        ViewFactory.getInstance().reviewQuiz(this.quiz, timeStartQuiz, timeCompleteQuiz, timeTaken, this.listController);
        ViewFactory.getInstance().routes(ViewFactory.SCENES.END_QUIZ);
        this.reset();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        countdownTimer = new CountdownTimer(timerLabel);

        btn_finish_attempt.setOnMouseClicked(event -> {
            quiz_pane.setDisable(true);
            quiz_pane.opacityProperty().setValue(0.5);
            attempt_pane.setVisible(true);
            attempt_pane.setDisable(false);
        });

        btn_cancel_finish.setOnMouseClicked(event -> {
            quiz_pane.setDisable(false);
            quiz_pane.opacityProperty().setValue(1);
            attempt_pane.setVisible(false);
            attempt_pane.setDisable(true);
        });

        btn_submit_quiz.setOnMouseClicked(event -> {
            System.out.println("Grade: " + this.getGradeQuiz());
            LocalDate now = LocalDate.now();
            this.end = LocalTime.now();
            DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE");
            DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
            this.timeCompleteQuiz = now.format(day) + ", " + now.format(date) + ", " + this.end.format(time);
            Duration timeTaken = Duration.between(start, end);

            ViewFactory.getInstance().reviewQuiz(this.quiz, timeStartQuiz, timeCompleteQuiz, timeTaken, this.listController);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.END_QUIZ);
            this.reset();
        });
    }


    //reset all
    private void reset() {
        listController.clear();
        vbox_question.getChildren().clear();
        grid_num_question.getChildren().clear();
        quiz_pane.setDisable(false);
        attempt_pane.setVisible(false);
        quiz_pane.opacityProperty().setValue(1);
    }

    public double getGradeQuiz() {
        double grade = 0;
        System.out.println("So cau hoi: " + listController.size());

        for (QuestionInStartController questionInStartController : listController) {
            grade += questionInStartController.getGrade();
        }
        return grade;
    }
}
