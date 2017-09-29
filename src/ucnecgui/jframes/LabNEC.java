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

package ucnecgui.jframes;

import controllers.PDFReader;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import ucnecgui.Global;
import ucnecgui.MetaGlobal;
import ucnecgui.jpanels.NECLabPanel;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */

public class LabNEC extends javax.swing.JFrame {

    private Global global;
    private NECLabPanel nlp;
    /**
     *
     */
    public LabNEC() {
    }

    /**
     *Constructor de la clase LabNEC. Se encarga de mostrar los componentes pertenecientes al laboratorio virtual de Antenas y Propagación de UCNEC
     * @param global Objeto de la clase Global
     */
    public LabNEC(Global global) {
        initComponents();
        this.setTitle("LabNEC");
        this.global = global;
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setSize(800, 500);
        setResizable(false);        
        NECLabPanel nlp = new NECLabPanel(global);
        this.nlp = nlp;
        bodyPanel.add(nlp, BorderLayout.CENTER);
        bodyPanel.revalidate();
        bodyPanel.repaint();
        setLocationRelativeTo(null);     
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (MetaGlobal.getNf() != null) {
                    if (MetaGlobal.getNf().isVisible()){
                        dispose();
                    }else{
                        System.exit(0);
                    }                    
                }else{
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent we) {
                  if (MetaGlobal.getNf() != null) {
                    if (MetaGlobal.getNf().isVisible()){
                        dispose();
                    }else{
                        System.exit(0);
                    }                    
                }else{
                    System.exit(0);
                }
            }
        });
    }  
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        bodyPanel = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel2.setPreferredSize(new java.awt.Dimension(600, 100));
        jPanel2.setLayout(new java.awt.BorderLayout());

        bodyPanel.setBackground(new java.awt.Color(255, 255, 255));
        bodyPanel.setLayout(new java.awt.BorderLayout());
        jPanel2.add(bodyPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(36, 113, 163));
        jMenuBar1.setForeground(new java.awt.Color(255, 255, 255));

        jMenu2.setBackground(new java.awt.Color(36, 113, 163));
        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Simulador");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem3.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem3.setText("Ir a Simulador");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setBackground(new java.awt.Color(36, 113, 163));
        jMenu3.setForeground(new java.awt.Color(255, 255, 255));
        jMenu3.setText("Ayuda");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem5.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem5.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem5.setText("Ver Manual de Ayuda");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
      PDFReader.loadPdf("manual.pdf");
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

}
