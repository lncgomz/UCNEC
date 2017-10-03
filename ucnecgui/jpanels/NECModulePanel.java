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
import controllers.RadiationPatternPlottingController;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.SystemUtils;
import ucnecgui.Global;
import ucnecgui.jframes.DataFrame;
import ucnecgui.jframes.MultiFrame;
import ucnecgui.jframes.NECFrame;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.Currents;
import ucnecgui.models.Frequency;
import ucnecgui.models.Ground;
import ucnecgui.models.Load;
import ucnecgui.models.Network;
import ucnecgui.models.PowerBudget;
import ucnecgui.models.RPLine;
import ucnecgui.models.RadiationPattern;
import ucnecgui.models.Source;
import ucnecgui.models.Tl;
import ucnecgui.models.Wire;
import ucnecgui.models.loadNEC;

/**
 *
 * @author Leoncio Gómez
 */
public class NECModulePanel extends javax.swing.JPanel {

    private Global global;

    /**
     * Constructor de la clase NECModulePanel
     *
     * @param global Objeto de la clase Global
     */
    public NECModulePanel(Global global) {
        initComponents();
        initializeInfo(global);
        this.global = global;
    }

    /**
     * Inicializar componentes del panel
     *
     * @param global Objeto de la clase Global
     */
    public void initializeInfo(Global global) {
        String p = global.getgDirectory();

        directoryInfo.setText(global.getgDirectory());
        if (global.getgFrequency().getFreq() != 0.0) {
            frequencyInfo.setText(global.getgFrequency().getFreq() + "");
            wavelengthInfo.setText(global.getWavelength() + "");
        }
        if (global.getgWires().size() > 0) {
            wireInfo.setText(global.wireInformation());
        } else {
            wireInfo.setText("");
        }
        if (global.getgSource().size() > 0) {
            sourceInfo.setText(global.getgSource().size() + " fuente(s)");
        } else {
            sourceInfo.setText("");
        }
        if (global.getgLoad().size() > 0) {
            loadInfo.setText(global.getgLoad().size() + " carga(s)");
        } else {
            loadInfo.setText("");
        }
        if (global.getgTl().size() > 0) {
            tlInfo.setText(global.getgTl().size() + " línea(s) de transmisición");
        } else {
            tlInfo.setText("");
        }
        if (global.getgGround().size() > 0) {
            groundTypeInfo.setText(global.groundTypeInformation());
        } else {
            groundTypeInfo.setText("");
        }
        if (global.getAlterZ0() > 0) {
            alterz0Info.setText(global.getAlterZ0() + "");
        } else {
            alterz0Info.setText("");
        }
        if (global.getgRadiationPattern().isSetted()) {
            rpInfo.setText(global.plotType2String());
        } else {
            rpInfo.setText("");
        }

        wirelossInfo.setText(global.wirelossInformation());
        unitsInfo.setText(global.unit2String());

    }

