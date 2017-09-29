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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import ucnecgui.Global;
import ucnecgui.MetaGlobal;
import ucnecgui.jframes.MultiFrame;
import static ucnecgui.jpanels.NECModulePanel.generateInputFile;
import static ucnecgui.jpanels.NECModulePanel.generateOutputData;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.Frequency;
import ucnecgui.models.Lab1_1;
import ucnecgui.models.Lab1_2;
import ucnecgui.models.Lab1_3;
import ucnecgui.models.Lab1_4;
import ucnecgui.models.Network;
import ucnecgui.models.RPLine;
import ucnecgui.models.SWR;
import ucnecgui.models.Source;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class Lab1Panel extends javax.swing.JPanel {

    private Global global;
    private DefaultTableModel model;
    private DefaultTableModel model1;
    private DefaultTableModel model2;
    private DefaultTableModel model3;
    private boolean firstExecution;
    private boolean firstExecutionLab12;
    private int lab11WireTag = -1;
    private int plotType = -1;
    private double swrZ0;
    private Wire lab2OriginalWire;

    /**
     * Constructor de la clase Lab1Panel
     *
     * @param global Objeto de la clase Global
     */
    public Lab1Panel(Global global) {
        initComponents();
        initTable(global);
        this.global = global;
        loadWires(global);

        firstExecution = true;
        firstExecutionLab12 = true;
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
        //Wire selector loader        
        for (int i = 0; i < global.getgWires().size(); i++) {
            if (i == global.getCurrentSourceTag() - 1) {
                continue;
            }
            ant1From.addItem(global.getgWires().get(i).getNumber() + "");
            ant1To.addItem(global.getgWires().get(i).getNumber() + "");
            ant2From.addItem(global.getgWires().get(i).getNumber() + "");
            ant2To.addItem(global.getgWires().get(i).getNumber() + "");
        }
        ant1From.revalidate();
        ant1To.revalidate();
        ant2From.revalidate();
        ant2To.revalidate();
    }

    /**
     * Inicializa los componentes del panel
     *
     * @param global Objeto de la clase Global
     */
    public void initTable(Global global) {
        //Configuración de la unidad mostrada
        unit.setText(global.unit2ShortString());
        // Títulos de las columnas de la tablas de resultados de los diferentes experimentos
        String columnName[] = {"L/D", "Diámetro (mm)", "Frecuencia (MHz) menor SWR", "Za", "Frecuencia de Diseño", "Menor SWR", "SWR Frecuencia de Diseño", "Ancho de Banda (MHz)"};
        String columnName1[] = {"Escala", "Longitud L (m)", "Diámetro (mm)", "Za para Frecuencia de Diseño", "SWR para Frecuencia de Diseño"};
        String columnName2[] = {"Distancia", "Voltaje Fuente 1", "Corriente Fuente 1", "Impedancia Fuente 1", "Voltaje Fuente 2", "Corriente Fuente 2", "Impedancia Fuente 2"};
        String columnName3[] = {"Factor de distanciamiento", "Distancia con respecto a tierra (m)", "Impedancia Ze1"};

        //Comportamiento del JCheckBox "Z0 akternativa" del experimento 1
        alterz0.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (alterz0.isSelected()) {
                    if (global.getAlterZ0() != 0) {
                        swrZ0 = global.getAlterZ0();
                    } else {
                        String msg = Global.getMessages().getString("Messages.alterZ0");
                        String input = global.inputMessage(msg).replace(",", ".");
                        double alterZ0 = 0;
                        if (Global.isNumeric(input)) {
                            alterZ0 = Double.valueOf(input);
                            if (alterZ0 > 0) {
                                global.setAlterZ0(alterZ0);
                                swrZ0 = alterZ0;
                            } else {
                                global.errorValidateInput();
                            }
                        } else {
                            global.errorValidateInput();
                        }
                    }
                }

            }
        });

        //Comportamiento del JCheckBox "Z0 akternativa" del experimento 2
        alterz1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (alterz1.isSelected()) {
                    if (global.getAlterZ0() != 0) {
                        swrZ0 = global.getAlterZ0();
                    } else {
                        String msg = Global.getMessages().getString("Messages.alterZ0");
                        String input = global.inputMessage(msg).replace(",", ".");
                        double alterZ0 = 0;
                        if (Global.isNumeric(input)) {
                            alterZ0 = Double.valueOf(input);
                            if (alterZ0 > 0) {
                                global.setAlterZ0(alterZ0);
                                swrZ0 = alterZ0;
                            } else {
                                global.errorValidateInput();
                            }
                        } else {
                            global.errorValidateInput();
                        }
                    }
                }

            }
        });

        //Modelo de la tabla de resultados del experimento 1
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

        //Modelo de la tabla de resultados del experimento 2
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
        resultTable1.setModel(model1);

        //Modelo de la tabla de resultados del experimento 3
        model2 = new DefaultTableModel() {

            @Override
            public int getColumnCount() {
                return columnName2.length;
            }

            @Override
            public String getColumnName(int index) {
                return columnName2[index];
            }

            @Override
            public boolean isCellEditable(int row,
                    int column) {
                return false;
            }
        };
        resultTable2.setModel(model2);

        //Modelo de la tabla de resultados del experimento 4
        model3 = new DefaultTableModel() {

            @Override
            public int getColumnCount() {
                return columnName3.length;
            }

            @Override
            public String getColumnName(int index) {
                return columnName3[index];
            }

            @Override
            public boolean isCellEditable(int row,
                    int column) {
                return false;
            }
        };
        resultTable3.setModel(model3);
    }

    /**
     * Inicializa el valor de la frecuencia de operación del experimento
     */
    public void initFrequencies() {
        if (global.getgFrequency().getFreq() != 0.0) {
            optFreq.setText(global.getgFrequency().getFreq() + "");
            optFreq1.setText(global.getgFrequency().getFreq() + "");
        }
    }

    /**
     * Exporta los resultados del experimento 1 a un archivo de texto
     *
     * @return Arreglo de objetos String con los resultados del experimento 1
     */
    public ArrayList<String> exportLab11() {
        ArrayList<String> resp = new ArrayList<String>();
        resp.add("L/D, Diámetro (mm), Frecuencia (MHz) menor SWR, Za, Frecuencia de Diseño, Menor SWR, SWR Frecuencia de Diseño, Ancho de Banda (MHz)");
        for (Lab1_1 lab11 : global.getgLab1_1()) {
            resp.add(lab11.getLd() + "," + (global.decimalShortFormat(lab11.getDiameter() * global.unit2UpperFactor())) + "," + lab11.getLowerSWRFreq() + "," + lab11.getZa() + "," + global.getgFrequency().getFreq() + "," + Global.decimalFormat(lab11.getLowerSWR()) + ","
                    + Global.decimalFormat(lab11.getDesignSWR()) + "," + Global.decimalFormat(lab11.getBw()));
        }
        return resp;
    }

    /**
     * Exporta los resultados del experimento 2 a un archivo de texto
     *
     * @return Arreglo de objetos String con los resultados del experimento 2
     */
    public ArrayList<String> exportLab12() {
        ArrayList<String> resp = new ArrayList<String>();
        resp.add("Escala, Longitud L (m), Diámetro (mm), Za para Frecuencia de Diseño, Frecuencia de Diseño (MHz),SWR para Frecuencia de Diseño");
        for (Lab1_2 lab12 : global.getgLab1_2()) {
            resp.add(lab12.getScale() + ",  " + lab12.getLength() + ",  " + global.decimalShortFormat(lab12.getDiameter() * global.unit2UpperFactor()) + ",  " + lab12.getZa() + ",  " + global.getgFrequency().getFreq() + ",  " + Global.decimalFormat(lab12.getDesignSWR()));
        }
        return resp;
    }

    /**
     * Exporta los resultados del experimento 3 a un archivo de texto
     *
     * @return Arreglo de objetos String con los resultados del experimento 3
     */
    public ArrayList<String> exportLab13() {
        ArrayList<String> resp = new ArrayList<String>();
        resp.add("Distancia (m), Voltaje Fuente 1 (V), Corriente Fuente 1 (I), Impedancia Fuente 1 (Ohm), Voltaje Fuente 2 (V), Corriente Fuente 2 (I), Impedancia Fuente 2 (Ohm)");
        for (Lab1_3 lab13 : global.getgLab1_3()) {
            resp.add(lab13.getDistance() + ",  " + lab13.getvSrc1() + ",  " + lab13.getiSrc1() + ",  " + lab13.getzSrc1() + ",  " + lab13.getvSrc2() + ",  " + lab13.getiSrc2() + ",  " + lab13.getzSrc2());
        }
        return resp;
    }

    /**
     * Exporta los resultados del experimento 4 a un archivo de texto
     *
     * @return Arreglo de objetos String con los resultados del experimento 4
     */
    public ArrayList<String> exportLab14() {
        ArrayList<String> resp = new ArrayList<String>();
        resp.add("Factor de distanciamiento, Distancia con respecto a tierra (m), Impedancia Ze1 (Ohm)");
        for (Lab1_4 lab14 : global.getgLab1_4()) {
            resp.add(lab14.getFactor() + "," + lab14.getHeight() + "," + lab14.getZa());
        }
        return resp;
    }

    /**
     * Solicita el valor de frecuencia (En MHz) en aquellos casos donde,
     * requiriéndose, no se ha establecido
     *
     * @return Valor de la frecuencia de operación en MHz
     */
    public double generateFrequency() {
        String msg = Global.getMessages().getString("SimulationPanel.geometry.inputFreqMessage");
        String input = global.inputMessage(msg).replace(",", ".");
        if (Global.isNumeric(input)) {
            double angle = Double.valueOf(input);
            if (angle > 0) {
                double wavelength = Global.LIGHTSPEED / (angle * 1E6);
                Frequency freq = new Frequency();
                freq.setFreq(angle);
                freq.setFreqIncrement(0);
                freq.setSteppingType(0);
                freq.setSteps(1);
                global.setgFrequency(freq);
                return freq.getFreq();
            } else {
                global.errorValidateInput();
                return 0;
            }
        } else {
            global.errorValidateInput();
            return 0;
        }
    }

    /**
     * Solicita el valor de los segmentos de los alambres de las antenas en
     * aquellos casos donde, requiriéndose, no se han establecido
     *
     * @return Valor de los segmentos de las antenas empleadas en el experimento
     */
    public int generateSegs() {
        String msg = Global.getMessages().getString("Messages.requireSegs");
        String input = global.inputMessage(msg).replace(",", ".");
        if (Global.isNumeric(input)) {
            int s = Integer.valueOf(input);
            return s;
        } else {
            global.errorValidateInput();
            return 0;
        }
    }

    /**
     * Genera una fuente de corriente, con su respectivo alambre auxiliar y red
     *
     * @param wire Alambre donde se sitúa la fuente de corriente
     * @return Fuente de corriente de amplitud unitaria
     */
    public Source generateCurrentSource(Wire wire) {

        int sourceCount = 0;
        int wireISource = global.getgWires().size() + 1;
        global.setCurrentSourceTag(wireISource);

        Source source = new Source();
        source.setSourceTypeId(0);
        source.setSourceSeg(wireISource);
        source.setPercentage(100);
        source.setSegPercentage(sourceCount + 1);
        source.setI4(0);
        double amplitudREValue = 1 * Math.sqrt(2) * Math.cos(Math.toRadians(0));
        source.setSourceAmplitudeRE(-1 * amplitudREValue);
        double amplitudIMValue = (1 * Math.sqrt(2) * Math.sin(Math.toRadians(0)));
        if (amplitudIMValue == 0) {
            source.setSourceAmplitudeIM(1E-15);
        } else {
            source.setSourceAmplitudeIM(amplitudIMValue);
        }
        global.getgSource().add(source);
        double wl = global.getWavelength();

        Wire iSrcWire = null;
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

        Network network = new Network();
        int wireTag = wire.getNumber();
        network.setTagNumberPort1(global.getCurrentSourceTag());
        network.setPort1Segment(sourceCount + 1);
        network.setTagNumberPort2(wireTag);
        long segPercentage = Math.round(global.getgWires().get(wireTag - 1).getSegs() * (0.5));
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
     * Genera un dipolo de Lambda / 2 a partir del valor de la longitud de onda
     * y razón LD
     *
     * @param wl Valor de la longitud de onda (en metros)
     * @param ld Razón LD
     * @return Dipolo de Lambda / 2 con razón LD determinada
     */
    public Wire generateDipole(double wl, double ld) {
        Wire dipole = new Wire();
        double l = wl / 2;
        dipole.setX1(Global.decimalFormat(0));
        dipole.setY1(Global.decimalFormat(0));
        dipole.setZ1(Global.decimalFormat(l / 2));
        dipole.setX2(Global.decimalFormat(0));
        dipole.setY2(Global.decimalFormat(0));
        dipole.setZ2(Global.decimalFormat(-l / 2));
        int segs = 0;
        while (segs == 0) {
            segs = generateSegs();
        }
        dipole.setSegs(segs);
        double length = wl / 2;
        double d = length / ld;
        dipole.setRadius(Global.decimalFormat(d));
        dipole.setNumber(global.getgWires().size() + 1);
        return dipole;
    }

    /**
     * Verifica la validez de los valores introducidos en el apartado SWR del
     * experimento 1
     *
     * @return true si los valores introducidos son correctos, de lo contrario,
     * devuelve false
     */
    public boolean validateSWRInput() {
        boolean condition1, condition2, condition3, condition4;

        condition1 = false;
        condition2 = false;
        condition3 = false;
        condition4 = false;

        if (Double.valueOf(optFreq.getText()) != global.getgFrequency().getFreq()) {
            global.getgFrequency().setFreq(Double.valueOf(optFreq.getText()));
        }

        if (times.isSelected()) {
            setPlotType(Global.PLOTTIMES);
        } else {
            setPlotType(Global.PLOTDB);
        }

        condition1 = (!initFreq.getText().isEmpty())
                && (!stepFreq.getText().isEmpty())
                && (!finalFreq.getText().isEmpty());
        if (condition1) {
            condition2 = (Double.valueOf(initFreq.getText().replace(",", ".")) > 0
                    && (Double.valueOf(finalFreq.getText().replace(",", ".")) > 0)
                    && (Double.valueOf(stepFreq.getText().replace(",", ".")) > 0));
            condition3 = (Double.valueOf(initFreq.getText().replace(",", "."))) < (Double.valueOf(finalFreq.getText().replace(",", ".")));
            condition4 = Double.valueOf(stepFreq.getText().replace(",", ".")) > 0;
        } else {
            global.errorValidateInput();
            return false;
        }
        return condition1 && condition2 && condition3 && condition4;
    }

    /**
     * Verifica la validez de los valores introducidos en el apartado SWR del
     * experimento 2
     *
     * @return true si los valores introducidos son correctos, de lo contrario,
     * devuelve false
     */
    public boolean validateSWRInput1() {
        boolean condition1, condition2, condition3, condition4;

        condition1 = false;
        condition2 = false;
        condition3 = false;
        condition4 = false;

        if (Double.valueOf(optFreq1.getText()) != global.getgFrequency().getFreq()) {
            global.getgFrequency().setFreq(Double.valueOf(optFreq1.getText()));
        }

        if (times1.isSelected()) {
            setPlotType(Global.PLOTTIMES);
        } else {
            setPlotType(Global.PLOTDB);
        }

        condition1 = (!initFreq1.getText().isEmpty())
                && (!stepFreq1.getText().isEmpty())
                && (!finalFreq1.getText().isEmpty());
        if (condition1) {
            condition2 = (Double.valueOf(initFreq1.getText().replace(",", ".")) > 0
                    && (Double.valueOf(finalFreq1.getText().replace(",", ".")) > 0)
                    && (Double.valueOf(stepFreq1.getText().replace(",", ".")) > 0));
            condition3 = (Double.valueOf(initFreq1.getText().replace(",", "."))) < (Double.valueOf(finalFreq1.getText().replace(",", ".")));
            condition4 = Double.valueOf(stepFreq1.getText().replace(",", ".")) >= 0;
        } else {
            global.errorValidateInput();
            return false;
        }
        return condition1 && condition2 && condition3 && condition4;
    }

    /**
     * Genera un objeto SWR a partir de los valores introducidos en el apartado
     * SWR en el experimento 1
     *
     * @return Objeto de la clase SWR
     */
    public SWR addSWR() {
        SWR swr = new SWR();
        swr.setUseAltZ0(alterz0.isSelected());
        swr.setAltZ0(global.getAlterZ0());
        swr.setSrcIndex(Integer.valueOf(selectSrc.getSelectedItem() + ""));
        swr.setInitFreq(Double.valueOf((initFreq.getText()).replace(",", ".")));
        swr.setFinalFreq(Double.valueOf((finalFreq.getText()).replace(",", ".")));
        int step = 0;
        double fin = Double.valueOf(finalFreq.getText().replace(",", "."));
        double initial = Double.valueOf(initFreq.getText().replace(",", "."));
        double st = Double.valueOf(stepFreq.getText().replace(",", "."));
        swr.setStepFreq(st);
        return swr;
    }

    /**
     **Genera un objeto SWR a partir de los valores introducidos en el apartado
     * SWR en el experimento 2
     *
     * @return Objeto de la clase SWR
     */
    public SWR addSWR1() {
        SWR swr = new SWR();
        swr.setUseAltZ0(alterz1.isSelected());
        swr.setAltZ0(global.getAlterZ0());
        swr.setSrcIndex(Integer.valueOf(selectSrc1.getSelectedItem() + ""));
        swr.setInitFreq(Double.valueOf((initFreq1.getText()).replace(",", ".")));
        swr.setFinalFreq(Double.valueOf((finalFreq1.getText()).replace(",", ".")));
        double st = Double.valueOf(stepFreq1.getText().replace(",", "."));
        swr.setStepFreq(st);
        return swr;
    }

    /**
     * Crea un dipolo a partir de los parámetros introducidos en los controles
     * del panel
     *
     * @return true si la generación fue exitosa, en caso contrario, devuelve
     * false
     */
    public boolean createDipole() {
        if (!ldvalue.getText().isEmpty()) {
            if (isFirstExecution()) {
                global.getgWires().clear();
                global.getgSource().clear();
                global.getgNetwork().clear();
                double wl = 0;
                double f = 0;
                if (global.getgFrequency().getFreq() != 0.0) {
                    wl = global.getWavelength();
                } else {
                    while (f == 0) {
                        f = generateFrequency();
                    }
                    wl = global.getWavelength();
                }
                double ld = Double.valueOf(ldvalue.getText().replace(",", "."));
                currentldlbl.setText(Global.integerFormat(ld) + "");
                Wire dipole = generateDipole(wl, ld);
                global.getgWires().add(dipole);
                setLab11WireTag(dipole.getNumber());
                generateCurrentSource(dipole);
                setFirstExecution(false);
            } else {
                double ld = Double.valueOf(ldvalue.getText().replace(",", "."));
                currentldlbl.setText(Global.integerFormat(ld) + "");
                double length = global.getWavelength() / 2;
                double d = length / ld;
                int wireTag = getLab11WireTag();
                global.getgWires().get(wireTag - 1).setRadius(d);
            }
            if (showAnt.isSelected()) {
                global.updatePlot(global);
            }

            if (MetaGlobal.getNmp() != null) {
                MetaGlobal.getNmp().initializeInfo(global);
            }
            return true;
        } else {
            global.errorValidateInput();
            return false;
        }
    }

    /**
     * Escala un dipolo según la razón especificada en el control
     * correspondiente del panel del experimento
     *
     * @return true si el escalamiento del dipolo fue exitoso, de lo contrario,
     * devuelve false
     */
    public boolean scaleDipole() {
        double scale = Double.valueOf(scalevalue.getText().replace(",", "."));
        currentScalelbl1.setText(scale + "");
        if (!scalevalue.getText().isEmpty()) {
            if (isFirstExecutionLab12()) {
                global.getgWires().clear();
                global.getgSource().clear();
                global.getgNetwork().clear();
                double wl = 0;
                double f = 0;
                if (global.getgFrequency().getFreq() != 0.0) {
                    wl = global.getWavelength();
                } else {
                    while (f == 0) {
                        f = generateFrequency();
                    }
                    wl = global.getWavelength();
                }

                Wire dipole = generateDipole(wl, 100);
                lab2OriginalWire = dipole;
                global.getgWires().add(dipole);
                global.setgScaleFactor(1);
                setLab11WireTag(dipole.getNumber());
                generateCurrentSource(dipole);
                setFirstExecutionLab12(false);
            } else {
                Wire scaledWire = global.scale(lab2OriginalWire, scale, true);
                global.getgWires().set(0, scaledWire);
            }
            if (showAnt1.isSelected()) {
                global.updatePlot(global);
            }
            if (MetaGlobal.getNmp() != null) {
                MetaGlobal.getNmp().initializeInfo(global);
            }
            return true;
        } else {
            global.errorValidateInput();
            return false;
        }
    }

    /**
     * Traslada un dipolo en dirección Z según el factor especificado en el
     * control correspondiente dentro del panel del experimento
     *
     * @return true si el traslado del dipolo fue exitoso, de lo contrario,
     * devuelve false
     */
    public boolean moveDipole() {
        double h = 0;
        double f = 0;
        if (!heightvalue.getText().isEmpty() && global.getgWires().size() > 0) {
            if (global.getgFrequency().getFreq() != 0.0) {
                f = global.getgFrequency().getFreq();
            } else {
                while (f == 0) {
                    f = generateFrequency();
                }
            }
            h = Double.valueOf(heightvalue.getText().replace(",", "."));
            currentHeightlbl.setText(h + "");
            for (Wire wire : global.getgWires()) {
                if (wire.getNumber() != global.getCurrentSourceTag()) {
                    wire.setZ1(wire.getZ1() + (h * global.getWavelength()));
                    wire.setZ2(wire.getZ2() + (h * global.getWavelength()));
                }
            }
            return true;
        } else {
            global.errorValidateInput();;
            return false;
        }
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lab1Instruction = new javax.swing.JButton();
        lab1GoNEC = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        lab1AddResult = new javax.swing.JButton();
        lab1DeleteResult = new javax.swing.JButton();
        lab1ExportResult = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        currentldlbl = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        ldvalue = new javax.swing.JFormattedTextField();
        jPanel60 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel63 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        showAnt = new javax.swing.JCheckBox();
        jPanel64 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        jPanel55 = new javax.swing.JPanel();
        optFreq_lbl = new javax.swing.JLabel();
        optFreq = new javax.swing.JFormattedTextField();
        initFreq_lbl = new javax.swing.JLabel();
        initFreq = new javax.swing.JFormattedTextField();
        finalFreq_lbl = new javax.swing.JLabel();
        finalFreq = new javax.swing.JFormattedTextField();
        stepFreq_lbl = new javax.swing.JLabel();
        stepFreq = new javax.swing.JFormattedTextField();
        selectSrcLbl = new javax.swing.JLabel();
        selectSrc = new javax.swing.JComboBox<>();
        alterz0lbl = new javax.swing.JLabel();
        alterz0 = new javax.swing.JCheckBox();
        serielbl = new javax.swing.JLabel();
        jPanel56 = new javax.swing.JPanel();
        times = new javax.swing.JRadioButton();
        db = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lab1Instruction1 = new javax.swing.JButton();
        lab1GoNEC1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        resultTable1 = new javax.swing.JTable();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        currentScalelbl1 = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        scalevalue = new javax.swing.JFormattedTextField();
        jPanel61 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jPanel66 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        showAnt1 = new javax.swing.JCheckBox();
        jPanel68 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        jPanel57 = new javax.swing.JPanel();
        optFreq_lbl1 = new javax.swing.JLabel();
        optFreq1 = new javax.swing.JFormattedTextField();
        initFreq_lbl1 = new javax.swing.JLabel();
        initFreq1 = new javax.swing.JFormattedTextField();
        finalFreq_lbl1 = new javax.swing.JLabel();
        finalFreq1 = new javax.swing.JFormattedTextField();
        stepFreq_lbl1 = new javax.swing.JLabel();
        stepFreq1 = new javax.swing.JFormattedTextField();
        selectSrcLbl1 = new javax.swing.JLabel();
        selectSrc1 = new javax.swing.JComboBox<>();
        alterz0lbl1 = new javax.swing.JLabel();
        alterz1 = new javax.swing.JCheckBox();
        serielbl1 = new javax.swing.JLabel();
        jPanel58 = new javax.swing.JPanel();
        times1 = new javax.swing.JRadioButton();
        db1 = new javax.swing.JRadioButton();
        jPanel11 = new javax.swing.JPanel();
        lab1AddResult1 = new javax.swing.JButton();
        lab1DeleteResult1 = new javax.swing.JButton();
        lab1ExportResult1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        lab1Instruction2 = new javax.swing.JButton();
        lab1GoNEC2 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        resultTable2 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ant1From = new javax.swing.JComboBox<>();
        jPanel20 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ant1To = new javax.swing.JComboBox<>();
        jPanel21 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        ant2From = new javax.swing.JComboBox<>();
        jPanel24 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        ant2To = new javax.swing.JComboBox<>();
        jPanel17 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        distance = new javax.swing.JFormattedTextField();
        unit = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        planeX = new javax.swing.JRadioButton();
        planeY = new javax.swing.JRadioButton();
        planeZ = new javax.swing.JRadioButton();
        jPanel28 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        abp1Re = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        abp1Im = new javax.swing.JFormattedTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        abp2Re = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        abp2Im = new javax.swing.JFormattedTextField();
        jPanel34 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        z2Re = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        z2Im = new javax.swing.JFormattedTextField();
        jPanel35 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        z12Re = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        z12Im = new javax.swing.JFormattedTextField();
        jPanel36 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        i1Mag = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        i1Phase = new javax.swing.JFormattedTextField();
        jPanel37 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        i2Mag = new javax.swing.JFormattedTextField();
        jLabel21 = new javax.swing.JLabel();
        i2Phase = new javax.swing.JFormattedTextField();
        jPanel38 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        zeMag = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        zePhase = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        coupling = new javax.swing.JLabel();
        calcZbtn = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        lab1AddResult2 = new javax.swing.JButton();
        lab1DeleteResult2 = new javax.swing.JButton();
        lab1ExportResult2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        lab1Instruction3 = new javax.swing.JButton();
        lab1GoNEC3 = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        resultTable3 = new javax.swing.JTable();
        jPanel50 = new javax.swing.JPanel();
        jPanel69 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPanel70 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel71 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel72 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel73 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel51 = new javax.swing.JPanel();
        jPanel52 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        currentHeightlbl = new javax.swing.JLabel();
        jPanel54 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        heightvalue = new javax.swing.JFormattedTextField();
        jPanel62 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jPanel67 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        showAnt2 = new javax.swing.JCheckBox();
        jPanel65 = new javax.swing.JPanel();
        lab1AddResult3 = new javax.swing.JButton();
        lab1DeleteResult3 = new javax.swing.JButton();
        lab1ExportResult3 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(350, 600));
        jTabbedPane1.setOpaque(true);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(800, 600));

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
        jPanel6.setPreferredSize(new java.awt.Dimension(600, 413));
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
        resultTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        resultTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(resultTable);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

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

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setPreferredSize(new java.awt.Dimension(350, 424));
        jPanel29.setLayout(new java.awt.GridLayout(3, 1));

        jPanel41.setBackground(new java.awt.Color(255, 255, 255));
        jPanel41.setForeground(new java.awt.Color(0, 0, 0));
        jPanel41.setLayout(new java.awt.BorderLayout());

        jPanel42.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel42.setLayout(new java.awt.GridLayout(3, 1));

        jLabel9.setBackground(new java.awt.Color(36, 113, 163));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Dipolo Lambda / 2");
        jLabel9.setOpaque(true);
        jPanel42.add(jLabel9);

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setLayout(new java.awt.BorderLayout());

        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("L/D Actual");
        jLabel24.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel43.add(jLabel24, java.awt.BorderLayout.WEST);

        currentldlbl.setForeground(new java.awt.Color(0, 0, 0));
        currentldlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentldlbl.setText("---");
        jPanel43.add(currentldlbl, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel43);

        jPanel44.setLayout(new java.awt.BorderLayout());

        jLabel26.setBackground(new java.awt.Color(255, 255, 255));
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("L/D");
        jLabel26.setOpaque(true);
        jLabel26.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel44.add(jLabel26, java.awt.BorderLayout.WEST);

        ldvalue.setBackground(new java.awt.Color(255, 255, 255));
        ldvalue.setForeground(new java.awt.Color(0, 0, 0));
        ldvalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        ldvalue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel44.add(ldvalue, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel44);

        jPanel41.add(jPanel42, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel41);

        jPanel60.setLayout(new java.awt.GridLayout(2, 1));

        jLabel35.setBackground(new java.awt.Color(36, 113, 163));
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Opciones de Simulación");
        jLabel35.setOpaque(true);
        jPanel60.add(jLabel35);

        jPanel63.setBackground(new java.awt.Color(255, 255, 255));
        jPanel63.setLayout(new java.awt.BorderLayout());

        jLabel39.setBackground(new java.awt.Color(255, 255, 255));
        jLabel39.setForeground(new java.awt.Color(0, 0, 0));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Mostrar Antena");
        jLabel39.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel63.add(jLabel39, java.awt.BorderLayout.WEST);

        showAnt.setBackground(new java.awt.Color(255, 255, 255));
        showAnt.setForeground(new java.awt.Color(0, 0, 0));
        showAnt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel63.add(showAnt, java.awt.BorderLayout.CENTER);

        jPanel60.add(jPanel63);

        jPanel29.add(jPanel60);

        jPanel64.setLayout(new java.awt.BorderLayout());

        jLabel38.setBackground(new java.awt.Color(36, 113, 163));
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("ROE");
        jLabel38.setOpaque(true);
        jPanel64.add(jLabel38, java.awt.BorderLayout.NORTH);

        jPanel55.setBackground(new java.awt.Color(255, 255, 255));
        jPanel55.setLayout(new java.awt.GridLayout(7, 2));

        optFreq_lbl.setBackground(new java.awt.Color(255, 255, 255));
        optFreq_lbl.setForeground(new java.awt.Color(0, 0, 0));
        optFreq_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        optFreq_lbl.setText("Frecuencia Operación (MHz)");
        jPanel55.add(optFreq_lbl);

        optFreq.setBackground(new java.awt.Color(255, 255, 255));
        optFreq.setForeground(new java.awt.Color(0, 0, 0));
        optFreq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        optFreq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel55.add(optFreq);

        initFreq_lbl.setBackground(new java.awt.Color(255, 255, 255));
        initFreq_lbl.setForeground(new java.awt.Color(0, 0, 0));
        initFreq_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        initFreq_lbl.setText("Frecuencia Inicial (MHz)");
        jPanel55.add(initFreq_lbl);

        initFreq.setBackground(new java.awt.Color(255, 255, 255));
        initFreq.setForeground(new java.awt.Color(0, 0, 0));
        initFreq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        initFreq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel55.add(initFreq);

        finalFreq_lbl.setForeground(new java.awt.Color(0, 0, 0));
        finalFreq_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        finalFreq_lbl.setText("Frecuencia Final (MHz)");
        jPanel55.add(finalFreq_lbl);

        finalFreq.setBackground(new java.awt.Color(255, 255, 255));
        finalFreq.setForeground(new java.awt.Color(0, 0, 0));
        finalFreq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        finalFreq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel55.add(finalFreq);

        stepFreq_lbl.setBackground(new java.awt.Color(255, 255, 255));
        stepFreq_lbl.setForeground(new java.awt.Color(0, 0, 0));
        stepFreq_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stepFreq_lbl.setText("Paso (MHz)");
        jPanel55.add(stepFreq_lbl);

        stepFreq.setBackground(new java.awt.Color(255, 255, 255));
        stepFreq.setForeground(new java.awt.Color(0, 0, 0));
        stepFreq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        stepFreq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel55.add(stepFreq);

        selectSrcLbl.setBackground(new java.awt.Color(255, 255, 255));
        selectSrcLbl.setForeground(new java.awt.Color(0, 0, 0));
        selectSrcLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectSrcLbl.setText("Fuente");
        jPanel55.add(selectSrcLbl);

        selectSrc.setBackground(new java.awt.Color(255, 255, 255));
        selectSrc.setForeground(new java.awt.Color(0, 0, 0));
        selectSrc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));
        selectSrc.setBorder(null);
        jPanel55.add(selectSrc);

        alterz0lbl.setBackground(new java.awt.Color(255, 255, 255));
        alterz0lbl.setForeground(new java.awt.Color(0, 0, 0));
        alterz0lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alterz0lbl.setText("Usar Z0 alternativa");
        jPanel55.add(alterz0lbl);

        alterz0.setBackground(new java.awt.Color(255, 255, 255));
        alterz0.setForeground(new java.awt.Color(0, 0, 0));
        alterz0.setText("Z0 alternativa");
        jPanel55.add(alterz0);

        serielbl.setBackground(new java.awt.Color(255, 255, 255));
        serielbl.setForeground(new java.awt.Color(0, 0, 0));
        serielbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serielbl.setText("Mostrar en");
        jPanel55.add(serielbl);

        jPanel56.setLayout(new java.awt.GridLayout(1, 2));

        times.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(times);
        times.setForeground(new java.awt.Color(0, 0, 0));
        times.setSelected(true);
        times.setText("Veces");
        jPanel56.add(times);

        db.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(db);
        db.setForeground(new java.awt.Color(0, 0, 0));
        db.setText("dB");
        jPanel56.add(db);

        jPanel55.add(jPanel56);

        jPanel64.add(jPanel55, java.awt.BorderLayout.CENTER);

        jPanel29.add(jPanel64);

        jPanel1.add(jPanel29, java.awt.BorderLayout.WEST);

        jTabbedPane1.addTab("Experimento 1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(36, 113, 163));
        jPanel9.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel9.setLayout(new java.awt.GridLayout(1, 0));

        lab1Instruction1.setBackground(new java.awt.Color(36, 113, 163));
        lab1Instruction1.setForeground(new java.awt.Color(255, 255, 255));
        lab1Instruction1.setText("Instrucciones");
        lab1Instruction1.setBorder(null);
        lab1Instruction1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1Instruction1ActionPerformed(evt);
            }
        });
        jPanel9.add(lab1Instruction1);

        lab1GoNEC1.setBackground(new java.awt.Color(36, 113, 163));
        lab1GoNEC1.setForeground(new java.awt.Color(255, 255, 255));
        lab1GoNEC1.setText("Ir al Simulador");
        lab1GoNEC1.setBorder(null);
        lab1GoNEC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1GoNEC1ActionPerformed(evt);
            }
        });
        jPanel9.add(lab1GoNEC1);

        jPanel2.add(jPanel9, java.awt.BorderLayout.NORTH);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.BorderLayout());

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        resultTable1.setBackground(new java.awt.Color(255, 255, 255));
        resultTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        resultTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        resultTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(resultTable1);

        jPanel10.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setPreferredSize(new java.awt.Dimension(350, 424));
        jPanel45.setLayout(new java.awt.GridLayout(3, 1));

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));
        jPanel46.setForeground(new java.awt.Color(0, 0, 0));
        jPanel46.setLayout(new java.awt.BorderLayout());

        jPanel47.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel47.setLayout(new java.awt.GridLayout(3, 1));

        jLabel25.setBackground(new java.awt.Color(36, 113, 163));
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Dipolo Lambda / 2");
        jLabel25.setOpaque(true);
        jPanel47.add(jLabel25);

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setLayout(new java.awt.BorderLayout());

        jLabel27.setForeground(new java.awt.Color(0, 0, 0));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("Escala Actual");
        jLabel27.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel48.add(jLabel27, java.awt.BorderLayout.WEST);

        currentScalelbl1.setForeground(new java.awt.Color(0, 0, 0));
        currentScalelbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentScalelbl1.setText("---");
        jPanel48.add(currentScalelbl1, java.awt.BorderLayout.CENTER);

        jPanel47.add(jPanel48);

        jPanel49.setLayout(new java.awt.BorderLayout());

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("Escala");
        jLabel28.setOpaque(true);
        jLabel28.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel49.add(jLabel28, java.awt.BorderLayout.WEST);

        scalevalue.setBackground(new java.awt.Color(255, 255, 255));
        scalevalue.setForeground(new java.awt.Color(0, 0, 0));
        scalevalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        scalevalue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel49.add(scalevalue, java.awt.BorderLayout.CENTER);

        jPanel47.add(jPanel49);

        jPanel46.add(jPanel47, java.awt.BorderLayout.CENTER);

        jPanel45.add(jPanel46);

        jPanel61.setLayout(new java.awt.GridLayout(2, 1));

        jLabel36.setBackground(new java.awt.Color(36, 113, 163));
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Opciones de Simulación");
        jLabel36.setOpaque(true);
        jPanel61.add(jLabel36);

        jPanel66.setBackground(new java.awt.Color(255, 255, 255));
        jPanel66.setLayout(new java.awt.BorderLayout());

        jLabel41.setBackground(new java.awt.Color(255, 255, 255));
        jLabel41.setForeground(new java.awt.Color(0, 0, 0));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Mostrar Antena");
        jLabel41.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel66.add(jLabel41, java.awt.BorderLayout.WEST);

        showAnt1.setBackground(new java.awt.Color(255, 255, 255));
        showAnt1.setForeground(new java.awt.Color(0, 0, 0));
        showAnt1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel66.add(showAnt1, java.awt.BorderLayout.CENTER);

        jPanel61.add(jPanel66);

        jPanel45.add(jPanel61);

        jPanel68.setLayout(new java.awt.BorderLayout());

        jLabel40.setBackground(new java.awt.Color(36, 113, 163));
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("ROE");
        jLabel40.setOpaque(true);
        jPanel68.add(jLabel40, java.awt.BorderLayout.NORTH);

        jPanel57.setBackground(new java.awt.Color(255, 255, 255));
        jPanel57.setLayout(new java.awt.GridLayout(7, 2));

        optFreq_lbl1.setBackground(new java.awt.Color(255, 255, 255));
        optFreq_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        optFreq_lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        optFreq_lbl1.setText("Frecuencia Operación (MHz)");
        jPanel57.add(optFreq_lbl1);

        optFreq1.setBackground(new java.awt.Color(255, 255, 255));
        optFreq1.setForeground(new java.awt.Color(0, 0, 0));
        optFreq1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        optFreq1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel57.add(optFreq1);

        initFreq_lbl1.setBackground(new java.awt.Color(255, 255, 255));
        initFreq_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        initFreq_lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        initFreq_lbl1.setText("Frecuencia Inicial (MHz)");
        jPanel57.add(initFreq_lbl1);

        initFreq1.setBackground(new java.awt.Color(255, 255, 255));
        initFreq1.setForeground(new java.awt.Color(0, 0, 0));
        initFreq1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        initFreq1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel57.add(initFreq1);

        finalFreq_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        finalFreq_lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        finalFreq_lbl1.setText("Frecuencia Final (MHz)");
        jPanel57.add(finalFreq_lbl1);

        finalFreq1.setBackground(new java.awt.Color(255, 255, 255));
        finalFreq1.setForeground(new java.awt.Color(0, 0, 0));
        finalFreq1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        finalFreq1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel57.add(finalFreq1);

        stepFreq_lbl1.setBackground(new java.awt.Color(255, 255, 255));
        stepFreq_lbl1.setForeground(new java.awt.Color(0, 0, 0));
        stepFreq_lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stepFreq_lbl1.setText("Paso (MHz)");
        jPanel57.add(stepFreq_lbl1);

        stepFreq1.setBackground(new java.awt.Color(255, 255, 255));
        stepFreq1.setForeground(new java.awt.Color(0, 0, 0));
        stepFreq1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        stepFreq1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel57.add(stepFreq1);

        selectSrcLbl1.setBackground(new java.awt.Color(255, 255, 255));
        selectSrcLbl1.setForeground(new java.awt.Color(0, 0, 0));
        selectSrcLbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectSrcLbl1.setText("Fuente");
        jPanel57.add(selectSrcLbl1);

        selectSrc1.setBackground(new java.awt.Color(255, 255, 255));
        selectSrc1.setForeground(new java.awt.Color(0, 0, 0));
        selectSrc1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1" }));
        selectSrc1.setBorder(null);
        jPanel57.add(selectSrc1);

        alterz0lbl1.setBackground(new java.awt.Color(255, 255, 255));
        alterz0lbl1.setForeground(new java.awt.Color(0, 0, 0));
        alterz0lbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alterz0lbl1.setText("Usar Z0 alternativa");
        jPanel57.add(alterz0lbl1);

        alterz1.setBackground(new java.awt.Color(255, 255, 255));
        alterz1.setForeground(new java.awt.Color(0, 0, 0));
        alterz1.setText("Z0 alternativa");
        jPanel57.add(alterz1);

        serielbl1.setBackground(new java.awt.Color(255, 255, 255));
        serielbl1.setForeground(new java.awt.Color(0, 0, 0));
        serielbl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serielbl1.setText("Mostrar en");
        jPanel57.add(serielbl1);

        jPanel58.setLayout(new java.awt.GridLayout(1, 2));

        times1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(times1);
        times1.setForeground(new java.awt.Color(0, 0, 0));
        times1.setText("Veces");
        jPanel58.add(times1);

        db1.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(db1);
        db1.setForeground(new java.awt.Color(0, 0, 0));
        db1.setText("dB");
        jPanel58.add(db1);

        jPanel57.add(jPanel58);

        jPanel68.add(jPanel57, java.awt.BorderLayout.CENTER);

        jPanel45.add(jPanel68);

        jPanel10.add(jPanel45, java.awt.BorderLayout.WEST);

        jPanel2.add(jPanel10, java.awt.BorderLayout.CENTER);

        jPanel11.setBackground(new java.awt.Color(36, 113, 163));
        jPanel11.setPreferredSize(new java.awt.Dimension(498, 25));
        jPanel11.setLayout(new java.awt.GridLayout(1, 0));

        lab1AddResult1.setBackground(new java.awt.Color(36, 113, 163));
        lab1AddResult1.setForeground(new java.awt.Color(255, 255, 255));
        lab1AddResult1.setText("Agregar Resultado");
        lab1AddResult1.setBorder(null);
        lab1AddResult1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1AddResult1ActionPerformed(evt);
            }
        });
        jPanel11.add(lab1AddResult1);

        lab1DeleteResult1.setBackground(new java.awt.Color(36, 113, 163));
        lab1DeleteResult1.setForeground(new java.awt.Color(255, 255, 255));
        lab1DeleteResult1.setText("Eliminar Resultado");
        lab1DeleteResult1.setBorder(null);
        lab1DeleteResult1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1DeleteResult1ActionPerformed(evt);
            }
        });
        jPanel11.add(lab1DeleteResult1);

        lab1ExportResult1.setBackground(new java.awt.Color(36, 113, 163));
        lab1ExportResult1.setForeground(new java.awt.Color(255, 255, 255));
        lab1ExportResult1.setText("Exportar Resultados");
        lab1ExportResult1.setBorder(null);
        lab1ExportResult1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1ExportResult1ActionPerformed(evt);
            }
        });
        jPanel11.add(lab1ExportResult1);

        jPanel2.add(jPanel11, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Experimento 2", jPanel2);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel12.setBackground(new java.awt.Color(36, 113, 163));
        jPanel12.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel12.setLayout(new java.awt.GridLayout(1, 0));

        lab1Instruction2.setBackground(new java.awt.Color(36, 113, 163));
        lab1Instruction2.setForeground(new java.awt.Color(255, 255, 255));
        lab1Instruction2.setText("Instrucciones");
        lab1Instruction2.setBorder(null);
        lab1Instruction2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1Instruction2ActionPerformed(evt);
            }
        });
        jPanel12.add(lab1Instruction2);

        lab1GoNEC2.setBackground(new java.awt.Color(36, 113, 163));
        lab1GoNEC2.setForeground(new java.awt.Color(255, 255, 255));
        lab1GoNEC2.setText("Ir al Simulador");
        lab1GoNEC2.setBorder(null);
        lab1GoNEC2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1GoNEC2ActionPerformed(evt);
            }
        });
        jPanel12.add(lab1GoNEC2);

        jPanel3.add(jPanel12, java.awt.BorderLayout.NORTH);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setLayout(new java.awt.BorderLayout());

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        resultTable2.setBackground(new java.awt.Color(255, 255, 255));
        resultTable2.setForeground(new java.awt.Color(0, 0, 0));
        resultTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        resultTable2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        resultTable2.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(resultTable2);

        jPanel13.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(250, 424));
        jPanel8.setLayout(new java.awt.GridLayout(3, 1));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setForeground(new java.awt.Color(0, 0, 0));
        jPanel15.setLayout(new java.awt.GridLayout(2, 1));

        jLabel1.setBackground(new java.awt.Color(36, 113, 163));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Antena Bajo Prueba 1");
        jLabel1.setOpaque(true);
        jPanel15.add(jLabel1);

        jPanel18.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel18.setLayout(new java.awt.GridLayout(2, 1));

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

        jPanel15.add(jPanel18);

        jPanel8.add(jPanel15);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setForeground(new java.awt.Color(0, 0, 0));
        jPanel21.setLayout(new java.awt.GridLayout(2, 1));

        jLabel4.setBackground(new java.awt.Color(36, 113, 163));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Antena Bajo Prueba 2");
        jLabel4.setOpaque(true);
        jPanel21.add(jLabel4);

        jPanel22.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel22.setLayout(new java.awt.GridLayout(2, 1));

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

        jPanel22.add(jPanel23);

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

        jPanel22.add(jPanel24);

        jPanel21.add(jPanel22);

        jPanel8.add(jPanel21);

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setForeground(new java.awt.Color(0, 0, 0));
        jPanel17.setLayout(new java.awt.BorderLayout());

        jLabel7.setBackground(new java.awt.Color(36, 113, 163));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Trasladar");
        jLabel7.setOpaque(true);
        jPanel17.add(jLabel7, java.awt.BorderLayout.PAGE_START);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new java.awt.BorderLayout());

        jPanel25.setPreferredSize(new java.awt.Dimension(250, 50));
        jPanel25.setLayout(new java.awt.BorderLayout());

        distance.setBackground(new java.awt.Color(255, 255, 255));
        distance.setForeground(new java.awt.Color(0, 0, 0));
        distance.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        distance.setPreferredSize(new java.awt.Dimension(150, 20));
        jPanel25.add(distance, java.awt.BorderLayout.LINE_START);

        unit.setBackground(new java.awt.Color(255, 255, 255));
        unit.setForeground(new java.awt.Color(0, 0, 0));
        unit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        unit.setOpaque(true);
        jPanel25.add(unit, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel25, java.awt.BorderLayout.NORTH);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jLabel8.setBackground(new java.awt.Color(36, 113, 163));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Eje de Traslación");
        jLabel8.setOpaque(true);
        jPanel26.add(jLabel8, java.awt.BorderLayout.PAGE_START);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setLayout(new java.awt.GridLayout(1, 3));

        planeX.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(planeX);
        planeX.setForeground(new java.awt.Color(0, 0, 0));
        planeX.setText("X");
        jPanel27.add(planeX);

        planeY.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(planeY);
        planeY.setForeground(new java.awt.Color(0, 0, 0));
        planeY.setText("Y");
        planeY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                planeYActionPerformed(evt);
            }
        });
        jPanel27.add(planeY);

        planeZ.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(planeZ);
        planeZ.setForeground(new java.awt.Color(0, 0, 0));
        planeZ.setText("Z");
        jPanel27.add(planeZ);

        jPanel26.add(jPanel27, java.awt.BorderLayout.CENTER);

        jPanel16.add(jPanel26, java.awt.BorderLayout.CENTER);

        jPanel17.add(jPanel16, java.awt.BorderLayout.CENTER);

        jPanel8.add(jPanel17);

        jPanel13.add(jPanel8, java.awt.BorderLayout.WEST);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setPreferredSize(new java.awt.Dimension(250, 424));
        jPanel28.setLayout(new java.awt.BorderLayout());

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setPreferredSize(new java.awt.Dimension(200, 196));
        jPanel31.setLayout(new java.awt.GridLayout(9, 1));

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Impedancia Propia ABP1", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Re: ");
        jPanel32.add(jLabel10);

        abp1Re.setBackground(new java.awt.Color(255, 255, 255));
        abp1Re.setForeground(new java.awt.Color(0, 0, 0));
        abp1Re.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        abp1Re.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel32.add(abp1Re);

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Im:  +j");
        jPanel32.add(jLabel11);

        abp1Im.setBackground(new java.awt.Color(255, 255, 255));
        abp1Im.setForeground(new java.awt.Color(0, 0, 0));
        abp1Im.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        abp1Im.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel32.add(abp1Im);

        jPanel31.add(jPanel32);

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));
        jPanel33.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Impedancia Propia ABP2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N

        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Re: ");
        jPanel33.add(jLabel12);

        abp2Re.setForeground(new java.awt.Color(0, 0, 0));
        abp2Re.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        abp2Re.setOpaque(false);
        abp2Re.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel33.add(abp2Re);

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Im:  +j");
        jPanel33.add(jLabel13);

        abp2Im.setForeground(new java.awt.Color(0, 0, 0));
        abp2Im.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        abp2Im.setOpaque(false);
        abp2Im.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel33.add(abp2Im);

        jPanel31.add(jPanel33);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Impedancia de Z2", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N

        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("Re: ");
        jPanel34.add(jLabel14);

        z2Re.setForeground(new java.awt.Color(0, 0, 0));
        z2Re.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        z2Re.setOpaque(false);
        z2Re.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel34.add(z2Re);

        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Im:  +j");
        jPanel34.add(jLabel15);

        z2Im.setForeground(new java.awt.Color(0, 0, 0));
        z2Im.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        z2Im.setOpaque(false);
        z2Im.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel34.add(z2Im);

        jPanel31.add(jPanel34);

        jPanel35.setBackground(new java.awt.Color(255, 255, 255));
        jPanel35.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Impedancia Mutua Z12", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N

        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Re: ");
        jPanel35.add(jLabel16);

        z12Re.setForeground(new java.awt.Color(0, 0, 0));
        z12Re.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        z12Re.setOpaque(false);
        z12Re.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel35.add(z12Re);

        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Im:  +j");
        jPanel35.add(jLabel17);

        z12Im.setForeground(new java.awt.Color(0, 0, 0));
        z12Im.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        z12Im.setOpaque(false);
        z12Im.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel35.add(z12Im);

        jPanel31.add(jPanel35);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "I1 Mag / Fase (grados)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N

        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Mag:");
        jPanel36.add(jLabel18);

        i1Mag.setForeground(new java.awt.Color(0, 0, 0));
        i1Mag.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        i1Mag.setOpaque(false);
        i1Mag.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel36.add(i1Mag);

        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Fase: ");
        jPanel36.add(jLabel19);

        i1Phase.setForeground(new java.awt.Color(0, 0, 0));
        i1Phase.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        i1Phase.setOpaque(false);
        i1Phase.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel36.add(i1Phase);

        jPanel31.add(jPanel36);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "I2 Mag / Fase (grados)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N

        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("Mag: ");
        jPanel37.add(jLabel20);

        i2Mag.setForeground(new java.awt.Color(0, 0, 0));
        i2Mag.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        i2Mag.setOpaque(false);
        i2Mag.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel37.add(i2Mag);

        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("Fase: ");
        jPanel37.add(jLabel21);

        i2Phase.setForeground(new java.awt.Color(0, 0, 0));
        i2Phase.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        i2Phase.setOpaque(false);
        i2Phase.setPreferredSize(new java.awt.Dimension(50, 20));
        jPanel37.add(i2Phase);

        jPanel31.add(jPanel37);

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Impedancia de Entrada", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N
        jPanel38.setToolTipText("");

        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Mag: ");
        jPanel38.add(jLabel22);

        zeMag.setForeground(new java.awt.Color(0, 0, 0));
        zeMag.setText("---");
        jPanel38.add(zeMag);

        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Fase: ");
        jPanel38.add(jLabel23);

        zePhase.setForeground(new java.awt.Color(0, 0, 0));
        zePhase.setText("---");
        jPanel38.add(zePhase);

        jPanel31.add(jPanel38);

        jPanel39.setBackground(new java.awt.Color(255, 255, 255));
        jPanel39.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Grado de Acoplamiento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(36, 113, 163))); // NOI18N

        coupling.setForeground(new java.awt.Color(0, 0, 0));
        coupling.setText("---");
        jPanel39.add(coupling);

        jPanel31.add(jPanel39);

        calcZbtn.setBackground(new java.awt.Color(36, 113, 163));
        calcZbtn.setForeground(new java.awt.Color(255, 255, 255));
        calcZbtn.setText("Calcular");
        calcZbtn.setBorder(null);
        calcZbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcZbtnActionPerformed(evt);
            }
        });
        jPanel31.add(calcZbtn);

        jPanel28.add(jPanel31, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel28, java.awt.BorderLayout.EAST);

        jPanel3.add(jPanel13, java.awt.BorderLayout.CENTER);

        jPanel14.setBackground(new java.awt.Color(36, 113, 163));
        jPanel14.setPreferredSize(new java.awt.Dimension(498, 25));
        jPanel14.setLayout(new java.awt.GridLayout(1, 0));

        lab1AddResult2.setBackground(new java.awt.Color(36, 113, 163));
        lab1AddResult2.setForeground(new java.awt.Color(255, 255, 255));
        lab1AddResult2.setText("Agregar Resultado");
        lab1AddResult2.setBorder(null);
        lab1AddResult2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1AddResult2ActionPerformed(evt);
            }
        });
        jPanel14.add(lab1AddResult2);

        lab1DeleteResult2.setBackground(new java.awt.Color(36, 113, 163));
        lab1DeleteResult2.setForeground(new java.awt.Color(255, 255, 255));
        lab1DeleteResult2.setText("Eliminar Resultado");
        lab1DeleteResult2.setBorder(null);
        lab1DeleteResult2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1DeleteResult2ActionPerformed(evt);
            }
        });
        jPanel14.add(lab1DeleteResult2);

        lab1ExportResult2.setBackground(new java.awt.Color(36, 113, 163));
        lab1ExportResult2.setForeground(new java.awt.Color(255, 255, 255));
        lab1ExportResult2.setText("Exportar Resultados");
        lab1ExportResult2.setBorder(null);
        lab1ExportResult2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1ExportResult2ActionPerformed(evt);
            }
        });
        jPanel14.add(lab1ExportResult2);

        jPanel3.add(jPanel14, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Experimento 3", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel30.setBackground(new java.awt.Color(36, 113, 163));
        jPanel30.setPreferredSize(new java.awt.Dimension(0, 25));
        jPanel30.setLayout(new java.awt.GridLayout(1, 0));

        lab1Instruction3.setBackground(new java.awt.Color(36, 113, 163));
        lab1Instruction3.setForeground(new java.awt.Color(255, 255, 255));
        lab1Instruction3.setText("Instrucciones");
        lab1Instruction3.setBorder(null);
        lab1Instruction3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1Instruction3ActionPerformed(evt);
            }
        });
        jPanel30.add(lab1Instruction3);

        lab1GoNEC3.setBackground(new java.awt.Color(36, 113, 163));
        lab1GoNEC3.setForeground(new java.awt.Color(255, 255, 255));
        lab1GoNEC3.setText("Ir al Simulador");
        lab1GoNEC3.setBorder(null);
        lab1GoNEC3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1GoNEC3ActionPerformed(evt);
            }
        });
        jPanel30.add(lab1GoNEC3);

        jPanel4.add(jPanel30, java.awt.BorderLayout.NORTH);

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setBorder(null);
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        resultTable3.setBackground(new java.awt.Color(255, 255, 255));
        resultTable3.setForeground(new java.awt.Color(0, 0, 0));
        resultTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        resultTable3.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        resultTable3.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(resultTable3);

        jPanel40.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));
        jPanel50.setPreferredSize(new java.awt.Dimension(250, 424));
        jPanel50.setLayout(new java.awt.GridLayout(5, 1));

        jPanel69.setLayout(new java.awt.GridLayout(3, 1));

        jLabel43.setBackground(new java.awt.Color(36, 113, 163));
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("Antena Bajo Prueba");
        jLabel43.setOpaque(true);
        jPanel69.add(jLabel43);

        jPanel70.setBackground(new java.awt.Color(255, 255, 255));
        jPanel70.setLayout(new java.awt.BorderLayout());

        jLabel44.setBackground(new java.awt.Color(255, 255, 255));
        jLabel44.setForeground(new java.awt.Color(0, 0, 0));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("Cargar Antena");
        jLabel44.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel70.add(jLabel44, java.awt.BorderLayout.WEST);

        jButton1.setBackground(new java.awt.Color(36, 113, 163));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText(">");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel70.add(jButton1, java.awt.BorderLayout.CENTER);

        jPanel69.add(jPanel70);

        jPanel71.setBackground(new java.awt.Color(255, 255, 255));
        jPanel71.setLayout(new java.awt.BorderLayout());

        jLabel45.setBackground(new java.awt.Color(255, 255, 255));
        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Editar Antena");
        jLabel45.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel71.add(jLabel45, java.awt.BorderLayout.WEST);

        jButton2.setBackground(new java.awt.Color(36, 113, 163));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText(">");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel71.add(jButton2, java.awt.BorderLayout.CENTER);

        jPanel69.add(jPanel71);

        jPanel50.add(jPanel69);

        jPanel72.setLayout(new java.awt.GridLayout(2, 1));

        jLabel46.setBackground(new java.awt.Color(36, 113, 163));
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Configuración de Tierra");
        jLabel46.setOpaque(true);
        jPanel72.add(jLabel46);

        jPanel73.setBackground(new java.awt.Color(255, 255, 255));
        jPanel73.setLayout(new java.awt.BorderLayout());

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setForeground(new java.awt.Color(0, 0, 0));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("Editar Tierra");
        jLabel47.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel73.add(jLabel47, java.awt.BorderLayout.WEST);

        jButton3.setBackground(new java.awt.Color(36, 113, 163));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText(">");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel73.add(jButton3, java.awt.BorderLayout.CENTER);

        jPanel72.add(jPanel73);

        jPanel50.add(jPanel72);

        jPanel51.setBackground(new java.awt.Color(255, 255, 255));
        jPanel51.setForeground(new java.awt.Color(0, 0, 0));
        jPanel51.setLayout(new java.awt.BorderLayout());

        jPanel52.setPreferredSize(new java.awt.Dimension(250, 100));
        jPanel52.setLayout(new java.awt.GridLayout(3, 1));

        jLabel29.setBackground(new java.awt.Color(36, 113, 163));
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("Distancia con Respecto a la Tierra");
        jLabel29.setOpaque(true);
        jPanel52.add(jLabel29);

        jPanel53.setBackground(new java.awt.Color(255, 255, 255));
        jPanel53.setLayout(new java.awt.BorderLayout());

        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Factor Distancia Actual");
        jLabel30.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel53.add(jLabel30, java.awt.BorderLayout.WEST);

        currentHeightlbl.setForeground(new java.awt.Color(0, 0, 0));
        currentHeightlbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        currentHeightlbl.setText("---");
        jPanel53.add(currentHeightlbl, java.awt.BorderLayout.CENTER);

        jPanel52.add(jPanel53);

        jPanel54.setLayout(new java.awt.BorderLayout());

        jLabel31.setBackground(new java.awt.Color(255, 255, 255));
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel31.setText("Factor Distancia:  Lambda");
        jLabel31.setOpaque(true);
        jLabel31.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel54.add(jLabel31, java.awt.BorderLayout.WEST);

        heightvalue.setBackground(new java.awt.Color(255, 255, 255));
        heightvalue.setForeground(new java.awt.Color(0, 0, 0));
        heightvalue.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        heightvalue.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel54.add(heightvalue, java.awt.BorderLayout.CENTER);

        jPanel52.add(jPanel54);

        jPanel51.add(jPanel52, java.awt.BorderLayout.CENTER);

        jPanel50.add(jPanel51);

        jPanel62.setLayout(new java.awt.GridLayout(2, 1));

        jLabel37.setBackground(new java.awt.Color(36, 113, 163));
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Opciones de Simulación");
        jLabel37.setOpaque(true);
        jPanel62.add(jLabel37);

        jPanel67.setBackground(new java.awt.Color(255, 255, 255));
        jPanel67.setLayout(new java.awt.BorderLayout());

        jLabel42.setBackground(new java.awt.Color(255, 255, 255));
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Mostrar Antena");
        jLabel42.setPreferredSize(new java.awt.Dimension(150, 16));
        jPanel67.add(jLabel42, java.awt.BorderLayout.WEST);

        showAnt2.setBackground(new java.awt.Color(255, 255, 255));
        showAnt2.setForeground(new java.awt.Color(0, 0, 0));
        showAnt2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel67.add(showAnt2, java.awt.BorderLayout.CENTER);

        jPanel62.add(jPanel67);

        jPanel50.add(jPanel62);

        jPanel40.add(jPanel50, java.awt.BorderLayout.WEST);

        jPanel4.add(jPanel40, java.awt.BorderLayout.CENTER);

        jPanel65.setBackground(new java.awt.Color(36, 113, 163));
        jPanel65.setPreferredSize(new java.awt.Dimension(498, 25));
        jPanel65.setLayout(new java.awt.GridLayout(1, 0));

        lab1AddResult3.setBackground(new java.awt.Color(36, 113, 163));
        lab1AddResult3.setForeground(new java.awt.Color(255, 255, 255));
        lab1AddResult3.setText("Agregar Resultado");
        lab1AddResult3.setBorder(null);
        lab1AddResult3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1AddResult3ActionPerformed(evt);
            }
        });
        jPanel65.add(lab1AddResult3);

        lab1DeleteResult3.setBackground(new java.awt.Color(36, 113, 163));
        lab1DeleteResult3.setForeground(new java.awt.Color(255, 255, 255));
        lab1DeleteResult3.setText("Eliminar Resultado");
        lab1DeleteResult3.setBorder(null);
        lab1DeleteResult3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1DeleteResult3ActionPerformed(evt);
            }
        });
        jPanel65.add(lab1DeleteResult3);

        lab1ExportResult3.setBackground(new java.awt.Color(36, 113, 163));
        lab1ExportResult3.setForeground(new java.awt.Color(255, 255, 255));
        lab1ExportResult3.setText("Exportar Resultados");
        lab1ExportResult3.setBorder(null);
        lab1ExportResult3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lab1ExportResult3ActionPerformed(evt);
            }
        });
        jPanel65.add(lab1ExportResult3);

        jPanel4.add(jPanel65, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Experimento 4", jPanel4);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
//Comportamiento del botón Exportar Resultados del experimento 2
    private void lab1ExportResult1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1ExportResult1ActionPerformed
        global.exportResult(exportLab12(), Global.LAB12);
    }//GEN-LAST:event_lab1ExportResult1ActionPerformed
