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
import javax.swing.table.DefaultTableModel;
import ucnecgui.Global;
import ucnecgui.MetaGlobal;
import static ucnecgui.jpanels.NECModulePanel.generateInputFile;
import static ucnecgui.jpanels.NECModulePanel.generateOutputData;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.Lab2_1;
import ucnecgui.models.Line;
import ucnecgui.models.PolarData;
import ucnecgui.models.Polarization;
import ucnecgui.models.RPLine;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class Lab2Panel extends javax.swing.JPanel {

    private Global global;
    private DefaultTableModel model;
    private int step = 0;
    private ArrayList<Wire> originalWire;
    private ArrayList<PolarData> localPolarData;

    /**
     * Constructor de la clase Lab2Panel
     *
     * @param global Objeto de la clase Global
     */
    public Lab2Panel(Global global) {
        initComponents();
        this.global = global;
        loadWires(global);
        initTable(global);
    }

    /**
     * Inicializa los componentes del panel
     *
     * @param global Objeto de la clase Global
     */
    public void initTable(Global global) {
        String columnName[] = {"Ángulo en Grados", "Magnitud de Voltaje ||V||"};

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
        resultTable.setModel(model);
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
     * @param from Índice del alambre desde donde se realizará la rotación
     * @param to Índice del alambre hasta donde se realizará la rotación
     * @param angle Ángulo de rotación (Grados)
     * @param plane Eje de rotación (X,Y o Z)
     *
     */
    public void rotate(Global global, boolean showAnt, int from, int to, double angle, int plane) {

        ArrayList<Integer> sWires = new ArrayList<Integer>();
        for (int i = from; i <= to; i++) {
            sWires.add(i);
        }

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
    public ArrayList<String> exportLab21() {
        ArrayList<String> resp = new ArrayList<String>();
        resp.add("Ángulo en Grados,Magnitud de Voltaje ||V||");
        for (Lab2_1 lab21 : global.getgLab2_1()) {
            resp.add(lab21.getAngle() + "," + lab21.getV().abs());
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
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lab1Instruction = new javax.swing.JButton();
        lab1GoNEC = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
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
        jPanel17 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        angleValue = new javax.swing.JFormattedTextField();
        unit = new javax.swing.JLabel();
        unit1 = new javax.swing.JLabel();
        isAntiClockWise = new javax.swing.JCheckBox();
        jPanel26 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        planeX = new javax.swing.JRadioButton();
        planeY = new javax.swing.JRadioButton();
        planeZ = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        sourceList = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        sourceTypelbl = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        plotTick = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        plotColor = new javax.swing.JComboBox<>();
        jPanel21 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        showAnt = new javax.swing.JCheckBox();
        jPanel22 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        showRP = new javax.swing.JCheckBox();
        jPanel25 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        tauValue = new javax.swing.JFormattedTextField();
        jPanel33 = new javax.swing.JPanel();
        generateElipse = new javax.swing.JButton();
        jPanel30 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        ratimes = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        radb = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        relipticidad = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        coefElipticidad = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        mcp = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        mcc = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        cprdb = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        lab1AddResult = new javax.swing.JButton();
        lab1DeleteResult = new javax.swing.JButton();
        lab1ExportResult = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

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

        jPanel1.add(jPanel5, java.awt.BorderLayout.NORTH);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        resultTable.setBackground(new java.awt.Color(255, 255, 255));
        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        resultTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(resultTable);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(250, 424));
        jPanel8.setLayout(new java.awt.GridLayout(4, 1));

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

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setForeground(new java.awt.Color(0, 0, 0));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(3, 1));

        jLabel11.setBackground(new java.awt.Color(36, 113, 163));
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Rotación");
        jLabel11.setOpaque(true);
        jPanel2.add(jLabel11);

        jPanel3.setLayout(new java.awt.BorderLayout());

        angleValue.setBackground(new java.awt.Color(255, 255, 255));
        angleValue.setForeground(new java.awt.Color(0, 0, 0));
        angleValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        angleValue.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel3.add(angleValue, java.awt.BorderLayout.CENTER);

        unit.setBackground(new java.awt.Color(255, 255, 255));
        unit.setForeground(new java.awt.Color(0, 0, 0));
        unit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        unit.setText("Grados");
        unit.setOpaque(true);
        unit.setPreferredSize(new java.awt.Dimension(60, 16));
        jPanel3.add(unit, java.awt.BorderLayout.EAST);

        unit1.setBackground(new java.awt.Color(255, 255, 255));
        unit1.setForeground(new java.awt.Color(0, 0, 0));
        unit1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        unit1.setText("Paso");
        unit1.setOpaque(true);
        unit1.setPreferredSize(new java.awt.Dimension(50, 16));
        jPanel3.add(unit1, java.awt.BorderLayout.WEST);

        jPanel2.add(jPanel3);

        isAntiClockWise.setBackground(new java.awt.Color(255, 255, 255));
        isAntiClockWise.setForeground(new java.awt.Color(0, 0, 0));
        isAntiClockWise.setText("Sentido Antihorario");
        jPanel2.add(isAntiClockWise);

        jPanel17.add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jLabel8.setBackground(new java.awt.Color(36, 113, 163));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Eje de Rotación");
        jLabel8.setOpaque(true);
        jPanel26.add(jLabel8, java.awt.BorderLayout.PAGE_START);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setLayout(new java.awt.GridLayout(1, 3));

        planeX.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(planeX);
        planeX.setForeground(new java.awt.Color(0, 0, 0));
        planeX.setText("X");
        jPanel27.add(planeX);

        planeY.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(planeY);
        planeY.setForeground(new java.awt.Color(0, 0, 0));
        planeY.setText("Y");
        planeY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planeYActionPerformed(evt);
            }
        });
        jPanel27.add(planeY);

        planeZ.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(planeZ);
        planeZ.setForeground(new java.awt.Color(0, 0, 0));
        planeZ.setText("Z");
        jPanel27.add(planeZ);

        jPanel26.add(jPanel27, java.awt.BorderLayout.CENTER);

        jPanel17.add(jPanel26, java.awt.BorderLayout.CENTER);

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

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel13.setLayout(new java.awt.GridLayout(5, 1));

        jLabel13.setBackground(new java.awt.Color(36, 113, 163));
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Opciones de Simulación");
        jLabel13.setOpaque(true);
        jPanel13.add(jLabel13);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Paso de Ángulo Polar");
        jLabel14.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel14.add(jLabel14, java.awt.BorderLayout.LINE_START);

        plotTick.setBackground(new java.awt.Color(255, 255, 255));
        plotTick.setForeground(new java.awt.Color(0, 0, 0));
        plotTick.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "18", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45" }));
        plotTick.setSelectedIndex(4);
        plotTick.setBorder(null);
        jPanel14.add(plotTick, java.awt.BorderLayout.CENTER);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Grados");
        jPanel14.add(jLabel1, java.awt.BorderLayout.EAST);

        jPanel13.add(jPanel14);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Color de Gráfica");
        jLabel16.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel16.add(jLabel16, java.awt.BorderLayout.WEST);

        plotColor.setBackground(new java.awt.Color(255, 255, 255));
        plotColor.setForeground(new java.awt.Color(0, 0, 0));
        plotColor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ROJO", "AMARILLO", "CYAN", "VERDE", "AZUL", "NEGRO" }));
        plotColor.setSelectedIndex(4);
        plotColor.setBorder(null);
        jPanel16.add(plotColor, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel16);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Mostrar Antena");
        jLabel17.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel21.add(jLabel17, java.awt.BorderLayout.WEST);

        showAnt.setBackground(new java.awt.Color(255, 255, 255));
        showAnt.setForeground(new java.awt.Color(0, 0, 0));
        showAnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel21.add(showAnt, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel21);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setLayout(new java.awt.BorderLayout());

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Mostrar Patrón de Radiación");
        jLabel18.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel22.add(jLabel18, java.awt.BorderLayout.WEST);

        showRP.setBackground(new java.awt.Color(255, 255, 255));
        showRP.setForeground(new java.awt.Color(0, 0, 0));
        showRP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel22.add(showRP, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel22);

        jPanel12.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel12);

        jPanel6.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setPreferredSize(new java.awt.Dimension(300, 424));
        jPanel25.setLayout(new java.awt.GridLayout(1, 1));

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setForeground(new java.awt.Color(0, 0, 0));
        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel29.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel29.setLayout(new java.awt.GridLayout(3, 1));

        jLabel21.setBackground(new java.awt.Color(36, 113, 163));
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Elipse de Polarización");
        jLabel21.setOpaque(true);
        jPanel29.add(jLabel21);

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setLayout(new java.awt.BorderLayout());

        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Ángulo de Inclinación");
        jLabel22.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel32.add(jLabel22, java.awt.BorderLayout.LINE_START);

        tauValue.setBackground(new java.awt.Color(255, 255, 255));
        tauValue.setForeground(new java.awt.Color(0, 0, 0));
        tauValue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        tauValue.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel32.add(tauValue, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel32);

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setLayout(new java.awt.BorderLayout());

        generateElipse.setBackground(new java.awt.Color(36, 113, 163));
        generateElipse.setForeground(new java.awt.Color(255, 255, 255));
        generateElipse.setText("Calcular");
        generateElipse.setBorder(null);
        generateElipse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateElipseActionPerformed(evt);
            }
        });
        jPanel33.add(generateElipse, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel33);

        jPanel28.add(jPanel29, java.awt.BorderLayout.NORTH);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setLayout(new java.awt.GridLayout(7, 1));

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setLayout(new java.awt.BorderLayout());

        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Razón Axial (Veces)");
        jLabel23.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel34.add(jLabel23, java.awt.BorderLayout.LINE_START);

        ratimes.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ratimes.setForeground(new java.awt.Color(0, 0, 0));
        ratimes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ratimes.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel34.add(ratimes, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel34);

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setLayout(new java.awt.BorderLayout());

        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Razón Axial (dB)");
        jLabel24.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel35.add(jLabel24, java.awt.BorderLayout.LINE_START);

        radb.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        radb.setForeground(new java.awt.Color(0, 0, 0));
        radb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        radb.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel35.add(radb, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel35);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setLayout(new java.awt.BorderLayout());

        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Razón Elipticidad");
        jLabel25.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel36.add(jLabel25, java.awt.BorderLayout.LINE_START);

        relipticidad.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        relipticidad.setForeground(new java.awt.Color(0, 0, 0));
        relipticidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        relipticidad.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel36.add(relipticidad, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel36);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setLayout(new java.awt.BorderLayout());

        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("Coeficiente Elipticidad");
        jLabel26.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel37.add(jLabel26, java.awt.BorderLayout.LINE_START);

        coefElipticidad.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        coefElipticidad.setForeground(new java.awt.Color(0, 0, 0));
        coefElipticidad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        coefElipticidad.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel37.add(coefElipticidad, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel37);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setLayout(new java.awt.BorderLayout());

        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Mag. Comp. Ppal.");
        jLabel27.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel38.add(jLabel27, java.awt.BorderLayout.LINE_START);

        mcp.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        mcp.setForeground(new java.awt.Color(0, 0, 0));
        mcp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mcp.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel38.add(mcp, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel38);

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setLayout(new java.awt.BorderLayout());

        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Mag. Comp. Cruzada");
        jLabel28.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel39.add(jLabel28, java.awt.BorderLayout.LINE_START);

        mcc.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        mcc.setForeground(new java.awt.Color(0, 0, 0));
        mcc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mcc.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel39.add(mcc, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel39);

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setLayout(new java.awt.BorderLayout());

        jLabel29.setForeground(new java.awt.Color(0, 0, 0));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("CPR (dB)");
        jLabel29.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel40.add(jLabel29, java.awt.BorderLayout.LINE_START);

        cprdb.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cprdb.setForeground(new java.awt.Color(0, 0, 0));
        cprdb.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cprdb.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel40.add(cprdb, java.awt.BorderLayout.EAST);

        jPanel30.add(jPanel40);

        jPanel28.add(jPanel30, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel28);

        jPanel6.add(jPanel25, java.awt.BorderLayout.EAST);

        jPanel1.add(jPanel6, java.awt.BorderLayout.CENTER);

        jPanel7.setBackground(new java.awt.Color(36, 113, 163));
        jPanel7.setPreferredSize(new java.awt.Dimension(498, 25));
        jPanel7.setLayout(new java.awt.GridLayout(1, 0));

        lab1AddResult.setBackground(new java.awt.Color(36, 113, 163));
        lab1AddResult.setForeground(new java.awt.Color(255, 255, 255));
        lab1AddResult.setText("Agregar Resultado");
        lab1AddResult.setBorder(null);
        lab1AddResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1AddResultActionPerformed(evt);
            }
        });
        jPanel7.add(lab1AddResult);

        lab1DeleteResult.setBackground(new java.awt.Color(36, 113, 163));
        lab1DeleteResult.setForeground(new java.awt.Color(255, 255, 255));
        lab1DeleteResult.setText("Eliminar Resultado");
        lab1DeleteResult.setBorder(null);
        lab1DeleteResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1DeleteResultActionPerformed(evt);
            }
        });
        jPanel7.add(lab1DeleteResult);

        lab1ExportResult.setBackground(new java.awt.Color(36, 113, 163));
        lab1ExportResult.setForeground(new java.awt.Color(255, 255, 255));
        lab1ExportResult.setText("Exportar Resultados");
        lab1ExportResult.setBorder(null);
        lab1ExportResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1ExportResultActionPerformed(evt);
            }
        });
        jPanel7.add(lab1ExportResult);

        jPanel1.add(jPanel7, java.awt.BorderLayout.SOUTH);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
