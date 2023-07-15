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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class StartQuizController implements Initializable {

    private static int sec;
    private static int quizTimeLimit;
    private static String quizTimeFormat;
    @FXML
    private AnchorPane quiz_pane;
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
    private AnchorPane attempt_pane;
    @FXML
    private Button btn_cancel_finish, btn_submit_quiz, btn_finish_attempt;
    private final List<QuestionInStartController> listController = new ArrayList<>();
    private String timeStartQuiz;
    private String timeCompleteQuiz;
    private LocalTime start;
    private LocalTime end;
    private Quiz quiz;


    public static void setQuizTime(int quizTimeLimit, String quizTimeFormat) {
        StartQuizController.quizTimeLimit = quizTimeLimit;
        StartQuizController.quizTimeFormat = quizTimeFormat;
    }

    public void runTimer() {
        if (quizTimeFormat == null) {
            timerLabel.setText("No time limit");
        } else {
            if (Objects.equals(quizTimeFormat, "hours")) {
                sec = quizTimeLimit * 3600;
            } else if (Objects.equals(quizTimeFormat, "minutes")) {
                sec = quizTimeLimit * 60;
            } else if (Objects.equals(quizTimeFormat, "seconds")) {
                sec = quizTimeLimit;
            }
            CountdownTimer countdownTimer = new CountdownTimer(sec, timerLabel);
            countdownTimer.start();
            LocalDate now = LocalDate.now();
            this.start = LocalTime.now();
            DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE");
            DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
            this.timeStartQuiz = now.format(day) + ", " + now.format(date)+ ", " + this.start.format(time);
        }
    }

    public void endQuiz() {
        //quiz_pane.setDisable(true);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().updateQuizHome();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
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
            this.timeCompleteQuiz = now.format(day) + ", " + now.format(date)+ ", " + this.end.format(time);
            Duration timeTaken = Duration.between(start, end);

            ViewFactory.getInstance().reviewQuiz(this.quiz, timeStartQuiz, timeCompleteQuiz, timeTaken, this.listController);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.END_QUIZ);
            this.reset();
        });
    }

    //update cac cau hoi trong quiz vao vBox
    public void updateQuestion(Quiz quiz) {
        this.quiz = quiz;
        int quiz_id = quiz.getQuiz_id();
        List<Question> listQuestion = QuizService.getQuestionQuiz(quiz_id);
        this.label_quiz_name_1.setText(quiz.getQuiz_name());
        this.label_quiz_name_2.setText(quiz.getQuiz_name());
        int i = 1;
        FXMLLoader[] listFXMLQuestionQuiz = new FXMLLoader[listQuestion.size() + 1];
        for (Question question : listQuestion) {
            listFXMLQuestionQuiz[i] = new FXMLLoader(getClass().getResource("/FXML/QuestionInStart.fxml"));
            try {
                Parent root = listFXMLQuestionQuiz[i].load();
                QuestionInStartController controller = listFXMLQuestionQuiz[i].getController();
                listController.add(controller);
                controller.setInforQuestion(question, i);
                vbox_question.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("At " + this.getClass());
            }
            i++;
        }
        List<Button> listButton = new ArrayList<>();
        for (int j = 0; j < listQuestion.size(); j++) {
            final int question_index = j + 1;
            Button button = new Button(String.valueOf(question_index));
            button.setPrefSize(40, 30);
            button.setOnAction(event -> {
                //vị trí của câu hỏi i trong VBox
                double place = vbox_question.getChildren().get(Integer.parseInt(button.getText()) - 1).getLayoutY() - (Integer.parseInt(button.getText())) * 10;
                //nhảy đến vị trí câu hỏi i
                scrollPane_quizView.setVvalue(place / vbox_question.getHeight());
                //System.out.println(place/vbox_question.getHeight());
            });
            grid_num_question.add(button, j % 5, j / 5);
        }
    }

    //reset all
    public void reset() {
        if (!listController.isEmpty())
            listController.clear();
        this.vbox_question.getChildren().clear();
        this.grid_num_question.getChildren().clear();
        quiz_pane.setDisable(false);
    }

    public double getGradeQuiz(){
        double grade = 0;
        System.out.println("So cau hoi: " + listController.size());

        for(QuestionInStartController questionInStartController : listController) {
            grade += questionInStartController.getGrade();
        }
        return grade;
    }
}
