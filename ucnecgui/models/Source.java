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
public class Source {

    private int sourceTypeId;
    private int sourceSeg;
    private long segPercentage;
    private int i4;
    private double sourceAmplitudeRE;
    private double sourceAmplitudeIM;
    private int percentage;
    private double inputPercentage;

    /**
     * Constructor de la clase Source
     */
    public Source() {
        sourceTypeId = 0;
        sourceSeg = 0;
        percentage = 0;
        segPercentage = 0;
        i4 = 0;
        sourceAmplitudeRE = 0.0;
        sourceAmplitudeIM = 0.0;
    }

    /**
     * Constructor de la clase Source. Genera un Source a partir de un vector de
     * String generalmente se trata del texto obtenido en el archivo de salida
     * del script nec2++
     *
     * @param row Texto obtenido en el archivo de salida del script nec2++
     */
    public Source(String[] row) {
        sourceTypeId = Integer.valueOf(row[0]);
        sourceSeg = Integer.valueOf(row[1]);
        percentage = Integer.valueOf(row[2]);
        i4 = Integer.valueOf(row[3]);
        sourceAmplitudeRE = Double.valueOf(row[4]);
        sourceAmplitudeIM = Double.valueOf(row[5]);
    }

    /**
     * @return the sourceTypeId
     */
    public int getSourceTypeId() {
        return sourceTypeId;
    }

    /**
     * @param sourceTypeId the sourceTypeId to set
     */
    public void setSourceTypeId(int sourceTypeId) {
        this.sourceTypeId = sourceTypeId;
    }

    /**
     * @return the sourceSeg
     */
    public int getSourceSeg() {
        return sourceSeg;
    }

    /**
     * @param sourceSeg the sourceSeg to set
     */
    public void setSourceSeg(int sourceSeg) {
        this.sourceSeg = sourceSeg;
    }

    /**
     * @return the percentage
     */
    public int getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the segPercentage
     */
    public long getSegPercentage() {
        return segPercentage;
    }

    /**
     * @param segPercentage the segPercentage to set
     */
    public void setSegPercentage(long segPercentage) {
        this.segPercentage = segPercentage;
    }

    /**
     * @return the i4
     */
    public int getI4() {
        return i4;
    }

    /**
     * @param i4 the i4 to set
     */
    public void setI4(int i4) {
        this.i4 = i4;
    }

    /**
     * @return the sourceAmplitudeRE
     */
    public double getSourceAmplitudeRE() {
        return sourceAmplitudeRE;
    }

    /**
     * @param sourceAmplitudeRE the sourceAmplitudeRE to set
     */
    public void setSourceAmplitudeRE(double sourceAmplitudeRE) {
        this.sourceAmplitudeRE = sourceAmplitudeRE;
    }

    /**
     * @return the sourceAmplitudeIM
     */
    public double getSourceAmplitudeIM() {
        return sourceAmplitudeIM;
    }

    /**
     * @param sourceAmplitudeIM the sourceAmplitudeIM to set
     */
    public void setSourceAmplitudeIM(double sourceAmplitudeIM) {
        this.sourceAmplitudeIM = sourceAmplitudeIM;
    }

    /**
     * Convierte el objeto Source a un comando interpretable por NEC
     *
     * @param sources Arreglo de objetos de la clase Source
     * @return Comando de fuentes de NEC
     */
    public static ArrayList<String> toString(ArrayList<Source> sources) {
        ArrayList<String> resp = new ArrayList<String>();
        String nSource = "";
        for (Source source : sources) {
            nSource = "EX "
                    + source.getSourceTypeId() + ","
                    + source.getSourceSeg() + ","
                    + source.getSegPercentage() + ","
                    + source.getI4() + ","
                    + source.getSourceAmplitudeRE() + ","
                    + source.getSourceAmplitudeIM();
            resp.add(nSource);
        }
        return resp;
    }

    /**
     * Convierte el comando de fuentes de NEC a un objeto Source
     *
     * @param line Comando de fuentes de NEC
     * @return Objeto Source
     */
    public static Source fromString(String line) {
        Source resp = new Source();
        String[] ln = line.split(",");

        String[] firstLn = ln[0].split("\\s+");
        int sourceTypeId = Integer.valueOf(firstLn[1]);
        resp.setSourceTypeId(sourceTypeId);
        resp.setSourceSeg(Integer.valueOf(ln[1]));
        resp.setSegPercentage(Long.valueOf(ln[2]));
        resp.setI4(0);
        resp.setSourceAmplitudeRE(Double.valueOf(ln[4]));
        resp.setSourceAmplitudeIM(Double.valueOf(ln[5]));
        return resp;
    }

    /**
     * Convierte el comando de fuentes de NEC a un objeto Source, Cambiando
     *el valor del segmento de alambre  relacionado  según lo indicado por factor (En
     * caso de encontrarse otros alambres cargados en el entorno al momento de
     * invocar este método)
     *
     * @param line Comando de fuentes de NEC
     * @param formerSourceTag Antigua etiqueta del alambre relacionado al segmento
     * @param newSourceTag Nueva etiqueta del alambre relacionado al segmento
     * @return Objeto Source
     */
    public static Source importFromString(String line, int formerSourceTag, int newSourceTag) {
        Source resp = new Source();
        String[] ln = line.split(",");

        String[] firstLn = ln[0].split("\\s+");
        int sourceTypeId = Integer.valueOf(firstLn[1]);
        resp.setSourceTypeId(sourceTypeId);
        resp.setSourceSeg(Integer.valueOf(ln[1]));
        resp.setSegPercentage(Long.valueOf(ln[2]));
        resp.setI4(0);
        resp.setSourceAmplitudeRE(Double.valueOf(ln[4]));
        resp.setSourceAmplitudeIM(Double.valueOf(ln[5]));
        if (resp.getSourceSeg() == formerSourceTag) {
            resp.setSourceSeg(newSourceTag);
        }
        return resp;
    }
}
