package com.hust.quiz.Services;

import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Choice;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChoiceService {
    public List<Choice> getChoice(int question_id) {
        List<Choice> result = new ArrayList<>();
        try (Connection conn = Utils.getConnection()){
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
    public void addChoice(Choice choice) {
        try(Connection conn = Utils.getConnection()){
            String sql = "INSERT INTO choice (choice_content, choice_is_correct, choice_grade, question_id)" +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, choice.getContent());
            pst.setBoolean(2, choice.getIsCorrect());
            pst.setInt(3, choice.getChoiceGrade());
            pst.setInt(4, choice.getQuestion_id());
            pst.executeUpdate();
            pst.close();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}
