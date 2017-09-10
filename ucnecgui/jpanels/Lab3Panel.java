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
import controllers.NECParser;
import controllers.PDFReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import ucnecgui.Global;
import ucnecgui.MetaGlobal;
import static ucnecgui.jpanels.NECModulePanel.generateInputFile;
import static ucnecgui.jpanels.NECModulePanel.generateOutputData;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.Lab3_1;
import ucnecgui.models.Line;
import ucnecgui.models.PolarData;
import ucnecgui.models.RPLine;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */

public class Lab3Panel extends javax.swing.JPanel {

    private Global global;
    private DefaultTableModel model;
    private DefaultTableModel model1;
    private int step = 0;
    private ArrayList<Wire> originalWire;
    private ArrayList<Lab3_1> localE;
    private ArrayList<Lab3_1> localH;

    /**
     *Constructor de la clase Lab3Panel
     * @param global Objeto de la clase Global
     */
    public Lab3Panel(Global global) {
        initComponents();
        this.global = global;
        loadWires(global);
        initTable(global);
        localE = new ArrayList<Lab3_1>();
        localH = new ArrayList<Lab3_1>();
    }

    /**
     *Inicializa los componentes del panel
     * @param global Objeto de la clase Global
     */
    public void initTable(Global global) {
        String columnName[] = {"Ángulo", "||Vi||"};
        String columnName1[] = {"Ángulo", "||Vi||"};

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
        resultTableE.setModel(model);

        model1 = new DefaultTableModel() {

            @Override
            public int getColumnCount() {
                return columnName1.length;
            }

            @Override
            public String getColumnName(int index) {
                return columnName1[index];
            }

            @Override
            public boolean isCellEditable(int row,
                    int column) {
                return false;
            }

        };
        resultTableH.setModel(model1);
    }

    /**
     * Cargas los alambres correspondientes a las antenas 1 y 2 a partir de los
     * segmentos indicados en los respectivos JComboBox, excluyendo al alambre
     * auxiliar de corriente, si lo hubiese
     *
     * @param global Objeto de la clase Global
     */
    public void loadWires(Global global) {
        ant1From.removeAllItems();
        ant1To.removeAllItems();
        ant2From.removeAllItems();
        ant2To.removeAllItems();
        sourceList.removeAllItems();
       
        for (int i = 0; i < global.getgWires().size(); i++) {
            if (i == global.getCurrentSourceTag() - 1) {
                continue;
            }
            ant1From.addItem(global.getgWires().get(i).getNumber() + "");
            ant1To.addItem(global.getgWires().get(i).getNumber() + "");
            ant2From.addItem(global.getgWires().get(i).getNumber() + "");
            ant2To.addItem(global.getgWires().get(i).getNumber() + "");
        }
        for (int i = 0; i < global.getgSource().size(); i++) {
            sourceList.addItem((i + 1) + "");
        }
        ant1From.revalidate();
        ant1To.revalidate();
        ant2From.revalidate();
        ant2To.revalidate();
    }

    /**
     * Rota los alambres correspondientes a la antena cuyos alambres tienen
     * índices comprendidos entre from y to
     *
     * @param global Objeto de la clase Global
     * @param showAnt Indica si se mostrará la gráfica de la antena en cada
     * simulación
     * @param ant2Wires Alambres correspondientes a la antena a rotar
     * @param angle Ángulo de rotación (Grados)
     * @param plane Eje de rotación (X,Y o Z)
     */
    public void rotate(Global global, boolean showAnt, ArrayList<Integer> ant2Wires, double angle, int plane) {

        ArrayList<Integer> sWires = ant2Wires;
        double ang = 0;

        if (sWires.size() > 0) {
            if (plane == Global.XAXIS) {
                if (isAntiClockWise.isSelected()) {
                    ang = angle;
                } else {
                    ang = 360 - angle;
                }

                for (int i = 0; i < sWires.size(); i++) {

                    Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                    Line line = new Line(nWire).rotateLine(Global.XAXIS, ang);
                    nWire.setX1(line.getX1());
                    nWire.setY1(line.getY1());
                    nWire.setZ1(line.getZ1());
                    nWire.setX2(line.getX2());
                    nWire.setY2(line.getY2());
                    nWire.setZ2(line.getZ2());
                }

            } else if (plane == Global.YAXIS) {

                if (isAntiClockWise.isSelected()) {
                    ang = angle;
                } else {
                    ang = 360 - angle;
                }

                for (int i = 0; i < sWires.size(); i++) {

                    Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                    Line line = new Line(nWire).rotateLine(Global.YAXIS, ang);
                    nWire.setX1(line.getX1());
                    nWire.setY1(line.getY1());
                    nWire.setZ1(line.getZ1());
                    nWire.setX2(line.getX2());
                    nWire.setY2(line.getY2());
                    nWire.setZ2(line.getZ2());
                }

            } else {

                if (isAntiClockWise.isSelected()) {
                    ang = angle;
                } else {
                    ang = 360 - angle;
                }

                for (int i = 0; i < sWires.size(); i++) {

                    Wire nWire = global.getgWires().get(sWires.get(i) - 1);
                    Line line = new Line(nWire).rotateLine(Global.ZAXIS, ang);
                    nWire.setX1(line.getX1());
                    nWire.setY1(line.getY1());
                    nWire.setZ1(line.getZ1());
                    nWire.setX2(line.getX2());
                    nWire.setY2(line.getY2());
                    nWire.setZ2(line.getZ2());
                }
            }
            if (showAnt) {
                global.updatePlot(global);
            }
        }
    }

