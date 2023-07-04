package com.hust.quiz.Services;

import java.io.*;
import java.sql.*;

public class ImageService {
    public static void getImage( int question_id) {
        try (Connection conn = Utils.getConnection()){
            String sql = "select image_data from choice where question_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, String.valueOf(question_id));
            ResultSet rs = statement.executeQuery();
            File file = new File("image.jpg");
            FileOutputStream fos = new FileOutputStream(file);
            byte b[];
            Blob blob;

            while(rs.next()){
                blob = rs.getBlob("image_data");
                b = blob.getBytes(1,(int)blob.length());
                fos.write(b);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void addImage(File file, int question_id) throws SQLException{
        try (Connection conn = Utils.getConnection()) {
            String sql = "UPDATE choice SET image_data = ? WHERE question_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            FileInputStream fis = new FileInputStream(file);
            statement.setInt(2, question_id);
            statement.setBinaryStream(1,fis,(int)file.length());
            statement.executeUpdate();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
