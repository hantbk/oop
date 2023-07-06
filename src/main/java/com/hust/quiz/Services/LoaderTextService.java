package com.hust.quiz.Services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        PreparedStatement questionStatement = null;
        PreparedStatement choiceStatement = null;
        PreparedStatement categoryStatement = null;
        PreparedStatement statement = null;
        int questionCount = LoaderDocxService.getNumberOfQuestions();
        Random random = new Random();
        int category_id = random.nextInt(1,19); // select a random category_id
        try {
            conn = Utils.getConnection();
            conn.setAutoCommit(false);

            // Check if the category_id exists in the category table
            String checkQuery = "SELECT category_id FROM category WHERE category_id = ?";
            categoryStatement = conn.prepareStatement(checkQuery);
            categoryStatement.setInt(1, category_id);
            ResultSet resultSet = categoryStatement.executeQuery();


            if (resultSet.next()) {
                //Update number of question in category
                String categoryQuery = "UPDATE category SET question_count = ? WHERE category_id = ?";
                statement = conn.prepareStatement(categoryQuery);
                statement.setInt(1, questionCount);
                statement.setInt(2, category_id);
                statement.executeUpdate();

                // The category_id exists, so update the question_count using triggers
                String questionQuery = "INSERT INTO question (question_name, question_text, category_id) " +
                        "VALUES (?, ?, ?)";

                questionStatement = conn.prepareStatement(questionQuery, Statement.RETURN_GENERATED_KEYS);

                String choiceQuery = "INSERT INTO choice (choice_content, choice_is_correct, question_id) " +
                        "VALUES (?, ?, ?)";

                choiceStatement = conn.prepareStatement(choiceQuery);

                for (LoaderTextService.Question question : questionList) {
                    // Insert question into the question table
                    questionStatement.setString(1, question.getQuestionText());
                    questionStatement.setString(2, question.getQuestionText());
                    questionStatement.setInt(3, category_id);

                    int affectedRows = questionStatement.executeUpdate();

                    if (affectedRows == 0) {
                        throw new SQLException("Creating question failed, no rows affected.");
                    }

                    try (ResultSet generatedKeys = questionStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int questionId = generatedKeys.getInt(1);

                            // Insert choices into the choice table
                            List<String> options = question.getOptions();
                            for (int i = 0; i < options.size(); i++) {
                                String option = options.get(i);
                                String Option = String.valueOf(option.charAt(0));

                                choiceStatement.setString(1, option);
                                choiceStatement.setBoolean(2, Option.equals(question.getCorrectOption()) );
                                choiceStatement.setInt(3, questionId);

                                choiceStatement.addBatch();
                            }
                        } else {
                            throw new SQLException("Creating question failed, no ID obtained.");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid category_id: " + category_id);
            }

            // Execute the choice batch insert
            choiceStatement.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            try {
                if (choiceStatement != null) {
                    choiceStatement.close();
                }
                if (questionStatement != null) {
                    questionStatement.close();
                }
                if (categoryStatement != null) {
                    categoryStatement.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                // Handle the exception
            }
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

    public static int getNumberOfQuestions() {
        return questionList.size();
    }
}
