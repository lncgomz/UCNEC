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
package ucnecgui.models;

import java.text.DecimalFormat;

/**
 *
 * @author Leoncio Gómez
 */
public class RadiationPattern {

    private double theta;
    private double phi;
    private int xnda;
    private double inTheta;
    private double inPhi;
    private double incTheta;
    private double incPhi;
    private double inputInAngle;
    private boolean setted;

    /**
     * Constructor de la clase RadiationPattern
     */
    public RadiationPattern() {
        theta = 0;
        phi = 0;
        xnda = 0000;
        inTheta = 0.0;
        inPhi = 0.0;
        incTheta = 0;
        incPhi = 0;
        inputInAngle = 0.0;
        setted = false;
    }

    /**
     * Constructor de la clase RadiationPattern. Genera un RadiationPattern a
     * partir de un vector de String generalmente se trata del texto obtenido en
     * el archivo de salida del script nec2++
     *
     * @param row Texto obtenido en el archivo de salida del script nec2++
     */
    public RadiationPattern(String[] row) {
        theta = Integer.valueOf(row[1]);
        phi = Integer.valueOf(row[2]);
        xnda = Integer.valueOf(row[3]);
        inTheta = Double.valueOf(row[4]);
        inPhi = Double.valueOf(row[5]);
        incTheta = Double.valueOf(row[6]);
        incPhi = Double.valueOf(row[7]);
    }

    /**
     * Genera un comando de patrón de radiación de NEC2 con valores triviales.
     *
     * @return Comando de patrón de radiación con valores triviales
     */
    public static String trivialRP() {
        return "RP 0,2,2,1001,0.0,0.0,2.0,2.0,0";
    }

    /**
     *Convierte el objeto RadiationPattern a un comando interpretable por NEC
     * @param radiationPattern Objeto RadiationPattern a convertir
     * @param is3DPlot Se trata de una gráfica tridimensional
     * @return Comando de patrón de radiación de NEC
     */
    public static String toString(RadiationPattern radiationPattern, boolean is3DPlot) {
        String nRP = "";
        DecimalFormat df = new DecimalFormat("#");
        if (is3DPlot) {
            nRP = "RP 0,91,181,1001,0.0,0.0," + radiationPattern.getIncTheta() + "," + radiationPattern.getIncPhi() + ",0";
        } else {
            nRP = "RP "
                    + "0" + ","
                    + df.format(radiationPattern.getTheta()) + ","
                    + df.format(radiationPattern.getPhi()) + ","
                    + radiationPattern.getXnda() + ","
                    + radiationPattern.getInTheta() + ","
                    + radiationPattern.getInPhi() + ","
                    + radiationPattern.getIncTheta() + ","
                    + radiationPattern.getIncPhi() + ","
                    + "0";

        }
        return nRP;
    }

    /**
     *Convierte el comando de patrón de radiación de NEC a un objeto RadiationPattern
     * @param line Comando de patrón de radiación de NEC
     * @return Objeto RadiationPattern 
     */
    public static RadiationPattern fromString(String line) {
        RadiationPattern resp = new RadiationPattern();
        String[] ln = line.split(",");
        resp.setTheta(Double.valueOf(ln[1]));
        resp.setPhi(Double.valueOf(ln[2]));
        resp.setXnda(Integer.valueOf(ln[3]));
        resp.setInTheta(Double.valueOf(ln[4]));
        resp.setInPhi(Double.valueOf(ln[5]));
        resp.setIncTheta(Double.valueOf(ln[6]));
        resp.setIncPhi(Double.valueOf(ln[7]));
        resp.setSetted(true);
        return resp;
    }

    /**
     * @return the theta
     */
    public double getTheta() {
        return theta;
    }

    /**
     * @param theta the theta to set
     */
    public void setTheta(double theta) {
        this.theta = theta;
    }

    /**
     * @return the phi
     */
    public double getPhi() {
        return phi;
    }

    /**
     * @param phi the phi to set
     */
    public void setPhi(double phi) {
        this.phi = phi;
    }

    /**
     * @return the xnda
     */
    public int getXnda() {
        return xnda;
    }

    /**
     * @param xnda the xnda to set
     */
    public void setXnda(int xnda) {
        this.xnda = xnda;
    }

    /**
     * @return the inTheta
     */
    public double getInTheta() {
        return inTheta;
    }

    /**
     * @param inTheta the inTheta to set
     */
    public void setInTheta(double inTheta) {
        this.inTheta = inTheta;
    }

    /**
     * @return the inPhi
     */
    public double getInPhi() {
        return inPhi;
    }

    /**
     * @param inPhi the inPhi to set
     */
    public void setInPhi(double inPhi) {
        this.inPhi = inPhi;
    }

    /**
     * @return the incTheta
     */
    public double getIncTheta() {
        return incTheta;
    }

    /**
     * @param incTheta the incTheta to set
     */
    public void setIncTheta(double incTheta) {
        this.incTheta = incTheta;
    }

    /**
     * @return the incPhi
     */
    public double getIncPhi() {
        return incPhi;
    }

    /**
     * @param incPhi the incPhi to set
     */
    public void setIncPhi(double incPhi) {
        this.incPhi = incPhi;
    }

    /**
     * @return the setted
     */
    public boolean isSetted() {
        return setted;
    }

    /**
     * @param setted the setted to set
     */
    public void setSetted(boolean setted) {
        this.setted = setted;
    }

    /**
     * @return the inputInAngle
     */
    public double getInputInAngle() {
        return inputInAngle;
    }

    /**
     * @param inputInAngle the inputInAngle to set
     */
    public void setInputInAngle(double inputInAngle) {
        this.inputInAngle = inputInAngle;
    }
}