//Comportamiento del botón Exportar Resultados del experimento 1
    private void lab1ExportResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1ExportResultActionPerformed
        global.exportResult(exportLab21(), Global.LAB21);
    }//GEN-LAST:event_lab1ExportResultActionPerformed
//Comportamiento del botón Eliminar Resultado del experimento 1
    private void lab1DeleteResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1DeleteResultActionPerformed
        int selected = resultTable.getSelectedRow();
        if (selected != -1) {
            model.removeRow(selected);
            global.getgLab2_1().remove(selected);
            setStep(getStep() - 1);
            model.fireTableDataChanged();
        } else {
            global.errorMessage("Messages.noResultSelectedTitle", "Messages.noResultSelected");
        }
    }//GEN-LAST:event_lab1DeleteResultActionPerformed
//Comportamiento del botón Añadir Resultados del experimento 1
    private void lab1AddResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1AddResultActionPerformed

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

                if (!angleValue.getText().isEmpty()) {
                    a = Double.valueOf(angleValue.getText().replace(",", ".")) * (getStep() + 1);
                    setStep(getStep() + 1);
                }

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

                int plane = 0;
                if (planeX.isSelected()) {
                    plane = Global.XAXIS;
                } else if (planeY.isSelected()) {
                    plane = Global.YAXIS;
                } else {
                    plane = Global.ZAXIS;
                }
                double ang = Double.valueOf(angleValue.getText() + "");
                rotate(global, isShowAnt, a2From, a2To, ang, plane);
                runSimulation(isShowRP);

                Lab2_1 lab21 = new Lab2_1();
                lab21.setAngle(a);
                double freq = global.getgFrequency().getFreq();
                double vSrc1Re = (((global.getgSrcInfo().get(sourceIndx)).get(freq)).get(0)).getVoltageReal();
                double vSrc1Im = (((global.getgSrcInfo().get(sourceIndx)).get(freq)).get(0)).getVoltageImaginary();
                lab21.setV(new Complex(vSrc1Re, vSrc1Im));
                global.getgLab2_1().add(lab21);

                if (a >= 180) {
                    if (global.getgLab2_1().size() >= 8) {
                        int selectedColor = Integer.valueOf(plotColor.getSelectedIndex() + "");
                        double plotTickAmount = Double.valueOf(plotTick.getSelectedItem() + "");
                        ArrayList<PolarData> pd = new ArrayList<PolarData>();
                        for (Lab2_1 nlab21 : global.getgLab2_1()) {
                            PolarData nPd = new PolarData(nlab21.getV().abs(), nlab21.getAngle());
                            pd.add(nPd);
                        }
                        int i = 1;
                        double stp = Integer.valueOf(angleValue.getText());
                        for (Lab2_1 nlab21 : global.getgLab2_1()) {
                            PolarData nPd = new PolarData(nlab21.getV().abs(), (180 + (i * stp)));
                            pd.add(nPd);
                            i++;
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
                        for (PolarData polarData : pd) {
                            PolarData newPolarData = new PolarData(polarData.getR() / maxVal, polarData.getAngle());
                            resp.add(newPolarData);
                        }
                        setLocalPolarData(pd);
                        global.setCurrentPlotType(Global.POLARIZATIONPLOT);
                        global.executePolarization(resp, plotTickAmount, selectedColor, global);

                    }

                }

                Object[] row = {a + "",
                    lab21.getV().abs() + ""};
                model.addRow(row);
                model.fireTableDataChanged();
            } else {
                global.errorValidateInput();
            }
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_lab1AddResultActionPerformed

