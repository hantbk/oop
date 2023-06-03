package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MultipleChoiceQuestionController  implements Initializable {
    @FXML
    private Button btn_blankchoice;
    @FXML
    private VBox vBoxAddChoiceBox;
    @FXML
    private Button btn_Cancel;

    @FXML
    private Button btn_SaveAndContinueEditting;
    @FXML
    private ComboBox kindOfCategory;

    @FXML
    private Button btn_SaveChanges;
    @FXML
    private TextField text_QuestionName;

    @FXML
    private TextArea text_QuestionText;
    @FXML
    private TextField text_DefaultMark;
    private int countChoice = 0;
    private List<ChoiceBoxController> listChoiceBoxController = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //add choiceBox1
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
            Parent root1 = loader1.load();
            ChoiceBoxController choiceBoxController1 = loader1.getController();
            choiceBoxController1.setNumberChoice(countChoice+1);
            listChoiceBoxController.add(choiceBoxController1);
            vBoxAddChoiceBox.getChildren().add(countChoice++, root1);
            //add choiceBox2
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
            Parent root2 = loader2.load();
            ChoiceBoxController choiceBoxController2 = loader2.getController();
            choiceBoxController2.setNumberChoice(countChoice+1);
            listChoiceBoxController.add(choiceBoxController2);
            vBoxAddChoiceBox.getChildren().add(countChoice++, root2);

        }catch(IOException exception){
            exception.printStackTrace();
        }
        btn_blankchoice.setOnAction(event -> {
            if(countChoice < 5) {
                try {
                    //add choiceBox3
                    FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                    Parent root3 = loader3.load();
                    ChoiceBoxController choiceBoxController3 = loader3.getController();
                    choiceBoxController3.setNumberChoice(countChoice+1);
                    listChoiceBoxController.add(choiceBoxController3);
                    vBoxAddChoiceBox.getChildren().add(countChoice++, root3);
                    //add choiceBox4
                    FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                    Parent root4 = loader4.load();
                    ChoiceBoxController choiceBoxController4 = loader4.getController();
                    choiceBoxController4.setNumberChoice(countChoice+1);
                    listChoiceBoxController.add(choiceBoxController4);
                    vBoxAddChoiceBox.getChildren().add(countChoice++, root4);
                    //add choiceBox5
                    FXMLLoader loader5 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                    Parent root5 = loader5.load();
                    ChoiceBoxController choiceBoxController5 = loader5.getController();
                    choiceBoxController5.setNumberChoice(countChoice+1);
                    listChoiceBoxController.add(choiceBoxController5);
                    vBoxAddChoiceBox.getChildren().add(countChoice++, root5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        btn_SaveAndContinueEditting.setOnAction(event -> {
            List<Choice> listChoice = new ArrayList<>();
            int id = 65;
            for(ChoiceBoxController controller : listChoiceBoxController){
                Choice choice = new Choice(Character.toString((char)(id++)), controller.getChoiceText());
                listChoice.add(choice);
            }
            Question question = new Question(text_QuestionName.getText(), text_QuestionText.getText(), listChoice);
            question.getDetailQuestion();
        });
    }
}

