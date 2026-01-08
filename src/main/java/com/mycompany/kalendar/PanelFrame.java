/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Vid
 */
public class PanelFrame {
    public static void main(String[] args){
        JFrame frame = new JFrame();
        
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500,500));
        panel.setBackground(Color.lightGray);
        panel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        
        panel.add(new JButton("1"));
        panel.add(new JButton("2"));
        panel.add(new JButton("3"));
        panel.add(new JButton("4"));
        panel.add(new JButton("5"));
        
        frame.add(panel);
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setSize(1000,500);
        frame.setVisible(true);
}
}
