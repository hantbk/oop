package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Question;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StartQuizController implements Initializable {

    @FXML
    private ImageView btn_menu_return;

    @FXML
    private Label label_quiz_name_1;

    @FXML
    private Label label_quiz_name_2;

    @FXML
    private VBox vbox_question;
    @FXML
    private GridPane grid_num_question;
    @FXML
    private ScrollPane scrollPane_quizView;
    private List<QuestionInStartController> listController = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().updateQuizHome();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
    }

    //update cac cau hoi trong quiz vao vBox
    public void updateQuestion(String quiz_name) {
        int quiz_id = QuizService.getId(quiz_name);
        List<Question> listQuestion = QuizService.getQuestionQuiz(quiz_id);
        this.label_quiz_name_1.setText(quiz_name);
        this.label_quiz_name_2.setText(quiz_name);
        int i = 1;
        FXMLLoader[] listFXMLQuestionQuiz = new FXMLLoader[listQuestion.size() +1];
        for(Question question : listQuestion){
            listFXMLQuestionQuiz[i] = new FXMLLoader(getClass().getResource("/FXML/QuestionInStart.fxml"));
            try {
                Parent root = listFXMLQuestionQuiz[i].load();
                QuestionInStartController controller = listFXMLQuestionQuiz[i].getController();
                controller.setInforQuestion(question, i);
                vbox_question.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("At "+this.getClass());
            }
            i++;
        }
        List<Button> listButton = new ArrayList<>();
        for(int j = 0; j < listQuestion.size(); j++){
            final int question_index = j+1;
            Button button = new Button(String.valueOf(question_index));
            button.setPrefSize(40, 30);
            button.setOnAction(event -> {
                //vị trí của câu hỏi i trong VBox
                double place = vbox_question.getChildren().get(Integer.valueOf(button.getText()) - 1).getLayoutY() - (Integer.valueOf(button.getText())) * 10;
                //nhảy đến vị trí câu hỏi i
                scrollPane_quizView.setVvalue(place/vbox_question.getHeight());
                //System.out.println(place/vbox_question.getHeight());
            });
            grid_num_question.add(button, j%5, j/5);
        }
    }

    //reset all
    public void reset(){
        if(!listController.isEmpty())
            listController.clear();
        this.vbox_question.getChildren().clear();
        this.grid_num_question.getChildren().clear();
    }
}
