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
                        rs.getInt("choice_grade"), question_id);
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
            String sql = "INSERT INTO choice (choice_content, choice_grade, question_id)" +
                    " VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, choice.getContent());
            pst.setDouble(2, choice.getChoiceGrade());
            pst.setInt(3, choice.getQuestion_id());
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
                String sql = "INSERT INTO choice (choice_content, choice_grade, question_id)" +
                        " VALUES ('" + choice.getContent() + "', " + choice.getChoiceGrade() + ", " + choice.getQuestion_id() + ")";
                st.executeUpdate(sql);
            }
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void updateChoice(List<Choice> choices) {
        // Avoid create connection many times
        try {
            Connection conn = Utils.getConnection();

            Statement st = conn.createStatement();
            for (Choice choice : choices) {
                String sql = "UPDATE choice SET choice_content = '" + choice.getContent()
                        + "', choice_grade = " + choice.getChoiceGrade() + " WHERE choice_id = " + choice.getId();
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
