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
import java.util.GregorianCalendar;
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
    JTextField hoursStartField,minutesStartField,durationField,titleField;
    JPanel pocetakPanel,pocetakInputPanel,grupaINaslovPanel;
    JLabel vremenaLabel;
    int dan,mjesec,godina;
    boolean isTimeValid() {
        String trenGrupa=groupBox.getSelectedItem().toString();
        int trajanje;
        try {
            trajanje=Integer.parseInt(durationField.getText());
        } catch (NumberFormatException e) {
            trajanje=0;
        }
        ArrayList<Interval> goodIntervals=getAvailableTimes(trenGrupa,trajanje);
        int sati;
        try {
            sati=Integer.parseInt(hoursStartField.getText());
        } catch (NumberFormatException e) {
            sati=0;
        }
        int minute;
        try {
            minute=Integer.parseInt(minutesStartField.getText());
        } catch (NumberFormatException e) {
            minute=0;
        }
        int x=sati*60+minute;
        for (int i=0;i<goodIntervals.size();++i) {
            if (goodIntervals.get(i).contains(x))
                return true;
        }
        return false;
    }
    void addToDb() {
        try (Connection conn = Db.getConnection()) {
            String trenGrupa=groupBox.getSelectedItem().toString();
            String naslov=titleField.getText();
            int trajanje;
            try {
                trajanje=Integer.parseInt(durationField.getText());
            } catch (NumberFormatException e) {
                trajanje=0;
            }
            int sati;
            try {
                sati=Integer.parseInt(hoursStartField.getText());
            } catch (NumberFormatException e) {
                sati=0;
            }
            int minute;
            try {
                minute=Integer.parseInt(minutesStartField.getText());
            } catch (NumberFormatException e) {
                minute=0;
            }
            GregorianCalendar startCalendar=new GregorianCalendar(godina,mjesec-1,dan);
            long startDana=startCalendar.getTime().getTime();
            java.sql.Timestamp startTimestamp=new java.sql.Timestamp(startDana+(sati*60+minute)*60000),
                    endTimestamp=new java.sql.Timestamp(startDana+(sati*60+minute+trajanje)*60000);
            
            PreparedStatement stEvents,stParticipants;
            if (trenGrupa=="Samostalni") {
                stEvents=conn.prepareStatement("""
                                         INSERT INTO events (created_by,title,start_time,end_time)
                                         VALUES (?,?,?,?)""");
                stEvents.setInt(1,userId);
                stEvents.setString(2,naslov);
                stEvents.setTimestamp(3,startTimestamp);
                stEvents.setTimestamp(4,endTimestamp);
                stEvents.executeUpdate();
                stParticipants=conn.prepareStatement("""
                                                     INSERT INTO event_participants 
                                                     (SELECT event_id,? FROM events WHERE
                                                     created_by=? AND title=? AND start_time=? AND end_time=?)""");
                stParticipants.setInt(1,userId);
                stParticipants.setInt(2,userId);
                stParticipants.setString(3,naslov);
                stParticipants.setTimestamp(4,startTimestamp);
                stParticipants.setTimestamp(5,endTimestamp);
                stParticipants.executeUpdate();
                
            } else {
                PreparedStatement zaIdGrupe=conn.prepareStatement("SELECT group_id FROM groups WHERE name=?");
                zaIdGrupe.setString(1,trenGrupa);
                ResultSet resGrupa=zaIdGrupe.executeQuery();
                resGrupa.next();
                int groupId=resGrupa.getInt("group_id");
                System.out.println("Nađen group id: ");
                System.out.println(groupId);
                
                stEvents=conn.prepareStatement("""
                                         INSERT INTO events (group_id,created_by,title,start_time,end_time)
                                         VALUES (?,?,?,?,?)""");
                stEvents.setInt(1,groupId);
                stEvents.setInt(2,userId);
                stEvents.setString(3,naslov);
                stEvents.setTimestamp(4,startTimestamp);
                stEvents.setTimestamp(5,endTimestamp);
                stEvents.executeUpdate();
                stParticipants=conn.prepareStatement("""
                                                     INSERT INTO event_participants 
                                                     (SELECT ev.event_id,gr.user_id FROM events ev, group_members gr WHERE
                                                     gr.group_id=? AND ev.created_by=? AND ev.title=? AND ev.start_time=? AND ev.end_time=?)""");
                stParticipants.setInt(1,groupId);
                stParticipants.setInt(2,userId);
                stParticipants.setString(3,naslov);
                stParticipants.setTimestamp(4,startTimestamp);
                stParticipants.setTimestamp(5,endTimestamp);
                stParticipants.executeUpdate();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private ArrayList<Interval> getAvailableTimes(String grupa,int trajanje) {
        ArrayList<Interval> res=new ArrayList<>();
        try (Connection conn = Db.getConnection()) {
            PreparedStatement getEventTimes;
            if (grupa.equals("Samostalni")) {
                getEventTimes=conn.prepareStatement("""
                                                    SELECT start_time,end_time FROM events e1 WHERE e1.event_id IN (
                                                    SELECT event_id FROM event_participants e2 WHERE e2.user_id=?)""");
                getEventTimes.setInt(1,userId);
            }
            else {
                getEventTimes=conn.prepareStatement("""
                                                    SELECT start_time,end_time FROM events e1 WHERE e1.event_id IN (
                                                    SELECT event_id FROM event_participants e2 WHERE e2.user_id IN (
                                                    SELECT user_id FROM group_members g1 WHERE g1.group_id IN (
                                                    SELECT group_id FROM groups g2 WHERE g2.name=?)))""");
                getEventTimes.setString(1,grupa);
            }
            ResultSet eventTimesResults=getEventTimes.executeQuery();
            ArrayList<Interval> nesmije=new ArrayList<>();
            long pocetakDana=new GregorianCalendar(godina,mjesec-1,dan).getTime().getTime();
            while (eventTimesResults.next()) {
                java.sql.Timestamp poc=eventTimesResults.getTimestamp("start_time"),kraj=eventTimesResults.getTimestamp("end_time");
                long pocmin=(poc.getTime()-pocetakDana)/60000;
                long krajmin=(kraj.getTime()-pocetakDana)/60000;
                if (krajmin<=0 || pocmin>=1440) continue;
                if (pocmin<=0) pocmin=0;
                if (krajmin>=1440) krajmin=1440;
                nesmije.add(new Interval((int)(pocmin-trajanje),(int)krajmin));
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
    DodajEventPanel(int userid,int trdan,int trmjesec,int trgodina) {
        userId=userid;
        dan=trdan;
        mjesec=trmjesec;
        godina=trgodina;
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
        titleField=new JTextField(20);
        JPanel naslovPanel=new JPanel();
        naslovPanel.add(new JLabel("Naslov: "));
        naslovPanel.add(titleField);
        grupaINaslovPanel=new JPanel();
        grupaINaslovPanel.setLayout(new BoxLayout(grupaINaslovPanel, BoxLayout.Y_AXIS));
        grupaINaslovPanel.add(grupaPanel,BorderLayout.NORTH);
        grupaINaslovPanel.add(naslovPanel,BorderLayout.SOUTH);
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
        this.add(grupaINaslovPanel,BorderLayout.NORTH);
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
        int trajanje;
        try {
            trajanje=Integer.parseInt(durationField.getText());
        } catch (NumberFormatException e) {
            trajanje=0;
        }
        System.out.println(trajanje);
        ArrayList<Interval> allowedIntervals=getAvailableTimes(trenGrupa,trajanje);
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
