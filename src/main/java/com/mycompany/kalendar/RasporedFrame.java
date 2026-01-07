/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

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
        String datum=dan + "." + mjesec + "." + godina;
        JLabel naslov=new JLabel();
        naslov.setText(datum);
        naslov.setBounds(400, 0, 200, 50);
        this.add(naslov);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000,500);
        this.setVisible(true);
    }
}
