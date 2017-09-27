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
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import ucnecgui.Global;
import ucnecgui.models.Line;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class TransformationPanel extends javax.swing.JPanel {

    public JTabbedPane tabbedPane;
    public int[] selectedWires;
    public int[] alterSelectedWires;
    private Global global;
    private JFrame frame;
    JComboBox selected;

    /**
     * Constructor de la clase TransformationPanel
     *
     * @param global Objeto de la clase Global
     * @param selectedWires Vector con los índices de los alambres seleccionados
     * @param frame Ventana que invoca este panel
     * @param selected Objeto JComboBox correspondiente al selector de alambres
     */
    public TransformationPanel(Global global,
            int[] selectedWires, JFrame frame, JComboBox selected) {
        initComponents();
        this.global = global;
        this.frame = frame;
        this.selectedWires = selectedWires;
        this.selected = selected;
        initializePanel();
    }

    /**
     * Inicializa los componentes de los diferentes paneles
     */
    public void initializePanel() {

        jLabel1.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jlabel1"));
        jLabel2.setText(Global.getMessages().getString("SimulationPanel.jTabbedPanel.label9"));
        jLabel3.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jlabel1"));
        jLabel4.setText(Global.getMessages().getString("TransformationPanel.rotation.degrees.label"));
        jLabel12.setText(Global.getMessages().getString("TransformationPanel.rotation.degrees.label"));
        jLabel13.setText(Global.getMessages().getString("TransformationPanel.rotation.degrees.label"));
        jLabel14.setText(global.unit2String());
        jLabel15.setText(global.unit2String());
        jLabel16.setText(global.unit2String());
        jLabel5.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jlabel5"));
        jLabel6.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jlabel6"));
        jLabel7.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jlabel7"));
        jLabel9.setText(Global.getMessages().
                getString("TransformationPanel.rotation.jlabel9"));
        jLabel10.setText(Global.getMessages().
                getString("TransformationPanel.rotation.jlabel10"));
        jLabel11.setText(Global.getMessages().
                getString("TransformationPanel.rotation.jlabel11"));
        jCheckBox1.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jcheckbox1"));
        jCheckBox2.setText(Global.getMessages().
                getString("TransformationPanel.rotation.jcheckbox2"));
        jCheckBox3.setText(Global.getMessages().
                getString("TransformationPanel.rotation.jcheckbox3"));
        jButton1.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jbutton1"));
        jButton2.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jbutton1"));
        jLabel8.setText(Global.getMessages().
                getString("TransformationPanel.traslation.jlabel8") + " " + selectedWires.length);
        updateWireSelector();

        // Comportamiento del JCheckBox  Copiar del panel Traslación
        jCheckBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jCheckBox1.isSelected()) {
                    jSpinner2.setEnabled(true);
                } else {
                    jSpinner2.setEnabled(false);
                }
            }
        });
        // Comportamiento del JCheckBox  Copiar del panel Rotación
        jCheckBox3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jCheckBox3.isSelected()) {
                    jSpinner3.setEnabled(true);
                } else {
                    jSpinner3.setEnabled(false);
                }
            }
        });
        // Comportamiento del botón Aceptar del panel Traslación
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double xOffset = 0;
                double yOffset = 0;
                double zOffset = 0;
                if (selectedWires.length > 0) {
                    if (jCheckBox5.isSelected() && !jFormattedTextField4.getText().isEmpty()) {
                        xOffset = Double.valueOf(jFormattedTextField4.getText().replace(",", "."));
                        xOffset = xOffset;
                    }
                    if (jCheckBox7.isSelected() && !jFormattedTextField6.getText().isEmpty()) {
                        yOffset = Double.valueOf(jFormattedTextField6.getText().replace(",", "."));
                        yOffset = yOffset;
                    }
                    if (jCheckBox6.isSelected() && !jFormattedTextField5.getText().isEmpty()) {
                        zOffset = Double.valueOf(jFormattedTextField5.getText().replace(",", "."));
                        zOffset = zOffset;
                    }
                    if (!jCheckBox1.isSelected()) {
                        for (int i = 0; i < selectedWires.length; i++) {
                            Wire nWire = global.getgWires().get(selectedWires[i] - 1);
                            nWire.setX1(Global.traslateValue(nWire.getX1(), xOffset));
                            nWire.setY1(Global.traslateValue(nWire.getY1(), yOffset));
                            nWire.setZ1(Global.traslateValue(nWire.getZ1(), zOffset));
                            nWire.setX2(Global.traslateValue(nWire.getX2(), xOffset));
                            nWire.setY2(Global.traslateValue(nWire.getY2(), yOffset));
                            nWire.setZ2(Global.traslateValue(nWire.getZ2(), zOffset));
                        }
                    } else {
                        for (int j = 1; j <= (Integer) jSpinner2.getValue(); j++) {
                            for (int i = 0; i < selectedWires.length; i++) {
                                Wire nWire = global.getgWires().get(selectedWires[i] - 1);
                                String number = String.valueOf(global.getLastWireNumber() + 1);
                                String x1 = String.valueOf(Global.traslateValue(nWire.getX1(), j * xOffset));
                                String y1 = String.valueOf(Global.traslateValue(nWire.getY1(), j * yOffset));
                                String z1 = String.valueOf(Global.traslateValue(nWire.getZ1(), j * zOffset));
                                String x2 = String.valueOf(Global.traslateValue(nWire.getX2(), j * xOffset));
                                String y2 = String.valueOf(Global.traslateValue(nWire.getY2(), j * yOffset));
                                String z2 = String.valueOf(Global.traslateValue(nWire.getZ2(), j * zOffset));
                                String diameter = String.valueOf(nWire.getRadius());
                                String seg = String.valueOf(nWire.getSegs());
                                String[] row = {number, x1, y1, z1, x2, y2, z2, diameter, seg};
                                global.getgWires().add(new Wire(row));
                            }

                        }
                    }
                    GeometryPanel.currentSelectedRow = -1;
                    global.sortWires();
                    Global.updateTable(GeometryPanel.model, global);
                    GeometryPanel.model.fireTableDataChanged();
                    updateWireSelector(selected);
                    global.updatePlot(global);
                    frame.dispose();
                }
            }
        });
        // Comportamiento del botón Aceptar del panel Rotación
        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double factor = global.convertUnits(global.getCurrentUnit(), Global.METER);
                ArrayList<Integer> sWires = new ArrayList<Integer>();
                double angle = 0;
                for (int selectedWire : selectedWires) {
                    sWires.add(selectedWire);
                }
                if (sWires.size() > 0) {
                    if (jRadioButton1.isSelected()) {
                        if (jCheckBox2.isSelected()) {
                            angle = Double.valueOf(jSpinner4.getValue() + "");
                        } else {
                            angle = 360 - Double.valueOf(jSpinner4.getValue() + "");
                        }
                        if (!jCheckBox3.isSelected()) {
                            for (int i = 0; i < sWires.size(); i++) {

                                Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                                Line line = new Line(nWire).rotateLine(Global.XAXIS, angle);
                                nWire.setX1(line.getX1());
                                nWire.setY1(line.getY1());
                                nWire.setZ1(line.getZ1());
                                nWire.setX2(line.getX2());
                                nWire.setY2(line.getY2());
                                nWire.setZ2(line.getZ2());
                            }
                        } else {
                            for (int j = 1; j <= (Integer) jSpinner3.getValue(); j++) {
                                for (int i = 0; i < sWires.size(); i++) {

                                    Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                                    Line line = new Line(nWire).rotateLine(Global.XAXIS, (j * angle));
                                    String x1 = String.valueOf(line.getX1());
                                    String x2 = String.valueOf(line.getX2());
                                    String y1 = String.valueOf(line.getY1());
                                    String y2 = String.valueOf(line.getY2());
                                    String z1 = String.valueOf(line.getZ1());
                                    String z2 = String.valueOf(line.getZ2());
                                    String diameter = String.valueOf(nWire.getRadius());
                                    String seg = String.valueOf(nWire.getSegs());
                                    String number = String.valueOf(global.getLastWireNumber() + 1);
                                    String[] row = {number, x1, y1, z1, x2, y2, z2, diameter, seg};
                                    global.getgWires().add(new Wire(row));
                                }
                            }
                        }
                    } else if (jRadioButton2.isSelected()) {

                        if (jCheckBox2.isSelected()) {
                            angle = Double.valueOf(jSpinner5.getValue() + "");
                        } else {
                            angle = 360 - Double.valueOf(jSpinner5.getValue() + "");
                        }
                        if (!jCheckBox3.isSelected()) {
                            for (int i = 0; i < sWires.size(); i++) {

                                Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                                Line line = new Line(nWire).rotateLine(Global.YAXIS, angle);
                                nWire.setX1(line.getX1());
                                nWire.setY1(line.getY1());
                                nWire.setZ1(line.getZ1());
                                nWire.setX2(line.getX2());
                                nWire.setY2(line.getY2());
                                nWire.setZ2(line.getZ2());
                            }
                        } else {
                            for (int j = 1; j <= (Integer) jSpinner3.getValue(); j++) {
                                for (int i = 0; i < sWires.size(); i++) {

                                    Wire nWire = global.getgWires().get(sWires.get(i) - 1);

                                    Line line = new Line(nWire).rotateLine(Global.YAXIS, (j * angle));
                                    String x1 = String.valueOf(line.getX1());
                                    String x2 = String.valueOf(line.getX2());
                                    String y1 = String.valueOf(line.getY1());
                                    String y2 = String.valueOf(line.getY2());
                                    String z1 = String.valueOf(line.getZ1());
                                    String z2 = String.valueOf(line.getZ2());
                                    String diameter = String.valueOf(nWire.getRadius());
                                    String seg = String.valueOf(nWire.getSegs());
                                    String number = String.valueOf(global.getLastWireNumber() + 1);
                                    String[] row = {number, x1, y1, z1, x2, y2, z2, diameter, seg};
                                    global.getgWires().add(new Wire(row));
                                }
                            }
                        }

                    } else {

                        if (!jCheckBox2.isSelected()) {
                            angle = Double.valueOf(jSpinner6.getValue() + "");
                        } else {
                            angle = 360 - Double.valueOf(jSpinner6.getValue() + "");
                        }
                        if (!jCheckBox3.isSelected()) {

                            for (int i = 0; i < sWires.size(); i++) {

                                Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                                Line line = new Line(nWire).rotateLine(Global.ZAXIS, angle);
                                nWire.setX1(line.getX1());
                                nWire.setY1(line.getY1());
                                nWire.setZ1(line.getZ1());
                                nWire.setX2(line.getX2());
                                nWire.setY2(line.getY2());
                                nWire.setZ2(line.getZ2());
                            }
                        } else {
                            for (int j = 1; j <= (Integer) jSpinner3.getValue(); j++) {
                                for (int i = 0; i < sWires.size(); i++) {

                                    Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                                    Line line = new Line(nWire).rotateLine(Global.ZAXIS, (j * angle));
                                    String x1 = String.valueOf(line.getX1());
                                    String x2 = String.valueOf(line.getX2());
                                    String y1 = String.valueOf(line.getY1());
                                    String y2 = String.valueOf(line.getY2());
                                    String z1 = String.valueOf(line.getZ1());
                                    String z2 = String.valueOf(line.getZ2());
                                    String diameter = String.valueOf(nWire.getRadius());
                                    String seg = String.valueOf(nWire.getSegs());
                                    String number = String.valueOf(global.getLastWireNumber() + 1);
                                    String[] row = {number, x1, y1, z1, x2, y2, z2, diameter, seg};
                                    global.getgWires().add(new Wire(row));
                                }
                            }
                        }
                    }
                    GeometryPanel.currentSelectedRow = -1;
                    GeometryPanel.model.fireTableDataChanged();
                    global.sortWires();
                    Global.updateTable(GeometryPanel.model, global);
                    Global.roundValues(GeometryPanel.table, GeometryPanel.model);
                    for (Wire gWire : global.getgWires()) {
                        System.out.println(gWire.toString());
                    }
                    global.updatePlot(global);
                    frame.dispose();
                }
            }
        });

        // Comportamiento del botón Aceptar del panel Escalamiento
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateScaling()) {
                    boolean keepD = keepDiameter.isSelected();
                    int from = Integer.valueOf(fromWire.getSelectedItem() + "");
                    int to = Integer.valueOf(toWire.getSelectedItem() + "");
                    double factor = Double.valueOf(scaleFactor.getText().replace(",", "."));
                    for (int i = from; i <= to; i++) {
                        if (i == global.getCurrentSourceTag()) {
                            continue;
                        }
                        Wire nWire = global.getgWires().get(i - 1);
                        global.scale(nWire, factor, keepD);
                    }
                } else {
                    global.errorValidateInput();
                }
                GeometryPanel.currentSelectedRow = -1;
                GeometryPanel.model.fireTableDataChanged();
                global.sortWires();
                Global.updateTable(GeometryPanel.model, global);
                global.updatePlot(global);
                frame.dispose();
            }
        });

    }

    /**
     * Actualiza el JComboBox del selector de alambres, agregando los nuevos
     * alambres creados e ignorando al alambre auxiliar de fuentes de corriente,
     * si lo hubiese
     *
     * @param wireSelector Objeto JComboBox correspondiente al selector de
     * alambres
     */
    public void updateWireSelector(JComboBox wireSelector) {
        wireSelector.removeAllItems();
        //Wire selector loader        

        for (int i = 0; i < global.getgWires().size(); i++) {
            if (i == global.getCurrentSourceTag() - 1) {
                continue;
            }
            wireSelector.addItem(global.getgWires().get(i).getNumber() + "");
        }
        if (wireSelector.getItemCount() >= 1) {
            wireSelector.setSelectedIndex(0);
            wireSelector.setEnabled(true);
        } else {
            wireSelector.setEnabled(false);
        }
        wireSelector.revalidate();
    }

    /**
     * Verifica si todos los campos jTextFormattedField, del panel de
     * escalamiento, contienen información, además, chequea la validez de los
     * datos introducidos en el panel
     *
     * @return true si todos los campos jTextFormattedField, del panel de
     * escalamiento, contienen información válida, de lo contrario, devuelve
     * false
     */
    public boolean validateScaling() {
        boolean condition1 = !scaleFactor.getText().isEmpty();
        boolean condition2 = Integer.valueOf(fromWire.getSelectedItem() + "") <= Integer.valueOf(toWire.getSelectedItem() + "");
        return condition1 && condition2;
    }

    /**
     * Actualiza los JComboBox del selector de alambres en el panel de
     * escalamiento, agregando los alambres disponibles e ignorando al alambre
     * auxiliar de fuentes de corriente, si lo hubiese
     *
     */
    public void updateWireSelector() {
        fromWire.removeAllItems();
        toWire.removeAllItems();
        for (int i = 0; i < global.getgWires().size(); i++) {
            if (i == global.getCurrentSourceTag() - 1) {
                continue;
            }
            fromWire.addItem(global.getgWires().get(i).getNumber() + "");
            toWire.addItem(global.getgWires().get(i).getNumber() + "");
        }
        fromWire.revalidate();
        toWire.revalidate();
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
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 0), new java.awt.Dimension(50, 32767));
        jLabel8 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jSpinner2 = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jSpinner4 = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jRadioButton2 = new javax.swing.JRadioButton();
        jSpinner5 = new javax.swing.JSpinner();
        jLabel12 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jRadioButton3 = new javax.swing.JRadioButton();
        jSpinner6 = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        jCheckBox3 = new javax.swing.JCheckBox();
        jSpinner3 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 0), new java.awt.Dimension(200, 0), new java.awt.Dimension(100, 32767));
        jButton2 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        scaleFactor = new javax.swing.JFormattedTextField();
        keepDiameter = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        fromWire = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        toWire = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(800, 700));
        setMinimumSize(new java.awt.Dimension(800, 700));
        setPreferredSize(new java.awt.Dimension(800, 700));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(800, 700));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 700));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(0, 0, 50));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));
        jPanel7.setRequestFocusEnabled(false);
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(36, 113, 163));
        jPanel1.setPreferredSize(new java.awt.Dimension(33, 25));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(36, 113, 163));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setPreferredSize(new java.awt.Dimension(0, 50));
        jPanel1.add(jLabel2, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel6.setBackground(new java.awt.Color(36, 113, 163));
        jPanel6.setPreferredSize(new java.awt.Dimension(600, 40));
        jPanel6.setLayout(new java.awt.GridLayout(1, 2));
        jPanel6.add(filler2);

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("jLabel8");
        jPanel6.add(jLabel8);

        jPanel3.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new java.awt.GridLayout(3, 1));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel11.setName(""); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel11.setRequestFocusEnabled(false);

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("jLabel5");
        jPanel11.add(jLabel5);

        jCheckBox5.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox5.setForeground(new java.awt.Color(0, 0, 0));
        jPanel11.add(jCheckBox5);

        jFormattedTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField4.setForeground(new java.awt.Color(0, 0, 0));
        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField4.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel11.add(jFormattedTextField4);

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("jLabel14");
        jPanel11.add(jLabel14);

        jPanel9.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("jLabel5");
        jPanel12.add(jLabel6);

        jCheckBox7.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox7.setForeground(new java.awt.Color(0, 0, 0));
        jPanel12.add(jCheckBox7);

        jFormattedTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField6.setForeground(new java.awt.Color(0, 0, 0));
        jFormattedTextField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField6.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel12.add(jFormattedTextField6);

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("jLabel14");
        jPanel12.add(jLabel15);

        jPanel9.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("jLabel5");
        jPanel13.add(jLabel7);

        jCheckBox6.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox6.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jCheckBox6);

        jFormattedTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField5.setForeground(new java.awt.Color(0, 0, 0));
        jFormattedTextField5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField5.setPreferredSize(new java.awt.Dimension(100, 20));
        jPanel13.add(jFormattedTextField5);

        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("jLabel14");
        jPanel13.add(jLabel16);

        jPanel9.add(jPanel13);

        jPanel5.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setLayout(new java.awt.GridLayout(2, 1));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setForeground(new java.awt.Color(0, 0, 0));
        jCheckBox1.setText("jCheckBox1");
        jPanel8.add(jCheckBox1);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinner2.setEnabled(false);
        jSpinner2.setMinimumSize(new java.awt.Dimension(50, 26));
        jSpinner2.setName(""); // NOI18N
        jSpinner2.setPreferredSize(new java.awt.Dimension(50, 26));
        jPanel8.add(jSpinner2);

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("jLabel1");
        jPanel8.add(jLabel1);

        jPanel10.add(jPanel8);

        jButton1.setText("jButton1");
        jPanel10.add(jButton1);

        jPanel5.add(jPanel10, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Traslación", jPanel5);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setLayout(new java.awt.GridLayout(3, 1));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel16.setName(""); // NOI18N
        jPanel16.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel16.setRequestFocusEnabled(false);

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("jLabel5");
        jPanel16.add(jLabel9);

        jRadioButton1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel16.add(jRadioButton1);

        jSpinner4.setModel(new javax.swing.SpinnerNumberModel(0, 0, 360, 1));
        jPanel16.add(jSpinner4);

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("jLabel4");
        jPanel16.add(jLabel4);

        jPanel15.add(jPanel16);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("jLabel5");
        jPanel17.add(jLabel10);

        jRadioButton2.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel17.add(jRadioButton2);

        jSpinner5.setModel(new javax.swing.SpinnerNumberModel(0, 0, 360, 1));
        jPanel17.add(jSpinner5);

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("jLabel4");
        jPanel17.add(jLabel12);

        jPanel15.add(jPanel17);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("jLabel5");
        jPanel18.add(jLabel11);

        jRadioButton3.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel18.add(jRadioButton3);

        jSpinner6.setModel(new javax.swing.SpinnerNumberModel(0, 0, 360, 1));
        jPanel18.add(jSpinner6);

        jLabel13.setBackground(new java.awt.Color(0, 0, 0));
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("jLabel4");
        jPanel18.add(jLabel13);

        jPanel15.add(jPanel18);

        jPanel14.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel19.setLayout(new java.awt.GridLayout(3, 1));

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setForeground(new java.awt.Color(0, 0, 0));
        jCheckBox2.setText("jCheckBox1");
        jPanel19.add(jCheckBox2);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setForeground(new java.awt.Color(0, 0, 0));
        jCheckBox3.setText("jCheckBox3");
        jPanel20.add(jCheckBox3);

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinner3.setEnabled(false);
        jPanel20.add(jSpinner3);

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("jLabel1");
        jPanel20.add(jLabel3);
        jPanel20.add(filler1);

        jPanel19.add(jPanel20);

        jButton2.setText("jButton1");
        jPanel19.add(jButton2);

        jPanel14.add(jPanel19, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Rotación", jPanel14);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel25.setPreferredSize(new java.awt.Dimension(10, 100));
        jPanel25.setLayout(new java.awt.GridLayout(3, 1));

        jPanel27.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel27.setLayout(new java.awt.BorderLayout());

        jLabel25.setBackground(new java.awt.Color(255, 255, 255));
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Factor de Escala");
        jLabel25.setOpaque(true);
        jLabel25.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel27.add(jLabel25, java.awt.BorderLayout.WEST);

        scaleFactor.setBackground(new java.awt.Color(255, 255, 255));
        scaleFactor.setForeground(new java.awt.Color(0, 0, 0));
        scaleFactor.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel27.add(scaleFactor, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel27);

        keepDiameter.setBackground(new java.awt.Color(255, 255, 255));
        keepDiameter.setForeground(new java.awt.Color(0, 0, 0));
        keepDiameter.setText("Mantener Diámetro");
        keepDiameter.setPreferredSize(new java.awt.Dimension(137, 20));
        jPanel25.add(keepDiameter);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(1, 4));

        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Desde Alambre");
        jPanel2.add(jLabel17);

        fromWire.setBackground(new java.awt.Color(255, 255, 255));
        fromWire.setForeground(new java.awt.Color(0, 0, 0));
        fromWire.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(fromWire);

        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Hasta Alambre");
        jPanel2.add(jLabel18);

        toWire.setBackground(new java.awt.Color(255, 255, 255));
        toWire.setForeground(new java.awt.Color(0, 0, 0));
        toWire.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel2.add(toWire);

        jPanel25.add(jPanel2);

        jPanel24.add(jPanel25, java.awt.BorderLayout.PAGE_START);

        jPanel21.add(jPanel24, java.awt.BorderLayout.CENTER);

        jButton3.setText("Aceptar");
        jPanel21.add(jButton3, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Escalamiento", jPanel21);

        jPanel3.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JComboBox<String> fromWire;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
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
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JSpinner jSpinner6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JCheckBox keepDiameter;
    private javax.swing.JFormattedTextField scaleFactor;
    private javax.swing.JComboBox<String> toWire;
    // End of variables declaration//GEN-END:variables
}
