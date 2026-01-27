/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.kalendar;

/**
 *
 * @author Klara
 */
import javax.swing.*;
import java.awt.*;

public class CheckBoxUser extends JPanel {
    public int userId;
    private JCheckBox checkBox;
    private JTextField roleField;

    public CheckBoxUser(int userId, String username) {
        this.userId = userId;

        setLayout(new BorderLayout());

        checkBox = new JCheckBox(username);
        roleField = new JTextField("member");
        roleField.setPreferredSize(new Dimension(80, 24));
        roleField.setEnabled(false);

        // role se može unositi samo ako je user označen
        checkBox.addActionListener(e -> roleField.setEnabled(checkBox.isSelected()));

        add(checkBox, BorderLayout.CENTER);
        add(roleField, BorderLayout.EAST);
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public String getRole() {
        return roleField.getText().trim().isEmpty() ? "member" : roleField.getText().trim();
    }
}