//Comportamiento del botón Eliminar Resultado del experimento 2
    private void lab1DeleteResult1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1DeleteResult1ActionPerformed
        int selected = resultTable1.getSelectedRow();
        if (selected != -1) {
            model1.removeRow(selected);
            global.getgLab1_2().remove(selected);
            model1.fireTableDataChanged();
        } else {
            global.errorMessage("Messages.noResultSelectedTitle", "Messages.noResultSelected");
        }
    }//GEN-LAST:event_lab1DeleteResult1ActionPerformed
//Comportamiento del botón Añadir Resultados del experimento 2
    private void lab1AddResult1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1AddResult1ActionPerformed
        if (!scalevalue.getText().isEmpty() && validateSWRInput1() && scaleDipole()) {
            runSimulation(false);
            global.setgSWR(addSWR1());
            global.executeSWR(global, getPlotType());
            Lab1_2 lab12 = new Lab1_2();
            lab12.setScale(global.getgScaleFactor());
            lab12.setLength(global.getgWires().get(0).getVector().getLength());
            lab12.setDiameter(global.getgWires().get(0).getRadius());
            double freq = global.getgFrequency().getFreq();
            int sourceId = global.getgSWR().getSrcIndex();
            double zaReal = ((global.getgSrcInfo().get(sourceId)).get(freq)).get(0).getImpedanceReal();
            double zaIm = ((global.getgSrcInfo().get(sourceId)).get(freq)).get(0).getImpedanceImaginary();
            Complex za = new Complex(zaReal, zaIm);
            lab12.setZa(za);
            if (global.getgSWR().getData().get(global.getgFrequency().getFreq()) != null) {
                lab12.setDesignSWR(Global.decimalFormat(global.getgSWR().getData().get(global.getgFrequency().getFreq())));
            } else {
                for (Map.Entry<Double, Double> entry : global.getgSWR().getData().entrySet()) {
                    Double key = entry.getKey();
                    Double value = entry.getValue();
                    if (key >= global.getgFrequency().getFreq()) {
                        lab12.setDesignSWR(Global.decimalFormat(value));
                        break;
                    }
                }
            }
            Object[] row = {lab12.getScale(),
                lab12.getLength(),
                (lab12.getDiameter()) * global.unit2UpperFactor(),
                lab12.getZa(),
                lab12.getDesignSWR()};
            model1.addRow(row);
            global.getgLab1_2().add(lab12);
            model1.fireTableDataChanged();
        } else {
            global.errorValidateInput();
        }

    }//GEN-LAST:event_lab1AddResult1ActionPerformed