    /**
     * Exporta los resultados del experimento 1 a un archivo de texto
     *
     * @return Arreglo de objetos String con los resultados del experimento 1
     */
    public ArrayList<String> exportLab31() {
        ArrayList<String> resp = new ArrayList<String>();
        resp.add("Ángulo, ||Vi||");
        for (Lab3_1 lab31 : global.getgLab3_1()) {
            //   resp.add(lab31.getAngle() + "," + lab31.getV().abs());
        }
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
            } else {
                generateInputFile(true, global);
                isOutputGenerated = generateOutputData();
                output.put("PLOT3D", NECParser.getRP3D(global.readOutputData(isOutputGenerated), global));
                generateInputFile(false, global);
                isOutputGenerated = generateOutputData();
                output.put("PLOT2D", NECParser.getRP3D(global.readOutputData(isOutputGenerated), global));
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lab3Instruction = new javax.swing.JButton();
        lab3GoNEC = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ant1From = new javax.swing.JComboBox<>();
        jPanel20 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ant1To = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        ant2From = new javax.swing.JComboBox<>();
        jPanel24 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        ant2To = new javax.swing.JComboBox<>();
        jPanel21 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        angleValue = new javax.swing.JFormattedTextField();
        unit2 = new javax.swing.JLabel();
        unit3 = new javax.swing.JLabel();
        isAntiClockWise = new javax.swing.JCheckBox();
        jPanel28 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        planeX = new javax.swing.JRadioButton();
        planeY = new javax.swing.JRadioButton();
        planeZ = new javax.swing.JRadioButton();
        jPanel17 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        planeE = new javax.swing.JRadioButton();
        planeH = new javax.swing.JRadioButton();
        jLabel20 = new javax.swing.JLabel();
        plotSelector = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        sourceList = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        sourceTypelbl = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        plotTick = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        plotColor = new javax.swing.JComboBox<>();
        jPanel26 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        showAnt = new javax.swing.JCheckBox();
        jPanel27 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        showRP = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultTableE = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTableH = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        lab3AddResult = new javax.swing.JButton();
        lab3DeleteResult = new javax.swing.JButton();
        lab3ExportResult = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(36, 113, 163));
        jPanel5.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel5.setLayout(new java.awt.GridLayout(1, 0));

