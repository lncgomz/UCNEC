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

public class Lab1_1 {

    private double ld;
    private double diameter;
    private double lowerSWRFreq;
    private Complex za;
    private double lowerSWR;
    private double designSWR;
    private double bw;

    /**
     *Constructor de la clase Lab1_1
     */
    public Lab1_1() {
        ld = 0;
        diameter = 0;
        lowerSWRFreq = 0;
        za = new Complex(0, 0);
        lowerSWR = 0;
        designSWR = 0;
        bw = 0;
    }

    /**
     * @return the diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * @param diameter the diameter to set
     */
    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    /**
     * @return the lowerSWRFreq
     */
    public double getLowerSWRFreq() {
        return lowerSWRFreq;
    }

    /**
     * @param lowerSWRFreq the lowerSWRFreq to set
     */
    public void setLowerSWRFreq(double lowerSWRFreq) {
        this.lowerSWRFreq = lowerSWRFreq;
    }

    /**
     * @return the za
     */
    public Complex getZa() {
        return za;
    }

    /**
     * @param za the za to set
     */
    public void setZa(Complex za) {
        this.za = za;
    }

    /**
     * @return the lowerSWR
     */
    public double getLowerSWR() {
        return lowerSWR;
    }

    /**
     * @param lowerSWR the lowerSWR to set
     */
    public void setLowerSWR(double lowerSWR) {
        this.lowerSWR = lowerSWR;
    }

    /**
     * @return the designSWR
     */
    public double getDesignSWR() {
        return designSWR;
    }

    /**
     * @param designSWR the designSWR to set
     */
    public void setDesignSWR(double designSWR) {
        this.designSWR = designSWR;
    }

    /**
     * @return the bw
     */
    public double getBw() {
        return bw;
    }

    /**
     * @param bw the bw to set
     */
    public void setBw(double bw) {
        this.bw = bw;
    }

    /**
     * @return the ld
     */
    public double getLd() {
        return ld;
    }

    /**
     * @param ld the ld to set
     */
    public void setLd(double ld) {
        this.ld = ld;
    }
}