//Comportamiento del botón Ir al Simulador del experimento 2
    private void lab1GoNEC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1GoNEC1ActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab1GoNEC1ActionPerformed
//Comportamiento del botón Exportar Resultado del experimento 1
    private void lab1ExportResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1ExportResultActionPerformed
        global.exportResult(exportLab11(), Global.LAB11);
    }//GEN-LAST:event_lab1ExportResultActionPerformed
//Comportamiento del botón Eliminar Resultado del experimento 1
    private void lab1DeleteResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1DeleteResultActionPerformed
        int selected = resultTable.getSelectedRow();
        if (selected != -1) {
            model.removeRow(selected);
            global.getgLab1_1().remove(selected);
            model.fireTableDataChanged();
        } else {
            global.errorMessage("Messages.noResultSelectedTitle", "Messages.noResultSelected");
        }
    }//GEN-LAST:event_lab1DeleteResultActionPerformed
//Comportamiento del botón Añadir Resultados del experimento 1
    private void lab1AddResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1AddResultActionPerformed
        if (!ldvalue.getText().isEmpty() && validateSWRInput() && createDipole()) {
            runSimulation(false);
            global.setgSWR(addSWR());
            global.executeSWR(global, getPlotType());
            Lab1_1 lab11 = new Lab1_1();
            lab11.setLd(Global.decimalFormat(global.getgWires().get(0).getVector().getLength() / global.getgWires().get(0).getRadius()));
            lab11.setDiameter(global.getgWires().get(0).getRadius());
            if (global.getgSWR().getData().get(global.getgFrequency().getFreq()) != null) {
                lab11.setDesignSWR(Global.decimalFormat(global.getgSWR().getData().get(global.getgFrequency().getFreq())));
            } else {
                for (Map.Entry<Double, Double> entry : global.getgSWR().getData().entrySet()) {
                    Double key = entry.getKey();
                    Double value = entry.getValue();
                    if (key >= global.getgFrequency().getFreq()) {
                        lab11.setDesignSWR(Global.decimalFormat(value));
                        break;
                    }
                }
            }

            lab11.setLowerSWR(Global.decimalFormat(global.getgSWR().getMinSWR().getValue()));
            lab11.setLowerSWRFreq(global.getgSWR().getMinSWR().getFrequency());
            int sourceId = global.getgSWR().getSrcIndex();
            double freq = global.getgFrequency().getFreq();
            double zaReal = ((global.getgSrcInfo().get(sourceId)).get(freq)).get(0).getImpedanceReal();
            double zaIm = ((global.getgSrcInfo().get(sourceId)).get(freq)).get(0).getImpedanceImaginary();
            Complex za = new Complex(zaReal, zaIm);
            lab11.setZa(za);
            lab11.setBw(Global.decimalFormat(global.getgSWR().getBw()));
            global.getgLab1_1().add(lab11);

            Object[] row = {lab11.getLd() + "",
                (lab11.getDiameter() * global.unit2UpperFactor()) + "",
                Global.decimalFormat(lab11.getLowerSWRFreq()) + "",
                lab11.getZa() + "",
                global.getgFrequency().getFreq() + "",
                Global.decimalFormat(lab11.getLowerSWR()) + "",
                Global.decimalFormat(lab11.getDesignSWR()) + "",
                lab11.getBw() + ""};
            model.addRow(row);
            model.fireTableDataChanged();
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_lab1AddResultActionPerformed
//Comportamiento del botón Ir al Simulador del experimento 1
    private void lab1GoNECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1GoNECActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab1GoNECActionPerformed
//Comportamiento del botón Ir al Simulador del experimento 3
    private void lab1GoNEC2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1GoNEC2ActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab1GoNEC2ActionPerformed
//Comportamiento del botón Añadir Resultados del experimento 3
    private void lab1AddResult2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1AddResult2ActionPerformed
        double d = 0;
        if (global.getgWires().size() > 0 && !distance.getText().isEmpty() && (Integer.valueOf(ant1From.getSelectedItem() + "") <= Integer.valueOf(ant1To.getSelectedItem() + "")) && (Integer.valueOf(ant2From.getSelectedItem() + "") <= Integer.valueOf(ant2From.getSelectedItem() + ""))) {
            ArrayList<Wire> ant1 = new ArrayList<Wire>();
            ArrayList<Wire> ant2 = new ArrayList<Wire>();
            int a1From = Integer.valueOf(ant1From.getSelectedItem() + "");
            int a1To = Integer.valueOf(ant1To.getSelectedItem() + "");
            int a2From = Integer.valueOf(ant2From.getSelectedItem() + "");
            int a2To = Integer.valueOf(ant2To.getSelectedItem() + "");

            if (!distance.getText().isEmpty()) {
                d = Double.valueOf(distance.getText().replace(",", "."));
            }
            int plane = 0;
            if (planeX.isSelected()) {
                plane = Global.XAXIS;
            } else if (planeY.isSelected()) {
                plane = Global.YAXIS;
            } else {
                plane = Global.ZAXIS;
            }

            for (int i = a1From; i <= a1To; i++) {
                ant1.add(global.getgWires().get(i - 1));
            }
            for (int i = a2From; i <= a2To; i++) {
                ant2.add(global.getgWires().get(i - 1));
            }

            double dist = d * global.convertUnits(global.getCurrentUnit(), Global.METER);
            switch (plane) {
                case Global.XAXIS:
                    for (Wire wire : ant2) {
                        wire.setX1(wire.getX1() + dist);
                        wire.setX2(wire.getX2() + dist);
                    }
                    break;
                case Global.YAXIS:
                    for (Wire wire : ant2) {
                        wire.setY1(wire.getY1() + dist);
                        wire.setY2(wire.getY2() + dist);
                    }
                    break;
                case Global.ZAXIS:
                    for (Wire wire : ant2) {
                        wire.setZ1(wire.getZ1() + dist);
                        wire.setZ2(wire.getZ2() + dist);
                    }
                    break;
                default:
                    throw new AssertionError();

            }
            runSimulation(false);
            global.updatePlot(global);

            Lab1_3 lab13 = new Lab1_3();

            lab13.setDistance(d);

            double freq = global.getgFrequency().getFreq();
            double vSrc1Re = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getVoltageReal();
            double vSrc1Im = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getVoltageImaginary();

            double iSrc1Re = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getCurrentReal();
            double iSrc1Im = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getCurrentImaginary();

            double zSrc1Re = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getImpedanceReal();
            double zSrc1Im = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getImpedanceImaginary();

            double vSrc2Re = (((global.getgSrcInfo().get(2)).get(freq)).get(0)).getVoltageReal();
            double vSrc2Im = ((global.getgSrcInfo().get(2)).get(freq)).get(0).getVoltageImaginary();

            double iSrc2Re = (((global.getgSrcInfo().get(2)).get(freq)).get(0)).getCurrentReal();
            double iSrc2Im = (((global.getgSrcInfo().get(2)).get(freq)).get(0)).getCurrentImaginary();

            double zSrc2Re = (((global.getgSrcInfo().get(2)).get(freq)).get(0)).getImpedanceReal();
            double zSrc2Im = (((global.getgSrcInfo().get(2)).get(freq)).get(0)).getImpedanceImaginary();

            if (vSrc1Re == vSrc2Re && iSrc1Re == iSrc2Re && zSrc1Re == zSrc2Re) {
                if ((global.getgSrcInfo().get(2)).get(freq).size() > 1) {
                    vSrc2Re = (((global.getgSrcInfo().get(2)).get(freq)).get(1)).getVoltageReal();
                    vSrc2Im = ((global.getgSrcInfo().get(2)).get(freq)).get(1).getVoltageImaginary();

                    iSrc2Re = (((global.getgSrcInfo().get(2)).get(freq)).get(1)).getCurrentReal();
                    iSrc2Im = (((global.getgSrcInfo().get(2)).get(freq)).get(1)).getCurrentImaginary();

                    zSrc2Re = (((global.getgSrcInfo().get(2)).get(freq)).get(1)).getImpedanceReal();
                    zSrc2Im = (((global.getgSrcInfo().get(2)).get(freq)).get(1)).getImpedanceImaginary();

                }
            }

            lab13.setvSrc1(new Complex(vSrc1Re, vSrc1Im));
            lab13.setiSrc1(new Complex(iSrc1Re, iSrc1Im));
            lab13.setzSrc1(new Complex(zSrc1Re, zSrc1Im));
            lab13.setvSrc2(new Complex(vSrc2Re, vSrc2Im));
            lab13.setiSrc2(new Complex(iSrc2Re, iSrc2Im));
            lab13.setzSrc2(new Complex(zSrc2Re, zSrc2Im));

            global.getgLab1_3().add(lab13);

            Object[] row = {lab13.getDistance() + "",
                lab13.getvSrc1() + "",
                lab13.getiSrc1() + "",
                lab13.getzSrc1() + "",
                lab13.getvSrc2() + "",
                lab13.getiSrc2() + "",
                lab13.getzSrc2()};
            model2.addRow(row);
            model2.fireTableDataChanged();
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_lab1AddResult2ActionPerformed
//Comportamiento del botón Eliminar Resultado del experimento 3
    private void lab1DeleteResult2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1DeleteResult2ActionPerformed
        int selected = resultTable2.getSelectedRow();
        if (selected != -1) {
            model2.removeRow(selected);
            global.getgLab1_3().remove(selected);
            model2.fireTableDataChanged();
        } else {
            global.errorMessage("Messages.noResultSelectedTitle", "Messages.noResultSelected");
        }
    }//GEN-LAST:event_lab1DeleteResult2ActionPerformed
//Comportamiento del botón Exportar Resultados del experimento 3
    private void lab1ExportResult2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1ExportResult2ActionPerformed
        global.exportResult(exportLab13(), Global.LAB13);
    }//GEN-LAST:event_lab1ExportResult2ActionPerformed

    private void planeYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_planeYActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_planeYActionPerformed