        lab3Instruction.setBackground(new java.awt.Color(36, 113, 163));
        lab3Instruction.setForeground(new java.awt.Color(255, 255, 255));
        lab3Instruction.setText("Instrucciones");
        lab3Instruction.setBorder(null);
        lab3Instruction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab3InstructionActionPerformed(evt);
            }
        });
        jPanel5.add(lab3Instruction);

        lab3GoNEC.setBackground(new java.awt.Color(36, 113, 163));
        lab3GoNEC.setForeground(new java.awt.Color(255, 255, 255));
        lab3GoNEC.setText("Ir al Simulador");
        lab3GoNEC.setBorder(null);
        lab3GoNEC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab3GoNECActionPerformed(evt);
            }
        });
        jPanel5.add(lab3GoNEC);

        jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel8.setPreferredSize(new java.awt.Dimension(250, 424));
        jPanel8.setLayout(new java.awt.GridLayout(5, 1));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setForeground(new java.awt.Color(0, 0, 0));
        jPanel15.setLayout(new java.awt.BorderLayout());

        jPanel18.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel18.setLayout(new java.awt.GridLayout(6, 1));

        jLabel7.setBackground(new java.awt.Color(36, 113, 163));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Antena Bajo Prueba");
        jLabel7.setOpaque(true);
        jPanel18.add(jLabel7);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setLayout(new java.awt.BorderLayout());

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Desde alambre");
        jLabel2.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel19.add(jLabel2, java.awt.BorderLayout.LINE_START);

        ant1From.setBackground(new java.awt.Color(255, 255, 255));
        ant1From.setForeground(new java.awt.Color(0, 0, 0));
        ant1From.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ant1From.setBorder(null);
        jPanel19.add(ant1From, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel19);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setLayout(new java.awt.BorderLayout());

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Hasta alambre");
        jLabel3.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel20.add(jLabel3, java.awt.BorderLayout.LINE_START);

        ant1To.setBackground(new java.awt.Color(255, 255, 255));
        ant1To.setForeground(new java.awt.Color(0, 0, 0));
        ant1To.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ant1To.setBorder(null);
        jPanel20.add(ant1To, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel20);

        jLabel9.setBackground(new java.awt.Color(36, 113, 163));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Antena Sensora");
        jLabel9.setOpaque(true);
        jPanel18.add(jLabel9);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setLayout(new java.awt.BorderLayout());

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Desde alambre");
        jLabel5.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel23.add(jLabel5, java.awt.BorderLayout.LINE_START);

        ant2From.setBackground(new java.awt.Color(255, 255, 255));
        ant2From.setForeground(new java.awt.Color(0, 0, 0));
        ant2From.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ant2From.setBorder(null);
        jPanel23.add(ant2From, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel23);

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setLayout(new java.awt.BorderLayout());

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Hasta alambre");
        jLabel6.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel24.add(jLabel6, java.awt.BorderLayout.LINE_START);

        ant2To.setBackground(new java.awt.Color(255, 255, 255));
        ant2To.setForeground(new java.awt.Color(0, 0, 0));
        ant2To.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ant2To.setBorder(null);
        jPanel24.add(ant2To, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel24);

        jPanel15.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel15);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setForeground(new java.awt.Color(0, 0, 0));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel12.setLayout(new java.awt.GridLayout(3, 1));

        jLabel13.setBackground(new java.awt.Color(36, 113, 163));
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Rotación");
        jLabel13.setOpaque(true);
        jPanel12.add(jLabel13);

        jPanel13.setLayout(new java.awt.BorderLayout());

        angleValue.setBackground(new java.awt.Color(255, 255, 255));
        angleValue.setForeground(new java.awt.Color(0, 0, 0));
        angleValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        angleValue.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel13.add(angleValue, java.awt.BorderLayout.CENTER);

        unit2.setBackground(new java.awt.Color(255, 255, 255));
        unit2.setForeground(new java.awt.Color(0, 0, 0));
        unit2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        unit2.setText("Grados");
        unit2.setOpaque(true);
        unit2.setPreferredSize(new java.awt.Dimension(60, 16));
        jPanel13.add(unit2, java.awt.BorderLayout.EAST);

        unit3.setBackground(new java.awt.Color(255, 255, 255));
        unit3.setForeground(new java.awt.Color(0, 0, 0));
        unit3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        unit3.setText("Paso");
        unit3.setOpaque(true);
        unit3.setPreferredSize(new java.awt.Dimension(50, 16));
        jPanel13.add(unit3, java.awt.BorderLayout.WEST);

        jPanel12.add(jPanel13);

        isAntiClockWise.setBackground(new java.awt.Color(255, 255, 255));
        isAntiClockWise.setForeground(new java.awt.Color(0, 0, 0));
        isAntiClockWise.setText("Sentido Antihorario");
        jPanel12.add(isAntiClockWise);

        jPanel21.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setLayout(new java.awt.BorderLayout());

        jLabel14.setBackground(new java.awt.Color(36, 113, 163));
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Eje de Rotación");
        jLabel14.setOpaque(true);
        jPanel28.add(jLabel14, java.awt.BorderLayout.PAGE_START);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setLayout(new java.awt.GridLayout(1, 3));

        planeX.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(planeX);
        planeX.setForeground(new java.awt.Color(0, 0, 0));
        planeX.setText("X");
        jPanel29.add(planeX);

        planeY.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(planeY);
        planeY.setForeground(new java.awt.Color(0, 0, 0));
        planeY.setText("Y");
        planeY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planeYActionPerformed(evt);
            }
        });
        jPanel29.add(planeY);

        planeZ.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(planeZ);
        planeZ.setForeground(new java.awt.Color(0, 0, 0));
        planeZ.setText("Z");
        jPanel29.add(planeZ);

        jPanel28.add(jPanel29, java.awt.BorderLayout.CENTER);

        jPanel21.add(jPanel28, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel21);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setForeground(new java.awt.Color(0, 0, 0));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(4, 1));

        jLabel11.setBackground(new java.awt.Color(36, 113, 163));
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Plano");
        jLabel11.setOpaque(true);
        jPanel2.add(jLabel11);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setLayout(new java.awt.GridLayout(1, 2));

        planeE.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup3.add(planeE);
        planeE.setForeground(new java.awt.Color(0, 0, 0));
        planeE.setSelected(true);
        planeE.setText("E");
        jPanel30.add(planeE);

        planeH.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup3.add(planeH);
        planeH.setForeground(new java.awt.Color(0, 0, 0));
        planeH.setText("H");
        planeH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planeHActionPerformed(evt);
            }
        });
        jPanel30.add(planeH);

        jPanel3.add(jPanel30, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel3);

        jLabel20.setBackground(new java.awt.Color(36, 113, 163));
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Tipo deGrafica");
        jLabel20.setOpaque(true);
        jPanel2.add(jLabel20);

        plotSelector.setBackground(new java.awt.Color(255, 255, 255));
        plotSelector.setForeground(new java.awt.Color(0, 0, 0));
        plotSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "|V| (veces)", "|Vnorm| (veces)", "Bidimensional" }));
        plotSelector.setBorder(null);
        plotSelector.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                plotSelectorItemStateChanged(evt);
            }
        });
        jPanel2.add(plotSelector);

        jPanel17.add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel8.add(jPanel17);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel10.setLayout(new java.awt.GridLayout(3, 1));

        jLabel12.setBackground(new java.awt.Color(36, 113, 163));
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Fuente a Sensar");
        jLabel12.setOpaque(true);
        jPanel10.add(jLabel12);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Número de Fuente");
        jLabel10.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel9.add(jLabel10, java.awt.BorderLayout.LINE_START);

        sourceList.setBackground(new java.awt.Color(255, 255, 255));
        sourceList.setForeground(new java.awt.Color(0, 0, 0));
        sourceList.setBorder(null);
        sourceList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                sourceListItemStateChanged(evt);
            }
        });
        jPanel9.add(sourceList, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel9);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setLayout(new java.awt.BorderLayout());

        sourceTypelbl.setBackground(new java.awt.Color(255, 255, 255));
        sourceTypelbl.setForeground(new java.awt.Color(0, 0, 0));
        sourceTypelbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sourceTypelbl.setText("---");
        jPanel11.add(sourceTypelbl, java.awt.BorderLayout.CENTER);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Tipo de Fuente");
        jLabel4.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel11.add(jLabel4, java.awt.BorderLayout.WEST);

        jPanel10.add(jPanel11);

        jPanel4.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel4);

        jPanel16.setLayout(new java.awt.GridLayout(5, 1));

        jLabel15.setBackground(new java.awt.Color(36, 113, 163));
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Opciones de Simulación");
        jLabel15.setOpaque(true);
        jPanel16.add(jLabel15);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Paso de Ángulo Polar");
        jLabel16.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel22.add(jLabel16, java.awt.BorderLayout.LINE_START);

        plotTick.setBackground(new java.awt.Color(255, 255, 255));
        plotTick.setForeground(new java.awt.Color(0, 0, 0));
        plotTick.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "18", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45" }));
        plotTick.setSelectedIndex(4);
        plotTick.setBorder(null);
        jPanel22.add(plotTick, java.awt.BorderLayout.CENTER);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Grados");
        jPanel22.add(jLabel1, java.awt.BorderLayout.EAST);

        jPanel16.add(jPanel22);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setLayout(new java.awt.BorderLayout());

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Color de Gráfica");
        jLabel17.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel25.add(jLabel17, java.awt.BorderLayout.WEST);

        plotColor.setBackground(new java.awt.Color(255, 255, 255));
        plotColor.setForeground(new java.awt.Color(0, 0, 0));
        plotColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ROJO", "AMARILLO", "CYAN", "VERDE", "AZUL", "NEGRO" }));
        plotColor.setSelectedIndex(4);
        plotColor.setBorder(null);
        jPanel25.add(plotColor, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel25);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Mostrar Antena");
        jLabel18.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel26.add(jLabel18, java.awt.BorderLayout.WEST);

        showAnt.setBackground(new java.awt.Color(255, 255, 255));
        showAnt.setForeground(new java.awt.Color(0, 0, 0));
        showAnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel26.add(showAnt, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel26);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setLayout(new java.awt.BorderLayout());

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Mostrar Patrón de Radiación");
        jLabel19.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel27.add(jLabel19, java.awt.BorderLayout.WEST);

        showRP.setBackground(new java.awt.Color(255, 255, 255));
        showRP.setForeground(new java.awt.Color(0, 0, 0));
        showRP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel27.add(showRP, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel27);

        jPanel8.add(jPanel16);

        jPanel6.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.GridLayout(1, 2));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        resultTableE.setBackground(new java.awt.Color(255, 255, 255));
        resultTableE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        resultTableE.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(resultTableE);

        jPanel14.add(jScrollPane2);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        resultTableH.setBackground(new java.awt.Color(255, 255, 255));
        resultTableH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        resultTableH.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(resultTableH);

        jPanel14.add(jScrollPane1);

        jPanel6.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(36, 113, 163));
        jPanel7.setPreferredSize(new java.awt.Dimension(498, 25));
        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        lab3AddResult.setBackground(new java.awt.Color(36, 113, 163));
        lab3AddResult.setForeground(new java.awt.Color(255, 255, 255));
        lab3AddResult.setText("Agregar Resultado");
        lab3AddResult.setBorder(null);
        lab3AddResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab3AddResultActionPerformed(evt);
            }
        });
        jPanel7.add(lab3AddResult);

        lab3DeleteResult.setBackground(new java.awt.Color(36, 113, 163));
        lab3DeleteResult.setForeground(new java.awt.Color(255, 255, 255));
        lab3DeleteResult.setText("Eliminar Resultado");
        lab3DeleteResult.setBorder(null);
        lab3DeleteResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab3DeleteResultActionPerformed(evt);
            }
        });
        jPanel7.add(lab3DeleteResult);

        lab3ExportResult.setBackground(new java.awt.Color(36, 113, 163));
        lab3ExportResult.setForeground(new java.awt.Color(255, 255, 255));
        lab3ExportResult.setText("Exportar Resultados");
        lab3ExportResult.setBorder(null);
        lab3ExportResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab3ExportResultActionPerformed(evt);
            }
        });
        jPanel7.add(lab3ExportResult);

        jPanel1.add(jPanel7, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
//Comportamiento del botón Exportar Resultados del experimento 1
    private void lab3ExportResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab3ExportResultActionPerformed
        global.exportResult(exportLab31(), Global.LAB31);
    }//GEN-LAST:event_lab3ExportResultActionPerformed
