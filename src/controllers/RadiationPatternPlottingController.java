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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import ucnecgui.Global;
import ucnecgui.models.Lab2_1;
import ucnecgui.models.PointColor;
import ucnecgui.models.PolarCoordenate;
import ucnecgui.models.RPLine;
import ucnecgui.models.SphericalCoordenate;

/**
 *
 * @author Leoncio Gómez
 */
public class RadiationPatternPlottingController {

    /**
     *Controla la obtención  y graficación del patrón de radiación a partir de los datos generados, por el script nec2++, en el archivo  salida
     * y convertidos en un arreglo de objetos RPLine, por cada uno de los ángulos de paso, en azimuth o elevación, según el caso.
     * @param outputData Colección de datos, arreglo de objetos RPLine, 
     * @param global Objeto de la clase Global
     */
    public static void RPPlot(HashMap<String, ArrayList<RPLine>> outputData, Global global) {
        ArrayList<RPLine> outputDataLines = new ArrayList<RPLine>();
        ArrayList<RPLine> outputMetadataLines = new ArrayList<RPLine>();
        LinkedHashMap<Double, ArrayList<SphericalCoordenate>> slices = null;
        JZY3DRPPlotter plotter = new JZY3DRPPlotter(global);
        double max = 0.0;
        switch (global.getCurrentPlotType()) {
            case Global.PLOT3D: //Gráfica 3D
                outputDataLines = outputData.get("PLOT3D"); //Se seleccionan  los datos de la simulación 3D
                slices = getSlices(outputDataLines, global); // Por cada ángulo theta, se obtiene un arreglo de objetos SphericalCoordenate correspondientes a Etotal
                max = getMax(outputDataLines); //Se calcula el |ETotal| máximo
                global.setMaxGainPosition(getMaxValue(slices, global)); //A partir de |ETotal| máximo se determina la posición de máxima radiación 
                global.setMaxGainValue(global.getGainAt(getMaxValue(slices, global))); //Se calcula el valor de ganancia máxima 
                plotter = new JZY3DRPPlotter(global); //Se instancia un objeto de la clase JZY3DRPPlotter para empezar el proceso de graficación
                plotter.plot(toRadarChart(slices, max, global)); //Se grafica, finalmente, el patrón de radiación
                break;
            case Global.AZIMUTHPLOT: //Gráfica Azimuth
                outputDataLines = outputData.get("PLOT2D");  //Se seleccionan  los datos de la simulación 2D
                outputMetadataLines = outputData.get("PLOT3D");  //Se seleccionan  los datos de la simulación 3D para los cálculos de los parámetros auxiliares y de normalización
                slices = getSlices(outputDataLines, global); // Por cada ángulo theta, se obtiene un arreglo de objetos SphericalCoordenate correspondientes a Etotal
                max = getMax(outputDataLines);
                getAperture(slices, max, global); //SE calcula el valor de la apertura de haz, relación F/B y relación F/S
                plotter = new JZY3DRPPlotter(global);
                plotter.plot(toRadarChart(slices, max, global));
                break;
            case Global.ELEVATIONPLOT: //Gráfica Elevación
                outputDataLines = outputData.get("PLOT2D");
                outputMetadataLines = outputData.get("PLOT3D");
                slices = getSlices(outputDataLines, global); // Por cada ángulo phi, se obtiene un arreglo de objetos SphericalCoordenate correspondientes a Etotal
                max = getMax(outputDataLines);
                getAperture(slices, max, global);
                plotter = new JZY3DRPPlotter(global);
                plotter.plot(toRadarChart(slices, max, global));
                break;
            default:
                throw new AssertionError();
        }
    }

    public static void Metadata(HashMap<String, ArrayList<RPLine>> outputData, Global global) {
        ArrayList<RPLine> outputMetadataLines = outputData.get("PLOT3D");
    }