    /**
     * Genera archivo de entrada interpretable por NEC2 a partir de los
     * parámetros geométricos y electromagnéticos establecidos a través del
     * programa
     *
     * @param is3DPlot Indica si el archivo se generará para una simulación de
     * patrón de radiación tridimensional
     * @param global Objeto de la clase Global
     */
    public static void generateInputFile(boolean is3DPlot, Global global) {
        ArrayList<String> necCard = new ArrayList<String>();

        if (global.getgWires().size() > 0) {

            necCard.add("CM UNIVERSIDAD DE CARABOBO");
            necCard.add("CM ESCUELA DE INGENIERÍA DE TELECOMUNICACIONES");
            necCard.add("CM SIMULACION UCNEC"); 
            necCard.add("CM -------------------------------------------");
            necCard.add("CE");

            ArrayList<Wire> processedWires = global.changeUnit(Global.METER, global.getCurrentUnit());

            ArrayList<String> geometry = Wire.toString(processedWires);
            for (String wire : geometry) {
                necCard.add(wire);
            }
            necCard.add("GE 0");

            if (global.getgLoad().size() > 0) {
                ArrayList<String> load = Load.toString(global.getgLoad());
                for (String ld : load) {
                    necCard.add(ld);
                }
            }

            if (global.getgTl().size() > 0) {
                ArrayList<String> tl = Tl.toString(global.getgTl());
                for (String tls : tl) {
                    necCard.add(tls);
                }
            }

            if (global.getgWl().size() > 0) {
                ArrayList<String> wl = new ArrayList<String>();
                for (String wireLoss : wl) {
                    necCard.add(wireLoss);
                }
            }

            if (global.getgFrequency().getFreq() != 0.0) {
                necCard.add(Frequency.toString(global.getgFrequency()));
            }

            if (global.getgGround().size() > 0) {
                ArrayList<String> ground = Ground.toString(global.getgGround());
                for (String gnd : ground) {
                    necCard.add(gnd);
                }
            }

            if (global.getgSource().size() > 0) {
                ArrayList<String> source = Source.toString(global.getgSource());
                for (String src : source) {
                    necCard.add(src);
                }
            }

            if (global.getgNetwork().size() > 0) {
                ArrayList<String> network = Network.toString(global.getgNetwork());
                for (String net : network) {
                    necCard.add(net);
                }
            }

            if (global.getgRadiationPattern().isSetted()) {
                necCard.add(RadiationPattern.toString(global.getgRadiationPattern(), is3DPlot));
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
                } catch (Exception ex) {/*ignore*/
                    ex.printStackTrace();
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, Global.getMessages().getString("Global.noEnoughInfo"), Global.getMessages().getString("Global.noEnoughInfoTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Verifica si se han establecido suficientes parámetros geométricos y
     * electromagnéticos para llevar a cabo una simulación
     *
     * @param global Objeto de la clase Global
     * @return true si se han establecido suficientes parámetros para ejecutar
     * la simulación, de lo contrario, devuelve false
     */
    public static boolean checkSimRequirements(Global global) {
        boolean condition1 = global.getgWires().size() > 0;
        boolean condition2 = global.getgSource().size() > 0;
        boolean condition3 = global.getgFrequency().getFreq() != 0.0;
        boolean condition4 = global.getgRadiationPattern().isSetted();
        String msg = "";
        if (!condition1) {
            msg = msg + Global.getMessages().getString("Messages.simReqWires") + System.lineSeparator();
        }
        if (!condition2) {
            msg = msg + Global.getMessages().getString("Messages.simReqSources") + System.lineSeparator();
        }
        if (!condition3) {
            msg = msg + Global.getMessages().getString("Messages.simReqFreq") + System.lineSeparator();
        }
        if (!condition4) {
            msg = msg + Global.getMessages().getString("Messages.simReqRP") + System.lineSeparator();
        }
        if (condition1 && condition2 && condition3 && condition4) {
            return true;
        } else {
            global.simulationMessages(msg);
            return false;
        }
    }

    /**
     * Verifica si se han establecido suficientes parámetros geométricos y
     * electromagnéticos para llevar a cabo el cálculo del SWR
     *   
     * @return true si se han establecido suficientes parámetros para ejecutar
     * el cálculo del SWR, de lo contrario, devuelve false
     */
    public boolean checkSWRRequirements() {
        boolean condition1 = global.getgWires().size() > 0;
        boolean condition2 = global.getgSource().size() > 0;
        String msg = "";
        if (!condition1) {
            msg = msg + Global.getMessages().getString("Messages.simReqWires") + System.lineSeparator();
        }
        if (!condition2) {
            msg = msg + Global.getMessages().getString("Messages.simReqSources") + System.lineSeparator();
        }

        if (condition1 && condition2) {
            return true;
        } else {
            global.simulationMessages(msg);
            return false;
        }
    }

    /**
     *     * Genera archivo de salida interpretable por NEC2 a partir de la
     * ejecución del script nec2++ cuya entrada en un archivo de entrada
     * interpretable por el script y generado con anterioridad
     *
     * @return true si la generación del archivo fue exitosa, de lo contrario,
     * devuelve false
     */
    public static boolean generateOutputData() {
        ArrayList<String> necOutput = new ArrayList<String>();
        ArrayList<String> necError = new ArrayList<String>();
        boolean resp = false;

        String path = NECFrame.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String dir = new File(path).getParentFile().getPath() + File.separator + "kernel" + File.separator;

        if (SystemUtils.IS_OS_WINDOWS) { //Entorno WINDOWS
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
        } else if (SystemUtils.IS_OS_LINUX) {  //Entorno LINUX

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
     * Genera una gráfica correspondiente al patrón de radiación del sistema
     * establecido a través del programa
     *
     * @param outputData Colección de objetos cuya clave es un ángulo de
     * iteración y su valor es un arreglo de objetos de la clase RPLine con los
     * valores de Etotal para un ángulo de iteración constante
     * @param global Objeto de la clase Global
     */
    public static void generateRPPlot(HashMap<String, ArrayList<RPLine>> outputData, Global global) {
        RadiationPatternPlottingController.RPPlot(outputData, global);
    }

    /**
     * Captura los valores de corrientes presentes en el archivo salida.nec
     * generado por el script nec2++ a partir del archivo entrada.nec. Estos son
     * convertidos a una lista de objetos de tipo String
     *
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores de corrientes de la
     * simulación
     *
     */
    public static ArrayList<String> generateCurrents(Global global) {
        ArrayList<String> resp = new ArrayList<String>();
        generateInputFile((global.getCurrentPlotType() == Global.PLOT3D), global);
        boolean isOutputGenerated = generateOutputData();
        resp = NECParser.getCurrents(global.readOutputData(isOutputGenerated), global);
        return resp;
    }

    /**
     * Captura la descripción de las fuentes presentes en el archivo salida.nec
     * generado por el script nec2++ a partir del archivo entrada.nec. Estos son
     * convertidos a una lista de objetos de tipo String
     *
     * @return Lista de Objetos String con los valores de fuentes de la
     * simulación
     */
    public ArrayList<String> generateSources() {
        ArrayList<String> resp = new ArrayList<String>();
        generateInputFile((global.getCurrentPlotType() == Global.PLOT3D), global);
        boolean isOutputGenerated = generateOutputData();
        resp = NECParser.getSources(global.readOutputData(isOutputGenerated), global);
        return resp;
    }

    /**
     * Captura la descripción de las cargas presentes en el archivo salida.nec
     * generado por el script nec2++ a partir del archivo entrada.nec. Estos son
     * convertidos a una lista de objetos de tipo String
     *
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores de cargas de la
     * simulación
     */
    public static ArrayList<String> generateLoads(Global global) {
        ArrayList<String> resp = new ArrayList<String>();
        generateInputFile((global.getCurrentPlotType() == Global.PLOT3D), global);
        boolean isOutputGenerated = generateOutputData();
        resp = NECParser.getLoads(global.readOutputData(isOutputGenerated), global);
        return resp;
    }

    /**
     * Captura la descripción del presupuesto de potencia en el archivo
     * salida.nec generado por el script nec2++ a partir del archivo
     * entrada.nec. Estos son convertidos a una lista de objetos de tipo String
     *
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores del presupuesto de
     * potencia de la simulación
     */
    public static ArrayList<String> generatePowers(Global global) {
        ArrayList<String> resp = new ArrayList<String>();
        generateInputFile((global.getCurrentPlotType() == Global.PLOT3D), global);
        boolean isOutputGenerated = generateOutputData();
        resp = NECParser.getPowers(global.readOutputData(isOutputGenerated), global);
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

        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        openbtn = new javax.swing.JButton();
        savebtn = new javax.swing.JButton();
        currentbtn = new javax.swing.JButton();
        sourcedatabtn = new javax.swing.JButton();
        loadsdatabtn = new javax.swing.JButton();
        powerdatabtn = new javax.swing.JButton();
        swrbtn = new javax.swing.JButton();
        viewantbtn = new javax.swing.JButton();
        plotbtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        directorybtn = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        Frequencybtn = new javax.swing.JButton();
        directoryInfo2 = new javax.swing.JLabel();
        wavelengthbtn = new javax.swing.JButton();
        directoryInfo3 = new javax.swing.JLabel();
        wirebtn = new javax.swing.JButton();
        directoryInfo4 = new javax.swing.JLabel();
        sourcebtn = new javax.swing.JButton();
        directoryInfo5 = new javax.swing.JLabel();
        loadbtn = new javax.swing.JButton();
        directoryInfo6 = new javax.swing.JLabel();
        tlbtn = new javax.swing.JButton();
        directoryInfo7 = new javax.swing.JLabel();
        groundTypebtn = new javax.swing.JButton();
        directoryInfo8 = new javax.swing.JLabel();
        wirelossbtn = new javax.swing.JButton();
        directoryInfo9 = new javax.swing.JLabel();
        unitsbtn = new javax.swing.JButton();
        directoryInfo10 = new javax.swing.JLabel();
        rpbtn = new javax.swing.JButton();
        directoryInfo12 = new javax.swing.JLabel();
        alterz0btn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        directoryInfo0 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        directoryInfo = new javax.swing.JLabel();
        frequencyInfo = new javax.swing.JLabel();
        wavelengthInfo = new javax.swing.JLabel();
        wireInfo = new javax.swing.JLabel();
        sourceInfo = new javax.swing.JLabel();
        loadInfo = new javax.swing.JLabel();
        tlInfo = new javax.swing.JLabel();
        groundTypeInfo = new javax.swing.JLabel();
        wirelossInfo = new javax.swing.JLabel();
        unitsInfo = new javax.swing.JLabel();
        rpInfo = new javax.swing.JLabel();
        alterz0Info = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(36, 113, 163));
        jPanel3.setPreferredSize(new java.awt.Dimension(500, 50));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        add(jPanel3, java.awt.BorderLayout.NORTH);

        jPanel1.setBackground(new java.awt.Color(36, 113, 163));
        jPanel1.setPreferredSize(new java.awt.Dimension(150, 500));
        jPanel1.setLayout(new java.awt.GridLayout(9, 1));

        openbtn.setBackground(new java.awt.Color(36, 113, 163));
        openbtn.setForeground(new java.awt.Color(255, 255, 255));
        openbtn.setText("ABRIR");
        openbtn.setBorder(null);
        openbtn.setBorderPainted(false);
        openbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        openbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openbtnActionPerformed(evt);
            }
        });
        jPanel1.add(openbtn);

        savebtn.setBackground(new java.awt.Color(36, 113, 163));
        savebtn.setForeground(new java.awt.Color(255, 255, 255));
        savebtn.setText("GUADAR COMO");
        savebtn.setBorder(null);
        savebtn.setBorderPainted(false);
        savebtn.setPreferredSize(new java.awt.Dimension(15, 32));
        savebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebtnActionPerformed(evt);
            }
        });
        jPanel1.add(savebtn);

