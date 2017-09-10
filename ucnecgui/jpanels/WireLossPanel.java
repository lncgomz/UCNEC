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

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import ucnecgui.Global;
import ucnecgui.models.WireLoss;

/**
 *
 * @author Leoncio Gómez
 */
public class WireLossPanel extends javax.swing.JPanel {

    private Global global;
    private JFrame frame;

    /**
     * Constructor de la clase WireLossPanel
     *
     * @param global Objeto de la clase Global
     * @param frame Ventana que invoca este panel
     */
    public WireLossPanel(Global global, JFrame frame) {
        initComponents();
        this.global = global;
        this.frame = frame;
        initializePanel();
        loadLDCard();
    }

    /**
     * Inicialización de los componentes del panel
     */
    public void initializePanel() {
        jLabel8.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.label8"));
        jLabel9.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.label9"));
        jRadioButton4.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.jradiobutton4"));
        jRadioButton5.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.jradiobutton5"));
        jRadioButton6.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.jradiobutton6"));
        jRadioButton7.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.jradiobutton7"));
        jRadioButton8.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.jradiobutton8"));
        jRadioButton9.setText(Global.getMessages().
                getString("SimulationPanel.param.wireloss.jradiobutton9"));
        okbtn.setText(Global.getMessages().
                getString("GT.ok"));
        cancelbtn.setText(Global.getMessages().
                getString("GT.cancel"));

        // Comportamiento del JRadioButton Personalizado
        jRadioButton9.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    jLabel8.setEnabled(true);
                    jLabel9.setEnabled(true);
                    jFormattedTextField4.setEnabled(true);
                    jFormattedTextField5.setEnabled(true);
                } else {
                    jLabel8.setEnabled(false);
                    jLabel9.setEnabled(false);
                    jFormattedTextField4.setEnabled(false);
                    jFormattedTextField5.setEnabled(false);
                }
            }
        });

        //Comportamiento del botón Aceptar
        okbtn.addActionListener((ActionEvent e) -> {
            if (validateWireloss()) {
                global.setgWl(generateLDCard());
                frame.dispose();
            } else {
                global.errorValidateInput();
            }

        });

        //Comportamiento del botón Cancelar
        cancelbtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
    }

    /**
     * Verifica si todos los campos jTextFormattedField contienen información,
     * además, chequea la validez de los datos introducidos en el panel
     *
     * @return true si todos los campos jTextFormattedField contienen
     * información válida, de lo contrario, devuelve false
     */
    public boolean validateWireloss() {
        if (jRadioButton9.isSelected()) {
            return (!jFormattedTextField4.getText().
                    isEmpty() && !jFormattedTextField5.
                            getText().isEmpty());
        } else {
            return true;
        }
    }

    /**
     * Genera el comando NEC2 de tipo cargas (Un tipo especial que configura la
     * pérdida de los alambres en la descripción de la geometría) a partir de
     * los parámetros introducidos en el panel
     *
     * @return Comando de unidades de NEC2
     */
    public ArrayList<WireLoss> generateLDCard() {
        ArrayList<WireLoss> WireLoss = new ArrayList<WireLoss>();
        int wires = global.getgWires().size();
        if (jRadioButton4.isSelected()) {
            return WireLoss;
        } else if (jRadioButton9.isSelected()) {

            for (int i = 0; i < wires; i++) {
                if (i == global.getCurrentSourceTag() - 1) {
                    continue;
                }
                WireLoss nLoad = new WireLoss();
                nLoad.setType(6);
                nLoad.setWireNumber(i + 1);

                nLoad.setConductivity(1 / Double.
                        valueOf(jFormattedTextField4.
                                getText()));
                nLoad.setPermeability(Double.
                        valueOf(jFormattedTextField5.
                                getText()));
                WireLoss.add(nLoad);
            }
        } else {
            for (int i = 0; i < wires; i++) {
                if (i == global.getCurrentSourceTag() - 1) {
                    continue;
                }
                WireLoss nLoad = new WireLoss();
                nLoad.setType(5);
                nLoad.setWireNumber(i + 1);
                if (jRadioButton5.isSelected()) {
                    nLoad.setConductivity(1 / 1.74E-08);
                } else if (jRadioButton6.isSelected()) {
                    nLoad.setConductivity(1 / 4E-08);
                } else if (jRadioButton7.isSelected()) {
                    nLoad.setConductivity(1 / 1.14E-07);
                } else if (jRadioButton8.isSelected()) {
                    nLoad.setConductivity(1 / 6E-08);
                }
                WireLoss.add(nLoad);
            }
        }
        return WireLoss;
    }

    /**
     * Convierte la información obtenida a partir de una descripción de pérdidas
     * en el alambre en NEC2 a un objeto tipo WireLoss para desplegar los
     * valores correspondientes en los controles del panel
     */
    public void loadLDCard() {
        if (global.getgWl().size() > 0) {
            WireLoss wireLoss = global.getgWl().get(0);
            if (wireLoss.getType() == 6) {
                jRadioButton9.setSelected(true);
                jFormattedTextField4.setText((1 / wireLoss.getConductivity()) + "");
                jFormattedTextField5.setText(wireLoss.getPermeability() + "");
            } else if (wireLoss.getType() == 5) {
                if (wireLoss.getConductivity() == 1 / 1.74E-08) {
                    jRadioButton5.setSelected(true);
                } else if (wireLoss.getConductivity() == 1 / 4E-08) {
                    jRadioButton6.setSelected(true);
                } else if (wireLoss.getConductivity() == 1 / 1.14E-07) {
                    jRadioButton7.setSelected(true);
                } else if (wireLoss.getConductivity() == 1 / 6E-08) {
                    jRadioButton8.setSelected(true);
                }
            }
        } else {
            jRadioButton4.setSelected(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        okbtn = new javax.swing.JButton();
        cancelbtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jPanel11 = new javax.swing.JPanel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 50));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        okbtn.setBackground(new java.awt.Color(255, 255, 255));
        okbtn.setForeground(new java.awt.Color(0, 0, 50));
        okbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        okbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okbtnActionPerformed(evt);
            }
        });
        jPanel1.add(okbtn);

        cancelbtn.setBackground(new java.awt.Color(255, 255, 255));
        cancelbtn.setForeground(new java.awt.Color(0, 0, 50));
        cancelbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        cancelbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelbtnActionPerformed(evt);
            }
        });
        jPanel1.add(cancelbtn);

        add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 200));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new java.awt.GridLayout(2, 2));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("jLabel8");
        jLabel8.setEnabled(false);
        jPanel12.add(jLabel8);

        jFormattedTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.###"))));
        jFormattedTextField4.setEnabled(false);
        jPanel12.add(jFormattedTextField4);

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("jLabel9");
        jLabel9.setEnabled(false);
        jPanel12.add(jLabel9);

        jFormattedTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jFormattedTextField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.###"))));
        jFormattedTextField5.setEnabled(false);
        jPanel12.add(jFormattedTextField5);

        jPanel2.add(jPanel12, java.awt.BorderLayout.SOUTH);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new java.awt.GridLayout(3, 2));

        jRadioButton4.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("Sin Pérdida");
        jPanel11.add(jRadioButton4);

        jRadioButton5.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setText("jRadioButton5");
        jPanel11.add(jRadioButton5);

        jRadioButton6.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setText("jRadioButton6");
        jPanel11.add(jRadioButton6);

        jRadioButton7.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton7);
        jRadioButton7.setText("jRadioButton7");
        jPanel11.add(jRadioButton7);

        jRadioButton8.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton8);
        jRadioButton8.setText("jRadioButton8");
        jPanel11.add(jRadioButton8);

        jRadioButton9.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton9);
        jRadioButton9.setText("jRadioButton9");
        jPanel11.add(jRadioButton9);

        jPanel2.add(jPanel11, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void okbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okbtnActionPerformed
        if (validateWireloss()) {
            global.setgWl(generateLDCard());
            frame.dispose();
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_okbtnActionPerformed

    private void cancelbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbtnActionPerformed
        frame.dispose();
    }//GEN-LAST:event_cancelbtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelbtn;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JButton okbtn;
    // End of variables declaration//GEN-END:variables
}
