/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author Klara
 */
public class DbInit {

    public static void init() {

        String createUsers = """
            CREATE TABLE IF NOT EXISTS users (
                user_id INT AUTO_INCREMENT PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                username VARCHAR(100) NOT NULL UNIQUE,
                password_hash VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createGroups = """
            CREATE TABLE IF NOT EXISTS groups (
                group_id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(200) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;

        String createGroupMembers = """
            CREATE TABLE IF NOT EXISTS group_members (
                group_id INT NOT NULL,
                user_id INT NOT NULL,
                role VARCHAR(30) DEFAULT 'member',
                PRIMARY KEY (group_id, user_id),
                FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;

        String createEvents = """
            CREATE TABLE IF NOT EXISTS events (
                event_id INT AUTO_INCREMENT PRIMARY KEY,
                group_id INT,
                created_by INT NOT NULL,
                title VARCHAR(300) NOT NULL,
                start_time DATETIME NOT NULL,
                end_time DATETIME NOT NULL,
                status VARCHAR(20) DEFAULT 'CONFIRMED',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE SET NULL,
                FOREIGN KEY (created_by) REFERENCES users(user_id),
                CHECK (end_time > start_time)
            )
        """;

        String createEventParticipants = """
            CREATE TABLE IF NOT EXISTS event_participants (
                event_id INT NOT NULL,
                user_id INT NOT NULL,
                PRIMARY KEY (event_id, user_id),
                FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;

        String createProposals = """
            CREATE TABLE IF NOT EXISTS meeting_proposals (
                proposal_id INT AUTO_INCREMENT PRIMARY KEY,
                group_id INT NOT NULL,
                created_by INT NOT NULL,
                title VARCHAR(300) NOT NULL,
                status VARCHAR(20) DEFAULT 'VOTING',
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (group_id) REFERENCES groups(group_id) ON DELETE CASCADE,
                FOREIGN KEY (created_by) REFERENCES users(user_id)
            )
        """;

        String createProposalParticipants = """
            CREATE TABLE IF NOT EXISTS proposal_participants (
                proposal_id INT NOT NULL,
                user_id INT NOT NULL,
                PRIMARY KEY (proposal_id, user_id),
                FOREIGN KEY (proposal_id) REFERENCES meeting_proposals(proposal_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;

        String createProposalSlots = """
            CREATE TABLE IF NOT EXISTS proposal_slots (
                slot_id INT AUTO_INCREMENT PRIMARY KEY,
                proposal_id INT NOT NULL,
                start_time DATETIME NOT NULL,
                end_time DATETIME NOT NULL,
                status VARCHAR(20) DEFAULT 'LOCKED',
                FOREIGN KEY (proposal_id) REFERENCES meeting_proposals(proposal_id) ON DELETE CASCADE,
                CHECK (end_time > start_time)
            )
        """;

        String createVotes = """
            CREATE TABLE IF NOT EXISTS votes (
                proposal_id INT NOT NULL,
                slot_id INT NOT NULL,
                user_id INT NOT NULL,
                vote_value INT DEFAULT 1,
                PRIMARY KEY (proposal_id, user_id),
                FOREIGN KEY (proposal_id) REFERENCES meeting_proposals(proposal_id) ON DELETE CASCADE,
                FOREIGN KEY (slot_id) REFERENCES proposal_slots(slot_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;

        String createTimeLocks = """
            CREATE TABLE IF NOT EXISTS time_locks (
                lock_id INT AUTO_INCREMENT PRIMARY KEY,
                proposal_id INT NOT NULL,
                user_id INT NOT NULL,
                start_time DATETIME NOT NULL,
                end_time DATETIME NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (proposal_id) REFERENCES meeting_proposals(proposal_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                CHECK (end_time > start_time)
            )
        """;

        try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword());
             Statement st = conn.createStatement()) {

            st.execute(createUsers);
            st.execute(createGroups);
            st.execute(createGroupMembers);
            st.execute(createEvents);
            st.execute(createEventParticipants);
            st.execute(createProposals);
            st.execute(createProposalParticipants);
            st.execute(createProposalSlots);
            st.execute(createVotes);
            st.execute(createTimeLocks);

            System.out.println("Sve tablice uspješno inicijalizirane.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
