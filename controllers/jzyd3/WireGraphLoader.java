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

package controllers.jzyd3;

import controllers.RadiationPatternPlottingController;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.CroppableLineStrip;
import org.jzy3d.plot3d.primitives.Point;
import ucnecgui.Global;

import ucnecgui.models.Line;
import ucnecgui.models.Load;
import ucnecgui.models.Network;
import ucnecgui.models.PointColor;
import ucnecgui.models.Source;
import ucnecgui.models.Tl;
import ucnecgui.models.Wire;

/**
 *
 * @author Leoncio Gómez
 */

public class WireGraphLoader {
    
    /**
     *
     */
    public List<CroppableLineStrip> lineStrips = new ArrayList<CroppableLineStrip>();
    String newline = System.getProperty("line.separator");

    /**
     *Genera una lista de objetos CroppableLineStrip que representan los segmentos indicados en el arreglo de objetos Wire wires, resaltando aquel cuya etiqueta
     * sea igual al índice indicado en selectedRows.
     * @param wires Arreglo de objetos Wire indicando la geometría que se desea graficar
     * @param selectedRows Índice del alambre a ser resaltado
     * @param global Objeto de la clase Global
     * @return  Lista de objetos CroppableLineStrip que representan la geometría a ser graficada
     */
    public List<CroppableLineStrip> graphWires(ArrayList<Wire> wires, int selectedRows, Global global) {
        int lineNumber = 0;

        CroppableLineStrip lineStrip = null;
        for (Wire wire : wires) {
            if (wire.getNumber() == global.getCurrentSourceTag()) {
                continue;
            }
            lineStrip = new CroppableLineStrip();

            if (isSelectedWire(selectedRows, wire.getNumber())) {
                lineStrip.setWireframeColor(Color.RED); //El segmento resaltado tendrá color rojo
            } else {
                lineStrip.setWireframeColor(Color.BLUE); //Los segmentos no resaltados tendrán color azul
            }
            try { //Se genera un segmento a partir de los puntos (X,Y,Z) 1 y (X,Y,Z) 2 que constituyen a cada objeto de la clase Wire                 
                lineStrip.add(new Point(new Coord3d((float) wire.getX1(), (float) wire.getY1(), (float) wire.getZ1())));
                lineStrip.add(new Point(new Coord3d((float) wire.getX2(), (float) wire.getY2(), (float) wire.getZ2())));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                String malformedCoordinateError = "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted";
                JOptionPane.showMessageDialog(null, malformedCoordinateError, "Incorrect Format", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(-1);
            }
            lineStrips.add(lineStrip);
            lineNumber++;
        }
        return lineStrips;
    }

    /**
     *Grafica los radios correespondientes a las gráficas polares, de longitud unitaria y espaciados a 45 grados entre sí.
     */
    public void graphRadius() {
        CroppableLineStrip lineStrip = null;

        ArrayList<PointColor> nRadio = RadiationPatternPlottingController.generateRadius();

        for (int i = 0; i < nRadio.size(); i++) {
            lineStrip = new CroppableLineStrip();
            lineStrip.setWireframeColor(new Color(230, 230, 230));
            try {
                lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) 0)));
                lineStrip.add(new Point(new Coord3d((float) nRadio.get(i).getA(), (float) nRadio.get(i).getB(), (float) nRadio.get(i).getC())));
                lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) 0)));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.exit(-1);
            }

            lineStrips.add(lineStrip);
        }
    }

    /**    
     *Grafica los círculos correespondientes a las gráficas polares, de longitud radio y centrados en el origen de coordenadas
     * @param ratio Radio del círculo a ser graficado
     */
    public void graphCircles(double ratio) {
        CroppableLineStrip lineStrip = null;

        ArrayList<PointColor> nRadio = RadiationPatternPlottingController.generateCircle(ratio);

        for (int i = 0; i < nRadio.size() - 1; i++) {
            lineStrip = new CroppableLineStrip();
            lineStrip.setWireframeColor(new Color(230, 230, 230));
            try {                
                lineStrip.add(new Point(new Coord3d((float) nRadio.get(i).getA(), (float) nRadio.get(i).getB(), (float) nRadio.get(i).getC())));
                lineStrip.add(new Point(new Coord3d((float) nRadio.get(i + 1).getA(), (float) nRadio.get(i + 1).getB(), (float) nRadio.get(i + 1).getC())));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            lineStrips.add(lineStrip);
        }
    }

    /**
     *Genera la representación de  todos los elementos que constituyen un Patrón de Radiación (Geometría, Fuentes, Cargas, Líneas de Transmisión, Redes, Ejes, Aperturas de Haz, 
     * lóbulo secundario, lóbulo trasero y leyendas)
     * @param points Arreglo de objetos PointColor, correspondientes a los valores de Etotal para el o los ángulos de iteración de interés
     * @param global Objeto de la clase Global
     * @return Lista de objetos CroppableLineStrip con la representación de los elementos constituyentes del diagrama de radiación
     */
    public List<CroppableLineStrip> graphRP(ArrayList<PointColor> points, Global global) {

        CroppableLineStrip lineStrip = null;

        int lineNumber = 0;
        if (global.getCurrentPlotType() != Global.PLOT3D) {
            graphRadius();
            graphCircles(1);
            graphCircles(1 / Math.sqrt(2));
            graphCircles(0.3);
            graphCircles(0.1);
        }
        
        for (int i = 0; i < points.size() - 1; i++) {
            lineStrip = new CroppableLineStrip();
            lineStrip.setWireframeColor(Color.BLUE);
            try {                
                lineStrip.add(new Point(new Coord3d((float) points.get(i).getA(), (float) points.get(i).getB(), (float) points.get(i).getC())));
                lineStrip.add(new Point(new Coord3d((float) points.get(i + 1).getA(), (float) points.get(i + 1).getB(), (float) points.get(i + 1).getC())));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                String malformedCoordinateError = "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted";
                JOptionPane.showMessageDialog(null, malformedCoordinateError, "Incorrect Format", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(-1);
            }

            lineStrips.add(lineStrip);
            lineNumber++;
        }

        if (global.getCurrentPlotType() != Global.PLOT3D) {
            lineStrip = new CroppableLineStrip();
            lineStrip.setWireframeColor(Color.GREEN);
            ArrayList<PointColor> maxVal = global.getgMaxValue();

            try {
                lineStrip.add(new Point(new Coord3d((float) ((maxVal.get(0)).getA()), (float) ((maxVal.get(0)).getB()), (float) 0)));
                lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) 0)));
                lineStrips.add(lineStrip);
            } catch (NumberFormatException e) {
            }
            ArrayList<PointColor> aperture = global.getgAperture();
            if (aperture.size() >= 2) {
                for (int i = 0; i < 2; i++) {
                    PointColor nAp = aperture.get(i);
                    lineStrip = new CroppableLineStrip();
                    lineStrip.setWireframeColor(Color.RED);
                    try {
                        lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) 0)));
                        lineStrip.add(new Point(new Coord3d((float) (nAp.getA()), (float) (nAp.getB()), (float) 0)));
                        lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) 0)));
                        lineStrips.add(lineStrip);

                    } catch (NumberFormatException e) {
                    }
                }
            }

            lineStrip = new CroppableLineStrip();
            lineStrip.setWireframeColor(Color.CYAN);
            PointColor sideLobe = global.getgSideValue();
            if (sideLobe != null) {
                try {
                    lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) 0)));
                    lineStrip.add(new Point(new Coord3d((float) (sideLobe.getA()), (float) (sideLobe.getB()), (float) 0)));
                    lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) 0)));
                    lineStrips.add(lineStrip);
                } catch (NumberFormatException e) {
                }
            }

        }
        return lineStrips;
    }

    /**
     *Genera la representación de una serie de ejes auxiliares para mejorar la visualización de la grilla de coordenadas en el API de graficación
     * @param global Objeto de la clase Global
     * @return Lista de objetos CroppableLineStrip con la representación de los ejes auxiliares
     */
    public List<CroppableLineStrip> graphAxis(Global global) {

        CroppableLineStrip lineStrip = null;
        lineStrip = new CroppableLineStrip();
        lineStrip.setWireframeColor(new Color(230, 230, 230));
        lineStrip.setWidth(1f);
        lineStrip.add(Coord3d.ORIGIN);
        lineStrip.add(new Point(new Coord3d((float) (0.0000001), (float) 0, (float) 0)));
        lineStrips.add(lineStrip);
        lineStrip = new CroppableLineStrip();
        lineStrip.setWireframeColor(new Color(230, 230, 230));
        lineStrip.add(Coord3d.ORIGIN);
        lineStrip.add(new Point(new Coord3d((float) 0, (float) (0.0000001), (float) 0)));
        lineStrips.add(lineStrip);
        lineStrip = new CroppableLineStrip();
        lineStrip.setWireframeColor(new Color(230, 230, 230));        
        lineStrip.add(Coord3d.ORIGIN);
        lineStrip.add(new Point(new Coord3d((float) 0, (float) 0, (float) (0.0000001))));
        lineStrips.add(lineStrip);
        return lineStrips;
    }

     /**
     *Genera la representación de  todos los elementos que constituyen las líneas de transmisión empleadas en la simulación
     * @param tls Arreglo de objetos Tl, correspondientes a las líneas de transmisión empleadas en la simulación
     * @param global Objeto de la clase Global
     * @return Lista de objetos CroppableLineStrip con la representación de las líneas de transmisión empleadas en la simulación
     */
    public List<CroppableLineStrip> graphTl(ArrayList<Tl> tls, Global global) {

        int lineNumber = 0;

        CroppableLineStrip lineStrip = null;
        ucnecgui.models.Point nPoint = null;
        ucnecgui.models.Point mPoint = null;
        ArrayList<Line> lines = new ArrayList<Line>();
        for (Tl tl : tls) {
            lineStrip = new CroppableLineStrip();
            lineStrip.setWireframeColor(Color.CYAN);
            int wireNumber = tl.getTlWireTag1();
            int wireNumber2 = tl.getTlWireTag2();
            Wire nWire = global.getgWires().get(wireNumber - 1);
            Wire mWire = global.getgWires().get(wireNumber2 - 1);
            nPoint = nWire.getLocation((tl.getTlWire1SegPercentage() / 100.00));
            mPoint = mWire.getLocation((tl.getTlWire2SegPercentage() / 100.00));
            lines.add(new Line(new ucnecgui.models.Point(nPoint.getA(), nPoint.getB(), nPoint.getC()), new ucnecgui.models.Point(mPoint.getA(), mPoint.getB(), mPoint.getC())));
        }

        for (Line line : lines) {
            try {
                // Add the map point to the line strip
                lineStrip.add(new Point(new Coord3d((float) line.getX1(), (float) line.getY1(), (float) line.getZ1())));
                lineStrip.add(new Point(new Coord3d((float) line.getX2(), (float) line.getY2(), (float) line.getZ2())));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                String malformedCoordinateError = "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted";
                JOptionPane.showMessageDialog(null, malformedCoordinateError, "Incorrect Format", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(-1);
            }
            lineStrips.add(lineStrip);
            lineNumber++;
        }

        return lineStrips;
    }

     /**
     *Genera la representación de  todos los elementos que constituyen las fuentes empleadas en la simulación
     * @param sources Arreglo de objetos Source, correspondientes a las fuentes empleadas en la simulación
     * @param global Objeto de la clase Global
     * @return Lista de objetos CroppableLineStrip con la representación de las fuentes empleadas en la simulación
     */
    public List<CroppableLineStrip> graphSource(ArrayList<Source> sources, Global global) {

       int lineNumber = 0;
        CroppableLineStrip lineStrip = null;
        ucnecgui.models.Point nPoint = null;
        ucnecgui.models.Point mPoint = null;
        
        ArrayList<Line> lines = new ArrayList<Line>();
        for (Source source : sources) {
            int wireNumber = source.getSourceSeg();
            if (wireNumber == global.getCurrentSourceTag()) {
                continue;
            }
            Wire nWire = null;
            if (wireNumber > 0) {
                nWire = global.getgWires().get(wireNumber - 1);
            } else {
                nWire = global.getgWires().get(wireNumber);
            }
            double percentage = global.calculatePercentaje(Integer.valueOf(source.getSegPercentage() + ""), source.getSourceSeg());
            if (percentage > 0 && percentage < 100) {
                nPoint = nWire.getLocation((percentage / 100.00) + 0.1);
                mPoint = nWire.getLocation((percentage / 100.00) - 0.1);
            } else if (percentage == 0) {
                nPoint = nWire.getLocation(0);
                mPoint = nWire.getLocation(0.2);
            } else if (percentage == 100) {
                nPoint = nWire.getLocation(0.98);
                mPoint = nWire.getLocation(1);
            }
            lines.add(new Line(new ucnecgui.models.Point(nPoint.getA(), nPoint.getB(), nPoint.getC()), new ucnecgui.models.Point(mPoint.getA(), mPoint.getB(), mPoint.getC())));
        }

        for (Line line : lines) {
            try {
                lineStrip = new CroppableLineStrip();
                lineStrip.setWireframeColor(Color.GREEN);
                lineStrip.add(new Point(new Coord3d((float) line.getX1(), (float) line.getY1(), (float) line.getZ1())));
                lineStrip.add(new Point(new Coord3d((float) line.getX2(), (float) line.getY2(), (float) line.getZ2())));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                String malformedCoordinateError = "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted";
                JOptionPane.showMessageDialog(null, malformedCoordinateError, "Incorrect Format", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(-1);
            }
            lineStrips.add(lineStrip);
            lineNumber++;
        }

        return lineStrips;
    }

     /**
     *Genera la representación de  todos los elementos que constituyen las redes empleadas en la simulación
     * @param networks Arreglo de objetos Network, correspondientes a las redes empleadas en la simulación
     * @param global Objeto de la clase Global
     * @return Lista de objetos CroppableLineStrip con la representación de las redes empleadas en la simulación
     */
    public List<CroppableLineStrip> graphNetwork(ArrayList<Network> networks, Global global) {

        int lineNumber = 0;
       CroppableLineStrip lineStrip = null;
        ucnecgui.models.Point nPoint = null;
        ucnecgui.models.Point mPoint = null;
        ArrayList<Line> lines = new ArrayList<Line>();
        for (Network network : networks) {
            int wireNumber = network.getTagNumberPort2();
            Wire nWire = null;
            if (wireNumber > 0) {
                nWire = global.getgWires().get(wireNumber - 1);
            } else {
                nWire = global.getgWires().get(wireNumber);
            }
            double percentage = global.calculatePercentaje(Integer.valueOf(network.getPort2Segment() + ""), wireNumber);
            if (percentage > 0 && percentage < 100) {
                nPoint = nWire.getLocation((percentage / 100.00) + 0.1);
                mPoint = nWire.getLocation((percentage / 100.00) - 0.1);
            } else if (percentage == 0) {
                nPoint = nWire.getLocation(0);
                mPoint = nWire.getLocation(0.2);
            } else if (percentage == 100) {
                nPoint = nWire.getLocation(0.98);
                mPoint = nWire.getLocation(1);
            }
            lines.add(new Line(new ucnecgui.models.Point(nPoint.getA(), nPoint.getB(), nPoint.getC()), new ucnecgui.models.Point(mPoint.getA(), mPoint.getB(), mPoint.getC())));
        }

        for (Line line : lines) {
            try {
                lineStrip = new CroppableLineStrip();
                lineStrip.setWireframeColor(Color.YELLOW);
                lineStrip.add(new Point(new Coord3d((float) line.getX1(), (float) line.getY1(), (float) line.getZ1())));
                lineStrip.add(new Point(new Coord3d((float) line.getX2(), (float) line.getY2(), (float) line.getZ2())));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                String malformedCoordinateError = "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted";
                JOptionPane.showMessageDialog(null, malformedCoordinateError, "Incorrect Format", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(-1);
            }
            lineStrips.add(lineStrip);
            lineNumber++;
        }

        return lineStrips;
    }

       /**
     *Genera la representación de  todos los elementos que constituyen las cargas empleadas en la simulación
     * @param loads Arreglo de objetos Load, correspondientes a las cargas empleadas en la simulación
     * @param global Objeto de la clase Global
     * @return Lista de objetos CroppableLineStrip con la representación de las cargas empleadas en la simulación
     */
    public List<CroppableLineStrip> graphLoad(ArrayList<Load> loads, Global global) {

        int lineNumber = 0;
        CroppableLineStrip lineStrip = null;
        ucnecgui.models.Point nPoint = null;
        ucnecgui.models.Point mPoint = null;

        ArrayList<Line> lines = new ArrayList<Line>();
        for (Load load : loads) {
            int wireNumber = load.getLoadSeg();
            Wire nWire = global.getgWires().get(wireNumber - 1);
            double percentage = global.calculatePercentaje(Integer.valueOf(load.getLoadPercentage() + ""), wireNumber);
            if (percentage > 0 && percentage < 100) {
                nPoint = nWire.getLocation((percentage / 100.00) + 0.1);
                mPoint = nWire.getLocation((percentage / 100.00) - 0.1);
            } else if (percentage == 0) {
                nPoint = nWire.getLocation(0);
                mPoint = nWire.getLocation(0.2);
            } else if (percentage == 100) {
                nPoint = nWire.getLocation(0.98);
                mPoint = nWire.getLocation(1);
            }
            lines.add(new Line(new ucnecgui.models.Point(nPoint.getA(), nPoint.getB(), nPoint.getC()), new ucnecgui.models.Point(mPoint.getA(), mPoint.getB(), mPoint.getC())));
        }

        for (Line line : lines) {
            try {
                lineStrip = new CroppableLineStrip();
                lineStrip.setWireframeColor(Color.CYAN);
                lineStrip.add(new Point(new Coord3d((float) line.getX1(), (float) line.getY1(), (float) line.getZ1())));
                lineStrip.add(new Point(new Coord3d((float) line.getX2(), (float) line.getY2(), (float) line.getZ2())));
                lineStrips.add(lineStrip);

            } catch (NumberFormatException e) {
                String malformedCoordinateError = "Error on line: " + lineNumber + newline + "Coordinate is incorrectly formatted";
                JOptionPane.showMessageDialog(null, malformedCoordinateError, "Incorrect Format", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                System.exit(-1);
            }
            lineStrips.add(lineStrip);
            lineNumber++;
        }

        return lineStrips;
    }

    /**
     *Determina si el alambre de índice nWire está indicado como seleccionado
     * @param selected Índice del alambre seleccionado
     * @param nWire Índice del alambre bajo evaluación
     * @return true si es alambre de índice nWire está seleccionado, en caso contrario, false
     */
    public boolean isSelectedWire(int selected, int nWire) {
        if (selected != -1) {
            if (selected == nWire) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
