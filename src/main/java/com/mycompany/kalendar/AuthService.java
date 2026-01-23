/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.Base64;

/**
 *
 * @author Klara
 */
public class AuthService {
     // HASH lozinke (isti princip kao seed)
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Greška kod hashiranja", e);
        }
    }

    // REGISTER
    public static int register(String username, String email, String plainPassword) throws SQLException {

        String sql = """
            INSERT INTO users (username, email, password_hash)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashPassword(plainPassword));

            ps.executeUpdate();
            return idFromUsername(username);

        } catch (SQLIntegrityConstraintViolationException e) {
            // username ili email već postoji (UNIQUE)
            return -1;
        }
    }

    // LOGIN
    public static int login(String username, String plainPassword) throws SQLException {

        String sql = "SELECT password_hash FROM users WHERE username = ? LIMIT 1";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) return -1;

                String storedHash = rs.getString("password_hash");
                String inputHash = hashPassword(plainPassword);

                if (storedHash.equals(inputHash)) return idFromUsername(username);
                else return -1;
            }
        }
    }
    
    // user_id iz username
    public static int idFromUsername(String username) throws SQLException {
        String sql = "SELECT user_id FROM username WHERE username = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,username);
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) return -1;
                return rs.getInt("user_id");
            }
        }
    }
}