    /**
     *Por cada ángulo de iteración (Theta o Phi, según el caso), se obtiene un arreglo de objetos SphericalCoordenate correspondientes a Etotal con valor Theta o Phi constante.
     * @param RPLines Arreglo de objetos de la clase SphericalCoordenate para un valor Theta o Phi dado
     * @param global Objeto de la clase Global
     * @return Colección de Objetos cuya clave es el ángulo de iteración y su valor es un arreglo de objetos del tipo SphericalCoordenate con los valores de Etotal para un ángulo de iteración constante
     */
    public static LinkedHashMap<Double, ArrayList<SphericalCoordenate>> getSlices(ArrayList<RPLine> RPLines, Global global) {
        LinkedHashMap<Double, ArrayList<SphericalCoordenate>> slice = new LinkedHashMap<Double, ArrayList<SphericalCoordenate>>();
        for (RPLine nRPLine : RPLines) {
            switch (global.getCurrentPlotType()) {
                case Global.AZIMUTHPLOT: //Gráfica Azimuth
                    if (slice.containsKey(nRPLine.getTheta())) {
                        slice.get(nRPLine.getTheta()).add(new SphericalCoordenate(nRPLine.getTheta(), nRPLine.getPhi(), Math.hypot(nRPLine.geteThetaMag(), nRPLine.getePhiMag())));
                    } else {
                        ArrayList<SphericalCoordenate> nSlice = new ArrayList<SphericalCoordenate>();
                        nSlice.add(new SphericalCoordenate(nRPLine.getTheta(), nRPLine.getPhi(), Math.hypot(nRPLine.geteThetaMag(), nRPLine.getePhiMag())));
                        slice.put(nRPLine.getTheta(), nSlice);
                    }
                    break;
                case Global.PLOT3D: //Gráfica Tridimensional
                    if (slice.containsKey(nRPLine.getTheta())) {
                        slice.get(nRPLine.getTheta()).add(new SphericalCoordenate(nRPLine.getTheta(), nRPLine.getPhi(), Math.hypot(nRPLine.geteThetaMag(), nRPLine.getePhiMag())));
                    } else {
                        ArrayList<SphericalCoordenate> nSlice = new ArrayList<SphericalCoordenate>();
                        nSlice.add(new SphericalCoordenate(nRPLine.getTheta(), nRPLine.getPhi(), Math.hypot(nRPLine.geteThetaMag(), nRPLine.getePhiMag())));
                        slice.put(nRPLine.getTheta(), nSlice);
                    }
                    break;
                case Global.ELEVATIONPLOT: //Grafica de Elevación
                    if (slice.containsKey(nRPLine.getPhi())) {
                        slice.get(nRPLine.getPhi()).add(new SphericalCoordenate(nRPLine.getTheta(), nRPLine.getPhi(), Math.hypot(nRPLine.geteThetaMag(), nRPLine.getePhiMag())));
                    } else {
                        ArrayList<SphericalCoordenate> nSlice = new ArrayList<SphericalCoordenate>();
                        nSlice.add(new SphericalCoordenate(nRPLine.getTheta(), nRPLine.getPhi(), Math.hypot(nRPLine.geteThetaMag(), nRPLine.getePhiMag())));
                        slice.put(nRPLine.getPhi(), nSlice);
                    }
                    
                    break;
                default:
                    throw new AssertionError();
            }
        }
        return slice;
    }

