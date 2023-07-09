package com.hust.quiz.Models;

import com.hust.quiz.Services.CategoryService;
import com.hust.quiz.Services.ChoiceService;
import com.hust.quiz.Services.QuestionService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AikenFormatChecker {

    public static String checkAikenFormat(String filePath) {
        int quest_id = QuestionService.getLastQuestionId();

        List<Question> questions = new ArrayList<>();
        List<Choice> choices = new ArrayList<>();
        String output = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;
            int questionCount = 0;

            Pattern answerPattern = Pattern.compile("^[A-Z]\\. .+");
            Set<String> validAnswers = new HashSet<>();

            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                Matcher answerMatcher = answerPattern.matcher(line);

                if (answerMatcher.matches()) {
                    Choice choice = new Choice(line.substring(3), 0, quest_id);
                    choices.add(choice);

                    validAnswers.add(line.substring(0, 1));
                } else if (line.startsWith("ANSWER: ")) {
                    String correctAnswer = line.substring(8).trim();

                    if (!validAnswers.contains(correctAnswer)) {
                        output = "Invalid correct answer at line " + lineCount;
                        return output;
                    }

                    if (validAnswers.size() < 2) {
                        output = "Not enough answers at line " + lineCount;
                        return output;
                    }

                    // index of correct answer in choices
                    // index = total number of choices - number of invalid answers in this question + index of correct answer
                    int index = choices.size() - validAnswers.size() + correctAnswer.charAt(0) - 'A';
                    choices.get(index).setChoiceGrade(100);
                } else {
                    quest_id++;
                    Question question = new Question(line);
                    questions.add(question);
                    questionCount++;

                    validAnswers.clear(); // Reset valid answers
                }
            }
            if (validAnswers.size() < 2) {
                output = "Not enough answers at line " + lineCount;
                return output;
            } else if (questionCount == 0) {
                output = "No question found";
                return output;
            } else {
                output = "Success: " + questionCount + " question(s) found";

                int category_id = CategoryService.addCategory(getTime());
                QuestionService.addQuestion(questions, category_id);
                ChoiceService.addChoice(choices);

                return output;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

    public static String checkAikenFormatDoc(String fileName) {
        String output = null;
        try (FileInputStream fis = new FileInputStream(fileName);
             XWPFDocument document = new XWPFDocument(fis)) {

            int lineCount = 0;// Đếm số dòng
            int questionCount = 0; //Đếm số câu hỏi
            boolean isValidFormat = true; //Kiểm tra định dạng hợp lệ
            Pattern answerPattern = Pattern.compile("^[A-Z]\\. .+");//Mẫu cho các dòng chứa đáp án
            Set<String> validAnswers = new HashSet<>();//Các đáp án hợp lệ
            boolean isQuestion = false; //Đánh dấu là đang đọc câu hỏi
            boolean hasCorrectAnswer = false;//Kiểm tra có đáp án đúng hay không
            boolean hasBlankLine = false;//Kiểm tra có gặp dòng trống trong câu hỏi hiện tại không, đảm bảo phải có ít nhất 1 dòng trống sau dòng ANSWER
            int answerLineCount = 0; // Đếm số dòng chứa đáp án

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                if (hasImage(paragraph)) {
                    continue; // Nếu có hình ảnh thì bỏ qua
                }

                lineCount++;
                String line = paragraph.getText().trim();//Lấy nội dung của dòng

                if (line.isEmpty()) {
                    if (isQuestion) {
                        if (!hasCorrectAnswer || !hasBlankLine || answerLineCount < 2) {
                            // Các điều kiện để câu hỏi hợp lệ
                            isValidFormat = false;
                            break;
                        }
                        isQuestion = false;
                        hasCorrectAnswer = false;
                        hasBlankLine = false;
                        answerLineCount = 0;
                    }
                    continue;
                }

                Matcher answerMatcher = answerPattern.matcher(line);

                if (!isQuestion) {
                    questionCount++;
                    isQuestion = true;
                    hasCorrectAnswer = false;
                    hasBlankLine = false;
                    answerLineCount = 0;
                } else if (answerMatcher.matches()) {
                    // Nếu dòng chứa đáp án đúng định dạng thì lấy chữ cái in hoa đầu tiên vào tập đáp án hợp lệ
                    validAnswers.add(line.substring(0, 1));
                    hasBlankLine = false;
                    answerLineCount++; // Increment answer line count
                } else if (line.startsWith("ANSWER: ")) {
                    // Dòng chua đáp án đúng của câu hỏi
                    String correctAnswer = line.substring(8).trim();

                    if (!validAnswers.contains(correctAnswer)) {
                        // Đáp án đúng không hợp lệ(không có trong các lựa chọn)
                        isValidFormat = false;
                        break;
                    }

                    hasCorrectAnswer = true;
                    hasBlankLine = false;
                } else {
                    // Invalid line within a question
                    isValidFormat = false;
                    break;
                }

                hasBlankLine = true; // Đánh dấu khi gặp 1 dòng trống
            }

            if (isValidFormat && questionCount > 0 && hasCorrectAnswer && answerLineCount >= 2) {
                output = "Success: " + questionCount + " question(s) found";
            } else {
                output = "Error at line " + lineCount;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    //Kiểm tra đoạn văn bản có hình ảnh hay không
    private static boolean hasImage(XWPFParagraph paragraph) {
        for (XWPFRun run : paragraph.getRuns()) {
            if (run.getEmbeddedPictures().size() > 0) {
                return true;
            }
        }
        return false;
    }

    private static String getTime() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        return day + "-" + month + "-" + year;
    }
}

