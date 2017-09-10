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

package controllers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import ucnecgui.Global;
import ucnecgui.models.AntInputLine;
import ucnecgui.models.RPLine;
import ucnecgui.models.SWR;
import ucnecgui.models.Source;

/**
 *
 * @author Leoncio Gómez
 */

public class NECParser {

    /**
     * Captura los valores geométricos presentes en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una lista de objetos de tipo String
     * @param data Archivo de salida generado por el script nec2++
     * @return Lista de Objetos String con los valores geométricos de la simulación
     */
    public static ArrayList<String> getWires(String[] data) {

        ArrayList<String> aux = new ArrayList();
        ArrayList<String> resp = new ArrayList();
        int inicio = 0;
        int fin = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].contains("STRUCTURE SPECIFICATION")) { //Busca esta cadena de texto en el archivo , la cual indica el inicio de la descripción geométrica
                inicio = i;
            } else if (data[i].contains("TOTAL SEGMENTS USED")) { //Busca esta cadena de texto en el archivo , la cual indica el fin de la descripción geométrica
                fin = i;
                break;
            }
        }

        for (int i = inicio; i < fin; i++) {
            aux.add(data[i]);
        }

        String[] aux2 = null;
        for (int i = 0; i < aux.size(); i++) {
            aux2 = aux.get(i).split("\\s+");
            if (aux2.length > 1 && isInteger(aux2[1])) {
                resp.add(aux.get(i));
            }
        }
        return resp;
    }

    /**
     * Busca mensajes de error en el archivo salida.nec (Generado por el script nec2++) para determinar si la
     * simulación fue ejecutada exitosamente.
     * @param global Ob
     * @return true si la ejecución fue exitosa, en caso contrario, false.     * 
     */
    public static boolean isSimulationOK(Global global) {
        ArrayList<String> data = Global.outputErrorData;
        String errorMsg = "";
        
        for (String line : data) {
            errorMsg = errorMsg + line + System.lineSeparator();
        }

        if (errorMsg.isEmpty()) {
            return true;
        } else {
            global.errorSimMessage("Messages.simulationErrorTitle", errorMsg);
            return false;
        }
    }

    /**
     * Captura los valores de corrientes  presentes en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una lista de objetos de tipo String
     * @param data Archivo de salida generado por el script nec2++
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores de corrientes de la simulación
     */
    public static ArrayList<String> getCurrents(ArrayList<String> data, Global global) {
        ArrayList<String> aux = new ArrayList();

        int begin = 0;
        int end = 0;
        int lineNumber = 0;
        boolean second = false;
        for (String line : data) {
            if (line.contains("DISTANCES IN WAVELENGTHS")) {  //Etiqueta para la descripción de las corrientes
                begin = lineNumber;
            } else if (begin != 0 && line.isEmpty()) {
                if (!second) {
                    second = true;
                } else {
                    end = lineNumber;
                    break;
                }
            }
            lineNumber++;
        }

        for (int index = begin; index < end; index++) {
            aux.add(data.get(index));
        }
        return aux;
    }
    
 /**
     * Captura la descripción de las fuentes  presentes en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una lista de objetos de tipo String
     * @param data Archivo de salida generado por el script nec2++
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores de fuentes de la simulación
     */
    public static ArrayList<String> getSources(ArrayList<String> data, Global global) {
        ArrayList<String> aux = new ArrayList();
        ArrayList<String> aux1 = new ArrayList();

        int begin = 0;
        int end = 0;
        int lineNumber = 0;
        if (global.getgNetwork().size() > 0) {
            for (String line : data) {
                                             
                if (line.contains("TAG   SEG       VOLTAGE (VOLTS)          CURRENT (AMPS)         IMPEDANCE (OHMS)       ADMITTANCE (MHOS)     POWER")) { //Etiqueta para la descripción de las fuentes de voltaje
                    begin = lineNumber;
                } else if (begin != 0 && line.isEmpty()) {
                    end = lineNumber;
                    break;
                }
                lineNumber++;
            }

            for (int index = begin; index < begin + 2; index++) {
                aux.add(data.get(index));
            }

            for (int index = begin + 2; index < end; index++) {
                String[] line = (data.get(index).trim()).split("\\s+");
                int tag = Integer.valueOf(line[0].trim());
                if (tag != global.getCurrentSourceTag()) {
                    aux.add(data.get(index));
                }
            }
        }

        lineNumber = 0;
        begin = 0;
        end = 0;

        for (String line : data) {
                                         
            if (line.contains("TAG   SEG       VOLTAGE (VOLTS)         CURRENT (AMPS)         IMPEDANCE (OHMS)        ADMITTANCE (MHOS)     POWER")) {  //Etiqueta para la descripción de las fuentes de corriente
                begin = lineNumber;
            } else if (begin != 0 && line.isEmpty()) {
                end = lineNumber;
                break;
            }
            lineNumber++;
        }
        if (!(global.getgNetwork().size() > 0)) {
            for (int index = begin; index < begin + 2; index++) {
                aux.add(data.get(index));
            }
        }
        for (int index = begin + 2; index < end; index++) {
            String[] line = (data.get(index).trim()).split(",");
            int tag = Integer.valueOf(line[0].trim());
            if (tag != global.getCurrentSourceTag()) {
                aux.add(data.get(index));
            }
        }

        return aux;
    }

 /**
     * Captura la descripción de las cargas  presentes en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una lista de objetos de tipo String
     * @param data Archivo de salida generado por el script nec2++
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores de cargas de la simulación
     */
    public static ArrayList<String> getLoads(ArrayList<String> data, Global global) {
        ArrayList<String> aux = new ArrayList();

        int begin = 0;
        int end = 0;
        int lineNumber = 0;
        boolean second = false;
        for (String line : data) {
            if (line.contains("LOCATION        RESISTANCE  INDUCTANCE  CAPACITANCE     IMPEDANCE (OHMS)   CONDUCTIVITY  CIRCUIT")) { //Etiqueta para la descripción de las cargas
                begin = lineNumber;
            } else if (begin != 0 && line.isEmpty()) {
                if (!second) {
                    second = true;
                } else {
                    end = lineNumber;
                    break;
                }
            }
            lineNumber++;
        }

        for (int index = begin; index < end; index++) {
            aux.add(data.get(index));
        }
        return aux;
    }

 /**
     * Captura la descripción del presupuesto de potencia en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una lista de objetos de tipo String
     * @param data Archivo de salida generado por el script nec2++
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores del presupuesto de potencia de la simulación
     */
    public static ArrayList<String> getPowers(ArrayList<String> data, Global global) {
        ArrayList<String> aux = new ArrayList();

        int begin = 0;
        int end = 0;
        int lineNumber = 0;
        boolean second = false;
        for (String line : data) {
            if (line.contains("---------- POWER BUDGET ---------")) { //Etiqueta para la descripción del presupuesto de potencia
                begin = lineNumber;
            } else if (begin != 0 && line.isEmpty()) {
                end = lineNumber;
                break;
            }
            lineNumber++;
        }

        for (int index = begin; index < end; index++) {
            aux.add(data.get(index));
        }
        return aux;
    }

 /**
     * Captura la descripción del patrón de radiación  en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una lista de objetos de tipo String
     * @param data Archivo de salida generado por el script nec2++
     * @param global Objeto de la clase Global
     * @return Lista de Objetos String con los valores del patrón de radiación de la simulación
     */
    public static ArrayList<RPLine> getRP3D(ArrayList<String> data, Global global) {
        ArrayList<String> aux = new ArrayList();

        int begin = 0;
        int end = 0;
        int lineNumber = 0;
        for (String line : data) {
            if (line.contains("---- ANGLES -----     ----- POWER GAINS -----       ---- POLARIZATION ----   ---- E(THETA) ----    ----- E(PHI) ------")) { //Etiqueta para la descripción del patrón de radiación
                begin = lineNumber;
            } else if (begin != 0 && line.isEmpty()) {
                end = lineNumber;
                break;
            }
            lineNumber++;
        }

        for (int index = begin; index < end; index++) {
            aux.add(data.get(index));
        }

        String[] aux2 = null;
        ArrayList<RPLine> RPLines = new ArrayList<RPLine>();
        for (int i = 0; i < aux.size(); i++) {
            aux2 = aux.get(i).split(",");
            if (aux2.length >= 12) {
                RPLine nRpLine = new RPLine(aux2);
                RPLines.add(nRpLine);
            }
        }
        return RPLines;
    }

 /**
     * Captura la descripción de los parámetros de entrada de la simulación  en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una colección de objetos, cuyas clave son las frecuencias de estudio y sus valores son los parámetros de entrada
     * correspondientes. Este método de emplea para la obtención del SWR a diferentes frecuencias.
     * @param data Archivo de salida generado por el script nec2++
     * @param global Objeto de la clase Global
     * @param swr Objeto de la clase SWR
     * @return Colección de Objetos con los parámetros de entrada de la simulación, por cada frecuencia estudiada.
     */
    public static LinkedHashMap<Double, ArrayList<AntInputLine>> getAntennaInput(ArrayList<String> data, Global global, SWR swr) {
        ArrayList<Integer> nAntInputList = new ArrayList<Integer>();
        ArrayList<Double> freqs = getFrequency(global);
        LinkedHashMap<Double, ArrayList<AntInputLine>> resp = null;

        int begin = 0;
        int end = 0;
        int lineNumber = 0;
        for (String ln : data) {
            if (global.getRelatedNetwork(swr.getSrcIndex()) == -1) {
                if (ln.contains("TAG   SEG       VOLTAGE (VOLTS)         CURRENT (AMPS)         IMPEDANCE (OHMS)        ADMITTANCE (MHOS)     POWER")) {  //Etiqueta para la descripción de los parámetros de entrada de la simulación, sin incluir fuentes de corriente
                    nAntInputList.add(lineNumber);
                    begin = lineNumber;
                }
                lineNumber++;
            } else {
                if (ln.contains("TAG   SEG       VOLTAGE (VOLTS)          CURRENT (AMPS)         IMPEDANCE (OHMS)       ADMITTANCE (MHOS)     POWER")) { //Etiqueta para la descripción de los parámetros de entrada de la simulación, incluyendo fuentes de corriente
                    nAntInputList.add(lineNumber);
                    begin = lineNumber;
                }
                lineNumber++;
            }
        }

        resp = new LinkedHashMap<Double, ArrayList<AntInputLine>>();
        int nFreq = 0;
        for (Integer nAntInputIndx : nAntInputList) {
            Integer index = nAntInputIndx + 2;
            String line = data.get(index);
            ArrayList<AntInputLine> AntInputLines = new ArrayList<AntInputLine>();
            while (!line.isEmpty()) {
                if (global.getRelatedNetwork(swr.getSrcIndex()) == -1) { //Determina si la fuente encontrada es de voltaje
                    if (line.split(",").length == 11) {
                        AntInputLine nAntInputLine = new AntInputLine(line.split(","));
                        AntInputLines.add(nAntInputLine);
                    }
                    index++;
                    line = data.get(index);
                } else { //La fuente encontrada es de corriente
                    if ((line.trim()).split("\\s+").length == 11) {
                        AntInputLine nAntInputLine = new AntInputLine((line.trim()).split("\\s+"));
                        AntInputLines.add(nAntInputLine);
                    }
                    index++;
                    line = data.get(index);
                }
            }
            resp.put(freqs.get(nFreq), AntInputLines);
            nFreq++;
        }
        return resp;
    }

