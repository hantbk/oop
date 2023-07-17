package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Category;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Services.QuestionService;
import com.hust.quiz.Services.QuizService;
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
import java.util.*;

public class EditQuizController implements Initializable {
    //    private final List<Question> listQuestion = new ArrayList<>(); // list question of quiz
//    private final List<Question> listQuestionToAdd = new ArrayList<>(); // list question to add to quiz
//    private final List<Question> listQuestionToRemove = new ArrayList<>(); // list question to remove from quiz
    private final Set<Question> questionList = new HashSet<>();
    private final List<Question> listQuestionSelected = new ArrayList<>();
    private final List<Question> listQuestionRandom = new ArrayList<>();
    //QuestionInforFromBankController of select question pane
    private final List<QuestionInfoFromBankController> listQuesSelectController = new ArrayList<>();

    //EDIT QUIZ PANE
    @FXML
    private VBox vbox_questionEditPane;
    @FXML
    private ImageView btn_menu_return;
    @FXML
    private Label label_quiz_name_IT, label_quiz_name_edit, number_of_questions;
    @FXML
    private AnchorPane add_question_option;
    @FXML
    private HBox add_new_question, add_from_bank, add_random;
    @FXML
    private ImageView arrow_add;
    @FXML
    private Button btn_save_edit_quiz;
    @FXML
    private Label lb_totalOfMark;
    // BLURRRRRR
    @FXML
    private AnchorPane anchor_blur;

    // ADD QUESTIONS FROM BANK
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
    private VBox vbox_questionBank;
    @FXML
    private Button btn_add_ques_bank_selected;

    // ADD A RANDOM QUESTION
    @FXML
    private AnchorPane anchor_add_question_random;
    @FXML
    private ImageView btn_exit_add_random;
    @FXML
    private Button btn_add_ques_random;
    @FXML
    private ComboBox<String> cb_category_random;
    @FXML
    private Spinner<Integer> spinner_numQues = new Spinner<>();
    @FXML
    private CheckBox showSubcategoryQuestionCheckbox_random;
    @FXML
    private VBox listQuestion_vbox_random;
    @FXML
    private AnchorPane list_question_pane_random;
    private Quiz quiz;

