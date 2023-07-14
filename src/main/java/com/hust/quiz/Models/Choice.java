package com.hust.quiz.Models;

public class Choice {
    private final int question_id;
    private int id;
    private String content;
    private double choice_grade;
    private String choice_image;

    public Choice(int id, String content, double choiceGrade, String choice_image, int question_id) {
        this.id = id;
        this.content = content;
        this.choice_grade = Math.round(choiceGrade * 1e5) / 1e5;
        this.choice_image = choice_image;
        this.question_id = question_id;
    }

    public Choice(String content, double choiceGrade, String choice_image, int question_id) {
        this.content = content;
        this.choice_grade = Math.round(choiceGrade * 1e5) / 1e5;
        this.choice_image = choice_image;
        this.question_id = question_id;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public double getChoiceGrade() {
        return choice_grade;
    }

    public void setChoiceGrade(double choice_grade) {
        this.choice_grade = Math.round(choice_grade * 1e5) / 1e5;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public String getChoiceImage() {
        return choice_image;
    }

    public void setInfo(String content, double choiceGrade, String choice_image) {
        this.content = content;
        this.choice_grade = Math.round(choiceGrade * 1e5) / 1e5;
        this.choice_image = choice_image;
    }
}
