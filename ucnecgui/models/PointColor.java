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
public class PointColor {

    private double a;
    private double b;
    private double c;
    private int colorLevel;

    /**
     * Constructor de la clase PointColor
     */
    public PointColor() {
        a = 0;
        b = 0;
        c = 0;
        colorLevel = 0;
    }

    /**
     * Constructor de la clase PointColor a partir de las coordenadas (A,B,C) y
     * un valor relativo a su color.
     *
     * @param A Coordenada A
     * @param B Coordenada B
     * @param C Coordenada C
     * @param colorLevel Valor de color
     */
    public PointColor(double A, double B, double C, int colorLevel) {
        this.a = A;
        this.b = B;
        this.c = C;
        this.colorLevel = colorLevel;
    }

    /**
     ** Constructor de la clase PointColor a partir de las coordenadas (A,B,C)
     *
     * @param A Coordenada A
     * @param B Coordenada B
     * @param C Coordenada C
     */
    public PointColor(String A, String B, String C) {
        this.a = Double.valueOf(A);
        this.b = Double.valueOf(B);
        this.c = Double.valueOf(C);
    }

 
    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

  
    public void setB(double b) {
        this.b = b;
    }

 
    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public int getColorLevel() {
        return colorLevel;
    }

    public void setColorLevel(int colorLevel) {
        this.colorLevel = colorLevel;
    }

    /**
     * Determina el plano cartesiano común a 3 puntos A, B y C
     *
     * @param A Punto A
     * @param B Punto B
     * @param C Punto C
     * @return Plano cartesiano común a los 3 puntos
     */
    public int getPlane(PointColor A, PointColor B, PointColor C) {
        if (A.getA() == B.getA() && A.getA() == C.getA()) {
            return Global.YZPLANE;
        } else if (A.getB() == B.getB() && A.getB() == C.getB()) {
            return Global.XZPLANE;
        } else {
            return Global.XYPLANE;
        }
    }

    /**
     * Devuelve la longitud comprendida desde el origen hasta el punto que
     * invoca el método
     *
     * @return Longitud del vector posición del punto
     */
    public double getLength() {
        return Math.sqrt(Math.pow(getA(), 2) + Math.pow(getB(), 2) + Math.pow(getC(), 2));
    }

    /**
     * Devuelve la representación del objeto Point a String
     *
     * @return Representación a String
     */
    public String toString() {
        return String.valueOf(getA()).replace(".", ",") + ";" + String.valueOf(getB()).replace(".", ",") + ";" + String.valueOf(getC()).replace(".", ",");
    }
}
