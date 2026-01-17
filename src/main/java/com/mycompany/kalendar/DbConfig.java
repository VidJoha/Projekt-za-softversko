/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

/**
 *
 * @author Klara
 */
import java.io.InputStream;
import java.util.Properties;

public class DbConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = DbConfig.class
                .getClassLoader()
                .getResourceAsStream("config/db.properties")) {

            if (is == null) {
                throw new RuntimeException("Ne mogu pronaći db.properties");
            }

            props.load(is);

        } catch (Exception e) {
            throw new RuntimeException("Greška pri učitavanju db.properties", e);
        }
    }

    public static String getUrl() {
        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port", "3306");
        String name = props.getProperty("db.name");
        String params = props.getProperty("db.params", "");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + name;

        if (!params.isBlank()) {
            url += "?" + params;
        }

        return url;
    }

    public static String getUser() {
        return props.getProperty("db.user");
    }

    public static String getPassword() {
        return props.getProperty("db.password");
    }
}

