package com.hust.quiz.Services;

import java.io.File;

public class ImageService {
    public static final String PATH = "D:\\IntelliJ\\oop\\src\\main\\resources\\question_img\\";

    /**
     * Get path of image
     *
     * @param question_id id of question
     * @return String path of image
     */
    public static String getImage(int question_id) {
        String filePath = PATH + "image_" + question_id + ".png";
        // check if image exists
        File file = new File(filePath);
        if (file.exists()) {
            return filePath;
        }
        return null;
    }
}
