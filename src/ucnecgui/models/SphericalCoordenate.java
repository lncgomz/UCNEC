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
public class SphericalCoordenate {

    private double theta;
    private double phi;
    private double rho;
    private int colorLevel;

    /**
     * Constructor de la clase SphericalCoordenate
     */
    public SphericalCoordenate() {
        this.theta = 0.0;
        this.phi = 0.0;
        this.rho = 0.0;

    }

    /**
     *     *Constructor de la clase SphericalCoordenate a partir del radio, ángulo
     * de elevación y ángulo azimutal
     *
     * @param theta Ángulo de Elevación (Grados)
     * @param phi Ángulo Azimutal (Grados)
     * @param rho Radio
     */
    public SphericalCoordenate(Double theta, Double phi, Double rho) {
        this.theta = theta;
        this.phi = phi;
        this.rho = rho;

    }

    public String toString() {
        return String.valueOf(getTheta()).replace(".", ",") + "; " + String.valueOf(getPhi()).replace(".", ",") + ";" + String.valueOf(getRho()).replace(".", ",");
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public double getPhi() {
        return phi;
    }

    public void setPhi(double phi) {
        this.phi = phi;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    public int getColorLevel() {
        return colorLevel;
    }

    public void setColorLevel(int c) {
        this.colorLevel = c;
    }
}
