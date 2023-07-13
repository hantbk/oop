package com.hust.quiz.Controllers;

import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button btn_add, btn_back, btn_question, btn_category, btn_import, btn_export, btn_turn_editing_on;
    @FXML
    private AnchorPane second_pane, first_pane;
    @FXML
    private Button button_exam_baohiem;
    @FXML
    private VBox vbox_quiz;
    private Parent questionBankView;
    private QuestionBankController questionBankController;

    /**
     * Reset home status
     */
    private void reset() {
        second_pane.setVisible(false);
        first_pane.setVisible(true);
        first_pane.setOpacity(1);
        first_pane.setDisable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // configure btn_add, btn_back
        btn_add.setOnAction(e -> {
            second_pane.setVisible(true);
            first_pane.setOpacity(0.1);
            first_pane.setDisable(true);
        });

        btn_back.setOnAction(e -> reset());

        // configure btn_turn_editing_on
        btn_turn_editing_on.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.ADD_QUIZ));

        // configure btn_question, btn_category, btn_import, btn_export
        FXMLLoader questionBankView = new FXMLLoader(getClass().getResource("/Fxml/QuestionBankView.fxml"));
        try {
            this.questionBankView = questionBankView.load();
        } catch (IOException e) {
            System.out.println("Error loading QuestionBankView.fxml");
        }
        questionBankController = questionBankView.getController();

        btn_question.setOnAction(event -> {
            // reset home status
            second_pane.setVisible(false);
            first_pane.setVisible(true);
            first_pane.setOpacity(1);
            first_pane.setDisable(false);
            // switch to question tab
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
            questionBankController.setTabPane(0);
        });

        btn_category.setOnAction(event -> {
            reset();
            // switch to category tab
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
            questionBankController.setTabPane(1);
        });

        btn_import.setOnAction(event -> {
            reset();
            // switch to import tab
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
            questionBankController.setTabPane(2);
        });

        btn_export.setOnAction(event -> {
            reset();
            // switch to export tab
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
            questionBankController.setTabPane(3);
        });

//        //bam de lam bai thi
//        button_exam_baohiem.setOnAction(event -> {
//            ViewFactory.getInstance().updateQuizView(button_exam_baohiem.getText());
//            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUIZ_VIEW);
////            ViewFactory.getInstance().updateQuestionQuiz(button_exam_baohiem.getText());
////            ViewFactory.getInstance().routes(ViewFactory.SCENES.START_QUIZ);
//        });
    }

    public Parent getQuestionBankView() {
        return questionBankView;
    }

    public QuestionBankController getQuestionBankController() {
        return questionBankController;
    }
}