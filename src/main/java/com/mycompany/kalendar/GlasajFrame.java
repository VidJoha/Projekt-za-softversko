/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Vid
 */
public class GlasajFrame extends JFrame{
    
    ArrayList<Integer> sviproposali;
    ArrayList<JButton> buttonglasovi;
    JPanel glasanjePanel;
    
    GlasajFrame(int trenutniuserid,int trenutnimjesec,int trenutnagodina){
        JPanel naslovPanel=new JPanel();
        JLabel naslovLabel=new JLabel();
        naslovLabel.setText("Ovo su eventi za koje možeš glasati");
        naslovLabel.setFont(new Font("Calibri",Font.PLAIN,60));
        naslovPanel.setBackground(Color.WHITE);
        naslovPanel.add(naslovLabel,BorderLayout.NORTH);
        
        sviproposali=AuthService.allProposals(trenutniuserid);
        
        for(int i=0;i<sviproposali.size();i++){
            
            String title=AuthService.getTitle(sviproposali.get(i));
            JLabel terminLabelNaslov=new JLabel();
            terminLabelNaslov.setText(title);
            JPanel terminPanel=new JPanel();
            terminPanel.add(terminLabelNaslov);
            int prviprolaz=1;
            String sql = "SELECT * FROM proposal_slots WHERE proposal_id = ?";
            try (Connection conn = Db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1,sviproposali.get(i));

                try (ResultSet rs = ps.executeQuery()) {
                    while(rs.next()){
                        if(prviprolaz==1){
                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            JLabel terminDatumLabel=new JLabel();
                            String dateToString=poc.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            terminDatumLabel.setText(dateToString);
                            terminPanel.add(terminDatumLabel);
                            
                            JToggleButton terminButton=new JToggleButton();
                            
                            terminButton.setText("%tR - %tR".formatted(poc.toLocalDateTime(),kraj.toLocalDateTime()));
                            ItemListener itemListener = new ItemListener() {
                                @Override    
                                public void itemStateChanged(ItemEvent itemEvent){
                                    int state = itemEvent.getStateChange();
                                    if (state == ItemEvent.SELECTED) {
                                        System.out.println("Selected");
                                    }
                                    else {
                                        System.out.println("Deselected");
                                    }
                                }
                            };
                            terminButton.addItemListener(itemListener);
                            terminPanel.add(terminButton);
                            prviprolaz=0;
                        }
                        else{
                            java.sql.Timestamp poc=rs.getTimestamp("start_time"),kraj=rs.getTimestamp("end_time");
                            JToggleButton terminButton=new JToggleButton();
                            
                            terminButton.setText("%tR - %tR".formatted(poc.toLocalDateTime(),kraj.toLocalDateTime()));
                            ItemListener itemListener = new ItemListener() {
                                @Override    
                                public void itemStateChanged(ItemEvent itemEvent){
                                    int state = itemEvent.getStateChange();
                                    if (state == ItemEvent.SELECTED) {
                                        System.out.println("Selected");
                                    }
                                    else {
                                        System.out.println("Deselected");
                                    }
                                }
                            };
                            terminButton.addItemListener(itemListener);
                            terminPanel.add(terminButton);
                            prviprolaz=0;
                        }
                    }

                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            this.add(terminPanel);        
            
        }
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(1000,1000);
        this.setVisible(true);
    }
    
    
    
}
