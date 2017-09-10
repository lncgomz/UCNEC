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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import ucnecgui.Global;
import ucnecgui.models.Ground;

/**
 *
 * @author Leoncio Gómez
 */
public class GroundTypePanel extends javax.swing.JPanel {

    private Global global;
    private JFrame frame;

    /**
     * Constructor de la clase GroundTypePanel
     *
     * @param global Objeto de la clase Global
     * @param frame Objeto de la clase JFrame correspondiente a la ventana que
     * invoca a este panel
     */
    public GroundTypePanel(Global global, JFrame frame) {
        initComponents();
        this.global = global;
        this.frame = frame;
        initializePanel();
        loadGNCard();
    }

    /**
     * Inicializar componentes del panel
     */
    public void initializePanel() {
        jRadioButton1.setText(Global.getMessages().
                getString("SimulationPanel.param.ground.jradiobutton1"));
        jRadioButton2.setText(Global.getMessages().
                getString("SimulationPanel.param.ground.jradiobutton2"));
        jRadioButton3.setText(Global.getMessages().
                getString("SimulationPanel.param.ground.jradiobutton3"));
        jLabel4.setText(Global.getMessages().
                getString("SimulationPanel.param.ground.jlabel4"));
        jLabel5.setText(Global.getMessages().
                getString("SimulationPanel.param.ground.jlabel5"));
        okbtn.setText(Global.getMessages().
                getString("GT.ok"));
        cancelbtn.setText(Global.getMessages().
                getString("GT.cancel"));

        //Comportamiento del botón "Real"
        jRadioButton3.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    jLabel4.setEnabled(true);
                    jLabel5.setEnabled(true);
                    jFormattedTextField1.setEnabled(true);
                    jFormattedTextField2.setEnabled(true);
                } else {
                    jLabel4.setEnabled(false);
                    jLabel5.setEnabled(false);
                    jFormattedTextField1.setEnabled(false);
                    jFormattedTextField2.setEnabled(false);
                }
            }
        });
    }

    /**
     * Verifica si todos los campos jTextFormattedField contienen información,
     * además, chequea la validez de los datos introducidos en el panel
     *
     * @return true si todos los campos jTextFormattedField contienen
     * información válida, de lo contrario, devuelve false
     */
    public boolean validateGround() {
        if (jRadioButton3.isSelected()) {
            return (!jFormattedTextField1.getText().
                    isEmpty() && !jFormattedTextField2.
                            getText().isEmpty());
        } else {
            return true;
        }
    }

    /**
     * Genera el comando NEC2 de tipo de tierra a partir de los parámetros
     * introducidos en el panel
     *
     * @return Comando de tipo de tierra de NEC2
     */
    public ArrayList<Ground> generateGNCard() {
        ArrayList<Ground> grounds = new ArrayList<Ground>();
        Ground ground = new Ground();
        if (jRadioButton1.isSelected()) { //Espacio Libre
            global.setCurrentGroundType(Global.FREESPACE);
            ground.setType(-1);
            ground.setNonReal(true);

        } else if (jRadioButton2.isSelected()) { //PEC
            global.setCurrentGroundType(Global.PEC);
            ground.setType(1);
            ground.setNonReal(true);
        } else {
            global.setCurrentGroundType(Global.REAL); //Real
            ground.setNonReal(false);
            ground.setType(2);
            ground.setRadialWires(0);
            ground.setEmpty1(0);
            ground.setEmpty2(0);
            ground.setRelDielConst(Double.
                    valueOf(jFormattedTextField2.getText().replace(",", ".")));
            ground.setConductivity(Double.
                    valueOf(jFormattedTextField1.getText().replace(",", ".")));
        }
        grounds.add(ground);
        return grounds;
    }

    /**
     * Convierte la información obtenida a partir de una descripción de tipo de
     * tierra en NEC2 a un objeto tipo Ground para desplegar los valores
     * correspondientes en los controles del panel
     */
    public void loadGNCard() {
        if (global.getgGround().size() > 0) {
            Ground ground = global.getgGround().get(0);
            switch (ground.getType()) {
                case -1:
                    jRadioButton1.setSelected(true); //Espacio LIbre
                    break;
                case 1:
                    jRadioButton2.setSelected(true); //PEC
                    break;
                case 2:
                    jRadioButton3.setSelected(true); //Real
                    break;
                default:
                    throw new AssertionError();
            }

            if (ground.getRelDielConst() != 0.0) {
                jFormattedTextField2.setText(ground.getRelDielConst() + "");
            }
            if (ground.getConductivity() != 0.0) {
                jFormattedTextField1.setText(ground.getConductivity() + "");
            }
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
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        okbtn = new javax.swing.JButton();
        cancelbtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jRadioButton1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Real");
        jPanel9.add(jRadioButton1);

        jRadioButton2.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("PEC");
        jPanel9.add(jRadioButton2);

        jRadioButton3.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Espacio Libre");
        jPanel9.add(jRadioButton3);

        jPanel2.add(jPanel9, java.awt.BorderLayout.NORTH);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.GridLayout(2, 1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("jLabel4");
        jLabel4.setEnabled(false);
        jPanel3.add(jLabel4);

        jFormattedTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jFormattedTextField1.setColumns(10);
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.###"))));
        jFormattedTextField1.setEnabled(false);
        jPanel3.add(jFormattedTextField1);

        jPanel10.add(jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("jLabel5");
        jLabel5.setEnabled(false);
        jPanel4.add(jLabel5);

        jFormattedTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jFormattedTextField2.setColumns(10);
        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jFormattedTextField2.setEnabled(false);
        jPanel4.add(jFormattedTextField2);

        jPanel10.add(jPanel4);

        jPanel2.add(jPanel10, java.awt.BorderLayout.CENTER);

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

        jPanel2.add(jPanel1, java.awt.BorderLayout.SOUTH);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
//Comportamiento del botón "Aceptar"
    private void okbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okbtnActionPerformed
        if (validateGround()) {
            global.setgGround(generateGNCard());
            frame.dispose();
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_okbtnActionPerformed
//Comportamiento del botón "Cancelar""
    private void cancelbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbtnActionPerformed
        frame.dispose();
    }//GEN-LAST:event_cancelbtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelbtn;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JButton okbtn;
    // End of variables declaration//GEN-END:variables
}
