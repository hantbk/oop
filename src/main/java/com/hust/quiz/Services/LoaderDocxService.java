package com.hust.quiz.Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

public class LoaderDocxService {

    private static StringBuilder questionText = new StringBuilder();
    private static List<String> options = new ArrayList<>();
    private static StringBuilder correctOption = new StringBuilder();
    private static byte[] imageData = null;
    private static List<Question> questionList = new ArrayList<>();

    public static void importFile(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             XWPFDocument doc = new XWPFDocument(fis)) {

            List<XWPFParagraph> paragraphs = doc.getParagraphs();

            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText().trim();

                if (!text.isEmpty()) {
                    if (text.startsWith("ANSWER: ")) {
                        correctOption.append(text.substring(8));
                    } else {
                        if (questionText.length() > 0) {
                            options.add(text);
                        } else {
                            questionText.append(text);
                        }
                    }
                } else {
                    if (isQuestionComplete()) {
                        // Add the question to the list
                        Question question = new Question(questionText.toString(), new ArrayList<>(options),
                                correctOption.toString(), imageData);
                        questionList.add(question);

                        // Reset the question-related variables
                        resetQuestionVariables();
                    }
                }
            }

            // Insert the last question if it is complete
            if (isQuestionComplete()) {
                Question question = new Question(questionText.toString(), new ArrayList<>(options),
                        correctOption.toString(), imageData);
                questionList.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the questions to the database
        saveQuestionsToDatabase();
    }

    private static boolean isQuestionComplete() {
        return questionText.length() > 0 && !options.isEmpty();
    }

    private static void resetQuestionVariables() {
        questionText.setLength(0);
        options.clear();
        correctOption.setLength(0);
        imageData = null;
    }

    private static void saveQuestionsToDatabase() {
        Connection conn = null;
        PreparedStatement questionStatement = null;
        PreparedStatement choiceStatement = null;
        PreparedStatement categoryStatement = null;
        PreparedStatement statement = null;
        int questionCount = LoaderDocxService.getNumberOfQuestions();
        Random random = new Random();
        int category_id = random.nextInt(1,19);
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

                String choiceQuery = "INSERT INTO choice (choice_content, choice_is_correct, question_id, image_data) " +
                        "VALUES (?, ?, ?, ?)";

                choiceStatement = conn.prepareStatement(choiceQuery);

                for (Question question : questionList) {
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
                                choiceStatement.setBytes(4, question.getImageData());

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
        private byte[] imageData;

        public Question(String questionText, List<String> options, String correctOption, byte[] imageData) {
            this.questionText = questionText;
            this.options = options;
            this.correctOption = correctOption;
            this.imageData = imageData;
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

        public byte[] getImageData() {
            return imageData;
        }
    }

    public static int getNumberOfQuestions() {
        return questionList.size();
    }
}
