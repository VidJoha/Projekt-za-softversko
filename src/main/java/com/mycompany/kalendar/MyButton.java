/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import static com.mycompany.kalendar.DbSeedTables.seedMeetingProposal;
import static com.mycompany.kalendar.DbSeedTables.seedProposalSlots;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;

/**
 *
 * @author Vid
 */
//Moj gumb
//Modificirani JButton da mu mogu odmah u konstruktoru namjestit lokaciju, veličinu i tekst
public class MyButton extends JButton {

    private int ButtonDan;
    private int ButtonMjesec;
    private int ButtonGodina;
    MyButton(int xpozicija,int ypozicija,int širina, int visina,int dan,int mjesec, int godina){
       this.setBounds(xpozicija,ypozicija,širina,visina);
       this.setText(dan + "." + mjesec);
       ButtonDan=dan;
       ButtonMjesec=mjesec;
       ButtonGodina=godina;
       
       
    }
    Integer getDan(){
        return ButtonDan;
    }
    Integer getMjesec(){
        return ButtonMjesec;
    }
    Integer getGodina(){
        return ButtonGodina;
    }
    public static void dodajutablicu()  throws NoSuchAlgorithmException{
        try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword());
             Statement st = conn.createStatement()) {
       seedMeetingProposal(1,1,"Pub Kviz","VOTING");
        st.execute(seedProposalSlots(1,"2026-01-25 11:00:00","2026-01-29 13:00:00","LOCKED"));
        st.execute(seedProposalSlots(1,"2026-01-25 13:00:00","2026-01-29 15:00:00","LOCKED"));
        st.execute(seedProposalSlots(1,"2026-01-25 15:00:00","2026-01-29 17:00:00","LOCKED"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws NoSuchAlgorithmException {
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
                FOREIGN KEY (proposal_id) REFERENCES meeting_proposals(proposal_id) ON DELETE CASCADE,
                FOREIGN KEY (slot_id) REFERENCES proposal_slots(slot_id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
            )
        """;
        try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword());
             Statement st = conn.createStatement()) {
            
            
            System.out.println("Popravljam tablice");
            /*st.execute(createProposals);
            st.execute(createProposalParticipants);
            st.execute(createProposalSlots);
            seedMeetingProposal(1,1,"Predaja projekta","VOTING");
            st.execute(seedProposalSlots(1,"2026-01-29 11:00:00","2026-01-29 12:00:00","LOCKED"));
            st.execute(seedProposalSlots(1,"2026-01-29 13:00:00","2026-01-29 14:00:00","LOCKED"));
            st.execute(seedProposalSlots(1,"2026-01-29 15:00:00","2026-01-29 16:00:00","LOCKED"));
            seedMeetingProposal(1,1,"Pub Kviz","VOTING");
            st.execute(seedProposalSlots(2,"2026-01-25 11:00:00","2026-01-25 13:00:00","LOCKED"));
            st.execute(seedProposalSlots(2,"2026-01-25 13:00:00","2026-01-25 15:00:00","LOCKED"));
            st.execute(seedProposalSlots(2,"2026-01-25 15:00:00","2026-01-25 17:00:00","LOCKED"));
            seedMeetingProposal(1,1,"Kava","VOTING");
            st.execute(seedProposalSlots(3,"2026-01-24 10:00:00","2026-01-24 13:00:00","LOCKED"));
            st.execute(seedProposalSlots(3,"2026-01-24 12:00:00","2026-01-24 15:00:00","LOCKED"));
            st.execute(seedProposalSlots(3,"2026-01-24 13:00:00","2026-01-24 17:00:00","LOCKED"));*/
            st.execute(createVotes);
            System.out.println("Sve tablice uspješno inicijalizirane.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        

        
        System.out.println("Tablice popravljene");
        


    }
    
   }
    //Ima i opciju da ispiše tekst, tek tak da provjerim radi li kad ga kliknem
    

