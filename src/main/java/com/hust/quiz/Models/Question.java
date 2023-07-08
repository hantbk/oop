package com.hust.quiz.Models;

public class Question {
    private int question_id;
    private String question_name;
    private String question_text;
    private int mark = 1;
    private int category_id;

    public Question(String question_text) {
        this.question_text = question_text;
        category_id = 0;
    }

    public Question(String question_name, String question_text, int mark, int category_id) {
        this.question_name = question_name;
        this.question_text = question_text;
        this.mark = mark;
        this.category_id = category_id;
    }

    public Question(int question_id, String question_name, String question_text, int mark, int category_id) {
        this.question_id = question_id;
        this.question_name = question_name;
        this.question_text = question_text;
        this.mark = mark;
        this.category_id = category_id;
    }

    public int getId() {
        return question_id;
    }

    public void setId(int id) {
        this.question_id = id;
    }

    public String getQuestion_name() {
        return question_name;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public int getMark() {
        return mark;
    }

    public String getQuestionContent() {
        return question_name + ": " + question_text;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setInfo(String question_name, String question_text, int mark, int category_id) {
        this.question_name = question_name;
        this.question_text = question_text;
        this.mark = mark;
        this.category_id = category_id;
    }
}
