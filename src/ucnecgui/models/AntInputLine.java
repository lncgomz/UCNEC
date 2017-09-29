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

public class AntInputLine {

    private int tagNumber;
    private double voltageReal;
    private double voltageImaginary;
    private double currentReal;
    private double currentImaginary;
    private double impedanceReal;
    private double impedanceImaginary;
    private double admitanceReal;
    private double admitanceImaginary;
    private double power;
    
    /**
     *Constructor de la clase AntInputLine. Genera un AntInputLine a partir de un vector de String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param line Texto obtenido en el archivo de salida del script nec2++
     */
    public AntInputLine(String[] line){
        this.tagNumber = Integer.valueOf(line[0].trim());
        this.voltageReal = Double.valueOf(line[2].trim());
        this.voltageImaginary = Double.valueOf(line[3].trim());
        this.currentReal = Double.valueOf(line[4].trim());
        this.currentImaginary = Double.valueOf(line[5].trim());
        this.impedanceReal = Double.valueOf(line[6].trim());
        this.impedanceImaginary = Double.valueOf(line[7].trim());
        this.admitanceReal = Double.valueOf(line[8].trim());
        this.admitanceImaginary = Double.valueOf(line[9].trim());
        this.power = Double.valueOf(line[10].trim());
    }

    /**
     *
     * @return the tagNumber
     */
    public int getTagNumber() {
        return tagNumber;
    }

    /**
     * @param tagNumber the tagNumber to set
     */
    public void setTagNumber(int tagNumber) {
        this.tagNumber = tagNumber;
    }

    /**
     * @return the voltageReal
     */
    public double getVoltageReal() {
        return voltageReal;
    }

    /**
     * @param voltageReal the voltageReal to set
     */
    public void setVoltageReal(double voltageReal) {
        this.voltageReal = voltageReal;
    }

    /**
     * @return the voltageImaginary
     */
    public double getVoltageImaginary() {
        return voltageImaginary;
    }

    /**
     * @param voltageImaginary the voltageImaginary to set
     */
    public void setVoltageImaginary(double voltageImaginary) {
        this.voltageImaginary = voltageImaginary;
    }

    /**
     * @return the currentReal
     */
    public double getCurrentReal() {
        return currentReal;
    }

    /**
     * @param currentReal the currentReal to set
     */
    public void setCurrentReal(double currentReal) {
        this.currentReal = currentReal;
    }

    /**
     * @return the currentImaginary
     */
    public double getCurrentImaginary() {
        return currentImaginary;
    }

    /**
     * @param currentImaginary the currentImaginary to set
     */
    public void setCurrentImaginary(double currentImaginary) {
        this.currentImaginary = currentImaginary;
    }

    /**
     * @return the impedanceReal
     */
    public double getImpedanceReal() {
        return impedanceReal;
    }

    /**
     * @param impedanceReal the impedanceReal to set
     */
    public void setImpedanceReal(double impedanceReal) {
        this.impedanceReal = impedanceReal;
    }

    /**
     * @return the impedanceImaginary
     */
    public double getImpedanceImaginary() {
        return impedanceImaginary;
    }

    /**
     * @param impedanceImaginary the impedanceImaginary to set
     */
    public void setImpedanceImaginary(double impedanceImaginary) {
        this.impedanceImaginary = impedanceImaginary;
    }

    /**
     * @return the admitanceReal
     */
    public double getAdmitanceReal() {
        return admitanceReal;
    }

    /**
     * @param admitanceReal the admitanceReal to set
     */
    public void setAdmitanceReal(double admitanceReal) {
        this.admitanceReal = admitanceReal;
    }

    /**
     * @return the admitanceImaginary
     */
    public double getAdmitanceImaginary() {
        return admitanceImaginary;
    }

    /**
     * @param admitanceImaginary the admitanceImaginary to set
     */
    public void setAdmitanceImaginary(double admitanceImaginary) {
        this.admitanceImaginary = admitanceImaginary;
    }

    /**
     * @return the power
     */
    public double getPower() {
        return power;
    }

    /**
     * @param power the power to set
     */
    public void setPower(double power) {
        this.power = power;
    }
}
