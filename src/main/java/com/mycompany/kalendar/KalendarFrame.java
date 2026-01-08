/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Vid
 */
//MyFrame modificirani JFrame tako da ima sve što nam treba

public class KalendarFrame extends JFrame implements ActionListener{
    //Pomoćne varijable koje će mi trebati kasnije
    //Arraylist svih gumba sa datumima, lista koja govori koliko koji mjesec ima dana
    ArrayList<JButton> gumbi= new ArrayList<>();
    List<String> daniutjednu= Arrays.asList("Mon","Tue","Wed","Thu","Fri","Sat","Sun");
    List<Integer> mjeseci= Arrays.asList(31,28,31,30,31,30,31,31,30,31,30,31);
    //Trenutnioffset govori koji dan u tjednu je 1. dan u trenutnom mjesecu, prijašnji dani u tjednu su
    //iz poršlog mjeseca i ne možemo ih kliknuti
    int trenutnimjesec;
    int trenutnagodina;
    int trenutnioffset;
    int kolikoovajmjesecimadana;
    int kolikoproslimjesecimadana;
    String trenutnimjesecstring;
    JButton left;
    JButton right;
    
    //Konstruktor bez parametara
    KalendarFrame(){

        //Dohvatimo trenutni datum i uzmem redni broj i naziv mjeseca
        //Također nađem trenutnioffset
        java.util.Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        trenutnimjesec = cal.get(Calendar.MONTH);
        trenutnimjesecstring = new DateFormatSymbols().getMonths()[trenutnimjesec];
        trenutnagodina= cal.get(Calendar.YEAR);
        trenutnioffset=nadioffset(trenutnagodina,trenutnimjesec+1);
        
        //Nađem koliko trenutni i prošli mjesec imaju dana
        if(trenutnimjesec==0){
            kolikoovajmjesecimadana=mjeseci.get(0);
            kolikoproslimjesecimadana=mjeseci.get(11);
        }
        else{
            kolikoovajmjesecimadana=mjeseci.get(trenutnimjesec);
            kolikoproslimjesecimadana=mjeseci.get(trenutnimjesec-1);
        }
        
        //Stvorim gumb za lijevo i desno,pozicioniram ih stavim ikone na njih i dodam ActionListenere da
        //zapravo naprave nešto kad ih kliknem
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
        
        //Stavim naslov
        JLabel naslov=new JLabel();
        naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
        naslov.setFont(new Font("Calibri",Font.PLAIN,50));

        JPanel naslov2=new JPanel();
        naslov2.setPreferredSize(new Dimension(2000,60));
        naslov2.setBackground(Color.WHITE);
        naslov2.add(naslov,BorderLayout.CENTER);
        
        JPanel naslov3=new JPanel();
        naslov3.setPreferredSize(new Dimension(2000,40));
        naslov3.setBackground(Color.lightGray);
        naslov3.setBorder(new EmptyBorder(0,60,0,0));
        for(int j=0;j<7;j++){
            JLabel danutjednu=new JLabel();
            danutjednu.setText(daniutjednu.get(j));
            danutjednu.setPreferredSize(new Dimension(100,40));
            danutjednu.setFont(new Font("Calibri",Font.PLAIN,25));
            naslov3.add(danutjednu,BorderLayout.CENTER);
        }
        
        JPanel naslovPanel=new JPanel();
        naslovPanel.setBackground(new Color(150,150,150));
        naslovPanel.setPreferredSize(new Dimension(800,120));
        naslovPanel.add(naslov2,BorderLayout.NORTH);
        naslovPanel.add(naslov3,BorderLayout.SOUTH);
        
        JPanel datumGumbiPanel1=new JPanel();
        JPanel datumGumbiPanel11=new JPanel();
        JPanel datumGumbiPanel12=new JPanel();
        JPanel datumGumbiPanel111=new JPanel();
        JPanel datumGumbiPanel112=new JPanel();
        JPanel datumGumbiPanel113=new JPanel();
        JPanel datumGumbiPanel121=new JPanel();
        JPanel datumGumbiPanel122=new JPanel();
        JPanel datumGumbiPanel123=new JPanel();
        
        datumGumbiPanel1.setBorder(new EmptyBorder(-5,0,0,0));
        datumGumbiPanel11.setBorder(new EmptyBorder(-5,0,0,0));
        datumGumbiPanel12.setBorder(new EmptyBorder(-5,0,0,0));
        
        datumGumbiPanel111.setBorder(new EmptyBorder(-5,0,0,0));
        datumGumbiPanel112.setBorder(new EmptyBorder(-5,0,0,0));
        datumGumbiPanel113.setBorder(new EmptyBorder(-5,0,0,0));
        datumGumbiPanel121.setBorder(new EmptyBorder(-5,0,0,0));
        datumGumbiPanel122.setBorder(new EmptyBorder(-5,0,0,0));
        datumGumbiPanel123.setBorder(new EmptyBorder(-5,0,0,0));
                
        datumGumbiPanel1.setBackground(new Color(210,210,210));
        datumGumbiPanel11.setBackground(new Color(210,210,210));
        datumGumbiPanel12.setBackground(new Color(210,210,210));
        datumGumbiPanel111.setBackground(new Color(220,220,220));
        datumGumbiPanel112.setBackground(new Color(230,230,230));
        datumGumbiPanel113.setBackground(new Color(240,240,240));
        datumGumbiPanel121.setBackground(new Color(220,220,220));
        datumGumbiPanel122.setBackground(new Color(230,230,230));
        datumGumbiPanel123.setBackground(new Color(240,240,240));
        
        datumGumbiPanel1.setPreferredSize(new Dimension(2000,2000));
        datumGumbiPanel11.setPreferredSize(new Dimension(2000,160));
        datumGumbiPanel12.setPreferredSize(new Dimension(2000,160));
        datumGumbiPanel111.setPreferredSize(new Dimension(2000,50));
        datumGumbiPanel112.setPreferredSize(new Dimension(2000,50));
        datumGumbiPanel113.setPreferredSize(new Dimension(2000,50));
        datumGumbiPanel121.setPreferredSize(new Dimension(2000,50));
        datumGumbiPanel122.setPreferredSize(new Dimension(2000,50));
        datumGumbiPanel123.setPreferredSize(new Dimension(2000,50));
        
        
        
        JPanel lijeviGumbPanel=new JPanel();
        lijeviGumbPanel.setBackground(new Color(100,100,100));
        lijeviGumbPanel.setPreferredSize(new Dimension(100,100));
        lijeviGumbPanel.add(left,BorderLayout.CENTER);
        
        JPanel desniGumbPanel=new JPanel();
        desniGumbPanel.setBackground(new Color(100,100,100));
        desniGumbPanel.setPreferredSize(new Dimension(100,100));
        desniGumbPanel.add(right,BorderLayout.CENTER);
        
        
        //Dodam sve gumbe za datume, pozicioniram ih i dodam ih u frame
        for (int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                String novistring;
                JButton novi;
                if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                    int novidan=i*7+j+1-trenutnioffset-kolikoovajmjesecimadana;
                    int novimjesec=trenutnimjesec+2;
                    if(novimjesec==13){
                            novimjesec=1;
                        }
                    novistring=novidan + "." + novimjesec;
                    novi= new JButton(novistring);
                    novi.setPreferredSize(new Dimension(100,50));
                    novi.setEnabled(false);
                }else if(i*7+j+1-trenutnioffset<=0){
                    int novidan=i*7+j+1-trenutnioffset+kolikoproslimjesecimadana;
                    int novimjesec=trenutnimjesec;
                    if(novimjesec==0){
                        novimjesec=12;
                    }
                    novistring=novidan + "." + novimjesec;
                    novi= new JButton(novistring);
                    novi.setPreferredSize(new Dimension(100,50));
                    novi.setEnabled(false);
                }else{
                    int novidan=i*7+j+1-trenutnioffset;
                    int novimjesec=trenutnimjesec+1;
                    novistring=novidan + "." + novimjesec;
                    novi= new JButton(novistring);
                    novi.setPreferredSize(new Dimension(100,50));
                    novi.setEnabled(true);
                }
                
                
                gumbi.add(novi);
                novi.addActionListener(this);
                switch(i){
                    case 0:
                        datumGumbiPanel111.add(novi);
                        break;
                    case 1:
                        datumGumbiPanel112.add(novi);
                        break;
                    case 2:
                        datumGumbiPanel113.add(novi);
                        break;
                    case 3:
                        datumGumbiPanel121.add(novi);
                        break;
                    case 4:
                        datumGumbiPanel122.add(novi);
                        break;
                    default:
                        datumGumbiPanel123.add(novi);
                        break;
                }
            }
            
        }
        //Dodam oznake za dane u tjednu
        
        datumGumbiPanel11.add(datumGumbiPanel111,BorderLayout.NORTH);
        datumGumbiPanel11.add(datumGumbiPanel112,BorderLayout.CENTER);
        datumGumbiPanel11.add(datumGumbiPanel113,BorderLayout.SOUTH);
        datumGumbiPanel12.add(datumGumbiPanel121,BorderLayout.NORTH);
        datumGumbiPanel12.add(datumGumbiPanel122,BorderLayout.CENTER);
        datumGumbiPanel12.add(datumGumbiPanel123,BorderLayout.SOUTH);
        
        datumGumbiPanel1.add(datumGumbiPanel11,BorderLayout.NORTH);
        datumGumbiPanel1.add(datumGumbiPanel12,BorderLayout.SOUTH);
        //Dodam sve stvari u frame
        
        this.add(lijeviGumbPanel,BorderLayout.WEST);
        this.add(desniGumbPanel,BorderLayout.EAST);
        this.add(naslovPanel,BorderLayout.NORTH);
        this.add(datumGumbiPanel1,BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000,500);
        this.setVisible(true);
        
    }
    
    //Konstruktor sa parametrima ako će nam ikad trebat
    //Konstruktor prima mjesec i godinu
    /*KalendarFrame(int trenutnagodina,int trenutnimjesec){
        
        //Nađem trenutnioffset
        trenutnioffset=nadioffset(trenutnagodina,trenutnimjesec+1);
        trenutnimjesecstring = new DateFormatSymbols().getMonths()[trenutnimjesec];
        
        //Nađem koliko ovaj i prošli mjesec imaju dana
        if(trenutnimjesec==0){
            kolikoovajmjesecimadana=mjeseci.get(0);
            kolikoproslimjesecimadana=mjeseci.get(11);
        }
        else{
            kolikoovajmjesecimadana=mjeseci.get(trenutnimjesec);
            kolikoproslimjesecimadana=mjeseci.get(trenutnimjesec-1);
        }
        
        //Dodam gumbe za lijevo i desno
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
        
        //Dodam naslov
        JLabel naslov=new JLabel();
        naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
        naslov.setBounds(400, 50, 200, 50);
        
        JPanel datumGumbiPanel= new JPanel();
        datumGumbiPanel.setPreferredSize(new Dimension(500,500));
        datumGumbiPanel.setBackground(Color.lightGray);
        datumGumbiPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        //Dodam gumbe s datumima, dodam actionListenere i dodam ih u frame
        for (int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                String novistring;
                JButton novi;
                if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                    int novidan=i*7+j+1-trenutnioffset-kolikoovajmjesecimadana;
                    int novimjesec=trenutnimjesec+2;
                    if(novimjesec==13){
                            novimjesec=1;
                        }
                    novistring=novidan + "." + novimjesec;
                    novi= new JButton(novistring);
                    novi.setEnabled(false);
                }else if(i*7+j+1-trenutnioffset<=0){
                    int novidan=i*7+j+1-trenutnioffset+kolikoproslimjesecimadana;
                    int novimjesec=trenutnimjesec;
                    if(novimjesec==0){
                        novimjesec=12;
                    }
                    novistring=novidan + "." + novimjesec;
                    novi= new JButton(novistring);
                    novi.setEnabled(false);
                }else{
                    int novidan=i*7+j+1-trenutnioffset;
                    int novimjesec=trenutnimjesec+1;
                    novistring=novidan + "." + novimjesec;
                    novi= new JButton(novistring);
                    novi.setEnabled(true);
                }


                gumbi.add(novi);
                novi.addActionListener(this);
                datumGumbiPanel.add(novi);
            }

        }
        //Dodam oznake za dane u tjednu
        for(int j=0;j<7;j++){
            JLabel danutjednu=new JLabel();
            danutjednu.setText(daniutjednu.get(j));
            danutjednu.setBounds(100*j+140, 50, 200, 50);
            this.add(danutjednu);
        }
        //Dodam sve u frame
        this.add(naslov);
        this.add(left);
        this.add(right);
        this.add(datumGumbiPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000,500);
        this.setVisible(true);
    }*/
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==left){
            //Ako si stisnuo lijevi gumb, umanji trenutni mjesec za 1, osim ako ja siječanj onda odi u prosinac
            if(trenutnimjesec==0){
                trenutnimjesec=11;
                trenutnagodina=trenutnagodina-1;
            }
            else{
                trenutnimjesec=trenutnimjesec-1;
            }
            //Onda nađi offset i nađi koliko ovaj i prošli mjesec imaju dana
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

            //Obriši sve u framu i na listi za gumbe
            this.getContentPane().removeAll();
            gumbi.clear();

            //Dodaj nove gumbe za lijevo i desno
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
            //Dodaj novi naslov
            JLabel naslov=new JLabel();
            naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
            naslov.setFont(new Font("Calibri",Font.PLAIN,50));

            JPanel naslov2=new JPanel();
            naslov2.setPreferredSize(new Dimension(2000,60));
            naslov2.setBackground(Color.WHITE);
            naslov2.add(naslov,BorderLayout.CENTER);

            JPanel naslov3=new JPanel();
            naslov3.setPreferredSize(new Dimension(2000,40));
            naslov3.setBackground(Color.lightGray);
            naslov3.setBorder(new EmptyBorder(0,50,0,0));
            for(int j=0;j<7;j++){
                JLabel danutjednu=new JLabel();
                danutjednu.setText(daniutjednu.get(j));
                danutjednu.setPreferredSize(new Dimension(100,40));
                danutjednu.setFont(new Font("Calibri",Font.PLAIN,25));
                naslov3.add(danutjednu,BorderLayout.CENTER);
            }

            JPanel naslovPanel=new JPanel();
            naslovPanel.setBackground(new Color(150,150,150));
            naslovPanel.setPreferredSize(new Dimension(2000,120));
            naslovPanel.add(naslov2,BorderLayout.NORTH);
            naslovPanel.add(naslov3,BorderLayout.SOUTH);

            JPanel datumGumbiPanel1=new JPanel();
            JPanel datumGumbiPanel11=new JPanel();
            JPanel datumGumbiPanel12=new JPanel();
            JPanel datumGumbiPanel111=new JPanel();
            JPanel datumGumbiPanel112=new JPanel();
            JPanel datumGumbiPanel113=new JPanel();
            JPanel datumGumbiPanel121=new JPanel();
            JPanel datumGumbiPanel122=new JPanel();
            JPanel datumGumbiPanel123=new JPanel();

            datumGumbiPanel1.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel11.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel12.setBorder(new EmptyBorder(-5,0,0,0));

            datumGumbiPanel111.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel112.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel113.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel121.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel122.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel123.setBorder(new EmptyBorder(-5,0,0,0));

            datumGumbiPanel1.setBackground(new Color(210,210,210));
            datumGumbiPanel11.setBackground(new Color(210,210,210));
            datumGumbiPanel12.setBackground(new Color(210,210,210));
            datumGumbiPanel111.setBackground(new Color(220,220,220));
            datumGumbiPanel112.setBackground(new Color(230,230,230));
            datumGumbiPanel113.setBackground(new Color(240,240,240));
            datumGumbiPanel121.setBackground(new Color(220,220,220));
            datumGumbiPanel122.setBackground(new Color(230,230,230));
            datumGumbiPanel123.setBackground(new Color(240,240,240));

            datumGumbiPanel1.setPreferredSize(new Dimension(2000,800));
            datumGumbiPanel11.setPreferredSize(new Dimension(2000,160));
            datumGumbiPanel12.setPreferredSize(new Dimension(2000,160));
            datumGumbiPanel111.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel112.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel113.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel121.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel122.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel123.setPreferredSize(new Dimension(2000,50));



            JPanel lijeviGumbPanel=new JPanel();
            lijeviGumbPanel.setBackground(new Color(100,100,100));
            lijeviGumbPanel.setPreferredSize(new Dimension(100,100));
            lijeviGumbPanel.add(left,BorderLayout.CENTER);

            JPanel desniGumbPanel=new JPanel();
            desniGumbPanel.setBackground(new Color(100,100,100));
            desniGumbPanel.setPreferredSize(new Dimension(100,100));
            desniGumbPanel.add(right,BorderLayout.CENTER);
            
            //Dodaj nove gumbe
            for (int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    String novistring;
                    JButton novi;
                    if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                        int novidan=i*7+j+1-trenutnioffset-kolikoovajmjesecimadana;
                        int novimjesec=trenutnimjesec+2;
                        if(novimjesec==13){
                            novimjesec=1;
                        }
                        novistring=novidan + "." + novimjesec;
                        JButton temp=new JButton(novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setPreferredSize(new Dimension(100,50));
                        novi.setEnabled(false);
                    }else if(i*7+j+1-trenutnioffset<=0){
                        int novidan=i*7+j+1-trenutnioffset+kolikoproslimjesecimadana;
                        int novimjesec=trenutnimjesec;
                        if(novimjesec==0){
                            novimjesec=12;
                        }
                        novistring=novidan + "." + novimjesec;
                        JButton temp=new JButton(novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setPreferredSize(new Dimension(100,50));
                        novi.setEnabled(false);
                    }else{
                        int novidan=i*7+j+1-trenutnioffset;
                        int novimjesec=trenutnimjesec+1;
                        novistring=novidan + "." + novimjesec;
                        JButton temp=new JButton(novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setPreferredSize(new Dimension(100,50));
                        novi.setEnabled(true);
                    }


                    gumbi.add(novi);
                    switch(i){
                        case 0:
                            datumGumbiPanel111.add(novi);
                            break;
                        case 1:
                            datumGumbiPanel112.add(novi);
                            break;
                        case 2:
                            datumGumbiPanel113.add(novi);
                            break;
                        case 3:
                            datumGumbiPanel121.add(novi);
                            break;
                        case 4:
                            datumGumbiPanel122.add(novi);
                            break;
                        default:
                            datumGumbiPanel123.add(novi);
                            break;
                    }
                }
            
            }
            //Dodaj nove oznake za dane u tjednu
            datumGumbiPanel11.add(datumGumbiPanel111,BorderLayout.NORTH);
            datumGumbiPanel11.add(datumGumbiPanel112,BorderLayout.CENTER);
            datumGumbiPanel11.add(datumGumbiPanel113,BorderLayout.SOUTH);
            datumGumbiPanel12.add(datumGumbiPanel121,BorderLayout.NORTH);
            datumGumbiPanel12.add(datumGumbiPanel122,BorderLayout.CENTER);
            datumGumbiPanel12.add(datumGumbiPanel123,BorderLayout.SOUTH);

            datumGumbiPanel1.add(datumGumbiPanel11,BorderLayout.NORTH);
            datumGumbiPanel1.add(datumGumbiPanel12,BorderLayout.SOUTH);
            //Dodam sve stvari u frame

            this.add(lijeviGumbPanel,BorderLayout.WEST);
            this.add(desniGumbPanel,BorderLayout.EAST);
            this.add(naslovPanel,BorderLayout.NORTH);
            this.add(datumGumbiPanel1,BorderLayout.CENTER);

            this.revalidate();
            this.repaint();
            }
        //---------------------------------
        else if(e.getSource()==right){
            //Ako odeš desno pomakni trenutni mjesec i mozda trenutnu godinu
            if(trenutnimjesec==11){
                trenutnimjesec=0;
                trenutnagodina=trenutnagodina+1;
            }
            else{
                trenutnimjesec=trenutnimjesec+1;
            }
            //Nađi novi offset
            trenutnioffset=nadioffset(trenutnagodina,trenutnimjesec+1);
            //Obriši sve u framu i gumbe u Arrayu
            this.getContentPane().removeAll();
            gumbi.clear();
            //Nađi koliko novi mjesec ima dana
            trenutnimjesecstring = new DateFormatSymbols().getMonths()[trenutnimjesec];
            if(trenutnimjesec==0){
                    kolikoovajmjesecimadana=mjeseci.get(0);
                    kolikoproslimjesecimadana=mjeseci.get(11);
                }
                else{
                    kolikoovajmjesecimadana=mjeseci.get(trenutnimjesec);
                    kolikoproslimjesecimadana=mjeseci.get(trenutnimjesec-1);
                }
            
            //Dodaj nove gumbe za lijevo i desno
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
            //Dodaj novi naslov
            JLabel naslov=new JLabel();
            naslov.setText(trenutnimjesecstring+" "+trenutnagodina);
            naslov.setFont(new Font("Calibri",Font.PLAIN,50));

            JPanel naslov2=new JPanel();
            naslov2.setPreferredSize(new Dimension(2000,60));
            naslov2.setBackground(Color.WHITE);
            naslov2.add(naslov,BorderLayout.CENTER);

            JPanel naslov3=new JPanel();
            naslov3.setPreferredSize(new Dimension(2000,40));
            naslov3.setBackground(Color.lightGray);
            naslov3.setBorder(new EmptyBorder(0,50,0,0));
            for(int j=0;j<7;j++){
                JLabel danutjednu=new JLabel();
                danutjednu.setText(daniutjednu.get(j));
                danutjednu.setPreferredSize(new Dimension(100,40));
                danutjednu.setFont(new Font("Calibri",Font.PLAIN,25));
                naslov3.add(danutjednu,BorderLayout.CENTER);
            }

            JPanel naslovPanel=new JPanel();
            naslovPanel.setBackground(new Color(150,150,150));
            naslovPanel.setPreferredSize(new Dimension(2000,120));
            naslovPanel.add(naslov2,BorderLayout.NORTH);
            naslovPanel.add(naslov3,BorderLayout.SOUTH);

            JPanel datumGumbiPanel1=new JPanel();
            JPanel datumGumbiPanel11=new JPanel();
            JPanel datumGumbiPanel12=new JPanel();
            JPanel datumGumbiPanel111=new JPanel();
            JPanel datumGumbiPanel112=new JPanel();
            JPanel datumGumbiPanel113=new JPanel();
            JPanel datumGumbiPanel121=new JPanel();
            JPanel datumGumbiPanel122=new JPanel();
            JPanel datumGumbiPanel123=new JPanel();

            datumGumbiPanel1.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel11.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel12.setBorder(new EmptyBorder(-5,0,0,0));

            datumGumbiPanel111.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel112.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel113.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel121.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel122.setBorder(new EmptyBorder(-5,0,0,0));
            datumGumbiPanel123.setBorder(new EmptyBorder(-5,0,0,0));

            datumGumbiPanel1.setBackground(new Color(210,210,210));
            datumGumbiPanel11.setBackground(new Color(210,210,210));
            datumGumbiPanel12.setBackground(new Color(210,210,210));
            datumGumbiPanel111.setBackground(new Color(220,220,220));
            datumGumbiPanel112.setBackground(new Color(230,230,230));
            datumGumbiPanel113.setBackground(new Color(240,240,240));
            datumGumbiPanel121.setBackground(new Color(220,220,220));
            datumGumbiPanel122.setBackground(new Color(230,230,230));
            datumGumbiPanel123.setBackground(new Color(240,240,240));

            datumGumbiPanel1.setPreferredSize(new Dimension(2000,800));
            datumGumbiPanel11.setPreferredSize(new Dimension(2000,160));
            datumGumbiPanel12.setPreferredSize(new Dimension(2000,160));
            datumGumbiPanel111.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel112.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel113.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel121.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel122.setPreferredSize(new Dimension(2000,50));
            datumGumbiPanel123.setPreferredSize(new Dimension(2000,50));



            JPanel lijeviGumbPanel=new JPanel();
            lijeviGumbPanel.setBackground(new Color(100,100,100));
            lijeviGumbPanel.setPreferredSize(new Dimension(100,100));
            lijeviGumbPanel.add(left,BorderLayout.CENTER);

            JPanel desniGumbPanel=new JPanel();
            desniGumbPanel.setBackground(new Color(100,100,100));
            desniGumbPanel.setPreferredSize(new Dimension(100,100));
            desniGumbPanel.add(right,BorderLayout.CENTER);
            
            //Dodaj nove gumbe i njihove ActionListenere
            for (int i=0;i<6;i++){
                for(int j=0;j<7;j++){
                    String novistring;
                    JButton novi;
                    if(i*7+j+1-trenutnioffset>kolikoovajmjesecimadana){
                        int novidan=i*7+j+1-trenutnioffset-kolikoovajmjesecimadana;
                        int novimjesec=trenutnimjesec+2;
                        if(novimjesec==13){
                            novimjesec=1;
                        }
                        novistring=novidan + "." + novimjesec;
                        JButton temp=new JButton(novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setPreferredSize(new Dimension(100,50));
                        novi.setEnabled(false);
                    }else if(i*7+j+1-trenutnioffset<=0){
                        int novidan=i*7+j+1-trenutnioffset+kolikoproslimjesecimadana;
                        int novimjesec=trenutnimjesec;
                        if(novimjesec==0){
                            novimjesec=12;
                        }
                        novistring=novidan + "." + novimjesec;
                        JButton temp=new JButton(novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setPreferredSize(new Dimension(100,50));
                        novi.setEnabled(false);
                    }else{
                        int novidan=i*7+j+1-trenutnioffset;
                        int novimjesec=trenutnimjesec+1;
                        novistring=novidan + "." + novimjesec;
                        JButton temp=new JButton(novistring);
                        temp.addActionListener(this);
                        novi= temp;
                        novi.setPreferredSize(new Dimension(100,50));
                        novi.setEnabled(true);
                    }


                    gumbi.add(novi);
                    switch(i){
                        case 0:
                            datumGumbiPanel111.add(novi);
                            break;
                        case 1:
                            datumGumbiPanel112.add(novi);
                            break;
                        case 2:
                            datumGumbiPanel113.add(novi);
                            break;
                        case 3:
                            datumGumbiPanel121.add(novi);
                            break;
                        case 4:
                            datumGumbiPanel122.add(novi);
                            break;
                        default:
                            datumGumbiPanel123.add(novi);
                            break;
                    }
                }

            }
            
            datumGumbiPanel11.add(datumGumbiPanel111,BorderLayout.NORTH);
            datumGumbiPanel11.add(datumGumbiPanel112,BorderLayout.CENTER);
            datumGumbiPanel11.add(datumGumbiPanel113,BorderLayout.SOUTH);
            datumGumbiPanel12.add(datumGumbiPanel121,BorderLayout.NORTH);
            datumGumbiPanel12.add(datumGumbiPanel122,BorderLayout.CENTER);
            datumGumbiPanel12.add(datumGumbiPanel123,BorderLayout.SOUTH);

            datumGumbiPanel1.add(datumGumbiPanel11,BorderLayout.NORTH);
            datumGumbiPanel1.add(datumGumbiPanel12,BorderLayout.SOUTH);
            
            //Dodaj sve ostale stvari u frame
            this.add(lijeviGumbPanel,BorderLayout.WEST);
            this.add(desniGumbPanel,BorderLayout.EAST);
            this.add(naslovPanel,BorderLayout.NORTH);
            this.add(datumGumbiPanel1,BorderLayout.CENTER);

            this.revalidate();
            this.repaint();
            }
        
            
        //Ako je stisnut gumb sa datumom ispiši datum
        for (int i=0;i<6;i++){
            for(int j=0;j<7;j++){
                if(e.getSource()==gumbi.get(i*7+j)){
                    JButton neki=gumbi.get(i*7+j);
                    String[] datum=neki.getText().split("[.]");
                    int dan=Integer.parseInt(datum[0]);
                    int mjesec=Integer.parseInt(datum[1]);
                    int godina=trenutnagodina;
                    System.out.println(dan + "." + mjesec + "." + godina);
                    new RasporedFrame(dan,mjesec,godina);
                    
                }
            }
        }
    }
    //Funkcija koja traži offset
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

