package com.hust.quiz.Services;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;


public class LoaderDocxService{

    private static StringBuilder questionText = new StringBuilder();
    private static StringBuilder optionA = new StringBuilder();
    private static StringBuilder optionB = new StringBuilder();
    private static StringBuilder optionC = new StringBuilder();
    private static StringBuilder optionD = new StringBuilder();
    private static StringBuilder optionE = new StringBuilder();
    private static StringBuilder correctOption = new StringBuilder();
    private static byte[] imageData = null;
    private static List<Question> questionList = new ArrayList<>();

    public static void importFile(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             XWPFDocument doc = new XWPFDocument(fis)) {

            List<XWPFParagraph> paragraphs = doc.getParagraphs();

            for (XWPFParagraph paragraph : paragraphs) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    List<XWPFPicture> pictures = run.getEmbeddedPictures();
                    for (XWPFPicture picture : pictures) {
                        XWPFPictureData pictureData = picture.getPictureData();
                        imageData = pictureData.getData();
                    }
                }

                String text = paragraph.getText().trim();
                if (!text.isEmpty()) {
                    if (text.startsWith("ANSWER: ")) {
                        correctOption.append(text.substring(8));
                    } else if (questionText.length() == 0) {
                        questionText.append(text);
                    } else if (optionA.length() == 0) {
                        optionA.append(text);
                    } else if (optionB.length() == 0) {
                        optionB.append(text);
                    } else if (optionC.length() == 0) {
                        optionC.append(text);
                    } else if (optionD.length() == 0) {
                        optionD.append(text);
                    } else if (optionE.length() == 0) {
                        optionE.append(text);
                    }
                } else if (isQuestionComplete()) {
                    // Add the question to the list
                    Question question = new Question(questionText.toString(), optionA.toString(), optionB.toString(),
                            optionC.toString(), optionD.toString(), optionE.toString(), correctOption.toString(), imageData);
                    questionList.add(question);

                    // Reset the question-related variables
                    resetQuestionVariables();
                }
            }

            // Insert the last question if it is not followed by a blank line and is complete
            if (isQuestionComplete()) {
                Question question = new Question(questionText.toString(), optionA.toString(), optionB.toString(),
                        optionC.toString(), optionD.toString(), optionE.toString(), correctOption.toString(), imageData);
                questionList.add(question);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save the questions to the database
        saveQuestionsToDatabase();
    }

    private static boolean isQuestionComplete() {
        return !questionText.toString().isEmpty() && !optionA.toString().isEmpty()
                && !optionB.toString().isEmpty();
    }

    private static void resetQuestionVariables() {
        questionText.setLength(0);
        optionA.setLength(0);
        optionB.setLength(0);
        optionC.setLength(0);
        optionD.setLength(0);
        optionE.setLength(0);
        correctOption.setLength(0);
        imageData = null;
    }

    private static void saveQuestionsToDatabase() {
        Connection conn = null;
        PreparedStatement statement = null;

        try {
            conn = Utils.getConnection();
            String query = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, option_e, correct_option, image_data) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            statement = conn.prepareStatement(query);

            for (Question question : questionList) {
                statement.setString(1, question.getQuestionText());
                statement.setString(2, question.getOptionA());
                statement.setString(3, question.getOptionB());
                statement.setString(4, question.getOptionC());
                statement.setString(5, question.getOptionD());
                statement.setString(6, question.getOptionE());
                statement.setString(7, question.getCorrectOption());
                statement.setBytes(8, question.getImageData());

                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static class Question {
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private String optionE;
        private String correctOption;
        private byte[] imageData;

        public Question(String questionText, String optionA, String optionB, String optionC,
                        String optionD, String optionE, String correctOption, byte[] imageData) {
            this.questionText = questionText;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.optionE = optionE;
            this.correctOption = correctOption;
            this.imageData = imageData;
        }

        public String getQuestionText() {
            return questionText;
        }

        public String getOptionA() {
            return optionA;
        }

        public String getOptionB() {
            return optionB;
        }

        public String getOptionC() {
            return optionC;
        }

        public String getOptionD() {
            return optionD;
        }

        public String getOptionE() {
            return optionE;
        }

        public String getCorrectOption() {
            return correctOption;
        }

        public byte[] getImageData() {
            return imageData;
        }
    }
}


