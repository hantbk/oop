package com.hust.quiz.Models;

import java.time.LocalDate;
import java.util.List;

public class Quiz {
    private int quiz_id;
    private String quiz_name;
    private String quiz_description;
    private String time_format;
    private int timeLimit;
    private LocalDate time_close;
    private LocalDate time_open;
    private List<Question> listQuestion;

    public Quiz(String quiz_name, String quiz_description) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
    }

    public Quiz(String quiz_name, String quiz_description, int timeLimit, String time_format) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
        this.time_format = time_format;
    }

    public Quiz(String quiz_name, String quiz_description, int timeLimit/*, String time_format*/) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
//        this.time_format = time_format;
    }

    public Quiz(int quiz_id, String quiz_name, String quiz_description, int timeLimit, String time_format) {
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
        this.time_format = time_format;
    }

    public Quiz(String quiz_name, String quiz_description, int timeLimit, LocalDate time_close, LocalDate time_open) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
        this.time_close = time_close;
        this.time_open = time_open;
    }

    public Quiz(String quiz_name, String quiz_description, int timeLimit, LocalDate time_open) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
        this.time_open = time_open;
    }

    public Quiz(String quiz_name, String quiz_description, LocalDate time_open) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.time_open = time_open;
    }

    public Quiz(int quiz_id, String quiz_name, String quiz_description) {
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
    }

    public Quiz(String quiz_name, String quiz_description, LocalDate time_close, LocalDate time_open) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.time_close = time_close;
        this.time_open = time_open;
    }

    public Quiz() {
    }

    ;

    public String getQuiz_name() {
        return quiz_name;
    }

    public String getQuiz_description() {
        return quiz_description;
    }

    public List<Question> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<Question> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public void setTime_close(LocalDate time_close) {
        this.time_close = time_close;
    }

    public void setTime_open(LocalDate time_open) {
        this.time_open = time_open;
    }

    public void setTime_format(String time_format) {
        this.time_format = time_format;
    }

    public String getTimeFormat() {
        return time_format;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

}
