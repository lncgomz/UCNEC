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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import ucnecgui.Global;
import ucnecgui.jframes.AnalyticalGeometry;
import ucnecgui.jframes.MultiFrame;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class GeometryPanel extends javax.swing.JPanel {

    public int tableRow;
    private Global global;
    public static JTable table;
    public static DefaultTableModel model;
    private static JLabel info = new JLabel();
    public static int currentSelectedRow;
    public boolean firstExecution;
    ArrayList<JFrame> childs = new ArrayList<JFrame>();

    /**
     * Constructor de la clase GeometryPanel
     *
     * @param global Objeto de la clase Global
     */
    public GeometryPanel(Global global) {
        initComponents();
        this.global = global;
        initializeTable();
    }

    /**
     * Inicializar tabla de geometría
     */
    public void initializeTable() {
        currentSelectedRow = -1; //Ninguna fila seleccionada
        firstExecution = true;
        // Texto de los componentes del GeometryPanel
        jLabel2.setText(Global.getMessages().
                getString("SimulationPanel.geometry.jlabel2.label"));
        jButton1.setText(Global.getMessages().
                getString("SimularionPanel.geometry.jlabel3.label"));
        jButton2.setText(Global.getMessages().
                getString("SimularionPanel.geometry.jlabel4.label"));
        addWire_btn.setText(Global.getMessages().
                getString("SimulationPanel.geometry.jbutton5.label"));
        editWire_btn.setText(Global.getMessages().
                getString("SimulationPanel.geometry.jbutton7.label"));
        deleteWire_btn.setText(Global.getMessages().
                getString("SimulationPanel.geometry.jbutton6.label"));

        // Títulos de las columnas de la tabla de geometrías
        String columnName[] = new String[9];
        for (int i = 0; i < 9; i++) {
            if (i >= 1 && i <= 6) {
                columnName[i] = Global.getMessages().
                        getString("SimulationPanel.geometry.jtable1.columnName" + i) + " " + "(" + global.unit2ShortString() + ")";
            } else if (i == 7) {
                columnName[i] = Global.getMessages().
                        getString("SimulationPanel.geometry.jtable1.columnName" + i) + " " + "(" + global.unit2LowerString() + ")";
            } else {
                columnName[i] = Global.getMessages().
                        getString("SimulationPanel.geometry.jtable1.columnName" + i);
            }
        }

        model = new DefaultTableModel() {

            @Override
            public int getColumnCount() {
                return columnName.length;
            }

            @Override
            public String getColumnName(int index) {
                return columnName[index];
            }

            @Override
            public boolean isCellEditable(int row,
                    int column) {
                return false;
            }

        };

        //Comportamiento del botón "Transformación de Coordenadas"
        jButton1.addActionListener((ActionEvent e) -> {
            int wires = global.getgWires().size();
            if (table.getSelectedRows().length > 0) { //Se requiere que se haya seleccionado al menos un alambre
                int[] selectedWires = findWireNumbers(table.getSelectedRows()); //Se busca el objeto Wire correspondiente al alambre seleccionado
                JFrame frame = new AnalyticalGeometry(400, 500, global, selectedWires, AnalyticalGeometry.TRANSFORMATION, wireSelector); //Se instancia un JFrame a través del método AnalyticalGeometry
                childs.add(frame); //La ventana AnalyticalGeometry se agrega como hija del GeometryPanel
                frame.setVisible(true);
                this.setFocusable(false);
                frame.setAlwaysOnTop(true);
            } else {
                global.errorMessage("Messages.noWiresSelectedTitle", "Messages.noWiresSelected");
            }

        });
        //Comportamiento del botón "Generador de Figuras"
        jButton2.addActionListener((ActionEvent e) -> {
            int[] selectedWires = findWireNumbers(table.getSelectedRows());
            JFrame frame = new AnalyticalGeometry(400, 500, global, selectedWires, AnalyticalGeometry.GENERATION, wireSelector);
            childs.add(frame);
            frame.setVisible(true);
            this.setFocusable(false);
            frame.setAlwaysOnTop(true);
        });

        //Comportamiento del botón "Añadir"
        addWire_btn.addActionListener((ActionEvent e) -> {
            MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("Multiframe.addGeometry")); //Se instancia un JFrame a través del método MultiFrame
            childs.add(frame);
            frame.add(new AddWirePanel(global, model, wireSelector)); //Se agrega un JPanel de la clase AddWirePanel a la ventana MultiFrame
            frame.pack();
            frame.setVisible(true);
        });

        //Comportamiento del botón "Editar"
        editWire_btn.addActionListener((ActionEvent e) -> {
            MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("Multiframe.editGeometry"));
            childs.add(frame);
            int wireId = table.getSelectedRow();
            if (wireId != -1) {
                frame.add(new AddWirePanel(global, model, findWireNumber(wireId)));
                frame.pack();
                frame.setVisible(true);
            } else {
                global.errorMessage("Messages.noWiresSelectedTitle", "Messages.noWiresSelected");
            }
        });

        //Comportamiento del botón "Quitar"
        deleteWire_btn.addActionListener((ActionEvent e) -> {
            deleteWires(); //Método que gestiona la eliminación de alambres y de los objetos agregados a los mismos (Fuentes, Cargas, Líneas de Transmisión, etc)
        });

        table = new JTable(model); //Instanciación de un objeto JTable a partir del modelo DefaultTableModel defnido anteriormente

        // Comportamiento de la tecla Supr en la tabla de geometría
        table.addKeyListener(new KeyAdapter() {
            boolean onePress = true;

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteWires();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                onePress = true;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        Object[] nullRow = {"", "", "", "", "", "", "", "", ""};
        jPanel3.add(scroll, BorderLayout.CENTER);
        if (global.getgWires().size() > 0) {
            Global.updateTable(model, global);
        }
        updateWireSelector();
        if (global.getgWires().size() > 0) {
            getInfo().setText(setInfoText(1, global.getgWires()));
        }
        info.setForeground(Color.WHITE);
        info.setBackground(new Color(36, 113, 163));
        info.setOpaque(true);
        jPanel10.add(getInfo());
    }

    /**
     * Encuentra los índices de los objetos Wire correspondientes a los alambres
     * seleccionados, indicados en el parámetro selectedRow
     *
     * @param selectedRow Vector con los índices de los alambres seleccionados
     * @return Vector con los índices de los objetos Wire correspondientes a los
     * alambres seleccionados
     */
    public int[] findWireNumbers(int[] selectedRow) {
        int[] resp = new int[selectedRow.length];
        int j = 0;
        for (int i : selectedRow) {
            resp[j] = Integer.valueOf(table.getValueAt(i, 0) + "");
            j++;
        }
        return resp;
    }

    /**
     * Encuentra el índice del objeto Wire correspondiente al alambre
     * seleccionado, indicado en el parámetro selectedRow
     *
     * @param selectedRow Vector con el índice del alambre seleccionado
     * @return Vector con el índice del objeto Wire correspondiente al alambre
     * seleccionado
     */
    public int findWireNumber(int selectedRow) {
        return Integer.valueOf(table.getValueAt(selectedRow, 0) + "");
    }

    /**
     * Elimina una serie de alambres seleccionados y los elementos agregados a
     * los mismos, si los hubiese
     */
    public void deleteWires() {
        int[] selectedRows = findWireNumbers(table.getSelectedRows());
        int[] alterSelectedRows = table.getSelectedRows();
        if (selectedRows.length > 0) {
            if (model.getRowCount() > 0) {
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    currentSelectedRow = -1;
                    model.removeRow(alterSelectedRows[i]);
                    global.cleanWireDelete(selectedRows[i]);
                    global.getgWires().remove(selectedRows[i] - 1);
                }
                int rowCount = model.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    model.setValueAt(i + 1, i, 0);
                }
                selectedRows = null;
                currentSelectedRow = -1;
                global.sortWires();
                Global.updateTable(model, global);
                updateWireSelector();
                model.fireTableDataChanged();
                global.updatePlot(global);
            }
        } else {
            global.errorMessage("Messages.noWiresSelectedTitle", "Messages.noWiresSelected");
        }
    }

    /**
     * Actualiza al objeto JComboBox correspondiente al selector de alambres,
     * excluyendo al alambre auxiliar de corriente, si lo hubiese
     */
    public void updateWireSelector() {
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
     *Genera el mensaje de información relacionado al alambre seleccionado
     * @param selectedWireTag Índice del alambre seleccionado
     * @param wires Arreglo de objetos de la clase Wire correspondientes a la geometría de la simulación
     * @return Texto con la información relacionada al alambre seleccionado
     */
    public String setInfoText(int selectedWireTag, ArrayList<Wire> wires) {
        String resp = "";
        double lowerFactor = global.unit2LowerFactor();
        double ld = Global.wireLength(wires.get(selectedWireTag - 1)) / (wires.get(selectedWireTag - 1).getRadius() * lowerFactor);

        resp = "Alambre número: " + wires.get(selectedWireTag - 1).getNumber() + " | " + " Longitud: " + Global.wireLength(wires.get(selectedWireTag - 1)) + " " + global.unit2ShortString() + " | "
                + " Diámetro: " + Global.decimalFormat(wires.get(selectedWireTag - 1).getRadius()) + " " + global.unit2LowerString() + " | " + "Razón L/D: " + Global.decimalShortFormat(ld);
        global.setSelectedRows(selectedWireTag);
        ;
        return resp;
    }

    /**
     *Devuelve el objeto JTable correspondiente a la tabla de geometría
     * @return Objeto JTable correspondiente a la tabla de geometría
     */
    public JTable getTable() {
        return this.table;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        wireSelector = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        addWire_btn = new javax.swing.JButton();
        editWire_btn = new javax.swing.JButton();
        deleteWire_btn = new javax.swing.JButton();

        jLabel2.setBackground(new java.awt.Color(0, 0, 50));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        setMaximumSize(new java.awt.Dimension(800, 700));
        setMinimumSize(new java.awt.Dimension(800, 700));
        setPreferredSize(new java.awt.Dimension(800, 700));
        setRequestFocusEnabled(false);
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                formFocusLost(evt);
            }
        });
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
        jPanel1.setLayout(new java.awt.GridLayout(2, 1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 50));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(1, 2));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setForeground(new java.awt.Color(0, 0, 50));
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jButton2);

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setForeground(new java.awt.Color(0, 0, 50));
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel2.add(jButton1);

        jPanel1.add(jPanel2);

        jPanel7.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel7, java.awt.BorderLayout.NORTH);

        jPanel5.setLayout(new java.awt.GridLayout(2, 1));

        jPanel8.setBackground(new java.awt.Color(36, 113, 163));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(200, 10));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(36, 113, 163));
        jLabel1.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Información del Alambre");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setOpaque(true);
        jPanel9.add(jLabel1, java.awt.BorderLayout.CENTER);

        wireSelector.setBackground(new java.awt.Color(36, 113, 163));
        wireSelector.setForeground(new java.awt.Color(255, 255, 255));
        wireSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        wireSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                wireSelectorItemStateChanged(evt);
            }
        });
        jPanel9.add(wireSelector, java.awt.BorderLayout.EAST);

        jPanel8.add(jPanel9, java.awt.BorderLayout.WEST);

        jPanel10.setLayout(new java.awt.BorderLayout());
        jPanel8.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel8);

        jPanel6.setBackground(new java.awt.Color(36, 113, 163));
        jPanel6.setPreferredSize(new java.awt.Dimension(600, 40));
        jPanel6.setLayout(new java.awt.GridLayout(1, 3));

        jPanel4.setLayout(new java.awt.GridLayout(1, 2));

        addWire_btn.setBackground(new java.awt.Color(255, 255, 255));
        addWire_btn.setForeground(new java.awt.Color(0, 0, 50));
        addWire_btn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        jPanel4.add(addWire_btn);

        editWire_btn.setBackground(new java.awt.Color(255, 255, 255));
        editWire_btn.setForeground(new java.awt.Color(0, 0, 50));
        editWire_btn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        jPanel4.add(editWire_btn);

        deleteWire_btn.setBackground(new java.awt.Color(255, 255, 255));
        deleteWire_btn.setForeground(new java.awt.Color(0, 0, 50));
        deleteWire_btn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        jPanel4.add(deleteWire_btn);

        jPanel6.add(jPanel4);

        jPanel5.add(jPanel6);

        jPanel3.add(jPanel5, java.awt.BorderLayout.SOUTH);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusLost
        this.setVisible(true);
    }//GEN-LAST:event_formFocusLost

    private void wireSelectorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_wireSelectorItemStateChanged
        Object s = wireSelector.getSelectedItem();
        if (getInfo() != null) {
            if (s != null) {
                int selectedTag = Integer.valueOf(wireSelector.getSelectedItem() + "");
                getInfo().setText(setInfoText(selectedTag, global.getgWires()));
                global.updatePlot(global);
            } else {
                getInfo().setText("No se ha especificado ningún alambre");
            }
        }
    }//GEN-LAST:event_wireSelectorItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addWire_btn;
    private javax.swing.JButton deleteWire_btn;
    private javax.swing.JButton editWire_btn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JComboBox<String> wireSelector;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the info
     */
    public static JLabel getInfo() {
        return info;
    }

    /**
     * @param aInfo the info to set
     */
    public static void setInfo(JLabel aInfo) {
        info = aInfo;
    }
}
