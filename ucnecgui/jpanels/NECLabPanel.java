/*
 * Copyright (C) 2017 Universidad de Carabobo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ucnecgui.jpanels;

import controllers.PDFReader;
import java.awt.BorderLayout;
import javax.swing.SwingUtilities;
import ucnecgui.Global;
import ucnecgui.jframes.MultiLabFrame;

/**
 *
 * @author Leoncio Gómez
 */

public class NECLabPanel extends javax.swing.JPanel {

    private Global global;
    
    /**
     * Constructor de la clase NECLabPanel
     * @param global Objeto de la clase Global
     */
    public NECLabPanel(Global global) {
        initComponents();
        this.global = global;              
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        o1 = new javax.swing.JButton();
        t1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        a1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        o2 = new javax.swing.JButton();
        t2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        a2 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        o3 = new javax.swing.JButton();
        t3 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        a3 = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        o4 = new javax.swing.JButton();
        t4 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        a4 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(2, 2));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163)));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(36, 113, 163));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MEDICIÓN DE IMPEDANCIA DE ENTRADA DE UNA ANTENA");
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel3.setForeground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(150, 198));
        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        o1.setBackground(new java.awt.Color(255, 255, 255));
        o1.setForeground(new java.awt.Color(0, 0, 0));
        o1.setText("OBJETIVOS");
        o1.setBorder(null);
        o1.setBorderPainted(false);
        o1.setPreferredSize(new java.awt.Dimension(15, 32));
        o1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o1ActionPerformed(evt);
            }
        });
        jPanel3.add(o1);

        t1.setBackground(new java.awt.Color(255, 255, 255));
        t1.setForeground(new java.awt.Color(0, 0, 0));
        t1.setText("TEORÍA");
        t1.setBorder(null);
        t1.setBorderPainted(false);
        t1.setPreferredSize(new java.awt.Dimension(15, 32));
        t1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1ActionPerformed(evt);
            }
        });
        jPanel3.add(t1);

        jPanel5.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel7.setPreferredSize(new java.awt.Dimension(300, 198));
        jPanel7.setLayout(new java.awt.BorderLayout());

        a1.setBackground(new java.awt.Color(255, 255, 255));
        a1.setForeground(new java.awt.Color(0, 0, 0));
        a1.setText("ACCEDER");
        a1.setBorder(null);
        a1.setBorderPainted(false);
        a1.setPreferredSize(new java.awt.Dimension(15, 32));
        a1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a1ActionPerformed(evt);
            }
        });
        jPanel7.add(a1, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel6);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163)));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel4.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(150, 198));
        jPanel4.setLayout(new java.awt.GridLayout(2, 1));

        o2.setBackground(new java.awt.Color(255, 255, 255));
        o2.setForeground(new java.awt.Color(0, 0, 0));
        o2.setText("OBJETIVOS");
        o2.setBorder(null);
        o2.setBorderPainted(false);
        o2.setPreferredSize(new java.awt.Dimension(15, 32));
        o2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o2ActionPerformed(evt);
            }
        });
        jPanel4.add(o2);

        t2.setBackground(new java.awt.Color(255, 255, 255));
        t2.setForeground(new java.awt.Color(0, 0, 0));
        t2.setText("TEORÍA");
        t2.setBorder(null);
        t2.setBorderPainted(false);
        t2.setPreferredSize(new java.awt.Dimension(15, 32));
        t2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2ActionPerformed(evt);
            }
        });
        jPanel4.add(t2);

        jPanel9.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel8.setPreferredSize(new java.awt.Dimension(300, 198));
        jPanel8.setLayout(new java.awt.BorderLayout());

        a2.setBackground(new java.awt.Color(255, 255, 255));
        a2.setForeground(new java.awt.Color(0, 0, 0));
        a2.setText("ACCEDER");
        a2.setBorder(null);
        a2.setBorderPainted(false);
        a2.setPreferredSize(new java.awt.Dimension(15, 32));
        a2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a2ActionPerformed(evt);
            }
        });
        jPanel8.add(a2, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel16.setBackground(new java.awt.Color(36, 113, 163));
        jPanel16.setForeground(new java.awt.Color(255, 255, 255));
        jPanel16.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("MEDICIÓN DE LA POLARIZACIÓN DE UNA ANTENA");
        jPanel16.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel16, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel9);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163)));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel12.setForeground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(150, 198));
        jPanel12.setLayout(new java.awt.GridLayout(2, 1));

        o3.setBackground(new java.awt.Color(255, 255, 255));
        o3.setForeground(new java.awt.Color(0, 0, 0));
        o3.setText("OBJETIVOS");
        o3.setBorder(null);
        o3.setBorderPainted(false);
        o3.setPreferredSize(new java.awt.Dimension(15, 32));
        o3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o3ActionPerformed(evt);
            }
        });
        jPanel12.add(o3);

        t3.setBackground(new java.awt.Color(255, 255, 255));
        t3.setForeground(new java.awt.Color(0, 0, 0));
        t3.setText("TEORÍA");
        t3.setBorder(null);
        t3.setBorderPainted(false);
        t3.setPreferredSize(new java.awt.Dimension(15, 32));
        t3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t3ActionPerformed(evt);
            }
        });
        jPanel12.add(t3);

        jPanel10.add(jPanel12, java.awt.BorderLayout.WEST);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel13.setPreferredSize(new java.awt.Dimension(300, 198));
        jPanel13.setLayout(new java.awt.BorderLayout());

        a3.setBackground(new java.awt.Color(255, 255, 255));
        a3.setForeground(new java.awt.Color(0, 0, 0));
        a3.setText("ACCEDER");
        a3.setBorder(null);
        a3.setBorderPainted(false);
        a3.setPreferredSize(new java.awt.Dimension(15, 32));
        a3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a3ActionPerformed(evt);
            }
        });
        jPanel13.add(a3, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel17.setBackground(new java.awt.Color(36, 113, 163));
        jPanel17.setForeground(new java.awt.Color(255, 255, 255));
        jPanel17.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("MEDICIÓN DEL DIAGRAMA DE RADIACIÓN DE UNA ANTENA");
        jPanel17.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel17, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163)));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel14.setForeground(new java.awt.Color(255, 255, 255));
        jPanel14.setPreferredSize(new java.awt.Dimension(150, 198));
        jPanel14.setLayout(new java.awt.GridLayout(2, 1));

        o4.setBackground(new java.awt.Color(255, 255, 255));
        o4.setForeground(new java.awt.Color(0, 0, 0));
        o4.setText("OBJETIVOS");
        o4.setBorder(null);
        o4.setBorderPainted(false);
        o4.setPreferredSize(new java.awt.Dimension(15, 32));
        o4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                o4ActionPerformed(evt);
            }
        });
        jPanel14.add(o4);

        t4.setBackground(new java.awt.Color(255, 255, 255));
        t4.setForeground(new java.awt.Color(0, 0, 0));
        t4.setText("TEORÍA");
        t4.setBorder(null);
        t4.setBorderPainted(false);
        t4.setPreferredSize(new java.awt.Dimension(15, 32));
        t4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t4ActionPerformed(evt);
            }
        });
        jPanel14.add(t4);

        jPanel11.add(jPanel14, java.awt.BorderLayout.WEST);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 2));
        jPanel15.setPreferredSize(new java.awt.Dimension(300, 198));
        jPanel15.setLayout(new java.awt.BorderLayout());

        a4.setBackground(new java.awt.Color(255, 255, 255));
        a4.setForeground(new java.awt.Color(0, 0, 0));
        a4.setText("ACCEDER");
        a4.setBorder(null);
        a4.setBorderPainted(false);
        a4.setPreferredSize(new java.awt.Dimension(15, 32));
        a4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                a4ActionPerformed(evt);
            }
        });
        jPanel15.add(a4, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel18.setBackground(new java.awt.Color(36, 113, 163));
        jPanel18.setForeground(new java.awt.Color(255, 255, 255));
        jPanel18.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("MEDICIÓN DE LA GANANCIA DE UNA ANTENA");
        jPanel18.add(jLabel4, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel18, java.awt.BorderLayout.NORTH);

        jPanel2.add(jPanel11);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
//Comportamiento del botón Acceder de la práctica 1
    private void a1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a1ActionPerformed
        Lab1Panel l1p = new Lab1Panel(global);
        MultiLabFrame mlf = new MultiLabFrame(l1p.getPreferredSize().width, l1p.getPreferredSize().height, "Laboratorio 1: MEDICIÓN DE IMPEDANCIA DE ENTRADA DE UNA ANTENA", global, SwingUtilities.getWindowAncestor(this));
        mlf.add(l1p, BorderLayout.CENTER);
        mlf.setVisible(true);
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }//GEN-LAST:event_a1ActionPerformed
//Comportamiento del botón Objetivos de la práctica 1
    private void o1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o1ActionPerformed
         PDFReader.loadPdf("objetivosLab1.pdf");
    }//GEN-LAST:event_o1ActionPerformed