        currentbtn.setBackground(new java.awt.Color(36, 113, 163));
        currentbtn.setForeground(new java.awt.Color(255, 255, 255));
        currentbtn.setText("CORRIENTES");
        currentbtn.setBorder(null);
        currentbtn.setBorderPainted(false);
        currentbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        currentbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentbtnActionPerformed(evt);
            }
        });
        jPanel1.add(currentbtn);

        sourcedatabtn.setBackground(new java.awt.Color(36, 113, 163));
        sourcedatabtn.setForeground(new java.awt.Color(255, 255, 255));
        sourcedatabtn.setText("DATOS DE FUENTES");
        sourcedatabtn.setBorder(null);
        sourcedatabtn.setBorderPainted(false);
        sourcedatabtn.setPreferredSize(new java.awt.Dimension(15, 32));
        sourcedatabtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourcedatabtnActionPerformed(evt);
            }
        });
        jPanel1.add(sourcedatabtn);

        loadsdatabtn.setBackground(new java.awt.Color(36, 113, 163));
        loadsdatabtn.setForeground(new java.awt.Color(255, 255, 255));
        loadsdatabtn.setText("DATOS DE CARGAS");
        loadsdatabtn.setBorder(null);
        loadsdatabtn.setBorderPainted(false);
        loadsdatabtn.setPreferredSize(new java.awt.Dimension(15, 32));
        loadsdatabtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadsdatabtnActionPerformed(evt);
            }
        });
        jPanel1.add(loadsdatabtn);

        powerdatabtn.setBackground(new java.awt.Color(36, 113, 163));
        powerdatabtn.setForeground(new java.awt.Color(255, 255, 255));
        powerdatabtn.setText("DATOS DE POTENCIA");
        powerdatabtn.setBorder(null);
        powerdatabtn.setBorderPainted(false);
        powerdatabtn.setPreferredSize(new java.awt.Dimension(15, 32));
        powerdatabtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                powerdatabtnActionPerformed(evt);
            }
        });
        jPanel1.add(powerdatabtn);

        swrbtn.setBackground(new java.awt.Color(36, 113, 163));
        swrbtn.setForeground(new java.awt.Color(255, 255, 255));
        swrbtn.setText("ROE");
        swrbtn.setBorder(null);
        swrbtn.setBorderPainted(false);
        swrbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        swrbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                swrbtnActionPerformed(evt);
            }
        });
        jPanel1.add(swrbtn);

        viewantbtn.setBackground(new java.awt.Color(36, 113, 163));
        viewantbtn.setForeground(new java.awt.Color(255, 255, 255));
        viewantbtn.setText("VER ANTENA");
        viewantbtn.setBorder(null);
        viewantbtn.setBorderPainted(false);
        viewantbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        viewantbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewantbtnActionPerformed(evt);
            }
        });
        jPanel1.add(viewantbtn);

        plotbtn.setBackground(new java.awt.Color(36, 113, 163));
        plotbtn.setForeground(new java.awt.Color(255, 255, 255));
        plotbtn.setText("PATRÓN DE RADIACIÓN");
        plotbtn.setBorder(null);
        plotbtn.setBorderPainted(false);
        plotbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        plotbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plotbtnActionPerformed(evt);
            }
        });
        jPanel1.add(plotbtn);

        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(150, 450));
        jPanel4.setLayout(new java.awt.GridLayout(12, 3));

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("1");
        jPanel4.add(jLabel3);

        directorybtn.setText(">");
        directorybtn.setPreferredSize(new java.awt.Dimension(15, 32));
        directorybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                directorybtnActionPerformed(evt);
            }
        });
        jPanel4.add(directorybtn);

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("2");
        jPanel4.add(jLabel9);

        Frequencybtn.setText(">");
        Frequencybtn.setPreferredSize(new java.awt.Dimension(15, 32));
        Frequencybtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FrequencybtnActionPerformed(evt);
            }
        });
        jPanel4.add(Frequencybtn);

        directoryInfo2.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo2.setText("3");
        jPanel4.add(directoryInfo2);

        wavelengthbtn.setText(">");
        wavelengthbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        wavelengthbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wavelengthbtnActionPerformed(evt);
            }
        });
        jPanel4.add(wavelengthbtn);

        directoryInfo3.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo3.setText("4");
        jPanel4.add(directoryInfo3);

        wirebtn.setText(">");
        wirebtn.setPreferredSize(new java.awt.Dimension(15, 32));
        wirebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wirebtnActionPerformed(evt);
            }
        });
        jPanel4.add(wirebtn);

        directoryInfo4.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo4.setText("5");
        jPanel4.add(directoryInfo4);

        sourcebtn.setText(">");
        sourcebtn.setPreferredSize(new java.awt.Dimension(15, 32));
        sourcebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourcebtnActionPerformed(evt);
            }
        });
        jPanel4.add(sourcebtn);

        directoryInfo5.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo5.setText("6");
        jPanel4.add(directoryInfo5);

        loadbtn.setText(">");
        loadbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        loadbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadbtnActionPerformed(evt);
            }
        });
        jPanel4.add(loadbtn);

        directoryInfo6.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo6.setText("7");
        jPanel4.add(directoryInfo6);

        tlbtn.setText(">");
        tlbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        tlbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tlbtnActionPerformed(evt);
            }
        });
        jPanel4.add(tlbtn);

        directoryInfo7.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo7.setText("8");
        jPanel4.add(directoryInfo7);

        groundTypebtn.setText(">");
        groundTypebtn.setPreferredSize(new java.awt.Dimension(15, 32));
        groundTypebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groundTypebtnActionPerformed(evt);
            }
        });
        jPanel4.add(groundTypebtn);

        directoryInfo8.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo8.setText("9");
        jPanel4.add(directoryInfo8);

        wirelossbtn.setText(">");
        wirelossbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        wirelossbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wirelossbtnActionPerformed(evt);
            }
        });
        jPanel4.add(wirelossbtn);

        directoryInfo9.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo9.setText("10");
        jPanel4.add(directoryInfo9);

        unitsbtn.setText(">");
        unitsbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        unitsbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitsbtnActionPerformed(evt);
            }
        });
        jPanel4.add(unitsbtn);

        directoryInfo10.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo10.setText("11");
        jPanel4.add(directoryInfo10);

        rpbtn.setText(">");
        rpbtn.setPreferredSize(new java.awt.Dimension(15, 32));
        rpbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rpbtnActionPerformed(evt);
            }
        });
        jPanel4.add(rpbtn);

        directoryInfo12.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo12.setText("12");
        jPanel4.add(directoryInfo12);

        alterz0btn.setText(">");
        alterz0btn.setPreferredSize(new java.awt.Dimension(15, 32));
        alterz0btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alterz0btnActionPerformed(evt);
            }
        });
        jPanel4.add(alterz0btn);

        jPanel2.add(jPanel4, java.awt.BorderLayout.WEST);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.GridLayout(12, 1));

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Directorio");
        jPanel7.add(jLabel6);

        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Frecuencia (MHz)");
        jPanel7.add(jLabel12);

        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Longitud de Onda (m)");
        jPanel7.add(jLabel18);

        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Alambres");
        jPanel7.add(jLabel24);

        jLabel30.setForeground(new java.awt.Color(0, 0, 0));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Fuentes");
        jPanel7.add(jLabel30);

        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("Cargas");
        jPanel7.add(jLabel36);

        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("Líneas de Transmisión");
        jPanel7.add(jLabel42);

        directoryInfo0.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo0.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        directoryInfo0.setText("Tipo de Tierra");
        jPanel7.add(directoryInfo0);

        jLabel46.setForeground(new java.awt.Color(0, 0, 0));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Pérdidas en el Alambre");
        jPanel7.add(jLabel46);

        jLabel48.setForeground(new java.awt.Color(0, 0, 0));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("Unidades");
        jPanel7.add(jLabel48);

        jLabel50.setForeground(new java.awt.Color(0, 0, 0));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("Gráfica");
        jPanel7.add(jLabel50);

        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("Z0 Alternativo para SWR (Ohm)");
        jPanel7.add(jLabel56);

        jPanel5.add(jPanel7, java.awt.BorderLayout.WEST);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setPreferredSize(new java.awt.Dimension(200, 600));
        jPanel8.setLayout(new java.awt.GridLayout(12, 1));

        directoryInfo.setBackground(new java.awt.Color(250, 250, 250));
        directoryInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        directoryInfo.setForeground(new java.awt.Color(0, 0, 0));
        directoryInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        directoryInfo.setAutoscrolls(true);
        directoryInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        directoryInfo.setOpaque(true);
        jPanel8.add(directoryInfo);

        frequencyInfo.setBackground(new java.awt.Color(250, 250, 250));
        frequencyInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        frequencyInfo.setForeground(new java.awt.Color(0, 0, 0));
        frequencyInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        frequencyInfo.setAutoscrolls(true);
        frequencyInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        frequencyInfo.setOpaque(true);
        jPanel8.add(frequencyInfo);

        wavelengthInfo.setBackground(new java.awt.Color(250, 250, 250));
        wavelengthInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        wavelengthInfo.setForeground(new java.awt.Color(0, 0, 0));
        wavelengthInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        wavelengthInfo.setAutoscrolls(true);
        wavelengthInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        wavelengthInfo.setOpaque(true);
        jPanel8.add(wavelengthInfo);

        wireInfo.setBackground(new java.awt.Color(250, 250, 250));
        wireInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        wireInfo.setForeground(new java.awt.Color(0, 0, 0));
        wireInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        wireInfo.setAutoscrolls(true);
        wireInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        wireInfo.setOpaque(true);
        jPanel8.add(wireInfo);

        sourceInfo.setBackground(new java.awt.Color(250, 250, 250));
        sourceInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        sourceInfo.setForeground(new java.awt.Color(0, 0, 0));
        sourceInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sourceInfo.setAutoscrolls(true);
        sourceInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        sourceInfo.setOpaque(true);
        jPanel8.add(sourceInfo);

        loadInfo.setBackground(new java.awt.Color(250, 250, 250));
        loadInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        loadInfo.setForeground(new java.awt.Color(0, 0, 0));
        loadInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        loadInfo.setAutoscrolls(true);
        loadInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        loadInfo.setOpaque(true);
        jPanel8.add(loadInfo);

        tlInfo.setBackground(new java.awt.Color(250, 250, 250));
        tlInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tlInfo.setForeground(new java.awt.Color(0, 0, 0));
        tlInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        tlInfo.setAutoscrolls(true);
        tlInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        tlInfo.setOpaque(true);
        jPanel8.add(tlInfo);

        groundTypeInfo.setBackground(new java.awt.Color(250, 250, 250));
        groundTypeInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        groundTypeInfo.setForeground(new java.awt.Color(0, 0, 0));
        groundTypeInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        groundTypeInfo.setAutoscrolls(true);
        groundTypeInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        groundTypeInfo.setOpaque(true);
        jPanel8.add(groundTypeInfo);

        wirelossInfo.setBackground(new java.awt.Color(250, 250, 250));
        wirelossInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        wirelossInfo.setForeground(new java.awt.Color(0, 0, 0));
        wirelossInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        wirelossInfo.setAutoscrolls(true);
        wirelossInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        wirelossInfo.setOpaque(true);
        jPanel8.add(wirelossInfo);

        unitsInfo.setBackground(new java.awt.Color(250, 250, 250));
        unitsInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        unitsInfo.setForeground(new java.awt.Color(0, 0, 0));
        unitsInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        unitsInfo.setAutoscrolls(true);
        unitsInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        unitsInfo.setOpaque(true);
        jPanel8.add(unitsInfo);

        rpInfo.setBackground(new java.awt.Color(250, 250, 250));
        rpInfo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        rpInfo.setForeground(new java.awt.Color(0, 0, 0));
        rpInfo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        rpInfo.setAutoscrolls(true);
        rpInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        rpInfo.setOpaque(true);
        jPanel8.add(rpInfo);

        alterz0Info.setBackground(new java.awt.Color(250, 250, 250));
        alterz0Info.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        alterz0Info.setForeground(new java.awt.Color(0, 0, 0));
        alterz0Info.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        alterz0Info.setAutoscrolls(true);
        alterz0Info.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        alterz0Info.setOpaque(true);
        jPanel8.add(alterz0Info);

        jPanel5.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void directorybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_directorybtnActionPerformed
        directoryInfo.setText(global.getDirectory());
    }//GEN-LAST:event_directorybtnActionPerformed
