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
//Moj gumb
//Modificirani JButton da mu mogu odmah u konstruktoru namjestit lokaciju, veličinu i tekst
public class MyButton extends JButton {
    private String tekstgumba;
    MyButton(int xpozicija,int ypozicija,int širina, int visina,String tekst){
       this.setBounds(xpozicija,ypozicija,širina,visina);
       this.setText(tekst);
       tekstgumba=tekst;
   }
    //Ima i opciju da ispiše tekst, tek tak da provjerim radi li kad ga kliknem
    void isprintajtekst(){
        System.out.println(tekstgumba);
    }
}