    public void editQuizDisplayInfo(Quiz quiz) {
        this.quiz = quiz;
        this.label_quiz_name_IT.setText(quiz.getQuiz_name());
        this.label_quiz_name_edit.setText(quiz.getQuiz_name());
        this.questionList.addAll(QuizService.getQuestionQuiz(quiz.getQuiz_id()));
        this.updateEditPane();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_menu_return.setOnMouseClicked(event -> {
            this.resetEditPane();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });

        //add list question to quiz
        btn_save_edit_quiz.setOnAction(event -> {
            int quiz_id = quiz.getQuiz_id();
            List<Question> listQues = new ArrayList<>(this.questionList);
            QuizService.updateQuiz(quiz_id, listQues);
            this.resetEditPane();
        });

        arrow_add.setOnMouseClicked(event -> add_question_option.setVisible(!add_question_option.isVisible()));

        // TODO: configure add a new question
        add_new_question.setOnMouseClicked(event -> {
            add_question_option.setVisible(false);
        });

        // TODO: configure add question from bank
        add_from_bank.setOnMouseClicked(event -> {
            updateCategory();
            add_question_option.setVisible(false);
            anchor_blur.setVisible(true);
            anchor_add_question_bank.setVisible(true);
            pane_question_list.setVisible(false);

            // configure display TreeView
            tree_view_category.getParent().setOnMouseClicked(e -> tree_view_category.setVisible(false));
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

                        vbox_questionBank.getChildren().clear();
                        // check checkbox show subcategory is selected or not BEFORE select category
                        if (showSubcategoryQuestionCheckbox.isSelected()) {
                            if (listQuestions != null && !listQuestions.isEmpty()) {
                                listQuestions.addAll(currentCategoryQuestions);
                                addToQuestionList(listQuestions, category_name, pane_question_list);
                            } else if (!currentCategoryQuestions.isEmpty()) {
                                addToQuestionList(currentCategoryQuestions, category_name, pane_question_list);
                            } else {
                                pane_question_list.setVisible(false);
                            }
                        } else {
                            addToQuestionList(currentCategoryQuestions, category_name, pane_question_list);
                        }
                        // check checkbox show subcategory is selected or not AFTER select category
                        showSubcategoryQuestionCheckbox.setOnAction(actionEvent -> {
                            if (showSubcategoryQuestionCheckbox.isSelected()) {
                                if (listQuestions != null && !listQuestions.isEmpty()) {
                                    listQuestions.addAll(currentCategoryQuestions);
                                    addToQuestionList(listQuestions, category_name, pane_question_list);
                                }
                            } else {
                                addToQuestionList(currentCategoryQuestions, category_name, pane_question_list);
                            }
                        });
                    }
                }
            });

            // configure add selected questions
            btn_add_ques_bank_selected.setOnAction(e -> {
                this.listQuestionSelected.addAll(this.getQuestionSelected());
                if (!this.listQuestionSelected.isEmpty()) {
                    this.questionList.addAll(this.listQuestionSelected);
                    this.vbox_questionBank.getChildren().clear();
                    this.listQuestionSelected.clear();
                }
                this.updateEditPane();
                anchor_blur.setVisible(false);
                anchor_add_question_bank.setVisible(false);
            });

            // configure exit
            btn_exit_add_bank.setOnMouseClicked(e -> {
                anchor_blur.setVisible(false);
                anchor_add_question_bank.setVisible(false);
            });
        });

        // TODO: configure add a random question
        add_random.setOnMouseClicked(event -> {
            this.updateCategoryRandom();
            add_question_option.setVisible(false);
            anchor_blur.setVisible(true);
            anchor_add_question_random.setVisible(true);
            cb_category_random.setOnAction(e -> {
                listQuestion_vbox_random.getChildren().clear();
                String category_name = this.cb_category_random.getValue();
                int category_id = CategoryService.getID(category_name);
                List<Question> questionOfCategory = QuestionService.getQuestions(category_id);
                spinner_numQues.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, questionOfCategory.size(), 0, 1));
                spinner_numQues.setOnMouseClicked(eventSpinner -> {
                    this.listQuestion_vbox_random.getChildren().clear();
                    listQuestionRandom.clear();
                    listQuestionRandom.addAll(QuestionService.getRandomQuestion(category_id, spinner_numQues.getValue()));
                    for (Question q : listQuestionRandom) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfoFromBank.fxml"));
                            Parent questionInfo = loader.load();
                            QuestionInfoFromBankController controller = loader.getController();
                            controller.updateQuestionInfo(q, category_name);
                            this.listQuestion_vbox_random.getChildren().add(questionInfo);
                        } catch (IOException excep) {
                            System.out.println(excep.getMessage());
                            excep.printStackTrace();
                        }
                    }
                });
            });

            // configure add random questions
            btn_add_ques_random.setOnAction(e -> {
                if (!this.listQuestionRandom.isEmpty()) {
                    this.questionList.addAll(this.listQuestionRandom);
                    this.listQuestion_vbox_random.getChildren().clear();
                    this.listQuestionRandom.clear();
                }
                this.updateEditPane();
                this.resetRandomPane();
                anchor_blur.setVisible(false);
                anchor_add_question_random.setVisible(false);
                // TODO: Add

            });
            // configure exit
            btn_exit_add_random.setOnMouseClicked(e -> {
                this.resetRandomPane();
                anchor_blur.setVisible(false);
                anchor_add_question_random.setVisible(false);
            });
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

    private void addToQuestionList(List<Question> questions, String category_name, AnchorPane listPane) {
        //xóa cái cũ để update
        if (!vbox_questionBank.getChildren().isEmpty()) {
            vbox_questionBank.getChildren().clear();
        }
        //xóa controller cũ
        if (!this.listQuesSelectController.isEmpty()) {
            this.listQuesSelectController.clear();
        }

        if (!questions.isEmpty()) {
            //add new_list_question
            for (Question q : questions) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfoFromBank.fxml"));
                    Parent questionInfo = loader.load();
                    QuestionInfoFromBankController controller = loader.getController();
                    this.listQuesSelectController.add(controller);
                    controller.updateQuestionInfo(q, category_name);
                    vbox_questionBank.getChildren().add(questionInfo);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            listPane.setVisible(true);
        } else {
            listPane.setVisible(false);
        }
    }

    public List<Question> getQuestionSelected() {
        List<Question> listQuesSelect = new ArrayList<>();
        if (!this.listQuesSelectController.isEmpty()) {
            for (QuestionInfoFromBankController controller : this.listQuesSelectController) {
                if (controller.getTicks()) {
                    listQuesSelect.add(controller.getQuestion());
                }
            }
        }
        return listQuesSelect;
    }

    //update question of quiz
    private void updateEditPane() {
        if (!vbox_questionEditPane.getChildren().isEmpty()) {
            vbox_questionEditPane.getChildren().clear();
        }
        this.number_of_questions.setText(questionList.size() + " questions");
        this.lb_totalOfMark.setText(questionList.size() + ".0");

        for (Question ques : questionList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfoFromBank.fxml"));
                Parent questionInfor = loader.load();
                QuestionInfoFromBankController controller = loader.getController();
                controller.updateQuestionInfo(ques, "");
                vbox_questionEditPane.getChildren().add(questionInfor);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void updateCategoryRandom() {
        List<Category> listCategory = CategoryService.getCategories();
        if (cb_category_random.isEditable()) {
            cb_category_random.getItems().clear();
        }
        for (Category category : listCategory) {
            cb_category_random.getItems().add(category.toString());
        }
    }

    public void resetRandomPane() {
        if (this.spinner_numQues.isEditable()) {
            this.spinner_numQues.getValueFactory().setValue(null);
        }
        if (this.cb_category_random.isEditable()) {
            cb_category_random.setValue(null);
        }
        this.listQuestion_vbox_random.getChildren().clear();

    }

    public void resetEditPane() {
        if (!this.questionList.isEmpty()) {
            this.questionList.clear();
        }
        if (!this.listQuesSelectController.isEmpty()) {
            this.listQuesSelectController.clear();
        }
    }
}