//Comportamiento del botón Longitud de Onda
    private void wavelengthbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wavelengthbtnActionPerformed
        String msg = Global.getMessages().getString("SimulationPanel.geometry.inputWlMessage");

        try {
            String input = global.inputMessage(msg).replace(",", ".");
            if (global.isNumeric(input)) {
                double wavelength = Double.valueOf(input);
                double angle = (Global.LIGHTSPEED / wavelength) / 1E6;
                frequencyInfo.setText(angle + "");
                wavelengthInfo.setText(wavelength + "");
                Frequency freq = new Frequency();
                freq.setFreq(angle);
                freq.setFreqIncrement(0);
                freq.setSteppingType(0);
                freq.setSteps(1);
                global.setgFrequency(freq);
            } else {
                global.errorValidateInput();
            }
        } catch (NullPointerException e) {
        }


    }//GEN-LAST:event_wavelengthbtnActionPerformed
//Comportamiento del botón Fuentes
    private void sourcebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourcebtnActionPerformed
        if (global.getgWires().size() > 0) {
            MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("SimulationPanel.source.jlabel2.label"), global, this);
            frame.add(new SourcePanel(global, frame));
            frame.pack();
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, Global.getMessages().getString("Messages.nowires"), Global.getMessages().getString("Messages.nowires_title"), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_sourcebtnActionPerformed
//Comportamiento del botón Tipo de Tierra
    private void groundTypebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groundTypebtnActionPerformed
        MultiFrame frame = new MultiFrame(400, 190, Global.getMessages().getString("GT.label"), global, this);
        frame.add(new GroundTypePanel(global, frame));
        frame.pack();
        frame.setVisible(true);
    }//GEN-LAST:event_groundTypebtnActionPerformed
