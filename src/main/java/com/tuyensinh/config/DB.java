package com.tuyensinh.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    public static Connection getConn() throws Exception {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/xettuyen2026",
                "root",
                "anhtuan2407");
    }
}
