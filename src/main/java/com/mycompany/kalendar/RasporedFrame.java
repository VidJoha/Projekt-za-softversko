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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 * @author Vid
 */
public class RasporedFrame  extends JFrame implements ActionListener{
    private int RasporedDan;
    private int RasporedMjesec;
    private int RasporedGodina;
    
    JButton dodaj;
    JButton premjesti;
    JButton makni;
    JButton izađi;
    RasporedFrame(int dan, int mjesec, int godina){
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
        
        premjesti=new JButton();
        premjesti.setText("Premjesti sastanak");
        premjesti.setFont(new Font("Calibri",Font.PLAIN,20));
        premjesti.setMargin(new Insets(0, 0, 0, 0));

        makni=new JButton();
        makni.setText("Makni sastanak");
        makni.setFont(new Font("Calibri",Font.PLAIN,20));
        makni.setMargin(new Insets(0, 0, 0, 0));
        
        izađi=new JButton();
        izađi.setText("Vrati se na kalendar");
        izađi.setFont(new Font("Calibri",Font.PLAIN,20));
        izađi.setMargin(new Insets(0, 0, 0, 0));
        izađi.addActionListener(this);
        
        dodaj.setPreferredSize(new Dimension(180,80));
        premjesti.setPreferredSize(new Dimension(180,80));
        makni.setPreferredSize(new Dimension(180,80));
        izađi.setPreferredSize(new Dimension(180,80));
        
        JPanel gumbiPanel=new JPanel();
        gumbiPanel.setPreferredSize(new Dimension(200,1000));

        gumbiPanel.setBackground(new Color(100,100,100));
        gumbiPanel.add(dodaj);
        gumbiPanel.add(premjesti);
        gumbiPanel.add(makni);
        gumbiPanel.add(izađi);
        
        JPanel rasporedPanel=new JPanel();
        rasporedPanel.setBackground(new Color(220,220,220));
        rasporedPanel.setPreferredSize(new Dimension(200,200));
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
            System.out.println("Idem zatvorit prozor");
            System.out.println("I otvorit novi");
            KalendarFrame noviKalendarFrame=new KalendarFrame(RasporedGodina,RasporedMjesec-1);
            noviKalendarFrame.setVisible(true);
            dispose();
        }
    
    }
}