    /**
     * Convierte los Objetos SphericalCoordenate a Objetos PointColor que pueden ser interpretados por el API de graficación
     * @param slices Colección de Objetos del tipo SphericalCoordenate con los valores de Etotal para ángulos de interación constante
     * @param max |Etotal| máximo
     * @param global Objeto de la clase Global
     * @return Colección de Objetos cuya clave son los ángulos de iteración y sus valores son arreglos de objetos PointColor para un ángulo de iteración constante
     */
    public static LinkedHashMap<Double, ArrayList<PointColor>> toRadarChart(LinkedHashMap<Double, ArrayList<SphericalCoordenate>> slices, double max, Global global) {

        LinkedHashMap<Double, ArrayList<PointColor>> resp = new LinkedHashMap<Double, ArrayList<PointColor>>();
        for (Map.Entry<Double, ArrayList<SphericalCoordenate>> entry : slices.entrySet()) {

            ArrayList<SphericalCoordenate> nSlice = entry.getValue();

            ArrayList<PolarCoordenate> polar = new ArrayList<PolarCoordenate>();
            double angle = 0.0;
            
            for (SphericalCoordenate slice : nSlice) {
                switch (global.getCurrentPlotType()) {
                    case Global.AZIMUTHPLOT: //Gráfica de Azimuth
                        if (slice.getPhi() >= 0) {
                            angle = slice.getPhi();
                        } else {
                            angle = slice.getPhi() + 360;
                        }
                        break;
                    case Global.PLOT3D: //Gráfica Tridimensional
                        if (slice.getPhi() > 0) {
                            angle = slice.getPhi();
                        } else {
                            angle = slice.getPhi() + 360;
                        }
                        break;
                    case Global.ELEVATIONPLOT: //Gráfica de Elevación                    
                        if (slice.getTheta() > 0) {
                            angle = slice.getTheta() - 90;
                        } else {
                            angle = (slice.getTheta() + 360) - 90;
                        }
                        break;
                    default:
                        throw new AssertionError();
                }
                polar.add(new PolarCoordenate(slice, angle, max, global));
            }

            ArrayList<PointColor> rectangularCoord = new ArrayList<PointColor>();
            for (PolarCoordenate p : polar) {
                rectangularCoord.add(p.toRectangular());
            }

            resp.put(entry.getKey(), rectangularCoord);
        }
        return resp;
    }

   /**
     * Convierte los Objetos Lab2_1 a Objetos PointColor que pueden ser interpretados por el API de graficación.
     * Éste método es empleado para la graficación del diagrama de polarización
     * @param samples Colección de Objetos del tipo Lab2_1 con los valores de Etotal para ángulos de interación constante
     * @param global Objeto de la clase Global
     * @return Arreglo de Objetos de la clase PointColor con los valores de Etoral para un ángulo de iteración constante
     */
    public static ArrayList<PointColor> toRadarChart(ArrayList<Lab2_1> samples, Global global) {

        ArrayList<PointColor> resp = new ArrayList<PointColor>();
        ArrayList<PolarCoordenate> polar = new ArrayList<PolarCoordenate>();
        double angle = 0.0;
        double step = 0.0;
        for (Lab2_1 n : samples) {
            if (n.getAngle() >= 0) {
                angle = n.getAngle();
            } else {
                angle = n.getAngle() + 360;
            }
            polar.add(new PolarCoordenate(n.getV().abs(), n.getAngle()));
        }

        for (PolarCoordenate p : polar) {
            resp.add(p.toRectangular());
        }
        return resp;
    }

    /**
     * Genera un arreglo de objetos PointColor, interpretables por el API de graficación, que representan a un círculo de radio ratio, éste método es empleado para la generación de las gráfica polares.
     * @param ratio Radio del círculo
     * @return Arreglo de objetos PointColor que representan a un círculo de radio ratio
     */
    public static ArrayList<PointColor> generateCircle(double ratio) {
        ArrayList<PointColor> resp = new ArrayList<PointColor>();
        for (int i = 0; i < 359; i = i + 2) {
            PolarCoordenate p = new PolarCoordenate(ratio, i, 0);
            resp.add(p.toRectangular());
        }
        return resp;
    }

    /**
     * Genera un arreglo de objetos PointColor, interpretables por el API de graficación, que representan radios unitarios espaciados cada 45 grados desde el origen de coordenadas, 
     * éste método es empleado para la generación de las gráfica polares.     
     * @return Arreglo de objetos PointColor  que representan radios unitarios espaciados cada 45 grados desde el origen de coordenadas, 
     */
    public static ArrayList<PointColor> generateRadius() {
        ArrayList<PointColor> resp = new ArrayList<PointColor>();
        double step = 45;
        for (int i = 0; i < 9; i++) {
            PolarCoordenate p = new PolarCoordenate(1, step * i, 0);
            resp.add(p.toRectangular());
        }
        return resp;
    }

