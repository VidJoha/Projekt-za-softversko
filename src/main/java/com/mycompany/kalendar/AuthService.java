/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
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
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,username);
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) return -1;
                return rs.getInt("user_id");
            }
        }
    }
    public static ArrayList<Integer> allProposals(int trenutniuserid){
        String sql = "SELECT proposal_id FROM proposal_participants WHERE user_id = ?";
        ArrayList<Integer> idsvihproposala=new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,trenutniuserid);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    idsvihproposala.add(rs.getInt("proposal_id"));
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return idsvihproposala;
    }
    public static ArrayList<Integer> allSlots(int proposal_id){
        String sql = "SELECT slot_id FROM proposal_slots WHERE proposal_id = ?";
        ArrayList<Integer> idsvihslotova=new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,proposal_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    idsvihslotova.add(rs.getInt("slot_id"));
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return idsvihslotova;
    }
    public static ArrayList<Integer> allMembers(int group_id){
        String sql = "SELECT user_id FROM group_members WHERE group_id = ?";
        ArrayList<Integer> idsvihčlanova=new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,group_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    idsvihčlanova.add(rs.getInt("user_id"));
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return idsvihčlanova;
    }
    public static int lastproposal(){
        String sql = "SELECT proposal_id FROM meeting_proposals";
        ArrayList<Integer> idsvihproposala=new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    idsvihproposala.add(rs.getInt("proposal_id"));
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(idsvihproposala.isEmpty()){
            return 1;
        }
        return idsvihproposala.get(idsvihproposala.size()-1)+1;
    }
    public static String getTitle(int proposal_id){
        String sql = "SELECT title FROM meeting_proposals WHERE proposal_id = ?";
        String naslov=new String();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,proposal_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    naslov=rs.getString("title");
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return naslov;
    }
    public static Integer isVoteSubmitted(int proposal_id,int slot_id,int user_id){
        String sql = "SELECT * FROM votes WHERE proposal_id = ? AND slot_id=? AND user_id=?";

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,proposal_id);
            ps.setInt(2,slot_id);
            ps.setInt(3,user_id);

            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    System.out.println(proposal_id+" "+slot_id+" "+ user_id+" vec postoji");
                    return 1;
                }
                
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