//Comportamiento del botón Calcular del experimento 3
    private void calcZbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcZbtnActionPerformed
        if (!abp1Re.getText().isEmpty()
                && !abp1Im.getText().isEmpty()
                && !abp2Re.getText().isEmpty()
                && !abp2Im.getText().isEmpty()
                && !z2Re.getText().isEmpty()
                && !z2Im.getText().isEmpty()
                && !z12Re.getText().isEmpty()
                && !z12Im.getText().isEmpty()
                && !i1Mag.getText().isEmpty()
                && !i1Phase.getText().isEmpty()
                && !i2Mag.getText().isEmpty()
                && !i2Phase.getText().isEmpty()) {

            Complex abp1 = new Complex(Double.valueOf(abp1Re.getText().replace(",", ".")), Double.valueOf(abp1Im.getText().replace(",", ".")));
            Complex abp2 = new Complex(Double.valueOf(abp2Re.getText().replace(",", ".")), Double.valueOf(abp2Im.getText().replace(",", ".")));
            Complex z12 = new Complex(Double.valueOf(z12Re.getText().replace(",", ".")), Double.valueOf(z12Im.getText().replace(",", ".")));
            Complex i1 = new Complex(Double.valueOf(i1Mag.getText().replace(",", ".")), Double.valueOf(i1Phase.getText().replace(",", ".")), false);
            Complex i2 = new Complex(Double.valueOf(i2Mag.getText().replace(",", ".")), Double.valueOf(i2Phase.getText().replace(",", ".")), false);

            Complex i2i1 = i2.divides(i1);
            Complex z12_i2i1 = z12.times(i2i1);
            Complex ze = (abp1.plus(z12_i2i1));

            Complex a1 = z12.times(z12);
            Complex den = abp1.times(abp2);

            double coup = (a1.divides(den)).abs();
            zeMag.setText(Global.decimalFormat(ze.abs()) + "");
            zePhase.setText(Global.decimalFormat(ze.phase()) + "");
            coupling.setText(Global.decimalFormat(coup) + "");
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_calcZbtnActionPerformed
//Comportamiento del botón Ir al Simulador del experimento 4
    private void lab1GoNEC3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1GoNEC3ActionPerformed
        MetaGlobal.getNf().setVisible(true);
    }//GEN-LAST:event_lab1GoNEC3ActionPerformed
