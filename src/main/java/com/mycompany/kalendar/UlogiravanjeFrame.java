package com.mycompany.kalendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Vid
 */
public class UlogiravanjeFrame extends JFrame implements ActionListener{
    JButton ulogirajgumb;
    JButton registrirajgumb;
    JButton loginskipgumb;
    
    JTextField ime;
    JPasswordField lozinka;

    JTextField Rime;
    JPasswordField Rlozinka;
    JTextField Rmail;


    UlogiravanjeFrame(){   
        JLabel naslov=new JLabel("Ulogirajte se");
        naslov.setFont(new Font("Calibri",Font.PLAIN,50));
        JPanel naslovPanel1=new JPanel();
        
        JPanel naslovPanel11=new JPanel();
        naslovPanel11.setPreferredSize(new Dimension(2000,75));
        naslovPanel11.add(naslov,BorderLayout.CENTER);
        
        
        ime=new JTextField();
        lozinka=new JPasswordField();
        ime.setPreferredSize(new Dimension(100,30));
        lozinka.setPreferredSize(new Dimension(100,30));
        
        JPanel naslovPanel12=new JPanel();
        naslovPanel12.setPreferredSize(new Dimension(2000,50));
        JLabel imeLabel=new JLabel("Ime");
        JLabel lozinkaLabel=new JLabel("Lozinka");
        
        naslovPanel12.add(imeLabel);
        naslovPanel12.add(ime);
        naslovPanel12.add(lozinkaLabel,BorderLayout.CENTER);
        naslovPanel12.add(lozinka,BorderLayout.CENTER);
        
        
        ulogirajgumb=new JButton("Ulogiraj se");
        ulogirajgumb.setPreferredSize(new Dimension(100,50));
        ulogirajgumb.addActionListener(this);
        JPanel naslovPanel13=new JPanel();
        naslovPanel13.setPreferredSize(new Dimension(2000,60));
        naslovPanel13.add(ulogirajgumb,BorderLayout.CENTER);
        
        
        naslovPanel1.setPreferredSize(new Dimension(2000,250));
        naslovPanel1.setBackground(new Color(210,210,210));
        naslovPanel1.add(naslovPanel11,BorderLayout.NORTH);
        naslovPanel1.add(naslovPanel12,BorderLayout.CENTER);
        naslovPanel1.add(naslovPanel13,BorderLayout.SOUTH);
        //--------------------------------------------------------
        JLabel Rnaslov=new JLabel("Registriraj se");
        Rnaslov.setFont(new Font("Calibri",Font.PLAIN,50));
        JPanel RnaslovPanel1=new JPanel();
        
        JPanel RnaslovPanel11=new JPanel();
        RnaslovPanel11.setPreferredSize(new Dimension(2000,75));
        RnaslovPanel11.add(Rnaslov,BorderLayout.CENTER);
        
        
        Rime=new JTextField();
        Rlozinka=new JPasswordField();
        Rmail=new JTextField();
        Rime.setPreferredSize(new Dimension(100,30));
        Rlozinka.setPreferredSize(new Dimension(100,30));
        Rmail.setPreferredSize(new Dimension(100,30));
        
        JPanel RnaslovPanel12=new JPanel();
        RnaslovPanel12.setPreferredSize(new Dimension(2000,50));
        JLabel RimeLabel=new JLabel("Ime");
        JLabel RlozinkaLabel=new JLabel("Lozinka");
        JLabel RmailLabel=new JLabel("E-mail");
        
        RnaslovPanel12.add(RimeLabel,BorderLayout.CENTER);
        RnaslovPanel12.add(Rime,BorderLayout.CENTER);
        RnaslovPanel12.add(RlozinkaLabel,BorderLayout.CENTER);
        RnaslovPanel12.add(Rlozinka,BorderLayout.CENTER);
        RnaslovPanel12.add(RmailLabel,BorderLayout.CENTER);
        RnaslovPanel12.add(Rmail,BorderLayout.CENTER);
        
        
        registrirajgumb=new JButton("Registriraj me");
        registrirajgumb.setPreferredSize(new Dimension(150,50));
        registrirajgumb.addActionListener(this);
        JPanel RnaslovPanel13=new JPanel();
        RnaslovPanel13.setPreferredSize(new Dimension(2000,60));
        RnaslovPanel13.add(registrirajgumb,BorderLayout.CENTER);
        
        
        RnaslovPanel1.setPreferredSize(new Dimension(2000,250));
        RnaslovPanel1.setBackground(new Color(210,210,210));
        RnaslovPanel1.add(RnaslovPanel11,BorderLayout.NORTH);
        RnaslovPanel1.add(RnaslovPanel12,BorderLayout.CENTER);
        RnaslovPanel1.add(RnaslovPanel13,BorderLayout.SOUTH);
        //--------------------------------------------------------
        JLabel velikinaslov=new JLabel("ROKOVNIK");
        velikinaslov.setFont(new Font("Calibri",Font.PLAIN,100));
        JPanel velikinaslovPanel=new JPanel();
        velikinaslovPanel.setPreferredSize(new Dimension(200,120));
        velikinaslovPanel.setBorder(new EmptyBorder(0,0,100,0));
        velikinaslovPanel.add(velikinaslov,BorderLayout.CENTER);
        //-------------------------------------------------------
        JPanel diozaupisivanje=new JPanel();
        diozaupisivanje.setPreferredSize(new Dimension(1000,2000));
        diozaupisivanje.add(naslovPanel1,BorderLayout.NORTH);
        diozaupisivanje.add(RnaslovPanel1,BorderLayout.SOUTH);
        //-----------------------------------------------------
        loginskipgumb=new JButton("Developer log in skip");
        loginskipgumb.setPreferredSize(new Dimension(300,100));
        loginskipgumb.addActionListener(this);
        JLabel obavijest=new JLabel("Maknuti prije predaje");
        JPanel loginskipPanel=new JPanel();
        loginskipPanel.setPreferredSize(new Dimension(2000,150));
        loginskipPanel.add(loginskipgumb,BorderLayout.NORTH);
        loginskipPanel.add(obavijest,BorderLayout.SOUTH);
        
        this.add(velikinaslovPanel,BorderLayout.NORTH);
        this.add(diozaupisivanje,BorderLayout.CENTER);
        this.add(loginskipPanel,BorderLayout.SOUTH);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(1000,1000);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==loginskipgumb){
            System.out.println("Idem zatvorit prozor");
            System.out.println("I otvorit novi");
            KalendarFrame noviKalendarFrame=new KalendarFrame(1);
            noviKalendarFrame.setVisible(true);
            dispose();
        }
        if (e.getSource() == ulogirajgumb) {
                try {
                    int userid = AuthService.login(
                            ime.getText().trim(),
                            new String(lozinka.getPassword())
                    );

                    if (userid!=-1) {
                        JOptionPane.showMessageDialog(this, "Uspješna prijava!");
                        new KalendarFrame(userid).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Krivo korisničko ime ili lozinka.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Greška baze: " + ex.getMessage());
                }
            }

            if (e.getSource() == registrirajgumb) {
                try {
                    int userid = AuthService.register(
                            Rime.getText().trim(),
                            Rmail.getText().trim(),
                            new String(Rlozinka.getPassword())
                    );

                    if (userid!=-1) {
                        JOptionPane.showMessageDialog(this, "Registracija uspješna!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Korisnik ili email već postoji.");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Greška registracije: " + ex.getMessage());
                }
            }

        }

    }