//Comportamiento del botón Pérdidas en el Alambre
    private void wirelossbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wirelossbtnActionPerformed
        MultiFrame frame = new MultiFrame(300, 300, Global.getMessages().getString("GT.label"), global, this);
        frame.add(new WireLossPanel(global, frame));
        frame.pack();
        frame.setVisible(true);
    }//GEN-LAST:event_wirelossbtnActionPerformed
//Comportamiento del botón Gráfica
    private void rpbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rpbtnActionPerformed
        MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("SimulationPanel.jTabbedPanel.label6"), global, this);
        frame.add(new RadiationPatternPanel(global, frame));
        frame.pack();
        frame.setVisible(true);
    }//GEN-LAST:event_rpbtnActionPerformed
//Comportamiento del botón Z0 Alternativo para SWR (Ohm)
    private void alterz0btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alterz0btnActionPerformed
        String msg = Global.getMessages().getString("Messages.alterZ0");
        String input = global.inputMessage(msg).replace(",", ".");
        double alterZ0 = 0;
        if (Global.isNumeric(input)) {
            alterZ0 = Double.valueOf(input);
            if (alterZ0 > 0) {
                global.setAlterZ0(alterZ0);
                alterz0Info.setText(alterZ0 + "");
            } else {
                global.errorValidateInput();
            }
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_alterz0btnActionPerformed
//Comportamiento del botón Frecuencia
    private void FrequencybtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FrequencybtnActionPerformed
        String msg = Global.getMessages().getString("SimulationPanel.geometry.inputFreqMessage");
        try {
            String input = global.inputMessage(msg).replace(",", ".");
            if (Global.isNumeric(input)) {
                double angle = Double.valueOf(input);
                if (angle > 0) {
                    double wavelength = Global.LIGHTSPEED / (angle * 1E6);
                    frequencyInfo.setText(angle + "");
                    wavelengthInfo.setText(wavelength + "");
                    Frequency freq = new Frequency();
                    freq.setFreq(angle);
                    freq.setFreqIncrement(0);
                    freq.setSteppingType(0);
                    freq.setSteps(1);
                    global.setgFrequency(freq);
                } else {
                    global.errorValidateInput();
                }
            } else {
                global.errorValidateInput();
            }
        } catch (NullPointerException e) {

        }

    }//GEN-LAST:event_FrequencybtnActionPerformed

    /**
     * Genera el presupuesto de potencia inherente a la simulación
     *
     * @param powerData Arreglo de String con los datos capturados del archivo
     * de salida NEC2 referentes al presupuesto de potencia
     * @param global Objeto de la clase Global
     * @return Objeto PowerBudget
     */
    public static PowerBudget calculatePowerBudget(ArrayList<String> powerData, Global global) {
        ArrayList<String> line = powerData;
        String[] aux0 = new String[2];
        String[] aux1 = new String[2];
        ArrayList<Double> param = new ArrayList<Double>();

        for (int i = 1; i < powerData.size(); i++) {
            aux0 = line.get(i).split("=");
            aux1 = (aux0[1].trim()).split("\\s+");
            param.add(Double.valueOf(aux1[0]));
        }
        PowerBudget pB = new PowerBudget(param);
        global.setgPower(pB);
        return pB;
    }

    /**
     * Genera el listado de corrientes inherente a la simulación
     *
     * @param currentsData Arreglo de String con los datos capturados del
     * archivo de salida NEC2 referentes a las corrientes de la simulación
     * @param global Objeto de la clase Global
     * @return Arreglo de objetos Currents
     */
    public static ArrayList<Currents> calculateCurrents(ArrayList<String> currentsData, Global global) {
        ArrayList<Currents> iList = new ArrayList<Currents>();
        for (String line : currentsData) {
            String[] lin = line.split(",");
            if (lin.length == 10) {
                int seg = Integer.valueOf(lin[0].trim());
                int tag = Integer.valueOf(lin[1].trim());
                double iRe = Double.valueOf(lin[6].trim());
                double iIm = Double.valueOf(lin[7].trim());
                Complex i = new Complex(iRe, iIm);
                Currents c = new Currents();
                c.setSeg(seg);
                c.setTag(tag);
                c.setI(i);
                iList.add(c);
            } else {
                continue;
            }
        }
        return iList;
    }

    /**
     * Genera el listado de cargas inherente a la simulación
     *
     * @param loadsData Arreglo de String con los datos capturados del archivo
     * de salida NEC2 referentes a las corrientes de la simulación
     * @param global Objeto de la clase Global
     * @return Arreglo de objetos loadNEC
     */
    public static ArrayList<loadNEC> calculateLoads(ArrayList<String> loadsData, Global global) {
        ArrayList<loadNEC> loadList = new ArrayList<loadNEC>();
        for (String line : loadsData) {
            String[] lin = (line.trim()).split("\\s+");
            if (lin.length == 6) {
                loadNEC ldN = new loadNEC();
                double zRe = Double.valueOf(lin[3]);
                Complex z = new Complex(zRe, 0);
                int seg = Integer.valueOf(lin[1].trim());
                int tag = Integer.valueOf(lin[0].trim());
                ldN.setZ(z);
                ldN.setTag(tag);
                ldN.setSeg(seg);
                loadList.add(ldN);
            } else if (lin.length == 7) {
                 loadNEC ldN = new loadNEC();
                double zRe = Double.valueOf(lin[3]);
                double zIm = Double.valueOf(lin[4]);
                Complex z = new Complex(zRe, zIm);
                int seg = Integer.valueOf(lin[1].trim());
                int tag = Integer.valueOf(lin[0].trim());
                ldN.setZ(z);
                ldN.setTag(tag);
                ldN.setSeg(seg);
                loadList.add(ldN);
            }else{
                continue;
            }
        }
        return loadList;
    }

    /**
     * Combina la información de Cargas, Corrientes y Presupuesto de Potencias
     * propias de la simulación
     *
     * @param loads Arreglo de objetos de la clase loadNEC (Cargas)
     * @param currents Arreglo de objetos de la clase Currents (Corrientes)
     * @param pB Objeto PowerBudget (Presupuesto de Potencia)
     */
    public static void mergeLoadsInfo(ArrayList<loadNEC> loads, ArrayList<Currents> currents, PowerBudget pB) {
        double pw = pB.getStructureLoss();
        double modI = 0;
        double modV = 0;
        double phase = 0;
        for (loadNEC nload : loads) {
            Currents c = searchCurrentBySeg(currents, nload.getSeg());
            modI = Math.sqrt(pw / nload.getZ().abs());
            modV = modI * nload.getZ().abs();
            phase = c.getI().phase();
            Complex I = new Complex(modI, phase, false);
            Complex V = new Complex(modV, phase, false);
            nload.setV(V);
            nload.setI(I);
            nload.setPower(pw);
        }
    }

    /**
     * Busca el valor de la corriente en un alambre determinado por el índice
     * seg
     *
     * @param currents Arreglo de objetos de la clase Currents
     * @param seg Índice del alambre donde se desea conocer su corriente
     * @return Objeto de la clase Currents
     */
    public static Currents searchCurrentBySeg(ArrayList<Currents> currents, int seg) {
        Currents resp = null;
        for (Currents ncurrent : currents) {
            if (ncurrent.getSeg() == seg) {
                resp = ncurrent;
            }
        }
        return resp;
    }
