package com.example.csit228_f1_v2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/dbcsit228sullano";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection c = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection(URL,USER,PASSWORD);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return c;
    }
}