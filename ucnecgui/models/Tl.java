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

import java.util.ArrayList;

/**
 *
 * @author Leoncio Gómez
 */

public class Tl {

    private int tlWireTag1;
    private long tlWire1SegPercentage;
    private int tlWireTag2;
    private long tlWire2SegPercentage;
    private double tlCharImp;
    private double tlLenght;
    private double reSY1;
    private double imSY1;
    private double reSY2;
    private double imSY2;

    /**
     *
     */
    public Tl() {
    }

    /**
     *Constructor de la clase Tl.  Genera un Tl a partir de un vector de String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param row  Texto obtenido en el archivo de salida del script nec2++
     */
    public Tl(String[] row) {
        tlWireTag1 = Integer.valueOf(row[0]);
        tlWire1SegPercentage = Integer.valueOf(row[1]);
        tlWireTag2 = Integer.valueOf(row[2]);
        tlWire2SegPercentage = Integer.valueOf(row[3]);
        tlCharImp = Double.valueOf(row[4]);
        tlLenght = Double.valueOf(row[5]);
        reSY1 = Double.valueOf(row[6]);
        imSY1 = Double.valueOf(row[7]);
        reSY2 = Double.valueOf(row[8]);
        imSY2 = Double.valueOf(row[9]);
    }

    /**
     * @return the tlWireTag1
     */
    public int getTlWireTag1() {
        return tlWireTag1;
    }

    /**
     * @param tlWireTag1 the tlWireTag1 to set
     */
    public void setTlWireTag1(int tlWireTag1) {
        this.tlWireTag1 = tlWireTag1;
    }

    /**
     * @return the tlWire1SegPercentage
     */
    public long getTlWire1SegPercentage() {
        return tlWire1SegPercentage;
    }

    /**
     * @param tlWire1SegPercentage the tlWire1SegPercentage
     * to set
     */
    public void setTlWire1SegPercentage(long tlWire1SegPercentage) {
        this.tlWire1SegPercentage = tlWire1SegPercentage;
    }

    /**
     * @return the tlWireTag2
     */
    public int getTlWireTag2() {
        return tlWireTag2;
    }

    /**
     * @param tlWireTag2 the tlWireTag2 to set
     */
    public void setTlWireTag2(int tlWireTag2) {
        this.tlWireTag2 = tlWireTag2;
    }

    /**
     * @return the tlWire2SegPercentage
     */
    public long getTlWire2SegPercentage() {
        return tlWire2SegPercentage;
    }

    /**
     * @param tlWire2SegPercentage the tlWire2SegPercentage
     * to set
     */
    public void setTlWire2SegPercentage(long tlWire2SegPercentage) {
        this.tlWire2SegPercentage = tlWire2SegPercentage;
    }

    /**
     * @return the tlCharImp
     */
    public double getTlCharImp() {
        return tlCharImp;
    }

    /**
     * @param tlCharImp the tlCharImp to set
     */
    public void setTlCharImp(double tlCharImp) {
        this.tlCharImp = tlCharImp;
    }

    /**
     * @return the tlLenght
     */
    public double getTlLenght() {
        return tlLenght;
    }

    /**
     * @param tlLenght the tlLenght to set
     */
    public void setTlLenght(double tlLenght) {
        this.tlLenght = tlLenght;
    }

    /**
     * @return the reSY1
     */
    public double getReSY1() {
        return reSY1;
    }

    /**
     * @param reSY1 the reSY1 to set
     */
    public void setReSY1(double reSY1) {
        this.reSY1 = reSY1;
    }

    /**
     * @return the imSY1
     */
    public double getImSY1() {
        return imSY1;
    }

    /**
     * @param imSY1 the imSY1 to set
     */
    public void setImSY1(double imSY1) {
        this.imSY1 = imSY1;
    }

    /**
     * @return the reSY2
     */
    public double getReSY2() {
        return reSY2;
    }

    /**
     * @param reSY2 the reSY2 to set
     */
    public void setReSY2(double reSY2) {
        this.reSY2 = reSY2;
    }

    /**
     * @return the imSY2
     */
    public double getImSY2() {
        return imSY2;
    }

    /**
     * @param imSY2 the imSY2 to set
     */
    public void setImSY2(double imSY2) {
        this.imSY2 = imSY2;
    }

    /**
     *Convierte el objeto Tl a un comando interpretable por NEC
     * @param tls Arreglo de objetos de la clase Tl
     * @return Comando de línea de transmisión de NEC
     */
    public static ArrayList<String> toString(ArrayList<Tl> tls) {
        ArrayList<String> resp = new ArrayList<String>();
        String nTl = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (Tl tl : tls) {
            nTl = "TL "
                    + tl.getTlWireTag1() + ","
                    + tl.getTlWire1SegPercentage() + ","
                    + tl.getTlWireTag2() + ","
                    + tl.getTlWire2SegPercentage() + ","
                    + tl.getTlCharImp() + ","
                    + tl.getTlLenght() + ","
                    + tl.getReSY1() + ","
                    + tl.getImSY1() + ";"
                    + tl.getReSY2() + ","
                    + tl.getImSY2();
            resp.add(nTl);
        }
        return resp;
    }
    
    /**
     *Convierte el comando de línea de transmisición de NEC a un objeto Tl
     * @param line Comando de línea de transmisión de NEC
     * @return Objeto Tl 
     */
    public static Tl fromString(String line) {
        
         Tl resp = new Tl();
        String[] ln = line.split(",");
        String[] firstLn = ln[0].split("\\s+");
        int tlWireTag1 = Integer.valueOf(firstLn[1]);
        
        resp.setTlWireTag1(tlWireTag1);
        resp.setTlWire1SegPercentage(Long.valueOf(ln[1]));
        resp.setTlWireTag2(Integer.valueOf(ln[2]));
        resp.setTlWire2SegPercentage(Long.valueOf(ln[3]));
        resp.setTlCharImp(Double.valueOf(ln[4]));
        resp.setTlLenght(Double.valueOf(ln[5]));
        resp.setReSY1(Double.valueOf(ln[6]));
        resp.setImSY1(Double.valueOf(ln[7]));
        resp.setReSY2(Double.valueOf(ln[8]));
        resp.setImSY2(Double.valueOf(ln[9]));
        
        return resp;        
    }
}