//Generación de la ventana GeometryPanel
    private void wirebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wirebtnActionPerformed
        if (global.getgFrequency().getFreq() != 0.0) {
            MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("SimulationPanel.jTabbedPanel.label1"), global, this);
            frame.add(new GeometryPanel(global));
            frame.pack();
            frame.setVisible(true);
        } else {
            global.errorMessage("Messages.nofreqTitle", "Messages.nofreq");
        }
    }//GEN-LAST:event_wirebtnActionPerformed
//Generación de la ventana LoadPanel
    private void loadbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadbtnActionPerformed
        if (global.getgWires().size() > 0) {
            MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("SimulationPanel.load.jlabel2.label2"), global, this);
            frame.add(new LoadPanel(global, frame));
            frame.pack();
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, Global.getMessages().getString("Messages.nowires"), Global.getMessages().getString("Messages.nowires_title"), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_loadbtnActionPerformed
//Generación de la ventana TlPanel
    private void tlbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tlbtnActionPerformed
        if (global.getgWires().size() > 0) {
            MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("SimulationPanel.tl.jlabel2.label"), global, this);
            frame.add(new TlPanel(global));
            frame.pack();
            frame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, Global.getMessages().getString("Messages.nowires"), Global.getMessages().getString("Messages.nowires_title"), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tlbtnActionPerformed
