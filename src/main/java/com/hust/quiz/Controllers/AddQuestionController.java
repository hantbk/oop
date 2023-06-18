package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Category;
import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Services.ChoiceService;
import com.hust.quiz.Services.QuestionService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddQuestionController implements Initializable {
    private final List<ChoiceBoxController> listChoiceBoxController = new ArrayList<>();
    @FXML
    private Button btn_blankChoice;
    @FXML
    private VBox vBoxAddChoiceBox;
    @FXML
    private Button btn_Cancel;
    @FXML
    private Label errorMessage;
    @FXML
    private Button btn_SaveAndContinueEditing;
    @FXML
    private ComboBox<String> kindOfCategory;
    @FXML
    private Button btn_SaveChanges;
    @FXML
    private TextField text_QuestionName;
    @FXML
    private TextArea text_QuestionText;
    @FXML
    private TextField text_DefaultMark;
    private int countChoice = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //add 2 choiceBox
        addTwoChoiceBox();
        //add list category to comboBox
        updateCategory();

        //Luu cac thong tin va day len csdl
        btn_SaveAndContinueEditing.setOnAction(event -> {
            if ((text_QuestionText != null) && (text_QuestionName != null) && (kindOfCategory.getValue() != null)) {
                String categoryName = kindOfCategory.getValue();
                Question newQuestion = new Question(text_QuestionName.getText(), text_QuestionText.getText(),
                        CategoryService.getID(categoryName));
                QuestionService.addQuestion(newQuestion);

                int id = QuestionService.getId(newQuestion.getQuestion_name());

                for (ChoiceBoxController controller : listChoiceBoxController) {
                    if (controller.getChoiceText() != null) {
                        Choice newChoice = new Choice(controller.getChoiceText(), false, controller.getGrade(), id);
                        ChoiceService.addChoice(newChoice);
                    }
                }

                //test addChoice
                testAddChoice(text_QuestionName.getText());

                //xoa cac thong tin vua add tranh add 2 lan bi trung lap
                text_QuestionName.setText(null);
                text_QuestionText.setText(null);
                text_DefaultMark.setText(null);
            } else {
                errorMessage.setText("Add failed! You need to fill all information about question!");
            }
        });

        //bam vao save changes de luu thong len csdl va quay ve Question_Bank
        btn_SaveChanges.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK));

        //nhan vao cancel de ve QuestionBank
        btn_Cancel.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK));

        //them 3 choice sau khi nhan vao btn
        btn_blankChoice.setOnAction(event -> {
            if (countChoice < 5) {
                try {
                    //add choiceBox3
                    FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                    Parent root3 = loader3.load();
                    ChoiceBoxController choiceBoxController3 = loader3.getController();
                    choiceBoxController3.setNumberChoice(countChoice + 1);   //set numberChoice de thay doi choice so i, ChoiceBoxController.
                    listChoiceBoxController.add(choiceBoxController3);      //lay controller de co the lay cac thuoc tinh trong box ve sau
                    vBoxAddChoiceBox.getChildren().add(countChoice++, root3);   //add vao Vbox
                    //add choiceBox4
                    FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                    Parent root4 = loader4.load();
                    ChoiceBoxController choiceBoxController4 = loader4.getController();
                    choiceBoxController4.setNumberChoice(countChoice + 1);
                    listChoiceBoxController.add(choiceBoxController4);
                    vBoxAddChoiceBox.getChildren().add(countChoice++, root4);
                    //add choiceBox5
                    FXMLLoader loader5 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                    Parent root5 = loader5.load();
                    ChoiceBoxController choiceBoxController5 = loader5.getController();
                    choiceBoxController5.setNumberChoice(countChoice + 1);
                    listChoiceBoxController.add(choiceBoxController5);
                    vBoxAddChoiceBox.getChildren().add(countChoice++, root5);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    //ham add 2 choiceBox
    public void addTwoChoiceBox() {
        try {
            //add choiceBox1
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
            Parent root1 = loader1.load();
            ChoiceBoxController choiceBoxController1 = loader1.getController();
            choiceBoxController1.setNumberChoice(countChoice + 1);
            listChoiceBoxController.add(choiceBoxController1);
            vBoxAddChoiceBox.getChildren().add(countChoice++, root1);
            //add choiceBox2
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
            Parent root2 = loader2.load();
            ChoiceBoxController choiceBoxController2 = loader2.getController();
            choiceBoxController2.setNumberChoice(countChoice + 1);
            listChoiceBoxController.add(choiceBoxController2);
            vBoxAddChoiceBox.getChildren().add(countChoice++, root2);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //ham update category vao combo-box
    public void updateCategory() {
        List<Category> listCategory = CategoryService.getCategories();
        for (Category category : listCategory) {
            kindOfCategory.getItems().add(category.toString());
        }
    }

    //test add choice
    public void testAddChoice(String nameOfQuestion) {
        int idQuestion = QuestionService.getId(nameOfQuestion);
        List<Choice> listChoice = ChoiceService.getChoice(idQuestion);
        for (Choice choice : listChoice) {
            System.out.println(choice.getContent() + "\n");
        }
    }
}
