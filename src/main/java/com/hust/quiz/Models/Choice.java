package com.hust.quiz.Models;

public class Choice {
    private int id;
    private String content;
    private boolean isCorrect = false;
    private int choiceGrade;
    private int question_id;

    public Choice() {

    }

    public Choice(int id, String content, boolean isCorrect, int choiceGrade, int question_id) {
        this.id = id;
        this.content = content;
        this.isCorrect = isCorrect;
        this.choiceGrade = choiceGrade;
        this.question_id = question_id;
    }

    public Choice(String content, boolean isCorrect, int choiceGrade, int question_id) {
        this.content = content;
        this.isCorrect = isCorrect;
        this.choiceGrade = choiceGrade;
        this.question_id = question_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getChoiceGrade() {
        return choiceGrade;
    }

    public void setChoiceGrade(int choiceGrade) {
        this.choiceGrade = choiceGrade;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
