package com.hust.quiz.Services;

import com.hust.quiz.Models.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    public static List<Category> getCategories() {
        List<Category> results = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM category");
            while (rs.next()) {
                Category c = new Category(rs.getInt("category_id"), rs.getString("category_name"), rs.getInt("parent_id"),
                        rs.getInt("question_count"), rs.getString("category_info"));
                results.add(c);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    public static void addCategory(Category c) throws SQLException {
        try (Connection conn = Utils.getConnection()) {
            Statement st = conn.createStatement();
            String sql = "INSERT INTO category (category_id, category_name, parent_id, question_count, category_info) VALUES ('" + c.getId() +
                    c.getName() + "', " + c.getParent_id() + ", " + c.getQuestion_count() + ", " + ", '" + c.getCategory_info() + "')";
            st.executeUpdate(sql);
            st.close();
        }
    }

    // create when import file
    public static int addCategory(String category_name) {
        int id = 0;
        try (Connection conn = Utils.getConnection()) {
            String sql = "INSERT INTO category (category_name) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, category_name);

            pst.executeUpdate();

            // get id of the category
            id = getID(category_name);
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return id;
    }

    public static int getID(String category_name) {
        int id = 0;
        try (Connection conn = Utils.getConnection()) {
            // SELECT row have category_name
            String sql = "SELECT category_id FROM category WHERE category_name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, Category.getName(category_name));

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // return ID of the category
                id = rs.getInt("category_id");
            } else {
                System.out.println("Not found category_id have name is " + category_name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return id;
    }

    public static int getParentID(String category_name) {
        int parent_id = 0;
        try (Connection conn = Utils.getConnection()) {
            // SELECT row have category_name
            String sql = "SELECT parent_id FROM category WHERE category_name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, Category.getName(category_name));

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // return parent ID of the category
                parent_id = rs.getInt("parent_id");
            } else {
                System.out.println("Not found parent_id have name is " + category_name);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return parent_id;
    }
}
