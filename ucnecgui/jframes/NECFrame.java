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

import controllers.QRGenerator;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FilenameUtils;
import ucnecgui.Global;
import ucnecgui.MetaGlobal;
import ucnecgui.jpanels.About;
import ucnecgui.jpanels.NECModulePanel;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class NECFrame extends javax.swing.JFrame {

    private Global global;
    private NECModulePanel nmp;

    public NECFrame() {
    }

    /**
     * Constructor de la clase NECFrame. Se encarga de mostrar la ventana
     * principal del Módulo NEC.
     *
     * @param global Objeto de la clase Global
     */
    public NECFrame(Global global) {
        initComponents();
        this.setTitle("UCNEC");
        this.global = global;
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setSize(800, 500);
        setResizable(false);
        NECModulePanel nmp = new NECModulePanel(global);
        this.nmp = nmp;
        bodyPanel.add(nmp, BorderLayout.CENTER);
        bodyPanel.revalidate();
        bodyPanel.repaint();
        setLocationRelativeTo(null);
        MetaGlobal.setNmp(nmp);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (MetaGlobal.getLn() != null) {
                    if (MetaGlobal.getLn().isVisible()) {
                        dispose();
                    } else {
                        System.exit(0);
                    }
                } else {
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent we) {
                if (MetaGlobal.getLn() != null) {
                    if (MetaGlobal.getLn().isVisible()) {
                        dispose();
                    } else {
                        System.exit(0);
                    }
                } else {
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
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        goToLab = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        goToAbout = new javax.swing.JMenuItem();

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

        jMenu1.setBackground(new java.awt.Color(36, 113, 163));
        jMenu1.setForeground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem1.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem1.setText("Abrir");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem2.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem2.setText("Guardar");
        jMenuItem2.setToolTipText("");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenu5.setBackground(new java.awt.Color(255, 255, 255));
        jMenu5.setForeground(new java.awt.Color(0, 0, 0));
        jMenu5.setText("Exportar");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem4.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem4.setText("Código QR");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem4);

        jMenu1.add(jMenu5);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        jMenuItem10.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem10.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem10.setText("Salir");
        jMenuItem10.setToolTipText("");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem10);

        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(36, 113, 163));
        jMenu2.setForeground(new java.awt.Color(255, 255, 255));
        jMenu2.setText("Descripción");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem3.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem3.setText("Importar");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        jMenu3.setBackground(new java.awt.Color(36, 113, 163));
        jMenu3.setForeground(new java.awt.Color(255, 255, 255));
        jMenu3.setText("Laboratorio");

        goToLab.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        goToLab.setBackground(new java.awt.Color(255, 255, 255));
        goToLab.setForeground(new java.awt.Color(0, 0, 0));
        goToLab.setText("Ir a Laboratorio");
        goToLab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToLabActionPerformed(evt);
            }
        });
        jMenu3.add(goToLab);

        jMenuBar1.add(jMenu3);

        jMenu4.setBackground(new java.awt.Color(36, 113, 163));
        jMenu4.setForeground(new java.awt.Color(255, 255, 255));
        jMenu4.setText("Acerca de");

        goToAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        goToAbout.setBackground(new java.awt.Color(255, 255, 255));
        goToAbout.setForeground(new java.awt.Color(0, 0, 0));
        goToAbout.setText("UCNEC");
        goToAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToAboutActionPerformed(evt);
            }
        });
        jMenu4.add(goToAbout);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        global.saveFile();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ArrayList<String> data = global.openFile();
        if (data != null) {
            Global.loadInputFile(data, global);
            nmp.initializeInfo(global);
        } else {
            global.errorMessage("Messages.loadAntenna.title", "Messages.loadAntenna.FAILED");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        dispose();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        ArrayList<String> data = global.openFile();
        if (data != null) {
            Global.importInputFile(data, global);
            nmp.initializeInfo(global);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void goToLabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToLabActionPerformed
         MetaGlobal.getLn().setVisible(true);
    }//GEN-LAST:event_goToLabActionPerformed

    private void goToAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToAboutActionPerformed
        About about = new About();
        MultiFrame mf = new MultiFrame(300, 300, "Acerca de UCNEC");
        mf.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        mf.add(about);
        mf.pack();
        mf.setVisible(true);
    }//GEN-LAST:event_goToAboutActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        ArrayList<String> qrData = global.generateQRData();
        JFileChooser selectfile = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Files", "png");
        selectfile.setFileFilter(filter);
        selectfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = selectfile.showSaveDialog(null);
        if (result != 1) {

            File pngFile = selectfile.getSelectedFile();
            if (pngFile == null || pngFile.getName().isEmpty()) {
                //Error Messages
            }
            if (FilenameUtils.getExtension(pngFile.getName()).equalsIgnoreCase("png")) {
            } else {
                pngFile = new File(pngFile.toString() + ".png");
            }
            String path = pngFile.getAbsolutePath();

            try {
                QRGenerator.generate(path, qrData, global);
            } catch (IOException ex) {
                Logger.getLogger(NECFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            global.errorMessage("Messages.generateQR.title", "Messages.generateQR.FAULT");
        }

    }//GEN-LAST:event_jMenuItem4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bodyPanel;
    private javax.swing.JMenuItem goToAbout;
    private javax.swing.JMenuItem goToLab;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

}