    /**
     *Calcula el valor de |Etotal| máximo dado un arreglo de objetos de tipo RPLines
     * @param RPLines Valores de Etotal para un ángulo de iteración constante
     * @return Valor de |Etotal| máximo
     */
    public static double getMax(ArrayList<RPLine> RPLines) {
        boolean first = true;
        double max = 0.0;
        for (RPLine slice : RPLines) {
            double rho = Math.hypot(slice.geteThetaMag(), slice.getePhiMag());
            if (first) {
                max = rho;

                first = false;
            } else if (rho > max) {
                max = rho;
            }
        }
        return max;
    }

    /**
     *Calcula el valor de la apertura de haz, lóbulo trasero y lóbulo laberal
     * @param slices Colección de objetos cuya clave es un  de ángulo de iteración y su valor es un arreglo de objetos SphericalCoordenate que representan a Etotal para un
     * ángulo de iteración constante.
     * @param max |Etotal| máximo
     * @param global Objeto de la clase Global
     */
    public static void getAperture(LinkedHashMap<Double, ArrayList<SphericalCoordenate>> slices, double max, Global global) {

        ArrayList<SphericalCoordenate> nSlice = new ArrayList<SphericalCoordenate>();
        global.getgMax().clear();
        global.getgMaxValue().clear();
        global.getgAper().clear();
        global.getgAperture().clear();
        global.setgSide(null);
        global.setgSideValue(null);
        global.setgBack(null);

        for (Map.Entry<Double, ArrayList<SphericalCoordenate>> entry : slices.entrySet()) {
            nSlice = entry.getValue();
            SphericalCoordenate maxValue = getMaxValue(nSlice, max, global); //Obtención del objeto SphericalCoordenate correspondiente al máximo Etotal
            getBackValue(nSlice, maxValue, max, global); //Obtención del lóbulo trasero
            ArrayList<Double> spans = new ArrayList<Double>();
            ArrayList<Double> auxSpans = new ArrayList<Double>();
            for (SphericalCoordenate nMaxValue : global.getgMax()) {
                auxSpans = searchAll(nSlice, nMaxValue, max, global);  //Calcula el valor del span del lóbulo, siendo este aquel comprendido entre +/- 1/sqrt(2) veces el valor de |Etotal| máximo
                for (Double auxSpan : auxSpans) {
                    spans.add(auxSpan);
                }
            }
            if (isIncluded(0, spans)) {
                spans.add(360.0);
            } else if (isIncluded(360, spans)) {
                spans.add(0.0);
            }
            searchSideLobe(nSlice, maxValue, max, spans, global); //Obtención del lóbulo secundario
        }
    }

    /**
     *Obtención del objeto SphericalCoordenate correspondiente al máximo Etotal a partir de un arreglo de objetos del tipo SphericalCoordenate correspondiente a los valores
     * de Etotal para un ángulo de iteración constante y |Etotal| máximo
     * @param slice Arreglo de objetos del tipo SphericalCoordenate correspondiente a los valores de Etotal para un ángulo de iteración constante
     * @param max |Etotal| máximo
     * @param global Objeto de la clase Global
     * @return SphericalCoordenate correspondiente al máximo Etotal
     */
    public static SphericalCoordenate getMaxValue(ArrayList<SphericalCoordenate> slice, double max, Global global) {
        double maxVal = 0.0;
        double rho = 0.0;
        ArrayList<SphericalCoordenate> values = new ArrayList<SphericalCoordenate>();
        ArrayList<PointColor> resp = new ArrayList<PointColor>();
        SphericalCoordenate aux = null;

        boolean first = true;
        for (SphericalCoordenate nSlice : slice) {
            rho = nSlice.getRho();
            if (first) {
                maxVal = rho;
                first = false;
            } else if (rho >= maxVal) {
                maxVal = rho;
            }
        }
        for (SphericalCoordenate nSlice : slice) {
            if (nSlice.getRho() == maxVal) {
                values.add(nSlice);
                aux = nSlice;
            }
        }

        for (SphericalCoordenate nValue : values) {
            PolarCoordenate p = null;
            switch (global.getCurrentPlotType()) {
                case Global.AZIMUTHPLOT: //Gráfica Azimuth
                    p = new PolarCoordenate(1, nValue.getPhi(), 0);
                    resp.add(p.toRectangular());

                    break;
                case Global.ELEVATIONPLOT: //Gráfica Elevación
                    p = new PolarCoordenate(1, nValue.getTheta(), 0);
                    resp.add(p.toRectangularRotated(-90));
                    break;
                default:
                    throw new AssertionError();
            }
            global.setgMaxValue(resp);
            global.setgMax(values);
        }
        return values.get(0);
    }

