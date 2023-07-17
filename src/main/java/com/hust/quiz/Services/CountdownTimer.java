package com.hust.quiz.Services;

import com.hust.quiz.Views.ViewFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class CountdownTimer {
    private final Label label;
    public boolean done;
    private int time; // in seconds
    private Timeline timeline;

    public CountdownTimer(Label label) {
        this.label = label;
        done = false;
    }

    public void setTimeAndRun(int time) {
        this.time = time;
        label.setText(getClockString());

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            this.time--;
            label.setText(getClockString());
        }));
        timeline.setCycleCount(time);
        timeline.setOnFinished(event -> {
            done = true;
            label.setText("Time's up!");
            ViewFactory.getInstance().endQUiz();
        });
        timeline.play();
    }

    private String getClockString() {
        long hours = TimeUnit.SECONDS.toHours(time);
        long sec = time - hours * 60 * 60;
        long min = TimeUnit.SECONDS.toMinutes(sec);
        sec = sec - (min * 60);
        return format(hours) + ':' + format(min) + ":" + format(sec);
    }

    private String format(long num) {
        if (num < 10) {
            return "0" + num;
        }
        return "" + num;
    }
}