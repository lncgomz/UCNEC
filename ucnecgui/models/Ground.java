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

public class Ground {

    private int type;
    private int radialWires;
    private int empty1;
    private int empty2;
    private double relDielConst;
    private double conductivity;
    private boolean nonReal;

    /**
     * Constructor de la clase Ground
     */
    public Ground() {
        type = 0;
        radialWires = 0;
        empty1 = 0;
        empty2 = 0;
        relDielConst = 0.0;
        conductivity = 0.0;
        nonReal = true;
    }

/**
     *Constructor de la clase Ground.  Genera un Ground a partir de un vector de String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param row  Texto obtenido en el archivo de salida del script nec2++
     */
    public Ground(String[] row) {
        type = Integer.valueOf(row[0]);
        radialWires = Integer.valueOf(row[1]);
        empty1 = Integer.valueOf(row[2]);
        empty2 = Integer.valueOf(row[3]);
        relDielConst = Double.valueOf(row[4]);
        conductivity = Double.valueOf(row[5]);
    }

/**
     *Convierte el objeto Ground a un comando interpretable por NEC
     * @param grounds Arreglo de objetos de la clase grounds
     * @return Comando de Tipo de Tierra de NEC
     */
    public static ArrayList<String> toString(ArrayList<Ground> grounds) {
        ArrayList<String> resp = new ArrayList<String>();
        String nGround = "";
        for (Ground ground : grounds) {
            if (!ground.nonReal) {
                nGround = "GN "
                        + ground.getType() + ","
                        + ground.getRadialWires() + ","
                        + ground.getEmpty1() + ","
                        + ground.getEmpty2() + ","
                        + ground.getRelDielConst() + ","
                        + ground.getConductivity() + ",";
                resp.add(nGround);
            } else {
                nGround = "GN "
                        + ground.getType() + ",";
                resp.add(nGround);
            }
        }
        return resp;
    }

    /**
     *Convierte el comando de tipo de tierra de NEC a un objeto Ground
     * @param line Comando de tipo de tierra de NEC
     * @return Objeto Ground 
     */
    public static Ground fromString(String line) {
        Ground resp = new Ground();

        String[] ln = line.split(",");

        if (ln.length == 6) {
            String[] firstLn = ln[0].split("\\s+");
            resp.setType(Integer.valueOf(firstLn[1]));
            resp.setRadialWires(Integer.valueOf(ln[1]));
            resp.setEmpty1(0);
            resp.setEmpty2(0);
            resp.setRelDielConst(Double.valueOf(ln[4]));
            resp.setConductivity(Double.valueOf(ln[5]));
        } else {
            String[] firstLn = ln[0].split("\\s+");
            resp.setType(Integer.valueOf(firstLn[1]));
        }
        
        return resp;
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

    /**
     * @return the radialWires
     */
    public int getRadialWires() {
        return radialWires;
    }

    /**
     * @param radialWires the radialWires to set
     */
    public void setRadialWires(int radialWires) {
        this.radialWires = radialWires;
    }

    /**
     * @return the empty1
     */
    public int getEmpty1() {
        return empty1;
    }

    /**
     * @param empty1 the empty1 to set
     */
    public void setEmpty1(int empty1) {
        this.empty1 = empty1;
    }

    /**
     * @return the empty2
     */
    public int getEmpty2() {
        return empty2;
    }

    /**
     * @param empty2 the empty2 to set
     */
    public void setEmpty2(int empty2) {
        this.empty2 = empty2;
    }

    /**
     * @return the relDielConst
     */
    public double getRelDielConst() {
        return relDielConst;
    }

    /**
     * @param relDielConst the relDielConst to set
     */
    public void setRelDielConst(double relDielConst) {
        this.relDielConst = relDielConst;
    }

    /**
     * @return the conductivity
     */
    public double getConductivity() {
        return conductivity;
    }

    /**
     * @param conductivity the conductivity to set
     */
    public void setConductivity(double conductivity) {
        this.conductivity = conductivity;
    }

    /**
     * @return the nonReal
     */
    public boolean isNonReal() {
        return nonReal;
    }

    /**
     * @param nonReal the nonReal to set
     */
    public void setNonReal(boolean nonReal) {
        this.nonReal = nonReal;
    }
}
