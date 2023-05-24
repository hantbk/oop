package com.hust.quiz.Services;

import com.hust.quiz.Models.Category;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    public List<Category> getCategories() {
        List<Category> results = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM category");

            while (rs.next()) {
                Category c = new Category(rs.getInt("id"), rs.getString("name"),
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
            String sql = "INSERT INTO category (name, parent_id, course_count, id_number, category_info) VALUES ('" +
                    c.getName() + "', " + c.getParent_id() + ", " + c.getCourse_count() + ", " + c.getId_number() + ", '" + c.getCategory_info() + "')";
            st.executeUpdate(sql);
            st.close();
        }
    }
}
