/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
/**
 *
 * @author Vid
 */
public class RasporedFrame  extends JFrame implements ActionListener{
    private int RasporedDan;
    private int RasporedMjesec;
    private int RasporedGodina;
    int RasporedUserId;
    JPanel rasporedPanel;
    
    JButton dodaj;
    //JButton premjesti;
    JButton makni;
    JButton izađi;
    void updateRasporedPanel() {
        rasporedPanel.removeAll();
        try (Connection conn = Db.getConnection();
             PreparedStatement st = conn.prepareStatement("""
                                                          SELECT * FROM events WHERE
                                                          start_time>=? AND end_time<? AND event_id IN (
                                                          SELECT event_id FROM event_participants WHERE user_id=?)
                                                          ORDER BY start_time
                                                          """)){
            GregorianCalendar startCalendar=new GregorianCalendar(RasporedGodina,RasporedMjesec-1,RasporedDan),
                    endCalendar=new GregorianCalendar(RasporedGodina,RasporedMjesec-1,RasporedDan);
            endCalendar.add(GregorianCalendar.DAY_OF_YEAR,1);
            java.sql.Timestamp startTime=new java.sql.Timestamp(startCalendar.getTime().getTime()),
                    endTime=new java.sql.Timestamp(endCalendar.getTime().getTime());
            st.setTimestamp(1,startTime);
            st.setTimestamp(2,endTime);
            st.setInt(3,RasporedUserId);
            ResultSet res=st.executeQuery();
            while (res.next()) {
                JLabel opisEventa=new JLabel();
                String ime=res.getString("title");
                java.sql.Timestamp poc=res.getTimestamp("start_time"),kraj=res.getTimestamp("end_time");
                opisEventa.setText("%s: %tR - %tR".formatted(ime,poc.toLocalDateTime(),kraj.toLocalDateTime()));
                opisEventa.setFont(new Font("Calibri",Font.PLAIN,20));
                rasporedPanel.add(opisEventa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    RasporedFrame(int userid, int dan, int mjesec, int godina){
        RasporedUserId=userid;
        RasporedDan=dan;
        RasporedMjesec=mjesec;
        RasporedGodina=godina;
        
        //Dodat naslov
        String datum=dan + "." + mjesec + "." + godina;
        JLabel naslov=new JLabel();
        naslov.setText(datum);
        naslov.setFont(new Font("Calibri",Font.PLAIN,50));
        JPanel naslovPanel=new JPanel();
        naslovPanel.add(naslov);
        naslovPanel.setBackground(new Color(200,200,200));
        naslovPanel.setPreferredSize(new Dimension(2000,75));
        
        //Dodani gumbi za dodat, premjestit i maknut sastanak
        dodaj=new JButton();
        dodaj.setText("Dodaj sastanak");
        dodaj.setFont(new Font("Calibri",Font.PLAIN,20));
        dodaj.setMargin(new Insets(0, 0, 0, 0));
        dodaj.addActionListener(this);
        
        /*premjesti=new JButton();
        premjesti.setText("Premjesti sastanak");
        premjesti.setFont(new Font("Calibri",Font.PLAIN,20));
        premjesti.setMargin(new Insets(0, 0, 0, 0));*/

        makni=new JButton();
        makni.setText("Makni sastanak");
        makni.setFont(new Font("Calibri",Font.PLAIN,20));
        makni.setMargin(new Insets(0, 0, 0, 0));
        makni.addActionListener(this);
        
        izađi=new JButton();
        izađi.setText("Vrati se na kalendar");
        izađi.setFont(new Font("Calibri",Font.PLAIN,20));
        izađi.setMargin(new Insets(0, 0, 0, 0));
        izađi.addActionListener(this);
        
        dodaj.setPreferredSize(new Dimension(180,80));
        //premjesti.setPreferredSize(new Dimension(180,80));
        makni.setPreferredSize(new Dimension(180,80));
        izađi.setPreferredSize(new Dimension(180,80));
        
        JPanel gumbiPanel=new JPanel();
        gumbiPanel.setPreferredSize(new Dimension(200,1000));

        gumbiPanel.setBackground(new Color(100,100,100));
        gumbiPanel.add(dodaj);
        //gumbiPanel.add(premjesti);
        gumbiPanel.add(makni);
        gumbiPanel.add(izađi);
        
        rasporedPanel=new JPanel();
        rasporedPanel.setBackground(new Color(220,220,220));
        rasporedPanel.setPreferredSize(new Dimension(200,200));
        rasporedPanel.setLayout(new BoxLayout(rasporedPanel, BoxLayout.Y_AXIS));
        updateRasporedPanel();
        JPanel rasporedPanel2=new JPanel();
        rasporedPanel2.setBackground(new Color(240,240,240));
        rasporedPanel2.setPreferredSize(new Dimension(200,50));
        
        this.add(naslovPanel,BorderLayout.NORTH);
        this.add(rasporedPanel2,BorderLayout.SOUTH);
        this.add(gumbiPanel,BorderLayout.EAST);
        this.add(rasporedPanel,BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(1000,500);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==izađi){
            KalendarFrame noviKalendarFrame=new KalendarFrame(RasporedUserId,RasporedGodina,RasporedMjesec-1);
            noviKalendarFrame.setVisible(true);
            dispose();
        }
        if (e.getSource()==dodaj){
            DodajEventPanel dodajPanel=new DodajEventPanel(RasporedUserId,RasporedDan,RasporedMjesec,RasporedGodina);
            JOptionPane pane=new JOptionPane(dodajPanel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
            JDialog dialog=pane.createDialog(null,"Dodavanje eventa");
            dialog.setResizable(true);
            dialog.setVisible(true);
            Object selectedValue=pane.getValue();
            int intValue;
            if (selectedValue==null) intValue=JOptionPane.CLOSED_OPTION;
            else intValue=Integer.parseInt(selectedValue.toString());
            //int result=JOptionPane.showConfirmDialog(null,dodajPanel,"Dodavanje eventa",JOptionPane.OK_CANCEL_OPTION);
            if (intValue == JOptionPane.OK_OPTION) {
                if (dodajPanel.groupBox.getSelectedItem().toString().equals("Samostalni") && 
                        dodajPanel.glasanjeCheckbox.isSelected()) {
                    JOptionPane.showMessageDialog(null,
                        "Ne može se napraviti glasanje za samostalni event.",
                        "Greška pri dodavanju eventa",
                        JOptionPane.ERROR_MESSAGE);
                } 
                else if (dodajPanel.isTimeValid()) {
                    dodajPanel.addToDb();
                    updateRasporedPanel();
                    rasporedPanel.revalidate();
                    rasporedPanel.repaint();
                }
                else {
                    JOptionPane.showMessageDialog(null,
                        "Vrijeme koje ste odabrali je već zauzeto.",
                        "Greška pri dodavanju eventa",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        if (e.getSource()==makni) {
            ArrayList<String> labelTexts=new ArrayList<String>();
            Component[] labels=rasporedPanel.getComponents();
            for (int i=0;i<labels.length;++i) {
                labelTexts.add(((JLabel)labels[i]).getText());
            }
            if (labelTexts.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Nema eventova za brisati.",
                    "Greška pri micanju eventa",
                    JOptionPane.ERROR_MESSAGE);
            }
            else {
                Object[] options=labelTexts.toArray();
                String chosenOption=(String)JOptionPane.showInputDialog(
                    null,
                    "Koji event želite maknuti?",
                    "Micanje eventa",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
                if (chosenOption!=null && chosenOption.length()>0) {
                    try (Connection conn = Db.getConnection()) {
                        PreparedStatement st = conn.prepareStatement("""
                                                          SELECT * FROM events WHERE
                                                          start_time>=? AND end_time<? AND event_id IN (
                                                          SELECT event_id FROM event_participants WHERE user_id=?)
                                                          ORDER BY start_time
                                                          """);
                        GregorianCalendar startCalendar=new GregorianCalendar(RasporedGodina,RasporedMjesec-1,RasporedDan),
                                endCalendar=new GregorianCalendar(RasporedGodina,RasporedMjesec-1,RasporedDan);
                        endCalendar.add(GregorianCalendar.DAY_OF_YEAR,1);
                        java.sql.Timestamp startTime=new java.sql.Timestamp(startCalendar.getTime().getTime()),
                                endTime=new java.sql.Timestamp(endCalendar.getTime().getTime());
                        st.setTimestamp(1,startTime);
                        st.setTimestamp(2,endTime);
                        st.setInt(3,RasporedUserId);
                        ResultSet res=st.executeQuery();
                        while (res.next()) {
                            String ime=res.getString("title");
                            java.sql.Timestamp poc=res.getTimestamp("start_time"),kraj=res.getTimestamp("end_time");
                            String curLabel="%s: %tR - %tR".formatted(ime,poc.toLocalDateTime(),kraj.toLocalDateTime());
                            if (curLabel.equals(chosenOption)) {
                                int eventId=res.getInt("event_id");
                                PreparedStatement eventsDelete = conn.prepareStatement("DELETE FROM events WHERE event_id=?");
                                PreparedStatement participantsDelete = conn.prepareStatement("DELETE FROM event_participants WHERE event_id=?");
                                eventsDelete.setInt(1,eventId);
                                participantsDelete.setInt(1,eventId);
                                eventsDelete.executeUpdate();
                                participantsDelete.executeUpdate();
                                updateRasporedPanel();
                                rasporedPanel.revalidate();
                                rasporedPanel.repaint();
                            }
                        }
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
        }
    
    }
}
