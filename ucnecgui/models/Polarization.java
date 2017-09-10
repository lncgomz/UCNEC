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

public class Polarization {

    private double ra_times;
    private double ra_db;
    private double elipticityRatio;
    private double elipticityCoeff;
    private double mcp;
    private double mcc;
    private double cpr;

    /**
     *Constructor de la clase Polarization. Almacena los valores de los cálculos relacionados al diagrama de polarización
     */
    public Polarization() {
        ra_times = 0;
        ra_db = 0;
        elipticityRatio = 0;
        elipticityCoeff = 0;
        mcp = 0;
        mcc = 0;
        cpr = 0;
    }

    /**
     * @return the ra_times
     */
    public double getRa_times() {
        return ra_times;
    }

    /**
     * @param ra_times the ra_times to set
     */
    public void setRa_times(double ra_times) {
        this.ra_times = ra_times;
    }

    /**
     * @return the ra_db
     */
    public double getRa_db() {
        return ra_db;
    }

    /**
     * @param ra_db the ra_db to set
     */
    public void setRa_db(double ra_db) {
        this.ra_db = ra_db;
    }

    /**
     * @return the elipticityRatio
     */
    public double getElipticityRatio() {
        return elipticityRatio;
    }

    /**
     * @param elipticityRatio the elipticityRatio to set
     */
    public void setElipticityRatio(double elipticityRatio) {
        this.elipticityRatio = elipticityRatio;
    }

    /**
     * @return the elipticityCoeff
     */
    public double getElipticityCoeff() {
        return elipticityCoeff;
    }

    /**
     * @param elipticityCoeff the elipticityCoeff to set
     */
    public void setElipticityCoeff(double elipticityCoeff) {
        this.elipticityCoeff = elipticityCoeff;
    }

    /**
     * @return the mcp
     */
    public double getMcp() {
        return mcp;
    }

    /**
     * @param mcp the mcp to set
     */
    public void setMcp(double mcp) {
        this.mcp = mcp;
    }

    /**
     * @return the mcc
     */
    public double getMcc() {
        return mcc;
    }

    /**
     * @param mcc the mcc to set
     */
    public void setMcc(double mcc) {
        this.mcc = mcc;
    }

    /**
     * @return the cpr
     */
    public double getCpr() {
        return cpr;
    }

    /**
     * @param cpr the cpr to set
     */
    public void setCpr(double cpr) {
        this.cpr = cpr;
    }
}