    /**
     *Obtención del objeto SphericalCoordenate correspondiente al máximo Etotal a partir de una colección de objetos cuya clave es un ángulo de iteración y su valor es un arreglo de objetos SphericalCoordenate 
     * correspondientes a los valores de Etotal para dicho ángulo de iteración. Es un ejemplo de polimorfismo del método anterior.
     * de Etotal para un ángulo de iteración constante y |Etotal| máximo
     * @param slice Colección de objetos cuya clave es un ángulo de iteración y su valor es un arreglo de objetos SphericalCoordenate 
     * correspondientes a los valores de Etotal para dicho ángulo de iteración     
     * @param global Objeto de la clase Global
     * @return SphericalCoordenate correspondiente al máximo Etotal
     */
    public static SphericalCoordenate getMaxValue(LinkedHashMap<Double, ArrayList<SphericalCoordenate>> slice, Global global) {
        double maxVal = 0.0;
        double rho = 0.0;

        ArrayList<PointColor> resp = new ArrayList<PointColor>();
        ArrayList<SphericalCoordenate> values = new ArrayList<SphericalCoordenate>();
        SphericalCoordenate aux = null;

        boolean first = true;
        for (Map.Entry<Double, ArrayList<SphericalCoordenate>> entry : slice.entrySet()) {
            ArrayList<SphericalCoordenate> value = entry.getValue();
            for (SphericalCoordenate sC : value) {
                rho = sC.getRho();
                if (first) {
                    maxVal = rho;
                    first = false;
                } else if (rho >= maxVal) {
                    maxVal = rho;
                }
            }
        }

        for (Map.Entry<Double, ArrayList<SphericalCoordenate>> entry : slice.entrySet()) {
            ArrayList<SphericalCoordenate> value = entry.getValue();
            for (SphericalCoordenate sC : value) {
                if (sC.getRho() == maxVal) {
                    values.add(sC);
                    aux = sC;
                }
            }
        }

        for (SphericalCoordenate nValue : values) {
            PolarCoordenate p = null;
            switch (global.getCurrentPlotType()) {
                case Global.AZIMUTHPLOT:
                    p = new PolarCoordenate(1, nValue.getPhi(), 0);
                    resp.add(p.toRectangular());
                    break;
                case Global.ELEVATIONPLOT:
                    p = new PolarCoordenate(1, nValue.getTheta(), 0);
                    resp.add(p.toRectangularRotated(-90));
                    break;
                case Global.PLOT3D:
                    break;
                default:
                    throw new AssertionError();
            }
            if (global.getCurrentPlotType() != Global.PLOT3D) {
                global.setgMaxValue(resp);
            }
            global.setgMax(values);
        }
        return values.get(0);
    }

