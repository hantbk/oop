package com.hust.quiz.Models;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String id;
    private String content;
    private int categoryId;
    private List<Choice> listChoice;

    public Question(){
    }

    public Question(String id, String content, List<Choice> listChoice) {
        this.id = id;
        this.content = content;
        this.listChoice = listChoice;
    }


    public void getDetailQuestion() {
        System.out.println(id + ": " + content);
        for(Choice choice : listChoice){
            System.out.println(choice.toString());
        }
    }

    public Question(String id, String content, int categoryId, List<Choice> listChoice) {
        this.id = id;
        this.content = content;
        this.categoryId = categoryId;
        this.listChoice = listChoice;
    }

//    public String

    public String getId() { return id;  }

    public void setId(String id) {  this.id = id;   }

    public String getContent() {    return content; }

    public void setContent(String content) {    this.content = content; }

    public int getCategoryId() {   return categoryId; }

    public void setCategoryId(int categoryId) {   this.categoryId = categoryId; }

    public List<Choice> getListChoice() {
        return listChoice;
    }

    public void setListChoice(List<Choice> listChoice) {
        this.listChoice = listChoice;
    }
}
