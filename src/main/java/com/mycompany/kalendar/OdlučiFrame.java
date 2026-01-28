/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class OdlučiFrame extends JFrame implements ActionListener{
    private final int RasporedMjesec;
    private final int RasporedGodina;
    int RasporedUserId;
    
    ArrayList<Integer> sviproposali;
    ArrayList<JButton> svitogglebuttonizaodabrat;
    ArrayList<Integer> sviproposalizaodabrat;
    ArrayList<Integer> svislotovizaodabrat;
    JPanel odlučivanjePanel;
    
    JButton izlazak=new JButton("Vrati se na kalendar");

    OdlučiFrame(int trenutniuserid,int trenutnimjesec,int trenutnagodina){
        RasporedUserId=trenutniuserid;
        RasporedGodina=trenutnagodina;
        RasporedMjesec=trenutnimjesec;
        sviproposali=AuthService.allProposalsWhereOwner(RasporedUserId);
        svitogglebuttonizaodabrat=new ArrayList<>();
        sviproposalizaodabrat=new ArrayList<>();
        svislotovizaodabrat=new ArrayList<>();

        odlučivanjePanel=new JPanel();
        BoxLayout boxlayout = new BoxLayout(odlučivanjePanel, BoxLayout.Y_AXIS);
        odlučivanjePanel.setLayout(boxlayout);
        odlučivanjePanel.setBackground(Color.lightGray);
        odlučivanjePanel.setBorder(new EmptyBorder(new Insets(50, 100, 50, 100)));
        
        JPanel naslovPanel=new JPanel();
        JPanel naslovPanel1=new JPanel();
        JPanel naslovPanel2=new JPanel();
        JLabel naslovLabel1=new JLabel();
        JLabel naslovLabel2=new JLabel();
        String line1="Ovo su eventi za koje možeš odlučiti termin";
        String line2="Klikni na termin koji želiš za taj event";
        naslovLabel1.setText(line1);
        naslovLabel2.setText(line2);
        naslovLabel1.setFont(new Font("Calibri",Font.PLAIN,30));
        naslovLabel2.setFont(new Font("Calibri",Font.PLAIN,30));
        
        naslovLabel1.setBackground(Color.lightGray);
        naslovLabel2.setBackground(Color.lightGray);
        naslovLabel1.setOpaque(true);
        naslovLabel2.setOpaque(true);
        naslovLabel1.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        naslovLabel2.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        naslovPanel1.add(naslovLabel1);
        naslovPanel2.add(naslovLabel2);
        naslovPanel1.setPreferredSize(new Dimension(2000,50));
        naslovPanel2.setPreferredSize(new Dimension(2000,50));
        naslovPanel.setPreferredSize(new Dimension(2000,115));
        naslovPanel.add(naslovPanel1,BorderLayout.NORTH);
        naslovPanel.add(naslovPanel2,BorderLayout.SOUTH);
        this.add(naslovPanel,BorderLayout.NORTH);
        
        JPanel izlazakPanel=new JPanel();
        izlazak.setPreferredSize(new Dimension(200,50));
        izlazak.addActionListener(this);
        izlazakPanel.add(izlazak);
        this.add(izlazakPanel,BorderLayout.SOUTH);
        int prviprolaz;
        System.out.println(sviproposali);
        
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
                        sviproposalizaodabrat.add(sviproposali.get(i));
                        svislotovizaodabrat.add(rs.getInt("slot_id"));
                        
                        if(prviprolaz==1){

                            
                            
                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            JLabel terminDatumLabel=new JLabel();
                            String dateToString=poc.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            
                            int slot_id=rs.getInt("slot_id");
                            int brojglasova=AuthService.allVotes(sviproposali.get(i), slot_id);

                            JLabel brojglasovazaterminLabel=new JLabel("Broj glasova: "+Integer.toString(brojglasova));
                            
                            terminDatumLabel.setText(dateToString);
                            terminPanel.add(terminDatumLabel);
                            
                            JButton terminButton=new JButton();
                            svitogglebuttonizaodabrat.add(terminButton);
                            
                            terminButton.setText("%tR - %tR".formatted(poc.toLocalDateTime(),kraj.toLocalDateTime()));

                            
                            terminButton.addActionListener(this);
                            terminButton.setBackground(Color.WHITE);
                            terminPanel.add(terminButton);
                            terminPanel.add(brojglasovazaterminLabel);
                            
                            prviprolaz=0;
                        }
                        else{

                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            
                            int slot_id=rs.getInt("slot_id");
                            int brojglasova=AuthService.allVotes(sviproposali.get(i), slot_id);
                            JLabel brojglasovazaterminLabel=new JLabel("Broj glasova: "+Integer.toString(brojglasova));

                            JButton terminButton=new JButton();
                            svitogglebuttonizaodabrat.add(terminButton);
                            
                            terminButton.setText("%tR - %tR".formatted(poc.toLocalDateTime(),kraj.toLocalDateTime()));
                            
                            
                            terminButton.addActionListener(this);
                            terminButton.setBackground(Color.WHITE);
                            terminPanel.add(terminButton);
                            terminPanel.add(brojglasovazaterminLabel);
                            
                            
                            prviprolaz=0;
                        }
                    }

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            odlučivanjePanel.add(terminPanel);
        }

        
        
        
        
        this.add(odlučivanjePanel,BorderLayout.CENTER);
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
        for(int i=0;i<svitogglebuttonizaodabrat.size();i++){
            if(e.getSource()==svitogglebuttonizaodabrat.get(i)){
                int result=JOptionPane.showConfirmDialog(null,"Jeste li sigurni da želite izabrati ovaj termin?","Confirmation",JOptionPane.YES_NO_OPTION);
                if(result==JOptionPane.YES_OPTION){
                int grupa=AuthService.getGroupId(sviproposalizaodabrat.get(i));
                int created_by=AuthService.getCreatedBy(sviproposalizaodabrat.get(i));
                String title=AuthService.getTitle(sviproposalizaodabrat.get(i));
                String start_time = AuthService.getStartTime(svislotovizaodabrat.get(i));
                String end_time=AuthService.getEndTime(svislotovizaodabrat.get(i));
                
                System.out.println(grupa+" "+created_by+" "+title+" "+start_time+" "+AuthService.getEndTime(svislotovizaodabrat.get(i)));
                
                try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword());
             Statement st = conn.createStatement()) {
                st.execute(DbSeedTables.seedEvent(grupa,created_by,title,start_time,end_time,"CONFIREMD"));
                System.out.println("Odabran novi event");
                st.execute(DbSeedTables.removeProposals(sviproposalizaodabrat.get(i)));
                st.execute(DbSeedTables.removeProposalSlots(sviproposalizaodabrat.get(i)));
                st.execute(DbSeedTables.removeProposalParticipants(sviproposalizaodabrat.get(i)));
                st.execute(DbSeedTables.removeVotes(sviproposalizaodabrat.get(i)));
                System.out.println("Uspješno izbrisano sve što je trebalo");
                } catch (Exception error) {
                error.printStackTrace();
                }
                updateOdlučivanjePanel();
                odlučivanjePanel.revalidate();
                odlučivanjePanel.repaint();
                }
                
            }
        }
    }
    void updateOdlučivanjePanel(){
        odlučivanjePanel.removeAll();
        sviproposali=AuthService.allProposalsWhereOwner(RasporedUserId);
        svitogglebuttonizaodabrat=new ArrayList<>();
        sviproposalizaodabrat=new ArrayList<>();
        svislotovizaodabrat=new ArrayList<>();
        
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
                        sviproposalizaodabrat.add(sviproposali.get(i));
                        svislotovizaodabrat.add(rs.getInt("slot_id"));
                        
                        if(prviprolaz==1){

                            
                            
                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            JLabel terminDatumLabel=new JLabel();
                            String dateToString=poc.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            
                            int slot_id=rs.getInt("slot_id");
                            int brojglasova=AuthService.allVotes(sviproposali.get(i), slot_id);

                            JLabel brojglasovazaterminLabel=new JLabel("Broj glasova: "+Integer.toString(brojglasova));
                            
                            terminDatumLabel.setText(dateToString);
                            terminPanel.add(terminDatumLabel);
                            
                            JButton terminButton=new JButton();
                            svitogglebuttonizaodabrat.add(terminButton);
                            
                            terminButton.setText("%tR - %tR".formatted(poc.toLocalDateTime(),kraj.toLocalDateTime()));

                            
                            terminButton.addActionListener(this);
                            terminButton.setBackground(Color.WHITE);
                            terminPanel.add(terminButton);
                            terminPanel.add(brojglasovazaterminLabel);
                            
                            prviprolaz=0;
                        }
                        else{

                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            
                            int slot_id=rs.getInt("slot_id");
                            int brojglasova=AuthService.allVotes(sviproposali.get(i), slot_id);
                            JLabel brojglasovazaterminLabel=new JLabel("Broj glasova: "+Integer.toString(brojglasova));

                            JButton terminButton=new JButton();
                            svitogglebuttonizaodabrat.add(terminButton);
                            
                            terminButton.setText("%tR - %tR".formatted(poc.toLocalDateTime(),kraj.toLocalDateTime()));
                            
                            
                            terminButton.addActionListener(this);
                            terminButton.setBackground(Color.WHITE);
                            terminPanel.add(terminButton);
                            terminPanel.add(brojglasovazaterminLabel);
                            
                            
                            prviprolaz=0;
                        }
                    }

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            odlučivanjePanel.add(terminPanel);
        }
    }
}
