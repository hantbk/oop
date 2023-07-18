package com.hust.quiz.Controllers;

import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import com.hust.quiz.Models.Quiz;
import com.hust.quiz.Services.ChoiceService;
import com.hust.quiz.Services.CountdownTimer;
import com.hust.quiz.Services.QuizService;
import com.hust.quiz.Views.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import javafx.scene.control.TextInputDialog;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;


public class StartQuizController implements Initializable {

    private static int sec;
    private static int quizTimeLimit;
    private static String quizTimeFormat;
    @FXML
    private AnchorPane quiz_pane, anchorPDF;
    @FXML
    private Label timerLabel;
    @FXML
    private ImageView btn_menu_return;
    @FXML
    private Label label_quiz_name_1, label_quiz_name_2;
    @FXML
    private VBox vbox_question;
    @FXML
    private GridPane grid_num_question;
    @FXML
    private ScrollPane scrollPane_quizView;
    @FXML
    private AnchorPane attempt_pane;
    @FXML
    private Button btn_cancel_finish, btn_submit_quiz, btn_finish_attempt, btnPDF;
    private final List<QuestionInStartController> listController = new ArrayList<>();
    private String timeStartQuiz;
    private String timeCompleteQuiz;
    private LocalTime start;
    private LocalTime end;
    private Quiz quiz;


    public static void setQuizTime(int quizTimeLimit, String quizTimeFormat) {
        StartQuizController.quizTimeLimit = quizTimeLimit;
        StartQuizController.quizTimeFormat = quizTimeFormat;
    }

    public void runTimer() {
        if (quizTimeFormat == null) {
            timerLabel.setText("No time limit");
        } else {
            if (Objects.equals(quizTimeFormat, "hours")) {
                sec = quizTimeLimit * 3600;
            } else if (Objects.equals(quizTimeFormat, "minutes")) {
                sec = quizTimeLimit * 60;
            } else if (Objects.equals(quizTimeFormat, "seconds")) {
                sec = quizTimeLimit;
            }
            CountdownTimer countdownTimer = new CountdownTimer(sec, timerLabel);
            countdownTimer.start();
            LocalDate now = LocalDate.now();
            this.start = LocalTime.now();
            DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE");
            DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
            this.timeStartQuiz = now.format(day) + ", " + now.format(date)+ ", " + this.start.format(time);
        }
    }

    public void endQuiz() {
        //quiz_pane.setDisable(true);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_menu_return.setOnMouseClicked(event -> {
            this.reset();
            ViewFactory.getInstance().updateQuizHome();
            ViewFactory.getInstance().routes(ViewFactory.SCENES.HOME);
        });
        btn_finish_attempt.setOnMouseClicked(event -> {
            quiz_pane.setDisable(true);
            quiz_pane.opacityProperty().setValue(0.5);
            attempt_pane.setVisible(true);
            attempt_pane.setDisable(false);
        });
        btn_cancel_finish.setOnMouseClicked(event -> {
            quiz_pane.setDisable(false);
            quiz_pane.opacityProperty().setValue(1);
            attempt_pane.setVisible(false);
            attempt_pane.setDisable(true);
        });
        btn_submit_quiz.setOnMouseClicked(event -> {
            System.out.println("Grade: " + this.getGradeQuiz());
            LocalDate now = LocalDate.now();
            this.end = LocalTime.now();
            DateTimeFormatter day = DateTimeFormatter.ofPattern("EEEE");
            DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm");
            this.timeCompleteQuiz = now.format(day) + ", " + now.format(date)+ ", " + this.end.format(time);
            Duration timeTaken = Duration.between(start, end);

            ViewFactory.getInstance().reviewQuiz(this.quiz, timeStartQuiz, timeCompleteQuiz, timeTaken, this.listController);
            ViewFactory.getInstance().routes(ViewFactory.SCENES.END_QUIZ);
            this.reset();
        });
        btnPDF.setOnMouseClicked(event -> {
                quiz_pane.setDisable(false);
                quiz_pane.opacityProperty().setValue(1);
                attempt_pane.setVisible(false);
                attempt_pane.setDisable(true);
                String password = null;
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("SET PASSWORD FOR THE PDF FILE");
                dialog.setHeaderText(null);
                dialog.setContentText("PASSWORD:");
                dialog.showAndWait().ifPresent(input->{
                            exportQuiztoPDF(input);
                        });

                //exportQuiztoPDF();

                quiz_pane.setDisable(true);
                quiz_pane.opacityProperty().setValue(0.5);
                attempt_pane.setVisible(true);
                attempt_pane.setDisable(false);
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
                    mapChoice.put(0, "A. "); mapChoice.put(1, "B. "); mapChoice.put(2, "C. "); mapChoice.put(3, "D. ");
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

    //update cac cau hoi trong quiz vao vBox
    public void updateQuestion(Quiz quiz) {
        this.quiz = quiz;
        int quiz_id = quiz.getQuiz_id();
        List<Question> listQuestion = QuizService.getQuestionQuiz(quiz_id);
        this.label_quiz_name_1.setText(quiz.getQuiz_name());
        this.label_quiz_name_2.setText(quiz.getQuiz_name());
        int i = 1;
        FXMLLoader[] listFXMLQuestionQuiz = new FXMLLoader[listQuestion.size() + 1];
        for (Question question : listQuestion) {
            listFXMLQuestionQuiz[i] = new FXMLLoader(getClass().getResource("/FXML/QuestionInStart.fxml"));
            try {
                Parent root = listFXMLQuestionQuiz[i].load();
                QuestionInStartController controller = listFXMLQuestionQuiz[i].getController();
                listController.add(controller);
                controller.setInforQuestion(question, i);
                vbox_question.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.print("At " + this.getClass());
            }
            i++;
        }
        List<Button> listButton = new ArrayList<>();
        for (int j = 0; j < listQuestion.size(); j++) {
            final int question_index = j + 1;
            Button button = new Button(String.valueOf(question_index));
            button.setPrefSize(40, 30);
            button.setOnAction(event -> {
                //vị trí của câu hỏi i trong VBox
                double place = vbox_question.getChildren().get(Integer.parseInt(button.getText()) - 1).getLayoutY() - (Integer.parseInt(button.getText())) * 10;
                //nhảy đến vị trí câu hỏi i
                scrollPane_quizView.setVvalue(place / vbox_question.getHeight());
                //System.out.println(place/vbox_question.getHeight());
            });
            grid_num_question.add(button, j % 5, j / 5);
        }
    }

    //reset all
    public void reset() {
        if (!listController.isEmpty())
            listController.clear();
        this.vbox_question.getChildren().clear();
        this.grid_num_question.getChildren().clear();
        quiz_pane.setDisable(false);
    }

    public double getGradeQuiz(){
        double grade = 0;
        System.out.println("So cau hoi: " + listController.size());

        for(QuestionInStartController questionInStartController : listController) {
            grade += questionInStartController.getGrade();
        }
        return grade;
    }
}
