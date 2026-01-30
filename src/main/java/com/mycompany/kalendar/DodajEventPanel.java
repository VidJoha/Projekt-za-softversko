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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
    JPanel pocetakPanel,pocetakInputPanel,grupaINaslovPanel,grupaPanel,pocetciPanel;
    ArrayList<JPanel> drugaVremena,drugaVremenaInput;
    ArrayList<JTextField> yearFields,monthFields,dayFields,hourFields,minuteFields;
    ArrayList<JLabel> vremenaLabels;
    JLabel vremenaLabel;
    JCheckBox glasanjeCheckbox;
    JButton dodajMoguciTermin;
    int dan,mjesec,godina;
    DocumentListener docListener;
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
        boolean isInGoodInterval=false;
        for (int i=0;i<goodIntervals.size();++i) {
            if (goodIntervals.get(i).contains(x)) isInGoodInterval=true;
        }
        if (!isInGoodInterval) return false;
        for (int i=0;i<drugaVremena.size();++i) {
            try {
                sati=Integer.parseInt(hourFields.get(i).getText());
            } catch (NumberFormatException e) {
                sati=0;
            }
            try {
                minute=Integer.parseInt(minuteFields.get(i).getText());
            } catch (NumberFormatException e) {
                minute=0;
            }
            x=sati*60+minute;
            ArrayList<Interval> curGoodIntervals=goodIntervals;
            int selectedYear=-1,selectedMonth=-1,selectedDay=-1;
            try {
                selectedYear=Integer.parseInt(yearFields.get(i).getText());
                selectedMonth=Integer.parseInt(monthFields.get(i).getText());
                selectedDay=Integer.parseInt(dayFields.get(i).getText());
                if (selectedYear!=godina || selectedMonth!=mjesec || selectedDay!=dan) {
                    curGoodIntervals=getAvailableTimes(trenGrupa,trajanje,selectedDay,selectedMonth,selectedYear);
                }
            } catch (NumberFormatException e) {
                curGoodIntervals=new ArrayList<Interval>();
            }
            isInGoodInterval=false;
            for (int j=0;j<curGoodIntervals.size();++j) {
                if (curGoodIntervals.get(j).contains(x)) isInGoodInterval=true;
            }
            if (!isInGoodInterval) return false;
        }
        return true;
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
            if (!glasanjeCheckbox.isSelected()) {
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
            }
            else {
                PreparedStatement stProposals,stParticipants,stSlots,stFinalize,zaIdGrupe,zaProposalId;
                
                zaIdGrupe=conn.prepareStatement("SELECT group_id FROM groups WHERE name=?");
                zaIdGrupe.setString(1,trenGrupa);
                ResultSet resGrupa=zaIdGrupe.executeQuery();
                resGrupa.next();
                int groupId=resGrupa.getInt("group_id");
                
                stProposals=conn.prepareStatement("""
                                                  INSERT INTO meeting_proposals (group_id,created_by,title,status)
                                                  VALUES (?,?,?,?)""");
                stProposals.setInt(1,groupId);
                stProposals.setInt(2,userId);
                stProposals.setString(3,naslov);
                stProposals.setString(4,"CREATION_IN_PROGRESS");
                stProposals.executeUpdate();
                
                zaProposalId=conn.prepareStatement("""
                                                   SELECT proposal_id FROM meeting_proposals WHERE
                                                   status=? AND created_by=?""");
                zaProposalId.setString(1,"CREATION_IN_PROGRESS");
                zaProposalId.setInt(2,userId);
                ResultSet resProposal=zaProposalId.executeQuery();
                resProposal.next();
                int proposalId=resProposal.getInt("proposal_id");
                
                stParticipants=conn.prepareStatement("""
                                                     INSERT INTO proposal_participants
                                                     (SELECT ?,user_id FROM group_members gr WHERE gr.group_id=?)""");
                stParticipants.setInt(1,proposalId);
                stParticipants.setInt(2,groupId);
                stParticipants.executeUpdate();
                
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
                stSlots=conn.prepareStatement("""
                                              INSERT INTO proposal_slots (proposal_id,start_time,end_time)
                                              VALUES (?,?,?)""");
                stSlots.setInt(1,proposalId);
                stSlots.setTimestamp(2,startTimestamp);
                stSlots.setTimestamp(3,endTimestamp);
                stSlots.executeUpdate();
                for (int i=0;i<drugaVremena.size();++i) {
                    try {
                        sati=Integer.parseInt(hourFields.get(i).getText());
                    } catch (NumberFormatException e) {
                        sati=0;
                    }
                    try {
                        minute=Integer.parseInt(minuteFields.get(i).getText());
                    } catch (NumberFormatException e) {
                        minute=0;
                    }
                    int selectedYear=Integer.parseInt(yearFields.get(i).getText()),
                            selectedMonth=Integer.parseInt(monthFields.get(i).getText()),
                            selectedDay=Integer.parseInt(dayFields.get(i).getText());
                    startCalendar=new GregorianCalendar(selectedYear,selectedMonth-1,selectedDay);
                    startDana=startCalendar.getTime().getTime();
                    startTimestamp=new java.sql.Timestamp(startDana+(sati*60+minute)*60000);
                    endTimestamp=new java.sql.Timestamp(startDana+(sati*60+minute+trajanje)*60000);
                    PreparedStatement stOtherSlots=conn.prepareStatement("""
                            INSERT INTO proposal_slots (proposal_id,start_time,end_time)
                            VALUES (?,?,?)""");
                    stOtherSlots.setInt(1,proposalId);
                    stOtherSlots.setTimestamp(2,startTimestamp);
                    stOtherSlots.setTimestamp(3,endTimestamp);
                    stOtherSlots.executeUpdate();
                }
                stFinalize=conn.prepareStatement("""
                                                 UPDATE meeting_proposals SET status=? WHERE proposal_id=?""");
                stFinalize.setString(1,"VOTING");
                stFinalize.setInt(2,proposalId);
                stFinalize.executeUpdate();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
    private ArrayList<Interval> getAvailableTimes(String grupa,int trajanje,int trdan,int trmjesec,int trgodina) {
        ArrayList<Interval> res=new ArrayList<>();
        try (Connection conn = Db.getConnection()) {
            PreparedStatement getEventTimes,getProposalTimes;
            if (grupa.equals("Samostalni")) {
                getEventTimes=conn.prepareStatement("""
                                                    SELECT start_time,end_time FROM events e1 WHERE e1.event_id IN (
                                                    SELECT event_id FROM event_participants e2 WHERE e2.user_id=?)""");
                getEventTimes.setInt(1,userId);
                getProposalTimes=conn.prepareStatement("""
                                                      SELECT slot.start_time,slot.end_time
                                                      FROM proposal_slots slot,proposal_participants parts
                                                      WHERE slot.proposal_id=parts.proposal_id AND parts.user_id=?""");
                getProposalTimes.setInt(1,userId);
            }
            else {
                getEventTimes=conn.prepareStatement("""
                                                    SELECT start_time,end_time FROM events e1 WHERE e1.event_id IN (
                                                    SELECT event_id FROM event_participants e2 WHERE e2.user_id IN (
                                                    SELECT user_id FROM group_members g1 WHERE g1.group_id IN (
                                                    SELECT group_id FROM groups g2 WHERE g2.name=?)))""");
                getEventTimes.setString(1,grupa);
                getProposalTimes=conn.prepareStatement("""
                                                       SELECT slot.start_time,slot.end_time
                                                       FROM proposal_slots slot,proposal_participants parts
                                                       WHERE slot.proposal_id=parts.proposal_id AND parts.user_id IN (
                                                       SELECT user_id FROM group_members g1 WHERE g1.group_id IN (
                                                       SELECT group_id FROM groups g2 WHERE g2.name=?))""");
                getProposalTimes.setString(1,grupa);
            }
            ResultSet eventTimesResults=getEventTimes.executeQuery(),proposalTimesResults=getProposalTimes.executeQuery();
            ArrayList<Interval> nesmije=new ArrayList<>();
            long pocetakDana=new GregorianCalendar(trgodina,trmjesec-1,trdan).getTime().getTime();
            while (eventTimesResults.next()) {
                java.sql.Timestamp poc=eventTimesResults.getTimestamp("start_time"),kraj=eventTimesResults.getTimestamp("end_time");
                long pocmin=(poc.getTime()-pocetakDana)/60000-trajanje;
                long krajmin=(kraj.getTime()-pocetakDana)/60000;
                if (krajmin<=0 || pocmin>=1440) continue;
                if (pocmin<=0) pocmin=0;
                if (krajmin>=1440) krajmin=1440;
                nesmije.add(new Interval((int)(pocmin),(int)krajmin));
            }
            while (proposalTimesResults.next()) {
                java.sql.Timestamp poc=proposalTimesResults.getTimestamp("start_time"),kraj=proposalTimesResults.getTimestamp("end_time");
                long pocmin=(poc.getTime()-pocetakDana)/60000-trajanje;
                long krajmin=(kraj.getTime()-pocetakDana)/60000;
                if (krajmin<=0 || pocmin>=1440) continue;
                if (pocmin<=0) pocmin=0;
                if (krajmin>=1440) krajmin=1440;
                nesmije.add(new Interval((int)(pocmin),(int)krajmin));
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
    private ArrayList<Interval> getAvailableTimes(String grupa,int trajanje) {
        return getAvailableTimes(grupa,trajanje,dan,mjesec,godina);
    }
    private String timesToLabel(ArrayList<Interval> times) {
        String vremena;
        if (times.isEmpty()) vremena="Nema mogućih vremena za event!";
        else vremena="Mogući početci eventa: ";
        for (int i=0;i<times.size();++i) {
            int curStart=times.get(i).start,curEnd=times.get(i).end;
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
            if (i<times.size()-1) vremena+=", ";
        }
        return vremena;
    }
    DodajEventPanel(int userid,int trdan,int trmjesec,int trgodina) {
        userId=userid;
        dan=trdan;
        mjesec=trmjesec;
        godina=trgodina;
        drugaVremena=new ArrayList<JPanel>();
        drugaVremenaInput=new ArrayList<JPanel>();
        yearFields=new ArrayList<JTextField>();
        monthFields=new ArrayList<JTextField>();
        dayFields=new ArrayList<JTextField>();
        hourFields=new ArrayList<JTextField>();
        minuteFields=new ArrayList<JTextField>();
        vremenaLabels=new ArrayList<JLabel>();
        grupaPanel=new JPanel();
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
        glasanjeCheckbox=new JCheckBox("Glasanje za termin");
        grupaPanel.add(glasanjeCheckbox);
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
        dodajMoguciTermin=new JButton("Dodaj termin za glasati");
        dodajMoguciTermin.setVisible(false);
        pocetakInputPanel.add(dodajMoguciTermin);
        String vremena=timesToLabel(allowedIntervals);
        vremenaLabel=new JLabel(vremena);
        pocetakPanel.setLayout(new BoxLayout(pocetakPanel, BoxLayout.Y_AXIS));
        pocetakPanel.add(pocetakInputPanel,BorderLayout.NORTH);
        pocetakPanel.add(vremenaLabel,BorderLayout.SOUTH);
        pocetciPanel=new JPanel();
        pocetciPanel.setLayout(new BoxLayout(pocetciPanel, BoxLayout.Y_AXIS));
        pocetciPanel.add(pocetakPanel);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(grupaINaslovPanel,BorderLayout.NORTH);
        this.add(trajanjePanel,BorderLayout.CENTER);
        this.add(pocetciPanel,BorderLayout.SOUTH);
        groupBox.addActionListener(this);
        glasanjeCheckbox.addActionListener(this);
        dodajMoguciTermin.addActionListener(this);
        docListener=new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {updateValues();}
            @Override
            public void removeUpdate(DocumentEvent e) {updateValues();}
            @Override
            public void insertUpdate(DocumentEvent e) {updateValues();}
        };
        durationField.getDocument().addDocumentListener(docListener);
    }
    private void updateValues() {
        String trenGrupa=groupBox.getSelectedItem().toString();
        int trajanje;
        try {
            trajanje=Integer.parseInt(durationField.getText());
        } catch (NumberFormatException e) {
            trajanje=0;
        }
        ArrayList<Interval> allowedIntervals=getAvailableTimes(trenGrupa,trajanje);
        vremenaLabel.setText(timesToLabel(allowedIntervals));
        for (int i=0;i<drugaVremena.size();++i) {
            ArrayList<Interval> otherAllowedIntervals=allowedIntervals;
            int selectedYear=-1,selectedMonth=-1,selectedDay=-1;
            try {
                selectedYear=Integer.parseInt(yearFields.get(i).getText());
                selectedMonth=Integer.parseInt(monthFields.get(i).getText());
                selectedDay=Integer.parseInt(dayFields.get(i).getText());
                if (selectedYear!=godina || selectedMonth!=mjesec || selectedDay!=dan) {
                    otherAllowedIntervals=getAvailableTimes(trenGrupa,trajanje,selectedDay,selectedMonth,selectedYear);
                }
            } catch (NumberFormatException e) {
                otherAllowedIntervals=new ArrayList<Interval>();
            }
            vremenaLabels.get(i).setText(timesToLabel(otherAllowedIntervals));
        }
        dodajMoguciTermin.setVisible(glasanjeCheckbox.isSelected());
        for (int i=0;i<drugaVremena.size();++i) drugaVremena.get(i).setVisible(glasanjeCheckbox.isSelected());
        this.revalidate();
        this.repaint();
        SwingUtilities.getWindowAncestor(this).pack();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==dodajMoguciTermin) {
            int noviInd=drugaVremena.size();
            drugaVremenaInput.add(new JPanel());
            drugaVremena.add(new JPanel());
            hourFields.add(new JTextField(2));
            minuteFields.add(new JTextField(2));
            yearFields.add(new JTextField(Integer.toString(godina),4));
            monthFields.add(new JTextField(Integer.toString(mjesec),2));
            dayFields.add(new JTextField(Integer.toString(dan),2));
            vremenaLabels.add(new JLabel(""));
            drugaVremenaInput.get(noviInd).add(dayFields.get(noviInd));
            drugaVremenaInput.get(noviInd).add(new JLabel("."));
            drugaVremenaInput.get(noviInd).add(monthFields.get(noviInd));
            drugaVremenaInput.get(noviInd).add(new JLabel("."));
            drugaVremenaInput.get(noviInd).add(yearFields.get(noviInd));
            drugaVremenaInput.get(noviInd).add(new JLabel(". "));
            drugaVremenaInput.get(noviInd).add(hourFields.get(noviInd));
            drugaVremenaInput.get(noviInd).add(new JLabel(":"));
            drugaVremenaInput.get(noviInd).add(minuteFields.get(noviInd));
            drugaVremena.get(noviInd).setLayout(new BoxLayout(drugaVremena.get(noviInd), BoxLayout.Y_AXIS));
            drugaVremena.get(noviInd).add(drugaVremenaInput.get(noviInd));
            drugaVremena.get(noviInd).add(vremenaLabels.get(noviInd));
            pocetciPanel.add(drugaVremena.get(noviInd));
            yearFields.get(noviInd).getDocument().addDocumentListener(docListener);
            monthFields.get(noviInd).getDocument().addDocumentListener(docListener);
            dayFields.get(noviInd).getDocument().addDocumentListener(docListener);
        }
        updateValues();
    }
}
