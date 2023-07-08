package com.hust.quiz.Services;

import com.hust.quiz.Models.Choice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChoiceService {
    /**
     * Get all choice from database with question_id
     *
     * @param question_id int
     * @return List of choice
     */
    public static List<Choice> getChoice(int question_id) {
        List<Choice> result = new ArrayList<>();
        try (Connection conn = Utils.getConnection()) {
            // write query
            String sql = "SELECT * FROM choice WHERE question_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, String.valueOf(question_id));

            // execute
            ResultSet rs = pst.executeQuery();

            // add questions found to list
            while (rs.next()) {
                Choice choice = new Choice(rs.getInt("choice_id"), rs.getString("choice_content"),
                        rs.getBoolean("choice_is_correct"), rs.getInt("choice_grade"), question_id);
                result.add(choice);
            }
            // close
            rs.close();
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    //add  choice to sql
    public static void addChoice(Choice choice) {
        try (Connection conn = Utils.getConnection()) {
            String sql = "INSERT INTO choice (choice_content, choice_is_correct, choice_grade, question_id)" +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, choice.getContent());
            pst.setBoolean(2, choice.getIsCorrect());
            pst.setInt(3, choice.getChoiceGrade());
            pst.setInt(4, choice.getQuestion_id());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Add list of choice to database
     *
     * @param choices List of choice
     */
    public static void addChoice(List<Choice> choices) {
        // Avoid create connection many times
        try {
            Connection conn = Utils.getConnection();

            Statement st = conn.createStatement();
            for (Choice choice : choices) {
                String sql = "INSERT INTO choice (choice_content, choice_is_correct, choice_grade, question_id)" +
                        " VALUES ('" + choice.getContent() + "', " + choice.getIsCorrect() + ", " + choice.getChoiceGrade() + ", " + choice.getQuestion_id() + ")";
                st.executeUpdate(sql);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
