package com.hust.quiz.Models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;

    public class FileChecker {
        public static boolean isTextFile(String filename) {
            Path path = Paths.get(filename);
            String contentType = null;
            try {
                contentType = Files.probeContentType(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contentType != null && contentType.equals("text/plain");
        }

        public static boolean isDocFile(String filename) {
            Path path = Paths.get(filename);
            String contentType = null;
            try {
                contentType = Files.probeContentType(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contentType != null && contentType.equals("doc/plain");
        }
    }
