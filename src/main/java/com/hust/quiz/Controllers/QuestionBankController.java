package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Category;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Services.LoaderDocxService;
import com.hust.quiz.Services.LoaderTextService;
import com.hust.quiz.Services.QuestionService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class QuestionBankController implements Initializable {
    @FXML
    private Button btn_turn_editing_on;
    @FXML
    private ImageView btn_menu_return;
    @FXML
    private TabPane tabPane;

    // TAB QUESTION
    @FXML
    private VBox container;
    @FXML
    private Label message;
    @FXML
    private CheckBox showSubcategoryQuestionCheckbox;
    @FXML
    private AnchorPane pane_question_list;
    @FXML
    private Button btnCreateQuestion;
    @FXML
    private TreeView<String> category;
    @FXML
    private ComboBox<String> btn_category;
    @FXML
    private VBox listQuestion_vbox;

    // TAB CATEGORY
    private int parent_id = -1;
    @FXML
    private TreeView<String> category2;
    @FXML
    private ComboBox<String> btn_category2;
    @FXML
    private Button btnAddCategory;
    @FXML
    private TextField nameCategory, idNewCategory;
    @FXML
    private TextArea categoryInfo;
    @FXML
    private Label noticeAddCategory;

    // TAB IMPORT
    private String directory;
    @FXML
    private VBox file_not_found;
    @FXML
    private Label file_name;
    @FXML
    private Button btnChooseFile, btnImport;

    // TAB EXPORT


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // # configure btn_menu_return - comeback to home
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
        // # configure btn_turn_editing_on - add quiz scene
        btn_turn_editing_on.setOnAction(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.ADD_QUIZ));

        updateCategory();

        // TAB QUESTION
        btnCreateQuestion.setOnAction(actionEvent -> ViewFactory.getInstance().routes(ViewFactory.SCENES.ADD_QUESTION));

        // configure btn_category
        btn_category.getParent().setOnMouseClicked(event -> category.setVisible(false));
        btn_category.setOnMouseClicked(event -> category.setVisible(!category.isVisible()));
        // double-click on item -> show question list
        category.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem = category.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    container.getChildren().remove(message);

                    String category_name = selectedItem.getValue();

                    btn_category.setValue(category_name);
                    category.setVisible(false);

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

        // TAB CATEGORY
        category2.getParent().setOnMouseClicked(event -> category2.setVisible(false));
        btn_category2.setOnMouseClicked(event -> category2.setVisible(!category2.isVisible()));
        category2.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem = category2.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    parent_id = CategoryService.getParentID(selectedItem.getValue());
                    System.out.println("Parent id: " + parent_id);
                    btn_category2.setValue(selectedItem.getValue());
                    category2.setVisible(false);
                }
            }
        });

        btnAddCategory.setOnAction(actionEvent -> {
            String name = nameCategory.getText();
            String info = categoryInfo.getText();
            if (name.equals("") || idNewCategory.getText().equals("")) {
                noticeAddCategory.setText("Please fill in all fields required!");
            } else if (parent_id < 0) {
                noticeAddCategory.setText("Please choose a parent category!");
            } else {
                int id = Integer.parseInt(idNewCategory.getText());
                Category c = new Category(id, name, parent_id, 0, info);
                try {
                    CategoryService.addCategory(c);
                    updateCategory();
                    noticeAddCategory.setText("Add category successfully!");
                } catch (SQLException e) {
                    String errorMessage = e.getMessage();
                    System.out.println(errorMessage);
                    if (errorMessage.contains("Duplicate entry")) {
                        String[] parts = errorMessage.split("'");
                        String columnName = parts[3]; // Lấy tên của cột bị trùng lặp
                        if (columnName.contains("category_id")) {
                            noticeAddCategory.setText("ID is already existed!");
                        } else if (columnName.contains("name")) {
                            noticeAddCategory.setText("Name is already existed!");
                        }
                    } else {
                        // Xử lý các loại ngoại lệ khác
                        noticeAddCategory.setText("Add category failed!");
                        System.out.println(e.getMessage());
                    }
                }
            }
        });

        // TAB IMPORT
        btnChooseFile.setOnAction(actionEvent -> {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Choose Quiz");
            File selectedFile = filechooser.showOpenDialog(null);
            if (selectedFile != null) {
                directory = selectedFile.getAbsolutePath();
                file_name.setText(selectedFile.getName());
                file_not_found.setVisible(false);
            } else {
                file_not_found.setVisible(true);
                file_name.setText("");
            }
        });
        btnImport.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if ((!directory.endsWith(".txt")) && (!directory.endsWith(".docx"))) {
                alert.setTitle("WARNING");
                alert.setHeaderText(null);
                alert.setContentText("WRONG FORMAT");
                alert.showAndWait();

            } else {
                String message;
                if (directory.endsWith(".txt")) {
                    message = LoaderTextService.importFile(directory);
                } else {
                    message = LoaderDocxService.importFile(directory);
                }
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
                file_not_found.setVisible(true);
                file_name.setText("");
                if (message != null && message.startsWith("Success")) {
                    updateCategory();
                }
            }
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

        category.setRoot(rootNode);
        category.setShowRoot(false);

        category2.setRoot(rootNode);
        category2.setShowRoot(false);
    }

    private void addToQuestionList(List<Question> questions, String category_name) {
        listQuestion_vbox.getChildren().clear();

        if (!questions.isEmpty()) {
            //add new_list_question
            for (Question q : questions) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/QuestionInfor.fxml"));
                    Parent questionInfo = loader.load();
                    QuestionInforController controller = loader.getController();
                    controller.updateInfoQuestion(q, category_name);
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

    public void setTabPane(int index) {
        tabPane.getSelectionModel().select(index);
    }

    public void reset() {
        btn_category.setValue("Default");
        listQuestion_vbox.getChildren().clear();
        pane_question_list.setVisible(false);
        showSubcategoryQuestionCheckbox.setSelected(false);
        category.setVisible(false);
        category2.setVisible(false);
        if (!container.getChildren().contains(message))
            container.getChildren().add(0, message);
        updateCategory();
        file_not_found.setVisible(true);
        file_name.setText("");
    }
}


