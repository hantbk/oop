package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.ChoiceService;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class QuizViewController implements Initializable {

    @FXML
    private Label label_quiz_name_IT, label_quiz_name_view, label_quiz_description;
    @FXML
    private Label timeFormatLabel, timeLimitLabel; // second pane
    @FXML
    private Label timeFormatLabel1, timeLimitLabel1; // first pane
    @FXML
    private ImageView btn_edit_quiz; // click jump to EditQuizView - NOT done
    @FXML
    private Button btn_preview_quiz;
    @FXML
    private ImageView btn_menu_return; // return to homeView
    @FXML
    private AnchorPane second_pane, blur_pane;
    @FXML
    private ImageView btn_close_confirm;
    @FXML
    private Button btn_start_attempt, btn_cancel_attempt, btn_export;
    private Quiz quiz;

    public void displayInfo(String quizName) {
        quiz = QuizService.getQuiz(quizName);
        if (quiz == null) {
            System.out.println("No quiz found with name: " + quizName);
            return;
        }
        label_quiz_name_IT.setText(quiz.getQuiz_name());
        label_quiz_name_view.setText(quiz.getQuiz_name());
        label_quiz_description.setText(quiz.getQuiz_description());

        if (quiz.getTimeLimit() > 0 && quiz.getTimeFormat() != null) {
            timeLimitLabel.setText(quiz.getTimeLimit() + " ");
            timeFormatLabel.setText(quiz.getTimeFormat());
            timeLimitLabel1.setText(quiz.getTimeLimit() + " ");
            timeFormatLabel1.setText(quiz.getTimeFormat());
        } else {
            timeLimitLabel.setText("No time limit");
            timeFormatLabel.setText("");
            timeLimitLabel1.setText("No");
            timeFormatLabel1.setText("");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        second_pane.setVisible(false);
        blur_pane.setVisible(false);

        btn_menu_return.setOnMouseClicked(event -> ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME));

        btn_edit_quiz.setOnMouseClicked(event -> {
            ViewFactory.getInstance().updateEditQuizView(quiz);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.EDIT_QUIZ);
        });

        // configure confirm attempt window
        btn_preview_quiz.setOnAction(event -> {
            blur_pane.setVisible(true);
            second_pane.setVisible(true);
        });

        btn_close_confirm.setOnMouseClicked(event -> {
            blur_pane.setVisible(false);
            second_pane.setVisible(false);
        });

        btn_cancel_attempt.setOnAction(event -> {
            blur_pane.setVisible(false);
            second_pane.setVisible(false);
        });

        // start quiz
        btn_start_attempt.setOnAction(event -> {
            blur_pane.setVisible(false);
            second_pane.setVisible(false);
            ViewFactory.getInstance().updateStartQuiz(quiz);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.START_QUIZ);
        });
        btn_export.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("SET PASSWORD FOR THE PDF FILE");
            dialog.setHeaderText(null);
            dialog.setContentText("PASSWORD:");
            dialog.showAndWait().ifPresent(input -> {
                exportQuiztoPDF(input);
            });

        });
    }

    private void exportQuiztoPDF(String password) {
        List<Question> listQuestion = QuizService.getQuestionQuiz(this.quiz.getQuiz_id());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                StandardProtectionPolicy policy = new StandardProtectionPolicy(password, password, new AccessPermission());
                policy.setEncryptionKeyLength(128); // Set the key length (can be 40, 128, or 256)
                policy.setPermissions(new AccessPermission());
                PDPage page = new PDPage();
                document.addPage(page);

                // Load the Arial font from the resources folder
                PDType0Font font = PDType0Font.load(document, StartQuizController.class.getClassLoader().getResourceAsStream("arial.ttf"));

                float margin = 50; // Margin on the left and right sides
                float yPosition = page.getMediaBox().getHeight() - 50; // Set initial y position
                float fontSize = 12;
                int maxLineWidth = (int) (page.getMediaBox().getWidth() - 2 * margin);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(font, fontSize);
                StringBuilder currentLine = new StringBuilder();

                for (Question question : listQuestion) {
                    // Print the question text
                    String questionText = question.getQuestion_text();
                    String[] words = questionText.split(" ");

                    for (String word : words) {
                        String potentialLine = currentLine + word + " ";
                        float lineWidth = fontSize * font.getStringWidth(potentialLine) / 1000;

                        if (lineWidth > maxLineWidth) {
                            contentStream.beginText();
                            contentStream.newLineAtOffset(margin, yPosition);
                            contentStream.showText(currentLine.toString());
                            contentStream.endText();
                            yPosition -= fontSize + 5; // Move to the next line
                            currentLine = new StringBuilder(word + " ");

                            // Check if there is enough space for another line
                            if (yPosition < margin) {
                                // Create a new page and reset the y position
                                contentStream.close();
                                page = new PDPage();
                                document.addPage(page);
                                contentStream = new PDPageContentStream(document, page);
                                contentStream.setFont(font, fontSize);
                                yPosition = page.getMediaBox().getHeight() - margin;
                            }
                        } else {
                            currentLine.append(word).append(" ");
                        }
                    }

                    // Write the remaining line for the question
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(currentLine.toString());
                    contentStream.endText();
                    yPosition -= fontSize + 5; // Move to the next line
                    currentLine = new StringBuilder(); // Reset currentLine for the next question

                    // Print the question image
                    String questionImageUrl = "file:///" + question.getQuestionImage();
                    if (question.getQuestionImage() != null && !question.getQuestionImage().isEmpty()) {
                        try {
                            URL url = new URL(questionImageUrl);
                            try (InputStream inputStream = url.openStream()) {
                                BufferedImage bufferedImage = ImageIO.read(inputStream);
                                if (bufferedImage != null) {
                                    float imageWidth = 200; // Adjust the image width
                                    float imageHeight = 100; // Adjust the image height
                                    if (yPosition + imageHeight < margin) {
                                        // Create a new page and reset the y position
                                        contentStream.close();
                                        page = new PDPage();
                                        document.addPage(page);
                                        contentStream = new PDPageContentStream(document, page);
                                        contentStream.setFont(font, fontSize);
                                        yPosition = page.getMediaBox().getHeight() - margin;
                                    }
                                    PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
                                    contentStream.drawImage(pdImage, margin, yPosition - imageHeight, imageWidth, imageHeight);
                                    yPosition -= imageHeight + 20; // Move to the next line after the image
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (yPosition < margin) {
                        // Create a new page and reset the y position
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(font, fontSize);
                        yPosition = page.getMediaBox().getHeight() - margin;
                    }

                    List<Choice> listChoice = ChoiceService.getChoice(question.getQuestion_id());
                    Map<Integer, String> mapChoice = new HashMap<>();
                    mapChoice.put(0, "A. ");
                    mapChoice.put(1, "B. ");
                    mapChoice.put(2, "C. ");
                    mapChoice.put(3, "D. ");
                    int index = 0;
                    for (Choice choice : listChoice) {
                        // Print the choice text
                        String choiceContent = mapChoice.get(index++) + choice.getContent();
                        String[] words1 = choiceContent.split(" ");
                        for (String word1 : words1) {
                            String potentialLine = currentLine + word1 + " ";
                            float lineWidth = fontSize * font.getStringWidth(potentialLine) / 1000;

                            if (lineWidth > maxLineWidth) {
                                contentStream.beginText();
                                contentStream.newLineAtOffset(80, yPosition);
                                contentStream.showText(currentLine.toString());
                                contentStream.endText();
                                yPosition -= fontSize + 5; // Move to the next line
                                currentLine = new StringBuilder(word1 + " ");

                                // Check if there is enough space for another line
                                if (yPosition < margin) {
                                    // Create a new page and reset the y position
                                    contentStream.close();
                                    page = new PDPage();
                                    document.addPage(page);
                                    contentStream = new PDPageContentStream(document, page);
                                    contentStream.setFont(font, fontSize);
                                    yPosition = page.getMediaBox().getHeight() - margin;
                                }
                            } else {
                                currentLine.append(word1).append(" ");
                            }
                        }

                        // Write the remaining line for the choice
                        contentStream.beginText();
                        contentStream.newLineAtOffset(80, yPosition);
                        contentStream.showText(currentLine.toString());
                        contentStream.endText();
                        yPosition -= fontSize + 5; // Move to the next line
                        currentLine = new StringBuilder(); // Reset currentLine for the next choice

                        // Print the choice image
                        String choiceImageUrl = "file:///" + choice.getChoiceImage();
                        if (choice.getChoiceImage() != null && !choice.getChoiceImage().isEmpty()) {
                            try {
                                URL url = new URL(choiceImageUrl);
                                try (InputStream inputStream = url.openStream()) {
                                    BufferedImage bufferedImage = ImageIO.read(inputStream);
                                    if (bufferedImage != null) {
                                        float imageWidth = 200; // Adjust the image width
                                        float imageHeight = 100; // Adjust the image height
                                        if (yPosition + imageHeight < margin) {
                                            // Create a new page and reset the y position
                                            contentStream.close();
                                            page = new PDPage();
                                            document.addPage(page);
                                            contentStream = new PDPageContentStream(document, page);
                                            contentStream.setFont(font, fontSize);
                                            yPosition = page.getMediaBox().getHeight() - margin;
                                        }
                                        PDImageXObject pdImage = LosslessFactory.createFromImage(document, bufferedImage);
                                        contentStream.drawImage(pdImage, 80, yPosition - imageHeight, imageWidth, imageHeight);
                                        yPosition -= imageHeight + 15; // Move to the next line after the image
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                contentStream.close();
                document.protect(policy);
                document.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
