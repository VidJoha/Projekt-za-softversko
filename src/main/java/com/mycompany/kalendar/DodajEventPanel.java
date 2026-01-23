/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author dorij
 */

class Interval {
    int start,end;
    Interval(int st,int en) {
        start=st;
        end=en;
    }
    boolean contains(int x) {
        return x>=start && x<end;
    }
    boolean intersects(Interval x) {
        return start<x.end && x.start<end;
    }
}


public class DodajEventPanel extends JPanel implements ActionListener {
    int userId;
    JComboBox<String> groupBox;
    JTextField hoursStartField,minutesStartField,durationField;
    JPanel pocetakPanel,pocetakInputPanel;
    JLabel vremenaLabel;
    private ArrayList<Interval> getAvailableTimes(String grupa,int trajanje) {
        ArrayList<Interval> res=new ArrayList<>();
        try (Connection conn = Db.getConnection()) {
            PreparedStatement getEventTimes;
            if (grupa.equals("Samostalni")) {
                getEventTimes=conn.prepareStatement("""
                                                    SELECT start_time,end_time FROM events WHERE group_id IN (
                                                    SELECT group_id FROM group_members g1 WHERE g1.user_id=?)""");
                getEventTimes.setInt(1,userId);
            }
            else {
                getEventTimes=conn.prepareStatement("""
                                                    SELECT start_time,end_time FROM events WHERE group_id IN (
                                                    SELECT group_id FROM group_members g1 WHERE g1.user_id IN (
                                                    SELECT user_id FROM group_members g2 WHERE g2.group_id IN (
                                                    SELECT group_id FROM groups g3 WHERE g3.name=?)))""");
                getEventTimes.setString(1,grupa);
            }
            ResultSet eventTimesResults=getEventTimes.executeQuery();
            ArrayList<Interval> nesmije=new ArrayList<>();
            while (eventTimesResults.next()) {
                java.sql.Timestamp poc=eventTimesResults.getTimestamp("start_time"),kraj=eventTimesResults.getTimestamp("end_time");
                int pocmin=poc.toLocalDateTime().getHour()*60+poc.toLocalDateTime().getMinute();
                int krajmin=kraj.toLocalDateTime().getHour()*60+kraj.toLocalDateTime().getMinute();
                nesmije.add(new Interval(pocmin-trajanje,krajmin));
            }
            int intervalStart=0;
            for (int meetingStart=0;meetingStart<1440;++meetingStart) {
                boolean available=true;
                for (int i=0;i<nesmije.size();++i) {
                    if (nesmije.get(i).contains(meetingStart))
                        available=false;
                }
                if (!available) {
                    if (intervalStart!=meetingStart) {
                        res.add(new Interval(intervalStart,meetingStart));
                    }
                    intervalStart=meetingStart+1;
                }
            }
            if (intervalStart!=1440) {
                res.add(new Interval(intervalStart,1439));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    DodajEventPanel(int userid,int dan,int mjesec,int godina) {
        userId=userid;
        JPanel grupaPanel=new JPanel();
        grupaPanel.add(new JLabel("Grupa: "));
        groupBox=new JComboBox<String>();
        groupBox.addItem("Samostalni");
        try (Connection conn = Db.getConnection()) {
            PreparedStatement getGroups=conn.prepareStatement("""
                                                              SELECT name FROM groups WHERE group_id IN (
                                                              SELECT group_id FROM group_members WHERE user_id=?)""");
            getGroups.setInt(1,userid);
            ResultSet groupNameResults=getGroups.executeQuery();
            while (groupNameResults.next()) {
                groupBox.addItem(groupNameResults.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        grupaPanel.add(groupBox);
        JPanel trajanjePanel=new JPanel();
        trajanjePanel.add(new JLabel("Trajanje: "));
        durationField=new JTextField("0",3);
        trajanjePanel.add(durationField);
        trajanjePanel.add(new JLabel(" minuta"));
        ArrayList<Interval> allowedIntervals=getAvailableTimes("Samostalni",0);
        if (!allowedIntervals.isEmpty()) {
            hoursStartField=new JTextField(Integer.toString(allowedIntervals.get(0).start/60),2);
            minutesStartField=new JTextField(Integer.toString(allowedIntervals.get(0).start%60),2);
        } else {
            hoursStartField=new JTextField(2);
            minutesStartField=new JTextField(2);
        }
        pocetakPanel=new JPanel();
        pocetakInputPanel=new JPanel();
        pocetakInputPanel.add(new JLabel("Početak eventa:"));
        pocetakInputPanel.add(hoursStartField);
        pocetakInputPanel.add(new JLabel(":"));
        pocetakInputPanel.add(minutesStartField);
        String vremena;
        if (allowedIntervals.isEmpty()) vremena="Nema mogućih vremena za event!";
        else vremena="Mogući početci eventa: ";
        for (int i=0;i<allowedIntervals.size();++i) {
            int curStart=allowedIntervals.get(i).start,curEnd=allowedIntervals.get(i).end;
            vremena+=Integer.toString(curStart/600);
            vremena+=Integer.toString((curStart/60)%10);
            vremena+=":";
            vremena+=Integer.toString((curStart/10)%6);
            vremena+=Integer.toString(curStart%10);
            vremena+="-";
            vremena+=Integer.toString(curEnd/600);
            vremena+=Integer.toString((curEnd/60)%10);
            vremena+=":";
            vremena+=Integer.toString((curEnd/10)%6);
            vremena+=Integer.toString(curEnd%10);
            if (i<allowedIntervals.size()-1) vremena+=", ";
        }
        vremenaLabel=new JLabel(vremena);
        pocetakPanel.setLayout(new BoxLayout(pocetakPanel, BoxLayout.Y_AXIS));
        pocetakPanel.add(pocetakInputPanel,BorderLayout.NORTH);
        pocetakPanel.add(vremenaLabel,BorderLayout.SOUTH);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(grupaPanel,BorderLayout.NORTH);
        this.add(trajanjePanel,BorderLayout.CENTER);
        this.add(pocetakPanel,BorderLayout.SOUTH);
        groupBox.addActionListener(this);
        durationField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateValues();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateValues();
            }
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateValues();
            }
        });
    }
    private void updateValues() {
        String trenGrupa=groupBox.getSelectedItem().toString();
        int trajanje=Integer.parseInt(durationField.getText());
        System.out.println(trajanje);
        ArrayList<Interval> allowedIntervals=getAvailableTimes("Samostalni",trajanje);
        if (!allowedIntervals.isEmpty()) {
            hoursStartField.setText(Integer.toString(allowedIntervals.get(0).start/60));
            minutesStartField.setText(Integer.toString(allowedIntervals.get(0).start%60));
        } else {
            hoursStartField.setText("");
            minutesStartField.setText("");
        }
        String vremena;
        if (allowedIntervals.isEmpty()) vremena="Nema mogućih vremena za event!";
        else vremena="Mogući početci eventa: ";
        for (int i=0;i<allowedIntervals.size();++i) {
            int curStart=allowedIntervals.get(i).start,curEnd=allowedIntervals.get(i).end;
            vremena+=Integer.toString(curStart/600);
            vremena+=Integer.toString((curStart/60)%10);
            vremena+=":";
            vremena+=Integer.toString((curStart/10)%6);
            vremena+=Integer.toString(curStart%10);
            vremena+="-";
            vremena+=Integer.toString(curEnd/600);
            vremena+=Integer.toString((curEnd/60)%10);
            vremena+=":";
            vremena+=Integer.toString((curEnd/10)%6);
            vremena+=Integer.toString(curEnd%10);
            if (i<allowedIntervals.size()-1) vremena+=", ";
        }
        vremenaLabel.setText(vremena);
        System.out.println(vremena);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        updateValues();
    }
}
