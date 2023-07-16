package com.hust.quiz.Services;

import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionService {

    //lấy id bằng question_name
    public static int getId(String question_name) throws SQLException {
        try (Connection conn = Utils.getConnection()) {
            // SELECT row have category_name
            String sql = "SELECT question_id FROM question WHERE question_name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, question_name);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // return ID of the category
                return rs.getInt("question_id");
            } else {
                System.out.println("No ID found");
                return -1;
            }
        }
    }

    //add question to database
    public static void addQuestion(Question question) throws SQLException {
        try (Connection conn = Utils.getConnection()) {
            String sql = "INSERT INTO question (question_name, question_text, question_image, mark, category_id)" +
                    " VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, question.getQuestion_name());
            pst.setString(2, question.getQuestion_text());
            pst.setString(3, question.getQuestionImage());
            pst.setInt(4, question.getMark());
            pst.setString(5, String.valueOf(question.getCategory_id()));
            pst.executeUpdate();
            pst.close();
        }
    }

    public static void addQuestion(List<Question> questions, int category_id) throws SQLException {
        // Avoid create connection many times
        try (Connection conn = Utils.getConnection()) {
            Statement stmt = conn.createStatement();

            String sql = "INSERT INTO question (question_name, question_text, question_image, mark, category_id)" +
                    " VALUES (?, ?, ?, ?, ?)";
            for (Question question : questions) {
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, question.getQuestion_name());
                pst.setString(2, question.getQuestion_text());
                pst.setString(3, question.getQuestionImage());
                pst.setInt(4, question.getMark());
                pst.setString(5, String.valueOf(category_id));
                pst.executeUpdate();
                pst.close();
            }

            stmt.close();
        }
    }

    /**
     * Get questions by category_id
     *
     * @param category_id int
     * @return List of questions
     */
    public static List<Question> getQuestions(int category_id) {
        List<Question> result = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            // write query
            String sql = "SELECT * FROM question WHERE category_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, String.valueOf(category_id));

            // execute
            ResultSet rs = stmt.executeQuery();

            // add questions found to list
            while (rs.next()) {
                Question question = new Question(rs.getInt("question_id"), rs.getString("question_name"),
                        rs.getString("question_text"), rs.getString("question_image"),
                        rs.getInt("mark"), rs.getInt("category_id"));
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

    //
    public static List<Question> getRandomQuestion(int category_id, int numQuestion){
        List<Question> result = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            // write query
            String sql = "SELECT * FROM question WHERE category_id = ? ORDER BY RAND() LIMIT ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, category_id);
            stmt.setInt(2, numQuestion);

            // execute
            ResultSet rs = stmt.executeQuery();

            // add questions found to list
            while (rs.next()) {
                Question question = new Question(rs.getInt("question_id"), rs.getString("question_name"),
                        rs.getString("question_text"), rs.getString("question_image"),
                        rs.getInt("mark"), rs.getInt("category_id"));
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

    /**
     * Get last question id
     *
     * @return int
     */
    public static int getLastQuestionId() {
        try (Connection conn = Utils.getConnection()) {
            String sql = "SELECT question_id FROM question ORDER BY question_id DESC LIMIT 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("question_id");
            }
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * Get questions from subcategory
     *
     * @param categoryId id of parent category
     * @return List of questions
     */
    public static List<Question> getQuestionFromSubcategory(int categoryId) {
        List<Integer> subcategoryIds = new ArrayList<>();

        try (Connection conn = Utils.getConnection()) {
            String query = "SELECT category_id FROM category WHERE parent_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                subcategoryIds.add(rs.getInt("category_id"));
            }

            if (!subcategoryIds.isEmpty()) {
                query = "SELECT * FROM question WHERE category_id IN (" + String.join(",", Collections.nCopies(subcategoryIds.size(), "?")) + ")";
                stmt = conn.prepareStatement(query);
                for (int i = 0; i < subcategoryIds.size(); i++) {
                    stmt.setInt(i + 1, subcategoryIds.get(i));
                }
                rs = stmt.executeQuery();
                List<Question> questions = new ArrayList<>();
                while (rs.next()) {
                    Question question = new Question(rs.getInt("question_id"), rs.getString("question_name"),
                            rs.getString("question_text"), rs.getString("question_image"),
                            rs.getInt("mark"), rs.getInt("category_id"));
                    questions.add(question);
                }
                return questions;
            } else {
                return null; // trả về null nếu không có danh mục con
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateQuestion(Question question) {
        try (Connection conn = Utils.getConnection()) {
            String sql = "UPDATE question SET question_name = ?, question_text = ?, question_image = ?, mark = ?, category_id = ? WHERE question_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, question.getQuestion_name());
            pst.setString(2, question.getQuestion_text());
            pst.setString(3, question.getQuestionImage());
            pst.setInt(4, question.getMark());
            pst.setString(5, String.valueOf(question.getCategory_id()));
            pst.setString(6, String.valueOf(question.getQuestion_id()));
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}







