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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
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
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import ucnecgui.Global;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.PointColor;
import ucnecgui.models.PolarData;
import ucnecgui.models.SWR;

/**
 *
 * @author Leoncio Gómez
 */
public class SWRPlot extends JFrame implements ChartMouseListener, KeyListener {

    private ChartPanel chartPanel;

    private Crosshair xCrosshair;

    private Crosshair yCrosshair;

    private LinkedHashMap<Double, ArrayList<AntInputLine>> swrData;

    private int plotType;

    private SWR swr;

    private ArrayList<PointColor> data;

    private ArrayList<PolarData> pData;

    /**
     * Constructor de la clase SWRPlot
     *
     * @param title Título de la ventana
     */
    public SWRPlot(String title) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setContentPane(createContent());
    }

    /**
     * Constructor de la clase SWRPlot a partir de los valores necesarios para
     * el cálculo del SWR
     *
     * @param title Título de la ventana
     * @param swrData Colección de Objetos cuya clase es la frecuencia de
     * iteración y su valor los datos correspondientes a los parámetros de la
     * fuente bajo estudio para dicha frecuencia
     * @param swr Objeto SWR con los parámetros de simulación para el cálculo
     * del SWR
     * @param plotType Tipo de gráfica a ser generada (Polar, Cartesiana)
     */
    public SWRPlot(String title, LinkedHashMap<Double, ArrayList<AntInputLine>> swrData, SWR swr, int plotType) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        this.swrData = swrData;
        this.swr = swr;
        this.plotType = plotType;
        setContentPane(createContent());

    }

    /**
     * Constructor de la clase SWRPlot a partir de los valores necesarios para
     * el cálculo del SWR, particularizado para la generación de diagramas
     * polares
     *
     * @param title Título de la ventana
     * @param data Arreglo de objetos de la clase PolarData con la información
     * de frecuencia y valor del SWR para dicha frecuencia
     * @param global Objeto de la clase Global
     */
    public SWRPlot(String title, ArrayList<PolarData> data, Global global) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        this.pData = data;
        setContentPane(createBDimContent());

    }

    /**
     * Constructor de la clase SWRPlot a partir de los valores necesarios para
     * el cálculo del SWR, particularizado para la generación de diagramas
     * cartesianos
     *
     * @param title Título de la ventana
     * @param data Arreglo de objetos de la clase PointColor con la información
     * de frecuencia y valor del SWR para dicha frecuencia
     * @param plotType Tipo de gráfica a ser generada
     */
    private SWRPlot(String title, ArrayList<PointColor> data, int plotType) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        this.data = data;
        this.plotType = plotType;
        setContentPane(createContent());
    }

    /**
     * Generación de gráfica cartesiana a través del API de JFreeChart
     *
     * @return Gráfica cartesiana generada a través del API de JFreeChart
     */
    private JPanel createContent() {
        JFreeChart chart = createChart(createDataset());

        //Configuración de la gráfica
        this.chartPanel = new ChartPanel(chart);
        chart.setBackgroundPaint(Color.WHITE);
        chartPanel.setBackground(Color.WHITE);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        plot.setRenderer(renderer);

        this.chartPanel.addChartMouseListener(this);
        Action anAction;
        anAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File imageFile = new File("SWRPlot.png");
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

        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);

        this.yCrosshair = new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        return chartPanel;
    }

    /**
     * Generación de gráfica cartesiana a través del API de JFreeChart
     *
     * @return Gráfica cartesiana generada a través del API de JFreeChart
     */
    private JPanel createBDimContent() {
        JFreeChart chart = createBDChart(createBDimDataset());
        this.chartPanel = new ChartPanel(chart);
        chart.setBackgroundPaint(Color.WHITE);
        chartPanel.setBackground(Color.WHITE);

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        plot.setRenderer(renderer);

        this.chartPanel.addChartMouseListener(this);
        Action anAction;
        anAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File imageFile = new File("SWRPlot.png");
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

        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f));
        this.xCrosshair.setLabelVisible(true);

        this.yCrosshair = new Crosshair(Double.NaN, Color.BLACK, new BasicStroke(0f));
        this.yCrosshair.setLabelVisible(true);
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        return chartPanel;
    }

    /**
     * Genera un objeto JFreeChart a partir de un conjunto de datos XYDataset
     *
     * @param dataset Conjunto de datos XYDataset con la información a ser
     * representada en la gráfica
     * @return Objeto de la clase JFreeChart
     */
    private JFreeChart createChart(XYDataset dataset) {

        String title = Global.getMessages().getString("SWR.label.label") + " " + Global.getMessages().getString("SWR.source") + " " + swr.getSrcIndex();
        String xLabel = Global.getMessages().getString("SWR.frequency");
        String yLabel = "";
        if (plotType == Global.PLOTTIMES) {
            yLabel = Global.getMessages().getString("SWR.times");
        } else if (plotType == Global.PLOTDB) {
            yLabel = Global.getMessages().getString("SWR.db");
        }
        JFreeChart chart = ChartFactory.createXYLineChart(title,
                xLabel, yLabel, dataset);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        return chart;
    }

    /**
     * Genera un objeto JFreeChart a partir de un conjunto de datos XYDataset.
     * Particularizado para la representación de un diagrama de radiación
     * bidimensional
     *
     * @param dataset Conjunto de datos XYDataset con la información a ser
     * representada en la gráfica
     * @return Objeto de la clase JFreeChart
     */
    private JFreeChart createBDChart(XYDataset dataset) {

        String title = "Patrón de Radiación Cartesiano";
        String xLabel = "Frecuencia (MHz)";
        String yLabel = "";
        if (plotType == Global.RPMODULE) {
            yLabel = Global.getMessages().getString("|V|");
        } else if (plotType == Global.RPNORMALIZED) {
            yLabel = Global.getMessages().getString("Vnorm");
        }
        JFreeChart chart = ChartFactory.createXYLineChart(title,
                xLabel, yLabel, dataset);
        chart.getPlot().setBackgroundPaint(Color.WHITE);

        return chart;
    }

    /**
     * Crea un objeto XYDatase que contiene los datos del diagrama de SWR
     *
     * @return Objeto XYDataset con los datos del diagrama de SWR
     */
    private XYDataset createDataset() {
        String seriesName = "";
        switch (plotType) {
            case Global.PLOTTIMES:
                seriesName = Global.getMessages().getString("SWR.times");
                break;
            case Global.PLOTDB:
                seriesName = Global.getMessages().getString("SWR.db");
                break;
            default:
                throw new AssertionError();
        }
        XYSeries series = new XYSeries(seriesName);

        Complex z0 = null;
        if (swr.isUseAltZ0()) {
            z0 = new Complex(swr.getAltZ0(), 0);
        } else {
            z0 = new Complex(50, 0);
        }

        for (Map.Entry<Double, ArrayList<AntInputLine>> entry : swrData.entrySet()) {
            double x = entry.getKey();
            ArrayList<AntInputLine> antInputLineList = entry.getValue();
            AntInputLine nAIL = antInputLineList.get(swr.getSrcIndex() - 1);

            //Cálculo del valor del SWR para una frecuencia determinada
            Complex zl = new Complex(nAIL.getImpedanceReal(), nAIL.getImpedanceImaginary());
            Complex num = zl.minus(z0);
            Complex den = zl.plus(z0);
            Complex rho = num.divides(den);
            double absRho = rho.abs();
            if (absRho == 1) {
                absRho = 0.99;
            }
            double y = (1 + absRho) / (1 - absRho);
            if (plotType == Global.PLOTTIMES) {
                series.add(x, y);
            } else {
                series.add(x, 10 * Math.log10(y));
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        return dataset;
    }

    /**
     * Crea un objeto XYDatase que contiene los datos del diagrama de radiación
     * bidimensional
     *
     * @return Objeto XYDataset con los datos del diagrama de radiación
     * bidimensional
     */
    private XYDataset createBDimDataset() {
        String seriesName = "";
        XYSeries series = new XYSeries(seriesName);

        for (PolarData pdata : pData) {
            double x = pdata.getAngle();
            double y = pdata.getR();
            series.add(x, y);
        }
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
    }

    @Override
    public void chartMouseClicked(ChartMouseEvent event) {
    }

    // Comportamiento del marcador controlador por el ratón de la computadora
    @Override
    public void chartMouseMoved(ChartMouseEvent event) {
        Rectangle2D dataArea = this.chartPanel.getScreenDataArea();

        JFreeChart chart = event.getChart();
        XYPlot plot = (XYPlot) chart.getPlot();
        ValueAxis xAxis = plot.getDomainAxis();
        double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea,
                RectangleEdge.BOTTOM);
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        this.yCrosshair.setValue(y);
    }

    /**
     * Ejecuta la generación del API JFreeChart a partir de los parámetros y
     * datos del objeto SWR.
     *
     * @param swrData Colección de Objetos cuya clase es la frecuencia de
     * iteración y su valor los datos correspondientes a los parámetros de la
     * fuente bajo estudio para dicha frecuencia
     * @param swr Objeto de la clase SWR
     * @param plotType Tipo de gráfica a ser generada
     * @param global Objeto de la clase Global
     */
    public static void execute(LinkedHashMap<Double, ArrayList<AntInputLine>> swrData, SWR swr, int plotType, Global global) {
        String title = Global.getMessages().getString("SWR.label0.label");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SWRPlot app = new SWRPlot(title, swrData, swr, plotType);
                app.pack();
                app.setVisible(true);
            }
        });
    }

    /**
     * Ejecuta la generación del API JFreeChart a partir de los parámetros y
     * datos de un arreglo de objetos PolarData
     *
     * @param title Título de la gráfica a ser generada
     * @param data Arreglo de objetos de la clase PolarData con la información
     * @param global Objeto de la clase Global
     */
    public static void execute(String title, ArrayList<PolarData> data, Global global) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SWRPlot app = new SWRPlot(title, data, global);
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
    public ArrayList<PointColor> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(ArrayList<PointColor> data) {
        this.data = data;
    }

    /**
     * @return the pData
     */
    public ArrayList<PolarData> getpData() {
        return pData;
    }

    /**
     * @param pData the pData to set
     */
    public void setpData(ArrayList<PolarData> pData) {
        this.pData = pData;
    }
}