/**
     * Captura la descripción de los parámetros de entrada de la simulación  en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una colección de objetos, cuya clave es la frecuencia de estudio y su valor es el parámetro de entrada
     * correspondiente. 
     * @param data Archivo de salida generado por el script nec2++
     * @param global Objeto de la clase Global
     * @param sourceIndex Índice de la fuente de estudio, en caso de utilizarse más de una fuente en la simulación
     * @return Colección de Objetos con el parámetro de entrada de la simulación, para la fuente indicada.
     */
    public static LinkedHashMap<Double, ArrayList<AntInputLine>> getAntennaInput(ArrayList<String> data, Global global, int sourceIndex) {
        ArrayList<Integer> nAntInputList = new ArrayList<Integer>();
        ArrayList<Double> freqs = getFrequency(global);
        Source nSource = global.getgSource().get(sourceIndex);
        LinkedHashMap<Double, ArrayList<AntInputLine>> resp = null;
        int lineNumber = 0;
        for (String ln : data) {
            if (global.getRelatedNetwork(sourceIndex+1) == -1) {
                if (ln.contains("TAG   SEG       VOLTAGE (VOLTS)         CURRENT (AMPS)         IMPEDANCE (OHMS)        ADMITTANCE (MHOS)     POWER")) { //Etiqueta para la descripción de la fuente de voltaje
                    nAntInputList.add(lineNumber);
                }
                lineNumber++;
            } else {
                if (ln.contains("TAG   SEG       VOLTAGE (VOLTS)          CURRENT (AMPS)         IMPEDANCE (OHMS)       ADMITTANCE (MHOS)     POWER")) { //Etiqueta para la descripción de la fuentes de corriente
                    nAntInputList.add(lineNumber);
                }
                lineNumber++;
            }
        }
        resp = new LinkedHashMap<Double, ArrayList<AntInputLine>>();
        int nFreq = 0;
        for (Integer nAntInputIndx : nAntInputList) {
            Integer index = nAntInputIndx + 2;
            String line = data.get(index);
            ArrayList<AntInputLine> AntInputLines = new ArrayList<AntInputLine>();
            while (!line.isEmpty()) {
                if (global.getRelatedNetwork(sourceIndex+1) == -1) { //Determina si la fuente encontrada es de voltaje
                    if (line.split(",").length == 11) {
                        AntInputLine nAntInputLine = new AntInputLine(line.split(","));
                        if (nAntInputLine.getTagNumber() !=  global.getCurrentSourceTag()) {
                            AntInputLines.add(nAntInputLine);
                        }
                    }
                    index++;
                    line = data.get(index);
                } else { //La fuente encontrada es de corriente
                    if ((line.trim()).split("\\s+").length == 11) {
                        AntInputLine nAntInputLine = new AntInputLine((line.trim()).split("\\s+"));
                        if (nAntInputLine.getTagNumber() !=  global.getCurrentSourceTag()) {
                            AntInputLines.add(nAntInputLine);
                        }
                    }
                    index++;
                    line = data.get(index);
                }
            }
            resp.put(freqs.get(nFreq), AntInputLines);
            nFreq++;
        }

        return resp;
    }

