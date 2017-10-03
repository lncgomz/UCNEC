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
public class WireLoss {

    private int type;
    private int wireNumber;
    private double conductivity;
    private double permeability;

    /**
     * Constructor de la clase WireLoss
     */
    public WireLoss() {
        type = 0;
        wireNumber = 0;
        conductivity = 0.0;
        permeability = 0.0;
    }

    /**
     * Constructor de la clase WireLoss. Genera un WireLoss a partir de un
     * vector de String generalmente se trata del texto obtenido en el archivo
     * de salida del script nec2++
     *
     * @param row Texto obtenido en el archivo de salida del script nec2++
     */
    public WireLoss(String[] row) {
        type = Integer.valueOf(row[1]);
        wireNumber = Integer.valueOf(row[2]);
        conductivity = Double.valueOf(row[5]);
        permeability = Double.valueOf(row[6]);
    }

    /**
     * Convierte el objeto WireLoss a un comando interpretable por NEC
     *
     * @param loads  Arreglo de objetos de la clase WireLoss
     * @return Comando de cargas de NEC. El mismo gestiona las pérdidas en los
     * alambres que intervienen en la geometría de la simulación
     */
    public static ArrayList<String> toString(ArrayList<WireLoss> loads) {

        ArrayList<String> resp = new ArrayList<String>();
        String nWireLoss = "";

        for (WireLoss load : loads) {
            nWireLoss = "LD "
                    + load.getType() + ","
                    + load.getWireNumber() + ","
                    + "0" + ","
                    + "0" + ","
                    + load.getConductivity() + ","
                    + load.getPermeability();
            resp.add(nWireLoss);
        }
        return resp;
    }

    /**
     *Convierte el comando especial de cargas de NEC a un objeto WireLoss
     * @param line Comando especial de cargas de NEC
     * @return Objeto WireLoss 
     */
    public static WireLoss fromString(String line) {

        WireLoss resp = new WireLoss();
        String[] ln = line.split(",");
        String[] firstLn = ln[0].split("\\s+");
        int loadType = Integer.valueOf(firstLn[1]);
        resp.setType(loadType);
        resp.setWireNumber(Integer.valueOf(ln[1]));
        resp.setConductivity(Double.valueOf(ln[4]));
        resp.setPermeability(Double.valueOf(ln[5]));

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
     * @return the wireNumber
     */
    public int getWireNumber() {
        return wireNumber;
    }

    /**
     * @param wireNumber the wireNumber to set
     */
    public void setWireNumber(int wireNumber) {
        this.wireNumber = wireNumber;
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
     * @return the permeability
     */
    public double getPermeability() {
        return permeability;
    }

    /**
     * @param permeability the permeability to set
     */
    public void setPermeability(double permeability) {
        this.permeability = permeability;
    }
}
