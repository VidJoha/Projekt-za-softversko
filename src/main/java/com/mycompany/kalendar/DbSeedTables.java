/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;

/**
 *
 * @author Klara
 */
public class DbSeedTables {

    public static void init() throws NoSuchAlgorithmException {

        try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword());
             Statement st = conn.createStatement()) {
            
            
            
            st.execute(seedUser("Ana"));
            st.execute(seedUser("Bob"));
            st.execute(seedUser("Camille"));
            st.execute(seedUser("Dado"));
            st.execute(seedUser("Emily"));
            st.execute(seedUser("Antonio"));
            st.execute(seedGroup("1.grupa za softversko"));
            st.execute(seedGroup("2.grupa za softversko"));
            st.execute(seedGroup("1.grupa za rwa"));
            st.execute(seedGroupMember(1,1,"Radi GUI"));
            st.execute(seedGroupMember(1,2,"Radi dokumentaciju"));
            st.execute(seedGroupMember(2,3,"Crta grafove"));
            st.execute(seedGroupMember(2,4,"Povezuje sve u cjelinu"));
            st.execute(seedGroupMember(3,5,"Piše Java script"));
            st.execute(seedGroupMember(3,6,"Uređuje stranicu"));
            st.execute(seedEvent(1,1,"Dijeljenje uloga","2026-01-20 15:00:00","2026-01-20 17:00:00","CONFIRMED"));
            st.execute(seedEvent(2,3,"Razrada projekta","2026-01-22 10:30:00","2026-01-22 12:00:00","CONFIRMED"));
            st.execute(seedEvent(3,6,"Prezentacija projekta","2026-01-30 11:00:00","2026-01-30 12:30:00","CONFIRMED"));
            st.execute(seedEventMember(1,1));
            st.execute(seedEventMember(1,2));
            st.execute(seedEventMember(2,3));
            st.execute(seedEventMember(2,4));
            st.execute(seedEventMember(3,5));
            st.execute(seedEventMember(3,6));
            seedMeetingProposal(1,1,"Predaja projekta","VOTING");
            st.execute(seedProposalSlots(1,"2026-01-29 11:00:00","2026-01-29 12:00:00","LOCKED"));
            st.execute(seedProposalSlots(1,"2026-01-29 13:00:00","2026-01-29 14:00:00","LOCKED"));
            st.execute(seedProposalSlots(1,"2026-01-29 15:00:00","2026-01-29 16:00:00","LOCKED"));
            seedMeetingProposal(1,1,"Pub Kviz","VOTING");
            st.execute(seedProposalSlots(2,"2026-01-25 11:00:00","2026-01-29 13:00:00","LOCKED"));
            st.execute(seedProposalSlots(2,"2026-01-25 13:00:00","2026-01-29 15:00:00","LOCKED"));
            st.execute(seedProposalSlots(2,"2026-01-25 15:00:00","2026-01-29 17:00:00","LOCKED"));
            
            
            
            System.out.println("Sve tablice uspješno inicijalizirane.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String hashSimple(String password) {
    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encodedhash);
    } catch (NoSuchAlgorithmException e) {
        // This should technically never happen for SHA-256 in standard Java
        throw new RuntimeException("Error: Encryption algorithm not found", e);
    }
}
    public static String seedUser(String ime){
        String mail=ime+"@gmail.com";
        String sifra=hashSimple(ime+"sifra");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String seedUser = """
                INSERT INTO users (email, username, password_hash, created_at) 
                VALUES ('%s', '%s', '%s', '%s');
                """.formatted(mail,ime,sifra, timestamp);
        return seedUser;
    }
    public static String seedGroup(String name){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String seedGroup = """
                INSERT INTO groups (name, created_at) 
                VALUES ('%s', '%s');
                """.formatted(name,timestamp);
        return seedGroup;
    }
    public static String seedGroupMember(int group_id,int user_id,String role){
        String seedGroupMember = """
                INSERT INTO group_members (group_id, user_id, role) 
                VALUES ('%s', '%s','%s');
                """.formatted(group_id,user_id,role);
        return seedGroupMember;
    }
    public static String seedEvent(int group_id,int created_by,String title,String start_time,String end_time,String status){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        LocalDateTime startTime = LocalDateTime.parse(start_time, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end_time, formatter);
        String seedEvent = """
                INSERT INTO events (group_id, created_by, title, start_time,end_time,status,created_at) 
                VALUES ('%s', '%s', '%s', '%s', '%s', '%s','%s');
                """.formatted(group_id,created_by,title,startTime,endTime,status,timestamp);
        return seedEvent;
    }
    public static String seedEventMember(int event_id,int user_id){
        String seedEventMember = """
                INSERT INTO event_participants (event_id, user_id) 
                VALUES ('%s', '%s');
                """.formatted(event_id,user_id);
        return seedEventMember;
    }
    public static void seedMeetingProposal(int group_id,int created_by,String title,String status){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String seedMeetingProposal = """
                INSERT INTO meeting_proposals (group_id,created_by,title,status,created_at) 
                VALUES ('%s', '%s', '%s', '%s', '%s');
                """.formatted(group_id,created_by,title,status,timestamp);
        
        ArrayList<Integer> članovigrupe;
        članovigrupe=AuthService.allMembers(group_id);
        int idzadnjegproposala=AuthService.lastproposal();
        
            try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword());
             Statement st = conn.createStatement()) {
                st.execute(seedMeetingProposal);
                for(int i=0;i<članovigrupe.size();i++){
                    st.execute(seedProposalParticipants(idzadnjegproposala,članovigrupe.get(i)));
                }
                
                } catch (Exception e) {
                e.printStackTrace();
            }
        
    }
    public static String seedProposalParticipants(int proposal_id,int user_id){
        String seedProposalParticipants = """
                INSERT INTO proposal_participants (proposal_id,user_id) 
                VALUES ('%s', '%s');
                """.formatted(proposal_id,user_id);
        return seedProposalParticipants;
    }
    public static String seedProposalSlots(int proposal_id,String start_time,String end_time,String status){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start_time, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end_time, formatter);
        String seedProposalSlots = """
                INSERT INTO proposal_slots (proposal_id,start_time,end_time,status) 
                VALUES ('%s', '%s', '%s', '%s');
                """.formatted(proposal_id,startTime,endTime,status);
        return seedProposalSlots;
    }
    public static String seedVotes(int proposal_id,int slot_id,int user_id){
        String seedVotes = """
                INSERT INTO votes (proposal_id,slot_id,user_id,vote_value) 
                VALUES ('%s', '%s', '%s', '%s');
                """.formatted(proposal_id,slot_id,user_id,1);
        return seedVotes;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException {
        
        System.out.println("SEED START");
        DbSeedTables.init();
        System.out.println("SEED END");


    }
}