    /**
     *Obtención del lóbulo trasero a partir de un arreglo de objetos SphericalCoordenate
     * @param slice Arreglo de objetos SphericalCoordenate correspondientes a los valores de Etotal para un ángulo de iteración constante
     * @param maxValue Objeto SphericalCoordenate correspondiente a Etotal máximo
     * @param max |Etotal| máximo
     * @param global Objeto de la clase Global
     */
    public static void getBackValue(ArrayList<SphericalCoordenate> slice, SphericalCoordenate maxValue, double max, Global global) {
        double backAngle = 0;
        SphericalCoordenate resp = null;
        switch (global.getCurrentPlotType()) {
            case Global.AZIMUTHPLOT:
                backAngle = maxValue.getPhi() + 180;
                if (backAngle > 360) {
                    backAngle = backAngle - 360;
                }
                break;
            case Global.ELEVATIONPLOT:
                backAngle = maxValue.getTheta() + 180;
                if (backAngle > 360) {
                    backAngle = backAngle - 360;
                }
                break;
            default:
                throw new AssertionError();
        }
        //Se busca el valor de Etotal para un ángulo tal que corresponda al del Etotal máximo + 180 grados,
        //de no encontrarse dicho valor por la resolución de la simulación, se busca el más cercano por arriba.
        
        boolean isPhi = global.getCurrentPlotType() == Global.AZIMUTHPLOT;
        boolean isTheta = global.getCurrentPlotType() == Global.ELEVATIONPLOT;
        int i = 0;
        resp = searchValueByAngle(slice, backAngle, isTheta, isPhi);
        while (resp == null) {
            i++;
            double nextBackAngle = backAngle + i;
            if (nextBackAngle > 360) {
                nextBackAngle = nextBackAngle - 360;
            }
            resp = searchValueByAngle(slice, nextBackAngle, isTheta, isPhi);
        }
        global.setgBack(resp);
    }

    /**
     *Obtención del lóbulo lateral a partir de un arreglo de objetos SphericalCoordenate
     * @param slice  Arreglo de objetos SphericalCoordenate correspondientes a los valores de Etotal para un ángulo de iteración constante
     * @param maxValue Objeto SphericalCoordenate correspondiente a Etotal máximo
     * @param max |Etotal| máximo
     * @param spans Ángulos pertenecientes al span del lóbulo principal
     * @param global Objeto de la clase Global
     * @return SphericalCoordenate correspondiente al lóbulo secundario
     */
    public static SphericalCoordenate searchSideLobe(ArrayList<SphericalCoordenate> slice, SphericalCoordenate maxValue, double max, ArrayList<Double> spans, Global global) {
        global.setgSide(null);
        global.setgSideValue(null);

        double maxVal = 0.0;
        double rho = 0.0;
        SphericalCoordenate aux = null;
        boolean first = true;
        for (SphericalCoordenate nSlice : slice) {
            switch (global.getCurrentPlotType()) {
                case Global.AZIMUTHPLOT:
                    if (isIncluded(nSlice.getPhi(), spans)) {
                        continue;
                    } else {
                        break;
                    }
                case Global.ELEVATIONPLOT:
                    if (isIncluded(nSlice.getTheta(), spans)) {
                        continue;
                    } else {
                        break;
                    }
                default:
                    throw new AssertionError();
            }
            rho = nSlice.getRho();
            if (first) {
                maxVal = rho;
                aux = new SphericalCoordenate(nSlice.getTheta(), nSlice.getPhi(), rho);
                first = false;
            } else if (rho > maxVal) {
                maxVal = rho;
                aux = new SphericalCoordenate(nSlice.getTheta(), nSlice.getPhi(), rho);
            }
        }
        if (aux != null) {
            PolarCoordenate p = null;
            switch (global.getCurrentPlotType()) {
                case Global.AZIMUTHPLOT:
                    p = new PolarCoordenate(aux.getRho() / max, aux.getPhi(), 0);
                    global.setgSideValue(p.toRectangular());
                    break;
                case Global.ELEVATIONPLOT:
                    p = new PolarCoordenate(aux.getRho() / max, aux.getTheta(), 0);
                    global.setgSideValue(p.toRectangularRotated(-90));
                    break;
                default:
                    throw new AssertionError();
            }
        } else {
            PolarCoordenate p = null;
            SphericalCoordenate aux0 = global.getgBack();
            switch (global.getCurrentPlotType()) {
                case Global.AZIMUTHPLOT:

                    p = new PolarCoordenate(aux0.getRho() / max, aux0.getPhi(), 0);
                    global.setgSideValue(p.toRectangular());
                    break;
                case Global.ELEVATIONPLOT:
                    p = new PolarCoordenate(aux0.getRho() / max, aux0.getTheta(), 0);
                    global.setgSideValue(p.toRectangularRotated(-90));
                    break;
                default:
                    throw new AssertionError();
            }
            aux = global.getgBack();
        }

        global.setgSide(aux);
        return aux;
    }

