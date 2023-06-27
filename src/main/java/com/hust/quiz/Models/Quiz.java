package com.hust.quiz.Models;

import java.util.List;

public class Quiz {
    private int quiz_id;
    private String quiz_name;
    private String quiz_description;
    private int timeLimit;
    private List<Question> listQuestion;
    public Quiz(String quiz_name) {
        this.quiz_name = quiz_name;
    }
    public Quiz(String quiz_name, String quiz_description) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
    }
    public Quiz(String quiz_name, String quiz_description, int timeLimit) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
    }
    public String getQuiz_name() {
        return quiz_name;
    }
    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }
    public String getQuiz_description() {
        return quiz_description;
    }
    public void setQuiz_description(String quiz_description) {
        this.quiz_description = quiz_description;
    }
    public List<Question> getListQuestion() {
        return listQuestion;
    }
    public void setListQuestion(List<Question> listQuestion) {
        this.listQuestion = listQuestion;
    }
}
