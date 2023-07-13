package com.hust.quiz.Services;

import com.hust.quiz.Models.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuizService {



    public static void addQuiz(Quiz quiz) {
        try (Connection conn = Utils.getConnection()) {
            String sql = "INSERT INTO quiz (quiz_name, quiz_description,quiz_time_limit,quiz_time_format) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, quiz.getQuiz_name());
            pst.setString(2, quiz.getQuiz_description());
            pst.setInt(3, quiz.getTimeLimit());
            pst.setString(4, quiz.getTimeFormat());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
