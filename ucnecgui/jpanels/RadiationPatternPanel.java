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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JFrame;
import ucnecgui.Global;
import ucnecgui.models.RadiationPattern;

/**
 *
 * @author Leoncio Gómez
 */
public class RadiationPatternPanel extends javax.swing.JPanel {

    private Global global;
    private JFrame frame;

    /**
     * Constructor de la clase RadiationPatternPanel
     *
     * @param global Objeto de la clase Global
     * @param frame Ventana que invoca al panel
     */
    public RadiationPatternPanel(Global global, JFrame frame) {
        initComponents();
        this.global = global;
        this.frame = frame;
        loadRPCard();
        initializePanel();

    }

    /**
     * Inicializa los componentes del panel
     */
    public void initializePanel() {

        //
        jLabel3.setText(Global.getMessages().
                getString("SimulationPanel.param.radiationpattern.jlabel3"));
        jLabel6.setText(Global.getMessages().
                getString("SimulationPanel.param.radiationpattern.jlabel6"));
        jLabel7.setText(Global.getMessages().
                getString("SimulationPanel.param.radiationpattern.jlabel7"));
        jLabel10.setText(Global.getMessages().
                getString("TransformationPanel.rotation.degrees.label"));
        jLabel11.setText(Global.getMessages().
                getString("TransformationPanel.rotation.degrees.label"));
        jRadioButton15.setText(Global.getMessages().
                getString("SimulationPanel.param.radiationpattern.jradiobutton15"));
        jRadioButton16.setText(Global.getMessages().
                getString("SimulationPanel.param.radiationpattern.jradiobutton16"));
        jRadioButton17.setText(Global.getMessages().
                getString("SimulationPanel.param.radiationpattern.jradiobutton17"));
        okbtn.setText(Global.getMessages().
                getString("GT.ok"));
        cancelbtn.setText(Global.getMessages().
                getString("GT.cancel"));

        //Comportamiento del JRadioButton Azimuth
        jRadioButton15.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                global.setCurrentPlotType(Global.AZIMUTHPLOT);
                if (state == ItemEvent.SELECTED) {
                    jPanel17.setVisible(true);
                    jLabel6.setEnabled(true);
                    jLabel6.setText(Global.getMessages().
                            getString("SimulationPanel.param.radiationpattern.jlabel6") + " de Elevación");
                    jLabel7.setEnabled(true);
                    jLabel7.setText(Global.getMessages().
                            getString("SimulationPanel.param.radiationpattern.jlabel7"));
                    jFormattedTextField6.setEnabled(true);
                    jFormattedTextField7.setEnabled(true);
                    repaint();
                }
            }
        });

        //Comportamiento del JRadioButton Elevación
        jRadioButton16.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                global.setCurrentPlotType(Global.ELEVATIONPLOT);
                if (state == ItemEvent.SELECTED) {
                    jPanel17.setVisible(true);
                    jLabel6.setEnabled(true);
                    jLabel6.setText(Global.getMessages().
                            getString("SimulationPanel.param.radiationpattern.jlabel6") + " de Azimuth");
                    jLabel7.setEnabled(true);
                    jLabel7.setText(Global.getMessages().
                            getString("SimulationPanel.param.radiationpattern.jlabel7"));
                    jFormattedTextField6.setEnabled(true);
                    jFormattedTextField7.setEnabled(true);
                    repaint();
                }
            }
        });

        //Comportamiento del JRadioButton 3D
        jRadioButton17.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                global.setCurrentPlotType(Global.PLOT3D);
                if (state == ItemEvent.SELECTED) {
                    jPanel17.setVisible(false);
                    jLabel6.setText("");
                    jLabel6.setEnabled(false);
                    jLabel7.setEnabled(true);
                    jFormattedTextField6.setEnabled(false);
                    jFormattedTextField7.setEnabled(true);
                    revalidate();
                }
            }
        });

        if (jRadioButton16.isSelected()) {
            jFormattedTextField6.setEnabled(true);
            jLabel6.setEnabled(true);
            jFormattedTextField7.setEnabled(true);
            jLabel7.setEnabled(true);
            jLabel6.
                    setText(jLabel6.getText() + " de Azimuth");
        } else if (jRadioButton15.isSelected()) {
            jFormattedTextField6.setEnabled(true);
            jLabel6.setEnabled(true);
            jFormattedTextField7.setEnabled(true);
            jLabel7.setEnabled(true);
            jLabel6.
                    setText(jLabel6.getText() + " de Elevación");
        }

        switch (global.getCurrentPlotType()) {
            case Global.AZIMUTHPLOT:
                jRadioButton15.setSelected(true);
                break;
            case Global.ELEVATIONPLOT:
                jRadioButton16.setSelected(true);
                break;
            case Global.PLOT3D:
                jRadioButton17.setSelected(true);
                break;

            default:
                throw new AssertionError();
        }
    }

    /**
     * Verifica que los parámetros introducidos en los componentes del panel
     * sean válidos
     *
     * @return true si los parámetros introducidos son válidos, de lo contrario,
     * devuelve false
     */
    public boolean validateRadiationPattern() {

        if (jRadioButton15.isSelected()) {
            if (!jFormattedTextField6.getText().isEmpty() && !jFormattedTextField7.getText().isEmpty()) {
                boolean validAngle = Integer.valueOf(jFormattedTextField6.getText()) >= -90
                        && Integer.valueOf(jFormattedTextField6.getText()) <= 90;
                boolean validStep = Double.valueOf(jFormattedTextField7.getText().replace(",", ".")) >= 0.1;
                return (validAngle && validStep);
            } else {
                return false;
            }
        } else if (jRadioButton16.isSelected()) {
            if (!jFormattedTextField6.getText().isEmpty() && !jFormattedTextField7.
                    getText().isEmpty()) {
                boolean validAngle = Integer.
                        valueOf(jFormattedTextField6.
                                getText()) >= 0 && Integer.
                                valueOf(jFormattedTextField6.
                                        getText()) <= 360;
                boolean validStep = Double.
                        valueOf(jFormattedTextField7.
                                getText().
                                replace(",", ".")) >= 0.1;
                return (validAngle && validStep);
            } else {
                return false;
            }
        } else {
            if (!jFormattedTextField7.
                    getText().isEmpty()) {
                boolean validStep = Double.
                        valueOf(jFormattedTextField7.
                                getText().
                                replace(",", ".")) >= 0.1;
                return validStep;
            } else {
                return false;
            }
        }
    }

    /**
     * Genera el comando patrón de radiación, interpretable por NEC2 a partir de
     * los parámetros introducidos en el panel
     *
     * @return Comando patrón de radiación de NEC2
     */
    public RadiationPattern generateRPCard() {

        RadiationPattern radiationPattern = new RadiationPattern();
        if (jRadioButton15.isSelected()) { // AZIMUTH
            radiationPattern.setTheta(1);
            double numberOfPhi = 360 / Double.valueOf(jFormattedTextField7.getText().replace(",", ".")) + 1;
            radiationPattern.setPhi(Math.round((float) numberOfPhi));
            radiationPattern.setXnda(1000);
            radiationPattern.setInTheta(Double.valueOf(jFormattedTextField6.getText().replace(",", ".")));

            radiationPattern.setInPhi(0);
            radiationPattern.setIncPhi(Double.valueOf(jFormattedTextField7.getText().replace(",", ".")));
            radiationPattern.setIncTheta(0);

        } else if (jRadioButton16.isSelected()) { // ELEVACIÓN
            double numberOfTheta = 360 / Double.valueOf(jFormattedTextField7.getText().replace(",", ".")) + 1;
            radiationPattern.setTheta(Math.round((float) numberOfTheta));
            radiationPattern.setPhi(1);
            radiationPattern.setXnda(1000);
            radiationPattern.setInTheta(0);
            radiationPattern.setInPhi(Double.valueOf((jFormattedTextField6.getText()).replace(",", ".")));

            radiationPattern.setIncTheta(Double.valueOf(jFormattedTextField7.getText().replace(",", ".")));
            radiationPattern.setIncPhi(0);
        } else { // 3D
            radiationPattern.setTheta(91);
            radiationPattern.setPhi(181);
            radiationPattern.setXnda(1001);
            radiationPattern.setInTheta(0);
            radiationPattern.setInPhi(0);
            if (Double.valueOf(jFormattedTextField7.getText()) >= 2) {
                radiationPattern.setIncTheta(Double.valueOf((jFormattedTextField7.getText()).replace(",", ".")));
                radiationPattern.setIncPhi(Double.valueOf((jFormattedTextField7.getText()).replace(",", ".")));
            } else {
                global.errorMessage("Messages.wrongStepTitle", "Messages.wrongStep");
            }
        }

        if (global.getCurrentPlotType() != Global.PLOT3D) {
            radiationPattern.setInputInAngle(Double.valueOf(jFormattedTextField6.getText().replace(",", ".")));
        }
        radiationPattern.setSetted(true);
        return radiationPattern;
    }

    /**
     * Convierte la información obtenida a partir de una descripción de tipo de
     * patrón de radiación en NEC2 a un objeto tipo RadiationPattern para
     * desplegar los valores correspondientes en los controles del panel
     */
    public void loadRPCard() {
        NumberFormat format = DecimalFormat.getInstance();
        format.setMinimumFractionDigits(0);

        RadiationPattern rp = global.getgRadiationPattern();
        if (rp.isSetted()) {
            if (rp.getTheta() == 1) {
                jRadioButton15.setSelected(true);
                jFormattedTextField7.setText(format.format(rp.getIncPhi()) + "");
            }

            if (rp.getPhi() == 1) {
                jRadioButton16.setSelected(true);
                jFormattedTextField7.setText(format.format((rp.getIncTheta())) + "");
            }

            if (rp.getTheta() != 1 && rp.getPhi() != 1) {
                jRadioButton17.setSelected(true);
                jPanel17.setVisible(false);
                jLabel6.setText("");
                jLabel6.setEnabled(false);
                jLabel7.setEnabled(true);
                jFormattedTextField6.setEnabled(false);
                jFormattedTextField7.setEnabled(true);

                if (rp.getInTheta() > 0) {
                    jFormattedTextField7.setText(format.format(rp.getIncTheta()) + "");
                }
            }

            if (rp.getTheta() == 0 && rp.getPhi() == 0) {
                jRadioButton17.setSelected(true);
                jRadioButton15.setSelected(true);
                jFormattedTextField7.setText(format.format(rp.getIncPhi()) + "");
            }

            if (rp.getInputInAngle() != 0.0) {
                jFormattedTextField6.setText(format.format(rp.getInputInAngle()) + "");
            }
        }
        jFormattedTextField7.setText("2");
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
        jPanel8 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jRadioButton15 = new javax.swing.JRadioButton();
        jRadioButton16 = new javax.swing.JRadioButton();
        jRadioButton17 = new javax.swing.JRadioButton();
        jPanel17 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        okbtn = new javax.swing.JButton();
        cancelbtn = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.GridLayout(3, 1));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("jLabel3");
        jLabel3.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel15.add(jLabel3, java.awt.BorderLayout.LINE_START);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new java.awt.GridLayout(3, 1));

        jRadioButton15.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton15);
        jRadioButton15.setSelected(true);
        jRadioButton15.setText("jRadioButton15");
        jPanel16.add(jRadioButton15);

        jRadioButton16.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton16);
        jRadioButton16.setText("jRadioButton16");
        jPanel16.add(jRadioButton16);

        jRadioButton17.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton17);
        jRadioButton17.setText("jRadioButton17");
        jPanel16.add(jRadioButton17);

        jPanel15.add(jPanel16, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel15);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new java.awt.GridLayout(1, 1));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setPreferredSize(new java.awt.Dimension(175, 18));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("jLabel6");
        jLabel6.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel19.add(jLabel6);

        jFormattedTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jFormattedTextField6.setColumns(10);
        jFormattedTextField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel19.add(jFormattedTextField6);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("jLabel10");
        jPanel19.add(jLabel10);

        jPanel17.add(jPanel19);

        jPanel13.add(jPanel17);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new java.awt.GridLayout(1, 1));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("jLabel7");
        jLabel7.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel20.add(jLabel7);

        jFormattedTextField7.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jFormattedTextField7.setColumns(10);
        jFormattedTextField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jPanel20.add(jFormattedTextField7);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("jLabel10");
        jPanel20.add(jLabel11);

        jPanel18.add(jPanel20);

        jPanel13.add(jPanel18);

        jPanel8.add(jPanel13, java.awt.BorderLayout.CENTER);

        add(jPanel8, java.awt.BorderLayout.CENTER);

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
    }// </editor-fold>//GEN-END:initComponents
//Comportamiento del botón Aceptar
    private void okbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okbtnActionPerformed
        if (validateRadiationPattern()) {
            global.setgRadiationPattern(generateRPCard());
            frame.dispose();
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_okbtnActionPerformed
//Comportamiento del botón Cancelar
    private void cancelbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbtnActionPerformed
        frame.dispose();
    }//GEN-LAST:event_cancelbtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelbtn;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRadioButton15;
    private javax.swing.JRadioButton jRadioButton16;
    private javax.swing.JRadioButton jRadioButton17;
    private javax.swing.JButton okbtn;
    // End of variables declaration//GEN-END:variables
}
