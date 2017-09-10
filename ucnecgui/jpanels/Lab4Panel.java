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

import controllers.NECParser;
import controllers.PDFReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.table.DefaultTableModel;
import ucnecgui.Global;
import ucnecgui.MetaGlobal;
import static ucnecgui.jpanels.NECModulePanel.generateInputFile;
import static ucnecgui.jpanels.NECModulePanel.generateOutputData;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.PolarData;
import ucnecgui.models.RPLine;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */

public class Lab4Panel extends javax.swing.JPanel {

    private Global global;
    private DefaultTableModel model;
    private int step = 0;
    private ArrayList<Wire> originalWire;
    private ArrayList<PolarData> localPolarData;

    /**
     * Constructor de la clase Lab4Panel
     * @param global Objeto de la clase Global
     */
    public Lab4Panel(Global global) {
        initComponents();
        this.global = global;
    }

    /**
     * Exporta los resultados del experimento 1 a un archivo de texto
     *
     * @return Arreglo de objetos String con los resultados del experimento 1
     */
    public ArrayList<String> exportLab41() {
        ArrayList<String> resp = new ArrayList<String>();
        resp.add("Distancia, Lambda, Pta (W), Ptb(W), Prb (W), Prc (W), Ganancia A (dB),  Ganancia B (dB), Ganancia C (dB)");
        resp.add(rvalue.getText() + "," + lvalue.getText() + "," + pta.getText() + "," + ptb.getText() + "," + prb.getText() + "," + prc.getText() + "");
        return resp;
    }

    /**
     * Exporta los resultados del experimento 2 a un archivo de texto
     *
     * @return Arreglo de objetos String con los resultados del experimento 2
     */
    public ArrayList<String> exportLab42() {
        ArrayList<String> resp = new ArrayList<String>();
        if (gsiVal.isSelected()) {
            resp.add("Ganancia Dipolo, Pdma (W), Pdms (W), Ganancia ABP (dBi)");
            resp.add(gsivalue.getText() + "," + pdmavalue.getText() + "," + pdms.getText() + "," + gai.getText());
            return resp;
        }
        resp.add("Ganancia Dipolo (dB), Pdma (W), Pdms (W), Ganancia ABP (dBi)");
        resp.add(gsidbvalue.getText() + "," + pdmavalue.getText() + "," + pdms.getText() + "," + gai.getText());
        return resp;
    }

    /**
     * Obtiene el valor máximo de una serie de objetos de la clase PolarData
     *
     * @param samples Arreglo de objetos de la clase PolarData
     * @return Valor r máximo entre una serie de objetos de la clase PolarData
     */
    public double getMaxValue(ArrayList<PolarData> samples) {
        boolean first = true;
        double aux = 0;
        for (PolarData sample : samples) {
            if (first) {
                aux = sample.getR();
                first = false;
            } else if (aux < sample.getR()) {
                aux = sample.getR();
            }
        }
        return aux;
    }

    /**
     * Obtiene el valor mínimo de una serie de objetos de la clase PolarData
     *
     * @param samples Arreglo de objetos de la clase PolarData
     * @return Valor r mínimo entre una serie de objetos de la clase PolarData
     */
    public double getMinValue(ArrayList<PolarData> samples) {
        boolean first = true;
        double resp = 0;
        double aux = 0;
        for (PolarData sample : samples) {
            if (first) {
                aux = sample.getR();
                first = false;
            } else if (aux > sample.getR()) {
                aux = sample.getR();
            }
        }
        return aux;
    }

  /**
     * Verifica la validez de los valores introducidos en el apartado Variables del
     * experimento 1
     *
     * @return true si los valores introducidos son correctos, de lo contrario,
     * devuelve false
     */
    public boolean validateLab411() {
        return !rvalue.getText().isEmpty()
                && !lvalue.getText().isEmpty()
                && !pta.getText().isEmpty()
                && !ptb.getText().isEmpty()
                && !prb.getText().isEmpty()
                && !prc.getText().isEmpty();
    }

