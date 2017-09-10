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

package ucnecgui;

import java.util.Locale;
import ucnecgui.jframes.Board;
import ucnecgui.jframes.MultiFrame;
import ucnecgui.jpanels.SelectModules;

/**
 *
 * @author Leoncio Gómez
 */
public class UCNECGUI {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { //Método principal de la aplicación
        Board obj = new Board(600, 600); 
        Locale.setDefault(Locale.US); //Esta configuración permite que el separador de decimales sea el punto (.) independientemente del SO
        boolean hold = obj.hold(); //Pausa de 4 segundos para visualización de ventana Splash
        if (hold) {
            obj.dispose();
            Global global = new Global();
            MultiFrame mf = new MultiFrame(800, 600, "UCNEC");            
            SelectModules sm = new SelectModules(global);
            mf.add(sm);
            mf.setVisible(true);
        }
    }
}