//Comportamiento del botón Ir al Simulador del experimento 1
    private void lab1GoNECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1GoNECActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab1GoNECActionPerformed

    private void planeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planeYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_planeYActionPerformed

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
//Comportamiento del botón Calcular del experimento 1
// Genera la elipse de polarazión a partir de los resultados del experimento y 
// la grafica
    private void generateElipseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateElipseActionPerformed
        if (!tauValue.getText().isEmpty() && global.getgLab2_1().size() > 8) {
            double tauVal = Double.valueOf(tauValue.getText().replace(",", "."));
            double gamma = Math.toRadians(tauVal);
            double maxVoltage = getMaxValue(getLocalPolarData());
            double minVoltage = getMinValue(getLocalPolarData());
            double Ra = maxVoltage / minVoltage;
            //double r00 = 1 / Ra;
            double r00 =  Ra;
            double a_1 = Math.pow(Math.cos(gamma), 2);
            double a2 = Math.pow(Math.sin(gamma), 2);
            double alfa = Math.sqrt((Math.pow(r00, 2) * a_1 + a2) / (1 + Math.pow(r00, 2)));
            ArrayList<PolarData> vNorm = new ArrayList<PolarData>();
            ArrayList<Double> r0 = new ArrayList<Double>();
            ArrayList<Double> r1 = new ArrayList<Double>();
            ArrayList<Double> r2 = new ArrayList<Double>();
            for (PolarData polarData : getLocalPolarData()) {
                PolarData n = new PolarData((polarData.getR() / maxVoltage), polarData.getAngle());
                vNorm.add(n);
            }
            double a = getMaxValue(vNorm);
            double b = getMinValue(vNorm);
            ArrayList<Double> m1 = new ArrayList<Double>();
            ArrayList<Double> m2 = new ArrayList<Double>();
            for (PolarData polarData : getLocalPolarData()) {
                double base = Math.cos(gamma - Math.toRadians(polarData.getAngle()));
                double base2 = Math.sin(gamma - Math.toRadians(polarData.getAngle()));
                m1.add(Math.pow(base, 2));
                m2.add(Math.pow(base2, 2));
            }
            double b1 = Math.pow(b, 2);
            double a1 = Math.pow(a, 2);
            for (int i = 0; i < m1.size(); i++) {
                r0.add((a * b) / Math.sqrt((b1 * m1.get(i)) + (a1 * m2.get(i))));
            }
            double aa = a;
            double bb = 0.00001;
            ArrayList<Double> m11 = new ArrayList<Double>();
            ArrayList<Double> m22 = new ArrayList<Double>();
            for (PolarData polarData : getLocalPolarData()) {
                double base = Math.cos(gamma - Math.toRadians(polarData.getAngle()));
                double base2 = Math.sin(gamma - Math.toRadians(polarData.getAngle()));
                m11.add(Math.pow(base, 2));
                m22.add(Math.pow(base2, 2));
            }
            double b11 = Math.pow(bb, 2);
            double a11 = Math.pow(aa, 2);
            for (int i = 0; i < m11.size(); i++) {
                r1.add((aa * bb) / Math.sqrt((b11 * m11.get(i)) + (a11 * m22.get(i))));
            }
            double aa1 = b;
            double bb1 = 0.00000000001;
            ArrayList<Double> m111 = new ArrayList<Double>();
            ArrayList<Double> m222 = new ArrayList<Double>();
            for (PolarData polarData : getLocalPolarData()) {
                double base = Math.cos(gamma + (Math.PI / 2) - Math.toRadians(polarData.getAngle()));
                double base2 = Math.sin(gamma + (Math.PI / 2) - Math.toRadians(polarData.getAngle()));
                m111.add(Math.pow(base, 2));
                m222.add(Math.pow(base2, 2));
            }
            double b111 = Math.pow(bb1, 2);
            double a111 = Math.pow(aa1, 2);
            for (int i = 0; i < m11.size(); i++) {
                r2.add((aa1 * bb1) / Math.sqrt((b111 * m111.get(i)) + (a111 * m222.get(i))));
            }

            double RadB = 20 * Math.log10(Ra);
            double elipsisCoef = 1 / Ra;
            double elipsisCoeffdB = 20 * Math.log10(elipsisCoef);
            double magCompPpal = 0;
            double magCompCross = 0;
            if (alfa > (1 / Math.sqrt(2))) {
                magCompPpal = alfa;
                magCompCross = Math.sqrt(1 - Math.pow(alfa, 2));
            } else {
                magCompCross = alfa;
                magCompPpal = Math.sqrt(1 - Math.pow(alfa, 2));
            }
            double CPRdB = 20 * Math.log10(alfa / Math.sqrt(1 - Math.pow(alfa, 2)));
            ArrayList<PolarData> r0Pol = new ArrayList<PolarData>();
            ArrayList<PolarData> r1Pol = new ArrayList<PolarData>();
            ArrayList<PolarData> r2Pol = new ArrayList<PolarData>();
            for (int i = 0; i < getLocalPolarData().size(); i++) {
                PolarData nr0Pol = new PolarData(r0.get(i), getLocalPolarData().get(i).getAngle());
                PolarData nr1Pol = new PolarData(r1.get(i), getLocalPolarData().get(i).getAngle());
                PolarData nr2Pol = new PolarData(r2.get(i), getLocalPolarData().get(i).getAngle());
                r0Pol.add(nr0Pol);
                r1Pol.add(nr1Pol);
                r2Pol.add(nr2Pol);
            }

            HashMap<String, ArrayList<PolarData>> resp = new HashMap<String, ArrayList<PolarData>>();
            resp.put("vnorm", vNorm);
            resp.put("r", r0Pol);
            resp.put("r1", r1Pol);
            resp.put("r2", r2Pol);

            ratimes.setText(Global.decimalFormat(Ra) + "");
            radb.setText(Global.decimalFormat(RadB) + "");
            relipticidad.setText(Global.decimalFormat(elipsisCoef) + "");
            coefElipticidad.setText(Global.decimalFormat(elipsisCoeffdB) + "");
            mcp.setText(Global.decimalFormat(magCompPpal) + "");
            mcc.setText(Global.decimalFormat(magCompCross) + "");
            if ( !Double.isInfinite(CPRdB)) {
                cprdb.setText(Global.decimalFormat(CPRdB) + "");
            }else{
                cprdb.setText("Inf");
            }

            Polarization pol = new Polarization();
            pol.setTau(Double.valueOf(tauValue.getText()));
            pol.setRa_times(Ra);
            pol.setRa_db(RadB);
            pol.setElipticityRatio(elipsisCoef);
            pol.setElipticityCoeff(elipsisCoeffdB);
            pol.setMcp(magCompPpal);
            pol.setMcc(magCompCross);
            pol.setCpr(CPRdB);

            global.setgPolarization(pol);
            double stp = Double.valueOf(plotTick.getSelectedItem() + "");
            int color = plotColor.getSelectedIndex();

            global.executePolarization(resp, stp , color, global);
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_generateElipseActionPerformed
//Comportamiento del botón Instrucciones del experimento 1
    private void lab1InstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1InstructionActionPerformed
        PDFReader.loadPdf("instruccioneslab21.pdf");
    }//GEN-LAST:event_lab1InstructionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField angleValue;
    private javax.swing.JComboBox<String> ant1From;
    private javax.swing.JComboBox<String> ant1To;
    private javax.swing.JComboBox<String> ant2From;
    private javax.swing.JComboBox<String> ant2To;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JLabel coefElipticidad;
    private javax.swing.JLabel cprdb;
    private javax.swing.JButton generateElipse;
    private javax.swing.JCheckBox isAntiClockWise;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
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
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lab1AddResult;
    private javax.swing.JButton lab1DeleteResult;
    private javax.swing.JButton lab1ExportResult;
    private javax.swing.JButton lab1GoNEC;
    private javax.swing.JButton lab1Instruction;
    private javax.swing.JLabel mcc;
    private javax.swing.JLabel mcp;
    private javax.swing.JRadioButton planeX;
    private javax.swing.JRadioButton planeY;
    private javax.swing.JRadioButton planeZ;
    private javax.swing.JComboBox<String> plotColor;
    private javax.swing.JComboBox<String> plotTick;
    private javax.swing.JLabel radb;
    private javax.swing.JLabel ratimes;
    private javax.swing.JLabel relipticidad;
    private javax.swing.JTable resultTable;
    private javax.swing.JCheckBox showAnt;
    private javax.swing.JCheckBox showRP;
    private javax.swing.JComboBox<String> sourceList;
    private javax.swing.JLabel sourceTypelbl;
    private javax.swing.JFormattedTextField tauValue;
    private javax.swing.JLabel unit;
    private javax.swing.JLabel unit1;
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
