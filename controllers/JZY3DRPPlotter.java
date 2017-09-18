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
package controllers;

import controllers.jzyd3.CustomCameraKeyController;
import controllers.jzyd3.CustomCameraMouseController;
import controllers.jzyd3.WireGraphLoader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import org.apache.log4j.BasicConfigurator;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.keyboard.camera.AWTCameraKeyController;
import org.jzy3d.chart.controllers.keyboard.screenshot.AWTScreenshotKeyController;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.chart.controllers.mouse.picking.AWTMousePickingController;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord2d;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.ViewBoundMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.plot3d.text.drawable.DrawableTextTexture;
import ucnecgui.Global;
import ucnecgui.jpanels.PatternPlotterPanel;
import ucnecgui.models.Point;
import ucnecgui.models.PointColor;
import ucnecgui.models.Tl;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class JZY3DRPPlotter extends JFrame {

    private WireGraphLoader WireGraph;
    private Chart chart;
    private JPanel panelCanvas;
    private Global global;

    /**
     * Constructor de la clase JZY3DRPPlotter
     *
     * @param global Objeto de la clase Global
     */
    public JZY3DRPPlotter(Global global) {
        super();
        this.global = global;
    }

    /**
     * Generar una gráfica de alambres, representando un patrón de radiación, a
     * partir de una colección de objetos en la cual para valor angular (De tipo
     * Double) existe una lista de coordenadas cartesianas indicando el valor
     * del campo eléctromagnético total para dicha posición (De tipo PointColor)
     *
     * @param rectangularCoord Colección de Objetos (Ángulo, Magnitud Etotal)
     */
    public void plot(HashMap<Double, ArrayList<PointColor>> rectangularCoord) {

        BasicConfigurator.configure();
        chart = AWTChartComponentFactory.chart(Quality.Advanced, IChartComponentFactory.Toolkit.swing);

        //Recorrido de la colección de objetos, si el ángulo está comprendido entre 0 y 360 grados, obtiene la lista de 
        //coodenadas correspondientes y las grafica a través del método graphRP, añadiéndolas despupes al objeto tipo Chart
        for (Map.Entry<Double, ArrayList<PointColor>> entry : rectangularCoord.entrySet()) {
            if (entry.getKey() >= 0 && entry.getKey() <= 360) {
                ArrayList<PointColor> nSlice = entry.getValue();
                WireGraph = new WireGraphLoader();
                WireGraph.graphRP(nSlice, global);
                getChart().getScene().getGraph().add(getWireGraph().lineStrips);
            }
        }

        //Si la gráfica es de tipo Azimutal o de Elevación, genera la leyenda correspondiente 
        if (global.getCurrentPlotType() != Global.PLOT3D) {
            generateText();
        }

        //Si la gráfica es de tipo 3D, configura al objeto tipo chart para que presente el comportamiento esperado
        if (global.getCurrentPlotType() == Global.PLOT3D) {
            chart.getView().setAxeBoxDisplayed(true);
            chart.getAxeLayout().setTickLineDisplayed(false);
            chart.getAxeLayout().setGridColor(Color.GRAY);
            float[] ticks = {0f, 0f, 0f};
            chart.getAxeLayout().setXTickProvider(new StaticTickProvider(ticks));
            chart.getAxeLayout().setYTickProvider(new StaticTickProvider(ticks));
            chart.getAxeLayout().setZTickProvider(new StaticTickProvider(ticks));
        } else {
            chart.getView().setAxeBoxDisplayed(false);
        }

        //Configuración del color de fondo de la gráfica
        getChart().getView().setBackgroundColor(Color.WHITE);

        //Configuración de la gráfica (Fondo, tipo de vista y márgenes)
         CustomCameraKeyController ckc = new CustomCameraKeyController(getChart());
        CustomCameraMouseController cmc = new CustomCameraMouseController(getChart());
        String path = "";
          if (global.isDirectorySet()) {
            path = global.getgDirectory() + "screenshot" + Calendar.YEAR + Calendar.MONTH + Calendar.DATE + "-" + Calendar.HOUR_OF_DAY + Calendar.MINUTE + Calendar.SECOND + ".png";
        } else {
            path = global.defaultDirectory() + "screenshot" + Calendar.YEAR + Calendar.MONTH + Calendar.DATE + "-" + Calendar.HOUR_OF_DAY + Calendar.MINUTE + Calendar.SECOND + ".png";
        }       
        getChart().addController(ckc);
        getChart().addController(cmc);
        getChart().addKeyboardScreenshotController();

        chart.getView().setViewPositionMode(ViewPositionMode.FREE);
        chart.getView().setBoundMode(ViewBoundMode.AUTO_FIT);

        //Configuración de directorio para guardar las capturas de imágenes
        Global.imagePath = System.getProperty("user.dir");

        //Si se  trata de una gráfica de tipo elevación, realiza una configuración adicional para ajustar la vista por defecto
        // de la gráfica
        if (global.getCurrentPlotType() == Global.ELEVATIONPLOT) {
            getChart().getView().setViewPoint(new Coord3d(-Math.PI / 2, Math.PI / 2, 0));
        } else {
            getChart().getView().setViewPoint(new Coord3d(-Math.PI / 2, Math.PI / 2, 0));
        }

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                e.getOppositeComponent().setVisible(true);
            }
        });

        //Adición de funcionalidades para el control de la gráfica por teclado
        addMouseListener(new AWTCameraMouseController(getChart()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(600, 600));
        setPanelCanvas(getChart());
        getContentPane().add(getPanelCanvas(), BorderLayout.CENTER);
        setResizable(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setTitle(global.plotType2String());
        pack();
        repaint();
        revalidate();
        setVisible(true);
    }

    /**
     * Genera la leyenda propia de los gráficos polares, en grados.
     */
    public void generateText() {
        int step = 45;
        int[] steps = {90, 45, 0, 315, 270, 225, 180, 135};
        for (int i = 0; i < 8; i++) {
            double x = 1.25 * Math.cos(Math.toRadians(step * i));
            double y = 1.25 * Math.sin(Math.toRadians(step * i));
            final DrawableTextTexture t;
            if (global.getCurrentPlotType() == Global.ELEVATIONPLOT || global.getCurrentPlotType() == Global.POLARIZATIONPLOT) {
                t = new DrawableTextTexture(steps[i] + "", new Coord2d(x, y), new Coord2d(0.2, 0.2));
            } else {
                t = new DrawableTextTexture((step * i) + "", new Coord2d(x, y), new Coord2d(0.2, 0.2));
                t.setBoundingBoxDisplayed(false);
            }

            t.setBoundingBoxColor(Color.WHITE);
            chart.getScene().getGraph().add(t);
        }

    }

    /**
     * @return the WireGraph
     */
    public WireGraphLoader getWireGraph() {
        return WireGraph;
    }

    /**
     * @return the chart
     */
    public Chart getChart() {
        return chart;
    }

    /**
     * @return the panelCanvas
     */
    public JPanel getPanelCanvas() {
        return panelCanvas;
    }

    /**
     * Configuración del panel donde se graficará la gráfica, recibe un objeto
     * tipo chart generado a través del método plot
     *
     * @param chart Objeto de la clase Chart
     */
    public void setPanelCanvas(Chart chart) {
        Component canvas = (Component) chart.getCanvas();
        PatternPlotterPanel panel = new PatternPlotterPanel(global);
        panel.setOpaque(false);
        panel.setBorder(new MatteBorder(5, 5, 5, 5, java.awt.Color.BLACK));
        panel.getPlotPanel().add(canvas, BorderLayout.CENTER);
        panel.generateInformation();
        panel.revalidate();
        panel.repaint();
        this.panelCanvas = panel;
    }
}
