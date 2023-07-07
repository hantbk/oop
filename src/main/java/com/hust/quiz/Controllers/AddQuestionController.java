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
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddQuestionController implements Initializable {
    private final List<ChoiceBoxController> listChoiceBoxController = new ArrayList<>();
    @FXML
    private ImageView btn_menu_return; // return to home
    @FXML
    private Button btn_blankChoice;
    @FXML
    private VBox vBoxAddChoiceBox;
    @FXML
    private Button btn_Cancel;
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
    @FXML
    private Label labelAlert;
    private int countChoice = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // configure btn_menu_return
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        //add list category to comboBox
        updateCategory();
        //add 2 choiceBox
        addTwoChoiceBox();


        //Luu cac thong tin va day len csdl
        btn_SaveAndContinueEditing.setOnAction(event -> {
            labelAlert.setText("");
            if (text_QuestionName.getText().equals("")) {
                labelAlert.setText("Question name is empty!");
            } else if (text_QuestionText.getText().equals("")) {
                labelAlert.setText("Question text is empty!");
            } else if (text_DefaultMark.getText().equals("")) {
                labelAlert.setText("Default mark is empty!");
            } else if (kindOfCategory.getValue() == null) {
                labelAlert.setText("Category is empty!");
            } else if (listChoiceBoxController.get(0).getChoiceText().equals("")) {
                labelAlert.setText("Choice 1 is empty!");
            } else if (listChoiceBoxController.get(1).getChoiceText().equals("")) {
                labelAlert.setText("Choice 2 is empty!");
            } else {
                String categoryName = kindOfCategory.getValue();
                Question newQuestion = new Question(text_QuestionName.getText(), text_QuestionText.getText(),
                        CategoryService.getID(categoryName));
                QuestionService.addQuestion(newQuestion);

                int id = QuestionService.getId(newQuestion.getQuestion_name());

                for (ChoiceBoxController controller : listChoiceBoxController) {
                    if (controller.getChoiceText() != null) {
                        Choice newChoice = new Choice(controller.getChoiceText(), false, controller.getGrade(), id);
                        ChoiceService.addChoice(newChoice);
                        controller.reset();
                    }
                }

                //xoa cac thong tin vua add tranh add 2 lan bi trung lap
               this.reset();
                labelAlert.setText("Add question successfully!");

//                QuestionBankController.updateCategory();
            }
        });

        //bam vao save changes de luu thong len csdl va quay ve Question_Bank
        btn_SaveChanges.setOnAction(event -> {
            labelAlert.setText("");
            if (text_QuestionName.getText().equals("")) {
                labelAlert.setText("Question name is empty!");
            } else if (text_QuestionText.getText().equals("")) {
                labelAlert.setText("Question text is empty!");
            } else if (text_DefaultMark.getText().equals("")) {
                labelAlert.setText("Default mark is empty!");
            } else if (kindOfCategory.getValue() == null) {
                labelAlert.setText("Category is empty!");
            } else if (listChoiceBoxController.get(0).getChoiceText().equals("")) {
                labelAlert.setText("Choice 1 is empty!");
            } else if (listChoiceBoxController.get(1).getChoiceText().equals("")) {
                labelAlert.setText("Choice 2 is empty!");
            } else {
                String categoryName = kindOfCategory.getValue();
                Question newQuestion = new Question(text_QuestionName.getText(), text_QuestionText.getText(),
                        CategoryService.getID(categoryName));
                QuestionService.addQuestion(newQuestion);

                int id = QuestionService.getId(newQuestion.getQuestion_name());

                for (ChoiceBoxController controller : listChoiceBoxController) {
                    if (controller.getChoiceText() != null) {
                        Choice newChoice = new Choice(controller.getChoiceText(), false, controller.getGrade(), id);
                        ChoiceService.addChoice(newChoice);
                        controller.reset();
                    }
                }
                //xoa cac thong tin vua add tranh add 2 lan bi trung lap
                this.reset();
                labelAlert.setText("Add question successfully!");
                ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
            }

        });

        //nhan vao cancel de ve QuestionBank
        btn_Cancel.setOnAction(event -> {
            this.reset();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUESTION_BANK);
        });

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

    //ham update cac thong tin question vao cac o khi bam edit trong question_bank
    public void setInfor(Question oldQuestion, String category_name){
        text_QuestionName.setText(oldQuestion.getQuestion_name());
        text_QuestionText.setText(oldQuestion.getQuestion_text());
        kindOfCategory.setValue(category_name);
    }

    //ham reset các ô điền về trống
    public void reset(){
        text_QuestionName.setText(null);
        text_QuestionText.setText(null);
        text_DefaultMark.setText(null);
        kindOfCategory.setValue(null);
    }

    //ham update category vao combo-box
    public void updateCategory() {
        List<Category> listCategory = CategoryService.getCategories();
        for (Category category : listCategory) {
            kindOfCategory.getItems().add(category.toString());
        }
    }
}

