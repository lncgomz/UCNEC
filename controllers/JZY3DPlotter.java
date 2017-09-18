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

import controllers.jzyd3.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.keyboard.camera.AWTCameraKeyController;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.chart.controllers.mouse.picking.AWTMousePickingController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.axes.layout.IAxeLayout;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.ViewBoundMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.apache.log4j.BasicConfigurator;
import org.jzy3d.chart.controllers.mouse.picking.AWTMousePickingPan2dController;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import ucnecgui.Global;
import ucnecgui.jpanels.PlotterPanel;
import ucnecgui.models.Point;
import ucnecgui.models.Tl;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */
public class JZY3DPlotter extends JFrame {

    private WireGraphLoader WireGraph;
    private Chart chart;
    private JPanel panelCanvas;
    private Global global;

    /**
     * Constructor de la clase JZY3DPlotter, recibe el objeto global Global
     *
     * @param global Objeto de la clase Global
     */
    public JZY3DPlotter(Global global) {
        super();
        this.global = global;
    }

    /**
     * Generar una gráfica de alambres a partir de una lista de objetos Wire,
     * resaltando el alambre cuya etiqueta coincida con el parámetro selectedRow
     * y los elementos incluídos en la clase global Global
     *
     * @param wires Lista de objetos Wire
     * @param selectedRow Índice del alambre seleccionado
     * @param global Objeto de la clase Global
     */
    public void plot(ArrayList<Wire> wires, int selectedRow, Global global) {
        BasicConfigurator.configure();
        WireGraphLoader marks = new WireGraphLoader();
        WireGraphLoader axis = new WireGraphLoader();
        WireGraphLoader source_load_marks = new WireGraphLoader();
        WireGraphLoader networks_marks = new WireGraphLoader();

        WireGraph = new WireGraphLoader();

        // Graficación de los objetos Wire
        getWireGraph().graphWires(wires, selectedRow, global);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, IChartComponentFactory.Toolkit.swing);

        //Configuración de la gráfica (Fondo, tipo de vista y márgenes)
        getChart().getView().setBackgroundColor(Color.WHITE);
        chart.getView().setViewPositionMode(ViewPositionMode.FREE);
        chart.getView().setBoundMode(ViewBoundMode.AUTO_FIT);
        chart.getView().setAxeBoxDisplayed(true);
        chart.getAxeLayout().setTickLineDisplayed(false);
        chart.getAxeLayout().setGridColor(Color.GRAY);
        float[] ticks = {0f, 0f, 0f};
        chart.getAxeLayout().setXTickProvider(new StaticTickProvider(ticks));
        chart.getAxeLayout().setYTickProvider(new StaticTickProvider(ticks));
        chart.getAxeLayout().setZTickProvider(new StaticTickProvider(ticks));

        //Graficación de alambres auxiliares triviales  para visualización de entorno 3D
        axis.graphAxis(global);

        //Se agregar los alambres de interés y los auxiliares a la gráfica
        getChart().getScene().getGraph().add(getWireGraph().lineStrips);
        getChart().getScene().getGraph().add(axis.lineStrips);

        //Graficación de Líneas de Transmisión (Si las hubiese)
        if (global.getgTl().size() > 0) {
            marks.graphTl(global.getgTl(), global);
            getChart().getScene().getGraph().add(marks.lineStrips);
        }

        //Graficación de Fuentes (Si las hubiese)
        if (global.getgSource().size() > 0) {
            source_load_marks.graphSource(global.getgSource(), global);
            getChart().getScene().getGraph().add(source_load_marks.lineStrips);
        }

        //Graficación de Cargas (Si las hubiese)
        if (global.getgLoad().size() > 0) {
            marks.graphLoad(global.getgLoad(), global);
            getChart().getScene().getGraph().add(marks.lineStrips);
        }

        //Graficación de Redes (Si las hubiese)
        if (global.getgNetwork().size() > 0) {
            networks_marks.graphNetwork(global.getgNetwork(), global);
            getChart().getScene().getGraph().add(networks_marks.lineStrips);
        }

        //Adición de funcionalidades para el control de la gráfica por teclado       
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
        

        //Configuración de la posición de la cámara por defecto
        getChart().getView().setViewPoint(new Coord3d(-2 * Math.PI / 3, Math.PI / 4, 0));

        //Leyenda de los ejes coordenados
        IAxeLayout axeLayout = getChart().getAxeLayout();
        axeLayout.setXAxeLabel("EJE X");
        axeLayout.setYAxeLabel("EJE Y");
        axeLayout.setZAxeLabel("EJE Z");

        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                e.getOppositeComponent().setVisible(true);
            }
        });

        //Configuración de la ventana donde aparecerá la gráfica
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 500));
        setPanelCanvas(getChart());
        getContentPane().add(getPanelCanvas(), BorderLayout.CENTER);
        setResizable(true);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setTitle("Plot");
        pack();
        setVisible(true);
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
        PlotterPanel panel = new PlotterPanel();
        panel.setOpaque(false);
        panel.setBorder(new MatteBorder(5, 5, 5, 5, java.awt.Color.BLACK));
        panel.getPlotPanel().add(canvas, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
        this.panelCanvas = panel;
    }

    /**
     * Método para convertir los objetos de tipo Tl (Líneas de transmisión) a
     * una lista de alambres, a fin de ser gráficados como tales.
     *
     * @return Lista de objetos Wire con la representación de las líneas de
     * transmisión
     */
    public ArrayList<Wire> getTlWires() {
        ArrayList<Tl> transmissionLines = global.getgTl();
        ArrayList<Wire> resp = new ArrayList<Wire>();
        for (Tl tls : transmissionLines) {
            Wire wA = global.getgWires().get(tls.getTlWireTag1());
            Wire wB = global.getgWires().get(tls.getTlWireTag2());
            Point pointA = wA.getLocation(tls.getTlWire1SegPercentage() / 100.00);
            Point pointB = wB.getLocation(tls.getTlWire2SegPercentage() / 100.00);
            Wire nWire = new Wire(pointA, pointB);
            resp.add(nWire);
        }
        return resp;
    }
}
