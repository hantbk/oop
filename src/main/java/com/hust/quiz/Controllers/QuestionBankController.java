package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Category;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Services.QuestionService;
import com.hust.quiz.Services.Utils;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.sql.*;
import java.util.*;

public class QuestionBankController implements Initializable {
    private Map<Integer, TreeItem<String>> treeItems;
    private int parent_id = 0;
    @FXML
    private TabPane tabPane;
    @FXML
    private ComboBox<String> btn_category, btn_category2;
    @FXML
    private TreeView<String> category, category2;
    @FXML
    private Button btnCreateQuestion, btnAddCategory;
    @FXML
    private TextField nameCategory, idNewCategory;
    @FXML
    private TextArea categoryInfo;
    @FXML
    private Label noticeAddCategory;
    @FXML
    private ScrollPane questionBankList;
    @FXML
    private AnchorPane pane_question_list;

    // https://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value
    public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
        Set<T> keys = new HashSet<>();
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private static void expandAll(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandAll(child);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCategory();
        btnCreateQuestion.setOnAction(actionEvent -> ViewFactory.getInstance().routes(ViewFactory.SCENES.MULTI_CHOICE));
        // configure btn_category
        btn_category.getParent().setOnMouseClicked(event -> category.setVisible(false));
        btn_category.setOnMouseClicked(event -> category.setVisible(!category.isVisible()));
        // double-click on item -> show question list
        category.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem = category.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    btn_category.setValue(selectedItem.getValue());
                    category.setVisible(!category.isVisible());
                    // add questions found in scrollPane by adding in listView
                    QuestionService questionService = new QuestionService();
                    // Get category_name
                    String btn_content = btn_category.getValue();
                    int openParenIndex = btn_content.indexOf(" (");
                    if (openParenIndex < 0) { // If we have no " ("
                        openParenIndex = btn_content.length();
                    }
                    String category_name = btn_content.substring(0, openParenIndex);
                    // Get the correct category_name: System.out.println(category_name);
                    // Find ID of the category
                    CategoryService categoryService = new CategoryService();
                    int id = categoryService.getID(category_name);
                    // Get the correct ID: System.out.println(id);
                    // DONE: using getQuestions(String category) in QuestionService: waiting for updating sql file
                    List<Question> questionList = questionService.getQuestions(id);
                    // Check if category has questions or not
                    if (!questionList.isEmpty()) {
                        ListView<HBox> listView = new ListView<>();
                        listView.setPrefSize(1089, 143);

                        // put every question in the list in listView
                        for (Question item : questionList) {
                            // checkbox first
                            CheckBox checkBox = new CheckBox();
                            checkBox.setPadding(new Insets(0, 10, 0, 0));

                            // question content
                            // test: System.out.println(item.getQuestion());
                            Label label = new Label(item.getQuestionContent());
                            label.setPrefWidth(990);
                            label.setFont(new Font(15));

                            // Button needs to edit: NOT DONE
                            Button btn_edit = new Button("edit");
                            btn_edit.setFont(Font.font(15));
                            btn_edit.setStyle("-fx-border-style: none; -fx-text-fill: #19a7ce");

                            // hBox contains all things CENTER_LEFT
                            HBox hBox = new HBox(checkBox, label, btn_edit);
                            hBox.setAlignment(Pos.CENTER_LEFT);

                            // adding hBox into listView
                            listView.getItems().add(hBox);
                        }
                        // show list
                        questionBankList.setContent(listView);
                        pane_question_list.setVisible(true);
                    } else {
                        pane_question_list.setVisible(false);
                    }
                }
            }
        });

        category2.getParent().setOnMouseClicked(event -> category2.setVisible(false));
        btn_category2.setOnMouseClicked(event -> category2.setVisible(!category2.isVisible()));
        category2.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem = category2.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    parent_id = getKeysByValue(treeItems, selectedItem).iterator().next();
                    System.out.println(parent_id);
                    btn_category2.setValue(selectedItem.getValue());
                    category2.setVisible(!category2.isVisible());
                }
            }
        });

        btnAddCategory.setOnAction(actionEvent -> {
            CategoryService s = new CategoryService();
            String name = nameCategory.getText();
            String info = categoryInfo.getText();
            if (name.equals("") || idNewCategory.getText().equals("")) {
                noticeAddCategory.setText("Please fill in all fields required!");
            } else if (parent_id == 0) {
                noticeAddCategory.setText("Please choose a parent category!");
            } else {
                int id = Integer.parseInt(idNewCategory.getText());
                Category c = new Category(name, parent_id, 0, id, info);
                try {
                    s.addCategory(c);
                    updateCategory();
                    noticeAddCategory.setText("Add category successfully!");
                } catch (SQLException e) {
                    String errorMessage = e.getMessage();
                    System.out.println(errorMessage);
                    if (errorMessage.contains("Duplicate entry")) {
                        String[] parts = errorMessage.split("'");
                        String columnName = parts[3]; // Lấy tên của cột bị trùng lặp
                        if (columnName.contains("id_number")) {
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
    }

    public void setTabPane(int index) {
        SelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        selectionModel.clearSelection();
        selectionModel.select(index);
    }

    private void updateCategory() {
        CategoryService s = new CategoryService();
        List<Category> categories = s.getCategories();
        // create TreeItem
        TreeItem<String> rootNode = new TreeItem<>("Root Node");
        treeItems = new HashMap<>(); // private Map<Integer, TreeItem<String>> treeItems; - line 24

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
}
