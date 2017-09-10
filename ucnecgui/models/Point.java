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
public class Point {

    private double a;
    private double b;
    private double c;

    /**
     * Constructor de la clase Point
     */
    public Point() {
        a = 0;
        b = 0;
        c = 0;
    }

    /**
     * Constructor de la clase Point a partir de las tres coordenadas (A,B,C) de
     * tipo Double
     *
     * @param A Coordenada A
     * @param B Coordenada B
     * @param C Coordenada C
     */
    public Point(double A, double B, double C) {
        this.a = A;
        this.b = B;
        this.c = C;
    }

    /**
     * Constructor de la clase Point a partir de las tres coordenadas (A,B,C) de
     * tipo String
     *
     * @param A Coordenada A
     * @param B Coordenada B
     * @param C Coordenada C
     */
    public Point(String A, String B, String C) {
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

    /**
     * Determina el plano cartesiano común a 3 puntos A, B y C
     * @param A Punto A
     * @param B Punto B
     * @param C Punto C
     * @return Plano cartesiano común a los 3 puntos
     */
    public int getPlane(Point A, Point B, Point C) {
        if (A.getA() == B.getA() && A.getA() == C.getA()) {
            return Global.YZPLANE;
        } else if (A.getB() == B.getB() && A.getB() == C.getB()) {
            return Global.XZPLANE;
        } else {
            return Global.XYPLANE;
        }
    }

    /**
     *Devuelve la longitud comprendida desde el origen hasta el punto que invoca el método
     * @return Longitud del vector posición del punto
     */
    public double getLength() {
        return Math.sqrt(Math.pow(getA(), 2) + Math.pow(getB(), 2) + Math.pow(getC(), 2));
    }
    
    /**
     *Devuelve la representación del objeto Point a String
     * @return Representación a String
     */

    public String toString() {
        return String.valueOf(getA()).replace(".", ",") + ";" + String.valueOf(getB()).replace(".", ",") + ";" + String.valueOf(getC()).replace(".", ",");
    }
}
