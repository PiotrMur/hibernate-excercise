package com.luv2code.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestJdbc {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/employees_database";
        String user = "root";
        String password = "!mch0teP11553478";

        try{
            Connection conn = DriverManager.getConnection(url,user,password);

            System.out.println("Connection successful");

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
