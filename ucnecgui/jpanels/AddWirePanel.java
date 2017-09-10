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
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import ucnecgui.Global;
import ucnecgui.models.Line;
import ucnecgui.models.Point;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class AddWirePanel extends javax.swing.JPanel implements ChangeListener {

    private Global global;
    private JFrame plot;
    private DefaultTableModel model;
    private int wireId;
    JComboBox selector;

    /**
     * Constructor de la clase AddWirePanel
     *
     * @param global Objeto de la clase Global
     * @param model Modelo de la jTable de geometría
     * @param selector Objeto JComboBox del selector de alambres
     */
    public AddWirePanel(Global global, DefaultTableModel model, JComboBox selector) {
        this.global = global;
        this.plot = plot;
        this.model = model;
        this.selector = selector;
        initComponents();
        initializePanel();
    }

    /**
     * Constructor de la clase AddWirePanel
     *
     * @param global Objeto de la clase Global
     * @param model Modelo de la jTable de geometría
     * @param wireId Alambre seleccionado
     */
    public AddWirePanel(Global global, DefaultTableModel model, int wireId) {
        this.global = global;
        this.plot = plot;
        this.model = model;
        this.wireId = wireId;
        initComponents();
        initializeEditPanel();
    }

    /**
     * Inicialización del Panel
     */
    public void initializePanel() {

        //Texto de las etiquetas
        title_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jlabel2.label"));
        wireX1_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName1"));
        wireY1_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName2"));
        wireZ1_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName3"));
        wireX2_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName4"));
        wireY2_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName5"));
        wireZ2_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName6"));
        wireDiameter_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName7"));
        wireSeg_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName8"));
        addWire_btn.setText(Global.getMessages().getString("SimulationPanel.source.jlabel3.label"));
        jLabel1.setText(global.unit2String());
        jLabel2.setText(global.unit2String());
        jLabel3.setText(global.unit2String());
        jLabel4.setText(global.unit2String());
        jLabel5.setText(global.unit2String());
        jLabel6.setText(global.unit2String());

        //Configuración de las unidades
        int currentUnit = global.getCurrentUnit();

        switch (currentUnit) {
            case 0:
                jLabel7.setText(Global.getMessages().getString("Global.units.milimeter"));
                break;
            case 1:
                jLabel7.setText(Global.getMessages().getString("Global.units.milimeter"));
                break;
            case 2:
                jLabel7.setText(Global.getMessages().getString("Global.units.inch"));
                break;
            case 3:
                jLabel7.setText(Global.getMessages().getString("Global.units.inch"));
                break;
            case 4:
                jLabel7.setText(Global.getMessages().getString("Global.units.wavelength"));
                break;
        }

        jLabel8.setText(Global.getMessages().getString("SimulationPanel.geometry.units.label"));
        TitledBorder titleA = BorderFactory.createTitledBorder(Global.getMessages().getString("SimulationPanel.geometry.jlabel5.label"));
        TitledBorder titleB = BorderFactory.createTitledBorder(Global.getMessages().getString("SimulationPanel.geometry.jlabel6.label"));
        TitledBorder titleC = BorderFactory.createTitledBorder(Global.getMessages().getString("SimulationPanel.geometry.jlabel7.label"));
        jPanel3.setBorder(titleA);
        jPanel4.setBorder(titleB);
        jPanel6.setBorder(titleC);

        //Funcionalidad del botón AÑADIR
        addWire_btn.addActionListener((ActionEvent e) -> {
            if (validateInput()) {
                global.getgWires().add(addWire());
                global.sortWires();
                Global.updateTable(model, global);
                if (selector != null) {
                    selector.removeAllItems();
                    for (int i = 0; i < global.getgWires().size(); i++) {
                        if (i == global.getCurrentSourceTag() - 1) {
                            continue;
                        }
                        selector.addItem(global.getgWires().get(i).getNumber() + "");
                    }
                    selector.revalidate();
                }

                global.updatePlot(global);
                cleanPanel();
            } else {
                global.errorValidateInput();
            }
        });
        autoSeg.addChangeListener(this);
    }

    /**
     *Inicializar componentes del panel
     */
    public void initializeEditPanel() {

        //Texto de las etiquetas
        title_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jlabel2.label"));
        wireX1_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName1"));
        wireY1_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName2"));
        wireZ1_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName3"));
        wireX2_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName4"));
        wireY2_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName5"));
        wireZ2_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName6"));
        wireDiameter_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName7"));
        wireSeg_lbl.setText(Global.getMessages().getString("SimulationPanel.geometry.jtable1.columnName8"));
        addWire_btn.setText(Global.getMessages().getString("SimulationPanel.geometry.jbutton7.label"));
        jLabel1.setText(global.unit2String());
        jLabel2.setText(global.unit2String());
        jLabel3.setText(global.unit2String());
        jLabel4.setText(global.unit2String());
        jLabel5.setText(global.unit2String());
        jLabel6.setText(global.unit2String());

        //Configuración de las unidades
        int currentUnit = global.getCurrentUnit();

        switch (currentUnit) {
            case 0:
                jLabel7.setText(Global.getMessages().getString("Global.units.milimeter"));
                break;
            case 1:
                jLabel7.setText(Global.getMessages().getString("Global.units.milimeter"));
                break;
            case 2:
                jLabel7.setText(Global.getMessages().getString("Global.units.inch"));
                break;
            case 3:
                jLabel7.setText(Global.getMessages().getString("Global.units.inch"));
                break;
            case 4:
                jLabel7.setText(Global.getMessages().getString("Global.units.wavelength"));
                break;
        }

        jLabel8.setText(Global.getMessages().getString("SimulationPanel.geometry.units.label"));
        TitledBorder titleA = BorderFactory.createTitledBorder(Global.getMessages().getString("SimulationPanel.geometry.jlabel5.label"));
        TitledBorder titleB = BorderFactory.createTitledBorder(Global.getMessages().getString("SimulationPanel.geometry.jlabel6.label"));
        TitledBorder titleC = BorderFactory.createTitledBorder(Global.getMessages().getString("SimulationPanel.geometry.jlabel7.label"));
        jPanel3.setBorder(titleA);
        jPanel4.setBorder(titleB);
        jPanel6.setBorder(titleC);

        //Funcionalidad del botón EDITAR
        addWire_btn.addActionListener((ActionEvent e) -> {
            if (validateInput()) {
                global.getgWires().set(wireId - 1, editWire(global.getgWires().get(wireId - 1)));
                Global.updateTable(model, global);
                global.updatePlot(global);
                SwingUtilities.getWindowAncestor(this).dispose();
            }
        });
        autoSeg.addChangeListener(this);
        fillInfo();
    }

    /**
     * Rellena a los jTextFormattedFIeld con los datos del alambre seleccionado
     */
    public void fillInfo() {
        Wire editedWire = global.getgWires().get(wireId - 1);

        wireX1.setText(editedWire.getX1() + "");
        wireY1.setText(editedWire.getY1() + "");
        wireZ1.setText(editedWire.getZ1() + "");
        wireX2.setText(editedWire.getX2() + "");
        wireY2.setText(editedWire.getY2() + "");
        wireZ2.setText(editedWire.getZ2() + "");
        double factor = global.unit2UpperFactor();

        wireDiameter.setText((editedWire.getRadius() * factor) + "");

        wireSeg.setText(editedWire.getSegs() + "");
    }

    /**
     * Limpia la información de los jTextFormattedFields del panel
     */
    public void cleanPanel() {
        wireX1.setText("");
        wireY1.setText("");
        wireZ1.setText("");
        wireX2.setText("");
        wireY2.setText("");
        wireZ2.setText("");
        wireSeg.setText("");
        autoSeg.setSelected(false);
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    /**
     * Verifica si todos los campos jTextFormattedField contienen información
     *
     * @return true si todos los campos jTextFormattedField contienen
     * información, de lo contrario, devuelve false
     */
    public boolean existsWire() {
        return !wireX1.getText().isEmpty() && !wireY1.getText().isEmpty() && !wireZ1.getText().isEmpty()
                && !wireX2.getText().isEmpty() && !wireY2.getText().isEmpty() && !wireZ2.getText().isEmpty()
                && !wireDiameter.getText().isEmpty();
    }

    /**
     * Verifica si todos los campos jTextFormattedField contienen información,
     * además, chequea la validez de los datos introducidos
     *
     * @return true si todos los campos jTextFormattedField contienen
     * información válida, de lo contrario, devuelve false
     */
    public boolean validateInput() {
        boolean condition1, condition2;
        condition1 = (!wireX1.getText().isEmpty())
                && (!wireY1.getText().isEmpty())
                && (!wireZ1.getText().isEmpty())
                && (!wireX2.getText().isEmpty())
                && (!wireY2.getText().isEmpty())
                && (!wireZ2.getText().isEmpty())
                && (!wireDiameter.getText().isEmpty())
                && (!wireSeg.getText().isEmpty());
        if (condition1) {
            condition2 = Double.valueOf(wireDiameter.getText().replace(",", ".")) > 0;
        } else {
            return false;
        }
        return condition1 && condition2;
    }

    /**
     * Modifica los parámetros de oldWire para hacerlos coincidir con los
     * introducidos en los jTextFormattedFields
     *
     * @param oldWire Objeto Wire original
     * @return Objeto Wire con parámetros editados
     */
    public Wire editWire(Wire oldWire) {
        Wire wire = new Wire();
        double X1 = Double.valueOf(wireX1.getText().replace(",", "."));
        double Y1 = Double.valueOf(wireY1.getText().replace(",", "."));
        double Z1 = Double.valueOf(wireZ1.getText().replace(",", "."));
        double X2 = Double.valueOf(wireX2.getText().replace(",", "."));
        double Y2 = Double.valueOf(wireY2.getText().replace(",", "."));
        double Z2 = Double.valueOf(wireZ2.getText().replace(",", "."));

        wire.setNumber(oldWire.getNumber());

        double lowerFactor = global.unit2LowerFactor();
        wire.setRadius(Double.valueOf(wireDiameter.getText().replace(",", ".")) * lowerFactor);
        int seg = Integer.valueOf(wireSeg.getText());
        wire.setSegs(seg);
        wire.setX1(X1);
        wire.setY1(Y1);
        wire.setZ1(Z1);
        wire.setX2(X2);
        wire.setY2(Y2);
        wire.setZ2(Z2);
        return wire;
    }

    /**
     * Crea un objeto Wire con los parámetros introducidos en los
     * JTextFormattedFields
     *
     * @return Nuevo objeto Wire
     */
    public Wire addWire() {
        Wire wire = new Wire();
        double X1 = Double.valueOf(wireX1.getText().replace(",", "."));
        double Y1 = Double.valueOf(wireY1.getText().replace(",", "."));
        double Z1 = Double.valueOf(wireZ1.getText().replace(",", "."));
        double X2 = Double.valueOf(wireX2.getText().replace(",", "."));
        double Y2 = Double.valueOf(wireY2.getText().replace(",", "."));
        double Z2 = Double.valueOf(wireZ2.getText().replace(",", "."));

        wire.setNumber(global.getgWires().size() + 1);
        wire.setRadius(Double.valueOf(wireDiameter.getText().replace(",", ".")));
        int seg = Integer.valueOf(wireSeg.getText());
        wire.setSegs(seg);
        wire.setX1(X1);
        wire.setY1(Y1);
        wire.setZ1(Z1);
        wire.setX2(X2);
        wire.setY2(Y2);
        wire.setZ2(Z2);
        return wire;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title_lbl = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        addWire_btn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        wireX1_lbl = new javax.swing.JLabel();
        wireX1 = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        wireY1_lbl = new javax.swing.JLabel();
        wireY1 = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        wireZ1_lbl = new javax.swing.JLabel();
        wireZ1 = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        wireX2_lbl = new javax.swing.JLabel();
        wireX2 = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        wireY2_lbl = new javax.swing.JLabel();
        wireY2 = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        wireZ2_lbl = new javax.swing.JLabel();
        wireZ2 = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        wireDiameter_lbl = new javax.swing.JLabel();
        wireDiameter = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        wireSeg_lbl = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        wireSeg = new javax.swing.JFormattedTextField();
        autoSeg = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 5));
        setPreferredSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.BorderLayout());

        title_lbl.setBackground(new java.awt.Color(36, 113, 163));
        title_lbl.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        title_lbl.setForeground(new java.awt.Color(255, 255, 255));
        title_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title_lbl.setText("jLabel1");
        title_lbl.setOpaque(true);
        add(title_lbl, java.awt.BorderLayout.NORTH);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridLayout(1, 1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        addWire_btn.setText("jButton2");
        jPanel2.add(addWire_btn, java.awt.BorderLayout.PAGE_END);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(1, 1));

        jPanel8.setLayout(new java.awt.GridLayout(3, 1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(3, 3));

        wireX1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireX1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireX1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireX1_lbl.setText("jLabel1");
        jPanel3.add(wireX1_lbl);

        wireX1.setForeground(new java.awt.Color(0, 0, 0));
        wireX1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00000"))));
        wireX1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(wireX1);

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("jLabel1");
        jPanel3.add(jLabel1);

        wireY1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireY1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireY1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireY1_lbl.setText("jLabel2");
        jPanel3.add(wireY1_lbl);

        wireY1.setForeground(new java.awt.Color(0, 0, 0));
        wireY1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00000"))));
        wireY1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(wireY1);

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("jLabel2");
        jPanel3.add(jLabel2);

        wireZ1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireZ1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireZ1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireZ1_lbl.setText("jLabel3");
        jPanel3.add(wireZ1_lbl);

        wireZ1.setForeground(new java.awt.Color(0, 0, 0));
        wireZ1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00000"))));
        wireZ1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(wireZ1);

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("jLabel3");
        jPanel3.add(jLabel3);

        jPanel8.add(jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.GridLayout(3, 3));

        wireX2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireX2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireX2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireX2_lbl.setText("jLabel4");
        jPanel4.add(wireX2_lbl);

        wireX2.setForeground(new java.awt.Color(0, 0, 0));
        wireX2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00000"))));
        wireX2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(wireX2);

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("jLabel4");
        jPanel4.add(jLabel4);

        wireY2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireY2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireY2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireY2_lbl.setText("jLabel5");
        jPanel4.add(wireY2_lbl);

        wireY2.setForeground(new java.awt.Color(0, 0, 0));
        wireY2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00000"))));
        wireY2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(wireY2);

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("jLabel5");
        jPanel4.add(jLabel5);

        wireZ2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireZ2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireZ2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireZ2_lbl.setText("jLabel5");
        jPanel4.add(wireZ2_lbl);

        wireZ2.setForeground(new java.awt.Color(0, 0, 0));
        wireZ2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00000"))));
        wireZ2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(wireZ2);

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("jLabel6");
        jPanel4.add(jLabel6);

        jPanel8.add(jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.GridLayout(2, 3));

        wireDiameter_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireDiameter_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireDiameter_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireDiameter_lbl.setText("jLabel5");
        jPanel6.add(wireDiameter_lbl);

        wireDiameter.setForeground(new java.awt.Color(0, 0, 0));
        wireDiameter.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00000"))));
        wireDiameter.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel6.add(wireDiameter);

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("jLabel7");
        jPanel6.add(jLabel7);

        wireSeg_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireSeg_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireSeg_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireSeg_lbl.setText("jLabel5");
        jPanel6.add(wireSeg_lbl);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.GridLayout(1, 3));

        wireSeg.setForeground(new java.awt.Color(0, 0, 0));
        wireSeg.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wireSeg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel7.add(wireSeg);

        autoSeg.setBackground(new java.awt.Color(255, 255, 255));
        autoSeg.setText("Auto");
        jPanel7.add(autoSeg);

        jPanel6.add(jPanel7);

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("jLabel8");
        jPanel6.add(jLabel8);

        jPanel8.add(jPanel6);

        jPanel5.add(jPanel8);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addWire_btn;
    private javax.swing.JCheckBox autoSeg;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel title_lbl;
    private javax.swing.JFormattedTextField wireDiameter;
    private javax.swing.JLabel wireDiameter_lbl;
    private javax.swing.JFormattedTextField wireSeg;
    private javax.swing.JLabel wireSeg_lbl;
    private javax.swing.JFormattedTextField wireX1;
    private javax.swing.JLabel wireX1_lbl;
    private javax.swing.JFormattedTextField wireX2;
    private javax.swing.JLabel wireX2_lbl;
    private javax.swing.JFormattedTextField wireY1;
    private javax.swing.JLabel wireY1_lbl;
    private javax.swing.JFormattedTextField wireY2;
    private javax.swing.JLabel wireY2_lbl;
    private javax.swing.JFormattedTextField wireZ1;
    private javax.swing.JLabel wireZ1_lbl;
    private javax.swing.JFormattedTextField wireZ2;
    private javax.swing.JLabel wireZ2_lbl;
    // End of variables declaration//GEN-END:variables

    @Override
    public void stateChanged(ChangeEvent e) {
        if (autoSeg.isSelected()) {
            int seg = (int) Math.round(autoSegGenerator());
            if (seg > 0) {
                wireSeg.setText(String.valueOf(seg));
            } else {
                wireSeg.setText("2");
            }
        }
    }

    /**
     * Invoca el cálculo del número de segmentos de un objeto Wire, solicitando
     * los parámetros requeridos en caso de que estos no hayan sido establecidos
     * aún; por ejemplo, la frecuencia.
     *
     * @return Número de segmentos del objeto Wire
     */
    public double autoSegGenerator() {
        double resp = 0;
        if (existsWire()) {
            if (global.getgFrequency().getFreq() == 0.0) {
                String strFreq = global.inputMessage(Global.getMessages().getString("SimulationPanel.geometry.inputFreqMessage"));
                if (Global.isNumeric(strFreq.replace(",", "."))) {
                    Double freq = Double.valueOf(strFreq.replace(",", "."));
                    global.getgFrequency().setFreq(freq);
                    resp = generateSegs();
                } else {
                    global.errorMessage("Messages.wrongFormatTitle", "Messages.wrongFormat");
                }
            } else {
                resp = generateSegs();
            }
        } else {
            global.errorMessage("Messages.nowires_title", "Messages.nowires");
        }
        return resp;
    }

    /**
     * Calcula el número de segmentos del objeto Wire especificado en el panel
     * el valor de la segmentación obedece a la fórmula 20 * d / wl donde d es
     * la longitud del alambre y wl la longitud de onda a la frecuencia de
     * diseño de la simulación
     *
     * @return Número de segmentos del objeto Wire
     */
    public double generateSegs() {
        double X1 = Double.valueOf(wireX1.getText().replace(",", "."));
        double Y1 = Double.valueOf(wireY1.getText().replace(",", "."));
        double Z1 = Double.valueOf(wireZ1.getText().replace(",", "."));
        double X2 = Double.valueOf(wireX2.getText().replace(",", "."));
        double Y2 = Double.valueOf(wireY2.getText().replace(",", "."));
        double Z2 = Double.valueOf(wireZ2.getText().replace(",", "."));
        Point A = new Point(X1, Y1, Z1);
        Point B = new Point(X2, Y2, Z2);
        Line line = new Line(A, B);
        return (20 * line.distance()) / global.getWavelength();
    }
}