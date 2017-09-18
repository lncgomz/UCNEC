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
public class PowerBudget {

    private double inputPower;
    private double radiatedPower;
    private double structureLoss;
    private double networkLoss;
    private double efficiency;

    /**
     * Constructor de la clase PowerBudget
     */
    public PowerBudget() {
        inputPower = 0;
        radiatedPower = 0;
        structureLoss = 0;
        networkLoss = 0;
        efficiency = 0;
    }

    /**
     ** Constructor de la clase PowerBudget a partir de un arreglo de objetos de la clase Double
     * 
     * @param param Arreglo de objetos de la clase Double
     */
    public PowerBudget(ArrayList<Double> param) {
        if (param.size() == 5) {
            inputPower = param.get(0);
            radiatedPower = param.get(1);
            structureLoss = param.get(2);
            networkLoss = param.get(3);
            efficiency = param.get(4);
        }
    }

    /**
     * @return the inputPower
     */
    public double getInputPower() {
        return inputPower;
    }

    /**
     * @param inputPower the inputPower to set
     */
    public void setInputPower(double inputPower) {
        this.inputPower = inputPower;
    }

    /**
     * @return the radiatedPower
     */
    public double getRadiatedPower() {
        return radiatedPower;
    }

    /**
     * @param radiatedPower the radiatedPower to set
     */
    public void setRadiatedPower(double radiatedPower) {
        this.radiatedPower = radiatedPower;
    }

    /**
     * @return the structureLoss
     */
    public double getStructureLoss() {
        return structureLoss;
    }

    /**
     * @param structureLoss the structureLoss to set
     */
    public void setStructureLoss(double structureLoss) {
        this.structureLoss = structureLoss;
    }

    /**
     * @return the networkLoss
     */
    public double getNetworkLoss() {
        return networkLoss;
    }

    /**
     * @param networkLoss the networkLoss to set
     */
    public void setNetworkLoss(double networkLoss) {
        this.networkLoss = networkLoss;
    }

    /**
     * @return the efficiency
     */
    public double getEfficiency() {
        return efficiency;
    }

    /**
     * @param efficiency the efficiency to set
     */
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
