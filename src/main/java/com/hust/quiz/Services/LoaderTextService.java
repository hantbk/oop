package com.hust.quiz.Services;


import com.hust.quiz.Services.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoaderTextService{

    public static void importfile(String filename) {
        //String filename = "questions.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder questionText = new StringBuilder();
            StringBuilder optionA = new StringBuilder();
            StringBuilder optionB = new StringBuilder();
            StringBuilder optionC = new StringBuilder();
            StringBuilder optionD = new StringBuilder();
            StringBuilder optionE = new StringBuilder();
            StringBuilder correctOption = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    if (line.startsWith("ANSWER: ")) {
                        correctOption.append(line.substring(8));
                    } else if (questionText.length() == 0) {
                        questionText.append(line);
                    } else if (optionA.length() == 0) {
                        optionA.append(line);
                    } else if (optionB.length() == 0) {
                        optionB.append(line);
                    } else if (optionC.length() == 0) {
                        optionC.append(line);
                    } else if (optionD.length() == 0) {
                        optionD.append(line);
                    }else if (optionE.length() == 0) {
                        optionE.append(line);
                    }

                } else if (questionText.length() > 0) {
                    // Insert the question into the database
                    insertQuestion(questionText.toString(), optionA.toString(), optionB.toString(),
                            optionC.toString(), optionD.toString(), optionE.toString(), correctOption.toString());

                    // Reset the StringBuilder objects
                    questionText.setLength(0);
                    optionA.setLength(0);
                    optionB.setLength(0);
                    optionC.setLength(0);
                    optionD.setLength(0);
                    optionE.setLength(0);
                    correctOption.setLength(0);
                }
            }

            // Insert the last question if it is not followed by a blank line
            if (questionText.length() > 0) {
                insertQuestion(questionText.toString(), optionA.toString(), optionB.toString(),
                        optionC.toString(), optionD.toString(), optionE.toString(), correctOption.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void insertQuestion(String questionText, String optionA, String optionB, String optionC,
                                       String optionD,String optionE, String correctOption) throws SQLException {

        Connection conn =  Utils.getConnection();

        String query = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d,option_e, correct_option) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";


        PreparedStatement statement = conn.prepareStatement(query); {

            statement.setString(1, questionText);
            statement.setString(2, optionA);
            statement.setString(3, optionB);
            statement.setString(4, optionC);
            statement.setString(5, optionD);
            statement.setString(6, optionE);
            statement.setString(7, correctOption);

            statement.executeUpdate();

        }
    }
}

