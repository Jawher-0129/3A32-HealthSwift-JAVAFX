package com.example.gestionressourcesmaterielles.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {
    private String url = "jdbc:mysql://localhost:3306/healthswift";
    private String login = "root";
    private String pwd = "";
    private Connection connetion;
    private static DataSource instance;

    private DataSource() {
        try {
            this.connetion = DriverManager.getConnection(this.url, this.login, this.pwd);
            System.out.println("Connexion établie");
        } catch (SQLException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }

        return instance;
    }

    public Connection getConnetion() {
        return this.connetion;
    }
}

