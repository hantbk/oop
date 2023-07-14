package com.hust.quiz.Services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageService {
    public static final String PATH_QUESTION = "D:\\IntelliJ\\oop\\src\\main\\resources\\question_img\\";
    public static final String PATH_CHOICE = "D:\\IntelliJ\\oop\\src\\main\\resources\\choice_img\\";

    /**
     * Get path of image
     *
     * @param question_id id of question
     * @return String path of image
     */
    public static String getImage(int question_id) {
        String filePath = PATH_QUESTION + "question_" + question_id + ".png";
        // check if image exists
        File file = new File(filePath);
        if (file.exists()) {
            return filePath;
        }
        return null;
    }

    /**
     * Copy image from path to PATH
     *
     * @param id         id of question
     * @param path       path of image
     * @param isQuestion true if image is question's image, false if image is choice's image
     * @return String new path of image
     */
    public static String saveImage(int id, String path, boolean isQuestion) {
        String newFilePath;
        if (isQuestion) {
            newFilePath = PATH_QUESTION + "question_" + id + ".png";
        } else {
            newFilePath = PATH_CHOICE + "choice_" + id + ".png";
        }
        if (path.equals(newFilePath)) {
            return newFilePath;
        }
        try {
            Files.copy(Path.of(path), Path.of(newFilePath));
        } catch (IOException e) {
            System.out.println("Error save image: " + e.getMessage());
        }
        return newFilePath;
    }
}
