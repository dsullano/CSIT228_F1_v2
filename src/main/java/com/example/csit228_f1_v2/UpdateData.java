package com.example.csit228_f1_v2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdateData {
    public static void main(String[] args) {
        try(Connection c = MySQLConnection.getConnection();
            PreparedStatement statement = c.prepareStatement(
                    "UPDATE tblusers SET name=? WHERE id=?"
            )){
            String new_name = "Fiel Rodrigo";
            int id = 2;

            statement.setString(1,new_name);
            statement.setInt(2,id);

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}