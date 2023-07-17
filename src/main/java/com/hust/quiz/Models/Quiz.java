package com.hust.quiz.Models;

import java.sql.Date;
import java.time.LocalDate;

public class Quiz {
    private final String quiz_name;
    private final String quiz_description;
    private final String time_format;
    private final int timeLimit;
    private final LocalDate open_date;
    private final LocalDate close_date;
    private int quiz_id;

    public Quiz(String quiz_name, String quiz_description, int timeLimit, String time_format, LocalDate open_date, LocalDate close_date) {
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
        this.time_format = time_format;
        this.open_date = open_date;
        this.close_date = close_date;
    }

    public Quiz(int quiz_id, String quiz_name, String quiz_description, int timeLimit, String time_format, LocalDate open_date, LocalDate close_date) {
        this.quiz_id = quiz_id;
        this.quiz_name = quiz_name;
        this.quiz_description = quiz_description;
        this.timeLimit = timeLimit;
        this.time_format = time_format;
        this.open_date = open_date;
        this.close_date = close_date;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public String getQuiz_description() {
        return quiz_description;
    }

    public String getTimeFormat() {
        return time_format;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public Date getOpen_date() {
        if (open_date == null) {
            return null;
        }
        return Date.valueOf(open_date);
    }

    public Date getClose_date() {
        if (close_date == null) {
            return null;
        }
        return Date.valueOf(close_date);
    }
}
