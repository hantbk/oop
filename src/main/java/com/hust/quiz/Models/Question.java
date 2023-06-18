package com.hust.quiz.Models;

import com.hust.quiz.Services.ChoiceService;

import java.util.List;

public class Question {
    private int question_id;
    private String question_name;
    private String question_text;
    private int category_id;
    private List<Choice> listChoice;

    public Question(String question_text) {
        this.question_text = question_text;
        category_id = 0;
    }

    public Question(String question_name, String question_text, int category_id) {
        this.question_name = question_name;
        this.question_text = question_text;
        this.category_id = category_id;
    }

    public Question(int question_id, String question_name, String question_text, int category_id) {
        this.question_id = question_id;
        this.question_name = question_name;
        this.question_text = question_text;
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

    public void setQuestion(String question_name, String question_text) {
        this.question_name = question_name;
        this.question_text = question_text;
    }

    public String getQuestionContent() {
        return question_name + ": " + question_text;
    }

    public List<Choice> getListChoice() {
        return listChoice;
    }

    public void setListChoice() {
        ChoiceService choiceService = new ChoiceService();
        this.listChoice = choiceService.getChoice(this.question_id);
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    //test add choice
    public void getDetailQuestion(int id) {

    }
}
