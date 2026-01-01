/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author Vid
 */
public class MyFrame extends JFrame implements ActionListener{
    ArrayList<MyButton> gumbi= new ArrayList<>();
    JButton button;
    MyFrame(int offset,int brojdanasadasnji, int brojdanaprosli){
        
        for (int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                String novistring;
                if(j*7+i+1-offset>brojdanasadasnji){
                    novistring=(j*7+i+1-offset-brojdanasadasnji)+"."+1+".";
                }else if(j*7+i+1-offset<=0){
                    novistring=(j*7+i+1-offset+brojdanaprosli)+"."+1+".";
                }else{
                    novistring=(j*7+i+1-offset)+"."+1+".";
                }
                
                MyButton novi= new MyButton(100*i,50*j,100,50,novistring);
                gumbi.add(novi);
                novi.addActionListener(this);
                this.add(novi);
            }
            
        }
        button = new JButton();
        button.setBounds(400,400,100,100);
        button.setText("Glavni gumb");
        button.addActionListener(this);
        this.add(button);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(500,500);
        this.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==button){
            System.out.println("aha");
        }
        for (int i=0;i<7;i++){
            for(int j=0;j<6;j++){
                if(e.getSource()==gumbi.get(j*7+i)){
                    MyButton neki=gumbi.get(j*7+i);
                    neki.isprintajtekst();
                }
            }
        }
        
        
        
    }
}