/**
     * Captura la descripción de las frecuencias de simulación  en el archivo salida.nec generado por el script nec2++ a partir del
     * archivo entrada.nec. Estos son convertidos a una lista de objetos Double. Este método se emplea para obtener la cantidad  y el
     * valor de las frecuencias barridas al momento de ejecutar un análisis de SWR.
     * @param global Objeto de la clase Global
     * @return Arreglo de objetos Double con los valores de frecuencias de estudio.
     */
    public static ArrayList<Double> getFrequency(Global global) {
        ArrayList<String> data = global.getgData();
        ArrayList<Integer> starts = new ArrayList<Integer>();
        ArrayList<Double> frequencies = new ArrayList<Double>();

        int lineNumber = 0;
        for (String line : data) {
            if (line.contains("--------- FREQUENCY --------")) { //Etiqueta para la descripción de las frecuencias
                starts.add(lineNumber);
            }
            lineNumber++;
        }

        for (Integer indx : starts) {
            String[] freqRaw = data.get(indx + 1).split("=");
            String[] freqAux = (freqRaw[1].trim()).split("\\s+");
            double frequency = Double.valueOf(freqAux[0]);
            frequencies.add(frequency);
        }
        return frequencies;
    }

    /**
     *Determina si un objeto String constituye un número entero
     * @param s Texto a analizar
     * @return true si el parámetro s es un número entero, en caso contrario, devuelve false
     */
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     *Determina si un objeto String constituye un número de punto flotante
     * @param s Texto a analizar
     * @return true si el parámetro s es un número de punto flotante, en caso contrario, devuelve false
     */
    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
