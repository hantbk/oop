package com.hust.quiz.Services;

import com.hust.quiz.Models.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    public List<Category> getCategories() {
        List<Category> results = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM category");
            while (rs.next()) {
                Category c = new Category(rs.getInt("category_id"), rs.getString("category_name"),
                        rs.getInt("parent_id"), rs.getInt("course_count"), rs.getInt("id_number"), rs.getString("category_info"));
                results.add(c);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    public void addCategory(Category c) throws SQLException {
        try (Connection conn = Utils.getConnection()) {
            Statement st = conn.createStatement();
            String sql = "INSERT INTO category (category_name, parent_id, course_count, id_number, category_info) VALUES ('" +
                    c.getName() + "', " + c.getParent_id() + ", " + c.getCourse_count() + ", " + c.getId_number() + ", '" + c.getCategory_info() + "')";
            st.executeUpdate(sql);
            st.close();
        }
    }

    public int getID(String category_name) {
        int id = 0;
        try (Connection conn = Utils.getConnection()) {
            // SELECT row have category_name
            String sql = "SELECT category_id FROM category WHERE category_name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, category_name);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // return ID of the category
                id = rs.getInt("category_id");
            } else {
                System.out.println("No ID found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return id;
    }
}
