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

public class Currents {
    private int tag;
    private int seg;
    private Complex i;
    
    /**
     *Constructor de la clase Currents
     */
    public Currents(){
        tag  = 0;
        seg = 0;
        i = new Complex(0,0);
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
}
