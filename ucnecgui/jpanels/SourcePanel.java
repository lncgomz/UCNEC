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

import controllers.Complex;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import ucnecgui.Global;
import ucnecgui.models.Network;
import ucnecgui.models.Source;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class SourcePanel extends javax.swing.JPanel {

    private Global global;
    private JFrame frame;

    /**
     * Constructor de la clase SourcePanel
     *
     * @param global Objeto de la clase Global
     * @param frame Ventana que invoca este panel
     */
    public SourcePanel(Global global, JFrame frame) {
        this.global = global;
        this.frame = frame;
        initComponents();
        initializePanel();
    }

    /**
     * Inicializa los componentes del panel
     */
    public void initializePanel() {

        title_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jlabel2.label"));
        wireTag_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName1"));
        wirePercentage_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName2"));
        wireSegPercentage_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName6"));
        sourceAmplitude_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName3"));
        sourceAngle_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName4"));
        sourceType_lbl.setText(Global.getMessages().getString("SimulationPanel.source.jtable1.columnName5"));
        addSource_btn.setText(Global.getMessages().getString("SimulationPanel.source.jlabel3.label"));
        editSource_btn.setText(Global.getMessages().getString("SimulationPanel.geometry.jbutton7.label"));
        deleteSource_btn.setText(Global.getMessages().getString("SimulationPanel.source.jlabel4.label"));

        updateList();
        wireTag.removeAllItems();
        for (int i = 0; i < global.getgWires().size(); i++) {
            if (i == global.getCurrentSourceTag() - 1) {
                continue;
            }
            wireTag.addItem(i + 1 + "");
        }
        wireTag.revalidate();
        sourceType.removeAllItems();
        sourceType.addItem(Global.getMessages().getString("SimulationPane1.source.jcombobox.valueV"));
        sourceType.addItem(Global.getMessages().getString("SimulationPane1.source.jcombobox.valueI"));
        wireTag.revalidate();
        wireTag.setSelectedIndex(0);
        sourceType.setSelectedIndex(0);

        //Comportamiento del botón Agregar 
        addSource_btn.addActionListener((ActionEvent e) -> {
            if (validateInput()) {
                global.getgSource().add(addSource());
                global.updatePlot(global);
                frame.dispose();
            } else {
                global.errorValidateInput();
            }
            updateList();
        });
        //Comportamiento del botón Editar 
        editSource_btn.addActionListener((ActionEvent e) -> {
            int srcId = sourceList.getSelectedIndex();

            if (srcId != -1) {
                if (validateInput()) {
                    Source src = global.getgSource().get(srcId);
                    if (getRelatedNetwork(srcId) != -1) {
                        if (sourceType.getSelectedIndex() == 0) { // Cambiar fuente de corriente a fuente de voltaje
                            global.getgNetwork().remove(getRelatedNetwork(srcId));
                            Wire wire = global.getgWires().get(global.getCurrentSourceTag() - 1);
                            if (wire.getSegs() > 1) {
                                wire.setSegs(wire.getSegs() - 1);
                            } else {
                                global.getgWires().remove(global.getCurrentSourceTag() - 1);
                                global.getgSource().set(srcId, addSource());
                                global.setCurrentSourceTag(0);
                            }
                        } else { // Cambiar fuente de corriente a fuente de corriente
                            int wireId = Integer.valueOf(wireTag.getSelectedItem() + "");
                            global.getgNetwork().get(getRelatedNetwork(srcId)).setTagNumberPort2(wireId);
                            long segPercentage = Math.round(global
                                    .getgWires().get(wireId - 1).getSegs()
                                    * (Double.valueOf(Double.valueOf(wirePercentage.getText().replace(",", "."))) / 100));
                            if (segPercentage == 0l) {
                                segPercentage = 1l;
                            }
                            global.getgNetwork().get(getRelatedNetwork(srcId)).setPort2Segment(segPercentage);
                            double amplitudREValue = (Double.valueOf(sourceAmplitude.getText().replace(",", "."))) * Math.sqrt(2) * Math.cos(Math.toRadians(Double.valueOf(sourceAngle.getText().replace(",", "."))));
                            src.setSourceAmplitudeRE(amplitudREValue);
                            double amplitudIMValue = (Double.valueOf(sourceAmplitude.getText().replace(",", "."))) * Math.sqrt(2) * Math.sin(Math.toRadians(Double.valueOf(sourceAngle.getText().replace(",", "."))));
                            if (amplitudIMValue == 0) {
                                src.setSourceAmplitudeIM(1E-15);
                            } else {
                                src.setSourceAmplitudeIM(amplitudIMValue);
                            }
                        }
                    } else {
                        if (sourceType.getSelectedIndex() == 0) { // Cambiar fuente de voltaje a fuente de voltaje
                            global.getgSource().set(srcId, addSource());
                        } else {
                            global.getgSource().set(srcId, generateCurrentSource());
                        }
                    }
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

        //Comportamiento del botón Quitar 
        deleteSource_btn.addActionListener((ActionEvent e) -> {
            if (sourceList.getSelectedValue() != null) {
                String selectedItem = (String) sourceList.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int selectedSource = Integer.valueOf(aux[1].trim());
                for (int i = 0; i < global.getgSource().size(); i++) {
                    if (i == selectedSource) {
                        int relatedNetwork = getRelatedNetwork(i);
                        if (relatedNetwork != -1) {
                            Wire relatedWire = global.getgWires().get(global.getCurrentSourceTag() - 1);
                            if (relatedWire.getSegs() > 1) {
                                relatedWire.setSegs(relatedWire.getSegs() - 1);
                            } else {
                                global.getgWires().remove(global.getCurrentSourceTag() - 1);
                                global.setCurrentSourceTag(0);
                            }
                            global.getgNetwork().remove(relatedNetwork);
                            global.getgSource().remove(i);
                        } else {
                            global.getgSource().remove(i);
                        }
                    }
                }
                updateList();
            }
        });

        sourceList.addMouseListener(mouseListener);
    }

    /**
     * Determina si una fuente de índice sourceIndex está conectada a una red
     *
     * @param sourceIndex Índice de la fuente en evaluación
     * @return -1 si la fuente no está conectada a una red; en caso de estarlo,
     * devuelve el índice de la red conectada a la fuente bajo evaluación
     */
    public int getRelatedNetwork(int sourceIndex) {
        int resp = -1;
        ArrayList<Network> network = global.getgNetwork();
        Source src = global.getgSource().get(sourceIndex);
        int i = 0;
        for (Network nt : network) {
            if (nt.getTagNumberPort1() == src.getSourceSeg() && nt.getPort1Segment() == src.getSegPercentage()) {
                resp = i;
            }
            i++;
        }
        return resp;
    }

    MouseListener mouseListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                String selectedItem = (String) sourceList.getSelectedValue();
                String[] aux = selectedItem.split(":");
                int srcId = Integer.valueOf(aux[1].trim());
                fillInfo(srcId);
            }
        }
    };

    /**
     * Obtiene los parámetros de la fuente identificada con el índice srcId y
     * los muestra en los diferentes componentes del panel
     *
     * @param srcId Índice de la fuente a mostrar
     */
    public void fillInfo(int srcId) {
        Source editedSrc = global.getgSource().get(srcId);

        if (getRelatedNetwork(srcId) != -1) {
            int relatedNetwork = getRelatedNetwork(srcId);
            Network related = global.getgNetwork().get(relatedNetwork);
            sourceType.setSelectedIndex(1);
            wireTag.setSelectedItem(related.getTagNumberPort2() + "");
            Wire wire = global.getgWires().get(related.getTagNumberPort2() - 1);
            double wireP = (Double.valueOf(related.getPort2Segment() + "") / Double.valueOf(wire.getSegs() + "")) * 100;
            wirePercentage.setText(Integer.valueOf(Math.round(wireP) + "") + "");
            double segPercentaje = Math.round((wireP / 100) * wire.getSegs());
            wireSegPercentage.setText(segPercentaje + "");
            Complex srcValue = new Complex(-1 * (editedSrc.getSourceAmplitudeRE() / Math.sqrt(2)), editedSrc.getSourceAmplitudeIM() / Math.sqrt(2));
            DecimalFormat df = new DecimalFormat("#0.00");
            sourceAmplitude.setText(df.format(srcValue.abs()) + "");
            sourceAngle.setText(df.format(Math.toDegrees(srcValue.phase())) + "");
        } else {
            sourceType.setSelectedIndex(0);
            wireTag.setSelectedItem(editedSrc.getSourceSeg() + "");
            Wire wire = global.getgWires().get(editedSrc.getSourceSeg() - 1);
            double wireP = (Double.valueOf(editedSrc.getSegPercentage() + "") / Double.valueOf(wire.getSegs() + "")) * 100;
            wirePercentage.setText(Integer.valueOf(Math.round(wireP) + "") + "");
            double segPercentaje = Math.round((wireP / 100) * wire.getSegs());
            wireSegPercentage.setText(segPercentaje + "");
            Complex srcValue = new Complex(editedSrc.getSourceAmplitudeRE() / Math.sqrt(2), editedSrc.getSourceAmplitudeIM() / Math.sqrt(2));
            DecimalFormat df = new DecimalFormat("#0.00");
            sourceAmplitude.setText(df.format(srcValue.abs()) + "");

            sourceAngle.setText(df.format(Math.toDegrees(srcValue.phase())) + "");
        }

    }

    /**
     * Actualiza la lista de fuentes creadas
     */
    public void updateList() {
        String label = Global.getMessages().getString("SimulationPanel.source.list.label");
        if (global.getgSource().size() > 0) {
            String[] data = new String[global.getgSource().size()];
            for (int i = 0; i < global.getgSource().size(); i++) {
                data[i] = String.valueOf(label + " " + i);
            }
            sourceList.setListData(data);
        } else {
            String[] empty = new String[0];
            sourceList.setListData(empty);
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
        boolean condition1, condition2, condition3;
        condition1 = (!wirePercentage.getText().isEmpty())
                && (!sourceAmplitude.getText().isEmpty())
                && (!sourceAngle.getText().isEmpty());
        if (condition1) {
            condition2 = Double.valueOf(sourceAmplitude.getText().replace(",", ".")) >= 0;
            condition3 = (Integer.valueOf(wirePercentage.getText().replace(",", ".")) > 0)
                    && (Integer.valueOf(wirePercentage.getText().replace(",", ".")) <= 100);
        } else {
            return false;
        }
        return condition1 && condition2 && condition3;
    }

    /**
     * Crea una nueva fuente de voltaje a partir de los parámetros introducidos en el panel
     * y la agrega la lista global de fuentes
     *
     * @return Objeto de la clase Source correspondiente a la nueva fuente de voltaje
     */
    public Source addSource() {
        Source source = new Source();
        int wireNumber = Integer.valueOf(wireTag.getSelectedItem() + "");
        if (sourceType.getSelectedIndex() == 0) {
            source.setSourceTypeId(0);
            source.setSourceSeg(Integer.valueOf(wireTag.getSelectedItem().toString()));
            source.setPercentage(Integer.valueOf(wirePercentage.getText()));
            long segPercentage = Math.round(global
                    .getgWires().get(wireNumber - 1).getSegs()
                    * (Double.valueOf(Double.valueOf(wirePercentage.getText().replace(",", "."))) / 100));
            if (segPercentage == 0l) {
                segPercentage = 1l;
            }
            source.setSegPercentage(segPercentage);
            source.setI4(0);

            double amplitudREValue = (Double.valueOf(sourceAmplitude.getText().replace(",", "."))) * Math.sqrt(2) * Math.cos(Math.toRadians(Double.valueOf(sourceAngle.getText().replace(",", "."))));
            source.setSourceAmplitudeRE(amplitudREValue);

            double amplitudIMValue = (Double.valueOf(sourceAmplitude.getText().replace(",", "."))) * Math.sqrt(2) * Math.sin(Math.toRadians(Double.valueOf(sourceAngle.getText().replace(",", "."))));
            if (amplitudIMValue == 0) {
                source.setSourceAmplitudeIM(1E-15);
            } else {
                source.setSourceAmplitudeIM(amplitudIMValue);
            }
        } else {
            source = generateCurrentSource();
        }
        return source;
    }

    /**
     * Crea una nueva fuente de corriente a partir de los parámetros introducidos en el panel
     * y la agrega la lista global de fuentes
     *
     * @return Objeto de la clase Source correspondiente a la nueva fuente de corriente
     */
    public Source generateCurrentSource() {

        int wireISource = global.getCurrentSourceTag();
        int sourceCount = global.getgNetwork().size();

        boolean thereIsSupportWire = sourceCount > 0;

        if (wireISource == 0) {
            wireISource = global.getgWires().size() + 1;
            global.setCurrentSourceTag(wireISource);
        }

        Source source = new Source();
        source.setSourceTypeId(0);
        source.setSourceSeg(wireISource);
        source.setPercentage(100);
        source.setSegPercentage(sourceCount + 1);
        source.setI4(0);
        double amplitudREValue = (Double.valueOf(sourceAmplitude.getText().replace(",", "."))) * Math.sqrt(2) * Math.cos(Math.toRadians(Double.valueOf(sourceAngle.getText().replace(",", "."))));
        source.setSourceAmplitudeRE(-1 * amplitudREValue);
        double amplitudIMValue = (Double.valueOf(sourceAmplitude.getText().replace(",", "."))) * Math.sqrt(2) * Math.sin(Math.toRadians(Double.valueOf(sourceAngle.getText().replace(",", "."))));
        if (amplitudIMValue == 0) {
            source.setSourceAmplitudeIM(1E-15);
        } else {
            source.setSourceAmplitudeIM(amplitudIMValue);
        }
        double wl = global.getWavelength();

        //Creación del alambre auxiliar de corriente
        Wire iSrcWire = null;
        if (!thereIsSupportWire) {
            iSrcWire = new Wire();
            iSrcWire.setNumber(wireISource);
            iSrcWire.setSegs(1);
            iSrcWire.setX1(100 * wl);
            iSrcWire.setY1(100 * wl);
            iSrcWire.setZ1(100 * wl);
            iSrcWire.setX2((100 * wl) + 0.001);
            iSrcWire.setY2((100 * wl) + 0.001);
            iSrcWire.setZ2((100 * wl) + 0.001);
            iSrcWire.setRadius(0.0002);
            global.getgWires().add(iSrcWire);
        } else {
            iSrcWire = global.getCurrentSourceWire(global.getCurrentSourceTag());
            iSrcWire.setSegs(sourceCount + 1);
        }

        Network network = new Network();
        network.setTagNumberPort1(global.getCurrentSourceTag());
        network.setPort1Segment(sourceCount + 1);
        network.setTagNumberPort2(Integer.valueOf(wireTag.getSelectedItem().toString()));
        int wireNumber = Integer.valueOf(wireTag.getSelectedItem() + "");
        long segPercentage = Math.round(global
                .getgWires().get(wireNumber - 1).getSegs()
                * (Double.valueOf(Double.valueOf(wirePercentage.getText().replace(",", "."))) / 100));
        if (segPercentage == 0l) {
            segPercentage = 1l;
        }
        network.setPort2Segment(segPercentage);
        network.setRealg11(0);
        network.setImg11(0);
        network.setRealg12(0);
        network.setImg12(1);
        network.setRealg22(0);
        network.setImg22(0);
        global.getgNetwork().add(network);

        return source;

    }

    /**
     * Genera el comando NEC2 de tipo de excitación a partir de los parámetros
     * introducidos en el panel
     *@param model Objeto de la clase DefaultTableModel
     * @return Comando de excitación de NEC2
     */
    public ArrayList<Source> generateEXCard(DefaultTableModel model) {
        ArrayList<Source> resp = new ArrayList<>();
        String[][] data = new String[model.getRowCount()][model.
                getColumnCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                data[i][j] = model.getValueAt(i, j).
                        toString();
            }
        }
        int rows = model.getRowCount();
        for (int i = 0; i < rows; i++) {
            Source source = new Source();
            if (data[i][5].equalsIgnoreCase("V")) {
                source.setSourceTypeId(0);
            } else {
                source.setSourceTypeId(4);
            }
            source.setSourceSeg(Integer.valueOf(data[i][1]));
            int j = Integer.valueOf(data[i][1]) - 1;
            int segPercentage = (int) Math.round(global.
                    getgWires().get(j).getSegs() * Double.
                    valueOf(data[i][2]) / 100);
            source.setSegPercentage(segPercentage);
            source.setI4(0);

            double amplitudREValue = (Double.
                    valueOf(data[i][3])) * Math.sqrt(2);
            source.setSourceAmplitudeRE(amplitudREValue);
            source.setSourceAmplitudeIM(Double.
                    valueOf(data[i][4]));
            resp.add(source);
        }
        return resp;
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
        addSource_btn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        wireTag_lbl = new javax.swing.JLabel();
        wireTag = new javax.swing.JComboBox<>();
        wirePercentage_lbl = new javax.swing.JLabel();
        wirePercentage = new javax.swing.JFormattedTextField();
        wireSegPercentage_lbl = new javax.swing.JLabel();
        wireSegPercentage = new javax.swing.JFormattedTextField();
        sourceAmplitude_lbl = new javax.swing.JLabel();
        sourceAmplitude = new javax.swing.JFormattedTextField();
        sourceAngle_lbl = new javax.swing.JLabel();
        sourceAngle = new javax.swing.JFormattedTextField();
        sourceType_lbl = new javax.swing.JLabel();
        sourceType = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        sourceList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        editSource_btn = new javax.swing.JButton();
        deleteSource_btn = new javax.swing.JButton();

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

        addSource_btn.setText("jButton2");
        jPanel2.add(addSource_btn, java.awt.BorderLayout.PAGE_END);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(6, 2));

        wireTag_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireTag_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireTag_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireTag_lbl.setText("jLabel1");
        jPanel5.add(wireTag_lbl);

        wireTag.setBackground(new java.awt.Color(255, 255, 255));
        wireTag.setForeground(new java.awt.Color(0, 0, 0));
        wireTag.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(wireTag);

        wirePercentage_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wirePercentage_lbl.setText("jLabel2");
        jPanel5.add(wirePercentage_lbl);

        wirePercentage.setBackground(new java.awt.Color(255, 255, 255));
        wirePercentage.setForeground(new java.awt.Color(0, 0, 0));
        wirePercentage.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wirePercentage.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(wirePercentage);

        wireSegPercentage_lbl.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage_lbl.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        wireSegPercentage_lbl.setText("jLabel2");
        jPanel5.add(wireSegPercentage_lbl);

        wireSegPercentage.setBackground(new java.awt.Color(255, 255, 255));
        wireSegPercentage.setForeground(new java.awt.Color(0, 0, 0));
        wireSegPercentage.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        wireSegPercentage.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        wireSegPercentage.setEnabled(false);
        jPanel5.add(wireSegPercentage);

        sourceAmplitude_lbl.setBackground(new java.awt.Color(255, 255, 255));
        sourceAmplitude_lbl.setForeground(new java.awt.Color(0, 0, 0));
        sourceAmplitude_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sourceAmplitude_lbl.setText("jLabel3");
        jPanel5.add(sourceAmplitude_lbl);

        sourceAmplitude.setBackground(new java.awt.Color(255, 255, 255));
        sourceAmplitude.setForeground(new java.awt.Color(0, 0, 0));
        sourceAmplitude.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        sourceAmplitude.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(sourceAmplitude);

        sourceAngle_lbl.setBackground(new java.awt.Color(255, 255, 255));
        sourceAngle_lbl.setForeground(new java.awt.Color(0, 0, 0));
        sourceAngle_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sourceAngle_lbl.setText("jLabel4");
        jPanel5.add(sourceAngle_lbl);

        sourceAngle.setBackground(new java.awt.Color(255, 255, 255));
        sourceAngle.setForeground(new java.awt.Color(0, 0, 0));
        sourceAngle.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        sourceAngle.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(sourceAngle);

        sourceType_lbl.setBackground(new java.awt.Color(255, 255, 255));
        sourceType_lbl.setForeground(new java.awt.Color(0, 0, 0));
        sourceType_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sourceType_lbl.setText("jLabel5");
        jPanel5.add(sourceType_lbl);

        sourceType.setBackground(new java.awt.Color(255, 255, 255));
        sourceType.setForeground(new java.awt.Color(0, 0, 0));
        sourceType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel5.add(sourceType);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2);

        jPanel4.setLayout(new java.awt.BorderLayout());

        sourceList.setBackground(new java.awt.Color(232, 232, 232));
        sourceList.setForeground(new java.awt.Color(0, 0, 0));
        sourceList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(sourceList);

        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        editSource_btn.setText("jButton1");
        jPanel3.add(editSource_btn);

        deleteSource_btn.setText("jButton1");
        jPanel3.add(deleteSource_btn);

        jPanel4.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel4);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSource_btn;
    private javax.swing.JButton deleteSource_btn;
    private javax.swing.JButton editSource_btn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JFormattedTextField sourceAmplitude;
    private javax.swing.JLabel sourceAmplitude_lbl;
    private javax.swing.JFormattedTextField sourceAngle;
    private javax.swing.JLabel sourceAngle_lbl;
    private javax.swing.JList<String> sourceList;
    private javax.swing.JComboBox<String> sourceType;
    private javax.swing.JLabel sourceType_lbl;
    private javax.swing.JLabel title_lbl;
    private javax.swing.JFormattedTextField wirePercentage;
    private javax.swing.JLabel wirePercentage_lbl;
    private javax.swing.JFormattedTextField wireSegPercentage;
    private javax.swing.JLabel wireSegPercentage_lbl;
    private javax.swing.JComboBox<String> wireTag;
    private javax.swing.JLabel wireTag_lbl;
    // End of variables declaration//GEN-END:variables
}
