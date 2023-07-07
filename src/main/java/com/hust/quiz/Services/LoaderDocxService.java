package com.hust.quiz.Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

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
        PreparedStatement categoryStatement = null;
        PreparedStatement questionStatement = null;
        PreparedStatement choiceStatement = null;
        int questionCount = LoaderDocxService.getNumberOfQuestions();
        Random random = new Random();

        try {
            conn = Utils.getConnection();
            conn.setAutoCommit(false);
            int categoryId = 0;

            // Check if the category table is empty
            String checkCategoryQuery = "SELECT COUNT(*) AS category_count FROM category";
            categoryStatement = conn.prepareStatement(checkCategoryQuery);
            ResultSet categoryResultSet = categoryStatement.executeQuery();
            categoryResultSet.next();
            int categoryCount = categoryResultSet.getInt("category_count");

            if (categoryCount == 0) {
                // If the category table is empty, create a new category with the current date as the category_name
                String currentDate = getCurrentDate(); // Helper method to get the current date in the desired format
                String createCategoryQuery = "INSERT INTO category (category_name, question_count) VALUES (?, ?)";
                categoryStatement = conn.prepareStatement(createCategoryQuery, Statement.RETURN_GENERATED_KEYS);
                categoryStatement.setString(1, currentDate);
                categoryStatement.setInt(2, questionCount);
                categoryStatement.executeUpdate();

                try (ResultSet generatedKeys = categoryStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        categoryId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating category failed, no ID obtained.");
                    }
                }
            } else {
                // If the category table is not empty, randomly select a category ID
                String selectCategoryIdQuery = "SELECT category_id FROM category";
                categoryStatement = conn.prepareStatement(selectCategoryIdQuery);
                ResultSet categoryIdResultSet = categoryStatement.executeQuery();
                List<Integer> categoryIds = new ArrayList<>();
                while (categoryIdResultSet.next()) {
                    categoryIds.add(categoryIdResultSet.getInt("category_id"));
                }
                int randomIndex = random.nextInt(categoryIds.size());
                categoryId = categoryIds.get(randomIndex);

                // Update the question count for the selected category
                String updateQuestionCountQuery = "UPDATE category SET question_count = ? WHERE category_id = ?";
                categoryStatement = conn.prepareStatement(updateQuestionCountQuery);
                categoryStatement.setInt(1, questionCount);
                categoryStatement.setInt(2, categoryId);
                categoryStatement.executeUpdate();
            }

            // Get the category_name for the current category_id
            String getCategoryNameQuery = "SELECT category_name FROM category WHERE category_id = ?";
            PreparedStatement categoryNameStatement = conn.prepareStatement(getCategoryNameQuery);
            categoryNameStatement.setInt(1, categoryId);
            ResultSet categoryNameResultSet = categoryNameStatement.executeQuery();
            categoryNameResultSet.next();
            String categoryName = categoryNameResultSet.getString("category_name");

            // Insert questions and choices
            String questionQuery = "INSERT INTO question (question_name, question_text, category_id) VALUES (?, ?, ?)";
            questionStatement = conn.prepareStatement(questionQuery, Statement.RETURN_GENERATED_KEYS);
            String choiceQuery = "INSERT INTO choice (choice_content, choice_is_correct, question_id, image_data) VALUES (?, ?, ?, ?)";
            choiceStatement = conn.prepareStatement(choiceQuery);

            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);

                // Generate the question_name by combining category_name and question index
                String questionName = categoryName + " " + (i + 1);

                // Insert question into the question table
                questionStatement.setString(1, questionName);
                questionStatement.setString(2, question.getQuestionText());
                questionStatement.setInt(3, categoryId);

                int affectedRows = questionStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Creating question failed, no rows affected.");
                }

                try (ResultSet generatedKeys = questionStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int questionId = generatedKeys.getInt(1);

                        // Insert choices into the choice table
                        List<String> options = question.getOptions();
                        for (int j = 0; j < options.size(); j++) {
                            String option = options.get(j);
                            String Option = String.valueOf(option.charAt(0));

                            choiceStatement.setString(1, option);
                            choiceStatement.setBoolean(2, Option.equals(question.getCorrectOption()));
                            choiceStatement.setInt(3, questionId);
                            choiceStatement.setBytes(4, question.getImageData());

                            choiceStatement.addBatch();
                        }
                    } else {
                        throw new SQLException("Creating question failed, no ID obtained.");
                    }
                }
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

    private static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return currentDate.format(formatter);
    }
}