    /**
     * Determina si el valor de un ángulo está contenido en el span del lóbulo principal
     * @param value Ángulo bajo evaluación
     * @param listValue Span de ángulos pertenecientes al lóbulo principal
     * @return true si value se encuentra contenido en el span, en caso contrario, regresa false
     */
    public static boolean isIncluded(double value, ArrayList<Double> listValue) {
        boolean resp = false;
        for (Double nValue : listValue) {
            if (value == nValue) {
                resp = true;
                break;
            }
        }
        return resp;
    }

    /**
     *Gestiona la búsqueda del span del lóbulo principal de acuerdo al tipo de gráfica que se desee generar
     * @param slice  Arreglo de objetos SphericalCoordenate correspondientes a los valores de Etotal para un ángulo de iteración constante
     * @param maxValue Objeto SphericalCoordenate correspondiente a Etotal máximo
     * @param max |Etotal| máximo
     * @param global Objeto de la clase Global
     * @return Span alrededor del  lóbulo principal
     */
    public static ArrayList<Double> searchAll(ArrayList<SphericalCoordenate> slice, SphericalCoordenate maxValue, double max, Global global) {
        ArrayList<Double> span = null;
        switch (global.getCurrentPlotType()) {
            case Global.AZIMUTHPLOT:
                span = searchSpan(slice, maxValue, max, false, true, global);
                break;
            case Global.ELEVATIONPLOT:
                span = searchSpan(slice, maxValue, max, true, false, global);
                break;
            default:
                throw new AssertionError();

        }
        return span;
    }

    /**
     *Convierte un objeto de la clase SphericalCoordenate a uno de la clase PointColor, interpretable por el API de graficación
     * @param sC Objeto a ser convertido
     * @param max |Etotal| máximo
     * @param global Objeto de la clase Global
     * @return Objeto PointColor correspondiente al objeto SphericalCoordenate introducido como parámetro
     */
    public static PointColor toPointColor(SphericalCoordenate sC, double max, Global global) {
        PolarCoordenate p = null;
        switch (global.getCurrentPlotType()) {
            case Global.AZIMUTHPLOT:
                p = new PolarCoordenate(sC.getRho() / max, sC.getPhi());
                return p.toRectangular();
            case Global.ELEVATIONPLOT:
                p = new PolarCoordenate(sC.getRho() / max, sC.getTheta());
                return p.toRectangularRotated(-90);
            default:
                throw new AssertionError();
        }
    }

    /**
     *Busca un SphericalCoordenate, dentro de un arreglo de objetos de este clase, cuyo ángulo corresponde al proporcionado en el parámetro value, ya sea iterando en theta o en phi.
     * @param slice Arreglo de objetos SphericalCoordenate correspondientes a los valores de Etotal para un ángulo de iteración constante
     * @param value Ángulo a buscar
     * @param byTheta Ángulo de iteración es Theta
     * @param byPhi Ángulo de iteración es Phi
     * @return Objeto dentro del arreglo de objetos SphericalCoordenate cuyo ángulo de iteración es igual a value
     */
    public static SphericalCoordenate searchValueByAngle(ArrayList<SphericalCoordenate> slice, double value, boolean byTheta, boolean byPhi) {
        SphericalCoordenate aux = null;
        double val = 0;
        if (value < 0) {
            val = value + 360;
        } else {
            val = value;
        }
        if (byTheta) {
            for (SphericalCoordenate nPoint : slice) {
                if (nPoint.getTheta() == val) {
                    aux = nPoint;
                    break;
                }
            }

        } else if (byPhi) {
            for (SphericalCoordenate nPoint : slice) {
                if (nPoint.getPhi() == val) {
                    aux = nPoint;
                    break;
                }
            }
        }
        return aux;
    }

