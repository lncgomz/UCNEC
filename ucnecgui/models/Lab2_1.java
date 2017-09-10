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

public class Lab2_1 {

    private double  angle;
    private Complex v;

    /**
     *Constructor de la clase Lab2_1
     */
    public Lab2_1() {
        angle = 0;
        v = new Complex(0, 0);
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
}
