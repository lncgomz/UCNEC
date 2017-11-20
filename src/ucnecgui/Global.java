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
package ucnecgui;

import controllers.Complex;
import controllers.JZY3DPlotter;
import controllers.NECParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.Double.NaN;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import ucnecgui.jframes.NECFrame;
import ucnecgui.jpanels.GeometryPanel;
import ucnecgui.jpanels.PlotterPanel;
import ucnecgui.jpanels.PolarPlotter;
import ucnecgui.jpanels.SWRPlot;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.Frequency;
import ucnecgui.models.Ground;
import ucnecgui.models.Lab1_1;
import ucnecgui.models.Lab1_2;
import ucnecgui.models.Lab1_3;
import ucnecgui.models.Lab1_4;
import ucnecgui.models.Lab2_1;
import ucnecgui.models.Lab3_1;
import ucnecgui.models.Line;
import ucnecgui.models.Load;
import ucnecgui.models.Network;
import ucnecgui.models.Point;
import ucnecgui.models.PointColor;
import ucnecgui.models.PolarData;
import ucnecgui.models.Polarization;
import ucnecgui.models.PowerBudget;
import ucnecgui.models.RadiationPattern;
import ucnecgui.models.SWR;
import ucnecgui.models.Source;
import ucnecgui.models.SphericalCoordenate;
import ucnecgui.models.Tl;
import ucnecgui.models.Unit;
import ucnecgui.models.Wire;
import ucnecgui.models.WireLoss;

/**
 *
 * @author Leoncio Gómez
 */
public class Global {

    private boolean moduleNECVisible;
    private boolean labNECVisible;
    private ArrayList<Wire> gWires = new ArrayList<Wire>();
    private ArrayList<Source> gSource = new ArrayList<Source>();
    private ArrayList<Load> gLoad = new ArrayList<Load>();
    private ArrayList<Tl> gTl = new ArrayList<Tl>();
    private ArrayList<WireLoss> gWl = new ArrayList<WireLoss>();
    private ArrayList<Ground> gGround = new ArrayList<Ground>();
    private ArrayList<String> gData = new ArrayList<String>();
    private Unit gUnit = new Unit();
    private Frequency gFrequency = new Frequency();
    private RadiationPattern gRadiationPattern = new RadiationPattern();
    private ArrayList<Network> gNetwork = new ArrayList<Network>();
    private ArrayList<PointColor> gAperture = new ArrayList<PointColor>();
    private ArrayList<Double> gSpan = new ArrayList<Double>();
    private PowerBudget gPower = new PowerBudget();
    private ArrayList<PointColor> gMaxValue = new ArrayList<PointColor>();
    private ArrayList<SphericalCoordenate> gMax = new ArrayList<SphericalCoordenate>();
    private PointColor gSideValue = new PointColor();
    private Polarization gPolarization;
    private SphericalCoordenate gSide = new SphericalCoordenate();
    private SphericalCoordenate gBack = new SphericalCoordenate();
    private ArrayList<SphericalCoordenate> gAper = new ArrayList<SphericalCoordenate>();
    private LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>> gSrcInfo = new LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>>();
    private SWR gSWR;
    private double gScaleFactor = 1;
    private String gDirectory = defaultDirectory();
    private boolean directorySet = false;

    private ArrayList<Lab1_1> gLab1_1 = new ArrayList<Lab1_1>();
    private ArrayList<Lab1_2> gLab1_2 = new ArrayList<Lab1_2>();
    private ArrayList<Lab1_3> gLab1_3 = new ArrayList<Lab1_3>();
    private ArrayList<Lab1_4> gLab1_4 = new ArrayList<Lab1_4>();
    private ArrayList<Lab2_1> gLab2_1 = new ArrayList<Lab2_1>();
    private ArrayList<Lab3_1> gLab3_1 = new ArrayList<Lab3_1>();

    private int currentWireloss;
    private double alterZ0 = 0;
    private SphericalCoordenate maxGainPosition;
    private double maxGainValue;
    private int currentGroundType = -1;
    private int currentUnit = 0;
    private int currentPlotType;
    private int currentSourceAmount = 0;
    private int currentSourceTag = 0;
    private int selectedRows = -1;
    private PlotterPanel plotterPanel;
    private JZY3DPlotter plotter;

    //Definición de Constantes
    public static DecimalFormat df = new DecimalFormat("#.#####");
    public static DecimalFormat bigDf = new DecimalFormat("#.#################");
    public static ArrayList<String> outputErrorData;
    public final static double LIGHTSPEED = 299792458;
    public final static int XAXIS = 0;
    public final static int YAXIS = 1;
    public final static int ZAXIS = 2;
    public final static int XYPLANE = 0;
    public final static int YZPLANE = 1;
    public final static int XZPLANE = 2;
    public final static int AZIMUTHPLOT = 0;
    public final static int ELEVATIONPLOT = 1;
    public final static int PLOT3D = 2;
    public final static int POLARIZATIONPLOT = 3;
    public final static int RPMODULE = 4;
    public final static int RPNORMALIZED = 5;
    public final static int RPBIDIMENSIONAL = 5;
    public final static int WIRES = 0;
    public final static int FREESPACE = 0;
    public final static int PEC = 1;
    public final static int REAL = 2;
    public final static int CUSTOM = 0;
    public final static int COOPER = 1;
    public final static int ALUMINIUM = 2;
    public final static int TIN = 3;
    public final static int ZINC = 4;
    public final static int LOSSLESS = 5;
    public final static int PLOTTIMES = 0;
    public final static int PLOTDB = 1;
    public final static int METER = 0;
    public final static int MILIMETER = 1;
    public final static int FEET = 2;
    public final static int INCH = 3;
    public final static int WAVELENGTH = 4;
    public final static int LAB11 = 0;
    public final static int LAB12 = 1;
    public final static int LAB13 = 2;
    public final static int LAB14 = 3;
    public final static int LAB21 = 4;
    public final static int LAB31 = 5;
    public final static int LAB41 = 6;
    public final static int LAB42 = 7;

    public static String imagePath = "";

    /**
     * Constructor de la clase Global
     */
    public Global() {
    }

    /**
     * Determina el directorio raíz desde donde se ejecuta la aplicación
     *
     * @return Director abreviado desde el cual se ejecuta la aplicación
     */
    public String defaultDirectory() {
        String path = Global.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String dir = new File(path).getPath();
        char sep = File.separatorChar;
        String p = dir.substring(dir.lastIndexOf(sep));
        String resp = "~" + p;
        return resp;
    }

    /**
     * Genera un texto con la información de la cantidad total de alambres y
     * segmentos presentes en la descripción de la geometría de la simulación
     *
     * @return Información textual sobre la cantidad de alambres y segmentos
     * existentes
     */
    public String wireInformation() {
        int segCounter = 0;
        for (Wire wire : getgWires()) {
            if (wire.getNumber() == getCurrentSourceTag()) {
                continue;
            }
            segCounter = segCounter + wire.getSegs();
        }
        if (getgNetwork().size() > 0) {
            return getgWires().size() - 1 + " " + "alambre(s)" + " , " + segCounter + " segmento(s)";
        } else {
            return getgWires().size() + " " + "alambre(s)" + " , " + segCounter + " segmento(s)";
        }
    }

    /**
     * Genera un texto con la información del tipo de tierra utilizado en la
     * simulación
     *
     * @return Información del tipo de tierra utilizado en la simulación
     */
    public String groundTypeInformation() {
        ArrayList<Ground> ground = getgGround();
        String resp = "";
        for (Ground gn : ground) {
            switch (gn.getType()) {
                case -1:
                    setCurrentGroundType(Global.FREESPACE);
                    resp = "Espacio Libre";
                    break;
                case 1:
                    setCurrentGroundType(Global.PEC);
                    resp = "PEC";
                    break;
                case 2:
                    resp = "REAL";
                    break;
                default:
                    setCurrentGroundType(Global.FREESPACE);
                    resp = "Espacio Libre";
                    break;
            }
        }
        return resp;
    }

    /**
     * Interfaz para acceder al archivo de salida, generado por el script
     * NEC2++, con los valores de texto correspondientes a etiquetas y mensajes,
     * categorizados por idiomas.
     *
     * @return Interfaz para el acceso a archivo de texto de etiquetas y
     * mensajes
     */
    public static ResourceBundle getMessages() {
        Locale aLocale = new Locale("es");
        ResourceBundle messages = ResourceBundle.getBundle("LabelsBundle", aLocale);
        return messages;
    }

    public ArrayList<Wire> getgWires() {
        return gWires;
    }

    public void setgWires(ArrayList<Wire> gWires) {
        this.gWires = gWires;
    }

    public ArrayList<Source> getgSource() {
        return gSource;
    }

    public void setgSource(ArrayList<Source> gSource) {
        this.gSource = gSource;
    }

    public ArrayList<Load> getgLoad() {
        return gLoad;
    }

    public void setgLoad(ArrayList<Load> gLoad) {
        this.gLoad = gLoad;
    }

    public ArrayList<Tl> getgTl() {
        return gTl;
    }

    public void setgTl(ArrayList<Tl> gTl) {
        this.gTl = gTl;
    }

    public ArrayList<WireLoss> getgWl() {
        return gWl;
    }

    public void setgWl(
            ArrayList<WireLoss> gWl) {
        this.gWl = gWl;
    }

    public ArrayList<Ground> getgGround() {
        return gGround;
    }

    public void setgGround(ArrayList<Ground> gGround) {
        this.gGround = gGround;
    }

    public Unit getgUnit() {
        return gUnit;
    }

    public void setgUnit(Unit gUnit) {
        this.gUnit = gUnit;
    }

    public Frequency getgFrequency() {
        return gFrequency;
    }

    public void setgFrequency(
            Frequency gFrequency) {
        this.gFrequency = gFrequency;
    }

    public RadiationPattern getgRadiationPattern() {
        return gRadiationPattern;
    }

    public int getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(int currentUnit) {
        this.currentUnit = currentUnit;

    }

    public void setgRadiationPattern(
            RadiationPattern gRadiationPattern) {
        this.gRadiationPattern = gRadiationPattern;
    }

    public PlotterPanel getPlotterPanel() {
        return plotterPanel;
    }

    public void setPlotterPanel(PlotterPanel plotterPanel) {
        this.plotterPanel = plotterPanel;
    }

    public JZY3DPlotter getPlotter() {
        return plotter;
    }

    public void setPlotter(JZY3DPlotter plotter) {
        this.plotter = plotter;
    }

    public int getCurrentPlotType() {
        return currentPlotType;
    }

    public void setCurrentPlotType(int currentPlotType) {
        this.currentPlotType = currentPlotType;
    }

    public ArrayList<String> getgData() {
        return gData;
    }

    public void setgData(ArrayList<String> gData) {
        this.gData = gData;
    }

    public ArrayList<PointColor> getgAperture() {
        return gAperture;
    }

    public void setgAperture(ArrayList<PointColor> gAperture) {
        this.gAperture = gAperture;
    }

    public SWR getgSWR() {
        return gSWR;
    }

    public void setgSWR(SWR gSWR) {
        this.gSWR = gSWR;
    }

    public String getgDirectory() {
        return gDirectory;
    }

    public void setgDirectory(String gDirectory) {
        this.gDirectory = gDirectory;
    }

    public int getCurrentGroundType() {
        return currentGroundType;
    }

    public void setCurrentGroundType(int currentGroundType) {
        this.currentGroundType = currentGroundType;
    }

    public double getAlterZ0() {
        return alterZ0;
    }

    public void setAlterZ0(double alterZ0) {
        this.alterZ0 = alterZ0;
    }

    public int getCurrentWireloss() {
        return currentWireloss;
    }

    public void setCurrentWireloss(int currentWireloss) {
        this.currentWireloss = currentWireloss;
    }

    public PointColor getgSideValue() {
        return gSideValue;
    }

    public void setgSideValue(PointColor gSideValue) {
        this.gSideValue = gSideValue;
    }

    public ArrayList<PointColor> getgMaxValue() {
        return gMaxValue;
    }

    public void setgMaxValue(ArrayList<PointColor> gMaxValue) {
        this.gMaxValue = gMaxValue;
    }

    public ArrayList<Double> getgSpan() {
        return gSpan;
    }

    public void setgSpan(ArrayList<Double> gSpan) {
        this.gSpan = gSpan;
    }

    public ArrayList<SphericalCoordenate> getgMax() {
        return gMax;
    }

    public void setgMax(ArrayList<SphericalCoordenate> gMax) {
        this.gMax = gMax;
    }

    public SphericalCoordenate getgSide() {
        return gSide;
    }

    public void setgSide(SphericalCoordenate gSide) {
        this.gSide = gSide;
    }

