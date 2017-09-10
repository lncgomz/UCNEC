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

import java.text.DecimalFormat;
import java.util.ArrayList;
import ucnecgui.Global;

/**
 *
 * @author Leoncio Gómez
 */

public class Wire {

    private int number;
    private double x1;
    private double y1;
    private double z1;
    private double x2;
    private double y2;
    private double z2;
    private double radius;
    private int segs;

    /**
     *Constructor de la clase Wire
     */
    public Wire() {
        x1 = 0;
        y1 = 0;
        z1 = 0;
        x2 = 0;
        y2 = 0;
        z2 = 0;
        radius = 0;
        segs = 0;
    }

    /**
     *Constructor de la clase Wire.  Genera un Wire a partir de un vector de String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param row  Texto obtenido en el archivo de salida del script nec2++
     */
    public Wire(String[] row) {
        number = Integer.valueOf(row[0]);
        x1 = Double.valueOf(row[1]);
        y1 = Double.valueOf(row[2]) ;
        z1 = Double.valueOf(row[3]) ;
        x2 = Double.valueOf(row[4]) ;
        y2 = Double.valueOf(row[5]) ;
        z2 = Double.valueOf(row[6]) ;
        radius = Double.valueOf(row[7]);
        segs = Integer.valueOf(row[8]);
    }
    

    /**
     *Constructor de la clase Wire.  Genera un Wire a partir de un objeto Line
     * @param line Objeto Line a partir del cual se generará el Wire
     */
    public Wire(Line line) {
        this.number = 0;
        this.x1 = line.getX1();
        this.y1 = line.getY1();
        this.z1 = line.getZ1();
        this.x2 = line.getX2();
        this.y2 = line.getX2();
        this.z2 = line.getZ2();
        this.radius = 0;
        this.segs = 0;
    }

    /**
     *Constructor de la clase Wire.  Genera un Wire a partir de dos objetos Point que representan los extremos del alambre
     * @param pointA Extremo A del alambre
     * @param pointB Extremo B del alambre
     */
    public Wire(Point pointA, Point pointB) {
        this.number = 0;
        this.x1 = pointA.getA();
        this.y1 = pointA.getB();
        this.z1 = pointA.getC();
        this.x2 = pointB.getA();
        this.y2 = pointB.getB();
        this.z2 = pointB.getC();
        this.radius = 0;
        this.segs = 0;
    }

     /**
     *Obtiene la representación vectorial del objeto Wire
     * @return Objeto Point que representa el vector del objeto Wire invocante
     */
    public Point getVector() {
        double x = this.getX2() - this.getX1();
        double y = this.getY2() - this.getY1();
        double z = this.getZ2() - this.getZ1();
        Point resp = new Point(x, y, z);
        return resp;
    }

    /**
     *Obtiene la representación del vector unitario del objeto Wire
     * @return Objeto Point que representa el vector unitario del objeto Wire invocante
     */
    public Point getUnitVector() {
        Point vector = getVector();
        double dimVector = vector.getLength();
        Point resp = new Point();
        resp.setA(vector.getA() / dimVector);
        resp.setB(vector.getB() / dimVector);
        resp.setC(vector.getC() / dimVector);
        return resp;
    }

