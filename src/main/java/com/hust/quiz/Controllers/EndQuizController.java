package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EndQuizController implements Initializable {
    private final List<QuestionReviewController> listControllerReview = new ArrayList<>();
    @FXML
    private Button btn_finish_review;
    @FXML
    private GridPane grid_num_question;
    @FXML
    private Label label_quiz_name_1, label_quiz_name_2, label_quiz_name_IT1;
    @FXML
    private Label lbGrade, lbMark, lbTimeComplete, lbTimeStart, lbTimeTaken;
    @FXML
    private VBox vbox_question;
    @FXML
    private ScrollPane scrollPane_quizView;
    private double mark = 0;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_finish_review.setOnMouseClicked(event -> {
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
            this.reset();
            System.out.println("Finish review");
        });
    }

    public void setInforQuiz(Quiz quiz, String timeStart, String timeComplete, Duration timeTaken, List<QuestionInStartController> listControllerAnswer) {
        this.lbTimeStart.setText(timeStart);
        this.lbTimeComplete.setText(timeComplete);
        this.lbTimeTaken.setText(this.getTimeTaken(timeTaken));

        this.updateQuestion(quiz);
        int numQues = listControllerAnswer.size();
        for (int i = 0; i < numQues; i++) {
            int selected = listControllerAnswer.get(i).getSelected();
            double grade = listControllerAnswer.get(i).getGrade();
            this.mark += grade;
            this.listControllerReview.get(i).setSelected(selected);
            this.listControllerReview.get(i).checkCorrect(grade != 0);
        }
        DecimalFormat df = new DecimalFormat("##.##");
        this.lbMark.setText(this.mark + " / " + (double) numQues);
        String tmp1 = df.format((this.mark / (double) numQues) * 10);
        String tmp2 = df.format((this.mark / (double) numQues));
        tmp1 = tmp1.replace(",", ".");
        tmp2 = tmp2.replace(",", ".");
        System.out.println(tmp1 + " " + tmp2);
        this.lbGrade.setText(tmp1 + " out of 10.00 (" + Double.parseDouble(tmp2) * 100 + "%)");
    }

    private void updateQuestion(Quiz quiz) {
        int quiz_id = quiz.getQuiz_id();
        List<Question> listQuestion = QuizService.getQuestionQuiz(quiz_id);
        this.label_quiz_name_1.setText(quiz.getQuiz_name());
        this.label_quiz_name_2.setText(quiz.getQuiz_name());

        int i = 1;
        for (Question question : listQuestion) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/QuestionReview.fxml"));
            try {
                Parent root = loader.load();
                QuestionReviewController controller = loader.getController();
                controller.setQuestion(question, i);
                this.listControllerReview.add(controller);
                vbox_question.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("At " + this.getClass());
            }
            i++;
        }

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

    public String getTimeTaken(Duration duration) {
        String timeTaken = "";
        long hour = duration.toHours();
        if (hour == 1) {
            timeTaken += "1 hour";
        } else if (hour > 1) {
            timeTaken += hour + " hours ";
        }

        long minute = duration.toMinutesPart();
        if (minute <= 1) {
            timeTaken += minute + " min ";
        } else {
            timeTaken += minute + " mins ";
        }

        long sec = duration.toSecondsPart();
        if (sec == 1) {
            timeTaken += sec + " sec";
        } else if (sec > 1) {
            timeTaken += sec + " secs";
        }
        return timeTaken;
    }

    private void reset() {
        this.vbox_question.getChildren().clear();
        this.grid_num_question.getChildren().clear();
        this.listControllerReview.clear();
        this.mark = 0;
    }

}