//Comportamiento del botón Teoría de la práctica 1
    private void t1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1ActionPerformed
      PDFReader.loadPdf("teoriaLab1.pdf");
    }//GEN-LAST:event_t1ActionPerformed
//Comportamiento del botón Objetivos de la práctica 2
    private void o2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o2ActionPerformed
       PDFReader.loadPdf("objetivosLab2.pdf");
    }//GEN-LAST:event_o2ActionPerformed
//Comportamiento del botón Teoría de la práctica 2
    private void t2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2ActionPerformed
      PDFReader.loadPdf("teoriaLab2.pdf");
    }//GEN-LAST:event_t2ActionPerformed
//Comportamiento del botón Acceder de la práctica 2
    private void a2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a2ActionPerformed
        Lab2Panel l2p = new Lab2Panel(global);
        MultiLabFrame mlf = new MultiLabFrame(l2p.getPreferredSize().width, l2p.getPreferredSize().height, "Laboratorio 2: MEDICIÓN DE LA POLARIZACIÓN DE UNA ANTENA", global, SwingUtilities.getWindowAncestor(this));
        mlf.add(l2p, BorderLayout.CENTER);
        mlf.setVisible(true);
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }//GEN-LAST:event_a2ActionPerformed
//Comportamiento del botón Objetivos de la práctica 3
    private void o3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o3ActionPerformed
       PDFReader.loadPdf("objetivosLab3.pdf");
    }//GEN-LAST:event_o3ActionPerformed