    /**
     * Obtiene un objeto Point con las coordenadas de la posición que representa un porcentaje percentage de la longitud del vector que 
     * invoca este método
     * @param percentage Porcentaje de la posición del vector
     * @return Coordenadas de la posición que representa un porcentaje percentage de la longitud del vector que 
     * invoca este método
     */
    public Point getLocation(double percentage) {
        Point unitVector = getUnitVector();
        double factor = getVector().getLength() * percentage;
        Point resp = new Point();
        Point endPoint = new Point();
        endPoint.setA(this.x1);
        endPoint.setB(this.y1);
        endPoint.setC(this.z1);
        resp.setA((unitVector.getA() * factor) + endPoint.getA());
        resp.setB((unitVector.getB() * factor) + endPoint.getB());
        resp.setC((unitVector.getC() * factor) + endPoint.getC());
        return resp;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
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
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @param radius the radius to set
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * @return the segs
     */
    public int getSegs() {
        return segs;
    }

    /**
     * @param segs the segs to set
     */
    public void setSegs(int segs) {
        this.segs = segs;
    }

    /**
     *Convierte el objeto Wire a un comando interpretable por NEC
     * @param wires Arreglo de objetos de la clase Wire
     * @return Comando de geometría de NEC
     */
    public static ArrayList<String> toString(ArrayList<Wire> wires) {
        ArrayList<String> resp = new ArrayList<String>();

        for (Wire wire : wires) {
            String nWire = "GW " + wire.getNumber() + ","
                    + wire.getSegs() + ","
                    + decimalFormat(wire.getX1()) + ","
                    + decimalFormat(wire.getY1()) + ","
                    + decimalFormat(wire.getZ1()) + ","
                    + decimalFormat(wire.getX2()) + ","
                    + decimalFormat(wire.getY2()) + ","
                    + decimalFormat(wire.getZ2()) + ","
                    + wire.getRadius() / 2;
            resp.add(nWire);
        }
        return resp;
    }
    
    /**
     *Convierte el comando de geometría de NEC a un objeto Wire
     * @param line Comando de geometría de NEC
     * @param global Objeto de la clase Global
     * @return Objeto Wire 
     * 
     */
    public static Wire fromString(String line, Global global) {
        String[] ln = line.split(",");
        Wire nWire = new Wire();
        String[] firstLn = ln[0].split("\\s+");
        nWire.setNumber(Integer.valueOf(firstLn[1]));
        nWire.setSegs(Integer.valueOf(ln[1]));
        nWire.setX1(Double.valueOf(ln[2]));
        nWire.setY1(Double.valueOf(ln[3]));
        nWire.setZ1(Double.valueOf(ln[4]));
        nWire.setX2(Double.valueOf(ln[5]));
        nWire.setY2(Double.valueOf(ln[6]));
        nWire.setZ2(Double.valueOf(ln[7]));
        double factor = global.unit2UpperFactor();
        nWire.setRadius(2 * (Double.valueOf(ln[8]))*factor);
        return nWire;
    }
    /*
     *Convierte el comando de geometría de NEC a un objeto Wire, incrementando la etiqueta del alambre correspondiente
     * según lo indicado por factor (En caso de encontrarse otros alambres cargados en el entorno al momento de invocar este 
     * método)
     * @param line Comando de geometría de NEC
     * @param factor Factor de incremento
     * @return Objeto Wire 
     */
    public static Wire importFromString(String line, int factor) {
        String[] ln = line.split(",");
        Wire nWire = new Wire();
        String[] firstLn = ln[0].split("\\s+");        
        nWire.setNumber(Integer.valueOf(firstLn[1]) + factor);
        nWire.setSegs(Integer.valueOf(ln[1]));
        nWire.setX1(Double.valueOf(ln[2]));
        nWire.setY1(Double.valueOf(ln[3]));
        nWire.setZ1(Double.valueOf(ln[4]));
        nWire.setX2(Double.valueOf(ln[5]));
        nWire.setY2(Double.valueOf(ln[6]));
        nWire.setZ2(Double.valueOf(ln[7]));
        nWire.setRadius(2 * Double.valueOf(ln[8]));
        return nWire;
    }
  
    /**
     *Aplica un formato decimal al parámetro number de forma tal que el mismo presente 4 posiciones decimales
     * @param number Número al que se le aplicará el formato
     * @return Número con nuevo formato decimal #.####
     */
    public static double decimalFormat(double number) {
        DecimalFormat formatter = new DecimalFormat("#.####");
        return Double.valueOf(formatter.format(number).replace(",", "."));
    }
}