//Comportamiento del botón Eliminar Resultado del experimento 1
    private void lab3DeleteResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab3DeleteResultActionPerformed
        int selected = resultTableH.getSelectedRow();
        if (selected != -1) {
            model.removeRow(selected);
            global.getgLab2_1().remove(selected);
            setStep(getStep() - 1);
            model.fireTableDataChanged();
        } else {
            global.errorMessage("Messages.noResultSelectedTitle", "Messages.noResultSelected");
        }
    }//GEN-LAST:event_lab3DeleteResultActionPerformed
//Comportamiento del botón Ir al Simulador del experimento 1
    private void lab3GoNECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab3GoNECActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab3GoNECActionPerformed

    private void sourceListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sourceListItemStateChanged
        int sourceId = -1;
        if (Global.isNumeric(sourceList.getSelectedItem() + "")) {
            sourceId = Integer.valueOf(sourceList.getSelectedItem() + "");
            if (global.getRelatedNetwork(sourceId) == -1) {
                sourceTypelbl.setText("V");
            } else {
                sourceTypelbl.setText("I");
            }
        }
    }//GEN-LAST:event_sourceListItemStateChanged

    private void planeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planeYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_planeYActionPerformed

    private void planeHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planeHActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_planeHActionPerformed
