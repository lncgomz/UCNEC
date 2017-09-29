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

public class Load {

    private int loadSeg;
    private long loadPercentage;
    private double r;
    private double x;
    private double l;
    private double c;
    private int type;

    /**
     * Constructor de la clase Load
     */
    public Load() {
        loadSeg = 0;
        loadPercentage = 0;
        r = 0.0;
        x = 0.0;
        l = 0.0;
        c = 0.0;
        type = 0;
    }

 /**
     *Constructor de la clase Load.  Genera un Load a partir de un vector de String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param row  Texto obtenido en el archivo de salida del script nec2++
     */
    public Load(String[] row) {
        loadSeg = Integer.valueOf(row[0]);
        loadPercentage = Integer.valueOf(row[1]);
        r = Double.valueOf(row[2]);
        x = Double.valueOf(row[3]);
        l = Double.valueOf(row[4]);
        c = Double.valueOf(row[5]);
        type = Integer.valueOf(row[6]);
    }


/**
     *Convierte el objeto Load a un comando interpretable por NEC
     * @param loads Arreglo de objetos de la clase Load
     * @return Comando de cargas  de NEC
     */
    public static ArrayList<String> toString(ArrayList<Load> loads) {
        ArrayList<String> resp = new ArrayList<String>();
        String nLoad = "";
        StringBuilder stringBuilder = new StringBuilder();

        for (Load load : loads) {
            int loadType = load.getType();
            switch (loadType) {
                case 0:
                case 1:
                    nLoad = "LD "
                            + load.getType() + ","
                            + load.getLoadSeg() + ","
                            + load.getLoadPercentage() + ","
                            + load.getLoadPercentage() + ","
                            + load.getR() + ","
                            + load.getL() + ","
                            + load.getC();
                    resp.add(nLoad);

                    break;
                case 4:
                    nLoad = "LD "
                            + load.getType() + ","
                            + load.getLoadSeg() + ","
                            + load.getLoadPercentage() + ","
                            + load.getLoadPercentage() + ","
                            + load.getR() + ","
                            + load.getX();
                    resp.add(nLoad);

                    break;
                case 5:
                    nLoad = "LD "
                            + load.getType() + ","
                            + load.getLoadSeg() + ","
                            + load.getLoadPercentage() + ","
                            + load.getLoadPercentage() + ","
                            + load.getR();
                    resp.add(nLoad);

                    break;
                case 6:
                    break;
                default:
                    throw new AssertionError();
            }

        }

        return resp;
    }

    /**
     *Convierte el comando de cargas de NEC a un objeto Load
     * @param line Comando de cargas de NEC
     * @return Objeto Load 
     */
    public static Load fromString(String line) {
        Load resp = new Load();

        String[] ln = line.split(",");

        String[] firstLn = ln[0].split("\\s+");
        int loadType = Integer.valueOf(firstLn[1]);
        resp.setType(loadType);

        switch (loadType) {
            case 0:
            case 1:
                resp.setLoadSeg(Integer.valueOf(ln[1]));
                resp.setLoadPercentage(Long.valueOf(ln[2]));
                resp.setR(Double.valueOf(ln[4]));
                resp.setL(Double.valueOf(ln[5]));
                resp.setC(Double.valueOf(ln[6]));
                break;
            case 4:
                resp.setLoadSeg(Integer.valueOf(ln[1]));
                resp.setLoadPercentage(Long.valueOf(ln[2]));
                resp.setR(Double.valueOf(ln[4]));
                resp.setX(Double.valueOf(ln[5]));

                break;
            case 5:
                if (ln.length == 5) {
                    resp.setLoadSeg(Integer.valueOf(ln[1]));
                    resp.setLoadPercentage(Long.valueOf(ln[2]));
                    resp.setR(Double.valueOf(ln[4]));
                }
                break;
            default:
                throw new AssertionError();
        }

        return resp;
    }

    /**
     * @return the loadSeg
     */
    public int getLoadSeg() {
        return loadSeg;
    }

    /**
     * @param loadSeg the loadSeg to set
     */
    public void setLoadSeg(int loadSeg) {
        this.loadSeg = loadSeg;
    }

    /**
     * @return the loadPercentage
     */
    public long getLoadPercentage() {
        return loadPercentage;
    }

    /**
     * @param loadPercentage the loadPercentage to set
     */
    public void setLoadPercentage(long loadPercentage) {
        this.loadPercentage = loadPercentage;
    }

    /**
     * @return the r
     */
    public double getR() {
        return r;
    }

    /**
     * @param r the r to set
     */
    public void setR(double r) {
        this.r = r;
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the l
     */
    public double getL() {
        return l;
    }

    /**
     * @param l the l to set
     */
    public void setL(double l) {
        this.l = l;
    }

    /**
     * @return the c
     */
    public double getC() {
        return c;
    }

    /**
     * @param c the c to set
     */
    public void setC(double c) {
        this.c = c;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
}
