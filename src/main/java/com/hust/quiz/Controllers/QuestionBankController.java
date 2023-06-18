package com.hust.quiz.Controllers;

import com.hust.quiz.Models.AikenFormatChecker;
import com.hust.quiz.Models.Category;
import com.hust.quiz.Models.FileChecker;
import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
    private Button btnChooseFile;

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
        btnCreateQuestion.setOnAction(actionEvent -> ViewFactory.getInstance().routes(ViewFactory.SCENES.MULTI_CHOICE));
        updateCategory();
        // configure btn_category
        btn_category.getParent().setOnMouseClicked(event -> category.setVisible(false));
        btn_category.setOnMouseClicked(event -> category.setVisible(!category.isVisible()));
        category.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> selectedItem = category.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    btn_category.setValue(selectedItem.getValue());
                    category.setVisible(!category.isVisible());
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

        btnChooseFile.setOnAction(actionEvent -> {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Open Aiken File");
            File selectedfile = filechooser.showOpenDialog(null);

            if(selectedfile != null) {
                String directory = selectedfile.getAbsolutePath();
                String new_directory = directory.replace("/", "//");
                System.out.println(new_directory);
                }else {
                System.out.println("file is not valid");
            }

            if(selectedfile != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("File Selected");
                alert.setHeaderText(null);
                if (FileChecker.isTextFile(selectedfile.getAbsolutePath().replace("/", "//")))
                    alert.setContentText(AikenFormatChecker.checkAikenFormat(selectedfile.getAbsolutePath().replace("/", "//")));
                else
                    alert.setContentText(AikenFormatChecker.checkAikenFormatDoc(selectedfile.getAbsolutePath().replace("/", "//")));
                alert.showAndWait();
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
        TreeItem<String> rootNode = new TreeItem<>("Root Node");
        treeItems = new HashMap<>();

        for (Category c : categories) {
            TreeItem<String> treeItem = new TreeItem<>(c.toString());
            treeItems.put(c.getId(), treeItem);
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


