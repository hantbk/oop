package com.hust.quiz.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ResourceBundle;


public class QuestionBankController implements Initializable {

    @FXML
    private ComboBox<String> btn_category;
    @FXML
    private TreeView<String> treeView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTreeView();

        // configure btn_category
        btn_category.getParent().setOnMouseClicked(event -> {
            treeView.setVisible(false);
        });
        btn_category.setOnMouseClicked(event -> {
            treeView.setVisible(!treeView.isVisible());
        });

        treeView.setOnMouseClicked(event -> {
            TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
            btn_category.setValue(selectedItem.getValue());
            if (event.getClickCount() == 2) {
                treeView.setVisible(!treeView.isVisible());
            }
        });



    }

    private void createTreeView() {
        TreeItem<String> rootNode = new TreeItem<>("Root Node");
        rootNode.setExpanded(true);

        // Create child nodes
        TreeItem<String> nodeA = new TreeItem<>("Node A");
        TreeItem<String> nodeB = new TreeItem<>("Node B");
        TreeItem<String> nodeC = new TreeItem<>("Node C");
        TreeItem<String> n1 = new TreeItem<>("Node C");
        TreeItem<String> n2 = new TreeItem<>("Node C");
        TreeItem<String> n3 = new TreeItem<>("Node C");
        TreeItem<String> n4 = new TreeItem<>("Node C");
        TreeItem<String> n5 = new TreeItem<>("Node C");
        TreeItem<String> n6 = new TreeItem<>("Node C");
        TreeItem<String> n7 = new TreeItem<>("Node C");
        TreeItem<String> n8 = new TreeItem<>("Node C");
        TreeItem<String> n9 = new TreeItem<>("Node C");
        TreeItem<String> n10 = new TreeItem<>("Node C");
        TreeItem<String> n11 = new TreeItem<>("Node C");
        TreeItem<String> n12 = new TreeItem<>("Node C");

        // Add child nodes to the root node
        rootNode.getChildren().addAll(nodeA, nodeB, nodeC, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12);
        treeView.setRoot(rootNode);
    }
}
