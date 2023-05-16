package com.hust.quiz.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;

public class QuestionBankController implements Initializable {

    @FXML
    private TreeView<String> btn_category;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       createTreeView();
    }

    private void createTreeView() {
        TreeItem<String> rootItem = new TreeItem<>("Root");
        TreeItem<String> item1 = new TreeItem<>("Item 1");
        TreeItem<String> item2 = new TreeItem<>("Item 2");
        rootItem.getChildren().addAll(item1, item2);

        btn_category.setRoot(rootItem);
    }
}
