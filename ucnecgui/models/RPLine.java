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

/**
 *
 * @author Leoncio Gómez
 */
public class RPLine {

    private double theta;
    private double phi;
    private double eThetaMag;
    private double eThetaDeg;
    private double ePhiMag;
    private double ePhiDeg;

    /**
     * Constructor de la clase RPLine
     */
    public RPLine() {
        this.theta = 0.0;
        this.phi = 0.0;
        this.eThetaMag = 0.0;
        this.eThetaDeg = 0.0;
        this.ePhiMag = 0.0;
        this.ePhiDeg = 0.0;
    }

    /**
     * Constructor de la clase RPLine. Genera un RPLine a partir de un vector de
     * String generalmente se trata del texto obtenido en el archivo de salida
     * del script nec2++
     *
     * @param necLine Texto obtenido en el archivo de salida del script nec2++
     */
    public RPLine(String[] necLine) {
        this.theta = Double.valueOf(necLine[0]);
        this.phi = Double.valueOf(necLine[1]);
        this.eThetaMag = Double.valueOf(necLine[8]);
        this.eThetaDeg = Double.valueOf(necLine[9]);
        this.ePhiMag = Double.valueOf(necLine[10]);
        this.ePhiDeg = Double.valueOf(necLine[11]);
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
     * @return the eThetaMag
     */
    public double geteThetaMag() {
        return eThetaMag;
    }

    /**
     * @param eThetaMag the eThetaMag to set
     */
    public void seteThetaMag(double eThetaMag) {
        this.eThetaMag = eThetaMag;
    }

    /**
     * @return the eThetaDeg
     */
    public double geteThetaDeg() {
        return eThetaDeg;
    }

    /**
     * @param eThetaDeg the eThetaDeg to set
     */
    public void seteThetaDeg(double eThetaDeg) {
        this.eThetaDeg = eThetaDeg;
    }

    /**
     * @return the ePhiMag
     */
    public double getePhiMag() {
        return ePhiMag;
    }

    /**
     * @param ePhiMag the ePhiMag to set
     */
    public void setePhiMag(double ePhiMag) {
        this.ePhiMag = ePhiMag;
    }

    /**
     * @return the ePhiDeg
     */
    public double getePhiDeg() {
        return ePhiDeg;
    }

    /**
     * @param ePhiDeg the ePhiDeg to set
     */
    public void setePhiDeg(double ePhiDeg) {
        this.ePhiDeg = ePhiDeg;
    }

    /**
     * Convierte el objeto RPLine a una representación String
     *
     * @return Representación String del objeto RPLine
     */
    @Override
    public String toString() {
        return getTheta() + ";" + getPhi() + ";" + Math.hypot(geteThetaMag(), getePhiMag());
    }
}