//Generación de la ventana UnitsPanel
    private void unitsbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitsbtnActionPerformed
        MultiFrame frame = new MultiFrame(300, 500, Global.getMessages().getString("SimulationPanel.geometry.units.label"), global, this);
        frame.add(new UnitsPanel(global, frame));
        frame.pack();
        frame.setVisible(true);
    }//GEN-LAST:event_unitsbtnActionPerformed
//Comportamiento del botón Guardar Como
    private void savebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebtnActionPerformed
        global.saveFile();
    }//GEN-LAST:event_savebtnActionPerformed
//Generación de la ventana DataFrame para la visualización de corrientes
    private void sourcedatabtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourcedatabtnActionPerformed
        if (global.getgWires().size() > 0 && global.getgSource().size() > 0) {
            DataFrame dataFrame = new DataFrame(global, generateSources());
            dataFrame.setVisible(true);
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_sourcedatabtnActionPerformed
//Generación de la ventana SWRPanel
    private void swrbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_swrbtnActionPerformed
        if (checkSWRRequirements()) {
            MultiFrame frame = new MultiFrame(300, 500, "SWR");
            frame.add(new SWRPanel(global, frame));
            frame.pack();
            frame.setVisible(true);
        }
    }//GEN-LAST:event_swrbtnActionPerformed
