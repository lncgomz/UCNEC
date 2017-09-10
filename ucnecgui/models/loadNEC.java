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

public class loadNEC {

    private int tag;
    private int seg;
    private Complex v;
    private Complex i;
    private Complex z;
    private double power;

    /**
     *Constructor de la clase loadNEC
     */
    public loadNEC() {
        tag = 0;
        seg = 0;
        v = new Complex(0, 0);
        i = new Complex(0, 0);
        z = new Complex(0, 0);
        power = 0;
    }

    /**
     *Convierte el objeto loadNEC a una representación String
     * @param nload Objeto de la clase loadNEC
     * @return Representación String del objeto loadNEC
     */
    public static String loadNEC2String(loadNEC nload) {        
        return  System.lineSeparator() + "Alambre: " + nload.getTag() + " Segmento:  " + nload.getSeg() + System.lineSeparator()
                + "Voltaje: " + nload.getV() + " V " + System.lineSeparator()
                + "Corriente: " + nload.getI() + " I " + System.lineSeparator()
                + "Impedancia: " + nload.getZ() + " Ohm " + System.lineSeparator()
                + "Potencia: " + nload.getPower() + " watts";
    }

    /**
     * @return the v
     */
    public Complex getV() {
        return v;
    }

    /**
     * @param v the v to set
     */
    public void setV(Complex v) {
        this.v = v;
    }

    /**
     * @return the i
     */
    public Complex getI() {
        return i;
    }

    /**
     * @param i the i to set
     */
    public void setI(Complex i) {
        this.i = i;
    }

    /**
     * @return the z
     */
    public Complex getZ() {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(Complex z) {
        this.z = z;
    }

    /**
     * @return the power
     */
    public double getPower() {
        return power;
    }

    /**
     * @param power the power to set
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * @return the tag
     */
    public int getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * @return the seg
     */
    public int getSeg() {
        return seg;
    }

    /**
     * @param seg the seg to set
     */
    public void setSeg(int seg) {
        this.seg = seg;
    }
}