    /**
     * Ejecuta la simulación del sistema descrito a través de los parámetros
     * introducidos en los controles propios del experimento, introduciendo el
     * resultado del mismo en una tabla
     *
     * @param showRP Indica si se mostrará la gráfica del patrón de radiación
     * después de la ejecución de la simulación
     */
    public void runSimulation(boolean showRP) {
        HashMap<String, ArrayList<RPLine>> output = new HashMap<String, ArrayList<RPLine>>();
        LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>> srcInfo = new LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>>();
        boolean isOutputGenerated = false;
        boolean isSimulationOK = false;

        generateInputFile(true, global);
        isOutputGenerated = generateOutputData();
        if (isOutputGenerated) {
            isSimulationOK = NECParser.isSimulationOK(global);
        }
        if (isSimulationOK) {

            if (global.getCurrentPlotType() == Global.PLOT3D) {
                generateInputFile(true, global);
                isOutputGenerated = generateOutputData();
                output.put("PLOT3D", NECParser.getRP3D(global.readOutputData(isOutputGenerated), global));
                for (int i = 0; i < global.getgSource().size(); i++) {
                    LinkedHashMap<Double, ArrayList<AntInputLine>> val = NECParser.getAntennaInput(global.readOutputData(isOutputGenerated), global, i);
                    srcInfo.put((i + 1), val);
                }
            }
            ArrayList<String> powerData = NECModulePanel.generatePowers(global);
            global.setgSrcInfo(srcInfo);
            NECModulePanel.calculatePowerBudget(powerData, global);
            if (showRP) {
                NECModulePanel.generateRPPlot(output, global);
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
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        rvalue = new javax.swing.JFormattedTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        lvalue = new javax.swing.JFormattedTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        pta = new javax.swing.JFormattedTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        ptb = new javax.swing.JFormattedTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        prb = new javax.swing.JFormattedTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        prc = new javax.swing.JFormattedTextField();
        jPanel22 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lab1ExportResult1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        ga = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        gb = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        gc = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lab1Instruction = new javax.swing.JButton();
        lab1GoNEC = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        gsivalue = new javax.swing.JFormattedTextField();
        gsiVal = new javax.swing.JRadioButton();
        jPanel34 = new javax.swing.JPanel();
        gsidbvalue = new javax.swing.JFormattedTextField();
        gsidbVal = new javax.swing.JRadioButton();
        jPanel29 = new javax.swing.JPanel();
        pdma = new javax.swing.JLabel();
        pdmavalue = new javax.swing.JFormattedTextField();
        jPanel30 = new javax.swing.JPanel();
        pdms = new javax.swing.JLabel();
        pdmsvalue = new javax.swing.JFormattedTextField();
        jPanel35 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        lab1ExportResult = new javax.swing.JButton();
        jPanel36 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        gai = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        lab1Instruction1 = new javax.swing.JButton();
        lab1GoNEC1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel31.setLayout(new java.awt.GridLayout(1, 2));

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBackground(new java.awt.Color(36, 113, 163));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(36, 113, 163));
        jPanel8.setPreferredSize(new java.awt.Dimension(250, 424));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(36, 113, 163));
        jPanel12.setLayout(new java.awt.BorderLayout());

        jLabel8.setBackground(new java.awt.Color(36, 113, 163));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Variables");
        jPanel12.add(jLabel8, java.awt.BorderLayout.NORTH);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setPreferredSize(new java.awt.Dimension(10, 100));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setPreferredSize(new java.awt.Dimension(10, 300));
        jPanel15.setLayout(new java.awt.GridLayout(6, 1));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Distancia (m)");
        jLabel9.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel16.add(jLabel9, java.awt.BorderLayout.WEST);

        rvalue.setBackground(new java.awt.Color(255, 255, 255));
        rvalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel16.add(rvalue, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel16);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Lambda (m)");
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel17.add(jLabel11, java.awt.BorderLayout.WEST);

        lvalue.setBackground(new java.awt.Color(255, 255, 255));
        lvalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel17.add(lvalue, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel17);

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Pta (W)");
        jLabel13.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel18.add(jLabel13, java.awt.BorderLayout.WEST);

        pta.setBackground(new java.awt.Color(255, 255, 255));
        pta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel18.add(pta, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel18);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Ptb (W)");
        jLabel15.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel19.add(jLabel15, java.awt.BorderLayout.WEST);

        ptb.setBackground(new java.awt.Color(255, 255, 255));
        ptb.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel19.add(ptb, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel19);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Prb (W)");
        jLabel17.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel20.add(jLabel17, java.awt.BorderLayout.WEST);

        prb.setBackground(new java.awt.Color(255, 255, 255));
        prb.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel20.add(prb, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel20);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Prc (W)");
        jLabel19.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel21.add(jLabel19, java.awt.BorderLayout.WEST);

        prc.setBackground(new java.awt.Color(255, 255, 255));
        prc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel21.add(prc, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel21);

        jPanel14.add(jPanel15, java.awt.BorderLayout.NORTH);

        jPanel22.setPreferredSize(new java.awt.Dimension(10, 50));
        jPanel22.setLayout(new java.awt.GridLayout(2, 1));

        jButton1.setBackground(new java.awt.Color(36, 113, 163));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Calcular");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel22.add(jButton1);

        lab1ExportResult1.setBackground(new java.awt.Color(36, 113, 163));
        lab1ExportResult1.setForeground(new java.awt.Color(255, 255, 255));
        lab1ExportResult1.setText("Exportar Resultados");
        lab1ExportResult1.setBorder(null);
        lab1ExportResult1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1ExportResult1ActionPerformed(evt);
            }
        });
        jPanel22.add(lab1ExportResult1);

        jPanel14.add(jPanel22, java.awt.BorderLayout.SOUTH);

        jPanel12.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eq1.png"))); // NOI18N
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(500, 372));
        jPanel4.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        jPanel9.setLayout(new java.awt.GridLayout(1, 3));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(36, 113, 163));
        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ga (dB)");
        jLabel1.setOpaque(true);
        jPanel10.add(jLabel1, java.awt.BorderLayout.NORTH);

        ga.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ga.setForeground(new java.awt.Color(0, 0, 0));
        ga.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ga.setText("---");
        jPanel10.add(ga, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jLabel3.setBackground(new java.awt.Color(36, 113, 163));
        jLabel3.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Gb (dB)");
        jLabel3.setOpaque(true);
        jPanel11.add(jLabel3, java.awt.BorderLayout.NORTH);

        gb.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        gb.setForeground(new java.awt.Color(0, 0, 0));
        gb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gb.setText("---");
        jPanel11.add(gb, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel11);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jLabel4.setBackground(new java.awt.Color(36, 113, 163));
        jLabel4.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Gc (dB)");
        jLabel4.setOpaque(true);
        jPanel13.add(jLabel4, java.awt.BorderLayout.NORTH);

        gc.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        gc.setForeground(new java.awt.Color(0, 0, 0));
        gc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gc.setText("---");
        jPanel13.add(gc, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel13);

        jPanel4.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(36, 113, 163));
        jPanel5.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        lab1Instruction.setBackground(new java.awt.Color(36, 113, 163));
        lab1Instruction.setForeground(new java.awt.Color(255, 255, 255));
        lab1Instruction.setText("Instrucciones");
        lab1Instruction.setBorder(null);
        lab1Instruction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1InstructionActionPerformed(evt);
            }
        });
        jPanel5.add(lab1Instruction);

        lab1GoNEC.setBackground(new java.awt.Color(36, 113, 163));
        lab1GoNEC.setForeground(new java.awt.Color(255, 255, 255));
        lab1GoNEC.setText("Ir al Simulador");
        lab1GoNEC.setBorder(null);
        lab1GoNEC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1GoNECActionPerformed(evt);
            }
        });
        jPanel5.add(lab1GoNEC);

        jPanel2.add(jPanel5, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Medición de la Ganancia Absoluta", jPanel2);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel24.setBackground(new java.awt.Color(36, 113, 163));
        jPanel24.setPreferredSize(new java.awt.Dimension(250, 424));
        jPanel24.setLayout(new java.awt.BorderLayout());

        jPanel25.setBackground(new java.awt.Color(36, 113, 163));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jLabel10.setBackground(new java.awt.Color(36, 113, 163));
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Variables");
        jPanel25.add(jLabel10, java.awt.BorderLayout.NORTH);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setPreferredSize(new java.awt.Dimension(10, 100));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setPreferredSize(new java.awt.Dimension(10, 150));
        jPanel27.setLayout(new java.awt.GridLayout(4, 1));

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setLayout(new java.awt.BorderLayout());

        gsivalue.setBackground(new java.awt.Color(255, 255, 255));
        gsivalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel28.add(gsivalue, java.awt.BorderLayout.CENTER);

        gsiVal.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(gsiVal);
        gsiVal.setForeground(new java.awt.Color(0, 0, 0));
        gsiVal.setSelected(true);
        gsiVal.setText("Gsi");
        gsiVal.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel28.add(gsiVal, java.awt.BorderLayout.WEST);

        jPanel27.add(jPanel28);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setLayout(new java.awt.BorderLayout());

        gsidbvalue.setBackground(new java.awt.Color(255, 255, 255));
        gsidbvalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel34.add(gsidbvalue, java.awt.BorderLayout.CENTER);

        gsidbVal.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(gsidbVal);
        gsidbVal.setForeground(new java.awt.Color(0, 0, 0));
        gsidbVal.setText("Gsi (dB)");
        gsidbVal.setPreferredSize(new java.awt.Dimension(100, 28));
        jPanel34.add(gsidbVal, java.awt.BorderLayout.WEST);

        jPanel27.add(jPanel34);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setLayout(new java.awt.BorderLayout());

        pdma.setForeground(new java.awt.Color(0, 0, 0));
        pdma.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pdma.setText("Pdma (W)");
        pdma.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel29.add(pdma, java.awt.BorderLayout.WEST);

        pdmavalue.setBackground(new java.awt.Color(255, 255, 255));
        pdmavalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel29.add(pdmavalue, java.awt.BorderLayout.CENTER);

        jPanel27.add(jPanel29);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setLayout(new java.awt.BorderLayout());

        pdms.setForeground(new java.awt.Color(0, 0, 0));
        pdms.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pdms.setText("Pdms (W)");
        pdms.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel30.add(pdms, java.awt.BorderLayout.WEST);

        pdmsvalue.setBackground(new java.awt.Color(255, 255, 255));
        pdmsvalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jPanel30.add(pdmsvalue, java.awt.BorderLayout.CENTER);

        jPanel27.add(jPanel30);

        jPanel26.add(jPanel27, java.awt.BorderLayout.NORTH);

        jPanel35.setPreferredSize(new java.awt.Dimension(10, 50));
        jPanel35.setLayout(new java.awt.GridLayout(2, 1));

        jButton2.setBackground(new java.awt.Color(36, 113, 163));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Calcular");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel35.add(jButton2);

        lab1ExportResult.setBackground(new java.awt.Color(36, 113, 163));
        lab1ExportResult.setForeground(new java.awt.Color(255, 255, 255));
        lab1ExportResult.setText("Exportar Resultados");
        lab1ExportResult.setBorder(null);
        lab1ExportResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1ExportResultActionPerformed(evt);
            }
        });
        jPanel35.add(lab1ExportResult);

        jPanel26.add(jPanel35, java.awt.BorderLayout.SOUTH);

        jPanel25.add(jPanel26, java.awt.BorderLayout.CENTER);

        jPanel24.add(jPanel25, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel24, java.awt.BorderLayout.WEST);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setLayout(new java.awt.BorderLayout());

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/eq2.png"))); // NOI18N
        jLabel5.setOpaque(true);
        jPanel36.add(jLabel5, java.awt.BorderLayout.PAGE_START);

        jPanel37.setPreferredSize(new java.awt.Dimension(50, 150));
        jPanel37.setLayout(new java.awt.GridLayout(1, 1));

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setLayout(new java.awt.BorderLayout());

        jLabel6.setBackground(new java.awt.Color(36, 113, 163));
        jLabel6.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Gai (dBi)");
        jLabel6.setOpaque(true);
        jPanel38.add(jLabel6, java.awt.BorderLayout.NORTH);

        gai.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        gai.setForeground(new java.awt.Color(0, 0, 0));
        gai.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gai.setText("---");
        jPanel38.add(gai, java.awt.BorderLayout.CENTER);

        jPanel37.add(jPanel38);

        jPanel36.add(jPanel37, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel36, java.awt.BorderLayout.CENTER);

        jPanel23.setBackground(new java.awt.Color(36, 113, 163));
        jPanel23.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel23.setLayout(new java.awt.GridLayout());

        lab1Instruction1.setBackground(new java.awt.Color(36, 113, 163));
        lab1Instruction1.setForeground(new java.awt.Color(255, 255, 255));
        lab1Instruction1.setText("Instrucciones");
        lab1Instruction1.setBorder(null);
        lab1Instruction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1Instruction1ActionPerformed(evt);
            }
        });
        jPanel23.add(lab1Instruction1);

        lab1GoNEC1.setBackground(new java.awt.Color(36, 113, 163));
        lab1GoNEC1.setForeground(new java.awt.Color(255, 255, 255));
        lab1GoNEC1.setText("Ir al Simulador");
        lab1GoNEC1.setBorder(null);
        lab1GoNEC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1GoNEC1ActionPerformed(evt);
            }
        });
        jPanel23.add(lab1GoNEC1);

        jPanel3.add(jPanel23, java.awt.BorderLayout.NORTH);

        jTabbedPane1.addTab("Medición de la Ganancia por Comparación", jPanel3);

        jPanel41.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel31.add(jPanel41);

        jPanel6.add(jPanel31, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(36, 113, 163));
        jPanel7.setPreferredSize(new java.awt.Dimension(498, 25));
        jPanel7.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(jPanel7, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
//Comportamiento del botón Exportar Resultados del experimento1
    private void lab1ExportResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1ExportResultActionPerformed
        global.exportResult(exportLab42(), Global.LAB42);
    }//GEN-LAST:event_lab1ExportResultActionPerformed