    /**
     * Busca el span alrededor de Etotal máximo, tanto ascendente como descendente, tal que los extremos tengan un valor igual 1/sqrt(2) veces el |Etotal| máximo
     * @param slice Arreglo de objetos SphericalCoordenate correspondientes a los valores de Etotal para un ángulo de iteración constante
     * @param maxValue Objeto SphericalCoordenate correspondiente a Etotal máximo
     * @param max |Etotal| máximo
     * @param byTheta Ángulo de iteración es Theta
     * @param byPhi Ángulo de iteración es Phi
     * @param global Objeto de la clase Global
     * @return Span alrededor del  lóbulo principal
     */
    public static ArrayList<Double> searchSpan(ArrayList<SphericalCoordenate> slice, SphericalCoordenate maxValue, double max, boolean byTheta, boolean byPhi, Global global) {
        ArrayList<Double> resp = new ArrayList<Double>();
        int maxQuantities = global.getgMax().size();
        int pointsNumber = slice.size();
        boolean firstAperture = true;
        boolean secondAperture = true;

        if (byTheta) {
            double start = maxValue.getTheta();
            double maxVal = maxValue.getRho();
            resp.add(start);
            for (int i = 1; i < 360; i++) {
                double angle = 0;
                if ((start + i) > 360) {
                    angle = (start + i) - 360;
                } else {
                    angle = start + i;
                }
                SphericalCoordenate nStep = searchValueByAngle(slice, start + i, true, false);
                if (nStep != null) {
                    resp.add(nStep.getTheta());
                    if (nStep.getRho() / maxValue.getRho() <= 1 / Math.sqrt(2) && firstAperture) {
                        if (maxQuantities < pointsNumber) {
                            global.getgAperture().add(toPointColor(nStep, max, global));
                            global.getgAper().add(nStep);
                        }
                        firstAperture = false;
                    }
                    if (nStep.getRho() / maxValue.getRho() <= 0.1) {
                        break;
                    }
                } else {
                    continue;
                }
            }

            for (int i = 1; i < 360; i++) {
                double angle = 0;
                if ((start + i) < 0) {
                    angle = (start - i) + 360;
                } else {
                    angle = start - i;
                }
                SphericalCoordenate nStep = searchValueByAngle(slice, angle, true, false);
                if (nStep != null) {
                    resp.add(nStep.getTheta());
                    if (nStep.getRho() / maxValue.getRho() <= 1 / Math.sqrt(2) && secondAperture) {
                        if (maxQuantities < pointsNumber) {
                            global.getgAperture().add(toPointColor(nStep, maxVal, global));
                            global.getgAper().add(nStep);
                        }
                        secondAperture = false;
                    }
                    if (nStep.getRho() / maxValue.getRho() <= 0.1) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        } else {

            double start = maxValue.getPhi();
            double maxVal = maxValue.getRho();
            resp.add(start);
            for (int i = 1; i < 360; i++) {
                double angle = 0;
                if ((start + i) > 360) {
                    angle = (start + i) - 360;
                } else {
                    angle = start + i;
                }
                SphericalCoordenate nStep = searchValueByAngle(slice, angle, false, true);
                if (nStep != null) {
                    resp.add(nStep.getPhi());
                    if (nStep.getRho() / maxValue.getRho() <= 1 / Math.sqrt(2) && firstAperture) {
                        if (maxQuantities < pointsNumber) {
                            global.getgAperture().add(toPointColor(nStep, maxVal, global));
                            global.getgAper().add(nStep);
                        }
                        firstAperture = false;
                    }
                    if (nStep.getRho() / maxValue.getRho() <= 0.1) {
                        break;
                    }
                } else {
                    continue;
                }
            }

            for (int i = 1; i < 360; i++) {
                double angle = 0;
                if ((start + i) < 0) {
                    angle = (start - i) + 360;
                } else {
                    angle = start - i;
                }
                SphericalCoordenate nStep = searchValueByAngle(slice, angle, false, true);
                if (nStep != null) {
                    resp.add(nStep.getPhi());
                    if (nStep.getRho() / maxValue.getRho() <= 1 / Math.sqrt(2) && secondAperture) {
                        if (maxQuantities < pointsNumber) {
                            global.getgAperture().add(toPointColor(nStep, maxVal, global));
                            global.getgAper().add(nStep);
                        }
                        secondAperture = false;
                    }
                    if (nStep.getRho() / maxValue.getRho() <= 0.1) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        return resp;
    }
}