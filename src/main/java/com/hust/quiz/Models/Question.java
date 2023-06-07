package com.hust.quiz.Models;

public class Question {
    private int question_id;
    private String question_content;
    private int category_id;
    public Question() {

    }

    // missing category: FIXED - DONE
    // constructor for getting questions
    public Question(int question_id, String question_content, int category_id) {
        this.question_id = question_id;
        this.question_content = question_content;
        this.category_id = category_id;
    }

    // missing category - DONE FIXED
    // constructor for adding questions: NOT DONE
    public int getId() {
        return question_id;
    }
    public void setId(int id) {
        this.question_id = id;
    }
    public String getQuestion() {
        return question_content;
    }
    public void setQuestion(String question) {
        this.question_content = question;
    }
}