//Comportamiento del botón Ir al Simulador del experimento1
    private void lab1GoNECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1GoNECActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab1GoNECActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (validateLab411()) {
            double r = Double.valueOf(rvalue.getText().replace(",", "."));
            double lambda = Double.valueOf(lvalue.getText().replace(",", "."));
            double ptaVal = Double.valueOf(pta.getText().replace(",", "."));
            double ptbVal = Double.valueOf(ptb.getText().replace(",", "."));
            double prbVal = Double.valueOf(prb.getText().replace(",", "."));
            double prcVal = Double.valueOf(prc.getText().replace(",", "."));

            double alfa = 20 * Math.log10(4 * Math.PI / lambda) + 10 * Math.log10(prbVal / ptaVal);
            double beta = 20 * Math.log10(4 * Math.PI / lambda) + 10 * Math.log10(prcVal / ptaVal);
            double gamma = 20 * Math.log10(4 * Math.PI / lambda) + 10 * Math.log10(prcVal / ptbVal);

            double gcVal = (beta - alfa + gamma) / 2;
            double gbVal = gamma - ((beta - alfa + gamma) / 2);
            double gaVal = alfa - gbVal;

            ga.setText(Global.decimalFormat(gaVal) + "");
            gb.setText(Global.decimalFormat(gbVal) + "");
            gc.setText(Global.decimalFormat(gcVal) + "");

        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_jButton1ActionPerformed
//Comportamiento del botón Calcular del experimento 2
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        double gsi = 0;
        double pdmav = 0;
        double pdmsv = 0;
        double gaiv = 0;
        if (gsiVal.isSelected()) {
            if (!gsivalue.getText().isEmpty() && !pdma.getText().isEmpty() && !pdms.getText().isEmpty()) {
                gsi = Double.valueOf(gsivalue.getText().replace(",", "."));
                pdmav = Double.valueOf(pdma.getText().replace(",", "."));
                pdmsv = Double.valueOf(pdms.getText().replace(",", "."));
                gaiv = 10 * Math.log10(gsi) + 10 * Math.log10(pdmav / pdmsv);
                gai.setText(Global.decimalFormat(gaiv) + "");
            }
        } else {
            gsi = Double.valueOf(gsidbvalue.getText().replace(",", "."));
            pdmav = Double.valueOf(pdma.getText().replace(",", "."));
            pdmsv = Double.valueOf(pdms.getText().replace(",", "."));
            gaiv = gsi + 10 * Math.log10(pdmav / pdmsv);
            gai.setText(Global.decimalFormat(gaiv) + "");
        }
    }//GEN-LAST:event_jButton2ActionPerformed