//Generación de la gráfica de la antena
    private void viewantbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewantbtnActionPerformed
        if (global.getgWires().size() > 0) {
            global.updatePlot(global);
        } else {
            global.errorMessage("Messages.nowires_title", "Messages.nowires");
        }
    }//GEN-LAST:event_viewantbtnActionPerformed
//Generación de la gráfica de patrón de radiación
    private void plotbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plotbtnActionPerformed
        HashMap<String, ArrayList<RPLine>> output = new HashMap<String, ArrayList<RPLine>>();
        LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>> srcInfo = new LinkedHashMap<Integer, LinkedHashMap<Double, ArrayList<AntInputLine>>>();
        boolean isOutputGenerated = false;
        boolean isSimulationOK = false;
        if (checkSimRequirements(global)) {
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
                global.setgSrcInfo(srcInfo);
                ArrayList<String> powerData = generatePowers(global);
                calculatePowerBudget(powerData, global);
                generateRPPlot(output, global);
            }
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_plotbtnActionPerformed
//Comportamiento del botón Abrir
    private void openbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openbtnActionPerformed
        ArrayList<String> data = global.openFile();
        if (data != null) {
            Global.loadInputFile(data, global);
            initializeInfo(global);
        }
    }//GEN-LAST:event_openbtnActionPerformed
//Comportamiento del botón Corrientes
    private void currentbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentbtnActionPerformed
        if (global.getgWires().size() > 0 && global.getgSource().size() > 0) {
            DataFrame dataFrame = new DataFrame(global, generateCurrents(global));
            dataFrame.setVisible(true);
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_currentbtnActionPerformed
//Comportamiento del botón Datos de Cargas
    private void loadsdatabtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadsdatabtnActionPerformed
        if (global.getgWires().size() > 0 && global.getgSource().size() > 0 && global.getgLoad().size() > 0) {
            ArrayList<String> powerData = generatePowers(global);
            PowerBudget pb = calculatePowerBudget(powerData, global);
            ArrayList<String> currentData = generateCurrents(global);
            ArrayList<Currents> iList = calculateCurrents(currentData, global);
            ArrayList<String> loadData = generateLoads(global);
            ArrayList<loadNEC> loadList = calculateLoads(loadData, global);
            mergeLoadsInfo(loadList, iList, pb);
            int i = 0;
            ArrayList<String> data = new ArrayList<String>();
            for (loadNEC nloadnec : loadList) {
                data.add("Carga " + i);
                data.add(loadNEC.loadNEC2String(nloadnec));
                i++;
            }
            DataFrame dataFrame = new DataFrame(global, data);
            dataFrame.setVisible(true);
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_loadsdatabtnActionPerformed
//Comportamiento del botón Datos de Potencia
    private void powerdatabtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_powerdatabtnActionPerformed
        if (global.getgWires().size() > 0 && global.getgSource().size() > 0) {
            DataFrame dataFrame = new DataFrame(global, generatePowers(global));
            dataFrame.setVisible(true);
        } else {
            global.errorValidateInput();
        }
    }//GEN-LAST:event_powerdatabtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Frequencybtn;
    private javax.swing.JLabel alterz0Info;
    private javax.swing.JButton alterz0btn;
    private javax.swing.JButton currentbtn;
    private javax.swing.JLabel directoryInfo;
    private javax.swing.JLabel directoryInfo0;
    private javax.swing.JLabel directoryInfo10;
    private javax.swing.JLabel directoryInfo12;
    private javax.swing.JLabel directoryInfo2;
    private javax.swing.JLabel directoryInfo3;
    private javax.swing.JLabel directoryInfo4;
    private javax.swing.JLabel directoryInfo5;
    private javax.swing.JLabel directoryInfo6;
    private javax.swing.JLabel directoryInfo7;
    private javax.swing.JLabel directoryInfo8;
    private javax.swing.JLabel directoryInfo9;
    private javax.swing.JButton directorybtn;
    private javax.swing.JLabel frequencyInfo;
    private javax.swing.JLabel groundTypeInfo;
    private javax.swing.JButton groundTypebtn;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel loadInfo;
    private javax.swing.JButton loadbtn;
    private javax.swing.JButton loadsdatabtn;
    private javax.swing.JButton openbtn;
    private javax.swing.JButton plotbtn;
    private javax.swing.JButton powerdatabtn;
    private javax.swing.JLabel rpInfo;
    private javax.swing.JButton rpbtn;
    private javax.swing.JButton savebtn;
    private javax.swing.JLabel sourceInfo;
    private javax.swing.JButton sourcebtn;
    private javax.swing.JButton sourcedatabtn;
    private javax.swing.JButton swrbtn;
    private javax.swing.JLabel tlInfo;
    private javax.swing.JButton tlbtn;
    private javax.swing.JLabel unitsInfo;
    private javax.swing.JButton unitsbtn;
    private javax.swing.JButton viewantbtn;
    private javax.swing.JLabel wavelengthInfo;
    private javax.swing.JButton wavelengthbtn;
    private javax.swing.JLabel wireInfo;
    private javax.swing.JButton wirebtn;
    private javax.swing.JLabel wirelossInfo;
    private javax.swing.JButton wirelossbtn;
    // End of variables declaration//GEN-END:variables
}
