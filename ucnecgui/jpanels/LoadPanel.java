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
import javax.swing.JFrame;
import ucnecgui.Global;
import ucnecgui.models.Load;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class LoadPanel extends javax.swing.JPanel {

    private Global global;
    private JFrame frame;

    /**
     * Constructor de la clase LoadPanel
     *
     * @param global Objeto de la clase Global
     * @param frame Ventana JFrame que invoca a este panel
     */
    public LoadPanel(Global global, JFrame frame) {
        this.global = global;
        this.frame = frame;
        initComponents();
        initializePanel();
    }

    /**
     * Inicializar componentes del panel
     */
    public void initializePanel() {

        //Títulos de los componentes
        title_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jlabel2.label2"));
        wireTag_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M1.columnName1"));
        wirePercentage_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M1.columnName2"));
        r_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M1.columnName3"));
        x_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M1.columnName4"));
        wireTag_lbl1.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M2.columnName1"));
        wirePercentage_lbl1.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M2.columnName2"));
        wireSegPercentage_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M1.columnName5"));
        wireSegPercentage1_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M1.columnName5"));
        r_lbl1.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M2.columnName3"));
        l_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M2.columnName4"));
        c_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M2.columnName5"));
        type_lbl.setText(Global.getMessages().getString("SimulationPanel.load.jtable1M2.columnName6"));
        jTabbedPane1.setTitleAt(0, Global.getMessages().getString("SimulationPane1.load.jradiobutton1.label"));
        jTabbedPane1.setTitleAt(1, Global.getMessages().getString("SimulationPane1.load.jradiobutton2.label"));
        addLoadRX_btn.setText(Global.getMessages().getString("SimulationPanel.load.jlabel3.label"));
        editLoadRX_btn.setText(Global.getMessages().getString("SimulationPanel.load.jlabel5.label"));
        deleteLoadRX_btn.setText(Global.getMessages().getString("SimulationPanel.load.jlabel4.label"));
        addLoadRLC_btn.setText(Global.getMessages().getString("SimulationPanel.load.jlabel3.label"));
        editLoadRLC_btn.setText(Global.getMessages().getString("SimulationPanel.load.jlabel5.label"));
        deleteLoadRLC_btn.setText(Global.getMessages().getString("SimulationPanel.load.jlabel4.label"));

        //Tipo de Cargas
        type.removeAllItems();
        type.addItem(Global.getMessages().getString("SimulationPane1.load.jcombobox.valueS"));
        type.addItem(Global.getMessages().getString("SimulationPane1.load.jcombobox.valueP"));
        type.revalidate();

        //Número de los alambres disponibles (Excluyendo al alambre auxiliar de corriente, si lo hubiese) 
        for (Wire wire : global.getgWires()) {

            if (wire.getNumber() == global.getCurrentSourceTag()) {
                continue;
            }
            wireTag.addItem(wire.getNumber() + "");
            wireTag1.addItem(wire.getNumber() + "");
        }

        wireTag.revalidate();
        wireTag1.revalidate();

        updateList();
        wireTag.setSelectedIndex(0);
        wireTag1.setSelectedIndex(0);

        //Comportamiento del botón añadir para cargas RX
        addLoadRX_btn.addActionListener((ActionEvent e) -> {
            if (validateInput()) {
                global.getgLoad().add(addLoad());
                global.updatePlot(global);
                frame.dispose();
                updateList();
            } else {
                global.errorValidateInput();
            }

        });

        //Comportamiento del botón Agregar para cargas RLC
        addLoadRLC_btn.addActionListener((ActionEvent e) -> {
            if (validateInput1()) {
                global.getgLoad().add(addLoad1());
                frame.dispose();
                global.updatePlot(global);
                updateList();
            } else {
                global.errorValidateInput();
            }

        });

        //Comportamiento del botón Quitar para cargas RX
        deleteLoadRX_btn.addActionListener((ActionEvent e) -> {
            if (loadList.getSelectedValue() != null) {
                String selectedItem = (String) loadList.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int selectedLoad = Integer.valueOf(aux[1].trim());
                for (int i = 0; i < global.getgLoad().size(); i++) {
                    if (i == selectedLoad) {
                        global.getgLoad().remove(i);
                    }
                }
                updateList();
            }
        });

        //Comportamiento del botón Quitar para cargas RLC
        deleteLoadRLC_btn.addActionListener((ActionEvent e) -> {
            if (loadList1.getSelectedValue() != null) {
                String selectedItem = (String) loadList1.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int selectedLoad = Integer.valueOf(aux[1].trim());
                for (int i = 0; i < global.getgLoad().size(); i++) {
                    if (i == selectedLoad) {
                        global.getgLoad().remove(i);
                    }
                }
                updateList();
            }
        });

        //Comportamiento del botón Editar para cargas RX
        editLoadRX_btn.addActionListener((ActionEvent e) -> {
            int loadRXId = loadList.getSelectedIndex();
            if (loadRXId != -1) {
                if (validateInput()) {
                    global.getgLoad().set(loadRXId, addLoad());
                    global.updatePlot(global);
                    frame.dispose();
                    updateList();
                } else {
                    global.errorValidateInput();
                }
            } else {
                global.errorMessage("Messages.noLoadSelectedTitle", "Messages.noLoadSelected");
            }
        });
        //Comportamiento del botón Editar para cargas RLC
        editLoadRLC_btn.addActionListener((ActionEvent e) -> {
            int loadRLCId = loadList1.getSelectedIndex();
            if (loadRLCId != -1) {
                if (validateInput()) {
                    global.getgLoad().set(loadRLCId, addLoad1());
                    global.updatePlot(global);
                    frame.dispose();
                    updateList();
                } else {
                    global.errorValidateInput();
                }
            } else {
                global.errorMessage("Messages.noSourceSelectedTitle", "Messages.noSourceSelected");
            }
        });

        loadList.addMouseListener(mouseListener);
        loadList1.addMouseListener(mouseListener1);

    }

    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                String selectedItem = (String) loadList.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int loadId = Integer.valueOf(aux[1].trim());
                fillInfo(loadId);
            }
        }
    };

    MouseListener mouseListener1 = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                String selectedItem = (String) loadList1.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int loadId11 = Integer.valueOf(aux[1].trim());
                fillInfo1(loadId11);
            }
        }
    };

    /**
     * Carga información de una carga RX existente para visualizar sus
     * parámetros en el panel
     *
     * @param loadId Índice de la carga RX
     */
    public void fillInfo(int loadId) {
        Load editedLoad = global.getgLoad().get(loadId);
        wireTag.setSelectedItem(editedLoad.getLoadSeg() + "");

        Wire wire = global.getgWires().get(editedLoad.getLoadSeg() - 1);
        double wireP = (Double.valueOf(editedLoad.getLoadPercentage() + "") / Double.valueOf(wire.getSegs() + "")) * 100;
        wirePercentage.setText(Integer.valueOf(Math.round(wireP) + "") + "");
        double segPercentaje = Math.round((wireP / 100) * wire.getSegs());
        wireSegPercentage.setText(segPercentaje + "");
        r.setText(editedLoad.getR() + "");
        x.setText(editedLoad.getX() + "");
    }

    /**
     * Carga información de una carga RLC existente para visualizar sus
     * parámetros en el panel
     *
     * @param loadId1 Índice de la carga RLC
     */
    public void fillInfo1(int loadId1) {
        Load editedLoad = global.getgLoad().get(loadId1);
        wireTag1.setSelectedItem(editedLoad.getLoadSeg() + "");

        Wire wire = global.getgWires().get(editedLoad.getLoadSeg() - 1);
        double wireP = (Double.valueOf(editedLoad.getLoadPercentage() + "") / Double.valueOf(wire.getSegs() + "")) * 100;
        wirePercentage1.setText(Integer.valueOf(Math.round(wireP) + "") + "");
        double segPercentaje = Math.round((wireP / 100) * wire.getSegs());
        wireSegPercentage1.setText(segPercentaje + "");
        r1.setText(editedLoad.getR() + "");
        l.setText(editedLoad.getL() + "");
        c.setText(editedLoad.getC() + "");
    }

    /**
     * Actualiza lista de cargas existentes para ser mostradas en una lista
     */
    public void updateList() {
        String label = Global.getMessages().getString("SimulationPanel.load.list.label");
        if (global.getgLoad().size() > 0) {
            String[] data = new String[global.getgLoad().size()];
            for (int i = 0; i < global.getgLoad().size(); i++) {
                data[i] = String.valueOf(label + " " + i);
            }
            loadList.setListData(data);
            loadList1.setListData(data);
        } else {
            String[] empty = new String[0];
            loadList.setListData(empty);
            loadList1.setListData(empty);
        }
    }

    /**
     * Valida que los parámetros introducidos en los controles del panel sean
     * válidos, para cargas RX
     *
     * @return true si los parámetros introducidos son válidos, de los
     * contrario, devuelve false
     */
    public boolean validateInput() {
        boolean condition1, condition2, condition3;
        condition1 = (!wirePercentage.getText().isEmpty())
                && (!r.getText().isEmpty())
                && (!x.getText().isEmpty());
        if (condition1) {
            condition2 = Double.valueOf(r.getText().replace(",", ".")) > 0;
            condition3 = (Integer.valueOf(wirePercentage.getText().replace(",", ".")) >= 0)
                    && (Integer.valueOf(wirePercentage.getText().replace(",", ".")) <= 100);
        } else {
            return false;
        }
        return condition1 && condition2 && condition3;
    }

    /**
     * Valida que los parámetros introducidos en los controles del panel sean
     * válidos, para cargas RLC
     *
     * @return true si los parámetros introducidos son válidos, de los
     * contrario, devuelve false
     */
    public boolean validateInput1() {
        boolean condition1, condition2, condition3;
        condition1 = (!wirePercentage.getText().isEmpty())
                && (!r.getText().isEmpty())
                && (!x.getText().isEmpty());
        if (condition1) {
            condition2 = Double.valueOf(r.getText().replace(",", ".")) > 0;
            condition3 = (Integer.valueOf(wirePercentage.getText().replace(",", ".")) >= 0)
                    && (Integer.valueOf(wirePercentage.getText().replace(",", ".")) <= 100);
        } else {
            return false;
        }
        return condition1 && condition2 && condition3;
    }

    /**
     * Agregar un nuevo objeto tipo Load RX
     *
     * @return Objeto Load RX creado a partir de los parámetros introducidos en
     * el panel
     */
    public Load addLoad() {
        Load load = new Load();
        load.setType(4);
        int wireNumber = Integer.valueOf(String.valueOf(wireTag.getSelectedItem()));
        load.setLoadSeg(wireNumber);
        int j = wireNumber - 1;
        long segPercentage = Math.round(global.getgWires().get(j).getSegs() * Double.valueOf(wirePercentage.getText()) / 100);
        load.setLoadPercentage(segPercentage);
        load.setR(Double.valueOf(r.getText().replace(",", ".")));
        load.setX(Double.valueOf(x.getText().replace(",", ".")));
        return load;
    }

    /**
     * Agregar un nuevo objeto tipo Load RLC
     *
     * @return Objeto Load RLC creado a partir de los parámetros introducidos en
     * el panel
     */
    public Load addLoad1() {
        Load load = new Load();
        if (type.getSelectedIndex() == 0) {
            load.setType(0);
        } else {
            load.setType(1);
        }
        int wireNumber = Integer.valueOf(String.valueOf(wireTag1.getSelectedItem()));
        load.setLoadSeg(wireNumber);
        int i = wireNumber - 1;
        long segPercentage = Math.round(global.getgWires().get(i).getSegs() * Double.valueOf(wirePercentage1.getText()) / 100);
        load.setLoadPercentage(segPercentage);
        load.setR(Double.valueOf(r1.getText().replace(",", ".")));
        Double lValue = Double.valueOf(l.getText().replace(",", ".")) * Math.pow(10, -6);
        load.setL(Double.valueOf(lValue));
        Double cValue = Double.valueOf(c.getText().replace(",", ".")) * Math.pow(10, -12);
        load.setC(cValue);
        return load;
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        wireTag_lbl = new javax.swing.JLabel();
        wireTag = new javax.swing.JComboBox<>();
        wirePercentage_lbl = new javax.swing.JLabel();
        wirePercentage = new javax.swing.JFormattedTextField();
        wireSegPercentage_lbl = new javax.swing.JLabel();
        wireSegPercentage = new javax.swing.JFormattedTextField();
        r_lbl = new javax.swing.JLabel();
        r = new javax.swing.JFormattedTextField();
        x_lbl = new javax.swing.JLabel();
        x = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        addLoadRX_btn = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        loadList = new javax.swing.JList<>();
        jPanel12 = new javax.swing.JPanel();
        editLoadRX_btn = new javax.swing.JButton();
        deleteLoadRX_btn = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        wireTag_lbl1 = new javax.swing.JLabel();
        wireTag1 = new javax.swing.JComboBox<>();
        wirePercentage_lbl1 = new javax.swing.JLabel();
        wirePercentage1 = new javax.swing.JFormattedTextField();
        wireSegPercentage1_lbl = new javax.swing.JLabel();
        wireSegPercentage1 = new javax.swing.JFormattedTextField();
        r_lbl1 = new javax.swing.JLabel();
        r1 = new javax.swing.JFormattedTextField();
        l_lbl = new javax.swing.JLabel();
        l = new javax.swing.JFormattedTextField();
        c_lbl = new javax.swing.JLabel();
        c = new javax.swing.JFormattedTextField();
        type_lbl = new javax.swing.JLabel();
        type = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        addLoadRLC_btn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        loadList1 = new javax.swing.JList<>();
        jPanel13 = new javax.swing.JPanel();
        editLoadRLC_btn = new javax.swing.JButton();
        deleteLoadRLC_btn = new javax.swing.JButton();

        setBackground(new java.awt.Color(36, 113, 163));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 5));
        setForeground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 200));
        setLayout(new java.awt.BorderLayout());

        title_lbl.setBackground(new java.awt.Color(0, 0, 0));
        title_lbl.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        title_lbl.setForeground(new java.awt.Color(255, 255, 255));
        title_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title_lbl.setText("jLabel1");
        add(title_lbl, java.awt.BorderLayout.NORTH);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridLayout(5, 2));

        wireTag_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireTag_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireTag_lbl.setText("jLabel2");
        jPanel2.add(wireTag_lbl);

        wireTag.setBackground(new java.awt.Color(255, 255, 255));
        wireTag.setForeground(new java.awt.Color(0, 0, 0));
        jPanel2.add(wireTag);

        wirePercentage_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wirePercentage_lbl.setText("jLabel3");
        jPanel2.add(wirePercentage_lbl);

        wirePercentage.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wirePercentage.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(wirePercentage);

        wireSegPercentage_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireSegPercentage_lbl.setText("jLabel3");
        jPanel2.add(wireSegPercentage_lbl);

        wireSegPercentage.setEditable(false);
        wireSegPercentage.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wireSegPercentage.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(wireSegPercentage);

        r_lbl.setForeground(new java.awt.Color(0, 0, 0));
        r_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r_lbl.setText("jLabel4");
        jPanel2.add(r_lbl);

        r.setBackground(new java.awt.Color(255, 255, 255));
        r.setForeground(new java.awt.Color(0, 0, 0));
        r.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        r.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(r);

        x_lbl.setForeground(new java.awt.Color(0, 0, 0));
        x_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        x_lbl.setText("jLabel5");
        jPanel2.add(x_lbl);

        x.setBackground(new java.awt.Color(255, 255, 255));
        x.setForeground(new java.awt.Color(0, 0, 0));
        x.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        x.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(x);

        jPanel5.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel4.setBackground(new java.awt.Color(0, 0, 50));
        jPanel4.setLayout(new java.awt.BorderLayout());

        addLoadRX_btn.setText("jButton1");
        jPanel4.add(addLoadRX_btn, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel4, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel5);

        jPanel7.setBackground(new java.awt.Color(232, 232, 232));
        jPanel7.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.setLayout(new java.awt.BorderLayout());

        loadList.setBackground(new java.awt.Color(232, 232, 232));
        loadList.setForeground(new java.awt.Color(0, 0, 0));
        loadList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(loadList);

        jPanel7.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel12.setLayout(new java.awt.GridLayout(1, 2));

        editLoadRX_btn.setText("jButton1");
        jPanel12.add(editLoadRX_btn);

        deleteLoadRX_btn.setText("jButton1");
        jPanel12.add(deleteLoadRX_btn);

        jPanel7.add(jPanel12, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel7);

        jTabbedPane1.addTab("tab1", jPanel3);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.GridLayout(1, 2));

        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.GridLayout(7, 2));

        wireTag_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        wireTag_lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireTag_lbl1.setText("jLabel2");
        jPanel10.add(wireTag_lbl1);

        wireTag1.setBackground(new java.awt.Color(255, 255, 255));
        wireTag1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(wireTag1);

        wirePercentage_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage_lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wirePercentage_lbl1.setText("jLabel3");
        jPanel10.add(wirePercentage_lbl1);

        wirePercentage1.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage1.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wirePercentage1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel10.add(wirePercentage1);

        wireSegPercentage1_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage1_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireSegPercentage1_lbl.setText("jLabel3");
        jPanel10.add(wireSegPercentage1_lbl);

        wireSegPercentage1.setEditable(false);
        wireSegPercentage1.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage1.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wireSegPercentage1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel10.add(wireSegPercentage1);

        r_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        r_lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        r_lbl1.setText("jLabel4");
        jPanel10.add(r_lbl1);

        r1.setBackground(new java.awt.Color(255, 255, 255));
        r1.setForeground(new java.awt.Color(0, 0, 0));
        r1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        r1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel10.add(r1);

        l_lbl.setForeground(new java.awt.Color(0, 0, 0));
        l_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        l_lbl.setText("jLabel5");
        jPanel10.add(l_lbl);

        l.setBackground(new java.awt.Color(255, 255, 255));
        l.setForeground(new java.awt.Color(0, 0, 0));
        l.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        l.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel10.add(l);

        c_lbl.setForeground(new java.awt.Color(0, 0, 0));
        c_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        c_lbl.setText("jLabel5");
        jPanel10.add(c_lbl);

        c.setBackground(new java.awt.Color(255, 255, 255));
        c.setForeground(new java.awt.Color(0, 0, 0));
        c.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.0000"))));
        c.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel10.add(c);

        type_lbl.setForeground(new java.awt.Color(0, 0, 0));
        type_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        type_lbl.setText("jLabel5");
        jPanel10.add(type_lbl);

        type.setBackground(new java.awt.Color(255, 255, 255));
        type.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(type);

        jPanel9.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel11.setBackground(new java.awt.Color(0, 0, 50));
        jPanel11.setLayout(new java.awt.BorderLayout());

        addLoadRLC_btn.setText("jButton1");
        jPanel11.add(addLoadRLC_btn, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jPanel6.add(jPanel9);

        jPanel8.setBackground(new java.awt.Color(232, 232, 232));
        jPanel8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel8.setLayout(new java.awt.BorderLayout());

        loadList1.setBackground(new java.awt.Color(232, 232, 232));
        loadList1.setForeground(new java.awt.Color(0, 0, 0));
        loadList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(loadList1);

        jPanel8.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel13.setLayout(new java.awt.GridLayout(1, 2));

        editLoadRLC_btn.setText("jButton1");
        jPanel13.add(editLoadRLC_btn);

        deleteLoadRLC_btn.setText("jButton1");
        jPanel13.add(deleteLoadRLC_btn);

        jPanel8.add(jPanel13, java.awt.BorderLayout.SOUTH);

        jPanel6.add(jPanel8);

        jTabbedPane1.addTab("tab2", jPanel6);

        jPanel1.add(jTabbedPane1);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addLoadRLC_btn;
    private javax.swing.JButton addLoadRX_btn;
    private javax.swing.JFormattedTextField c;
    private javax.swing.JLabel c_lbl;
    private javax.swing.JButton deleteLoadRLC_btn;
    private javax.swing.JButton deleteLoadRX_btn;
    private javax.swing.JButton editLoadRLC_btn;
    private javax.swing.JButton editLoadRX_btn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JFormattedTextField l;
    private javax.swing.JLabel l_lbl;
    private javax.swing.JList<String> loadList;
    private javax.swing.JList<String> loadList1;
    private javax.swing.JFormattedTextField r;
    private javax.swing.JFormattedTextField r1;
    private javax.swing.JLabel r_lbl;
    private javax.swing.JLabel r_lbl1;
    private javax.swing.JLabel title_lbl;
    private javax.swing.JComboBox<String> type;
    private javax.swing.JLabel type_lbl;
    private javax.swing.JFormattedTextField wirePercentage;
    private javax.swing.JFormattedTextField wirePercentage1;
    private javax.swing.JLabel wirePercentage_lbl;
    private javax.swing.JLabel wirePercentage_lbl1;
    private javax.swing.JFormattedTextField wireSegPercentage;
    private javax.swing.JFormattedTextField wireSegPercentage1;
    private javax.swing.JLabel wireSegPercentage1_lbl;
    private javax.swing.JLabel wireSegPercentage_lbl;
    private javax.swing.JComboBox<String> wireTag;
    private javax.swing.JComboBox<String> wireTag1;
    private javax.swing.JLabel wireTag_lbl;
    private javax.swing.JLabel wireTag_lbl1;
    private javax.swing.JFormattedTextField x;
    private javax.swing.JLabel x_lbl;
    // End of variables declaration//GEN-END:variables
}
