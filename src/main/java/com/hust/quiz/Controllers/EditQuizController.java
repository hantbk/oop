package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Category;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Services.QuestionService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditQuizController implements Initializable {
    @FXML
    private ImageView btn_menu_return;
    @FXML
    private Label label_quiz_name_IT, label_quiz_name_edit;
    @FXML
    private Label number_of_questions;
    @FXML
    private AnchorPane add_question_option;
    @FXML
    private HBox add_new_question;
    @FXML
    private HBox add_from_bank;
    @FXML
    private HBox add_random;
    @FXML
    private ImageView arrow_add;

    // ADD QUESTIONS FROM BANK
    @FXML
    private AnchorPane anchor_blur;
    @FXML
    private AnchorPane anchor_add_question_bank;
    @FXML
    private ImageView btn_exit_add_bank;
    @FXML
    private AnchorPane pane_question_list;
    @FXML
    private ComboBox<String> btn_category;
    @FXML
    private TreeView<String> tree_view_category;
    @FXML
    private CheckBox showSubcategoryQuestionCheckbox;
    @FXML
    private VBox listQuestion_vbox;

    public void editQuizDisplayInfo(String quizName) {
        // TODO: Update num
        label_quiz_name_IT.setText(quizName);
        label_quiz_name_edit.setText(quizName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_menu_return.setOnMouseClicked(event -> {
            ViewFactory.getInstance().updateQuizHome();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        add_question_option.setVisible(false);
        anchor_blur.setVisible(false);
        anchor_add_question_bank.setVisible(false);

        arrow_add.setOnMouseClicked(event -> add_question_option.setVisible(true));

        // configure add a new question
        add_new_question.setOnMouseClicked(event -> {
            add_question_option.setVisible(false);
        });
        // configure add question from bank
        add_from_bank.setOnMouseClicked(event -> {
            updateCategory();
            add_question_option.setVisible(false);
            anchor_blur.setVisible(true);
            anchor_add_question_bank.setVisible(true);
            pane_question_list.setVisible(false);

            // configure display TreeView
            btn_category.getParent().setOnMouseClicked(e -> tree_view_category.setVisible(false));
            btn_category.setOnMouseClicked(e -> tree_view_category.setVisible(!tree_view_category.isVisible()));

            tree_view_category.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    TreeItem<String> selectedItem = tree_view_category.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        String category_name = selectedItem.getValue();
                        btn_category.setValue(category_name);
                        tree_view_category.setVisible(false);

                        // Find ID of the category
                        int idCategory = CategoryService.getID(category_name);
                        List<Question> currentCategoryQuestions = QuestionService.getQuestions(idCategory);
                        List<Question> listQuestions = QuestionService.getQuestionFromSubcategory(idCategory);

                        listQuestion_vbox.getChildren().clear();
                        // check checkbox show subcategory is selected or not BEFORE select category
                        if (showSubcategoryQuestionCheckbox.isSelected()) {
                            if (listQuestions != null && !listQuestions.isEmpty()) {
                                listQuestions.addAll(currentCategoryQuestions);
                                addToQuestionList(listQuestions, category_name);
                            } else if (!currentCategoryQuestions.isEmpty()) {
                                addToQuestionList(currentCategoryQuestions, category_name);
                            } else {
                                pane_question_list.setVisible(false);
                            }
                        } else {
                            addToQuestionList(currentCategoryQuestions, category_name);
                        }
                        // check checkbox show subcategory is selected or not AFTER select category
                        showSubcategoryQuestionCheckbox.setOnAction(actionEvent -> {
                            if (showSubcategoryQuestionCheckbox.isSelected()) {
                                if (listQuestions != null && !listQuestions.isEmpty()) {
                                    listQuestions.addAll(currentCategoryQuestions);
                                    addToQuestionList(listQuestions, category_name);
                                }
                            } else {
                                addToQuestionList(currentCategoryQuestions, category_name);
                            }
                        });
                    }
                }
            });

            // configure exit
            btn_exit_add_bank.setOnMouseClicked(e -> {
                anchor_blur.setVisible(false);
                anchor_add_question_bank.setVisible(false);
            });
        });
        // configure add a random question
        add_random.setOnMouseClicked(event -> {
            add_question_option.setVisible(false);
        });
    }

    private void expandAll(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandAll(child);
            }
        }
    }
    private void updateCategory() {
        List<Category> categories = CategoryService.getCategories();
        // create TreeItem
        TreeItem<String> rootNode = new TreeItem<>("Root Node");
        Map<Integer, TreeItem<String>> treeItems = new HashMap<>();

        for (Category c : categories) {
            TreeItem<String> treeItem = new TreeItem<>(c.toString()); // line 33 - Category.java
            treeItems.put(c.getId(), treeItem); // treeItem is return of toString()
            // btn_category.getValue() return treeItem
            int parent_id = c.getParent_id();
            if (parent_id == 0) {
                rootNode.getChildren().add(treeItem);
            } else {
                treeItems.get(parent_id).getChildren().add(treeItem);
            }
        }
        rootNode.setExpanded(true);
        expandAll(rootNode);

        tree_view_category.setRoot(rootNode);
        tree_view_category.setShowRoot(false);
    }

    private void addToQuestionList(List<Question> questions, String category_name) {
        listQuestion_vbox.getChildren().clear();

        if (!questions.isEmpty()) {
            //add new_list_question
            for (Question q : questions) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfoFromBank.fxml"));
                    Parent questionInfo = loader.load();
                    QuestionInfoFromBankController controller = loader.getController();
                    controller.updateQuestionInfo(q, category_name);
                    listQuestion_vbox.getChildren().add(questionInfo);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            pane_question_list.setVisible(true);
        } else {
            pane_question_list.setVisible(false);
        }
    }
}