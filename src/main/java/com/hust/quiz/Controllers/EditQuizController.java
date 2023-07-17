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
    private final Set<Question> listQuestion = new HashSet<>(); // list question of quiz
    private final Set<Question> listQuestionToAdd = new HashSet<>(); // list question to add to quiz
    private final Set<Question> listQuestionToRemove = new HashSet<>(); // list question to remove from quiz
    //QuestionInfoFromBankController of select question pane
    private final List<QuestionInfoFromBankController> listQuesSelectController = new ArrayList<>();
    private final List<Question> listQuestionRandom = new ArrayList<>();
    private Set<Question> addedListQuestion = new HashSet<>(); // list question added to quiz
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
    private AnchorPane pane_questionBankList;
    @FXML
    private ComboBox<String> btn_category;
    @FXML
    private TreeView<String> tree_view_category;
    @FXML
    private CheckBox showSubcategoryQuestionCheckbox, checkBox_selectAll;
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
    private VBox vbox_questionRandom;
    private Quiz quiz;

    public void editQuizDisplayInfo(Quiz quiz) {
        this.quiz = quiz;
        label_quiz_name_IT.setText(quiz.getQuiz_name());
        label_quiz_name_edit.setText(quiz.getQuiz_name());
        addedListQuestion.addAll(QuizService.getQuestionQuiz(quiz.getQuiz_id()));
        listQuestion.addAll(addedListQuestion);
        updateEditPane();
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
            List<Question> listAdd = new ArrayList<>(listQuestionToAdd);
            List<Question> listRemove = new ArrayList<>(listQuestionToRemove);

            QuizService.updateQuestionQuiz(quiz_id, listAdd, listRemove);
            addedListQuestion = listQuestion;
            this.resetEditPane(); // don't reset before update QuestionQuiz
            ViewFactory.getInstance().routes(ViewFactory.SCENES.QUIZ_VIEW);
        });

        arrow_add.setOnMouseClicked(event -> add_question_option.setVisible(!add_question_option.isVisible()));

        // TODO: configure add a new question
        add_new_question.setOnMouseClicked(event -> {
            add_question_option.setVisible(false);
//            anchor_blur.setVisible(true);

        });

        // TODO: configure add question from bank
        add_from_bank.setOnMouseClicked(event -> {
            updateCategory();
            add_question_option.setVisible(false);
            anchor_blur.setVisible(true);
            anchor_add_question_bank.setVisible(true);

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
                                addToQuestionList(listQuestions, category_name);
                            } else if (!currentCategoryQuestions.isEmpty()) {
                                addToQuestionList(currentCategoryQuestions, category_name);
                            } else {
                                pane_questionBankList.setVisible(false);
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

            checkBox_selectAll.setOnAction(e -> {
                for (QuestionInfoFromBankController controller : this.listQuesSelectController) {
                    controller.setTicks(!controller.getTicks());
                }
            });

            // configure add selected questions
            btn_add_ques_bank_selected.setOnAction(e -> {
                for (QuestionInfoFromBankController controller : this.listQuesSelectController) {
                    Question question = controller.getQuestion();
                    if (controller.getTicks() && !controller.isAdded()) {
                        listQuestionToAdd.add(question);
                        System.out.println("add id: " + question.getQuestion_id());
                    } else if (!controller.getTicks() && controller.isAdded()) {
                        listQuestionToRemove.add(question);
                        System.out.println("remove id: " + question.getQuestion_id());
                    }
                    if (controller.getTicks()) {
                        listQuestion.add(question);
                    } else {
                        listQuestion.remove(question);
                    }
                }
                updateEditPane();
                resetQuestionBankPane();
            });

            // configure exit
            btn_exit_add_bank.setOnMouseClicked(e -> resetQuestionBankPane());
        });

        // TODO: configure add a random question
        add_random.setOnMouseClicked(event -> {
            this.updateCategoryRandom();
            add_question_option.setVisible(false);
            anchor_blur.setVisible(true);
            anchor_add_question_random.setVisible(true);

            cb_category_random.setOnMouseClicked(e -> {
                vbox_questionRandom.getChildren().clear();
                String category_name = this.cb_category_random.getValue();
                if (category_name == null) {
                    return;
                }
                Category category = CategoryService.getCategoryByName(category_name);

                spinner_numQues.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, category.getQuestion_count(), 0, 1));
                spinner_numQues.setOnMouseClicked(eventSpinner -> {
                    vbox_questionRandom.getChildren().clear();

                    listQuestionRandom.clear();
                    listQuestionRandom.addAll(QuestionService.getRandomQuestion(category.getId(), spinner_numQues.getValue()));

                    for (Question q : listQuestionRandom) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfoFromBank.fxml"));
                            Parent questionInfo = loader.load();
                            QuestionInfoFromBankController controller = loader.getController();
                            controller.updateQuestionInfo(q, category_name);
                            controller.setTicks(true);
                            vbox_questionRandom.getChildren().add(questionInfo);
                        } catch (IOException except) {
                            System.out.println(except.getMessage());
                            except.printStackTrace();
                        }
                    }
                });
            });

            // configure add random questions
            btn_add_ques_random.setOnAction(e -> {
                listQuestion.addAll(listQuestionRandom);
                listQuestionToAdd.addAll(listQuestionRandom);

                updateEditPane();
                resetRandomPane();
            });
            // configure exit
            btn_exit_add_random.setOnMouseClicked(e -> this.resetRandomPane());
        });
    }

    private void addToQuestionList(List<Question> questions, String category_name) {
        //xóa cái cũ để update
        vbox_questionBank.getChildren().clear();
        //xóa controller cũ
        listQuesSelectController.clear();

        if (!questions.isEmpty()) {
            //add new_list_question
            for (Question q : questions) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfoFromBank.fxml"));
                    Parent questionInfo = loader.load();
                    QuestionInfoFromBankController controller = loader.getController();
                    controller.updateQuestionInfo(q, category_name);

                    if (this.listQuestion.contains(q)) {
                        controller.setTicks(true);
                    }
                    if (addedListQuestion.contains(q)) {
                        controller.setAdded(true);
                    }

                    listQuesSelectController.add(controller);
                    vbox_questionBank.getChildren().add(questionInfo);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            pane_questionBankList.setVisible(true);
        } else {
            pane_questionBankList.setVisible(false);
        }
    }

    //update question of quiz
    private void updateEditPane() {
        vbox_questionEditPane.getChildren().clear();
        this.number_of_questions.setText(listQuestion.size() + " questions");
        this.lb_totalOfMark.setText(listQuestion.size() + ".0");

        for (Question ques : listQuestion) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfoFromBank.fxml"));
                Parent questionInfo = loader.load();
                QuestionInfoFromBankController controller = loader.getController();
                controller.updateQuestionInfo(ques, "");
                controller.setTicks(true);
                vbox_questionEditPane.getChildren().add(questionInfo);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void updateCategoryRandom() {
        List<Category> listCategory = CategoryService.getCategories();
        cb_category_random.getItems().clear();
        for (Category category : listCategory) {
            cb_category_random.getItems().add(category.toString());
        }
    }

    private void resetRandomPane() {
        listQuestionRandom.clear();
//        spinner_numQues.getValueFactory().setValue(0); // has bug
        cb_category_random.setValue(null);
        vbox_questionRandom.getChildren().clear();
        anchor_blur.setVisible(false);
        anchor_add_question_random.setVisible(false);
    }

    private void resetEditPane() {
        listQuestionToAdd.clear();
        listQuestionToRemove.clear();
        listQuestion.clear();
        addedListQuestion.clear();
    }

    private void resetQuestionBankPane() {
        anchor_blur.setVisible(false);
        anchor_add_question_bank.setVisible(false);
        vbox_questionBank.getChildren().clear();
        pane_questionBankList.setVisible(false);
        tree_view_category.setVisible(false);
        btn_category.setValue("Default");
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

    private void expandAll(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandAll(child);
            }
        }
    }
}