//Comportamiento del botón Añadir Resultados del experimento 4
    private void lab1AddResult3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1AddResult3ActionPerformed
        if (moveDipole()) {
            runSimulation(false);
            if (showAnt2.isSelected()) {
                global.updatePlot(global);
            }
            Lab1_4 lab14 = new Lab1_4();
            double f = Double.valueOf(currentHeightlbl.getText().replace(",", "."));
            double h = f * global.getWavelength();
            lab14.setFactor(f);
            lab14.setHeight(h);
            double freq = global.getgFrequency().getFreq();
            double zSrc1Re = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getImpedanceReal();
            double zSrc1Im = (((global.getgSrcInfo().get(1)).get(freq)).get(0)).getImpedanceImaginary();
            lab14.setZa(new Complex(zSrc1Re, zSrc1Im));

            global.getgLab1_4().add(lab14);

            Object[] row = {lab14.getFactor() + "",
                lab14.getHeight() + "",
                lab14.getZa() + ""};
            model3.addRow(row);
            model3.fireTableDataChanged();

        }
    }//GEN-LAST:event_lab1AddResult3ActionPerformed
//Comportamiento del botón Eliminar Resultado del experimento 4
    private void lab1DeleteResult3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1DeleteResult3ActionPerformed
        int selected = resultTable3.getSelectedRow();
        if (selected != -1) {
            model3.removeRow(selected);
            global.getgLab1_4().remove(selected);
            model3.fireTableDataChanged();
        } else {
            global.errorMessage("Messages.noResultSelectedTitle", "Messages.noResultSelected");
        }
    }//GEN-LAST:event_lab1DeleteResult3ActionPerformed
