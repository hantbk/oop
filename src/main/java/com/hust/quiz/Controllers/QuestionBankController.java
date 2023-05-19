package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;


public class QuestionBankController implements Initializable {

    @FXML
    private ComboBox<String> btn_category;
    @FXML
    private TreeView<String> category;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    private void updateCategory() {
        TreeItem<String> rootNode = Model.getInstance().updateCategory();

        category.setRoot(rootNode);
        category.setShowRoot(false);
    }
}
