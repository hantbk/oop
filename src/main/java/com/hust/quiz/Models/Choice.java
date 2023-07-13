package com.hust.quiz.Models;

public class Choice {
    private final int question_id;
    private int id;
    private String content;
    private double choiceGrade;

    public Choice(int id, String content, double choiceGrade, int question_id) {
        this.id = id;
        this.content = content;
        this.choiceGrade = Math.round(choiceGrade * 1e5) / 1e5;
        this.question_id = question_id;
    }

    public Choice(String content, double choiceGrade, int question_id) {
        this.content = content;
        this.choiceGrade = Math.round(choiceGrade * 1e5) / 1e5;
        this.question_id = question_id;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public double getChoiceGrade() {
        return choiceGrade;
    }

    public void setChoiceGrade(double choiceGrade) {
        this.choiceGrade = Math.round(choiceGrade * 1e5) / 1e5;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setInfo(String content, double choiceGrade) {
        this.content = content;
        this.choiceGrade = Math.round(choiceGrade * 1e5) / 1e5;
    }
}
