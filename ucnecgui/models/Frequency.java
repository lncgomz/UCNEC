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

public class Frequency {
    
    private int steppingType;
    private int steps;
    private double freq;
    private double freqIncrement;
    
    /**
     *Constructor de la clase Frequency
     */
    public Frequency() {
        steppingType = 0;
        steps = 0;
        freq = 0.0;
        freqIncrement = 0.0;
    }
    
    /**
     *Constructor de la clase Frequency.  Genera un Frequency a partir de un vector de String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param row  Texto obtenido en el archivo de salida del script nec2++
     */
    public Frequency(String[] row) {
        steppingType = Integer.valueOf(row[1]);
        steps = Integer.valueOf(row[2]);
        freq = Double.valueOf(row[4]);
        freqIncrement = Double.valueOf(row[5]);
    }
       
    /**
     *Convierte el objeto Frequency a un comando interpretable por NEC
     * @param frequency Objeto de la clase Frequency
     * @return Comando de frecuencia de NEC
     */
    public static String toString(Frequency frequency) {
        String resp = "";
        if (frequency.getFreq() > 0.0) {
            resp = "FR "
                    + frequency.getSteppingType() + ","
                    + frequency.getSteps() + ","
                    + "0,0,"
                    + frequency.getFreq();
        }
        return resp;
    }
    
    /**
     *Convierte el comando de frecuencia de NEC a un objeto Frequency
     * @param line Comando de frecuencia de NEC
     * @return Objeto frecuency 
     */
    public static Frequency fromString(String line) {
        Frequency resp = new Frequency();
        
        String[] ln = line.split(",");
        
        String[] firstLn = ln[0].split("\\s+");
        resp.setSteppingType(Integer.valueOf(firstLn[1]));
        resp.setSteps(Integer.valueOf(ln[1]));
        resp.setFreq(Double.valueOf(ln[4]));
        
        return resp;
    }
    
    /**
     *Convierte el objeto Frequency a un comando interpretable por NEC y que es usado para el barrido
     * de frecuencias requerido para el cálculo del SWR en un r ango de frecuencias
     * @param swr Objeto SWR
     * @return Comando interpretable por NEC, usado para el barrido
     * de frecuencias requerido para el cálculo del SWR en un r ango de frecuencias
     */
    public static String toSWRString(SWR swr) {
        String resp = "";
        double initFreq = swr.getInitFreq();
        int  stepFreq = Math.round((float) ((swr.getFinalFreq() - swr.getInitFreq())/swr.getStepFreq())); //Paso de ángulos de iteración
        double increment = swr.getStepFreq();
        
        resp = "FR "
                + 0 + ","
                + stepFreq + ","
                + "0,0,"
                + initFreq + ","
                + increment;
        return resp;
    }

    /**
     * @return the steppingType
     */
    public int getSteppingType() {
        return steppingType;
    }

    /**
     * @param steppingType the steppingType to set
     */
    public void setSteppingType(int steppingType) {
        this.steppingType = steppingType;
    }

    /**
     * @return the steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * @param steps the steps to set
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }

    /**
     * @return the freq
     */
    public double getFreq() {
        return freq;
    }

    /**
     * @param freq the freq to set
     */
    public void setFreq(double freq) {
        this.freq = freq;
    }

    /**
     * @return the freqIncrement
     */
    public double getFreqIncrement() {
        return freqIncrement;
    }

    /**
     * @param freqIncrement the freqIncrement to set
     */
    public void setFreqIncrement(double freqIncrement) {
        this.freqIncrement = freqIncrement;
    }
}
