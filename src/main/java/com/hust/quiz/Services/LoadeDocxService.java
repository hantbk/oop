package com.hust.quiz.Services;

import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadeDocxService {

    public static String importFile(String path) {
        int quest_id = QuestionService.getLastQuestionId();

        List<Question> questions = new ArrayList<>();
        List<Choice> choices = new ArrayList<>();

        String line;
        int lineCount = 0;
        int questionCount = 0;

        Pattern answerPattern = Pattern.compile("^[A-Z]\\. .+");
        Set<String> validAnswers = new HashSet<>();

        try (FileInputStream fis = new FileInputStream(path)) {
            XWPFDocument docx = new XWPFDocument(fis);
            for (XWPFParagraph paragraph : docx.getParagraphs()) {
                lineCount++;
                // check if paragraph is image
                String pathImage = checkAndSaveImage(paragraph, quest_id);
                if (pathImage != null && questions.size() == questionCount) {
                    System.out.println(pathImage + " " + quest_id);
                    questions.get(questionCount - 1).setQuestion_image(pathImage);
                } else {
                    line = paragraph.getText().trim();

                    if (line.isEmpty()) {
                        continue;
                    }

                    Matcher answerMatcher = answerPattern.matcher(line);
                    if (answerMatcher.matches()) { // if line is answer
                        Choice choice = new Choice(line.substring(3), 0, quest_id);
                        choices.add(choice);

                        validAnswers.add(line.substring(0, 1));
                    } else if (line.startsWith("ANSWER: ")) { // if line is correct answer
                        String[] answers = line.substring(8).split(",");
                        int index;
                        double grade = 100.0 / answers.length;

                        for (String answer : answers) {
                            answer = answer.trim();
                            if (!validAnswers.contains(answer) || answer.length() > 1) {
                                return "Invalid correct answer at line " + lineCount;
                            }
                            // index of correct answer in choices
                            // index = total number of choices - number of invalid answers in this question + index of correct answer
                            index = choices.size() - validAnswers.size() + answer.charAt(0) - 'A';
                            choices.get(index).setChoiceGrade(grade);
                        }

                        // check if there is at least 2 answers, and check if you have duplicate line start with "ANSWER: "
                        if (validAnswers.size() < 2) {
                            return "Not enough answers at line " + lineCount;
                        }

                        validAnswers.clear(); // Reset valid answers
                    } else { // if line is question
                        quest_id++;
                        Question question = new Question(quest_id, line, null);
                        questions.add(question);
                        questionCount++;
                    }
                }
            }
            if (questionCount == 0) {
                return "No question found";
            } else {
                int category_id = CategoryService.addCategory(getTime());
                QuestionService.addQuestion(questions, category_id);
                ChoiceService.addChoice(choices);

                return "Success: " + questionCount + " question(s) found";
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static String checkAndSaveImage(XWPFParagraph paragraph, int imageIndex) throws IOException {
        for (XWPFRun run : paragraph.getRuns()) {
            for (XWPFPicture picture : run.getEmbeddedPictures()) {
                XWPFPictureData pictureData = picture.getPictureData();
                String fileName = "image_" + imageIndex + ".png";
                File imageFile = new File(ImageService.PATH + fileName);
                FileOutputStream fos = new FileOutputStream(imageFile);
                fos.write(pictureData.getData());
                fos.close();
                return ImageService.PATH + fileName;
            }
        }
        return null;
    }

    private static String getTime() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        return day + "-" + month + "-" + year;
    }
}
