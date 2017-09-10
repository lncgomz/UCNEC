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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;

import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ucnecgui.Global;
import ucnecgui.models.PolarData;

/**
 *
 * @author Leoncio Gómez
 */
public class PolarPlotter extends JFrame implements ChartMouseListener, KeyListener {

    private ChartPanel chartPanel;
    private ArrayList<PolarData> data;
    private HashMap<String, ArrayList<PolarData>> dat;
    private String title;
    private double tick;
    private int colorPlot;
    private Global global;

    /**
     * Constructor de la clase PolarPlotter
     *
     * @param title Título de la ventana
     */
    public PolarPlotter(String title) {
        super(title);
        setContentPane(createContent());
    }

    /**
     * Constructor de la clase PolarPlotter a partir de los datos a graficar,
     * separación angular y color de la gráfica
     *
     * @param title Título de la ventana
     * @param data Arreglo de objetos PolarData con los datos a ser
     * representados en el diagrama polar
     * @param tick Separación angular en la gráfica polar
     * @param colorPlot Color de la gráfica pola r
     * @param global Objeto de la clase Global
     */
    public PolarPlotter(String title, ArrayList<PolarData> data, double tick, int colorPlot, Global global) {
        super(title);
        this.title = title;
        this.data = data;
        this.global = global;
        this.tick = tick;
        this.colorPlot = colorPlot;
        setContentPane(createContent());
    }

    /**
     * Constructor de la clase PolarPlotter a partir de los datos a graficar
     * (Como colección de objetos), separación angular y color de la gráfica
     *
     * @param title Título de la ventana
     * @param data Colección de objetos PolarData cuya clave es el ángulo de
     * iteración y su valor es un arreglo de objetos PolarData con los valores
     * de Etotal para un ángulo de iteración constante
     * @param tick Separación angular en la gráfica polar
     * @param colorPlot Color de la gráfica pola r
     * @param global Objeto de la clase Global
     */
    public PolarPlotter(String title, HashMap<String, ArrayList<PolarData>> data, double tick, int colorPlot, Global global) {
        super(title);
        this.title = title;
        this.dat = data;
        this.global = global;
        this.tick = tick;
        this.colorPlot = colorPlot;
        setContentPane(createElipsisContent());
    }

    /**
     * Constructor de la clase PolarPlotter a partir de los datos a graficar
     * (Como arreglo de objetos), separación angular y color de la gráfica
     *
     * @param title Título de la ventana
     * @param data Arreglo de objetos PolarData con los datos a ser
     * representados en el diagrama polar
     * @param tick Separación angular en la gráfica polar     
     * @param global Objeto de la clase Global
     */
    public PolarPlotter(String title, ArrayList<PolarData> data, double tick, Global global) {
        super(title);
        this.title = title;
        this.data = data;
        this.global = global;
        this.tick = tick;
        setContentPane(createRPContent());
    }

