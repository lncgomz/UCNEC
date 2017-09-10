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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import ucnecgui.Global;
import ucnecgui.models.Tl;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */

public class TlPanel extends javax.swing.JPanel {

    private Global global;

    /**
     *Constructor de la clase TlPanel
     * @param global Objeto de la clase Global
     */
    public TlPanel(Global global) {
        this.global = global;
        initComponents();
        initializePanel();
    }

    /**
     *Inicializa los componentes del panel
     */
    public void initializePanel() {

        //Labels initialization
        title_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jlabel2.label"));
        wireTag1_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName1"));
        wirePercentage1_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName2"));
        wireSegPercentage1_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName11"));
        wireSegPercentage2_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName11"));
        wireTag2_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName3"));
        wirePercentage2_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName4"));
        editTl_btn.setText(Global.getMessages().getString("SimulationPanel.tl.jlabel5.label"));
        z0_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName5"));
        length_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName10"));
        rYshunt1_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName6"));
        iYshunt1_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName7"));
        rYshunt2_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName8"));
        iYshunt2_lbl.setText(Global.getMessages().getString("SimulationPanel.tl.jtable1.columnName9"));
        addTl_btn.setText(Global.getMessages().getString("SimulationPanel.tl.jlabel3.label"));
        deleteTl_btn.setText(Global.getMessages().getString("SimulationPanel.tl.jlabel4.label"));

       
        updateList();
        wireTag1.removeAllItems();
        wireTag2.removeAllItems();
        for (int i = 0; i < global.getgWires().size(); i++) {
             if (i == global.getCurrentSourceTag() - 1){
                continue;
            }
            wireTag1.addItem(global.getgWires().get(i).getNumber() + "");
            wireTag2.addItem(global.getgWires().get(i).getNumber() + "");
        }
        wireTag1.revalidate();
        wireTag2.revalidate();  
        wireTag1.setSelectedIndex(0);
        wireTag2.setSelectedIndex(0);

        //Comportamiento del botón Agregar 
        addTl_btn.addActionListener((ActionEvent e) -> {
            if (validateInput()) {
                global.getgTl().add(addTl());
                global.updatePlot(global);                
                SwingUtilities.getWindowAncestor(this).dispose();
            }else{
                global.errorValidateInput();
            }
            updateList();
        });

       //Comportamiento del botón Editar 
        editTl_btn.addActionListener((ActionEvent e) -> {
            int tlId = tlList.getSelectedIndex();
            if (tlId != -1) {
                if (validateInput()) {
                    global.getgTl().add(addTl());
                    global.updatePlot(global);
                    SwingUtilities.getWindowAncestor(this).dispose();
                    updateList();
                } else {
                    global.errorValidateInput();
                }
            } else {
                global.errorMessage("Messages.noTlSelectedTitle", "Messages.noTlSelected");
            }

        });

       //Comportamiento del botón Quitar 
        deleteTl_btn.addActionListener((ActionEvent e) -> {
            if (tlList.getSelectedValue() != null) {
                String selectedItem = (String) tlList.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int selectedTl = Integer.valueOf(aux[1].trim());               
                for (int i = 0; i < global.getgTl().size(); i++) {
                    if (i == selectedTl) {
                        global.getgTl().remove(i);
                    }
                }
                updateList();
            }
        });

        tlList.addMouseListener(mouseListener);
    }

    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                String selectedItem = (String) tlList.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int tlId = Integer.valueOf(aux[1].trim());
                fillInfo(tlId);
            }
        }
    };

     /**
     * Obtiene los parámetros de la línea de transmisión identificada con el índice tlId y
     * los muestra en los diferentes componentes del panel
     *
     * @param tlId Índice de la fuente a mostrar
     */
    public void fillInfo(int tlId) {
        Tl editedTl = global.getgTl().get(tlId);
        wireTag1.setSelectedItem(editedTl.getTlWireTag1() + "");
        wireTag2.setSelectedItem(editedTl.getTlWireTag2() + "");
        int j = (Integer.valueOf(String.valueOf(wireTag1.getSelectedItem()))) - 1;
        int k = (Integer.valueOf(String.valueOf(wireTag2.getSelectedItem()))) - 1;
        Wire wire1 = global.getgWires().get(j);
        Wire wire2 = global.getgWires().get(k);
        double wireP1 = (Double.valueOf(editedTl.getTlWire1SegPercentage() + "") / Double.valueOf(wire1.getSegs() + "")) * 100;
        double wireP2 = (Double.valueOf(editedTl.getTlWire2SegPercentage() + "") / Double.valueOf(wire2.getSegs() + "")) * 100;
        wirePercentage1.setText(Integer.valueOf(Math.round(wireP1) + "") + "");
        wirePercentage2.setText(Integer.valueOf(Math.round(wireP2) + "") + "");
        double segPercentaje1 = Math.round((wireP1 / 100) * wire1.getSegs());
        double segPercentaje2 = Math.round((wireP2 / 100) * wire2.getSegs());
        wireSegPercentage1.setText(segPercentaje1 + "");
        wireSegPercentage2.setText(segPercentaje2 + "");
        z0.setText(editedTl.getTlCharImp() + "");
        rYshunt1.setText(editedTl.getReSY1() + "");
        iYshunt1.setText(editedTl.getImSY1() + "");
        rYshunt2.setText(editedTl.getReSY2() + "");
        iYshunt2.setText(editedTl.getImSY2() + "");
    }

    /**
     * Actualiza la lista de líneas de transmisión creadas
     */
    public void updateList() {
        String label = Global.getMessages().getString("SimulationPanel.tl.list.label");
        if (global.getgTl().size() > 0) {
            String[] data = new String[global.getgTl().size()];
            for (int i = 0; i < global.getgTl().size(); i++) {
                data[i] = String.valueOf(label + "  " + i);
            }
            tlList.setListData(data);
        } else {
            String[] empty = new String[0];
            tlList.setListData(empty);
        }
    }

    /**
     * Verifica si todos los campos jTextFormattedField contienen información,
     * además, chequea la validez de los datos introducidos en el panel
     *
     * @return true si todos los campos jTextFormattedField contienen
     * información válida, de lo contrario, devuelve false
     */
    public boolean validateInput() {
        boolean condition1, condition2, condition3, condition4, condition5;
        condition1 = (!wirePercentage1.getText().isEmpty())
                && (!wirePercentage2.getText().isEmpty())
                && (!z0.getText().isEmpty())
                && (!length.getText().isEmpty())
                && (!rYshunt1.getText().isEmpty())
                && (!iYshunt1.getText().isEmpty())
                && (!rYshunt2.getText().isEmpty())
                && (!iYshunt2.getText().isEmpty());
        if (condition1) {
            condition2 = Double.valueOf(z0.getText().replace(",", ".")) > 0;
            condition3 = (Integer.valueOf(wirePercentage1.getText().replace(",", ".")) >= 0)
                    && (Integer.valueOf(wirePercentage1.getText().replace(",", ".")) <= 100);
            condition4 = (Integer.valueOf(wirePercentage2.getText().replace(",", ".")) >= 0)
                    && (Integer.valueOf(wirePercentage2.getText().replace(",", ".")) <= 100);
            condition5 = (Integer.valueOf(String.valueOf(wireTag1.getSelectedItem()))) != (Integer.valueOf(String.valueOf(wireTag2.getSelectedItem())));
        } else {
            return false;
        }
        return condition1 && condition2 && condition3 && condition4 && condition5;
    }

    /**
     * Crea una nueva línea de transmisión a partir de los parámetros introducidos en el panel
     * y la agrega la lista global de líneas de transmisición
     *
     * @return Objeto de la clase Tl correspondiente a la nueva línea de transmisición
     */
    public Tl addTl() {
        Tl tl = new Tl();
        tl.setTlWireTag1(Integer.valueOf(String.valueOf(wireTag1.getSelectedItem())));
        tl.setTlWireTag2(Integer.valueOf(String.valueOf(wireTag2.getSelectedItem())));
        int j = (Integer.valueOf(String.valueOf(wireTag1.getSelectedItem()))) - 1;
        long segPercentage = Math.round(global.getgWires().get(j).getSegs() * (Double.valueOf(wirePercentage1.getText()) / 100.00));
        tl.setTlWire1SegPercentage(segPercentage);
        j = (Integer.valueOf(String.valueOf(wireTag2.getSelectedItem()))) - 1;
        segPercentage = Math.round(global.getgWires().get(j).getSegs() * Double.valueOf(wirePercentage2.getText()) / 100.00);
        tl.setTlWire2SegPercentage(segPercentage);
        tl.setTlCharImp(Double.valueOf(z0.getText().replace(",", ".")));
        tl.setTlLenght(Double.valueOf(length.getText().replace(",", ".")));
        tl.setReSY1(Double.valueOf(rYshunt1.getText().replace(",", ".")));
        tl.setImSY1(Double.valueOf(iYshunt1.getText().replace(",", ".")));
        tl.setReSY2(Double.valueOf(rYshunt2.getText().replace(",", ".")));
        tl.setImSY2(Double.valueOf(iYshunt2.getText().replace(",", ".")));
        return tl;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title_lbl = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        addTl_btn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        wireTag1_lbl = new javax.swing.JLabel();
        wireTag1 = new javax.swing.JComboBox<>();
        wirePercentage1_lbl = new javax.swing.JLabel();
        wirePercentage1 = new javax.swing.JFormattedTextField();
        wireSegPercentage1_lbl = new javax.swing.JLabel();
        wireSegPercentage1 = new javax.swing.JFormattedTextField();
        wireTag2_lbl = new javax.swing.JLabel();
        wireTag2 = new javax.swing.JComboBox<>();
        wirePercentage2_lbl = new javax.swing.JLabel();
        wirePercentage2 = new javax.swing.JFormattedTextField();
        wireSegPercentage2_lbl = new javax.swing.JLabel();
        wireSegPercentage2 = new javax.swing.JFormattedTextField();
        z0_lbl = new javax.swing.JLabel();
        z0 = new javax.swing.JFormattedTextField();
        length_lbl = new javax.swing.JLabel();
        length = new javax.swing.JFormattedTextField();
        rYshunt1_lbl = new javax.swing.JLabel();
        rYshunt1 = new javax.swing.JFormattedTextField();
        iYshunt1_lbl = new javax.swing.JLabel();
        iYshunt1 = new javax.swing.JFormattedTextField();
        rYshunt2_lbl = new javax.swing.JLabel();
        rYshunt2 = new javax.swing.JFormattedTextField();
        iYshunt2_lbl = new javax.swing.JLabel();
        iYshunt2 = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tlList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        editTl_btn = new javax.swing.JButton();
        deleteTl_btn = new javax.swing.JButton();

        setBackground(new java.awt.Color(36, 113, 163));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 5));
        setLayout(new java.awt.BorderLayout());

        title_lbl.setBackground(new java.awt.Color(36, 113, 163));
        title_lbl.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        title_lbl.setForeground(new java.awt.Color(255, 255, 255));
        title_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title_lbl.setText("jLabel1");
        add(title_lbl, java.awt.BorderLayout.NORTH);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        addTl_btn.setText("jButton2");
        jPanel2.add(addTl_btn, java.awt.BorderLayout.PAGE_END);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(12, 2));

        wireTag1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireTag1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireTag1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireTag1_lbl.setText("jLabel1");
        jPanel5.add(wireTag1_lbl);

        wireTag1.setBackground(new java.awt.Color(255, 255, 255));
        wireTag1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel5.add(wireTag1);

        wirePercentage1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wirePercentage1_lbl.setText("jLabel2");
        jPanel5.add(wirePercentage1_lbl);

        wirePercentage1.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage1.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wirePercentage1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(wirePercentage1);

        wireSegPercentage1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireSegPercentage1_lbl.setText("jLabel2");
        jPanel5.add(wireSegPercentage1_lbl);

        wireSegPercentage1.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage1.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wireSegPercentage1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        wireSegPercentage1.setEnabled(false);
        jPanel5.add(wireSegPercentage1);

        wireTag2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireTag2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireTag2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireTag2_lbl.setText("jLabel5");
        jPanel5.add(wireTag2_lbl);

        wireTag2.setBackground(new java.awt.Color(255, 255, 255));
        wireTag2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel5.add(wireTag2);

        wirePercentage2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wirePercentage2_lbl.setText("jLabel3");
        jPanel5.add(wirePercentage2_lbl);

        wirePercentage2.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage2.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wirePercentage2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(wirePercentage2);

        wireSegPercentage2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireSegPercentage2_lbl.setText("jLabel2");
        wireSegPercentage2_lbl.setEnabled(false);
        jPanel5.add(wireSegPercentage2_lbl);

        wireSegPercentage2.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage2.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wireSegPercentage2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(wireSegPercentage2);

        z0_lbl.setBackground(new java.awt.Color(255, 255, 255));
        z0_lbl.setForeground(new java.awt.Color(0, 0, 0));
        z0_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        z0_lbl.setText("jLabel4");
        jPanel5.add(z0_lbl);

        z0.setBackground(new java.awt.Color(255, 255, 255));
        z0.setForeground(new java.awt.Color(0, 0, 0));
        z0.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        z0.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(z0);

        length_lbl.setBackground(new java.awt.Color(255, 255, 255));
        length_lbl.setForeground(new java.awt.Color(0, 0, 0));
        length_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        length_lbl.setText("jLabel4");
        jPanel5.add(length_lbl);

        length.setBackground(new java.awt.Color(255, 255, 255));
        length.setForeground(new java.awt.Color(0, 0, 0));
        length.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        length.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(length);

        rYshunt1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        rYshunt1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        rYshunt1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rYshunt1_lbl.setText("jLabel4");
        jPanel5.add(rYshunt1_lbl);

        rYshunt1.setBackground(new java.awt.Color(255, 255, 255));
        rYshunt1.setForeground(new java.awt.Color(0, 0, 0));
        rYshunt1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        rYshunt1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(rYshunt1);

        iYshunt1_lbl.setBackground(new java.awt.Color(255, 255, 255));
        iYshunt1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        iYshunt1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iYshunt1_lbl.setText("jLabel4");
        jPanel5.add(iYshunt1_lbl);

        iYshunt1.setBackground(new java.awt.Color(255, 255, 255));
        iYshunt1.setForeground(new java.awt.Color(0, 0, 0));
        iYshunt1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        iYshunt1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(iYshunt1);

        rYshunt2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        rYshunt2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        rYshunt2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rYshunt2_lbl.setText("jLabel4");
        jPanel5.add(rYshunt2_lbl);

        rYshunt2.setBackground(new java.awt.Color(255, 255, 255));
        rYshunt2.setForeground(new java.awt.Color(0, 0, 0));
        rYshunt2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        rYshunt2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(rYshunt2);

        iYshunt2_lbl.setBackground(new java.awt.Color(255, 255, 255));
        iYshunt2_lbl.setForeground(new java.awt.Color(0, 0, 0));
        iYshunt2_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        iYshunt2_lbl.setText("jLabel4");
        jPanel5.add(iYshunt2_lbl);

        iYshunt2.setBackground(new java.awt.Color(255, 255, 255));
        iYshunt2.setForeground(new java.awt.Color(0, 0, 0));
        iYshunt2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        iYshunt2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(iYshunt2);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2);

        jPanel4.setLayout(new java.awt.BorderLayout());

        tlList.setBackground(new java.awt.Color(232, 232, 232));
        tlList.setForeground(new java.awt.Color(0, 0, 0));
        tlList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tlList);

        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        editTl_btn.setText("jButton1");
        jPanel3.add(editTl_btn);

        deleteTl_btn.setText("jButton1");
        jPanel3.add(deleteTl_btn);

        jPanel4.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel4);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTl_btn;
    private javax.swing.JButton deleteTl_btn;
    private javax.swing.JButton editTl_btn;
    private javax.swing.JFormattedTextField iYshunt1;
    private javax.swing.JLabel iYshunt1_lbl;
    private javax.swing.JFormattedTextField iYshunt2;
    private javax.swing.JLabel iYshunt2_lbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JFormattedTextField length;
    private javax.swing.JLabel length_lbl;
    private javax.swing.JFormattedTextField rYshunt1;
    private javax.swing.JLabel rYshunt1_lbl;
    private javax.swing.JFormattedTextField rYshunt2;
    private javax.swing.JLabel rYshunt2_lbl;
    private javax.swing.JLabel title_lbl;
    private javax.swing.JList<String> tlList;
    private javax.swing.JFormattedTextField wirePercentage1;
    private javax.swing.JLabel wirePercentage1_lbl;
    private javax.swing.JFormattedTextField wirePercentage2;
    private javax.swing.JLabel wirePercentage2_lbl;
    private javax.swing.JFormattedTextField wireSegPercentage1;
    private javax.swing.JLabel wireSegPercentage1_lbl;
    private javax.swing.JFormattedTextField wireSegPercentage2;
    private javax.swing.JLabel wireSegPercentage2_lbl;
    private javax.swing.JComboBox<String> wireTag1;
    private javax.swing.JLabel wireTag1_lbl;
    private javax.swing.JComboBox<String> wireTag2;
    private javax.swing.JLabel wireTag2_lbl;
    private javax.swing.JFormattedTextField z0;
    private javax.swing.JLabel z0_lbl;
    // End of variables declaration//GEN-END:variables
}