//Comportamiento del botón Teoría de la práctica 3
    private void t3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t3ActionPerformed
       PDFReader.loadPdf("teoriaLab3.pdf");
    }//GEN-LAST:event_t3ActionPerformed
//Comportamiento del botón Acceder de la práctica 3
    private void a3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a3ActionPerformed
        Lab3Panel l3p = new Lab3Panel(global);  
        MultiLabFrame mlf = new MultiLabFrame(l3p.getPreferredSize().width, l3p.getPreferredSize().height, "Laboratorio 3: MEDICIÓN DEL DIAGRAMA DE RADIACIÓN DE UNA ANTENA", global, SwingUtilities.getWindowAncestor(this));
        mlf.add(l3p, BorderLayout.CENTER);
        mlf.setVisible(true);
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }//GEN-LAST:event_a3ActionPerformed
//Comportamiento del botón Objetivos de la práctica 4
    private void o4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_o4ActionPerformed
     PDFReader.loadPdf("objetivosLab4.pdf");
    }//GEN-LAST:event_o4ActionPerformed

    private void t4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t4ActionPerformed
      PDFReader.loadPdf("teoriaLab4.pdf");
    }//GEN-LAST:event_t4ActionPerformed
//Comportamiento del botón Acceder de la práctica 4
    private void a4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_a4ActionPerformed
         Lab4Panel l4p = new Lab4Panel(global);
        MultiLabFrame mlf = new MultiLabFrame(l4p.getPreferredSize().width, l4p.getPreferredSize().height, "Laboratorio 4: MEDICIÓN DE LA GANANCIA DE UNA ANTENA", global, SwingUtilities.getWindowAncestor(this));
        mlf.add(l4p, BorderLayout.CENTER);
        mlf.setVisible(true);
        SwingUtilities.getWindowAncestor(this).setVisible(false);
    }//GEN-LAST:event_a4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton a1;
    private javax.swing.JButton a2;
    private javax.swing.JButton a3;
    private javax.swing.JButton a4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton o1;
    private javax.swing.JButton o2;
    private javax.swing.JButton o3;
    private javax.swing.JButton o4;
    private javax.swing.JButton t1;
    private javax.swing.JButton t2;
    private javax.swing.JButton t3;
    private javax.swing.JButton t4;
    // End of variables declaration//GEN-END:variables
}