//Comportamiento del botón Exportar Resultado del experimento 4
    private void lab1ExportResult3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1ExportResult3ActionPerformed
        global.exportResult(exportLab14(), Global.LAB14);
    }//GEN-LAST:event_lab1ExportResult3ActionPerformed
//Comportamiento del botón Instrucciones del experimento 3
    private void lab1Instruction2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1Instruction2ActionPerformed
        PDFReader.loadPdf("instruccioneslab13.pdf");
    }//GEN-LAST:event_lab1Instruction2ActionPerformed
//Comportamiento del botón Instrucciones del experimento 1
    private void lab1InstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1InstructionActionPerformed
        PDFReader.loadPdf("instruccioneslab11.pdf");
    }//GEN-LAST:event_lab1InstructionActionPerformed
//Comportamiento del botón Instrucciones del experimento 2
    private void lab1Instruction1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1Instruction1ActionPerformed
        PDFReader.loadPdf("instruccioneslab12.pdf");
    }//GEN-LAST:event_lab1Instruction1ActionPerformed
//Comportamiento del botón Instrucciones del experimento 4
    private void lab1Instruction3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lab1Instruction3ActionPerformed
        PDFReader.loadPdf("instruccioneslab14.pdf");
    }//GEN-LAST:event_lab1Instruction3ActionPerformed
