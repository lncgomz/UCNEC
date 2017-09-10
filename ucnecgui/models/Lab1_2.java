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

public class Lab1_2 {

    private double scale;
    private double length;
    private double diameter;
    private Complex za;
    private double designSWR;

    /**
     *Constructor de la clase Lab1_2
     */
    public Lab1_2() {
        scale = 0;
        length = 0;
        diameter = 0;
        za = new Complex(0, 0);
        designSWR = 0;
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
     * @return the scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }
}
