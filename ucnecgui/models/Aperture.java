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

public class Aperture {
    private double theta1;
    private double theta2;
    private double phi1;
    private double phi2;
    private double hAperture;
    private double vAperture;
    private double thetaAmplitude;
    private double phiAmplitude;
    private double maxMagTheta;
    private double maxMagPhi;
    private double maxMagAmp;
    
    /**
     *Constructor de la clase Aperture. Genera un objeto Aperture vacío
     */
    public Aperture(){ 
        this.theta1 = 0;
        this.theta2 = 0;
        this.phi1 = 0;
        this.phi2 = 0;
        this.thetaAmplitude = 0;
        this.phiAmplitude = 0;
    }

    /**
     * @return the theta1
     */
    public double getTheta1() {
        return theta1;
    }

    /**
     * @param theta1 the theta1 to set
     */
    public void setTheta1(double theta1) {
        this.theta1 = theta1;
    }

    /**
     * @return the theta2
     */
    public double getTheta2() {
        return theta2;
    }

    /**
     * @param theta2 the theta2 to set
     */
    public void setTheta2(double theta2) {
        this.theta2 = theta2;
    }

    /**
     * @return the phi1
     */
    public double getPhi1() {
        return phi1;
    }

    /**
     * @param phi1 the phi1 to set
     */
    public void setPhi1(double phi1) {
        this.phi1 = phi1;
    }

    /**
     * @return the phi2
     */
    public double getPhi2() {
        return phi2;
    }

    /**
     * @param phi2 the phi2 to set
     */
    public void setPhi2(double phi2) {
        this.phi2 = phi2;
    }

    /**
     * @return the hAperture
     */
    public double gethAperture() {
        return Math.abs(this.phi1 - this.phi2);
    }

    /**
     * @param hAperture the hAperture to set
     */
    public void sethAperture(double hAperture) {
        this.hAperture = gethAperture();
    }

    /**
     * @return the vAperture
     */
    public double getvAperture() {
        return Math.abs(this.theta1 - this.theta2);
    }

    /**
     * @param vAperture the vAperture to set
     */
    public void setvAperture(double vAperture) {
        this.vAperture = getvAperture();
    }

    /**
     * @return the thetaAmplitude
     */
    public double getThetaAmplitude() {
        return thetaAmplitude;
    }

    /**
     * @param thetaAmplitude the thetaAmplitude to set
     */
    public void setThetaAmplitude(double thetaAmplitude) {
        this.thetaAmplitude = thetaAmplitude;
    }

    /**
     * @return the phiAmplitude
     */
    public double getPhiAmplitude() {
        return phiAmplitude;
    }

    /**
     * @param phiAmplitude the phiAmplitude to set
     */
    public void setPhiAmplitude(double phiAmplitude) {
        this.phiAmplitude = phiAmplitude;
    }

    /**
     * @return the maxMagTheta
     */
    public double getMaxMagTheta() {
        return maxMagTheta;
    }

    /**
     * @param maxMagTheta the maxMagTheta to set
     */
    public void setMaxMagTheta(double maxMagTheta) {
        this.maxMagTheta = maxMagTheta;
    }

    /**
     * @return the maxMagPhi
     */
    public double getMaxMagPhi() {
        return maxMagPhi;
    }

    /**
     * @param maxMagPhi the maxMagPhi to set
     */
    public void setMaxMagPhi(double maxMagPhi) {
        this.maxMagPhi = maxMagPhi;
    }

    /**
     * @return the maxMagAmp
     */
    public double getMaxMagAmp() {
        return maxMagAmp;
    }

    /**
     * @param maxMagAmp the maxMagAmp to set
     */
    public void setMaxMagAmp(double maxMagAmp) {
        this.maxMagAmp = maxMagAmp;
    }
}
