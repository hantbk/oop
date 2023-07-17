package com.hust.quiz.Services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImageService {
    private static final String BASE_PATH = System.getProperty("user.dir").endsWith("oop") ?
            System.getProperty("user.dir") : System.getProperty("user.dir") + "\\oop";

    public static final String PATH_QUESTION = BASE_PATH + "\\src\\main\\resources\\question_img\\";
    public static final String PATH_CHOICE = BASE_PATH + "\\src\\main\\resources\\choice_img\\";

    /**
     * Copy image from path to PATH
     *
     * @param id         id of question
     * @param path       path of image
     * @param isQuestion true if image is question's image, false if image is choice's image
     * @return String new path of image
     */
    public static String saveImage(int id, String path, boolean isQuestion) {
        if (path == null) {
            return null;
        }
        String extension = path.substring(path.lastIndexOf("."));
        String newFilePath;
        System.out.println(BASE_PATH);
        if (isQuestion) {
            newFilePath = PATH_QUESTION + "question_" + id + extension;
        } else {
            newFilePath = PATH_CHOICE + "choice_" + id + extension;
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
