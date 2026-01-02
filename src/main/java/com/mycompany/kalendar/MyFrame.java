/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Vid
 */
public class MyFrame extends JFrame implements ActionListener{
    ArrayList<MyButton> gumbi= new ArrayList<>();
    List<Integer> mjeseci= Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31);
    int trenutnimjesec;
    int trenutnagodina;
    int trenutnioffset;
    int kolikoovajmjesecimadana;
    int kolikoproslimjesecimadana;
    String trenutnimjesecstring;
    JButton left;
    JButton right;
    
    MyFrame(){
        
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        trenutnimjesec = cal.get(Calendar.MONTH);
        trenutnimjesecstring = new DateFormatSymbols().getMonths()[trenutnimjesec];
        trenutnagodina= cal.get(Calendar.YEAR);
        trenutnioffset=nadioffset(trenutnagodina,trenutnimjesec+1);

        if(trenutnimjesec==0){
            kolikoovajmjesecimadana=mjeseci.get(0);
            kolikoproslimjesecimadana=mjeseci.get(11);
        }
        else{
            kolikoovajmjesecimadana=mjeseci.get(trenutnimjesec);
            kolikoproslimjesecimadana=mjeseci.get(trenutnimjesec-1);
        }
        
        left =new JButton();
        right= new JButton();
        left.setBounds(0,200,50,50);
        right.setBounds(900,200,50,50);
        URL imageL = getClass().getResource("/arrow2.png");
        ImageIcon iconL = new ImageIcon(imageL);
        Image scaledImageL = iconL.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon maliiconL= new ImageIcon(scaledImageL);
        URL imageR = getClass().getResource("/arrow4.png");
        ImageIcon iconR = new ImageIcon(imageR);
        Image scaledImageR = iconR.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon maliiconR= new ImageIcon(scaledImageR);
        left.setIcon(maliiconL);
        right.setIcon(maliiconR);
        left.addActionListener(this);
        right.addActionListener(this);
        
        JLabel naslov=new JLabel();
        naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
        naslov.setBounds(400, 50, 200, 50);
        
        for (int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                String novistring;
                MyButton novi;
                if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                    novistring=(i*7+j+1-trenutnioffset-kolikoovajmjesecimadana)+"."+(trenutnimjesec+1)+".";
                    novi= new MyButton(100*j+100,50*i+100,100,50,novistring);
                    novi.setEnabled(false);
                }else if(i*7+j+1-trenutnioffset<=0){
                    novistring=(i*7+j+1-trenutnioffset+kolikoproslimjesecimadana)+"."+(trenutnimjesec+1)+".";
                    novi= new MyButton(100*j+100,50*i+100,100,50,novistring);
                    novi.setEnabled(false);
                }else{
                    novistring=(i*7+j+1-trenutnioffset)+"."+(trenutnimjesec+1)+".";
                    novi= new MyButton(100*j+100,50*i+100,100,50,novistring);
                    novi.setEnabled(true);
                }
                
                
                gumbi.add(novi);
                novi.addActionListener(this);
                this.add(novi);
            }
            
        }

        this.add(left);
        this.add(right);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000,500);
        this.setVisible(true);
        this.add(naslov);
    }
    MyFrame(int trenutnagodina,int trenutnimjesec){
        trenutnioffset=nadioffset(trenutnagodina,trenutnimjesec+1);
        trenutnimjesecstring = new DateFormatSymbols().getMonths()[trenutnimjesec];
        if(trenutnimjesec==0){
            kolikoovajmjesecimadana=mjeseci.get(0);
            kolikoproslimjesecimadana=mjeseci.get(11);
        }
        else{
            kolikoovajmjesecimadana=mjeseci.get(trenutnimjesec);
            kolikoproslimjesecimadana=mjeseci.get(trenutnimjesec-1);
        }
        left =new JButton();
        right= new JButton();
        left.setBounds(0,200,50,50);
        right.setBounds(900,200,50,50);
        URL imageL = getClass().getResource("/arrow2.png");
        ImageIcon iconL = new ImageIcon(imageL);
        Image scaledImageL = iconL.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon maliiconL= new ImageIcon(scaledImageL);
        URL imageR = getClass().getResource("/arrow4.png");
        ImageIcon iconR = new ImageIcon(imageR);
        Image scaledImageR = iconR.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon maliiconR= new ImageIcon(scaledImageR);
        left.setIcon(maliiconL);
        right.setIcon(maliiconR);
        left.addActionListener(this);
        right.addActionListener(this);

        JLabel naslov=new JLabel();
        naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
        naslov.setBounds(400, 50, 200, 50);
        
        for (int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                String novistring;
                MyButton novi;
                if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                    novistring=(i*7+j+1-trenutnioffset-kolikoovajmjesecimadana)+"."+(trenutnimjesec+1)+".";
                    novi= new MyButton(100*j+100,50*i+100,100,50,novistring);
                    novi.setEnabled(false);
                }else if(i*7+j+1-trenutnioffset<=0){
                    novistring=(i*7+j+1-trenutnioffset+kolikoproslimjesecimadana)+"."+(trenutnimjesec+1)+".";
                    novi= new MyButton(100*j+100,50*i+100,100,50,novistring);
                    novi.setEnabled(false);
                }else{
                    novistring=(i*7+j+1-trenutnioffset)+"."+(trenutnimjesec+1)+".";
                    novi= new MyButton(100*j+100,50*i+100,100,50,novistring);
                    novi.setEnabled(true);
                }


                gumbi.add(novi);
                novi.addActionListener(this);
                this.add(novi);
            }

        }
        this.add(naslov);
        this.add(left);
        this.add(right);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000,500);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==left){

            if(trenutnimjesec==0){
                trenutnimjesec=11;
                trenutnagodina=trenutnagodina-1;
            }
            else{
                trenutnimjesec=trenutnimjesec-1;
            }
            trenutnioffset=nadioffset(trenutnagodina,trenutnimjesec+1);
            trenutnimjesecstring = new DateFormatSymbols().getMonths()[trenutnimjesec];
            if(trenutnimjesec==0){
                kolikoovajmjesecimadana=mjeseci.get(0);
                kolikoproslimjesecimadana=mjeseci.get(11);
            }
            else{
                kolikoovajmjesecimadana=mjeseci.get(trenutnimjesec);
                kolikoproslimjesecimadana=mjeseci.get(trenutnimjesec-1);
            }

            
            JButton oldleft =(JButton) e.getSource();
            
            this.getContentPane().removeAll();
            gumbi.clear();

            
            JButton nextleft=new JButton();
            JButton nextright= new JButton();
            nextleft.setBounds(0,200,50,50);
            nextright.setBounds(900,200,50,50);
            URL imageL = getClass().getResource("/arrow2.png");
            ImageIcon iconL = new ImageIcon(imageL);
            Image scaledImageL = iconL.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon maliiconL= new ImageIcon(scaledImageL);
            URL imageR = getClass().getResource("/arrow4.png");
            ImageIcon iconR = new ImageIcon(imageR);
            Image scaledImageR = iconR.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon maliiconR= new ImageIcon(scaledImageR);
            nextleft.setIcon(maliiconL);
            nextright.setIcon(maliiconR);
            nextleft.addActionListener(this);
            nextright.addActionListener(this);
            
            left=nextleft;
            right=nextright;
            
            JLabel naslov=new JLabel();
            naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
            naslov.setBounds(400, 50, 200, 50);
            
            for (int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    String novistring;
                    MyButton novi;
                    if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                        novistring=(i*7+j+1-trenutnioffset-kolikoovajmjesecimadana)+"."+(trenutnimjesec+1)+".";
                        MyButton temp=new MyButton(100*j+100,50*i+100,100,50,novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setEnabled(false);
                    }else if(i*7+j+1-trenutnioffset<=0){
                        novistring=(i*7+j+1-trenutnioffset+kolikoproslimjesecimadana)+"."+(trenutnimjesec+1)+".";
                        MyButton temp=new MyButton(100*j+100,50*i+100,100,50,novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setEnabled(false);
                    }else{
                        novistring=(i*7+j+1-trenutnioffset)+"."+(trenutnimjesec+1)+".";
                        MyButton temp=new MyButton(100*j+100,50*i+100,100,50,novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setEnabled(true);
                    }


                    gumbi.add(novi);
                    this.add(novi);
                }
            
            }
                this.add(naslov);
                this.add(left);
                this.add(right);
                this.revalidate();
                this.repaint();
            }
        //---------------------------------
        else if(e.getSource()==right){
            if(trenutnimjesec==11){
                trenutnimjesec=0;
                trenutnagodina=trenutnagodina+1;
            }
            else{
                trenutnimjesec=trenutnimjesec+1;
            }
            trenutnioffset=nadioffset(trenutnagodina,trenutnimjesec+1);
            JButton oldright =(JButton) e.getSource();
            this.getContentPane().removeAll();
            gumbi.clear();
            
            trenutnimjesecstring = new DateFormatSymbols().getMonths()[trenutnimjesec];
            if(trenutnimjesec==0){
                    kolikoovajmjesecimadana=mjeseci.get(0);
                    kolikoproslimjesecimadana=mjeseci.get(11);
                }
                else{
                    kolikoovajmjesecimadana=mjeseci.get(trenutnimjesec);
                    kolikoproslimjesecimadana=mjeseci.get(trenutnimjesec-1);
                }
            for (int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    String novistring;
                    MyButton novi;
                    if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                        novistring=(i*7+j+1-trenutnioffset-kolikoovajmjesecimadana)+"."+(trenutnimjesec+1)+".";
                        MyButton temp=new MyButton(100*j+100,50*i+100,100,50,novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setEnabled(false);
                    }else if(i*7+j+1-trenutnioffset<=0){
                        novistring=(i*7+j+1-trenutnioffset+kolikoproslimjesecimadana)+"."+(trenutnimjesec+1)+".";
                        MyButton temp=new MyButton(100*j+100,50*i+100,100,50,novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setEnabled(false);
                    }else{
                        novistring=(i*7+j+1-trenutnioffset)+"."+(trenutnimjesec+1)+".";
                        MyButton temp=new MyButton(100*j+100,50*i+100,100,50,novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setEnabled(true);
                    }


                    gumbi.add(novi);
                    this.add(novi);
                }

            }
            

            JButton nextleft=new JButton();
            JButton nextright= new JButton();
            nextleft.setBounds(0,200,50,50);
            nextright.setBounds(900,200,50,50);
            URL imageL = getClass().getResource("/arrow2.png");
            ImageIcon iconL = new ImageIcon(imageL);
            Image scaledImageL = iconL.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon maliiconL= new ImageIcon(scaledImageL);
            URL imageR = getClass().getResource("/arrow4.png");
            ImageIcon iconR = new ImageIcon(imageR);
            Image scaledImageR = iconR.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon maliiconR= new ImageIcon(scaledImageR);
            nextleft.setIcon(maliiconL);
            nextright.setIcon(maliiconR);
            nextleft.addActionListener(this);
            nextright.addActionListener(this);

            JLabel naslov=new JLabel();
            naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
            naslov.setBounds(400, 50, 200, 50);

            this.add(naslov);
            this.add(left);
            this.add(right);
            this.revalidate();
            this.repaint();
            }
        for (int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                if(e.getSource()==gumbi.get(i*7+j)){
                    MyButton neki=gumbi.get(i*7+j);
                    neki.isprintajtekst();
                    
                }
            }
        }
    }
    private static Integer nadioffset(int godina, int mjesec){
        LocalDate myDate = LocalDate.of(godina,mjesec,1);
        DayOfWeek dayofTheWeek =myDate.getDayOfWeek();
        String danutjednu=dayofTheWeek.toString();
        switch (danutjednu) {
            case "MONDAY":
                return 0;
            case "TUESDAY":
                return 1;
            case "WEDNESDAY":
                return 2;
            case "THURSDAY":
                return 3;
            case "FRIDAY":
                return 4;
            case "SATURDAY":
                return 5;
            default:
                return 6;
        }
    }   
        
        
    }

