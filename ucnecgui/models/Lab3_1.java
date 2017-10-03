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

public class Lab3_1 {

    private double angle;
    private double vmodule;
    private double fdb;
    private double ftimes;
    private int plane;
    public static int E = 0;
    public static int H = 1;

    /**
     *Constructor de la clase Lab3_1
     */
    public Lab3_1() {
        angle = 0;
        vmodule = 0;
        fdb = 0;
        ftimes = 0;
        plane = Lab3_1.E;
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
     * @return the vmodule
     */
    public double getVmodule() {
        return vmodule;
    }

    /**
     * @param vmodule the vmodule to set
     */
    public void setVmodule(double vmodule) {
        this.vmodule = vmodule;
    }

    /**
     * @return the fdb
     */
    public double getFdb() {
        return fdb;
    }

    /**
     * @param fdb the fdb to set
     */
    public void setFdb(double fdb) {
        this.fdb = fdb;
    }

    /**
     * @return the plane
     */
    public int getPlane() {
        return plane;
    }

    /**
     * @param plane the plane to set
     */
    public void setPlane(int plane) {
        this.plane = plane;
    }

    /**
     * @return the ftimes
     */
    public double getFtimes() {
        return ftimes;
    }

    /**
     * @param ftimes the ftimes to set
     */
    public void setFtimes(double ftimes) {
        this.ftimes = ftimes;
    }
}
