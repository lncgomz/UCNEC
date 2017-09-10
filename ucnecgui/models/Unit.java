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

public class Unit {

    private double scale; 
    private int unit;

    /**
     *Constructor de la clase Unit
     */
    public Unit() {
        scale = 1;
    }

    /**
     *Constructor de la clase Unit.  Genera un Unit a partir de un vector de String
     * generalmente se trata del texto obtenido en el archivo de salida del script nec2++
     * @param row  Texto obtenido en el archivo de salida del script nec2++
     */
    public Unit(String[] row) {
        scale = Double.valueOf(row[0]);
    }
   
    /**
     *Convierte el objeto Unit a un comando interpretable por NEC
     * @param units Objeto de la clase Unit
     * @return Comando de unidades de NEC
     */
    public static String asString(Unit units){
        StringBuilder stringBuilder = new StringBuilder();
        
            stringBuilder.append("GS");            
            stringBuilder.append("   ");
            stringBuilder.append(",");
            stringBuilder.append("     ");
            stringBuilder.append(",");
            stringBuilder.append(String.valueOf(units.unit));
            stringBuilder.append(",");
            stringBuilder.append(" ");
            stringBuilder.append(",");
            stringBuilder.append(" ");
            stringBuilder.append(",");
            stringBuilder.append(" ");  
            stringBuilder.append(",");
            stringBuilder.append(" "); 
            stringBuilder.append(",");
            stringBuilder.append(" "); 
            stringBuilder.append(",");
            stringBuilder.append(" "); 
            stringBuilder.append(System.lineSeparator());
        
        return stringBuilder.toString();
    }
   
    /**
     *Convierte el comando de unidades de NEC a un objeto Unit
     * @param line Comando de unidades de NEC
     * @return Objeto Unit 
     */
    public static Unit fromString(String line){
       String[] ln = line.split(",");
       Unit unit = new Unit();
       unit.setUnit(Integer.valueOf(ln[2]));
       return unit;
   }

    /**
     * @return the scale
     */
    public double getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(double scale) {
        this.scale = scale;
    }

    /**
     * @return the unit
     */
    public int getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(int unit) {
        this.unit = unit;
    }  
}