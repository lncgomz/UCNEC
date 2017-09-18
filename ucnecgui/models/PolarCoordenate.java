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

import ucnecgui.Global;

/**
 *
 * @author Leoncio Gómez
 */
public class PolarCoordenate {

    private double rho;
    private double angle;
    private double z;
    private double r;
    private int colorLevel;
    private SphericalCoordenate ss;

    /**
     * Constructor de la clase PolarCoordenate
     */
    public PolarCoordenate() {
        this.rho = 0.0;
        this.angle = 0.0;
        this.z = 0.0;
        this.r = 0.0;
    }

    /**
     * Constructor de la clase PolarCoordenate a partir de los valores de radio
     * y ángulo
     *
     * @param rho Radio de la coordenada polar
     * @param angle Ángulo polar (Grados)
     */
    public PolarCoordenate(double rho, double angle) {
        this.rho = rho;
        this.angle = angle;
        this.z = 0;
        this.r = rho;
    }

    /**
     * Constructor de la clase PolarCoordenate a partir de los valores de radio,
     * ángulo y altura (Transformación a Cilíndrica)
     *
     * @param rho Radio de la coordenada polar
     * @param angle Ángulo polar (Grados)
     * @param z Altura cilíndrica
     */
    public PolarCoordenate(double rho, double angle, double z) {
        this.rho = rho;
        this.angle = angle;
        this.z = z;
        this.r = rho;
    }

    /**
     * Constructor de la clase PolarCoordenate a partir de un objeto
     * SphericalCoordenate, ángulo polar y |Etotal| máxima. Lleva a cabo la
     * conversión del objeto SphericalCoordenate a un formato consistente con la
     * graficación en un diagrama polar
     *
     * @param s Objeto SphericalCoordenate
     * @param angle Ángulo polar (Grados)
     * @param max |Etotal| máximo
     * @param global Objeto de la clase Global
     */
    public PolarCoordenate(SphericalCoordenate s, double angle, double max, Global global) {
        this.r = s.getRho();
        switch (global.getCurrentPlotType()) {
            case Global.PLOT3D:
                this.angle = angle;
                this.colorLevel = s.getColorLevel();
                this.z = s.getRho() * Math.cos(Math.toRadians(s.getTheta()));
                double l = s.getRho() * Math.sin(Math.toRadians(s.getTheta()));
                if (l != 0) {
                    this.r = l / max;
                } else {
                    this.r = 0;
                }
                break;
            case Global.ELEVATIONPLOT:
                this.angle = angle;
                this.r = s.getRho() / max;
                this.z = 0;
                this.rho = s.getRho();
                this.colorLevel = 1;
                break;
            case Global.AZIMUTHPLOT:
                this.angle = angle;
                this.r = s.getRho() / max;
                this.z = 0;
                this.rho = s.getRho();
                this.colorLevel = 1;
                break;
            default:
                throw new AssertionError();
        }
    }

    /**
     * Convierte al Objeto PolarCoordenate a un sistema de coordenadas
     * rectangular
     *
     * @return Objeto PointColor equivalente a la conversión, en coordenadas
     * rectangulares, del objeto PolarCoordenate
     */
    public PointColor toRectangular() {
        double x = this.r * Math.cos(Math.toRadians(getAngle()));
        double y = this.r * Math.sin(Math.toRadians(getAngle()));
        double z = this.z;
        int color = this.colorLevel;
        return new PointColor(x, y, z, color);
    }

    /**
     * Convierte al Objeto PolarCoordenate a un sistema de coordenadas
     * rectangular, efectuando una rotación angular adicional
     *
     * @param alfa Ángulo de rotación, sentido horario, en grados
     * @return Objeto PointColor equivalente a la conversión, en coordenadas
     * rectangulares, del objeto PolarCoordenate
     */
    public PointColor toRectangularRotated(double alfa) {
        double x = this.r * Math.cos(Math.toRadians(getAngle() + alfa));
        double y = this.r * Math.sin(Math.toRadians(getAngle() + alfa));
        double z = this.z;
        int color = this.colorLevel;
        return new PointColor(x, y, z, color);
    }

    /**
     * Convierte el objeto PolarCoordenate a una representación String
     *
     * @return Representación String del objeto PolarCoordenate
     */
    public String toString() {
        return String.valueOf(getAngle()).replace(".", ",") + " ; " + String.valueOf(getRho()).replace(".", ",");
    }

    /**
     * @return the rho
     */
    public double getRho() {
        return rho;
    }

    /**
     * @param rho the rho to set
     */
    public void setRho(double rho) {
        this.rho = rho;
    }

    /**
     * @return the angle
     */
    public double getAngle() {
        return angle;
    }

    /**
     * @param angle the angle to set
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @return the colorLevel
     */
    public int getColorLevel() {
        return colorLevel;
    }

    /**
     * @param colorLevel the colorLevel to set
     */
    public void setColorLevel(int colorLevel) {
        this.colorLevel = colorLevel;
    }
}
