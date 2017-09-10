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

import controllers.Complex;

/**
 *
 * @author Leoncio Gómez
 */
public class Lab1_3 {

    private double distance;
    private Complex vSrc1;
    private Complex iSrc1;
    private Complex zSrc1;
    private Complex vSrc2;
    private Complex iSrc2;
    private Complex zSrc2;
    private Complex zMutual;
    private double coupling;

    /**
     * Constructor de la clase Lab1_3
     */
    public Lab1_3() {
        distance = 0;
        vSrc1 = new Complex(0, 0);
        iSrc1 = new Complex(0, 0);
        zSrc1 = new Complex(0, 0);
        vSrc2 = new Complex(0, 0);
        iSrc2 = new Complex(0, 0);
        zSrc2 = new Complex(0, 0);
        zMutual = new Complex(0, 0);
        coupling = 0;
    }

    /**
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the vSrc1
     */
    public Complex getvSrc1() {
        return vSrc1;
    }

    /**
     * @param vSrc1 the vSrc1 to set
     */
    public void setvSrc1(Complex vSrc1) {
        this.vSrc1 = vSrc1;
    }

    /**
     * @return the iSrc1
     */
    public Complex getiSrc1() {
        return iSrc1;
    }

    /**
     * @param iSrc1 the iSrc1 to set
     */
    public void setiSrc1(Complex iSrc1) {
        this.iSrc1 = iSrc1;
    }

    /**
     * @return the zSrc1
     */
    public Complex getzSrc1() {
        return zSrc1;
    }

    /**
     * @param zSrc1 the zSrc1 to set
     */
    public void setzSrc1(Complex zSrc1) {
        this.zSrc1 = zSrc1;
    }

    /**
     * @return the vSrc2
     */
    public Complex getvSrc2() {
        return vSrc2;
    }

    /**
     * @param vSrc2 the vSrc2 to set
     */
    public void setvSrc2(Complex vSrc2) {
        this.vSrc2 = vSrc2;
    }

    /**
     * @return the iSrc2
     */
    public Complex getiSrc2() {
        return iSrc2;
    }

    /**
     * @param iSrc2 the iSrc2 to set
     */
    public void setiSrc2(Complex iSrc2) {
        this.iSrc2 = iSrc2;
    }

    /**
     * @return the zSrc2
     */
    public Complex getzSrc2() {
        return zSrc2;
    }

    /**
     * @param zSrc2 the zSrc2 to set
     */
    public void setzSrc2(Complex zSrc2) {
        this.zSrc2 = zSrc2;
    }

    /**
     * @return the zMutual
     */
    public Complex getzMutual() {
        return zMutual;
    }

    /**
     * @param zMutual the zMutual to set
     */
    public void setzMutual(Complex zMutual) {
        this.zMutual = zMutual;
    }

    /**
     * @return the coupling
     */
    public double getCoupling() {
        return coupling;
    }

    /**
     * @param coupling the coupling to set
     */
    public void setCoupling(double coupling) {
        this.coupling = coupling;
    }
}
