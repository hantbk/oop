package com.hust.quiz.Services;

import com.hust.quiz.Models.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {
    public List<Question> getQuestions(int category_id) {
        List<Question> result = new ArrayList<>();
        try (Connection conn = Utils.getConnection()){
            // write query
            String sql = "SELECT * FROM question WHERE category_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(category_id));

            // execute
            ResultSet rs = stmt.executeQuery();

            // add questions found to list
            while (rs.next()) {
                Question question = new Question(rs.getInt("question_id"), rs.getString("question_content"),
                        rs.getInt("category_id"));
                result.add(question);
            }
            // close
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
