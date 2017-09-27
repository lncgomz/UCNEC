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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import ucnecgui.Global;
import ucnecgui.models.Line;
import ucnecgui.models.Point;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class GeneratorPanel extends javax.swing.JPanel {

    public JTabbedPane tabbedPane;
    public int[] selectedWires;
    private JComboBox selected;
    private Global global;
    private JFrame frame;

    /**
     * Constructor de la clase GeneratorPanel
     *
     * @param global Objeto de la clase Global
     * @param selectedWires Vector de enteros con los índices de los alambres
     * seleccionados
     * @param frame JFrame padre del JPanel GeneratorPanel
     * @param selected Componente JComboBox correspondiente al selector de
     * alambres
     */
    public GeneratorPanel(Global global, int[] selectedWires, JFrame frame, JComboBox selected) {
        initComponents();
        this.global = global;
        this.frame = frame;
        this.selectedWires = selectedWires;
        this.selected = selected;
        initializePanel();
    }

    /**
     * Inicialización de los componentes constituyentes del GeometryPanel
     */
    public void initializePanel() {
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/circle.png")));
        ImageIcon icon3 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/arc.png")));
        ImageIcon icon4 = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/dipole.png")));
        jLabel2.setText(Global.getMessages().getString("GenerationPanel.label"));
        jLabel8.setText(global.unit2String());
        jTabbedPane1.removeAll();
        jTabbedPane1.addTab(Global.getMessages().getString("GenerationPanel.circle.label"), jPanel5);
        jTabbedPane1.addTab(Global.getMessages().getString("GenerationPanel.arc.label"), jPanel6);
        jTabbedPane1.addTab(Global.getMessages().getString("GenerationPanel.dipole.label"), jPanel33);

// Componentes del aparado de generación de círculo
        jLabel4.setIcon(icon);
        jLabel1.setText(Global.getMessages().
                getString("GenerationPanel.circle.plane"));
        jLabel5.setText(Global.getMessages().
                getString("GenerationPanel.circle.center"));
        jLabel6.setText(Global.getMessages().
                getString("GenerationPanel.circle.radius"));
        jLabel7.setText(Global.getMessages().
                getString("GenerationPanel.circle.segments"));
        jLabel8.setText(global.unit2String());
        jCheckBox1.setText(Global.getMessages().
                getString("GenerationPanel.circle.drawRadii"));
        jButton1.setText(Global.getMessages().getString("GenerationPanel.circle.ok"));
        
// Componentes del aparado de generación de arco
        jLabel10.setIcon(icon3);
        jLabel13.setText(Global.getMessages().
                getString("GenerationPanel.circle.plane"));
        jLabel14.setText(Global.getMessages().
                getString("GenerationPanel.circle.center"));
        jLabel20.setText(Global.getMessages().
                getString("GenerationPanel.circle.radius"));
        jLabel21.setText(global.unit2String());
        jLabel22.setText(Global.getMessages().
                getString("GenerationPanel.circle.segments"));
        jButton3.setText(Global.getMessages().getString("GenerationPanel.circle.ok"));
        
// Componentes del aparado de generación de dipolo de lambda / 2
        jButton4.setText(Global.getMessages().getString("GenerationPanel.circle.ok"));
        jLabel23.setIcon(icon4);
        TitledBorder title = BorderFactory.createTitledBorder(Global.getMessages().getString("GenerationPanel.arc.center"));
        jPanel4.setBorder(title);
        centerXunits.setText(global.unit2String());
        centerYunits.setText(global.unit2String());
        centerZunits.setText(global.unit2String());

        // Comportamiento del botón Aceptar del apartado Círculo
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int plane = Global.XYPLANE; // El círculo se dibuja en el plano XY por defecto
                int segments = Integer.valueOf(jSpinner2.getValue().toString()); // Tramos del círculo a ser generado
                double radius = Double.valueOf(jFormattedTextField4.getText().replace(",", ".")); //Radio del círculo
                boolean drawRadius = jCheckBox1.isSelected(); //Determina si se dibujarán también los radios del círculo

                //Centro del círculo
                Point center = new Point(Double.valueOf(jFormattedTextField1.getText().replace(",", ".")),
                        Double.valueOf(jFormattedTextField2.getText().replace(",", ".")),
                        Double.valueOf(jFormattedTextField3.getText().replace(",", ".")));

                if (jRadioButton5.isSelected()) {
                    plane = Global.YZPLANE; //El círculo se dibuja en el plano YZ
                } else if (jRadioButton6.isSelected()) {
                    plane = Global.XZPLANE; //El círculo se dibuja en el plano XZ
                } else {
                    plane = Global.XYPLANE; // El círculo se dibuja en el plano XY
                }

                ArrayList<Line> lines = Global.generateCircle(plane, center, segments, radius, drawRadius); //Se genera un arreglo de objetos Line con los tramos que constituyen el círculo a partir de los parámetros introducidos
                for (Line line : lines) {
                    double diam = Double.valueOf((Global.df.format((2 * global.getWavelength() / 100) * 0.5)).replace(",", ".")); //Cálculo de diámetro por regla de diseño NEC2 (radio < longitud de onda / 100
                    int seg = global.getSegments(line); // Cálculo de segmentos por regla de diseño NEC (segmentos = 20 * distancia / longitud de onda)
                    String number = String.valueOf(global.getLastWireNumber() + 1);
                    String x1 = line.getX1() + "";
                    String y1 = line.getY1() + "";
                    String z1 = line.getZ1() + "";
                    String x2 = line.getX2() + "";
                    String y2 = line.getY2() + "";
                    String z2 = line.getZ2() + "";
                    String diameter = diam + "";
                    String segs = seg + "";
                    String[] row = {number, x1, y1, z1, x2, y2, z2, diameter, segs};
                    global.getgWires().add(new Wire(row)); //Creación de objeto Wire a partir del tramo del círculo
                }
                GeometryPanel.currentSelectedRow = -1; //No se ha seleccionado ningún alambre
                global.sortWires(); //Ordenar segmentos, colocando al segmento auxiliar de corriente en la última posición (Si lo hubiese)
                Global.updateTable(GeometryPanel.model, global); //Actualizar tabla de geometrías
                GeometryPanel.model.fireTableDataChanged(); 
                frame.dispose(); //Cerrar ventana
                updateWireSelector(selected); //Actualizar selector de alambres
                global.updatePlot(global); //Actualizar gráfica
            }

        });

        // Comportamiento del botón Aceptar del apartado Arco
        jButton3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int plane = Global.XYPLANE;
                int segments = Integer.valueOf(jSpinner3.getValue().toString());
                double radius = Double.valueOf(jFormattedTextField17.getText().replace(",", "."));
                Point center = new Point(Double.valueOf(jFormattedTextField5.getText().replace(",", ".")),
                        Double.valueOf(jFormattedTextField9.getText().replace(",", ".")),
                        Double.valueOf(jFormattedTextField16.getText().replace(",", ".")));

                if (jRadioButton8.isSelected()) {
                    plane = Global.YZPLANE;
                } else if (jRadioButton9.isSelected()) {
                    plane = Global.XZPLANE;
                } else {
                    plane = Global.XYPLANE;
                }

                ArrayList<Line> lines = Global.generateArc(plane, center, segments, radius); //Se genera un arreglo de objetos Line con los tramos que constituyen el arco a partir de los parámetros introducidos
                for (Line line : lines) {
                    double diam = Double.valueOf((Global.df.format((2 * global.getWavelength() / 100) * 0.5)).replace(",", "."));
                    int seg = global.getSegments(line);

                    String number = String.valueOf(global.getLastWireNumber() + 1);
                    String x1 = line.getX1() + "";
                    String y1 = line.getY1() + "";
                    String z1 = line.getZ1() + "";
                    String x2 = line.getX2() + "";
                    String y2 = line.getY2() + "";
                    String z2 = line.getZ2() + "";
                    String diameter = diam + "";
                    String segs = seg + "";
                    String[] row = {number, x1, y1, z1, x2, y2, z2, diameter, segs};
                    global.getgWires().add(new Wire(row));
                }
                GeometryPanel.currentSelectedRow = -1;
                global.sortWires();
                updateWireSelector(selected);
                Global.updateTable(GeometryPanel.model, global);
                GeometryPanel.model.fireTableDataChanged();
                frame.dispose();
            }

        });

        // Comportamiento del botón Aceptar del apartado Dipolo de Lambda / 2
        jButton4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validateInputDipole()) { //Se verifica que los parámetros introducidos para la creación del dipolo sean válidos
                    double cX = Double.valueOf((centerX.getText()).replace(",", "."));
                    double cY = Double.valueOf((centerY.getText()).replace(",", "."));
                    double cZ = Double.valueOf((centerZ.getText()).replace(",", "."));
                    double ldRatio = Double.valueOf((ld.getText()).replace(",", "."));
                    int segs = Integer.valueOf((segments.getText()));
                    double wlFactor = global.convertUnits(Global.METER, global.getCurrentUnit()); //Convierte el valor de la longitud de onda de metros a la unidad de diseño actual
                    double wl = global.getWavelength();
                    double l = (wl / 2) * wlFactor;
                    double upperfactor = global.unit2UpperFactor();
                    double d = (l / ldRatio); //Convierte el valor del diámetro a una unidad adecuada (En general, menor) para una mejor visualización

                    //Crea un objeto Wire a partir de los parámetros introducidos, considerando el plano donde se encontrará el dipolo
                    Wire dipole = new Wire();
                    if (xPlane.isSelected()) {
                        dipole.setX1(Global.decimalFormat(cX + l / 2));
                        dipole.setY1(Global.decimalFormat(cY));
                        dipole.setZ1(Global.decimalFormat(cZ));
                        dipole.setX2(Global.decimalFormat(cX - l / 2));
                        dipole.setY2(Global.decimalFormat(cY));
                        dipole.setZ2(Global.decimalFormat(cZ));
                        dipole.setSegs(segs);
                        dipole.setRadius(d);
                        dipole.setNumber(global.getgWires().size() + 1);

                    } else if (yPlane.isSelected()) {
                        dipole.setX1(Global.decimalFormat(cX));
                        dipole.setY1(Global.decimalFormat(cY + l / 2));
                        dipole.setZ1(Global.decimalFormat(cZ));
                        dipole.setX2(Global.decimalFormat(cX));
                        dipole.setY2(Global.decimalFormat(cY - l / 2));
                        dipole.setZ2(Global.decimalFormat(cZ));
                        dipole.setSegs(segs);
                        dipole.setRadius(Global.decimalFormat((d)));
                        dipole.setNumber(global.getgWires().size() + 1);
                    } else {
                        dipole.setX1(Global.decimalFormat(cX));
                        dipole.setY1(Global.decimalFormat(cY));
                        dipole.setZ1(Global.decimalFormat(cZ + l / 2));
                        dipole.setX2(Global.decimalFormat(cX));
                        dipole.setY2(Global.decimalFormat(cY));
                        dipole.setZ2(Global.decimalFormat(cZ - l / 2));
                        dipole.setSegs(segs);
                        dipole.setRadius(Global.decimalFormat(d));
                        dipole.setNumber(global.getgWires().size() + 1);
                    }
                    global.getgWires().add(dipole);
                    global.sortWires();
                    Global.updateTable(GeometryPanel.model, global);
                    updateWireSelector(selected);
                    global.updatePlot(global);

                } else {
                    global.errorValidateInput();
                }
            }
        });
    }

    /**
     *Actualiza al objeto JComboBox del selector de alambres, considerando los nuevos alambres creados
     * @param wireSelector Objeto JComboBox del selector de alambres
     */
    public void updateWireSelector(JComboBox wireSelector) {
        wireSelector.removeAllItems();
        for (int i = 0; i < global.getgWires().size(); i++) {
            if (i == global.getCurrentSourceTag() - 1) { //Excluye al alambre auxiliar de corriente, si lo hubiese
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
     *Verifica que los parámetros introducidos para la creación del dipolo sean corrector
     * @return true si los valores introducidos son válidos, de lo contrario, devuelve false
     */
    public boolean validateInputDipole() {
        boolean condition1, condition2;
        condition1 = (!centerX.getText().isEmpty())
                && (!centerY.getText().isEmpty())
                && (!centerZ.getText().isEmpty())
                && (!ld.getText().isEmpty())
                && (!segments.getText().isEmpty());
        if (condition1) {
            condition2 = (Double.valueOf(ld.getText().replace(",", ".")) > 0) && (Double.valueOf(segments.getText().replace(",", ".")) > 0);
        } else {
            return false;
        }
        return condition1 && condition2;
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
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jPanel22 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jPanel13 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jPanel30 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jFormattedTextField9 = new javax.swing.JFormattedTextField();
        jFormattedTextField16 = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jFormattedTextField17 = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jPanel34 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel33 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jPanel37 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        centerXlbl = new javax.swing.JLabel();
        centerX = new javax.swing.JFormattedTextField();
        centerXunits = new javax.swing.JLabel();
        centerYlbl = new javax.swing.JLabel();
        centerY = new javax.swing.JFormattedTextField();
        centerYunits = new javax.swing.JLabel();
        centerZlbl = new javax.swing.JLabel();
        centerZ = new javax.swing.JFormattedTextField();
        centerZunits = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        ldlbl = new javax.swing.JLabel();
        ld = new javax.swing.JFormattedTextField();
        lblunits = new javax.swing.JLabel();
        segmentslbl = new javax.swing.JLabel();
        segments = new javax.swing.JFormattedTextField();
        segmentunits = new javax.swing.JLabel();
        planelbl = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        xPlane = new javax.swing.JRadioButton();
        yPlane = new javax.swing.JRadioButton();
        zPlane = new javax.swing.JRadioButton();
        jPanel41 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(800, 700));
        setMinimumSize(new java.awt.Dimension(800, 700));
        setPreferredSize(new java.awt.Dimension(800, 700));
        setRequestFocusEnabled(false);
        setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setMaximumSize(new java.awt.Dimension(800, 700));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 700));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel2.setBackground(new java.awt.Color(36, 113, 163));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(700, 25));
        jPanel3.add(jLabel2, java.awt.BorderLayout.NORTH);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane1.setToolTipText("");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new java.awt.GridLayout(2, 1));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel11.setName(""); // NOI18N
        jPanel11.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel11.setRequestFocusEnabled(false);
        jPanel11.setLayout(new java.awt.BorderLayout());

        jLabel4.setText("jLabel4");
        jPanel11.add(jLabel4, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setLayout(new java.awt.GridLayout(5, 1));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("jLabel1");
        jPanel21.add(jLabel1);

        jRadioButton4.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton4);
        jRadioButton4.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton4.setText("XY");
        jPanel21.add(jRadioButton4);

        jRadioButton5.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton5);
        jRadioButton5.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton5.setText("YZ");
        jPanel21.add(jRadioButton5);

        jRadioButton6.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton6);
        jRadioButton6.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton6.setText("XZ");
        jPanel21.add(jRadioButton6);

        jPanel12.add(jPanel21);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("jLabel5");
        jPanel22.add(jLabel5);

        jFormattedTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField1.setColumns(3);
        jFormattedTextField1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel22.add(jFormattedTextField1);

        jFormattedTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField2.setColumns(3);
        jFormattedTextField2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel22.add(jFormattedTextField2);

        jFormattedTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField3.setColumns(3);
        jFormattedTextField3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel22.add(jFormattedTextField3);

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("[X, Y, Z]");
        jPanel22.add(jLabel3);

        jPanel12.add(jPanel22);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("jLabel6");
        jPanel23.add(jLabel6);

        jFormattedTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField4.setColumns(3);
        jFormattedTextField4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jFormattedTextField4);

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jLabel8);

        jPanel12.add(jPanel23);

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("jLabel7");
        jPanel24.add(jLabel7);

        jSpinner2.setModel(new javax.swing.SpinnerNumberModel(4, 4, 500, 1));
        jSpinner2.setPreferredSize(new java.awt.Dimension(50, 26));
        jPanel24.add(jSpinner2);

        jPanel12.add(jPanel24);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setForeground(new java.awt.Color(0, 0, 0));
        jCheckBox1.setText("jCheckBox1");
        jPanel13.add(jCheckBox1);

        jPanel12.add(jPanel13);

        jPanel9.add(jPanel12);

        jPanel5.add(jPanel9, java.awt.BorderLayout.CENTER);

        jPanel10.setLayout(new java.awt.GridLayout(1, 1));

        jButton1.setText("jButton1");
        jPanel10.add(jButton1);

        jPanel5.add(jPanel10, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("tab1", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new java.awt.GridLayout(2, 1));

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel19.setName(""); // NOI18N
        jPanel19.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel19.setRequestFocusEnabled(false);
        jPanel19.setLayout(new java.awt.BorderLayout());

        jLabel10.setText("jLabel4");
        jPanel19.add(jLabel10, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel19);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setLayout(new java.awt.GridLayout(4, 1));

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("jLabel1");
        jPanel29.add(jLabel13);

        jRadioButton7.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton7);
        jRadioButton7.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton7.setSelected(true);
        jRadioButton7.setText("XY");
        jPanel29.add(jRadioButton7);

        jRadioButton8.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton8);
        jRadioButton8.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton8.setText("YZ");
        jPanel29.add(jRadioButton8);

        jRadioButton9.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton9);
        jRadioButton9.setForeground(new java.awt.Color(0, 0, 0));
        jRadioButton9.setText("XZ");
        jPanel29.add(jRadioButton9);

        jPanel28.add(jPanel29);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("jLabel5");
        jPanel30.add(jLabel14);

        jFormattedTextField5.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField5.setColumns(3);
        jFormattedTextField5.setForeground(new java.awt.Color(0, 0, 0));
        jPanel30.add(jFormattedTextField5);

        jFormattedTextField9.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField9.setColumns(3);
        jFormattedTextField9.setForeground(new java.awt.Color(0, 0, 0));
        jPanel30.add(jFormattedTextField9);

        jFormattedTextField16.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField16.setColumns(3);
        jFormattedTextField16.setForeground(new java.awt.Color(0, 0, 0));
        jPanel30.add(jFormattedTextField16);

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("[X, Y, Z]");
        jPanel30.add(jLabel15);

        jPanel28.add(jPanel30);

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("jLabel6");
        jPanel31.add(jLabel20);

        jFormattedTextField17.setBackground(new java.awt.Color(255, 255, 255));
        jFormattedTextField17.setColumns(3);
        jFormattedTextField17.setForeground(new java.awt.Color(0, 0, 0));
        jPanel31.add(jFormattedTextField17);

        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jPanel31.add(jLabel21);

        jPanel28.add(jPanel31);

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("jLabel7");
        jPanel32.add(jLabel22);

        jSpinner3.setModel(new javax.swing.SpinnerNumberModel(4, 4, 500, 1));
        jSpinner3.setMinimumSize(new java.awt.Dimension(40, 26));
        jSpinner3.setPreferredSize(new java.awt.Dimension(50, 26));
        jPanel32.add(jSpinner3);

        jPanel28.add(jPanel32);

        jPanel14.add(jPanel28);

        jPanel6.add(jPanel14, java.awt.BorderLayout.CENTER);

        jPanel34.setLayout(new java.awt.GridLayout(1, 1));

        jButton3.setText("jButton1");
        jPanel34.add(jButton3);

        jPanel6.add(jPanel34, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("tab3", jPanel6);

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setLayout(new java.awt.BorderLayout());

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setLayout(new java.awt.GridLayout(2, 1));

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel36.setName(""); // NOI18N
        jPanel36.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel36.setRequestFocusEnabled(false);
        jPanel36.setLayout(new java.awt.BorderLayout());

        jLabel23.setText("jLabel4");
        jPanel36.add(jLabel23, java.awt.BorderLayout.CENTER);

        jPanel35.add(jPanel36);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setLayout(new java.awt.GridLayout(2, 1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.GridLayout(3, 3));

        centerXlbl.setBackground(new java.awt.Color(255, 255, 255));
        centerXlbl.setForeground(new java.awt.Color(0, 0, 0));
        centerXlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerXlbl.setText("X");
        jPanel4.add(centerXlbl);

        centerX.setForeground(new java.awt.Color(0, 0, 0));
        centerX.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00000"))));
        centerX.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(centerX);

        centerXunits.setForeground(new java.awt.Color(0, 0, 0));
        centerXunits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerXunits.setText("jLabel1");
        jPanel4.add(centerXunits);

        centerYlbl.setBackground(new java.awt.Color(255, 255, 255));
        centerYlbl.setForeground(new java.awt.Color(0, 0, 0));
        centerYlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerYlbl.setText("Y");
        jPanel4.add(centerYlbl);

        centerY.setForeground(new java.awt.Color(0, 0, 0));
        centerY.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00000"))));
        centerY.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(centerY);

        centerYunits.setForeground(new java.awt.Color(0, 0, 0));
        centerYunits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerYunits.setText("jLabel2");
        jPanel4.add(centerYunits);

        centerZlbl.setBackground(new java.awt.Color(255, 255, 255));
        centerZlbl.setForeground(new java.awt.Color(0, 0, 0));
        centerZlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerZlbl.setText("Z");
        jPanel4.add(centerZlbl);

        centerZ.setForeground(new java.awt.Color(0, 0, 0));
        centerZ.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00000"))));
        centerZ.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(centerZ);

        centerZunits.setForeground(new java.awt.Color(0, 0, 0));
        centerZunits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        centerZunits.setText("jLabel3");
        jPanel4.add(centerZunits);

        jPanel37.add(jPanel4);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setLayout(new java.awt.GridLayout(3, 3));

        ldlbl.setBackground(new java.awt.Color(255, 255, 255));
        ldlbl.setForeground(new java.awt.Color(0, 0, 0));
        ldlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ldlbl.setText("L/D");
        jPanel38.add(ldlbl);

        ld.setForeground(new java.awt.Color(0, 0, 0));
        ld.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        ld.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel38.add(ld);

        lblunits.setBackground(new java.awt.Color(255, 255, 255));
        lblunits.setForeground(new java.awt.Color(0, 0, 0));
        lblunits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel38.add(lblunits);

        segmentslbl.setBackground(new java.awt.Color(255, 255, 255));
        segmentslbl.setForeground(new java.awt.Color(0, 0, 0));
        segmentslbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        segmentslbl.setText("Segmentos");
        jPanel38.add(segmentslbl);

        segments.setForeground(new java.awt.Color(0, 0, 0));
        segments.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        segments.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel38.add(segments);

        segmentunits.setBackground(new java.awt.Color(255, 255, 255));
        segmentunits.setForeground(new java.awt.Color(0, 0, 0));
        segmentunits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        segmentunits.setText("Unidades");
        jPanel38.add(segmentunits);

        planelbl.setBackground(new java.awt.Color(255, 255, 255));
        planelbl.setForeground(new java.awt.Color(0, 0, 0));
        planelbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        planelbl.setText("Eje");
        jPanel38.add(planelbl);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridLayout(1, 3));

        xPlane.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(xPlane);
        xPlane.setForeground(new java.awt.Color(0, 0, 0));
        xPlane.setSelected(true);
        xPlane.setText("X");
        jPanel1.add(xPlane);

        yPlane.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(yPlane);
        yPlane.setForeground(new java.awt.Color(0, 0, 0));
        yPlane.setText("Y");
        jPanel1.add(yPlane);

        zPlane.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup2.add(zPlane);
        zPlane.setForeground(new java.awt.Color(0, 0, 0));
        zPlane.setText("Z");
        jPanel1.add(zPlane);

        jPanel38.add(jPanel1);

        jPanel37.add(jPanel38);

        jPanel35.add(jPanel37);

        jPanel33.add(jPanel35, java.awt.BorderLayout.CENTER);

        jPanel41.setLayout(new java.awt.GridLayout(1, 1));

        jButton4.setText("jButton1");
        jPanel41.add(jButton4);

        jPanel33.add(jPanel41, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("tab2", jPanel33);

        jPanel3.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        add(jPanel3, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JFormattedTextField centerX;
    private javax.swing.JLabel centerXlbl;
    private javax.swing.JLabel centerXunits;
    private javax.swing.JFormattedTextField centerY;
    private javax.swing.JLabel centerYlbl;
    private javax.swing.JLabel centerYunits;
    private javax.swing.JFormattedTextField centerZ;
    private javax.swing.JLabel centerZlbl;
    private javax.swing.JLabel centerZunits;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField16;
    private javax.swing.JFormattedTextField jFormattedTextField17;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JFormattedTextField jFormattedTextField9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton9;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblunits;
    private javax.swing.JFormattedTextField ld;
    private javax.swing.JLabel ldlbl;
    private javax.swing.JLabel planelbl;
    private javax.swing.JFormattedTextField segments;
    private javax.swing.JLabel segmentslbl;
    private javax.swing.JLabel segmentunits;
    private javax.swing.JRadioButton xPlane;
    private javax.swing.JRadioButton yPlane;
    private javax.swing.JRadioButton zPlane;
    // End of variables declaration//GEN-END:variables
}
