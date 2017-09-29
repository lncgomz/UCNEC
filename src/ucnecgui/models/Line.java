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
public class Line {

    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;

    /**
     * Constructor de la clase Line
     */
    public Line() {
        x1 = 0;
        y1 = 0;
        z1 = 0;
        x2 = 0;
        y2 = 0;
        z2 = 0;
    }

    /**
     *Constructor de la clase Line. Genera un Line a partir de dos puntos cartesianos A y B, siendo estos
     * los extremos de la línea
     * @param A Extremo A de la línea
     * @param B Extremo de la línea
     */
    public Line(Point A, Point B) {
        x1 = A.getA();
        y1 = A.getB();
        z1 = A.getC();
        x2 = B.getA();
        y2 = B.getB();
        z2 = B.getC();
    }

    /**
    *Constructor de la clase Line. Genera un Line a partir de un objeto Wire.
     * @param wire Objeto Wire
     */
    public Line(Wire wire) {
        x1 = wire.getX1();
        y1 = wire.getY1();
        z1 = wire.getZ1();
        x2 = wire.getX2();
        y2 = wire.getY2();
        z2 = wire.getZ2();
    }

    /**
     * Rotación de un objeto Line. Este procedimiento es llevado a cabo a través de las matrices de rotación en el espacio euclídeo.
     * La resolución de esta matriz está particularizada para cada eje de rotación (X,Y,Z) y en sentido antihorario.
     * @param axis Eje de rotación (X,Y o Z)
     * @param angle Ángulo de rotación antihorario, en grados.
     * @return Objeto Line rotado en el eje axis a un ángulo de angle grados
     */
    public Line rotateLine(int axis, double angle) {
        double radians = Math.toRadians(angle);
        Line line = new Line();
        switch (axis) {
            case Global.XAXIS:
                line.setX1(this.getX1());
                line.setY1(this.getY1() * Math.cos(radians) - this.getZ1() * Math.sin(radians));
                line.setZ1(this.getY1() * Math.sin(radians) + this.getZ1() * Math.cos(radians));
                line.setX2(this.getX2());
                line.setY2(this.getY2() * Math.cos(radians) - this.getZ2() * Math.sin(radians));
                line.setZ2(this.getY2() * Math.sin(radians) + this.getZ2() * Math.cos(radians));
                break;
            case Global.YAXIS:
                line.setX1(this.getX1() * Math.cos(radians) + this.getZ1() * Math.sin(radians));
                line.setY1(this.getY1());
                line.setZ1((-1 * this.getX1()) * Math.sin(radians) + this.getZ1() * Math.cos(radians));

                line.setX2(this.getX2() * Math.cos(radians) + this.getZ2() * Math.sin(radians));
                line.setY2(this.getY2());
                line.setZ2((-1 * this.getX2()) * Math.sin(radians) + this.getZ2() * Math.cos(radians));
                break;
            case Global.ZAXIS:
                line.setX1(this.getX1() * Math.cos(radians) - this.getY1() * Math.sin(radians));
                line.setY1(this.getX1() * Math.sin(radians) + this.getY1() * Math.cos(radians));
                line.setZ1(this.getZ1());

                line.setX2(this.getX2() * Math.cos(radians) - this.getY2() * Math.sin(radians));
                line.setY2(this.getX2() * Math.sin(radians) + this.getY2() * Math.cos(radians));
                line.setZ2(this.getZ2());
                break;
            default:
                throw new AssertionError();
        }
        return line;
    }

    /**
     * @return the x1
     */
    public double getX1() {
        return x1;
    }

    /**
     * @param x1 the x1 to set
     */
    public void setX1(double x1) {
        this.x1 = x1;
    }

    /**
     * @return the y1
     */
    public double getY1() {
        return y1;
    }

    /**
     * @param y1 the y1 to set
     */
    public void setY1(double y1) {
        this.y1 = y1;
    }

    /**
     * @return the z1
     */
    public double getZ1() {
        return z1;
    }

    /**
     * @param z1 the z1 to set
     */
    public void setZ1(double z1) {
        this.z1 = z1;
    }

    /**
     * @return the x2
     */
    public double getX2() {
        return x2;
    }

    /**
     * @param x2 the x2 to set
     */
    public void setX2(double x2) {
        this.x2 = x2;
    }

    /**
     * @return the y2
     */
    public double getY2() {
        return y2;
    }

    /**
     * @param y2 the y2 to set
     */
    public void setY2(double y2) {
        this.y2 = y2;
    }

    /**
     * @return the z2
     */
    public double getZ2() {
        return z2;
    }

    /**
     * @param z2 the z2 to set
     */
    public void setZ2(double z2) {
        this.z2 = z2;
    }

    /**
     * Devuelve la longitud del objeto Line
     * @return Distancia del objeto Line
     */
    public double distance() {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2) + Math.pow((z2 - z1), 2));
    }
}
