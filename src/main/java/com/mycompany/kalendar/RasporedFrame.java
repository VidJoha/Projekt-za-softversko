/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 *
 * @author Vid
 */
public class RasporedFrame extends JFrame{
    private int RasporedDan;
    private int RasporedMjesec;
    private int RasporedGodina;
    RasporedFrame(int dan, int mjesec, int godina){
        RasporedDan=dan;
        RasporedMjesec=mjesec;
        RasporedGodina=godina;
        
        //Dodat naslov
        String datum=dan + "." + mjesec + "." + godina;
        JLabel naslov=new JLabel();
        naslov.setText(datum);
        naslov.setBounds(500, 0, 200, 50);
        
        //Dodani gumbi za dodat, premjestit i maknut sastanak
        JButton dodaj=new JButton();
        dodaj.setText("Dodaj sastanak");
        dodaj.setBounds(100,300,200,50);
        JButton premjesti=new JButton();
        premjesti.setText("Premjesti sastanak");
        premjesti.setBounds(400,300,200,50);
        JButton makni=new JButton();
        makni.setText("Makni sastanak");
        makni.setBounds(700,300,200,50);
        
        this.add(naslov);
        this.add(dodaj);
        this.add(premjesti);
        this.add(makni);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000,500);
        this.setVisible(true);
    }
}
