/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public static ArrayList<Integer> allProposalsWhereOwner(int trenutniuserid){
        String sql = "SELECT proposal_id FROM meeting_proposals WHERE created_by = ?";
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
    public static Integer allVotes(int proposal_id,int slot_id){
        String sql = "SELECT * FROM votes WHERE proposal_id = ? AND slot_id=?";
        int brojglasova=0;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,proposal_id);
            ps.setInt(2, slot_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    brojglasova+=1;
                }
                
            }
        }
        
        catch (Exception e) {
            e.printStackTrace();
        }
        return brojglasova;
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
    public static int lastevent(){
        String sql = "SELECT event_id FROM events";
        ArrayList<Integer> idsvihevenata=new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    idsvihevenata.add(rs.getInt("event_id"));
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(idsvihevenata.isEmpty()){
            return 1;
        }
        System.out.println(idsvihevenata.get(idsvihevenata.size()-1)+1);
        return idsvihevenata.get(idsvihevenata.size()-1)+1;
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
    public static Integer getGroupId(int proposal_id){
        String sql = "SELECT group_id FROM meeting_proposals WHERE proposal_id = ?";
        int group_id=0;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,proposal_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    group_id=rs.getInt("group_id");
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return group_id;
    }
    public static Integer getCreatedBy(int proposal_id){
        String sql = "SELECT created_by FROM meeting_proposals WHERE proposal_id = ?";
        int created_by=0;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,proposal_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    created_by=rs.getInt("created_by");
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return created_by;
    }
    public static String getStartTime(int slot_id){
        String sql = "SELECT start_time FROM proposal_slots WHERE slot_id = ?";
        LocalDateTime start_time = null;
        String start_time_string = null;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,slot_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    start_time=rs.getObject("start_time",LocalDateTime.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
                    // 3. Convert to string
                    start_time_string = start_time.format(formatter);
                    System.out.println(start_time_string);
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return start_time_string;
    }
    public static String getEndTime(int slot_id){
        String sql = "SELECT end_time FROM proposal_slots WHERE slot_id = ?";
        LocalDateTime end_time = null;
        String end_time_string = null;
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,slot_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    end_time=rs.getObject("end_time",LocalDateTime.class);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
                    // 3. Convert to string
                    end_time_string = end_time.format(formatter);
                    System.out.println(end_time_string);
                    
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return end_time_string;
    }
    public static Date getEndTimeDate(int slot_id){
        String sql = "SELECT end_time FROM proposal_slots WHERE slot_id = ?";
        Date end_time = null;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,slot_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    end_time=rs.getDate("end_time");

                    System.out.println(end_time);
                    
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return end_time;
    }
    public static Date getStartTimeDate(int slot_id){
        String sql = "SELECT start_time FROM proposal_slots WHERE slot_id = ?";
        Date start_time = null;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,slot_id);

            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    start_time=rs.getDate("start_time");

                    System.out.println(start_time);
                    
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return start_time;
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
