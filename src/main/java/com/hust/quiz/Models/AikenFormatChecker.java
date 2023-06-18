package com.hust.quiz.Models;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hust.quiz.Services.Utils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import java.time.LocalDate;
import com.hust.quiz.Services.QuestionService;


public class AikenFormatChecker {
    public static void main(String[] args) {
        String filePath = "D:\\questions.txt"; // Thay bằng đường dẫn phù hợp
        String fileName = "D:\\questions.doc"; // Thay bằng đường dẫn phù hợp

        checkAikenFormat(filePath);
        checkAikenFormat(fileName);
    }

    public static String checkAikenFormat(String filePath) {
        int quest_id = QuestionService.getLastQuestionId();
        //System.out.println("Last question id: " + quest_id);
        List<Question> questions = new ArrayList<>();
        List<Choice> choices = new ArrayList<>();
        String output = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineCount = 0;
            int questionCount = 0;
            boolean isValidFormat = true;
            Pattern answerPattern = Pattern.compile("^[A-Z]\\. .+");
            Set<String> validAnswers = new HashSet<>();
            boolean isQuestion = true;
            boolean hasCorrectAnswer = false;
            boolean hasBlankLine = false;
            int answerLineCount = 0;

            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();

                if (line.isEmpty()) {
                    if (!isQuestion) {
                        if (!hasCorrectAnswer || !hasBlankLine || answerLineCount < 2) {
                            // Không đúng AikenFormat
                            isValidFormat = false;
                            break;
                        }
                        isQuestion = true;
                        hasCorrectAnswer = false;
                        hasBlankLine = false;
                        answerLineCount = 0; // Reset answer line count
                    }
                    continue;
                }

                Matcher answerMatcher = answerPattern.matcher(line);
                //System.out.println(answerMatcher.matches());

                if (isQuestion) {
                    //System.out.println("Line " + lineCount + ": " + line);
                    // First line of a new question
                    quest_id++;
                    Question question = new Question(line);
                    questions.add(question);
                    questionCount++;
                    isQuestion = false;
                    hasCorrectAnswer = false;
                    hasBlankLine = false;
                    answerLineCount = 0; // Reset answer line count
                } else if (answerMatcher.matches()) {
                    //System.out.println(line.substring(3));
                        Choice choice = new Choice(line.substring(3), false, 0, quest_id);
                        //System.out.println(line.substring(3));
                        choices.add(choice);
                        System.out.println(choice.getQuestion_id());
                        String answer = answerMatcher.group();
                        validAnswers.add(answer.substring(0, 1));
                        answerLineCount++;
                    // print choices

                    // Answer line within a question
                    validAnswers.add(line.substring(0, 1));
                    hasBlankLine = false;
                    answerLineCount++; // Increment answer line count
                } else if (line.startsWith("ANSWER: ")) {
                    // Correct answer line within a question
                    String correctAnswer = line.substring(8).trim();


                    if (!validAnswers.contains(correctAnswer)) {
                        // Invalid correct answer
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

                hasBlankLine = true; // Set the flag when a non-empty line is encountered
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

    public static String getTime(){
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        return day + "-" + month + "-" + year;
    }
}