    /**
     * Inicializa el API de graficación de JFreeChart para la generación del
     * diagrama Polar
     *
     * @return Diagrama Polar de JFreeChart
     */
    private JPanel createContent() {
        JFreeChart chart = createChart(createDataset());
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        this.chartPanel = new ChartPanel(chart);
        chart.setBackgroundPaint(Color.WHITE);
        chartPanel.setBackground(Color.WHITE);
        this.chartPanel.addChartMouseListener(this);
        Action anAction;
        anAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File imageFile = new File("PolarizationPlot.png");
                int width = 600;
                int height = 600;

                try {
                    ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        };
        this.chartPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_S)),
                "doSomething");
        this.chartPanel.getActionMap().put("doSomething", anAction);
        return chartPanel;
    }

    /**
     * Inicializa el API de graficación de JFreeChart para la generación del
     * diagrama Polar, particularizado para visualizar un patrón de radiación
     *
     * @return Diagrama del Patrón de Radiación construído con JFreeChart
     */
    private JPanel createRPContent() {
        JFreeChart chart = createChart(createRPDataset());
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        this.chartPanel = new ChartPanel(chart);
        chart.setBackgroundPaint(Color.WHITE);
        chartPanel.setBackground(Color.WHITE);
        this.chartPanel.addChartMouseListener(this);
        Action anAction;
        anAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File imageFile = new File("RPplott.png");
                int width = 600;
                int height = 600;

                try {
                    ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        };
        this.chartPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_S)),
                "doSomething");
        this.chartPanel.getActionMap().put("doSomething", anAction);
        return chartPanel;
    }

    /**
     * Inicializa el API de graficación de JFreeChart para la generación de la
     * elipse de polarización
     *
     * @return Diagrama de la elipse de polarización construída con JFreeChart
     */
    private JPanel createElipsisContent() {
        JFreeChart chart = createChart(createElipsisDataset());
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        this.chartPanel = new ChartPanel(chart);
        chart.setBackgroundPaint(Color.WHITE);
        chartPanel.setBackground(Color.WHITE);
        this.chartPanel.addChartMouseListener(this);
        Action anAction;
        anAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File imageFile = new File("PolarizationPlot.png");
                int width = 600;
                int height = 600;

                try {
                    ChartUtilities.saveChartAsPNG(imageFile, chart, width, height);
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        };
        this.chartPanel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_S)),
                "doSomething");
        this.chartPanel.getActionMap().put("doSomething", anAction);
        return chartPanel;
    }

    /**
     * Inicializa el API de graficación de JFreeChart para la generación de la
     * una gráfica cartesiana
     *
     * @param dataset Conjunto de coordenadas cartesianas a ser representadas en
     * el diagrama
     *
     * @return Diagrama cartesiano construído con JFreeChart
     */
    private JFreeChart createChart(XYDataset dataset) {

        final JFreeChart chart = ChartFactory.createPolarChart(
                title, dataset, true, true, false
        );
        final PolarPlot plot = (PolarPlot) chart.getPlot();
        plot.setAngleGridlinesVisible(true);
        plot.setRadiusGridlinesVisible(true);
        plot.setAngleTickUnit(new NumberTickUnit(tick));
        plot.setAngleGridlinePaint(Color.BLACK);
        final DefaultPolarItemRenderer renderer = (DefaultPolarItemRenderer) plot.getRenderer();

        renderer.setSeriesFilled(0, true);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        Color c = null;
        switch (colorPlot) {
            case 0:
                c = Color.RED;
                break;
            case 1:
                c = Color.YELLOW;
                break;
            case 2:
                c = Color.CYAN;
                break;
            case 3:
                c = Color.GREEN;
                break;
            case 4:
                c = Color.BLUE;
                break;
            case 5:
                c = Color.BLACK;
                break;
            default:
                throw new AssertionError();
        }
        renderer.setSeriesPaint(0, c);
        renderer.setSeriesFillPaint(0, c);
        renderer.setSeriesOutlinePaint(0, c);
        return chart;
    }

    /**
     * Genera el conjunto de coordenadas polares a ser representadas en el
     * diagrama polar
     *
     * @return Conjunto de coordenadas polares
     */
    private XYDataset createDataset() {

        XYSeries series = new XYSeries("|V|");
        for (PolarData polarData : data) {
            final double theta = polarData.getAngle();
            final double radius = polarData.getR();
            series.add(theta, radius);
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        return dataset;
    }

    /**
     * Genera el conjunto de coordenadas polares a ser representadas en el
     * diagrama polar, particularizadas para la construcción de la elipse de
     * polarización
     *
     * @return Conjunto de coordenadas polares para elipse de polarización
     */
    public XYDataset createElipsisDataset() {
        XYSeries seriesVnorm = new XYSeries("|Vnorm|");
        ArrayList<PolarData> vnorm = dat.get("vnorm");
        for (PolarData polarData : vnorm) {
            final double theta = polarData.getAngle();
            final double radius = polarData.getR();
            seriesVnorm.add(theta, radius);
        }
        XYSeries seriesR = new XYSeries("r");
        ArrayList<PolarData> r = dat.get("r");
        for (PolarData polarData : r) {
            final double theta = polarData.getAngle();
            final double radius = polarData.getR();
            seriesR.add(theta, radius);
        }
        XYSeries seriesR1 = new XYSeries("r1");
        ArrayList<PolarData> r1 = dat.get("r1");
        for (PolarData polarData : r1) {
            final double theta = polarData.getAngle();
            final double radius = polarData.getR();
            seriesR1.add(theta, radius);
        }

        XYSeries seriesR2 = new XYSeries("r2");
        ArrayList<PolarData> r2 = dat.get("r2");
        for (PolarData polarData : r2) {
            final double theta = polarData.getAngle();
            final double radius = polarData.getR();
            seriesR2.add(theta, radius);
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesVnorm);
        dataset.addSeries(seriesR);
        dataset.addSeries(seriesR1);
        dataset.addSeries(seriesR2);
        return dataset;
    }

    /**
     * Genera el conjunto de coordenadas polares a ser representadas en el
     * diagrama polar, particularizadas para la construcción del patrón de
     * radiación
     *
     * @return Conjunto de coordenadas polares para patrón de radiación
     */
    public XYDataset createRPDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        switch (global.getCurrentPlotType()) {
            case Global.RPMODULE:
                XYSeries seriesV = new XYSeries("|V|");
                for (PolarData polarData : data) {
                    final double theta = polarData.getAngle();
                    final double radius = polarData.getR();
                    seriesV.add(theta, radius);
                }
                dataset.addSeries(seriesV);
                break;
            case (Global.RPNORMALIZED):
                XYSeries seriesVnorm = new XYSeries("|Vnorm|");
                for (PolarData polarData : data) {
                    final double theta = polarData.getAngle();
                    final double radius = polarData.getR();
                    seriesVnorm.add(theta, radius);
                }
                dataset.addSeries(seriesVnorm);
            default:
                throw new AssertionError();
        }
        return dataset;
    }


    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
    }

    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
    }

    /**
     * Ejecuta el API de JFreeChart
     *
     * @param title Título de la gráfica
     * @param data Arreglo de objetos de la clase PolarData con las coordenadas
     * polares a ser representadas en el correspondiente diagrama
     * @param ticks Separación angular dentro de la gráfica polar
     * @param global Objeto de la clase Global
     */
    public static void execute(String title, ArrayList<PolarData> data, double ticks, Global global) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PolarPlotter app = new PolarPlotter(title, data, ticks, global);
                app.pack();
                app.setVisible(true);
            }
        });
    }

    /**
     * Ejecuta el API de JFreeChart, generando una gráfica de color específico
     *
     * @param title Título de la gráfica
     * @param data Arreglo de objetos de la clase PolarData con las coordenadas
     * polares a ser representadas en el correspondiente diagrama
     * @param ticks Separación angular dentro de la gráfica polar
     * @param colorPlot Color de la gráfica a ser generada
     * @param global Objeto de la clase Global
     */
    public static void execute(String title, ArrayList<PolarData> data, double ticks, int colorPlot, Global global) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PolarPlotter app = new PolarPlotter(title, data, ticks, colorPlot, global);
                app.pack();
                app.setVisible(true);
            }
        });
    }

    /**
     * Ejecuta el API de JFreeChart, generando una gráfica de color específico,
     * a partir de una colección de objetos cuya clave es un ángulo de iteración
     * y su valor un arreglo de objetos PolarData con las coordenadas polares de
     * los puntos a ser representados
     *
     * @param title Título de la gráfica
     * @param data Colección de objetos cuya clave es un ángulo de iteración
     * y su valor un arreglo de objetos PolarData con las coordenadas polares de
     * los puntos a ser representados
     * @param ticks Separación angular dentro de la gráfica polar
     * @param colorPlot Color de la gráfica a ser generada
     * @param global Objeto de la clase Global
     */
    public static void execute(String title, HashMap<String, ArrayList<PolarData>> data, double ticks, int colorPlot, Global global) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PolarPlotter app = new PolarPlotter(title, data, ticks, colorPlot, global);
                app.pack();
                app.setVisible(true);
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char ch = e.getKeyChar();
        if (ch == KeyEvent.VK_S) {
            File imageFile = new File("SWRChart.png");
            int width = 640;
            int height = 480;

            try {
                ChartUtilities.saveChartAsPNG(imageFile, chartPanel.getChart(), width, height);

            } catch (IOException ex) {
                Logger.getLogger(SWRPlot.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the data
     */
    public ArrayList<PolarData> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ArrayList<PolarData> data) {
        this.data = data;
    }
}
