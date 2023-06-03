package com.hust.quiz.Models;

public class Choice {
    private String id;
    private String content;
    private boolean isCorrect = false;

    public Choice(String id, String content){
        this.id = id;
        this.content = content;
    }
    public Choice(String id, String content, boolean isCorrect) {
        this.id = id;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    @Override
    public String toString() {
        return id + content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
