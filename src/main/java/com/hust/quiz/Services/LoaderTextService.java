package com.hust.quiz.Services;

import com.hust.quiz.Models.Choice;
import com.hust.quiz.Models.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoaderTextService {

    public static String importFile(String filePath) {
        int quest_id = QuestionService.getLastQuestionId();

        List<Question> questions = new ArrayList<>();
        List<Choice> choices = new ArrayList<>();

        String line;
        int lineCount = 0;
        int questionCount = 0;

        Pattern answerPattern = Pattern.compile("^[A-Z]\\. .+");
        Set<String> validAnswers = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                lineCount++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                Matcher answerMatcher = answerPattern.matcher(line);

                if (answerMatcher.matches()) {
                    Choice choice = new Choice(line.substring(3), 0, null, quest_id);
                    choices.add(choice);

                    validAnswers.add(line.substring(0, 1));
                } else if (line.startsWith("ANSWER: ")) {
                    String[] answers = line.substring(8).split(",");
                    int index;
                    double grade = 100.0 / answers.length;

                    for (String answer : answers) {
                        if (!validAnswers.contains(answer) || answer.length() > 1) {
                            return "Invalid correct answer at line " + lineCount;
                        }
                        // index of correct answer in choices
                        // index = total number of choices - number of invalid answers in this question + index of correct answer
                        index = choices.size() - validAnswers.size() + answer.charAt(0) - 'A';
                        choices.get(index).setChoiceGrade(grade);
                    }

                    if (validAnswers.size() < 2) {
                        return "Not enough answers at line " + lineCount;
                    }

                    validAnswers.clear(); // Reset valid answers
                } else {
                    quest_id++;
                    Question question = new Question(quest_id, getTime() + " " + quest_id, line, null);
                    questions.add(question);
                    questionCount++;
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
        } catch (IOException | SQLException e) {
            System.out.println(e.getMessage());
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
