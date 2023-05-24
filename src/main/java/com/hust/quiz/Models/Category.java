package com.hust.quiz.Models;

public class Category {
    private int id;
    private String name;
    private int parent_id;
    private int course_count;
    private String category_info;

    public Category() {

    }

    public Category(int id, String name, int parent_id, int course_count, String category_info) {
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
        this.category_info = category_info;
        this.course_count = course_count;
    }

    @Override
    public String toString() {
        if (course_count == 0) {
            return name;
        } else {
            return name + " (" + course_count + ")";
        }
    }

    public int getId() {
        return id;
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

    public int getCourse_count() {
        return course_count;
    }
}
