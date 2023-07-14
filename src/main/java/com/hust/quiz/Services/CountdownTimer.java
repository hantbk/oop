package com.hust.quiz.Services;

import com.hust.quiz.Views.ViewFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

public class CountdownTimer {

    public boolean running = true;
    private long seconds;
    private Label timerLabel;
    private Timeline timeline;

    public CountdownTimer(int seconds, Label timerLabel) {
        this.seconds = seconds;
        this.timerLabel = timerLabel;
    }

    public void start() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (seconds == 0) {
                stop();
            } else {
                seconds--;
            }
            if (running) {
                timerLabel.setText(getClockString());
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() {
        timeline.stop();
        timerLabel.setText("Time's up!");
        running = false;
        ViewFactory.getInstance().endQUiz();
    }

    private String formatTime(int hours, int minutes, int seconds) {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String getClockString() {
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long sec = seconds - hours * 60 * 60;
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