/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import static com.mycompany.kalendar.DbSeedTables.seedVotes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Vid
 */
public class GlasajFrame extends JFrame implements ActionListener{
    private int RasporedMjesec;
    private int RasporedGodina;
    int RasporedUserId;
    
    ArrayList<Integer> sviproposali;
    ArrayList<Integer> svitogglebuttoniselected;
    ArrayList<JButton> svitogglebuttonizaglasat;
    ArrayList<Integer> sviproposalizaglasat;
    ArrayList<Integer> svislotovizaglasat;
    ArrayList<Integer> svezasubmit;
    
    JPanel glasanjePanel=new JPanel();
    JButton izlazak=new JButton("Vrati se na kalendar");
    JButton submitglasove=new JButton("Predaj glasove");
    
    GlasajFrame(int trenutniuserid,int trenutnimjesec,int trenutnagodina){
        RasporedMjesec=trenutnimjesec;
        RasporedGodina=trenutnagodina;
        RasporedUserId=trenutniuserid;
        
        sviproposali=new ArrayList<>();
        svitogglebuttoniselected=new ArrayList<>();
        svitogglebuttonizaglasat=new ArrayList<>();
        sviproposalizaglasat=new ArrayList<>();
        svislotovizaglasat=new ArrayList<>();
        svezasubmit=new ArrayList<>();
        
        JPanel naslovPanel=new JPanel();
        JPanel naslovPanel1=new JPanel();
        JPanel naslovPanel2=new JPanel();
        JLabel naslovLabel1=new JLabel();
        JLabel naslovLabel2=new JLabel();
        String line1="Ovo su eventi za koje možeš glasati";
        String line2="Označi sve termine koji ti odgovaraju i klikni";
        naslovLabel1.setText(line1);
        naslovLabel2.setText(line2);
        naslovLabel1.setFont(new Font("Calibri",Font.PLAIN,30));
        naslovLabel2.setFont(new Font("Calibri",Font.PLAIN,30));
        
        naslovPanel1.setBackground(Color.lightGray);
        naslovPanel2.setBackground(Color.lightGray);
        naslovPanel1.add(naslovLabel1);
        naslovPanel2.add(naslovLabel2);
        naslovPanel.setPreferredSize(new Dimension(2000,110));
        naslovPanel.add(naslovPanel1,BorderLayout.NORTH);
        naslovPanel.add(naslovPanel2,BorderLayout.SOUTH);
        
        this.add(naslovPanel,BorderLayout.NORTH);
        
        JPanel izlazakPanel=new JPanel();
        izlazak.setPreferredSize(new Dimension(200,50));
        izlazak.addActionListener(this);
        izlazakPanel.add(izlazak);
        submitglasove.addActionListener(this);
        submitglasove.setPreferredSize(new Dimension(200,50));
        izlazakPanel.add(submitglasove);
        this.add(izlazakPanel,BorderLayout.SOUTH);
        
        
        BoxLayout boxlayout = new BoxLayout(glasanjePanel, BoxLayout.Y_AXIS);
        glasanjePanel.setLayout(boxlayout);
        glasanjePanel.setBorder(new EmptyBorder(new Insets(100, 150, 100, 150)));
        sviproposali=AuthService.allProposals(trenutniuserid);
        int prviprolaz;

        for(int i=0;i<sviproposali.size();i++){
            
            String title=AuthService.getTitle(sviproposali.get(i));
            JLabel terminLabelNaslov=new JLabel();
            terminLabelNaslov.setText(title);
            JPanel terminPanel=new JPanel();
            terminPanel.add(terminLabelNaslov);
            terminPanel.setPreferredSize(new Dimension(2000,50));
            terminPanel.setBackground(Color.lightGray);
            prviprolaz=1;

            String sql = "SELECT * FROM proposal_slots WHERE proposal_id = ?";
            
            try (Connection conn = Db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1,sviproposali.get(i));
                
                try (ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        svezasubmit.add(0);
                        sviproposalizaglasat.add(sviproposali.get(i));
                        svislotovizaglasat.add(rs.getInt("slot_id"));
                        svitogglebuttoniselected.add(0);
                        if(prviprolaz==1){
                            
                            

                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            JLabel terminDatumLabel=new JLabel();
                            String dateToString=poc.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            terminDatumLabel.setText(dateToString);
                            terminPanel.add(terminDatumLabel);
                            
                            JButton terminButton=new JButton();
                            svitogglebuttonizaglasat.add(terminButton);
                            
                            terminButton.setText("%td.%tm. %tR-%tR".formatted(poc.toLocalDateTime(),poc.toLocalDateTime(),poc.toLocalDateTime(),kraj.toLocalDateTime()));

                            
                            terminButton.addActionListener(this);
                            terminButton.setBackground(Color.WHITE);
                            terminPanel.add(terminButton);
                            
                            prviprolaz=0;
                        }
                        else{

                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            
                            JButton terminButton=new JButton();
                            svitogglebuttonizaglasat.add(terminButton);
                            
                            terminButton.setText("%td.%tm. %tR-%tR".formatted(poc.toLocalDateTime(),poc.toLocalDateTime(),poc.toLocalDateTime(),kraj.toLocalDateTime()));
                            
                            
                            terminButton.addActionListener(this);
                            terminButton.setBackground(Color.WHITE);
                            terminPanel.add(terminButton);
                            
                            
                            prviprolaz=0;
                        }
                    }

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            glasanjePanel.add(terminPanel);        
            
        }

        this.add(glasanjePanel,BorderLayout.CENTER);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(1000,1000);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==izlazak){
            KalendarFrame noviKalendarFrame=new KalendarFrame(RasporedUserId,RasporedGodina,RasporedMjesec);
            noviKalendarFrame.setVisible(true);
            dispose();
        }
        if(e.getSource()==submitglasove){
            int jelvotepostoji;
            int jelistauneseno=0;
            try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword());
             Statement st = conn.createStatement()) {
                for(int i=0;i<svitogglebuttoniselected.size();i++){
                    jelvotepostoji=AuthService.isVoteSubmitted(sviproposalizaglasat.get(i),svislotovizaglasat.get(i),RasporedUserId);
                    if(svitogglebuttoniselected.get(i)==1 && jelvotepostoji==0){
                        System.out.println(sviproposalizaglasat.get(i)+" "+svislotovizaglasat.get(i)+" "+RasporedUserId);
                        st.execute(seedVotes(sviproposalizaglasat.get(i),svislotovizaglasat.get(i),RasporedUserId));
                        jelistauneseno=1;
                    }
                }
                } catch (Exception error) {
                error.printStackTrace();
            }
            if(jelistauneseno==0){
                JOptionPane.showMessageDialog(this, "Nema nikakvih glasova za unijeti");
            }
            else{
                JOptionPane.showMessageDialog(this, "Uspješno glasanje!");
                new KalendarFrame(RasporedUserId,RasporedGodina,RasporedMjesec).setVisible(true);
                dispose();
            }
            
        }
        for(int i=0;i<svitogglebuttonizaglasat.size();i++){
            if(e.getSource()==svitogglebuttonizaglasat.get(i)){
                if(svitogglebuttoniselected.get(i)==0){
                    svitogglebuttoniselected.set(i,1);
                    svezasubmit.set(i,1);
                    svitogglebuttonizaglasat.get(i).setBackground(Color.GRAY);
                    System.out.println("Postavio sam gumb "+i+" na 1");
                }
                else{
                    svitogglebuttoniselected.set(i,0);
                    svezasubmit.set(i,0);
                    svitogglebuttonizaglasat.get(i).setBackground(Color.WHITE);
                    System.out.println("Postavio sam gumb "+i+" na 0");
                }
           
            }
        }
        
    }
    
    
}