    public ArrayList<SphericalCoordenate> getgAper() {
        return gAper;
    }

    public void setgAper(ArrayList<SphericalCoordenate> gAper) {
        this.gAper = gAper;
    }

    public PowerBudget getgPower() {
        return gPower;
    }

    public void setgPower(PowerBudget gPower) {
        this.gPower = gPower;
    }

    public SphericalCoordenate getgBack() {
        return gBack;
    }

    public void setgBack(SphericalCoordenate gBack) {
        this.gBack = gBack;
    }

    public ArrayList<Network> getgNetwork() {
        return gNetwork;
    }

    public void setgNetwork(ArrayList<Network> gNetwork) {
        this.gNetwork = gNetwork;
    }

    public int getCurrentSourceAmount() {
        return currentSourceAmount;
    }

    public void setCurrentSourceAmount(int currentSourceAmount) {
        this.currentSourceAmount = currentSourceAmount;
    }

    public int getCurrentSourceTag() {
        return currentSourceTag;
    }

    public void setCurrentSourceTag(int currentSourceTag) {
        this.currentSourceTag = currentSourceTag;
    }

    public int getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(int aSelectedRows) {
        selectedRows = aSelectedRows;
    }

    public LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>> getgSrcInfo() {
        return gSrcInfo;
    }

    public void setgSrcInfo(LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>> gSrcInfo) {
        this.gSrcInfo = gSrcInfo;
    }

    public boolean isModuleNECVisible() {
        return moduleNECVisible;
    }

    public void setModuleNECVisible(boolean moduleNECVisible) {
        this.moduleNECVisible = moduleNECVisible;
    }

    public boolean isLabNECVisible() {
        return labNECVisible;
    }

    public void setLabNECVisible(boolean labNECVisible) {
        this.labNECVisible = labNECVisible;
    }

    public ArrayList<Lab1_1> getgLab1_1() {
        return gLab1_1;
    }

    public void setgLab1_1(ArrayList<Lab1_1> gLab1_1) {
        this.gLab1_1 = gLab1_1;
    }

    public double getgScaleFactor() {
        return gScaleFactor;
    }

    public void setgScaleFactor(double gScaleFactor) {
        this.gScaleFactor = gScaleFactor;
    }

    public ArrayList<Lab1_2> getgLab1_2() {
        return gLab1_2;
    }

    public void setgLab1_2(ArrayList<Lab1_2> gLab1_2) {
        this.gLab1_2 = gLab1_2;
    }

    public ArrayList<Lab1_3> getgLab1_3() {
        return gLab1_3;
    }

    public void setgLab1_3(ArrayList<Lab1_3> gLab1_3) {
        this.gLab1_3 = gLab1_3;
    }

    public ArrayList<Lab1_4> getgLab1_4() {
        return gLab1_4;
    }

    public void setgLab1_4(ArrayList<Lab1_4> gLab1_4) {
        this.gLab1_4 = gLab1_4;
    }

    public ArrayList<Lab2_1> getgLab2_1() {
        return gLab2_1;
    }

    public void setgLab2_1(ArrayList<Lab2_1> gLab2_1) {
        this.gLab2_1 = gLab2_1;
    }

    public Polarization getgPolarization() {
        return gPolarization;
    }

    public void setgPolarization(Polarization gPolarization) {
        this.gPolarization = gPolarization;
    }

    public ArrayList<Lab3_1> getgLab3_1() {
        return gLab3_1;
    }

    public void setgLab3_1(ArrayList<Lab3_1> gLab3_1) {
        this.gLab3_1 = gLab3_1;
    }

    public SphericalCoordenate getMaxGainPosition() {
        return maxGainPosition;
    }

    public void setMaxGainPosition(SphericalCoordenate maxGainPosition) {
        this.maxGainPosition = maxGainPosition;
    }

    public double getMaxGainValue() {
        return maxGainValue;
    }

    public void setMaxGainValue(double maxGainValue) {
        this.maxGainValue = maxGainValue;
    }

    public boolean isDirectorySet() {
        return directorySet;
    }

    public void setDirectorySet(boolean directorySet) {
        this.directorySet = directorySet;
    }

    /**
     * Devuelve el alambre auxiliar para fuentes de corriente
     *
     * @param wireId Etiqueta del alambre auxiliar para fuentes de corriente
     * @return Objeto Wire correspondiente al alambre auxiliar para fuentes de
     * corriente
     */
    public Wire getCurrentSourceWire(int wireId) {
        ArrayList<Wire> wires = getgWires();
        Wire resp = null;
        for (Wire wire : wires) {
            if (wire.getNumber() == wireId) {
                resp = wire;
                break;
            }
        }
        return resp;
    }

    /**
     * Devuelve el porcentaje de la longitud de un alambre donde su ubica un
     * segmento determinado
     *
     * @param seg Número del segmento cuya ubicación se desea obtener
     * @param wireTag Índice del alambre bajo evaluación
     * @return Porcentaje de la longitud de un alambre donde su ubica un
     * segmento determinado
     */
    public double calculatePercentaje(int seg, int wireTag) {
        Wire wire = getgWires().get(wireTag - 1);
        int segs = wire.getSegs();
        return (seg * 100) / segs;
    }