//Comportamiento del botón Añadir Resultados del experimento 1
    private void lab3AddResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab3AddResultActionPerformed
        double a = 0;
        boolean isShowRP = showRP.isSelected();
        boolean isShowAnt = showAnt.isSelected();
        if (global.getgWires().size() > 0 && !angleValue.getText().isEmpty()) {
            if (Double.valueOf(angleValue.getText()) > 0) {
                ArrayList<Integer> ant1 = new ArrayList<Integer>();
                ArrayList<Integer> ant2 = new ArrayList<Integer>();

                int a1From = Integer.valueOf(ant1From.getSelectedItem() + "");
                int a1To = Integer.valueOf(ant1To.getSelectedItem() + "");
                int a2From = Integer.valueOf(ant2From.getSelectedItem() + "");
                int a2To = Integer.valueOf(ant2To.getSelectedItem() + "");
                int sourceIndx = Integer.valueOf(sourceList.getSelectedItem() + "");

                a = Double.valueOf(angleValue.getText().replace(",", ".")) * (getStep() + 1);
                setStep(getStep() + 1);

                if ((a1From <= a1To) && (a2From <= a2To)) {
                    for (int i = a1From; i <= a1To; i++) {
                        ant1.add(i);
                    }
                    for (int i = a2From; i <= a2To; i++) {
                        ant2.add(i);
                    }
                } else {
                    global.errorValidateInput();
                }

                if (getOriginalWire() == null) {
                    setOriginalWire(new ArrayList<Wire>());
                    ArrayList<Wire> oWires = new ArrayList<Wire>();
                    for (int wireIndex : ant2) {
                        originalWire.add(global.getgWires().get(wireIndex - 1));
                    }
                }

                int plane = 0;
                if (planeX.isSelected()) {
                    plane = Global.XAXIS;
                } else if (planeY.isSelected()) {
                    plane = Global.YAXIS;
                } else {
                    plane = Global.ZAXIS;
                }

                int emPlane = 0;
                if (planeE.isSelected()) {
                    emPlane = Lab3_1.E;
                } else {
                    emPlane = Lab3_1.H;
                }
                double ang = Double.valueOf(angleValue.getText() + "");
                rotate(global, isShowAnt, ant2, ang, plane);
                runSimulation(false);

                Lab3_1 lab31 = new Lab3_1();
                lab31.setAngle(a);
                lab31.setPlane(emPlane);
                double freq = global.getgFrequency().getFreq();
                double vSrc1Re = (((global.getgSrcInfo().get(sourceIndx)).get(freq)).get(0)).getVoltageReal();
                double vSrc1Im = (((global.getgSrcInfo().get(sourceIndx)).get(freq)).get(0)).getVoltageImaginary();
                Complex v = new Complex(vSrc1Re, vSrc1Im);
                lab31.setVmodule(v.abs());
                if (planeE.isSelected()) {
                    localE.add(lab31);
                } else {
                    localH.add(lab31);
                }

                Object[] row = {lab31.getAngle() + "",
                    lab31.getVmodule() + "" };
                if (lab31.getPlane() == Lab3_1.E) {
                    model.addRow(row);
                    model.fireTableDataChanged();
                } else {
                    model1.addRow(row);
                    model1.fireTableDataChanged();
                }

                if (a >= 360) {
                    if (localE.size() >= 8 || localH.size() >= 8) {
                        int selectedColor = Integer.valueOf(plotColor.getSelectedIndex() + "");
                        double plotTickAmount = Double.valueOf(plotTick.getSelectedItem() + "");
                        ArrayList<PolarData> pd = new ArrayList<PolarData>();
                        if (planeE.isSelected()) {
                            for (Lab3_1 nlab31 : localE) {
                                PolarData nPd = new PolarData(nlab31.getVmodule(), nlab31.getAngle());
                                pd.add(nPd);
                            }
                        } else {
                            for (Lab3_1 nlab31 : localH) {
                                PolarData nPd = new PolarData(nlab31.getVmodule(), nlab31.getAngle());
                                pd.add(nPd);
                            }
                        }

                        boolean first = true;
                        double maxVal = 0;
                        for (PolarData polarData : pd) {
                            if (first) {
                                maxVal = polarData.getR();
                                first = false;
                            } else if (maxVal < polarData.getR()) {
                                maxVal = polarData.getR();
                            }
                        }
                        ArrayList<PolarData> resp = new ArrayList<PolarData>();
                        ArrayList<PolarData> vn = new ArrayList<PolarData>();

                        if (planeE.isSelected()) {
                            for (Lab3_1 nLab31 : localE) {
                                nLab31.setFdb(20 * Math.log10(nLab31.getVmodule() / maxVal));
                                nLab31.setFtimes(nLab31.getVmodule() / maxVal);
                                PolarData nPd = new PolarData(nLab31.getFtimes(), nLab31.getAngle());
                                PolarData mPd = new PolarData(nLab31.getVmodule(), nLab31.getAngle());
                                resp.add(nPd);
                                vn.add(mPd);
                            }
                        } else {
                            for (Lab3_1 nLab31 : localH) {
                                nLab31.setFdb(20 * Math.log10(nLab31.getVmodule() / maxVal));
                                nLab31.setFtimes(nLab31.getVmodule() / maxVal);
                                PolarData nPd = new PolarData(nLab31.getFtimes(), nLab31.getAngle());
                                PolarData mPd = new PolarData(nLab31.getVmodule(), nLab31.getAngle());
                                resp.add(nPd);
                                vn.add(mPd);
                            }
                        }

                        switch (plotSelector.getSelectedIndex()) {
                            case 0: //Module
                                global.setCurrentPlotType(Global.RPMODULE);
                                global.executePolarization(vn, plotTickAmount, global);
                                break;
                            case 1://Normalized
                                global.setCurrentPlotType(Global.RPNORMALIZED);
                                global.executePolarization(resp, plotTickAmount, global);
                                break;
                            case 2://Bidimensional
                                global.setCurrentPlotType(Global.RPBIDIMENSIONAL);
                                SWRPlot.execute("Diagrama de Polarización", vn, global);
                                break;
                            default:
                                throw new AssertionError();
                        }

                        a = 0;
                        setStep(0);
                        if (planeE.isSelected()) {
                            planeE.setSelected(false);
                            planeH.setSelected(true);
                        } else {
                            planeE.setSelected(true);
                            planeH.setSelected(false);
                        }
                    }
                }
            } else {
                global.errorValidateInput();
            }
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_lab3AddResultActionPerformed

    private void plotSelectorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_plotSelectorItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_plotSelectorItemStateChanged
//Comportamiento del botón Instrucciones del experimento 1
    private void lab3InstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab3InstructionActionPerformed
      PDFReader.loadPdf("instruccioneslab31.pdf");
    }//GEN-LAST:event_lab3InstructionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField angleValue;
    private javax.swing.JComboBox<String> ant1From;
    private javax.swing.JComboBox<String> ant1To;
    private javax.swing.JComboBox<String> ant2From;
    private javax.swing.JComboBox<String> ant2To;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JCheckBox isAntiClockWise;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton lab3AddResult;
    private javax.swing.JButton lab3DeleteResult;
    private javax.swing.JButton lab3ExportResult;
    private javax.swing.JButton lab3GoNEC;
    private javax.swing.JButton lab3Instruction;
    private javax.swing.JRadioButton planeE;
    private javax.swing.JRadioButton planeH;
    private javax.swing.JRadioButton planeX;
    private javax.swing.JRadioButton planeY;
    private javax.swing.JRadioButton planeZ;
    private javax.swing.JComboBox<String> plotColor;
    private javax.swing.JComboBox<String> plotSelector;
    private javax.swing.JComboBox<String> plotTick;
    private javax.swing.JTable resultTableE;
    private javax.swing.JTable resultTableH;
    private javax.swing.JCheckBox showAnt;
    private javax.swing.JCheckBox showRP;
    private javax.swing.JComboBox<String> sourceList;
    private javax.swing.JLabel sourceTypelbl;
    private javax.swing.JLabel unit2;
    private javax.swing.JLabel unit3;
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
     * @return the localE
     */
    public ArrayList<Lab3_1> getLocalE() {
        return localE;
    }

    /**
     * @param localE the localE to set
     */
    public void setLocalE(ArrayList<Lab3_1> localE) {
        this.localE = localE;
    }

    /**
     * @return the localH
     */
    public ArrayList<Lab3_1> getLocalH() {
        return localH;
    }

    /**
     * @param localH the localH to set
     */
    public void setLocalH(ArrayList<Lab3_1> localH) {
        this.localH = localH;
    }
}