//Comportamiento del botón Exportar Resultados del experimento 2
    private void lab1ExportResult1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1ExportResult1ActionPerformed
        global.exportResult(exportLab41(), Global.LAB41);
    }//GEN-LAST:event_lab1ExportResult1ActionPerformed
//Comportamiento del botón Instrucciones del experimento1
    private void lab1InstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1InstructionActionPerformed
       PDFReader.loadPdf("instruccioneslab41.pdf");
    }//GEN-LAST:event_lab1InstructionActionPerformed
//Comportamiento del botón Instrucciones del experimento2
    private void lab1Instruction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1Instruction1ActionPerformed
      PDFReader.loadPdf("instruccioneslab42.pdf");
    }//GEN-LAST:event_lab1Instruction1ActionPerformed
//Comportamiento del botón Ir al Simulador del experimento 2
    private void lab1GoNEC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1GoNEC1ActionPerformed
     MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab1GoNEC1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel ga;
    private javax.swing.JLabel gai;
    private javax.swing.JLabel gb;
    private javax.swing.JLabel gc;
    private javax.swing.JRadioButton gsiVal;
    private javax.swing.JRadioButton gsidbVal;
    private javax.swing.JFormattedTextField gsidbvalue;
    private javax.swing.JFormattedTextField gsivalue;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
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
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton lab1ExportResult;
    private javax.swing.JButton lab1ExportResult1;
    private javax.swing.JButton lab1GoNEC;
    private javax.swing.JButton lab1GoNEC1;
    private javax.swing.JButton lab1Instruction;
    private javax.swing.JButton lab1Instruction1;
    private javax.swing.JFormattedTextField lvalue;
    private javax.swing.JLabel pdma;
    private javax.swing.JFormattedTextField pdmavalue;
    private javax.swing.JLabel pdms;
    private javax.swing.JFormattedTextField pdmsvalue;
    private javax.swing.JFormattedTextField prb;
    private javax.swing.JFormattedTextField prc;
    private javax.swing.JFormattedTextField pta;
    private javax.swing.JFormattedTextField ptb;
    private javax.swing.JFormattedTextField rvalue;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the step
     */
    public int getStep() {
        return step;
    }

    /**
     * @param step the step to set
     */
    public void setStep(int step) {
        this.step = step;
    }

    /**
     * @return the originalWire
     */
    public ArrayList<Wire> getOriginalWire() {
        return originalWire;
    }

    /**
     * @param originalWire the originalWire to set
     */
    public void setOriginalWire(ArrayList<Wire> originalWire) {
        this.originalWire = originalWire;
    }

    /**
     * @return the localPolarData
     */
    public ArrayList<PolarData> getLocalPolarData() {
        return localPolarData;
    }

    /**
     * @param localPolarData the localPolarData to set
     */
    public void setLocalPolarData(ArrayList<PolarData> localPolarData) {
        this.localPolarData = localPolarData;
    }
}
