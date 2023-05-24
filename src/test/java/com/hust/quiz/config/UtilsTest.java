package com.hust.quiz.config;

import com.hust.quiz.Services.Utils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UtilsTest {
    private static Connection connection;

    @Test
    public void beforeAll() throws SQLException {
        connection = Utils.getConnection();
    }

    @Test
    public void afterAll() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testQuantity() throws SQLException {
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery("SELECT * FROM quiz.course_categories");

        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }

    }
}
