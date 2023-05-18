package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
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
        TreeItem<String> rootNode = Model.getInstance().query("SELECT * FROM quiz.course_categories");



        category.setRoot(rootNode);
        category.setShowRoot(false);
    }
}
