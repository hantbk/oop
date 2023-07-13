package com.hust.quiz.Services;

import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizService {

    public static void addQuiz(Quiz quiz) {
        try (Connection conn = Utils.getConnection()) {
            String sql = "INSERT INTO quiz (quiz_name, quiz_description, quiz_time_limit, quiz_time_format) VALUES (?, ?, ?, ?)";
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

    //lay id tu ten quiz
    public static int getId(String quizName) {
        int quiz_id = 0;
        try (Connection conn = Utils.getConnection()) {
            // SELECT row have category_name
            String sql = "SELECT quiz_id FROM quiz WHERE quiz_name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, quizName);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // return ID of the category
                quiz_id = rs.getInt("quiz_id");
            } else {
                System.out.println("No ID found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return quiz_id;
    }

    //lấy danh sách câu hỏi của quiz
    public static List<Question> getQuestionQuiz(int quiz_id) {
        List<Question> result = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            String sql = "SELECT * FROM question JOIN quiz_question ON question.question_id = quiz_question.question_id WHERE quiz_question.quiz_id = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(quiz_id));

            // execute
            ResultSet rs = stmt.executeQuery();

            // add questions found to list
            while (rs.next()) {
                Question question = new Question(rs.getInt("question_id"), rs.getString("question_name"),
                        rs.getString("question_text"),rs.getString("question_image"), 1, rs.getInt("category_id"));
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

    //lấy quiz từ id
    public static Quiz getQuiz(int quiz_id) {
        Quiz result = new Quiz();
        try (Connection conn = Utils.getConnection()) {
            // write query
            String sql = "SELECT * FROM quiz WHERE quiz_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quiz_id);

            // execute
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = new Quiz(rs.getInt("quiz_id"), rs.getString("quiz_name"),
                        rs.getString("quiz_description"), rs.getInt("quiz_time_limit"));
            } else {
                System.out.println("No Quiz found");
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
    public static Quiz getQuiz(String quiz_name) {
        int id = QuizService.getId(quiz_name);
        Quiz result = QuizService.getQuiz(id);
        return result;
    }

    public static List<String> getAllQuiz(){
        List<String> result = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            // write query
            String sql = "SELECT quiz_name FROM quiz";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // execute
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                result.add(rs.getString("quiz_name"));
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
