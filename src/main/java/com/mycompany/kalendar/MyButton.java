/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import javax.swing.JButton;

/**
 *
 * @author Vid
 */
public class MyButton extends JButton {
    private String tekstgumba;
    MyButton(int xpozicija,int ypozicija,int širina, int visina,String tekst){
       this.setBounds(xpozicija,ypozicija,širina,visina);
       this.setText(tekst);
       tekstgumba=tekst;
   }
    void isprintajtekst(){
        System.out.println(tekstgumba);
    }
}
