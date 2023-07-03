package com.hust.quiz.Services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoaderTextService {

    private static StringBuilder questionText = new StringBuilder();
    private static List<String> options = new ArrayList<>();
    private static StringBuilder correctOption = new StringBuilder();
    private static List<Question> questionList = new ArrayList<>();

    public static void importFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("ANSWER: ")) {
                        correctOption.append(line.substring(8));
                    } else if (questionText.length() == 0) {
                        questionText.append(line);
                    } else if (options.size() < 5) {
                        options.add(line);
                    }
                } else {
                    // Add the question to the list
                    if (questionText.length() > 0) {
                        Question question = new Question(questionText.toString(), new ArrayList<>(options),
                                correctOption.toString());
                        questionList.add(question);
                    }

                    // Reset the question-related variables
                    resetQuestionVariables();
                }
            }

            // Insert the last question
            if (questionText.length() > 0) {
                Question question = new Question(questionText.toString(), new ArrayList<>(options),
                        correctOption.toString());
                questionList.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the questions to the database
        saveQuestionsToDatabase();
    }

    private static void resetQuestionVariables() {
        questionText.setLength(0);
        options.clear();
        correctOption.setLength(0);
    }

    private static void saveQuestionsToDatabase() {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = Utils.getConnection();
            String query = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, option_e, correct_option) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            statement = conn.prepareStatement(query);

            for (Question question : questionList) {
                statement.setString(1, question.getQuestionText());
                List<String> options = question.getOptions();
                for (int i = 0; i < 5; i++) {
                    if (i < options.size()) {
                        statement.setString(i + 2, options.get(i));
                    } else {
                        statement.setString(i + 2, null);
                    }
                }
                statement.setString(7, question.getCorrectOption());

                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static class Question {
        private String questionText;
        private List<String> options;
        private String correctOption;

        public Question(String questionText, List<String> options, String correctOption) {
            this.questionText = questionText;
            this.options = options;
            this.correctOption = correctOption;
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<String> getOptions() {
            return options;
        }

        public String getCorrectOption() {
            return correctOption;
        }
    }
}