    /**
     * Lee el archivo de salida generado por el script NEC2++ para convertirlo
     * en un arreglo de objetos de la clase String cuyos elementos corresponden
     * a cada línea del archivo leído
     *
     * @param isOutdateGenerated Indica si el archivo de salida ya ha sido
     * generado
     * @return Arreglo de objetos de la clase String cuyos elementos
     * corresponden a cada línea del archivo leído
     */
    public ArrayList<String> readOutputData(boolean isOutdateGenerated) {
        ArrayList<String> resp = new ArrayList<String>();
        if (isOutdateGenerated) {
            BufferedReader br = null;
            try {
                String path = NECFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String dir = new File(path).getParentFile().getPath() + File.separator + "kernel" + File.separator;
                br = new BufferedReader(new FileReader(dir + "output.nec"));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    resp.add(line);
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                setgData(resp);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NECFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(NECFrame.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(NECFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return resp;
    }

    /**
     * Genera un archivo de salida de extensión .nec a partir del script NEC2++
     * y un archivo de entrada, de extensión .nec,con comandos interpretables
     * por ese programa. Esta lectura está particularizada para ser ejecutada en
     * SO Windows y Linux
     *
     * @return true si la generación del archivo de salida fue exitosa, de lo
     * contrario, devuelve false
     */
    public static boolean generateOutputData() {
        ArrayList<String> necOutput = new ArrayList<String>();
        ArrayList<String> necError = new ArrayList<String>();
        boolean resp = false;

        String path = NECFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String dir = new File(path).getParentFile().getPath() + File.separator + "kernel" + File.separator;

        if (SystemUtils.IS_OS_WINDOWS) {
            try {
                System.out.println("Output PATH: " + dir);
                String target = new String(dir + "nec2++ -s -c -i " + dir + "input.nec" + " -o " + dir + "output.nec");
                System.out.println("COMMAND: " + target);
                Runtime rt = Runtime.getRuntime();
                Process proc = rt.exec(target);
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                necOutput = new ArrayList<String>();
                String s = null;
                while ((s = stdInput.readLine()) != null) {
                    necOutput.add(s);
                }
                while ((s = stdError.readLine()) != null) {
                    necError.add(s);
                }
                Global.outputErrorData = necError;
                proc.waitFor();
                proc.destroy();

            } catch (Throwable t) {
                t.printStackTrace();
                resp = false;
            }
            resp = true;
        } else if (SystemUtils.IS_OS_LINUX) {
            List<String> commands = new ArrayList<String>();
            commands.add("nec2++");
            commands.add("-s");
            commands.add("-c");
            commands.add("-i");
            commands.add("input.nec");
            commands.add("-o");
            commands.add("output.nec");
            System.out.println(commands);
            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.directory(new File(dir));
            pb.redirectErrorStream(true);
            Process process = null;
            try {
                process = pb.start();
            } catch (IOException ex) {
                Logger.getLogger(Global.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s = null;
            try {
                while ((s = stdInput.readLine()) != null) {
                    necOutput.add(s);
                }
                while ((s = stdError.readLine()) != null) {
                    necError.add(s);
                }
                Global.outputErrorData = necError;
                process.waitFor();
                process.destroy();
            } catch (Throwable t) {
                t.printStackTrace();
                resp = false;
            }
            resp = true;
        }
        return resp;
    }

    /**
     * Ejecuta el cálculo del SWR para la generación de la gráfica
     * correspondiente a partir del API JFreeChart
     *
     * @param global Objeto de la clase Global
     * @param plotType Tipo de gráfica a generar
     */
    public void executeSWR(Global global, int plotType) {
        global.generateSWRInputFile();
        LinkedHashMap<Double, ArrayList<AntInputLine>> swrData = NECParser.getAntennaInput(readOutputData(generateOutputData()), global, global.getgSWR());
        global.generateSWRInfo(swrData);
        SWRPlot.execute(swrData, global.getgSWR(), plotType, global);
    }

    /**
     * Ejecuta la graficación del diagrama de polarización a partir de un
     * arreglo de objetos de la clase PolarData; a través del API JFreeChart
     *
     * @param points Arreglo de objetos de la clase PolarData con los datos del
     * diagrama de polarización
     * @param tick Separación angular del gráfico polar
     * @param global Objeto de la clase Global
     * @param selectedColor Color de la gráfica polar
     */
    public void executePolarization(ArrayList<PolarData> points, Double tick, Global global, int selectedColor) {
        PolarPlotter.execute("Patrón de Radiación", points, tick, global, selectedColor);
    }

    /**
     * Ejecuta la graficación del diagrama de polarización a partir de un
     * arreglo de objetos de la clase PolarData; a través del API JFreeChart,
     * empleando un color determinado para la gráfica
     *
     * @param points Arreglo de objetos de la clase PolarData con los datos del
     * diagrama de polarización
     * @param tick Separación angular del gráfico polar
     * @param colorPlot Color de la gráfica
     * @param global Objeto de la clase Global
     */
    public void executePolarization(ArrayList<PolarData> points, Double tick, int colorPlot, Global global) {
        PolarPlotter.execute("Patrón de Polarización", points, tick, colorPlot, global);
    }

    /**
     * Ejecuta la graficación del diagrama de polarización a partir de una
     * colección de objetos cuya clave es la frecuencia de iteración y su valor
     * es un arreglo de objetos de la clase PolarData con los datos del diagrama
     * de polarización.
     *
     * @param points Colección de objetos cuya clave es la frecuencia de
     * iteración y su valor es un arreglo de objetos de la clase PolarData con
     * los datos del diagrama de polarización.
     * @param tick Separación angular del gráfico polar
     * @param colorPlot Color de la gráfica
     * @param global Objeto de la clase Global
     */
    public void executePolarization(HashMap<String, ArrayList<PolarData>> points, Double tick, int colorPlot, Global global) {
        PolarPlotter.execute("Elipse de Polarización", points, tick, colorPlot, global);
    }

    /**
     * Genera un archivo de entrada, interpretable por el script NEC2++ para el
     * cálculo del SWR
     */
    public void generateSWRInputFile() {
        ArrayList<String> necCard = new ArrayList<String>();

        if (getgWires().size() > 0) {

            necCard.add("CM UNIVERSIDAD DE CARABOBO");
            necCard.add("CM ESCUELA DE INGENIERÍA DE TELECOMUNICACIONES");
            necCard.add("CM UCNEC SIMULATION");
            necCard.add("CE");

            ArrayList<String> geometry = Wire.toString(getgWires());
            for (String wire : geometry) {
                necCard.add(wire);
            }
            necCard.add("GE 0");

            if (getgLoad().size() > 0) {
                ArrayList<String> load = Load.toString(getgLoad());
                for (String ld : load) {
                    necCard.add(ld);
                }
            }

            if (getgTl().size() > 0) {
                ArrayList<String> tl = Tl.toString(getgTl());
                for (String tls : tl) {
                    necCard.add(tls);
                }
            }

            if (getgWl().size() > 0) {
                ArrayList<String> wl = new ArrayList<String>();
                for (String wireLoss : wl) {
                    necCard.add(wireLoss);
                }
            }

            necCard.add(Frequency.toSWRString(getgSWR()));

            if (getgGround().size() > 0) {
                ArrayList<String> ground = Ground.toString(getgGround());
                for (String gnd : ground) {
                    necCard.add(gnd);
                }
            }

            if (getgSource().size() > 0) {
                ArrayList<String> source = Source.toString(getgSource());
                for (String src : source) {
                    necCard.add(src);
                }
            }

            if (getgNetwork().size() > 0) {
                ArrayList<String> network = Network.toString(getgNetwork());
                for (String nt : network) {
                    necCard.add(nt);
                }
            }

            necCard.add(RadiationPattern.trivialRP());

            necCard.add("EN");

            Writer writer = null;

            try {
                String path = NECFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String dir = new File(path).getParentFile().getPath() + File.separator + "kernel" + File.separator + "input.nec";
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(dir), "utf-8"));
                for (String line : necCard) {
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {/*ignore*/
                    ex.printStackTrace();
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, Global.getMessages().getString("Global.noEnoughInfo"), Global.getMessages().getString("Global.noEnoughInfoTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Analiza el archivo de salida generado por el script NEC2++ para obtener
     * los parámetros de la fuente bajo estudio en un determinado rango de
     * frecuencias, a fin de calcular el valor del SWR dentro de este mismo
     * rango
     *
     * @param swrData Colección de objetos cuya clave es la frecuencia de
     * iteración y su valor es un arreglo de objetos de la clase AntInputLine
     * con parámetros de la fuente bajo estudio en un determinado rango de
     * frecuencias
     */
    public void generateSWRInfo(LinkedHashMap<Double, ArrayList<AntInputLine>> swrData) {
        LinkedHashMap<Double, Double> swrValues = new LinkedHashMap<Double, Double>();
        Complex z0 = null;
        if (getgSWR().isUseAltZ0()) {
            z0 = new Complex(getgSWR().getAltZ0(), 0);
        } else {
            z0 = new Complex(50, 0);
        }
        boolean first = true;
        double minFreq = 0;
        double min = 0;
        for (Map.Entry<Double, ArrayList<AntInputLine>> entry : swrData.entrySet()) {
            double freq = entry.getKey();
            ArrayList<AntInputLine> antInputLineList = entry.getValue();
            AntInputLine nAIL = antInputLineList.get(getgSWR().getSrcIndex() - 1);
            Complex zl = new Complex(nAIL.getImpedanceReal(), nAIL.getImpedanceImaginary());
            Complex num = zl.minus(z0);
            Complex den = zl.plus(z0);
            Complex rho = num.divides(den);
            double absRho = rho.abs();
            if (absRho == 1) {
                absRho = 0.99;
            }
            double val = (1 + absRho) / (1 - absRho);
            swrValues.put(freq, val);
            if (first) {
                min = val;
                minFreq = freq;
                first = false;
            } else if (val < min) {
                min = val;
                minFreq = freq;
            }
        }
        getgSWR().setData(swrValues);
        getgSWR().getMinSWR().setFrequency(minFreq);
        getgSWR().getMinSWR().setValue(min);
        ArrayList<Double> candidates = new ArrayList<Double>();
        boolean firstfound = false;
        boolean minReached = false;
        for (Map.Entry<Double, ArrayList<AntInputLine>> entry : swrData.entrySet()) {
            double freq = entry.getKey();
            ArrayList<AntInputLine> antInputLineList = entry.getValue();
            AntInputLine nAIL = antInputLineList.get(getgSWR().getSrcIndex() - 1);
            Complex zl = new Complex(nAIL.getImpedanceReal(), nAIL.getImpedanceImaginary());
            Complex num = zl.minus(z0);
            Complex den = zl.plus(z0);
            Complex rho = num.divides(den);
            double absRho = rho.abs();
            if (absRho == 1) {
                absRho = 0.99;
            }
            double val = (1 + absRho) / (1 - absRho);
            if (!firstfound) {
                if (val <= 1.051 * min) {
                    candidates.add(freq);
                    firstfound = true;
                }
            } else if (firstfound && !minReached) {
                if (val == min) {
                    minReached = true;
                }
            } else if (firstfound && minReached) {
                if (val >= 1.049 * min) {
                    candidates.add(freq);
                    break;
                }
            }
        }
        if (candidates.size() == 2) {
            double c1 = candidates.get(0);
            double c2 = candidates.get(1);
            if (c1 > c2) {
                getgSWR().setBw(c1 - c2);
            } else {
                getgSWR().setBw(c2 - c1);
            }
        } else {
            getgSWR().setBw(-1);
        }
    }

    /**
     * Elimina un alambre de forma limpia, quitando los componentes adicionales
     * conectados al mismo alambre
     *
     * @param wireTag Índice del alambre a eliminar
     */
    public void cleanWireDelete(int wireTag) {

        if (getRelatedSource(wireTag) != null) {
            int srcIndx = getSourcePosition(getRelatedSource(wireTag));
            getgSource().remove(srcIndx);
        }

        if (getRelatedNetworkByWire(wireTag) != -1) {
            int networkIndx = getRelatedNetworkByWire(wireTag);
            Network relatedNetwork = getgNetwork().get(networkIndx);
            int wireTag2 = relatedNetwork.getTagNumberPort1();
            if (getRelatedSource(wireTag2) != null) {
                int srcIndx2 = getSourcePosition(getRelatedSource(wireTag2));
                getgSource().remove(srcIndx2);
                getgNetwork().remove(networkIndx);
                Wire auxWire = getgWires().get(getCurrentSourceTag() - 1);
                if (auxWire.getSegs() > 1) {
                    auxWire.setSegs(auxWire.getSegs() - 1);
                } else {
                    int currentSourceTagIndx = getSourceWireTagPosition(getCurrentSourceTag());
                    getgWires().remove(currentSourceTagIndx);
                    setCurrentSourceTag(0);
                }
            }
        }

        if (getRelatedLoad(wireTag) != null) {
            int ldIndx = getLoadPosition(getRelatedLoad(wireTag));
            getgLoad().remove(ldIndx);
        }
        if (getRelatedTL(wireTag) != null) {
            int tlIndx = getTLPosition(getRelatedTL(wireTag));
            getgTl().remove(tlIndx);
        }
    }

    /**
     * Ordena un arreglo de objetos del tipo Wire, colocando al alambre auxiliar
     * de corriente en la última posición, de existir.
     *
     * @return Arreglo ordenado de objetos Wire. Correspondiente a la geometría
     * de la simulación
     */
    public ArrayList<Wire> sortWires() {
        ArrayList<Wire> aux = new ArrayList<Wire>();
        for (Wire wire : getgWires()) {
            if (wire.getNumber() != getCurrentSourceTag()) {
                aux.add(wire);
            }
        }

        for (Wire wire : getgWires()) {
            if (wire.getNumber() == getCurrentSourceTag()) {
                aux.add(wire);
            }
        }
        for (int i = 0; i < aux.size(); i++) {
            int wireTag = aux.get(i).getNumber();
            if (getRelatedSource(wireTag) != null) {
                if (getRelatedNetworkByWire(wireTag) != -1) {
                    getgNetwork().get(getRelatedNetworkByWire(wireTag)).setTagNumberPort2(i + 1);
                    getgNetwork().get(getRelatedNetworkByWire(wireTag)).setTagNumberPort1(aux.size());
                }
                getRelatedSource(wireTag).setSourceSeg(i + 1);
            }

            if (getRelatedLoad(wireTag) != null) {
                getRelatedLoad(wireTag).setLoadSeg(i + 1);
            }
            if (getRelatedTL(wireTag) != null) {
                if (getRelatedTL(wireTag).getTlWireTag1() == wireTag) {
                    getRelatedTL(wireTag).setTlWireTag1(i + 1);
                } else {
                    getRelatedTL(wireTag).setTlWireTag2(i + 1);
                }
            }
            aux.get(i).setNumber(i + 1);
        }
        getgWires().clear();
        for (Wire wire : aux) {
            getgWires().add(wire);
        }

        if (getCurrentSourceTag() != 0) {
            setCurrentSourceTag(aux.size());
        }
        return aux;
    }

    /**
     * Devuelve el índice del alambre auxiliar de corriente, a partir de su
     * etiqueta
     *
     * @param sourceWireTag Etiqueta del alambre auxiliar de corriente
     * @return Índice del alambre auxiliar de corriente
     */
    public int getSourceWireTagPosition(int sourceWireTag) {
        for (int i = 0; i < getgWires().size(); i++) {
            if (getgWires().get(i).getNumber() == sourceWireTag) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve el índice de la fuente introducida como parámetro
     *
     * @param src Objeto de la clase Source cuyo índice se desea conocer
     * @return Índice de la fuente
     */
    public int getSourcePosition(Source src) {
        for (int i = 0; i < getgSource().size(); i++) {
            if (getgSource().get(i) == src) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve la posición de la red conectada al alambre cuyo índice es el
     * parámetro tag2Number
     *
     * @param tag2Number Índice del alambre al que se desea consultar si tiene
     * una red conectada
     * @return Índice de la red conectada al alambre de índice tag2Number, de no
     * encontrarla,, devuelve -1
     */
    public int getRelatedNetworkPosition(int tag2Number) {
        for (int i = 0; i < getgNetwork().size(); i++) {
            if (getgNetwork().get(i).getTagNumberPort2() == tag2Number) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve el índice de la carga introducida como parámetro
     *
     * @param ld Objeto de la clase Load cuyo índice se desea conocer
     * @return Índice de la carga
     */
    public int getLoadPosition(Load ld) {
        for (int i = 0; i < getgLoad().size(); i++) {
            if (getgLoad().get(i) == ld) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve el índice de la línea de transmisión introducida como parámetro
     *
     * @param tl Objeto de la clase Línea de Transmisición cuyo índice se desea
     * conocer
     * @return Índice de la línea de transmisición
     */
    public int getTLPosition(Tl tl) {
        for (int i = 0; i < getgTl().size(); i++) {
            if (getgTl().get(i) == tl) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve el índice de la fuente conectada al alambre de índice wireTag
     *
     * @param wireTag Índice del alambre sobre el que se desea consultar si
     * tiene fuentes conectadas a él
     * @return Índice de la fuente, de no encotnrarla, retorna null
     */
    public Source getRelatedSource(int wireTag) {
        for (Source nSrc : getgSource()) {
            if (nSrc.getSourceSeg() == wireTag) {
                return nSrc;
            }
        }
        return null;
    }

    /**
     * Devuelve el índice de la fuente conectada a la red de índice ntIndx
     *
     * @param ntIndx Índice de la red sobre el que se desea consultar si tiene
     * fuentes conectadas a él
     * @return Índice de la fuente, de no encotnrarla, retorna null
     */
    public int getRelatedSourceByNT(int ntIndx) {
        Network nt = getgNetwork().get(ntIndx);
        for (int i = 0; i < getgSource().size(); i++) {
            if (getgSource().get(i).getSourceSeg() == nt.getTagNumberPort1() && getgSource().get(i).getSegPercentage() == nt.getPort1Segment()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Devuelve la carga conectada al alambre de índice wireTag
     *
     * @param wireTag Índice del alambre sobre el que se desea consultar si
     * tiene cargas conectadas a él
     * @return Objeto tipo carga, de no encotnrarlo, retorna null
     */
    public Load getRelatedLoad(int wireTag) {
        for (Load nLoad : getgLoad()) {
            if (nLoad.getLoadSeg() == wireTag) {
                return nLoad;
            }
        }
        return null;
    }

    /**
     * Devuelve la línea de transmisión conectada al alambre de índice wireTag
     *
     * @param wireTag Índice del alambre sobre el que se desea consultar si
     * tiene cargas conectadas a él
     * @return Objeto tipo carga, de no encotnrarlo, retorna null
     */
    public Tl getRelatedTL(int wireTag) {
        for (Tl nTl : getgTl()) {
            if (nTl.getTlWireTag1() == wireTag || nTl.getTlWireTag2() == wireTag) {
                return nTl;
            }
        }
        return null;
    }

    /**
     * Devuelve el índice de la red conectada a la fuente de índice sourceIndex
     *
     * @param sourceIndex Índice de la fuente sobre la que se desea consultar si
     * tiene redes conectadas a él
     * @return Índice de la red, de no encotnrarla, retorna null
     */
    public int getRelatedNetwork(int sourceIndex) {
        int resp = -1;
        ArrayList<Network> network = getgNetwork();
        Source src = getgSource().get(sourceIndex - 1);
        int i = 0;
        for (Network nt : network) {
            if (nt.getTagNumberPort1() == src.getSourceSeg() && nt.getPort1Segment() == src.getSegPercentage()) {
                resp = i;
            }
            i++;
        }
        return resp;
    }

    /**
     * Devuelve el índice de la red conectada al alambre de índice sourceIndex
     *
     * @param wireTag Índice del alambre sobre la que se desea consultar si
     * tiene redes conectadas a él
     * @return Índice del red, de no encotnrarla, retorna null
     */
    public int getRelatedNetworkByWire(int wireTag) {

        for (int i = 0; i < getgNetwork().size(); i++) {
            if (getgNetwork().get(i).getTagNumberPort2() == wireTag) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Actualiza la gráfica de alambre, creada a través del API de JZY3D
     *
     * @param global Objeto de la clase Global
     */
    public void updatePlot(Global global) {

        if (getPlotter() != null) {
            getPlotter().dispose();
            setPlotter(new JZY3DPlotter(global));
            getPlotter().plot(getgWires(), getSelectedRows(), this);
        } else {
            setPlotter(new JZY3DPlotter(global));
            getPlotter().plot(getgWires(), getSelectedRows(), this);
        }
    }

    /**
     * Genera el mensaje de información relacionada al alambre seleccionado en
     * el panel de geometría
     *
     * @param selectedWireTag Índice del alambre seleccionado
     * @param wires Arreglo de objetos de la clase Wire
     * @param global Objeto de la clase Global
     * @return Información relacionada al alambre seleccionado en el panel de
     * geometría
     */
    public static String setInfoText(int selectedWireTag, ArrayList<Wire> wires, Global global) {
        String resp = "";
        if (wires.size() > 0) {
            double ld = Global.wireLength(wires.get(selectedWireTag - 1)) / (wires.get(selectedWireTag - 1).getRadius());

            resp = "Alambre número: " + wires.get(selectedWireTag - 1).getNumber() + " | " + " Longitud: " + Global.wireLength(wires.get(selectedWireTag - 1)) + " " + global.unit2ShortString() + " | "
                    + " Diámetro: " + Global.decimalFormat(wires.get(selectedWireTag - 1).getRadius() * global.unit2UpperFactor()) + " " + global.unit2LowerString() + " | " + "Razón L/D: " + Global.decimalShortFormat(ld);
            ;
        } else {
            resp = "No se ha especificado ningún alambre";
        }
        return resp;
    }

    /**
     * Redondea los valores de la tabla de geometría de forma tal que todo
     * número menor a 1E-4 sea representado como un cero
     *
     * @param table Tabla de geometría del panel Geometry
     * @param model Objeto DefaultTableModel correspondiente al modelo de la
     * tabla de geometría del panel Geometry
     */
    public static void roundValues(JTable table, DefaultTableModel model) {
        String[][] data = new String[model.getRowCount()][model.getColumnCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                data[i][j] = model.getValueAt(i, j).toString();
            }
        }
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                if ((data[i][j] != null && !data[i][j].isEmpty()) && (Math.abs(Double.valueOf(data[i][j])) < 0.0001)) {
                    model.setValueAt(0, i, j);
                }
            }
        }
    }

    /**
     * Efectua la modificación de un parámetro, correspondiente a una coordenada
     * de un alambre determinado, adicionando algebraicamente el valor del
     * parámetro offset
     *
     * @param value Valor original de la coordenada a trasladar
     * @param offset Factor de traslación
     * @return Valor modificado de la coordenada, por el factor indicado en el
     * parámetro offset
     */
    public static double traslateValue(double value, double offset) {
        return Double.valueOf(value) + offset;
    }

    /**
     * Efectúa el escalamiento de un objeto de la clase Wire por un factor
     * indicado en el parámetro factor, el diámetro es modificado o conservado
     * en función del parámetro keepDiameter
     *
     * @param wire Alambre a ser escalado
     * @param factor Factor de escalamiento
     * @param keepDiameter Indica si el diámetro será modificado, de acuerdo a
     * la razón LD original, o bien será mantenido a su valor original
     *
     * @return Alambre escalado por un factor
     */
    public Wire scale(Wire wire, double factor, boolean keepDiameter) {

        wire.setX1(wire.getX1() * factor);
        wire.setY1(wire.getY1() * factor);
        wire.setZ1(wire.getZ1() * factor);
        wire.setX2(wire.getX2() * factor);
        wire.setY2(wire.getY2() * factor);
        wire.setZ2(wire.getZ2() * factor);
        double ld = wire.getVector().getLength() / wire.getRadius();
        if (!keepDiameter) {
            wire.setRadius(wire.getVector().getLength() / ld);
        }
        setgScaleFactor(factor);
        return wire;
    }

    /**
     * Actualiza la tabla de geometrías, luego de la modificación de alguno de
     * sus registros
     *
     * @param global Objeto de la clase Global
     * @param model Objeto DefaultTableModel correspondiente al modelo de la
     * tabla de geometría del panel Geometry
     */
    public static void updateTable(DefaultTableModel model, Global global) {
        model.setRowCount(0);
        double factor = global.unit2UpperFactor();
        for (Wire wire : global.getgWires()) {
            if (wire.getNumber() == global.getCurrentSourceTag()) {
                continue;
            }
            Object[] rowData = {(wire.getNumber() + "").replace(",", "."), (wire.getX1() + "").replace(",", "."), (wire.getY1() + "").replace(",", "."), (wire.getZ1() + "").replace(",", "."), (wire.getX2()
                + "").replace(",", "."), (wire.getY2() + "").replace(",", "."), (wire.getZ2() + "").replace(",", "."), ((wire.getRadius() * factor) + "").replace(",", "."), (wire.getSegs() + "").replace(",", ".")};
            model.addRow(rowData);
        }
        model.fireTableDataChanged();
        GeometryPanel.getInfo().setText(setInfoText(1, global.getgWires(), global));
    }

    /**
     * Devuelve la etiqueta del último alambre generado en la simulación
     *
     * @return Etiqueta del ultimo alambre generado
     */
    public int getLastWireNumber() {
        if (getgWires().size() > 0) {
            return getgWires().get(getgWires().size() - 1).getNumber();
        } else {
            return 0;
        }
    }

    /**
     * Abre un archivo de extensión .nec con la descripción de los parámetros
     * necesarios para la ejecución de la simulación.
     *
     * @return Arreglo de objetos del tipo String cuyos elementos
     */
    public ArrayList<String> openFile() {
        ArrayList<String> necFileText = new ArrayList<String>();
        JFileChooser selectfile = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("NEC Files", "nec");
        selectfile.setFileFilter(filter);
        selectfile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = selectfile.showOpenDialog(null);
        if (result != 1) {
            File necFile = selectfile.getSelectedFile();
            if (necFile == null || necFile.getName().isEmpty()) {
                errorMessage("Message.wrongfileTitle", "Message.wrongfile");
                return null;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(necFile))) {

                String sCurrentLine;

                while ((sCurrentLine = br.readLine()) != null) {
                    necFileText.add(sCurrentLine);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return necFileText;
        } else {
            return null;
        }
    }

    /**
     * Genera un archivo de texto, con formato NEC, con la descripción de los
     * parámetros geométricos y electromagnéticos de la simulación, en forma de
     * comandos interpretables por NEC.
     *
     * @return Arreglo de objetos de la clase String, donde cada elemento
     * corresponde a un comando interpretable por NEC.
     */
    public ArrayList<String> saveFile() {
        ArrayList<String> necFileText = new ArrayList<String>();
        String path = "";

        if (!isDirectorySet()) {
            JFileChooser selectfile = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("NEC Files", "nec");
            selectfile.setFileFilter(filter);
            selectfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = selectfile.showSaveDialog(null);
            File necFile = selectfile.getSelectedFile();
            if (necFile == null || necFile.getName().isEmpty()) {
                return null;
            }
            if (FilenameUtils.getExtension(necFile.getName()).equalsIgnoreCase("nec")) {
            } else {
                necFile = new File(necFile.toString() + ".nec");
            }
            path = necFile.getAbsolutePath();
        } else {
            String fileName = inputMessage("Nombre del archivo a guardar en: " + "\n" + getgDirectory());
            String fn = "";
            if (!fileName.isEmpty()) {
                String input[] = fileName.split("[.]");
                if (input.length > 1) {
                    fn = (input[0]) + ".nec";
                } else {
                    fn = fileName + ".nec";
                }
                path = getgDirectory() + File.separator + fn;
            } else {
                fn = "input.nec";
                path = getgDirectory() + File.separator + fn;
            }
        }

        boolean is3DPlot = (currentPlotType == PLOT3D);
        generateInputFile(is3DPlot, path, true);

        return necFileText;
    }

    /**
     * Exporta los resultados de los experimentos realizados a través del módulo
     * LabNEC, en forma de un archivo de extensión .txt
     *
     * @param results Arreglo de objetos de la clase String con los resultados
     * de los experimentos realizados en el módulo LabNEC
     * @param labId Identificador de la práctica de laboratorio a exportar
     * @return true si la generación fue exitosa, de lo contrario, devuelve
     * false.
     */
    public boolean exportResult(ArrayList<String> results, int labId) {

        JFileChooser selectfile = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
        selectfile.setFileFilter(filter);
        selectfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = selectfile.showSaveDialog(null);
        File necFile = selectfile.getSelectedFile();
        if (necFile == null || necFile.getName().isEmpty()) {
            return false;
        }
        if (FilenameUtils.getExtension(necFile.getName()).equalsIgnoreCase("txt")) {
        } else {
            necFile = new File(necFile.toString() + ".txt");
        }

        String path = necFile.getAbsolutePath();

        return generateResultFile(results, path, labId);
    }

    /**
     * Exporta los parámetros inherentes a la simulación (Corrientes, Fuentes,
     * Cargas, Presupuesto de Potencia) en un archivo de extensión .txt
     *
     * @param data Arreglo de objetos de la clase String con los parámetros a
     * ser exportados a un archivo de texto
     */
    public void saveDataFile(ArrayList<String> data) {
        ArrayList<String> necFileText = new ArrayList<String>();
        String path = "";
        if (!isDirectorySet()) {
            JFileChooser selectfile = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT Files", "txt");
            selectfile.setFileFilter(filter);
            selectfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = selectfile.showSaveDialog(null);
            if (result != 1) {
                File necFile = selectfile.getSelectedFile();
                if (necFile == null || necFile.getName().isEmpty()) {
                    errorMessage("Message.wrongfileTitle", "Message.wrongfile");
                }
                if (FilenameUtils.getExtension(necFile.getName()).equalsIgnoreCase("txt")) {
                } else {
                    necFile = new File(necFile.toString() + ".txt");
                }
                path = necFile.getAbsolutePath();
            } else {
                String fileName = inputMessage("Nombre del archivo a guardar en: " + "\n" + getgDirectory());
                String fn = "";
                if (!fileName.isEmpty()) {
                    String input[] = fileName.split("[.]");
                    if (input.length > 1) {
                        fn = (input[0]) + ".txt";
                    } else {
                        fn = fileName + ".txt";
                    }
                    path = getgDirectory() + File.separator + fn;
                } else {
                    fn = "data.txt";
                    path = getgDirectory() + File.separator + fn;
                }
            }
            generateDataFile(data, path);
        }
    }

    /**
     * Devuelve la ruta del directorio seleccionado por el usuario a través de
     * la ventana JFileChooser
     *
     * @return Ruta del directorio seleccionado por el usuario
     */
    public String getDirectory() {
        JFileChooser selectfile = new JFileChooser();
        selectfile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = selectfile.showSaveDialog(null);
        File necFile = selectfile.getSelectedFile();

        if (necFile == null || necFile.getName().isEmpty()) {
            errorMessage("Message.wrongfileTitle", "Message.wrongfile");
            return "";
        }
        String path = necFile.getAbsolutePath();
        setgDirectory(path);
        setDirectorySet(true);

        char sep = File.separatorChar;
        String p = path.substring(path.lastIndexOf(sep));

        String resp = "~" + p;
        return resp;
    }

    /**
     * Genera el archivo de entrada de extensión .NEC a partir de los parámetros
     * geométricos y electromagnéticos especificados en el programa convertidos
     * a comandos interpretables por el código NEC2
     *
     * @param is3DPlot Indica si la simulación es generada para obtener un
     * gráfico de diagrama de radiación 3D
     */
    public void generateInputFile(boolean is3DPlot) {
        ArrayList<String> necCard = new ArrayList<String>();

        if (getgWires().size() > 0) {

            necCard.add("CM UNIVERSIDAD DE CARABOBO");
            necCard.add("CM ESCUELA DE INGENIERÍA DE TELECOMUNICACIONES");
            necCard.add("CM SIMULACION UCNEC");
            necCard.add("CM -------------------------------------------");
            necCard.add("CE");

            changeUnit(Global.METER, getCurrentUnit());

            ArrayList<String> geometry = Wire.toString(getgWires());
            for (String wire : geometry) {
                necCard.add(wire);
            }
            necCard.add("GE 0");

            if (getgLoad().size() > 0) {
                ArrayList<String> load = Load.toString(getgLoad());
                for (String ld : load) {
                    necCard.add(ld);
                }
            }

            if (getgTl().size() > 0) {
                ArrayList<String> tl = Tl.toString(getgTl());
                for (String tls : tl) {
                    necCard.add(tls);
                }
            }

            if (getgWl().size() > 0) {
                ArrayList<String> wl = new ArrayList<String>();
                for (String wireLoss : wl) {
                    necCard.add(wireLoss);
                }
            }

            if (getgFrequency().getFreq() != 0.0) {
                necCard.add(Frequency.toString(getgFrequency()));
            }

            if (getgGround().size() > 0) {
                ArrayList<String> ground = Ground.toString(getgGround());
                for (String gnd : ground) {
                    necCard.add(gnd);
                }
            }

            if (getgSource().size() > 0) {
                ArrayList<String> source = Source.toString(getgSource());
                for (String src : source) {
                    necCard.add(src);
                }
            }

            if (getgNetwork().size() > 0) {
                ArrayList<String> network = Network.toString(getgNetwork());
                for (String net : network) {
                    necCard.add(net);
                }
            }

            if (getgRadiationPattern().isSetted()) {
                necCard.add(RadiationPattern.toString(getgRadiationPattern(), is3DPlot));
            } else {
                necCard.add(RadiationPattern.trivialRP());
            }
            necCard.add("EN");

            Writer writer = null;

            try {
                String path = NECFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String dir = new File(path).getParentFile().getPath() + File.separator + "kernel" + File.separator + "input.nec";
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(dir), "utf-8"));
                for (String line : necCard) {
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, Global.getMessages().getString("Global.noEnoughInfo"), Global.getMessages().getString("Global.noEnoughInfoTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Genera el archivo de entrada de extensión .NEC a partir de los parámetros
     * geométricos y electromagnéticos especificados en el programa convertidos
     * a comandos interpretables por el código NEC2. Éste método está
     * particularizado para guardar la actual simulación en un archivo .nec
     *
     * @param is3DPlot Indica si la simulación es generada para obtener un
     * gráfico de diagrama de radiación 3D
     * @param path Directorio donde se guardará el archivo de entrada
     * @param toSave Indica si la generación del archivo de entrada está
     * dirigida a guardar la actual simulación
     */
    public void generateInputFile(boolean is3DPlot, String path, boolean toSave) {
        ArrayList<String> necCard = new ArrayList<String>();

        if (getgWires().size() > 0) {

            necCard.add("CM UNIVERSIDAD DE CARABOBO");
            necCard.add("CM ESCUELA DE INGENIERÍA DE TELECOMUNICACIONES");
            necCard.add("CM UCNEC SIMULATION");
            necCard.add("CE");

            ArrayList<Wire> processedWires = changeUnit(Global.METER, getCurrentUnit());

            ArrayList<String> geometry = Wire.toString(processedWires);
            for (String wire : geometry) {
                necCard.add(wire);
            }
            necCard.add("GE 0");

            if (getgLoad().size() > 0) {
                ArrayList<String> load = Load.toString(getgLoad());
                for (String ld : load) {
                    necCard.add(ld);
                }
            }

            if (getgTl().size() > 0) {
                ArrayList<String> tl = Tl.toString(getgTl());
                for (String tls : tl) {
                    necCard.add(tls);
                }
            }

            if (getgWl().size() > 0) {
                ArrayList<String> wl = new ArrayList<String>();
                for (String wireLoss : wl) {
                    necCard.add(wireLoss);
                }
            }

            if (getgFrequency().getFreq() != 0.0) {
                necCard.add(Frequency.toString(getgFrequency()));
            }

            if (getgGround().size() > 0) {
                ArrayList<String> ground = Ground.toString(getgGround());
                for (String gnd : ground) {
                    necCard.add(gnd);
                }
            }

            if (getgSource().size() > 0) {
                ArrayList<String> source = Source.toString(getgSource());
                for (String src : source) {
                    necCard.add(src);
                }
            }

            if (getgNetwork().size() > 0) {
                ArrayList<String> network = Network.toString(getgNetwork());
                for (String nt : network) {
                    necCard.add(nt);
                }
            }

            if (getgRadiationPattern().isSetted()) {
                necCard.add(RadiationPattern.toString(getgRadiationPattern(), is3DPlot));
            }

            if (toSave) {
                necCard.add(Unit.asString(getgUnit()));
            }
            necCard.add("EN");

            Writer writer = null;
            try {
                String dir = new File(path).getPath();
                writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(dir), "utf-8"));
                for (String line : necCard) {
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    writer.close();
                    saveFileMessages();
                } catch (Exception ex) {/*ignore*/
                    ex.printStackTrace();
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, Global.getMessages().getString("Global.noEnoughInfo"), Global.getMessages().getString("Global.noEnoughInfoTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Genera el archivo de entrada de extensión .NEC a partir de los parámetros
     * geométricos y electromagnéticos especificados en el programa convertidos
     * a comandos interpretables por el código NEC2. Éste método está
     * particularizado para la generación de un código QR
     *
     * @return Arreglo de objetos de la clase String con los parámetros de
     * simulación convertidos en comandos NEC
     */
    public ArrayList<String> generateQRData() {
        ArrayList<String> necCard = new ArrayList<String>();

        if (getgWires().size() > 0) {

            necCard.add("CM UNIVERSIDAD DE CARABOBO");
            necCard.add("CM ESCUELA DE INGENIERÍA DE TELECOMUNICACIONES");
            necCard.add("CM UCNEC SIMULATION");
            necCard.add("CE");

            ArrayList<String> geometry = Wire.toString(getgWires());
            for (String wire : geometry) {
                necCard.add(wire);
            }
            necCard.add("GE 0");

            if (getgLoad().size() > 0) {
                ArrayList<String> load = Load.toString(getgLoad());
                for (String ld : load) {
                    necCard.add(ld);
                }
            }

            if (getgTl().size() > 0) {
                ArrayList<String> tl = Tl.toString(getgTl());
                for (String tls : tl) {
                    necCard.add(tls);
                }
            }

            if (getgWl().size() > 0) {
                ArrayList<String> wl = new ArrayList<String>();
                for (String wireLoss : wl) {
                    necCard.add(wireLoss);
                }
            }

            if (getgFrequency().getFreq() != 0.0) {
                necCard.add(Frequency.toString(getgFrequency()));
            }

            if (getgGround().size() > 0) {
                ArrayList<String> ground = Ground.toString(getgGround());
                for (String gnd : ground) {
                    necCard.add(gnd);
                }
            }

            if (getgSource().size() > 0) {
                ArrayList<String> source = Source.toString(getgSource());
                for (String src : source) {
                    necCard.add(src);
                }
            }

            if (getgNetwork().size() > 0) {
                ArrayList<String> network = Network.toString(getgNetwork());
                for (String nt : network) {
                    necCard.add(nt);
                }
            }

            if (getgRadiationPattern().isSetted()) {
                boolean isPlot3D = currentPlotType == Global.PLOT3D;
                necCard.add(RadiationPattern.toString(getgRadiationPattern(), isPlot3D));
            }
            necCard.add(Unit.asString(getgUnit()));
            necCard.add("EN");
            return necCard;
        } else {
            return null;
        }
    }

    /**
     * Genera el encabezado y el resúmen que conforman los archivos con los
     * resultados de las diferentes prácticas de laboratorio
     *
     * @param results Arreglo de objetos de la clase String con los resultados
     * de determinada práctica de laboratorio
     * @param path Directorio donde se guardará el archivo de extensión .txt con
     * los resultados de las prácticas de laboratorio
     * @param labId Identificador de la práctica de laboratorio
     * @return true si la generación del archivo fue exitosa, de lo contrario,
     * devuelve false
     */
    public boolean generateResultFile(ArrayList<String> results, String path, int labId) {
        ArrayList<String> resp = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        String date = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
        String time = cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);

        Writer writer = null;
        try {
            String[] cols = null;
            String dir = new File(path).getPath();
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(dir), "utf-8"));
            if (labId == Global.LAB11 || labId == Global.LAB12 || labId == Global.LAB13 || labId == Global.LAB14) {
                writer.write("Resultados del Laboratorio I de Antenas y Propagación" + System.lineSeparator());
            } else if (labId == Global.LAB21) {
                writer.write("Resultados del Laboratorio II de Antenas y Propagación" + System.lineSeparator());
            } else if (labId == Global.LAB31) {
                writer.write("Resultados del Laboratorio III de Antenas y Propagación" + System.lineSeparator());
            } else if (labId == Global.LAB41) {
                writer.write("Resultados del Laboratorio IV de Antenas y Propagación" + System.lineSeparator());
            } else if (labId == Global.LAB21) {
                writer.write("Resultados del Laboratorio V de Antenas y Propagación" + System.lineSeparator());
            }
            switch (labId) {
                case Global.LAB11:
                    writer.write("Experimento 1" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    break;
                case Global.LAB12:
                    writer.write("Experimento 2" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    break;
                case Global.LAB13:
                    writer.write("Experimento 3" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    break;
                case Global.LAB14:
                    writer.write("Experimento 4" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    break;
                case Global.LAB21:
                    writer.write("Experimento I" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    if (getgPolarization() != null) {
                        writer.write("Ángulo de Inclinación" + ": " + getgPolarization().getTau() + System.lineSeparator());
                        writer.write("Razón Axial (Veces)" + ": " + getgPolarization().getRa_times() + System.lineSeparator());
                        writer.write("Razón Axial (dB)" + ": " + getgPolarization().getRa_db() + System.lineSeparator());
                        writer.write("Razón de Elipticidad" + ": " + getgPolarization().getElipticityRatio() + System.lineSeparator());
                        writer.write("Coeficiente de Elipticidad (dB)" + ": " + getgPolarization().getElipticityCoeff() + System.lineSeparator());
                        writer.write("Magnitud del Componente Principal" + ": " + getgPolarization().getMcp() + System.lineSeparator());
                        writer.write("Magnitud del Componente Cruzado" + ": " + getgPolarization().getMcc() + System.lineSeparator());
                    }
                    break;
                case Global.LAB31:
                    writer.write("Experimento I" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    break;
                case Global.LAB41:
                    writer.write("Experimento I" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    break;
                case Global.LAB42:
                    writer.write("Experimento II" + System.lineSeparator());
                    writer.write("Fecha: " + date + " Hora: " + time);
                    writer.write(System.lineSeparator());
                    writer.write(System.lineSeparator());
                    cols = results.get(0).split(",");
                    for (int i = 1; i < results.size(); i++) {
                        String[] val = results.get(i).split(",");
                        for (int j = 0; j < val.length; j++) {
                            writer.write(cols[j] + ": " + val[j]);
                            writer.write(System.lineSeparator());
                        }
                        writer.write(System.lineSeparator());
                    }
                    break;
                default:
                    throw new AssertionError();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                writer.close();
                saveFileMessages();
                return true;
            } catch (Exception ex) {/*ignore*/
                ex.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Genera un archivo de formato .txt a partir de la información suministrada
     * a través del arreglo de objetos String del parámetro data
     *
     * @param data Arreglo de objetos de la clase String con la información a
     * ser exportada en forma de archivo de texto
     * @param path Directorio donde será generado el archivo de texto
     */
    public void generateDataFile(ArrayList<String> data, String path) {
        Writer writer = null;
        try {
            String dir = new File(path).getPath();
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(dir), "utf-8"));
            for (String line : data) {
                writer.write(line);
                writer.write(System.getProperty("line.separator"));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {/*ignore*/
                ex.printStackTrace();
            }
        }
    }

    /**
     * Carga un archivo de extensión .nec con la descripción de los parámetros
     * geométricos y electromagnéticos de una simulación, para luego cargarlos
     * en el programa.
     *
     * @param data Arreglo de objetos de la clase String con los comandos NEC
     * correspondientes al archivo de descripción .nec
     * @param global Objeto de la clase Global
     */
    public static void loadInputFile(ArrayList<String> data, Global global) {
        ArrayList<Wire> wires = new ArrayList<Wire>();
        Frequency freq = new Frequency();
        ArrayList<Ground> ground = new ArrayList<Ground>();
        ArrayList<Load> load = new ArrayList<Load>();
        RadiationPattern rp = new RadiationPattern();
        ArrayList<Source> sources = new ArrayList<Source>();
        ArrayList<Tl> tls = new ArrayList<Tl>();
        ArrayList<WireLoss> wls = new ArrayList<WireLoss>();
        ArrayList<Network> nt = new ArrayList<Network>();
        global.setCurrentSourceTag(0);
        Unit unit = new Unit();
        for (String line : data) {
            String[] aux = line.split("\\s+");

            if (aux[0].equalsIgnoreCase("GW")) {
                wires.add(Wire.fromString(line, global));
            }
            if (aux[0].equalsIgnoreCase("FR")) {
                freq = Frequency.fromString(line);
            }
            if (aux[0].equalsIgnoreCase("GN")) {
                ground.add(Ground.fromString(line));
            }
            if (aux[0].equalsIgnoreCase("LD")) {
                String[] ln = line.split(",");
                if (Load.fromString(line) != null) {
                    load.add(Load.fromString(line));
                } else {
                    wls.add(WireLoss.fromString(line));
                }
            }

            if (aux[0].equalsIgnoreCase("NT")) {
                Network network = Network.fromString(line);
                global.setCurrentSourceTag(network.getTagNumberPort1());
                nt.add(network);
            }
            if (aux[0].equalsIgnoreCase("RP")) {
                rp = RadiationPattern.fromString(line);
                global.setCurrentPlotType(global.calculatePlotType(rp));
            }

            if (aux[0].equalsIgnoreCase("EX")) {
                sources.add(Source.fromString(line));
            }
            if (aux[0].equalsIgnoreCase("TL")) {
                tls.add(Tl.fromString(line));
            }

            if (aux[0].equalsIgnoreCase("GS")) {
                unit = Unit.fromString(line);
            }
        }

        global.setCurrentUnit(unit.getUnit());

        global.setgWires(wires);
        global.setgGround(ground);
        global.setgLoad(load);
        global.setgFrequency(freq);
        global.setgRadiationPattern(rp);
        global.setgSource(sources);
        global.setgTl(tls);
        global.setgWl(wls);
        global.setgUnit(unit);
        global.setgNetwork(nt);

        global.InfoMessages("Messages.loadAntenna.title", "Messages.loadAntenna.OK");

    }

    /**
     * Importa un archivo de extensión .nec con la descripción de los parámetros
     * geométricos y electromagnéticos de una simulación, incorporándo estos
     * datos a las variables existentes durante la ejecución del programa
     *
     * @param data Arreglo de objetos de la clase String con los comandos NEC
     * correspondientes al archivo de descripción .nec
     * @param global Objeto de la clase Global
     */
    public static void importInputFile(ArrayList<String> data, Global global) {
        ArrayList<Wire> wires = new ArrayList<Wire>();
        ArrayList<Wire> newWires = new ArrayList<Wire>();
        ArrayList<Network> nt = new ArrayList<Network>();
        int iSourceTag = -1;

        int factor = global.getgWires().size();
        for (String line : data) {
            String[] aux = line.split("\\s+");

            if (aux[0].equalsIgnoreCase("GW")) {
                newWires.add(Wire.importFromString(line, 0));
            }

            if (aux[0].equalsIgnoreCase("NT")) {
                Network network = Network.importFromString(line, 0);
                iSourceTag = network.getTagNumberPort1();
            }
        }

        int currentIndex = -1;
        for (int i = 0; i < newWires.size(); i++) {
            Wire nWire = newWires.get(i);
            if (nWire.getNumber() == iSourceTag) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex != -1) {
            newWires.remove(currentIndex);
        }

        double convertionFactor = global.convertUnits(Global.METER, global.currentUnit);
        scaleWires(newWires, convertionFactor);

        for (Wire nNewWire : newWires) {
            factor++;
            nNewWire.setNumber(factor);
            global.getgWires().add(nNewWire);
        }
    }

    /**
     * Escala a todos los elementos de un arreglo de objetos de la clase Wire
     * por un factor determinado en el parámetro factor, este escalamiento
     * modifica también el valor del diámetro por el factor indicado
     *
     * @param wires Arreglo de objetos de la clase Wire
     * @param factor Factor de escalamiento aplicado
     */
    public static void scaleWires(ArrayList<Wire> wires, double factor) {
        for (Wire wire : wires) {

            wire.setX1(Global.decimalFormat(wire.getX1() * factor));
            wire.setY1(Global.decimalFormat(wire.getY1() * factor));
            wire.setZ1(Global.decimalFormat(wire.getZ1() * factor));
            wire.setX2(Global.decimalFormat(wire.getX2() * factor));
            wire.setY2(Global.decimalFormat(wire.getY2() * factor));
            wire.setZ2(Global.decimalFormat(wire.getZ2() * factor));
            wire.setRadius(decimalFormat(wire.getRadius() * factor));

        }
    }

    /**
     * Determina el tipo de gráfica descrita a partir de los datos de un objeto
     * de la clase RadiationPattern
     *
     * @param rp Objeto de la clase RadiationPattern
     * @return Indicativo del tipo de gráfica descrito en el objeto
     * RadiationPattern (Azimuth, Elevación o Tridimensional)
     */
    private static int calculatePlotType(RadiationPattern rp) {
        if (rp.getIncTheta() == 0 && rp.getIncPhi() != 0) {
            return Global.AZIMUTHPLOT;
        } else if (rp.getIncTheta() != 0 && rp.getIncPhi() == 0) {
            return Global.ELEVATIONPLOT;
        } else {
            return Global.PLOT3D;
        }
    }

    /**
     * Calcula el valor de la apertura de haz a partir de los extremos del span
     * alrededor de la posición de |Etotal| máxima
     *
     * @return Valor de la apertura de Haz
     */
    public double calculateAperture() {
        ArrayList<SphericalCoordenate> apertures = getgAper();
        switch (currentPlotType) {
            case AZIMUTHPLOT:
                if (apertures.size() >= 2) {
                    double angle1 = apertures.get(0).getTheta();
                    double angle2 = apertures.get(1).getTheta();
                    if (angle1 > angle2) {
                        return angle1 - angle2;
                    } else {
                        return angle2 - angle1;
                    }
                } else {
                    return 0;
                }
            case ELEVATIONPLOT:
                if (apertures.size() >= 2) {
                    double angle1 = apertures.get(0).getPhi();
                    double angle2 = apertures.get(1).getPhi();
                    if (angle1 > angle2) {
                        return angle1 - angle2;
                    } else {
                        return angle2 - angle1;
                    }
                } else {
                    return 0;
                }
            default:
                throw new AssertionError();
        }
    }

    /**
     * Calcula el valor de la ganancia de la antena, en una posición específica,
     * a partir de la ecuación 4 * pi * U / Pin donde U = e / 2 eta e = r^2 eta
     * = 120 * pi pin = Potencia de entrada
     *
     * @param point Objeto SphericalCoordenate correspondiente a la posición
     * donde se desea calcula la ganancia de la antena
     *
     * @return Valor de la ganancia de la antena en la posición del objeto
     * SphericalCoordenate
     */
    public double getGainAt(SphericalCoordenate point) {
        double eta = 120 * Math.PI;
        double e = Math.pow(point.getRho(), 2);
        double u = e / (2 * eta);
        double pin = getgPower().getInputPower();
        if (e != 0) {
            return (4 * Math.PI * u) / pin;
        } else {
            return 1E-7;
        }
    }

    /**
     * Devuelve información sobre la configuración actual de pérdida en los
     * alambres que componentes la geometría de la simulación
     *
     * @return Texto con la información sobre la configuración actual de pérdida
     * en los alambres
     */
    public String wirelossInformation() {
        String resp = "";
        if (getgWl().size() > 0) {
            WireLoss wireLoss = getgWl().get(0);
            if (wireLoss.getType() == 6) {
                setCurrentWireloss(Global.CUSTOM);
                resp = "Personalizado";
            } else if (wireLoss.getType() == 5) {
                if (wireLoss.getConductivity() == 1 / 1.74E-08) {
                    setCurrentWireloss(Global.COOPER);
                    resp = "Cobre";
                } else if (wireLoss.getConductivity() == 1 / 4E-08) {
                    setCurrentWireloss(Global.ALUMINIUM);
                    resp = "Aluminio";
                } else if (wireLoss.getConductivity() == 1 / 1.14E-07) {
                    setCurrentWireloss(Global.TIN);
                    resp = "Lata";
                } else if (wireLoss.getConductivity() == 1 / 6E-08) {
                    setCurrentWireloss(Global.ZINC);
                    resp = "Zinc";
                }
            }
        } else {
            setCurrentWireloss(Global.LOSSLESS);
            resp = "Sin Pérdidas";
        }
        return resp;
    }

    /**
     * Devuelve la representación textual de la unidad de longitud empleada en
     * la simulación
     *
     * @return Representación textual de la unidad de longitud empleada en la
     * simulación
     */
    public String unit2String() {
        int currentUnit = getCurrentUnit();

        switch (currentUnit) {
            case 0:
                return Global.getMessages().getString("Global.units.meter");
            case 1:
                return Global.getMessages().getString("Global.units.milimeter");
            case 2:
                return Global.getMessages().
                        getString("Global.units.feet");
            case 3:
                return Global.getMessages().
                        getString("Global.units.inch");
            case 4:
                return Global.getMessages().
                        getString("Global.units.wavelength");
            default:
                return "";
        }
    }

    /**
     * Devuelve la representación textual, abreviada, de la unidad de longitud
     * empleada en la simulación
     *
     * @return Representación textual, abreviada, de la unidad de longitud
     * empleada en la simulación
     */
    public String unit2ShortString() {
        int currentUnit = getCurrentUnit();

        switch (currentUnit) {
            case 0:
                return "m";
            case 1:
                return "mm";
            case 2:
                return "ft";
            case 3:
                return "in";
            case 4:
                return "wl";
            default:
                return "";
        }
    }

    /**
     * Devuelve la representación textual de la unidad de longitud inferior a la
     * empleada en la simulación; por ejemplo metros -- milímetros
     *
     * @return Representación textual de la unidad de longitud inferior a la
     * empleada en la simulación.
     */
    public String unit2LowerString() {
        int currentUnit = getCurrentUnit();

        switch (currentUnit) {
            case Global.METER:
            case Global.MILIMETER:
                return "mm";
            case Global.FEET:
            case Global.INCH:
                return "in";
            case Global.WAVELENGTH:
                return "wl";
            default:
                return "";
        }
    }

    /**
     * Devuelve el factor de conversión de la unidad de longitud inferior a la
     * empleada en la simulación; por ejemplo metros -- milímetros -- 0.001
     *
     * @return Factor de conversión de la unidad de longitud inferior a la
     * empleada en la simulación.
     */
    public double unit2LowerFactor() {
        int currentUnit = getCurrentUnit();

        switch (currentUnit) {
            case 0:
                return 0.001;
            case 1:
                return 0.001;
            case 2:
                return 0.0254;
            case 3:
                return 0.0254;
            case 4:
                return 1;
            default:
                return 1;
        }
    }

    /**
     * Devuelve el factor de conversión de la unidad de longitud superior a la
     * empleada en la simulación; por ejemplo mílimetros -- metros -- 1000
     *
     * @return Factor de conversión de la unidad de longitud superior a la
     * empleada en la simulación
     */
    public double unit2UpperFactor() {
        int currentUnit = getCurrentUnit();

        switch (currentUnit) {
            case 0:
                return 1000;
            case 1:
                return 1000;
            case 2:
                return 1 / 0.0254;
            case 3:
                return 1 / 0.0254;
            case 4:
                return 1;
            default:
                return 1;
        }
    }

    /**
     * Devuelve el factor de conversión de una unidad from a la unidad to
     *
     * @param from Unidades desde la que se desea realizar la conversión
     * @param to Unidades hacia la que se desea realizar la conversión
     * @return Factor de conversión de una unidad from a la unidad to
     */
    public double convertUnits(int from, int to) {
        switch (from) {
            case Global.METER:
                switch (to) {
                    case Global.METER:
                        return 1;
                    case Global.MILIMETER:
                        return 1000;
                    case Global.FEET:
                        return 3.281;
                    case Global.INCH:
                        return 39.37;
                    case Global.WAVELENGTH:
                        return (1 / getWavelength());
                    default:
                        throw new AssertionError();
                }

            case Global.MILIMETER:
                switch (to) {
                    case Global.METER:
                        return 0.001;
                    case Global.MILIMETER:
                        return 1;
                    case Global.FEET:
                        return 0.003281;
                    case Global.INCH:
                        return 0.03937;
                    case Global.WAVELENGTH:
                        return (1 / getWavelength()) * 0.001;
                    default:
                        throw new AssertionError();
                }
            case Global.FEET:
                switch (to) {
                    case Global.METER:
                        return 0.3048;
                    case Global.MILIMETER:
                        return 304.785;
                    case Global.FEET:
                        return 1;
                    case Global.INCH:
                        return 12;
                    case Global.WAVELENGTH:
                        return (1 / getWavelength()) * 0.3048;
                    default:
                        throw new AssertionError();
                }
            case Global.INCH:
                switch (to) {
                    case Global.METER:
                        return 0.0254;
                    case Global.MILIMETER:
                        return 25.4;
                    case Global.FEET:
                        return 0.0833;
                    case Global.INCH:
                        return 1;
                    case Global.WAVELENGTH:
                        return (1 / getWavelength()) * 0.0254;
                    default:
                        throw new AssertionError();
                }
            case Global.WAVELENGTH:
                switch (to) {
                    case Global.METER:
                        return getWavelength();
                    case Global.MILIMETER:
                        return getWavelength() * 1000;
                    case Global.FEET:
                        return getWavelength() * 3.281;
                    case Global.INCH:
                        return getWavelength() * 39.37;
                    case Global.WAVELENGTH:
                        return 1;
                    default:
                        throw new AssertionError();
                }

            default:
                throw new AssertionError();
        }
    }

    /**
     * Devuelve la representación textual del tipo de patrón de radiación
     * configurado para la simulación
     *
     * @return Representación textual del tipo de patrón de radiación
     * configurado para la simulación
     */
    public String plotType2String() {
        int currentUnit = getCurrentPlotType();
        RadiationPattern rp = getgRadiationPattern();
        String type = "";
        switch (currentUnit) {
            case Global.PLOT3D:
                type = Global.getMessages().getString("RP.plot3d") + " (Veces)";
                setCurrentPlotType(Global.PLOT3D);
                break;
            case Global.AZIMUTHPLOT:
                type = Global.getMessages().getString("RP.azimuth") + " @ " + rp.getInTheta()
                        + " " + Global.getMessages().getString("TransformationPanel.rotation.degrees.label")
                        + "  -  " + rp.getIncPhi()
                        + " " + Global.getMessages().getString("RP.steps") + " (Veces)";
                setCurrentPlotType(Global.AZIMUTHPLOT);
                break;
            case Global.ELEVATIONPLOT:
                type = Global.getMessages().
                        getString("RP.elevacion") + " @ " + rp.getInPhi()
                        + " " + Global.getMessages().getString("TransformationPanel.rotation.degrees.label")
                        //                        + "  -  " + (-1 * rp.getIncTheta())
                        + "  -  " + (rp.getIncTheta())
                        + " " + Global.getMessages().getString("RP.steps") + " (Veces)";
                setCurrentPlotType(Global.ELEVATIONPLOT);
                break;
            default:
                type = "";
                break;
        }
        return type;
    }

    /**
     * Devuelve la representación textual del ángulo de iteración empleado para
     * la generación de la gráfica del patrón de radiación configurado para la
     * simulación
     *
     * @return Representación textual del ángulo de iteración empleado para la
     * generación de la gráfica del patrón de radiación configurado para la
     * simulación
     */
    public String plotTypeAngle2String() {
        int currentUnit = getCurrentPlotType();
        RadiationPattern rp = getgRadiationPattern();
        String type = "";
        switch (currentUnit) {
            case Global.PLOT3D:
                type = "N/A";
                break;
            case Global.AZIMUTHPLOT:
                type = "Elevación (Grados)";
                break;
            case Global.ELEVATIONPLOT:
                type = "Azimuth (Grados)";
                break;
            default:
                type = "";
                break;
        }

        return type;
    }

    /**
     * Efectúa la conversión de unidades de los elementos que componen la
     * descripción geométrica de la simulación.
     *
     * @param newUnit Unidad a la cual se desean convertir los alambres que
     * constituyen la descripción geométrica de la simulación
     * @param oldUnit Unidades en las que están descritos los alambres que
     * constituyen la descripción geométrica de la simulación
     * @return Arreglo de objetos de la clase Wire
     */
    public ArrayList<Wire> changeUnit(int newUnit, int oldUnit) {

        ArrayList<Wire> ans = new ArrayList<Wire>();
        double factor = convertUnits(oldUnit, newUnit);
        double lowerFactor = unit2UpperFactor();
        ArrayList<Wire> wires = getgWires();
        for (Wire nwire : wires) {
            if (nwire.getNumber() == getCurrentSourceTag()) {
                continue;
            }
            Wire wire = new Wire();
            wire.setNumber(nwire.getNumber());
            wire.setSegs(nwire.getSegs());
            wire.setX1(decimalFormat(nwire.getX1() * factor));
            wire.setY1(decimalFormat(nwire.getY1() * factor));
            wire.setZ1(decimalFormat(nwire.getZ1() * factor));
            wire.setX2(decimalFormat(nwire.getX2() * factor));
            wire.setY2(decimalFormat(nwire.getY2() * factor));
            wire.setZ2(decimalFormat(nwire.getZ2() * factor));
            switch (oldUnit) {
                case Global.METER:
                case Global.MILIMETER:
                    wire.setRadius(decimalFormat(nwire.getRadius()));
                    break;
                case Global.INCH:
                case Global.FEET:
                    //  lowerFactor = convertUnits(Global.INCH, newUnit);
                    wire.setRadius(decimalFormat(nwire.getRadius()));
                    break;
                case Global.WAVELENGTH:
                    //   lowerFactor = convertUnits(Global.WAVELENGTH, newUnit);
                    wire.setRadius(decimalFormat(nwire.getRadius()));
                    break;
                default:
                    throw new AssertionError();
            }
            ans.add(wire);
        }
        for (Wire nwire : wires) {
            if (nwire.getNumber() == getCurrentSourceTag()) {
                ans.add(nwire);
            }
        }
        return ans;
    }

    /**
     * Devuelve el ángulo de iteración común a los elementos que componen al
     * patrón de radiación del tipo azimutal o de elevación
     *
     * @return Ángulo de iteración común a los elementos que componen al patrón
     * de radiación del tipo azimutal o de elevación
     */
    public String getPlotAngle() {
        RadiationPattern rp = getgRadiationPattern();
        switch (currentPlotType) {
            case Global.PLOT3D:
                return "N/A";
            case Global.AZIMUTHPLOT:
                return (rp.getInTheta() + "");
            case Global.ELEVATIONPLOT:
                return (rp.getInPhi() + "");
            default:
                return "";
        }
    }

    /**
     * Devuelve la representación textual de la relación Front to Back en las
     * gráficas de patrón de radiación de tipo azimutal y de elevación
     *
     * @param global Objeto de la clase Global
     * @return Representación textual de la relación Front to Back en las
     * gráficas de patrón de radiación de tipo azimutal y de elevación
     */
    public String fb2String(Global global) {
        if (getCurrentPlotType() != Global.PLOT3D) {
            SphericalCoordenate front = global.getgMax().get(0);
            SphericalCoordenate back = global.getgBack();
            double frontGain = getGainAt(front);
            double backGain = getGainAt(back);
            if (backGain != 0 && !Double.isNaN(Math.log10(frontGain / backGain))) {
                return Global.decimalFormat(10 * Math.log10(frontGain / backGain)) + " dB";
            } else {
                return ">100 dB";
            }
        } else {
            return "N/A";
        }
    }

    /**
     * Devuelve la representación textual de la relación Front to Side en las
     * gráficas de patrón de radiación de tipo azimutal y de elevación
     *
     * @param global Objeto de la clase Global
     * @return Representación textual de la relación Front to Side en las
     * gráficas de patrón de radiación de tipo azimutal y de elevación
     */
    public String fs2String(Global global) {
        if (getCurrentPlotType() != Global.PLOT3D) {
            if (global.getgSide() != null) {
                SphericalCoordenate front = global.getgMax().get(0);
                SphericalCoordenate side = global.getgSide();
                double frontGain = getGainAt(front);
                double sideGain = getGainAt(side);
                if (sideGain != 0 && !Double.isNaN(10 * Math.log10(frontGain / sideGain))) {
                    return Global.decimalFormat(10 * Math.log10(frontGain / sideGain)) + " dB";
                } else {
                    return "N/A";
                }
            } else {
                return "N/A";
            }
        } else {
            return "N/A";
        }

    }

    /**
     * Devuelve la representación textual de la ganancia máxima en la gráficas
     * de patrón de radiación de tipo azimutal y de elevación
     *
     * @param maxValue Arreglo de objetos de la clase SphericalCoordenate
     * correspondientes a los valores máximos del patrón de radiación
     * @return Arreglo de objetos de la clase SphericalCoordenate
     * correspondientes a los valores máximos del patrón de radiación
     */
    public String maxGain2String(ArrayList<SphericalCoordenate> maxValue) {
        String resp = "N/A";
        boolean first = true;
        loop:
        for (SphericalCoordenate sC : maxValue) {
            switch (getCurrentPlotType()) {
                case Global.AZIMUTHPLOT:
                    if (first) {
                        resp = decimalFormat(getGainAt(sC)) + " @ phi: " + sC.getPhi() + " grados";
                        first = false;
                    } else {
                        resp = resp + " | " + decimalFormat(getGainAt(sC)) + " @ phi: " + sC.getPhi() + " grados";
                    }
                    break;
                case Global.ELEVATIONPLOT:
                    if (first) {
                        resp = decimalFormat(getGainAt(sC)) + " @ theta: " + sC.getTheta() + " grados";
                        first = false;
                    } else {
                        resp = resp + " | " + decimalFormat(getGainAt(sC)) + " @ theta: " + sC.getTheta() + " grados";
                    }
                    break;
                case Global.PLOT3D:
                    resp = resp + " | " + decimalFormat(getMaxGainValue());
                    break loop;
                default:
                    resp = "N/A";
            }
        }
        return resp;
    }

    /**
     * Devuelve información sobre las características del lóbulo lateral en las
     * gráficas de patrón de radiación de tipo azimutal y de elevación
     *
     * @param sideValue Objeto de la clase SphericalCoordenate correspondientes
     * al lóbulo lateral del patrón de radiación
     * @return Información sobre las características del lóbulo lateral en las
     * gráficas de patrón de radiación de tipo azimutal y de elevación
     */
    public String sidelobe2String(SphericalCoordenate sideValue) {
        String resp = "N/A";
        if (sideValue != null) {
            boolean first = true;
            SphericalCoordenate sC = sideValue;
            switch (getCurrentPlotType()) {
                case Global.AZIMUTHPLOT:
                    if (first) {
                        resp = decimalFormat(getGainAt(sC)) + " @ phi: " + sC.getPhi() + " grados";
                        first = false;
                    } else {
                        resp = resp + " | " + decimalFormat(getGainAt(sC)) + " @ phi: " + sC.getPhi() + " grados";
                    }
                    break;
                case Global.ELEVATIONPLOT:
                    if (first) {
                        resp = decimalFormat(getGainAt(sC)) + " @ theta: " + sC.getTheta() + " grados";
                        first = false;
                    } else {
                        resp = resp + " | " + decimalFormat(getGainAt(sC)) + " @ theta: " + sC.getTheta() + " grados";
                    }
                    break;
                case Global.PLOT3D:
                    resp = "N/A";
                    break;
                default:
                    resp = "";
            }
        }
        return resp;
    }

    /**
     * Devuelve información sobre la apertura de haz en las gráficas de patrón
     * de radiación de tipo azimutal y de elevación
     *
     * @param apertures Arreglo de objetos de la clase SphericalCoordenate
     * correspondientes a los extremos de los span alrededor de los valores
     * máximos en los diagramas de radiación de tipo azimutal y de elevación
     * @return Información sobre la apertura de haz en las gráficas de patrón de
     * radiación de tipo azimutal y de elevación
     */
    public String aperture2String(ArrayList<SphericalCoordenate> apertures) {
        switch (getCurrentPlotType()) {
            case Global.AZIMUTHPLOT:
                if (apertures.size() >= 2) {
                    double angle1 = apertures.get(0).getPhi();
                    double angle2 = apertures.get(1).getPhi();
                    if (angle1 > angle2) {
                        double resp = angle1 - angle2;
                        if (resp < 180) {
                            return (angle1 - angle2) + "";
                        } else {
                            return (360 - (angle1 - angle2)) + "";
                        }
                    } else {
                        double resp = angle2 - angle1;
                        if (resp < 180) {
                            return (angle2 - angle1) + "";
                        } else {
                            return (360 - (angle2 - angle1)) + "";
                        }
                    }
                } else {
                    return "N/A";
                }

            case Global.ELEVATIONPLOT:
                if (apertures.size() >= 2) {
                    double angle1 = apertures.get(0).getTheta();
                    double angle2 = apertures.get(1).getTheta();
                    if (angle1 > angle2) {
                        double resp = angle1 - angle2;
                        if (resp < 180) {
                            return (angle1 - angle2) + "";
                        } else {
                            return (360 - (angle1 - angle2)) + "";
                        }
                    } else {
                        double resp = angle2 - angle1;
                        if (resp < 180) {
                            return (angle2 - angle1) + "";
                        } else {
                            return (360 - (angle2 - angle1)) + "";
                        }
                    }
                } else {
                    return "N/A";
                }
            default:
                return "N/A";
        }
    }

    /**
     * Genera la representación geométrica de un círculo de un radio determinado
     * a partir de una cantidad especificada de segmentos
     *
     * @param plane Plano en el cual se generará el círculo
     * @param center Coordenadas del centro del círculo
     * @param segments Número de alambres que constituirán al círculo
     * @param radius Radio del círculo
     * @param drawRad Indica si se han de dibujar también los radios del círculo
     * @return Arreglo de objetos de la clase Line con los alambres que
     * constituye el círculo con las características indicadas
     */
    public static ArrayList<Line> generateCircle(int plane, Point center, int segments, double radius, boolean drawRad) {
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Point> pointsA = new ArrayList<>();
        ArrayList<Point> pointsB = new ArrayList<>();
        double a, b, c;
        double x;
        double b1, b2;
        double alpha = (2 * radius) / segments; //El diámetro del círculo es dividido en segments segmentos
        Point A, B;
        for (int i = 0; i <= segments; i++) {
            switch (plane) {
                case XYPLANE:
                    a = center.getA(); //Coordenada X del centro del círculo
                    b = center.getB(); //Coordenada Y del centro del círculo
                    c = center.getC(); //Coordenada Z del centro del círculo
                    break;
                case XZPLANE:
                    a = center.getA(); //Coordenada X del centro del círculo
                    b = center.getC(); //Coordenada Y del centro del círculo
                    c = center.getB(); //Coordenada Z del centro del círculo
                    break;
                case YZPLANE:
                    a = center.getB(); //Coordenada X del centro del círculo
                    b = center.getC(); //Coordenada Y del centro del círculo
                    c = center.getA(); //Coordenada Z del centro del círculo 
                    break;
                default:
                    throw new AssertionError();
            }

            x = a - radius + i * alpha;  //N-ésima posición horizontal del segmento del círculo
            b1 = Math.sqrt(Math.abs(Math.pow(radius, 2) - (Math.pow((x - a), 2)))) + b;
            b2 = -Math.sqrt(Math.abs(Math.pow(radius, 2) - (Math.pow((x - a), 2)))) + b;

            //x = Double.valueOf(df.format(x).replace(",", "."));
            switch (plane) {
                case XYPLANE:
                    A = new Point(x, b1, c); // N-ésima posición vertical superior del segmento del círculo
                    B = new Point(x, b2, c); // N-ésima posición vertical inferior del segmento del círculo
                    break;
                case XZPLANE:
                    A = new Point(x, c, b1);  // N-ésima posición vertical superior del segmento del círculo
                    B = new Point(x, c, b2); // N-ésima posición vertical inferior del segmento del círculo
                    break;
                case YZPLANE:
                    A = new Point(c, x, b1);  // N-ésima posición vertical superior del segmento del círculo
                    B = new Point(c, x, b2); // N-ésima posición vertical inferior del segmento del círculo
                    break;
                default:
                    throw new AssertionError();
            }
            pointsA.add(A); //Colección de posiciones verticales superiores del círculo
            pointsB.add(B);  //Colección de posiciones verticales inferiores del círculo

        }
        for (int i = 0; i < segments; i++) {
            Line nLine = new Line(pointsA.get(i), pointsA.get(i + 1)); // Se generan líneas que conectan las posiciones verticales superiores entre sí
            lines.add(nLine);
        }
        for (int j = segments; j > 0; j--) {
            Line mLine = new Line(pointsB.get(j), pointsB.get(j - 1));  // Se generan líneas que conectan las posiciones verticales inferiores entre sí
            lines.add(mLine);
        }
        if (drawRad) { //Si se ha indicado dibujar los radios del círculo se generan líneas desde el centro hasta las posiciones verticales superiores e inferiores del círculo
            for (int i = 0; i < segments - 1; i++) {
                Line lLine = new Line(center, pointsA.get(i));
                Line kLine = new Line(center, pointsB.get(i));
                lines.add(lLine);
                lines.add(kLine);
            }
        }
        return lines;
    }

    /**
     * Genera la representación geométrica de un arco de un radio determinado a
     * partir de una cantidad especificada de segmentos
     *
     * @param plane Plano en el cual se generará el arco
     * @param center Coordenadas del centro del arco
     * @param segments Número de alambres que constituirán al arco
     * @param radius Radio del arco
     * @return Arreglo de objetos de la clase Line con los alambres que
     * constituye el arco con las características indicadas
     */
    public static ArrayList<Line> generateArc(int plane, Point center, int segments, double radius) {
        ArrayList<Line> lines = new ArrayList<>();
        ArrayList<Point> pointsA = new ArrayList<>();
        double a, b, c;
        double x;
        double beta, b1;
        double alpha = (2 * radius) / segments; //El diámetro del arco es dividido en segments segmentos
        Point A;
        for (int i = 0; i <= segments; i++) {
            switch (plane) {
                case XYPLANE:
                    a = center.getA(); //Coordenada X del centro del círculo
                    b = center.getB(); //Coordenada Y del centro del círculo
                    c = center.getC(); //Coordenada Z del centro del círculo
                    break;
                case XZPLANE:
                    a = center.getA(); //Coordenada X del centro del círculo
                    b = center.getC(); //Coordenada Y del centro del círculo
                    c = center.getB(); //Coordenada Z del centro del círculo
                    break;
                case YZPLANE:
                    a = center.getB(); //Coordenada X del centro del círculo
                    b = center.getC(); //Coordenada Y del centro del círculo
                    c = center.getA(); //Coordenada Zdel centro del círculo
                    break;
                default:
                    throw new AssertionError();
            }

            x = a - radius + i * alpha; //N-ésima posición horizontal del segmento del arco
            b1 = Math.sqrt(Math.abs(Math.pow(radius, 2) - (Math.pow((x - a), 2)))) + b;
            //beta = Math.pow(b, 2) - Math.pow(radius, 2) + Math.pow(x, 2) - (2 * a * x) + a;  // beta = b^2 - r^2 + x^2 - 2*a*x
            //b1 = (2 * b + Math.sqrt((4 * Math.pow(b, 2)) - (4 * beta))) / 2; //b1 =( 2 * b * sqrt(4 * b^2) - 4 * betha ) / 2
            x = Double.valueOf(df.format(x).replace(",", "."));

            switch (plane) {
                case XYPLANE:
                    A = new Point(x, b1, c);  // N-ésima posición vertical superior del segmento del arco
                    break;
                case XZPLANE:
                    A = new Point(x, c, b1);  // N-ésima posición vertical superior del segmento del arco
                    break;
                case YZPLANE:
                    A = new Point(c, x, b1);  // N-ésima posición vertical superior del segmento del arco
                    break;
                default:
                    throw new AssertionError();
            }
            pointsA.add(A); //Colección de posiciones verticales superiores del arco

        }
        for (int i = 0; i < segments; i++) { // Se generan líneas que conectan las posiciones verticales superiores entre sí
            Line nLine = new Line(pointsA.get(i), pointsA.get(i + 1));
            lines.add(nLine);
        }
        return lines;
    }

    /**
     * Devuelve el valor de la longitud de onda, en metros (WL = c / F (MHz))
     *
     * @return Longitud de Onda, en metros
     */
    public double getWavelength() {
        if (getgFrequency().getFreq() != 0.0) {
            return LIGHTSPEED / (getgFrequency().getFreq() * 1E6);
        } else {
            return generateFrequency();
        }
    }

    /**
     * Genera una ventana en la que se solicita la introducción del valor de la
     * frecuencia de diseño, en MHz. Realizando las respectivas validaciones
     *
     * @return Valor de la frecuencia de diseño, en MHz
     */
    public double generateFrequency() {
        String msg = Global.getMessages().getString("SimulationPanel.geometry.inputFreqMessage");
        String input = inputMessage(msg).replace(",", ".");
        double wavelength = 0;
        if (Global.isNumeric(input)) {
            double angle = Double.valueOf(input);
            if (angle > 0) {
                wavelength = Global.LIGHTSPEED / (angle * 1E6);
                Frequency freq = new Frequency();
                freq.setFreq(angle);
                freq.setFreqIncrement(0);
                freq.setSteppingType(0);
                freq.setSteps(1);
                setgFrequency(freq);
            } else {
                errorValidateInput();
            }
        } else {
            errorValidateInput();
        }
        return wavelength;
    }

    /**
     * Devuelve la cantidad de segmentos correspondientes al alambre introducido
     * como parámetro Wire, según la relación Segmentos = 20 * d / Wl
     *
     * @param line Objeto de la clase Line cuya cantidad de segmentos se desea
     * determinar
     * @return Cantidad de segmentos correspondientes al objeto Line
     */
    public int getSegments(Line line) {
        Double seg = ((20 * line.distance()) / getWavelength());
        if (seg < 1) {
            return 1;
        } else {
            return seg.intValue();
        }
    }

    /**
     * Devuelve la longitud del alambre Wire
     *
     * @param line Alambre cuya longitud se desea obtener
     * @return Longitud del alambre
     */
    public static double wireLength(Wire line) {
        double l = Math.sqrt(Math.pow((line.getX2() - line.getX1()), 2) + Math.pow((line.getY2() - line.getY1()), 2) + Math.pow((line.getZ2() - line.getZ1()), 2));
        return decimalFormat(l);
    }

    /**
     * Aplica un formato a la representación textual de un número de punto
     * flotante, de forma tal que el mismo sea expresado con 4 decimales
     *
     * @param number Representación textual del número al cual se desea aplicar
     * el formato
     * @return Valor numérico de punto flotante con 4 posiciones decimales
     */
    public static double decimalFormat(String number) {
        DecimalFormat formatter = new DecimalFormat("#.####");
        return Double.valueOf(formatter.format(number).replace(",", "."));
    }

    /**
     * Aplica un formato a un número de punto flotante, de forma tal que el
     * mismo sea expresado con 4 decimales
     *
     * @param number Número al cual se desea aplicar el formato
     * @return Valor numérico de punto flotante con 4 posiciones decimales
     */
    public static double decimalFormat(double number) {
        DecimalFormat formatter = new DecimalFormat("#.####");
        return Double.valueOf(formatter.format(number).replace(",", "."));
    }

    /**
     * Aplica un formato a un número de punto flotante, de forma tal que el
     * mismo sea expresado con 2 decimales
     *
     * @param number Número al cual se desea aplicar el formato
     * @return Valor numérico de punto flotante con 2 posiciones decimales
     */
    public static double decimalShortFormat(double number) {
        DecimalFormat formatter = new DecimalFormat("#.##");
        return Double.valueOf(formatter.format(number).replace(",", "."));
    }

    /**
     * Aplica un formato a un número, de forma tal que el mismo sea expresado
     * como un entero, realizando el redondeo correspondiente
     *
     * @param number Número al cual se desea aplicar el formato
     * @return Valor numérico expresado como entero
     */
    public static String integerFormat(double number) {
        DecimalFormat formatter = new DecimalFormat("#0");
        return formatter.format(number);
    }

    /**
     * Genera una ventana con un mensaje de error de texto message y título
     * title
     *
     * @param title Título de la ventana
     * @param message Mensaje de error
     */
    public void errorMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, Global.getMessages().getString(message), Global.getMessages().getString(title), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Genera una ventana con un mensaje de error de texto message y título
     * title, particularizado para la validación de los parámetros de simulación
     *
     * @param title Título de la ventana
     * @param message Mensaje de error
     */
    public void errorSimMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, Global.getMessages().getString(title), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Genera una ventana con un mensaje desde la cual el usuario puede ingresar
     * texto
     *
     * @param message Mensaje de la ventana
     * @return Valor textual introducido por el usuario
     */
    public String inputMessage(String message) {
        return JOptionPane.showInputDialog(message);
    }

    /**
     * Genera una ventana con un mensaje de error de texto message, relacionado
     * a la ejecución de la simulación
     *
     * @param message Mensaje de error
     */
    public void simulationMessages(String message) {
        JOptionPane.showMessageDialog(null, message, Global.getMessages().getString("Messages.simReqTitle"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     ** Genera una ventana con un mensaje indicando que un archivo fue guardado
     * exitosamente
     */
    public void saveFileMessages() {
        JOptionPane.showMessageDialog(null, Global.getMessages().getString("Messages.generateFile"), Global.getMessages().getString("Messages.generateFileTitle"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Genera una ventana con un mensaje de error indicando inconsistencias
     * durante el proceso de validación
     */
    public void errorValidateInput() {
        JOptionPane.showMessageDialog(null, Global.getMessages().getString("Messages.IPT"), Global.getMessages().getString("Messages.IPTTitle"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Genera una ventana con un mensaje de información de propósito general
     *
     * @param title Título de la ventana
     * @param msg Mensaje de la ventana
     */
    public void InfoMessages(String title, String msg) {
        JOptionPane.showMessageDialog(null, Global.getMessages().getString(msg), Global.getMessages().getString(title), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Determina si el texto introducido como parámetro, representa un valor
     * numérico
     *
     * @param str Texto bajo evaluación
     * @return true si el texto introducido representa un valor numérico, de lo
     * contrario, retorna false
     */
    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
