package com.hust.quiz.Models;

public class Category {
    // final because we don't have any setter
    private final int id_number;
    private final String name;
    private final int parent_id;
    private final int question_count;
    private final String category_info;

    public Category(int id_number, String name, int parent_id, int question_count, String category_info) {
        this.id_number = id_number;
        this.name = name;
        this.parent_id = parent_id;
        this.question_count = question_count;
        this.category_info = category_info;
    }

    public static String getName(String category_name) {
        int openParenIndex = category_name.indexOf(" (");
        if (openParenIndex < 0) { // If we have no " ("
            openParenIndex = category_name.length();
        }
        return category_name.substring(0, openParenIndex);
    }

    @Override
    public String toString() {
        if (question_count == 0) {
            return name;
        } else {
            return name + " (" + question_count + ")";
        }
    }

    public int getId() {
        return id_number;
    }

    public String getName() {
        return name;
    }

    public int getParent_id() {
        return parent_id;
    }

    public String getCategory_info() {
        return category_info;
    }

    public int getQuestion_count() {
        return question_count;
    }
}