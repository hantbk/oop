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

public class EditQuestionController implements Initializable {
    private final List<ChoiceBoxController> listChoiceBoxController = new ArrayList<>();
    private int countChoice = 0;
    private Question question;
    private List<Choice> listChoice;
    @FXML
    private ImageView btn_menu_return; // return to home
    @FXML
    private Button btn_blankChoice, btn_Cancel, btn_SaveAndContinueEditing, btn_SaveChanges;
    @FXML
    private VBox vBoxAddChoiceBox;
    @FXML
    private ComboBox<String> kindOfCategory;
    @FXML
    private TextField text_QuestionName, text_DefaultMark;
    @FXML
    private TextArea text_QuestionText;
    @FXML
    private Label labelAlert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //add list category to comboBox
        updateCategory();

        // configure btn_menu_return
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().updateQuizHome();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        //Luu cac thong tin va day len csdl
        btn_SaveAndContinueEditing.setOnAction(event -> {
            labelAlert.setText("");
            if (text_QuestionName.getText().equals("")) {
                labelAlert.setText("Question name is empty!");
            } else if (text_QuestionText.getText().equals("")) {
                labelAlert.setText("Question text is empty!");
            } else if (isNotNumber(text_DefaultMark.getText())) {
                labelAlert.setText("Default mark must be a number!");
            } else if (kindOfCategory.getValue() == null) {
                labelAlert.setText("Category is empty!");
            } else if (listChoiceBoxController.get(0).getChoiceText().equals("")) {
                labelAlert.setText("Choice 1 is empty!");
            } else if (listChoiceBoxController.get(1).getChoiceText().equals("")) {
                labelAlert.setText("Choice 2 is empty!");
            } else if (ChoiceBoxController.getTotalGrade() != 100) {
                labelAlert.setText("Total of grade must be 100!");
            } else {
                updateQuestion();
            }
        });

        //bam vao save changes de luu thong len csdl va quay ve Question_Bank
        btn_SaveChanges.setOnAction(event -> {
            labelAlert.setText("");
            if (text_QuestionName.getText().equals("")) {
                labelAlert.setText("Question name is empty!");
            } else if (text_QuestionText.getText().equals("")) {
                labelAlert.setText("Question text is empty!");
            } else if (isNotNumber(text_DefaultMark.getText())) {
                labelAlert.setText("Default mark must be a number!");
            } else if (kindOfCategory.getValue() == null) {
                labelAlert.setText("Category is empty!");
            } else if (listChoiceBoxController.get(0).getChoiceText().equals("")) {
                labelAlert.setText("Choice 1 is empty!");
            } else if (listChoiceBoxController.get(1).getChoiceText().equals("")) {
                labelAlert.setText("Choice 2 is empty!");
            } else if ((ChoiceBoxController.getTotalGrade()) != 100) {
                labelAlert.setText("Total of grade must be 100!");
            } else {
                updateQuestion();
                this.reset();
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
                addChoiceBox(3);
            } else {
                labelAlert.setText("You can only add 5 choices!");
            }
        });
    }

    private void updateQuestion() {
        // update question
        String categoryName = kindOfCategory.getValue();

        question.setInfo(text_QuestionName.getText(), text_QuestionText.getText(),
                Integer.parseInt(text_DefaultMark.getText()), CategoryService.getID(categoryName));

        QuestionService.updateQuestion(question);

        // update choice
        for (int i = 0; i < listChoice.size(); i++) {
            String text = listChoiceBoxController.get(i).getChoiceText();
            if (!text.equals("")) {
                listChoice.get(i).setInfo(text, listChoiceBoxController.get(i).getGrade());
            }
        }
        ChoiceService.updateChoice(listChoice);

        // add new choice if user add more choice
        for (int i = listChoice.size(); i < countChoice; i++) {
            String text = listChoiceBoxController.get(i).getChoiceText();
            if (!text.equals("")) {
                Choice newChoice = new Choice(listChoiceBoxController.get(i).getChoiceText(),
                        listChoiceBoxController.get(i).getGrade(), question.getId());
                ChoiceService.addChoice(newChoice);
            }
        }

        labelAlert.setText("Update question successfully!");
    }

    private void addChoiceBox(int numberChoice) {
        try {
            for (int i = 0; i < numberChoice && countChoice < 5; i++) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                Parent root = loader.load();
                ChoiceBoxController choiceBoxController = loader.getController();
                choiceBoxController.setNumberChoice(countChoice + 1);
                listChoiceBoxController.add(choiceBoxController);
                vBoxAddChoiceBox.getChildren().add(countChoice++, root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addChoiceBox(List<Choice> listChoice) {
        try {
            for (Choice choice : listChoice) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/choiceQuestionBox.fxml"));
                Parent root = loader.load();
                ChoiceBoxController choiceBoxController = loader.getController();
                choiceBoxController.setNumberChoice(countChoice + 1);
                choiceBoxController.setInfo(choice.getContent(), choice.getChoiceGrade());
                listChoiceBoxController.add(choiceBoxController);
                vBoxAddChoiceBox.getChildren().add(countChoice++, root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ham reset các ô điền về trống
    private void reset() {
        if(!listChoiceBoxController.isEmpty())
            listChoiceBoxController.get(0).reset();  // reset total grade = 0
        listChoiceBoxController.clear();
        vBoxAddChoiceBox.getChildren().clear();
        countChoice = 0;

        labelAlert.setText("");
        text_QuestionName.setText(null);
        text_QuestionText.setText(null);
        text_DefaultMark.setText(null);
        kindOfCategory.setValue(null);
        updateCategory();
    }

    //ham update category vao combo-box
    private void updateCategory() {
        List<Category> listCategory = CategoryService.getCategories();
        for (Category category : listCategory) {
            kindOfCategory.getItems().add(category.toString());
        }
    }

    private boolean isNotNumber(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    //ham update cac thong tin question vao cac o khi bam edit trong question_bank
    public void setInfo(Question oldQuestion, String category_name) {
        this.question = oldQuestion;
        text_QuestionName.setText(oldQuestion.getQuestion_name());
        text_QuestionText.setText(oldQuestion.getQuestion_text());
        kindOfCategory.setValue(category_name);
        text_DefaultMark.setText(String.valueOf(oldQuestion.getMark()));

        //set cac choice vao cac o
        listChoice = ChoiceService.getChoice(oldQuestion.getQuestion_id());
        addChoiceBox(listChoice);
    }
}