//Comportamiento del botón Cargar Antena del experimento 4
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ArrayList<String> data = global.openFile();
        if (data != null) {
            Global.loadInputFile(data, global);
            if (MetaGlobal.getNmp() != null) {
                MetaGlobal.getNmp().initializeInfo(global);
            }
            global.InfoMessages("Messages.loadAntenna.title", "Messages.loadAntenna.OK");
        } else {
            global.InfoMessages("Messages.loadAntenna.title", " Messages.loadAntenna.FAILED");
        }
    }//GEN-LAST:event_jButton1ActionPerformed
//Comportamiento del botón Editar Antena del experimento 4
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (global.getgFrequency().getFreq() != 0.0) {
            MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("SimulationPanel.jTabbedPanel.label1"), global, MetaGlobal.getNmp());
            frame.add(new GeometryPanel(global));
            frame.pack();
            frame.setVisible(true);
        } else {
            global.errorMessage("Messages.nofreqTitle", "Messages.nofreq");
        }
    }//GEN-LAST:event_jButton2ActionPerformed
//Comportamiento del botón Editar Tierra del experimento 4
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        MultiFrame frame = new MultiFrame(400, 190, Global.getMessages().getString("GT.label"), global, MetaGlobal.getNmp());
        frame.add(new GroundTypePanel(global, frame));
        frame.pack();
        frame.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField abp1Im;
    private javax.swing.JFormattedTextField abp1Re;
    private javax.swing.JFormattedTextField abp2Im;
    private javax.swing.JFormattedTextField abp2Re;
    private javax.swing.JCheckBox alterz0;
    private javax.swing.JLabel alterz0lbl;
    private javax.swing.JLabel alterz0lbl1;
    private javax.swing.JCheckBox alterz1;
    private javax.swing.JComboBox<String> ant1From;
    private javax.swing.JComboBox<String> ant1To;
    private javax.swing.JComboBox<String> ant2From;
    private javax.swing.JComboBox<String> ant2To;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton calcZbtn;
    private javax.swing.JLabel coupling;
    private javax.swing.JLabel currentHeightlbl;
    private javax.swing.JLabel currentScalelbl1;
    private javax.swing.JLabel currentldlbl;
    private javax.swing.JRadioButton db;
    private javax.swing.JRadioButton db1;
    private javax.swing.JFormattedTextField distance;
    private javax.swing.JFormattedTextField finalFreq;
    private javax.swing.JFormattedTextField finalFreq1;
    private javax.swing.JLabel finalFreq_lbl;
    private javax.swing.JLabel finalFreq_lbl1;
    private javax.swing.JFormattedTextField heightvalue;
    private javax.swing.JFormattedTextField i1Mag;
    private javax.swing.JFormattedTextField i1Phase;
    private javax.swing.JFormattedTextField i2Mag;
    private javax.swing.JFormattedTextField i2Phase;
    private javax.swing.JFormattedTextField initFreq;
    private javax.swing.JFormattedTextField initFreq1;
    private javax.swing.JLabel initFreq_lbl;
    private javax.swing.JLabel initFreq_lbl1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
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
    private javax.swing.JPanel jPanel31;
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
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel60;
    private javax.swing.JPanel jPanel61;
    private javax.swing.JPanel jPanel62;
    private javax.swing.JPanel jPanel63;
    private javax.swing.JPanel jPanel64;
    private javax.swing.JPanel jPanel65;
    private javax.swing.JPanel jPanel66;
    private javax.swing.JPanel jPanel67;
    private javax.swing.JPanel jPanel68;
    private javax.swing.JPanel jPanel69;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel70;
    private javax.swing.JPanel jPanel71;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel73;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton lab1AddResult;
    private javax.swing.JButton lab1AddResult1;
    private javax.swing.JButton lab1AddResult2;
    private javax.swing.JButton lab1AddResult3;
    private javax.swing.JButton lab1DeleteResult;
    private javax.swing.JButton lab1DeleteResult1;
    private javax.swing.JButton lab1DeleteResult2;
    private javax.swing.JButton lab1DeleteResult3;
    private javax.swing.JButton lab1ExportResult;
    private javax.swing.JButton lab1ExportResult1;
    private javax.swing.JButton lab1ExportResult2;
    private javax.swing.JButton lab1ExportResult3;
    private javax.swing.JButton lab1GoNEC;
    private javax.swing.JButton lab1GoNEC1;
    private javax.swing.JButton lab1GoNEC2;
    private javax.swing.JButton lab1GoNEC3;
    private javax.swing.JButton lab1Instruction;
    private javax.swing.JButton lab1Instruction1;
    private javax.swing.JButton lab1Instruction2;
    private javax.swing.JButton lab1Instruction3;
    private javax.swing.JFormattedTextField ldvalue;
    private javax.swing.JFormattedTextField optFreq;
    private javax.swing.JFormattedTextField optFreq1;
    private javax.swing.JLabel optFreq_lbl;
    private javax.swing.JLabel optFreq_lbl1;
    private javax.swing.JRadioButton planeX;
    private javax.swing.JRadioButton planeY;
    private javax.swing.JRadioButton planeZ;
    private javax.swing.JTable resultTable;
    private javax.swing.JTable resultTable1;
    private javax.swing.JTable resultTable2;
    private javax.swing.JTable resultTable3;
    private javax.swing.JFormattedTextField scalevalue;
    private javax.swing.JComboBox<String> selectSrc;
    private javax.swing.JComboBox<String> selectSrc1;
    private javax.swing.JLabel selectSrcLbl;
    private javax.swing.JLabel selectSrcLbl1;
    private javax.swing.JLabel serielbl;
    private javax.swing.JLabel serielbl1;
    private javax.swing.JCheckBox showAnt;
    private javax.swing.JCheckBox showAnt1;
    private javax.swing.JCheckBox showAnt2;
    private javax.swing.JFormattedTextField stepFreq;
    private javax.swing.JFormattedTextField stepFreq1;
    private javax.swing.JLabel stepFreq_lbl;
    private javax.swing.JLabel stepFreq_lbl1;
    private javax.swing.JRadioButton times;
    private javax.swing.JRadioButton times1;
    private javax.swing.JLabel unit;
    private javax.swing.JFormattedTextField z12Im;
    private javax.swing.JFormattedTextField z12Re;
    private javax.swing.JFormattedTextField z2Im;
    private javax.swing.JFormattedTextField z2Re;
    private javax.swing.JLabel zeMag;
    private javax.swing.JLabel zePhase;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the firstExecution
     */
    public boolean isFirstExecution() {
        return firstExecution;
    }

    /**
     * @param firstExecution the firstExecution to set
     */
    public void setFirstExecution(boolean firstExecution) {
        this.firstExecution = firstExecution;
    }

    /**
     * @return the lab11WireTag
     */
    public int getLab11WireTag() {
        return lab11WireTag;
    }

    /**
     * @param lab11WireTag the lab11WireTag to set
     */
    public void setLab11WireTag(int lab11WireTag) {
        this.lab11WireTag = lab11WireTag;
    }

    /**
     * @return the firstExecutionLab12
     */
    public boolean isFirstExecutionLab12() {
        return firstExecutionLab12;
    }

    /**
     * @param firstExecutionLab12 the firstExecutionLab12 to set
     */
    public void setFirstExecutionLab12(boolean firstExecutionLab12) {
        this.firstExecutionLab12 = firstExecutionLab12;
    }

    /**
     * @return the plotType
     */
    public int getPlotType() {
        return plotType;
    }

    /**
     * @param plotType the plotType to set
     */
    public void setPlotType(int plotType) {
        this.plotType = plotType;
    }
}
