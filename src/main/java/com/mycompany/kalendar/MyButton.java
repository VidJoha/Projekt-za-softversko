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

    private int ButtonDan;
    private int ButtonMjesec;
    private int ButtonGodina;
    MyButton(int xpozicija,int ypozicija,int širina, int visina,int dan,int mjesec, int godina){
       this.setBounds(xpozicija,ypozicija,širina,visina);
       this.setText(dan + "." + mjesec);
       ButtonDan=dan;
       ButtonMjesec=mjesec;
       ButtonGodina=godina;
       
       
    }
    Integer getDan(){
        return ButtonDan;
    }
    Integer getMjesec(){
        return ButtonMjesec;
    }
    Integer getGodina(){
        return ButtonGodina;
    }
   }
    //Ima i opciju da ispiše tekst, tek tak da provjerim radi li kad ga kliknem
    

