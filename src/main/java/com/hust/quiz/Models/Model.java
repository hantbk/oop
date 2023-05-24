package com.hust.quiz.Models;

import javafx.scene.control.TreeItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Model {
    static final String url = "jdbc:mysql://localhost:3306/quiz";
    static final String username = "root";
    static final String password = "root";
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static Model instance;

    private Model() {
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    // query
    public TreeItem<String> updateCategory() {
        TreeItem<String> rootNode = new TreeItem<>("Root Node");
        rootNode.setExpanded(true);
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(url, username, password);
            if (conn == null) {
                System.out.println("Connect failed");
                return rootNode;
            }
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM quiz.course_categories");

            ArrayList<TreeItem<String>> treeItems = new ArrayList<>();

            while (resultSet.next()) {
                TreeItem<String> treeItem = new TreeItem<>(resultSet.getString("name")
                        + " (" + resultSet.getInt("course_count") + ")");
                treeItems.add(treeItem);
                int parent_id = resultSet.getInt("parent_id");
                if (parent_id == 0) {
                    rootNode.getChildren().add(treeItem);
                } else {
                    treeItems.get(parent_id - 1).getChildren().add(treeItem);
                }
            }

            // Clean-up environment
            resultSet.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return rootNode;
    }
}
