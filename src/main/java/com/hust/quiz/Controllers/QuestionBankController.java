package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Category;
import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class QuestionBankController implements Initializable {

    @FXML
    private TabPane tabPane;
    @FXML
    private ComboBox<String> btn_category;
    @FXML
    private TreeView<String> category;
    @FXML
    private Button btnCreateQuestion;

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
                btn_category.setValue(selectedItem.getValue());
                category.setVisible(!category.isVisible());
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
        ArrayList<TreeItem<String>> treeItems = new ArrayList<>();

        for (Category c : categories) {
            TreeItem<String> treeItem = new TreeItem<>(c.toString());
            treeItems.add(treeItem);
            int parent_id = c.getParent_id();
            if (parent_id == 0) {
                rootNode.getChildren().add(treeItem);
            } else {
                treeItems.get(parent_id - 1).getChildren().add(treeItem);
            }
        }
        rootNode.setExpanded(true);
        category.setRoot(rootNode);
        category.setShowRoot(false);
    }
}
