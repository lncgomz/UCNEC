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

import java.util.LinkedHashMap;

/**
 *
 * @author Leoncio Gómez
 */

public class SWR {

    /**
     *
     */
    public double initFreq;
    private double finalFreq;
    private int stepFreq;
    private int srcIndex;
    private boolean useAltZ0;
    private double altZ0;
    private MinimunSWR minSWR;
    private double bw;
    private LinkedHashMap<Double, Double> data;

    /**
     *Constructor de la clase SWR
     */
    public SWR() {
        initFreq = 0;
        finalFreq = 0;
        srcIndex = 0;
        useAltZ0 = false;
        minSWR = new MinimunSWR(0, 0);
        bw = 0;
        data = new LinkedHashMap<Double, Double>();
    }

    /**
     * @return the initFreq
     */
    public double getInitFreq() {
        return initFreq;
    }

    /**
     * @param initFreq the initFreq to set
     */
    public void setInitFreq(double initFreq) {
        this.initFreq = initFreq;
    }

    /**
     * @return the finalFreq
     */
    public double getFinalFreq() {
        return finalFreq;
    }

    /**
     * @param finalFreq the finalFreq to set
     */
    public void setFinalFreq(double finalFreq) {
        this.finalFreq = finalFreq;
    }

    /**
     * @return the stepFreq
     */
    public int getStepFreq() {
        return stepFreq;
    }

    /**
     * @param stepFreq the stepFreq to set
     */
    public void setStepFreq(int stepFreq) {
        this.stepFreq = stepFreq;
    }

    /**
     * @return the srcIndex
     */
    public int getSrcIndex() {
        return srcIndex;
    }

    /**
     * @param srcIndex the srcIndex to set
     */
    public void setSrcIndex(int srcIndex) {
        this.srcIndex = srcIndex;
    }

    /**
     * @return the useAltZ0
     */
    public boolean isUseAltZ0() {
        return useAltZ0;
    }

    /**
     * @param useAltZ0 the useAltZ0 to set
     */
    public void setUseAltZ0(boolean useAltZ0) {
        this.useAltZ0 = useAltZ0;
    }

    /**
     * @return the altZ0
     */
    public double getAltZ0() {
        return altZ0;
    }

    /**
     * @param altZ0 the altZ0 to set
     */
    public void setAltZ0(double altZ0) {
        this.altZ0 = altZ0;
    }
    
     /**
     * @return the minSWR
     */
    public MinimunSWR getMinSWR() {
        return minSWR;
    }

    /**
     * @param minSWR the minSWR to set
     */
    public void setMinSWR(MinimunSWR minSWR) {
        this.minSWR = minSWR;
    }

    /**
     * @return the bw
     */
    public double getBw() {
        return bw;
    }

    /**
     * @param bw the bw to set
     */
    public void setBw(double bw) {
        this.bw = bw;
    }

    /**
     * @return the data
     */
    public LinkedHashMap<Double, Double> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(LinkedHashMap<Double, Double> data) {
        this.data = data;
    }

    /**
     *Clase pública anidada MinimumSWR
     */
    public class MinimunSWR {
        private double frequency;
        private double value;        
        
        /**
         * Constructor de la clase anidada MinimumSWR
         * @param freq Frecuencia de estudio
         * @param val Valor del SWR a la frecuencia de estudio
         */
        public MinimunSWR(double freq, double val){
            this.frequency = freq;
            this.value = val;
        }

        /**
         * @return the frequency
         */
        public double getFrequency() {
            return frequency;
        }

        /**
         * @param frequency the frequency to set
         */
        public void setFrequency(double frequency) {
            this.frequency = frequency;
        }

        /**
         * @return the value
         */
        public double getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(double value) {
            this.value = value;
        }        
    }  
}
