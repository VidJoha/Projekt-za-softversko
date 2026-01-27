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
import java.sql.*;
import java.util.ArrayList;

public class NovaGrupaFrame extends JFrame {

    private JTextField nazivGrupeField;
    private JPanel usersPanel;
    private int currentUserId;

    public NovaGrupaFrame(int currentUserId) {
        this.currentUserId = currentUserId;

        setTitle("Stvori novu grupu");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        setLayout(new BorderLayout());
        
        nazivGrupeField = new JTextField();

        usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));

        ucitajUsere();

        JScrollPane scroll = new JScrollPane(usersPanel);

        JButton btnSpremi = new JButton("Stvori grupu");
        btnSpremi.addActionListener(e -> spremiGrupu());

        JButton btnOdustani = new JButton("Odustani");
        btnOdustani.addActionListener(e -> {
            dispose();
            new KalendarFrame(currentUserId).setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(btnSpremi);
        bottomPanel.add(btnOdustani);

        add(bottomPanel, BorderLayout.SOUTH);

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Naziv grupe:"), BorderLayout.NORTH);
        top.add(nazivGrupeField, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // 🔹 Učitavanje usera – SORTIRANO + bez kreatora
    private void ucitajUsere() {
        try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword())) {

            String sql = """
                SELECT user_id, username
                FROM users
                WHERE user_id != ?
                ORDER BY username ASC
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, currentUserId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usersPanel.add(
                        new CheckBoxUser(
                                rs.getInt("user_id"),
                                rs.getString("username")
                        )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju usera");
        }
    }

    private void spremiGrupu() {

        String naziv = nazivGrupeField.getText().trim();
        ArrayList<CheckBoxUser> clanovi = new ArrayList<>();

        for (Component c : usersPanel.getComponents()) {
            CheckBoxUser cb = (CheckBoxUser) c;
            if (cb.isSelected()) {
                clanovi.add(cb);
            }
        }

        if (naziv.isEmpty() || clanovi.size() < 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Grupa mora imati naziv i barem 2 člana!"
            );
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                DbConfig.getUrl(),
                DbConfig.getUser(),
                DbConfig.getPassword())) {

            conn.setAutoCommit(false);

            // 🔎 provjera imena grupe
            String checkSql = "SELECT COUNT(*) FROM groups WHERE name = ?";
            PreparedStatement psCheck = conn.prepareStatement(checkSql);
            psCheck.setString(1, naziv);

            ResultSet rsCheck = psCheck.executeQuery();
            rsCheck.next();

            if (rsCheck.getInt(1) > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Grupa s tim imenom već postoji!"
                );
                conn.rollback();
                return;
            }

            // 1️⃣ groups
            String sqlGroup = "INSERT INTO groups(name) VALUES (?)";
            PreparedStatement psGroup
                    = conn.prepareStatement(sqlGroup, Statement.RETURN_GENERATED_KEYS);

            psGroup.setString(1, naziv);
            psGroup.executeUpdate();

            ResultSet keys = psGroup.getGeneratedKeys();
            keys.next();
            int groupId = keys.getInt(1);

            // 2️⃣ group_members
            String sqlMember
                    = "INSERT INTO group_members(group_id, user_id, role) VALUES (?, ?, ?)";

            PreparedStatement psMember = conn.prepareStatement(sqlMember);

            // kreator
            psMember.setInt(1, groupId);
            psMember.setInt(2, currentUserId);
            psMember.setString(3, "admin");
            psMember.executeUpdate();

            // ostali članovi
            for (CheckBoxUser cb : clanovi) {
                psMember.setInt(1, groupId);
                psMember.setInt(2, cb.userId);
                psMember.setString(3, cb.getRole());
                psMember.executeUpdate();
            }

            conn.commit();

            JOptionPane.showMessageDialog(this, "Grupa uspješno stvorena!");
            dispose();

            new KalendarFrame(currentUserId).setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri spremanju grupe");
        }
    }

}
