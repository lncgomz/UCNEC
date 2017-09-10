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

public class Network {

    private int tagNumberPort1;
    private int port1Segment;
    private int tagNumberPort2;
    private long port2Segment;
    private double realg11;
    private double img11;
    private double realg12;
    private double img12;
    private double realg22;
    private double img22;

    /**
     * Constructor de la clase Network
     */
    public Network() {
        this.tagNumberPort1 = 0;
        this.port1Segment = 0;
        this.tagNumberPort2 = 0;
        this.port2Segment = 0;
        this.realg11 = 0.0;
        this.img11 = 0.0;
        this.realg12 = 0.0;
        this.img12 = 0.0;
        this.realg22 = 0.0;
        this.img22 = 0.0;
    }

    /**
     *Convierte el objeto Network a un comando interpretable por NEC
     * @param networks Arreglo de objetos de la clase Networks
     * @return Comando de redes de NEC
     */
    public static ArrayList<String> toString(ArrayList<Network> networks) {
        ArrayList<String> resp = new ArrayList<String>();
        String nNetwork = "";
        for (Network network : networks) {
            nNetwork = "NT "
                    + network.getTagNumberPort1() + ","
                    + network.getPort1Segment() + ","
                    + network.getTagNumberPort2() + ","
                    + network.getPort2Segment() + ","
                    + network.getRealg11() + ","
                    + network.getImg11() + ","
                    + network.getRealg12() + ","
                    + network.getImg12() + ","
                    + network.getRealg22() + ","
                    + network.getImg22();
            resp.add(nNetwork);
        }
        return resp;
    }
    
    /**
     *Constructor de la clase Network.  Genera un Network a partir de un  String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param line  Texto obtenido en el archivo de salida del script nec2++
     * @return Objeto Network
     */
    public static Network fromString (String line){
        
        String[] ln = line.split(",");
        Network network = new Network();
        String[] firstLn = ln[0].split("\\s+");
        network.setTagNumberPort1(Integer.valueOf(firstLn[1]));
        network.setPort1Segment(Integer.valueOf(ln[1]));
        network.setTagNumberPort2(Integer.valueOf(ln[2]));
        network.setPort2Segment(Integer.valueOf(ln[3]));
        network.setRealg11(Double.valueOf(ln[4]));
        network.setImg11(Double.valueOf(ln[5]));
        network.setRealg12(Double.valueOf(ln[6]));
        network.setImg12(Double.valueOf(ln[7]));
        network.setRealg22(Double.valueOf(ln[8]));
        network.setImg22(Double.valueOf(ln[9]));        
        return network;
    }
    
    /**
     *Convierte el comando de redes de NEC a un objeto Network, incrementando la etiqueta del puerto 1 correspondiente
     * según lo indicado por factor (En caso de encontrarse otros alambres cargados en el entorno al momento de invocar este 
     * método)
     * @param line Comando de redes de NEC
     * @param factor Factor de incremento
     * @return Objeto Network 
     */
    public static Network importFromString (String line, int factor){
        
        String[] ln = line.split(",");
        Network network = new Network();
        String[] firstLn = ln[0].split("\\s+");
        network.setTagNumberPort1(Integer.valueOf(firstLn[1]) + factor);
        network.setPort1Segment(Integer.valueOf(ln[1]));
        network.setTagNumberPort2(Integer.valueOf(ln[2]) + factor);
        network.setPort2Segment(Integer.valueOf(ln[3]));
        network.setRealg11(Double.valueOf(ln[4]));
        network.setImg11(Double.valueOf(ln[5]));
        network.setRealg12(Double.valueOf(ln[6]));
        network.setImg12(Double.valueOf(ln[7]));
        network.setRealg22(Double.valueOf(ln[8]));
        network.setImg22(Double.valueOf(ln[9]));        
        return network;
    }

    /**
     * @return the tagNumberPort1
     */
    public int getTagNumberPort1() {
        return tagNumberPort1;
    }

    /**
     * @param tagNumberPort1 the tagNumberPort1 to set
     */
    public void setTagNumberPort1(int tagNumberPort1) {
        this.tagNumberPort1 = tagNumberPort1;
    }

    /**
     * @return the port1Segment
     */
    public int getPort1Segment() {
        return port1Segment;
    }

    /**
     * @param port1Segment the port1Segment to set
     */
    public void setPort1Segment(int port1Segment) {
        this.port1Segment = port1Segment;
    }

    /**
     * @return the tagNumberPort2
     */
    public int getTagNumberPort2() {
        return tagNumberPort2;
    }

    /**
     * @param tagNumberPort2 the tagNumberPort2 to set
     */
    public void setTagNumberPort2(int tagNumberPort2) {
        this.tagNumberPort2 = tagNumberPort2;
    }

    /**
     * @return the port2Segment
     */
    public long getPort2Segment() {
        return port2Segment;
    }

    /**
     * @param port2Segment the port2Segment to set
     */
    public void setPort2Segment(long port2Segment) {
        this.port2Segment = port2Segment;
    }

    /**
     * @return the realg11
     */
    public double getRealg11() {
        return realg11;
    }

    /**
     * @param realg11 the realg11 to set
     */
    public void setRealg11(double realg11) {
        this.realg11 = realg11;
    }

    /**
     * @return the img11
     */
    public double getImg11() {
        return img11;
    }

    /**
     * @param img11 the img11 to set
     */
    public void setImg11(double img11) {
        this.img11 = img11;
    }

    /**
     * @return the realg12
     */
    public double getRealg12() {
        return realg12;
    }

    /**
     * @param realg12 the realg12 to set
     */
    public void setRealg12(double realg12) {
        this.realg12 = realg12;
    }

    /**
     * @return the img12
     */
    public double getImg12() {
        return img12;
    }

    /**
     * @param img12 the img12 to set
     */
    public void setImg12(double img12) {
        this.img12 = img12;
    }

    /**
     * @return the realg22
     */
    public double getRealg22() {
        return realg22;
    }

    /**
     * @param realg22 the realg22 to set
     */
    public void setRealg22(double realg22) {
        this.realg22 = realg22;
    }

    /**
     * @return the img22
     */
    public double getImg22() {
        return img22;
    }

    /**
     * @param img22 the img22 to set
     */
    public void setImg22(double img22) {
        this.img22 = img22;
    }
}
