package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Category;
import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Services.*;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditQuestionController implements Initializable {
    private final List<ChoiceBoxController> listChoiceBoxController = new ArrayList<>();
    private int countChoice = 0;
    private String imagePath = null;
    private Question question;
    private List<Choice> listChoice;
    @FXML
    private ImageView btn_menu_return, image_question; // return to home
    @FXML
    private Button btn_blankChoice, btn_Cancel, btn_SaveAndContinueEditing, btn_SaveChanges, btn_image_question;
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
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        //Luu cac thong tin va day len csdl
        btn_SaveAndContinueEditing.setOnAction(event -> {
            labelAlert.setText("");
            if (text_QuestionName.getText().equals("")) {
                labelAlert.setText("Question name is empty!");
            } else if (text_QuestionText.getText().equals("")) {
                labelAlert.setText("Question text is empty!");
            } else if (Utils.isNotNumber(text_DefaultMark.getText())) {
                labelAlert.setText("Default mark must be a number!");
            } else if (kindOfCategory.getValue() == null) {
                labelAlert.setText("Category is empty!");
            } else if (ChoiceBoxController.getCountChoice() < 2) {
                labelAlert.setText("You must have at least 2 choices!");
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
            } else if (Utils.isNotNumber(text_DefaultMark.getText())) {
                labelAlert.setText("Default mark must be a number!");
            } else if (kindOfCategory.getValue() == null) {
                labelAlert.setText("Category is empty!");
            } else if (ChoiceBoxController.getCountChoice() < 2) {
                labelAlert.setText("You must have at least 2 choices!");
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
                addChoiceBox(5 - countChoice);
            } else {
                labelAlert.setText("You can only add 5 choices!");
            }
        });

        btn_image_question.setOnAction(event -> {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Choose Image");
            File selectedFile = filechooser.showOpenDialog(null);
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
                if (imagePath.endsWith(".jpg") || imagePath.endsWith(".png")) {
                    btn_image_question.setText("Image is selected");
                    image_question.setImage(new Image(imagePath));
                } else {
                    btn_image_question.setText("Image must be .jpg or .png");
                    imagePath = null;
                }
            }
        });
    }

    private void updateQuestion() {
        // update question
        String categoryName = kindOfCategory.getValue();

        if (imagePath != null) {
            imagePath = ImageService.saveImage(question.getId(), imagePath, true);
        }

        question.setInfo(text_QuestionName.getText(), text_QuestionText.getText().trim(), imagePath,
                Integer.parseInt(text_DefaultMark.getText()), CategoryService.getID(categoryName));

        QuestionService.updateQuestion(question);

        // update choice
        for (int i = 0; i < listChoice.size(); i++) {
            ChoiceBoxController controller = listChoiceBoxController.get(i);
            String text = controller.getChoiceText();
            if (!text.equals("")) {
                String imageChoice = controller.getImagePath();
                if (imageChoice != null) {
                    imageChoice = ImageService.saveImage(listChoice.get(i).getId(), imageChoice, false);
                }
                listChoice.get(i).setInfo(text, controller.getGrade(), imageChoice);
            }
        }
        ChoiceService.updateChoice(listChoice);

        // add new choice if user add more choice
        if (countChoice > listChoice.size()) {
            int newChoiceId = ChoiceService.getLastChoiceId() + 1;
            for (int i = listChoice.size(); i < countChoice; i++) {
                ChoiceBoxController controller = listChoiceBoxController.get(i);
                String text = controller.getChoiceText();
                if (!text.equals("")) {
                    String imageChoice = controller.getImagePath();
                    if (imageChoice != null) {
                        imageChoice = ImageService.saveImage(newChoiceId, imageChoice, false);
                    }
                    Choice newChoice = new Choice(controller.getChoiceText(), controller.getGrade(), imageChoice, question.getId());
                    ChoiceService.addChoice(newChoice);
                }
            }
        }

        labelAlert.setText("Update question successfully!");
    }

    private void addChoiceBox(int numberChoice) {
        try {
            for (int i = 0; i < numberChoice; i++) {
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
                choiceBoxController.setInfo(choice.getContent(), choice.getChoiceGrade(), choice.getChoiceImage());
                listChoiceBoxController.add(choiceBoxController);
                vBoxAddChoiceBox.getChildren().add(countChoice++, root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //ham reset các ô điền về trống
    private void reset() {
        if (!listChoiceBoxController.isEmpty())
            listChoiceBoxController.get(0).reset();  // reset total grade = 0
        listChoiceBoxController.clear();
        vBoxAddChoiceBox.getChildren().clear();
        countChoice = 0;
        System.out.println(ChoiceBoxController.getCountChoice());

        labelAlert.setText("");
        updateCategory();
    }

    //ham update category vao combo-box
    private void updateCategory() {
        List<Category> listCategory = CategoryService.getCategories();
        for (Category category : listCategory) {
            kindOfCategory.getItems().add(category.toString());
        }
    }

    //ham update cac thong tin question vao cac o khi bam edit trong question_bank
    public void setInfo(Question oldQuestion, String category_name) {
        this.question = oldQuestion;
        text_QuestionName.setText(oldQuestion.getQuestion_name());
        text_QuestionText.setText(oldQuestion.getQuestion_text());
        kindOfCategory.setValue(category_name);
        text_DefaultMark.setText(String.valueOf(oldQuestion.getMark()));

        // image question
        imagePath = oldQuestion.getQuestionImage();
        if (imagePath != null) {
            Image image = new Image(imagePath);
            image_question.setImage(image);
            btn_image_question.setText("Change image");
        } else {
            btn_image_question.setText("Add image");
        }

        //set cac choice vao cac o
        listChoice = ChoiceService.getChoice(oldQuestion.getQuestion_id());
        addChoiceBox(listChoice);
    }
}

