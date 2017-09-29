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

package ucnecgui.jframes;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import ucnecgui.jpanels.Splash;

/**
 *
 * @author Leoncio Gómez
 */

public class Board extends JFrame {
    
    private static final long serialVersionUID = 1L;
    public Container content;
    public JFrame frame;

    /**
     * Constructor de la clase Board. Se encarga de mostrar la ventana Splash de la aplicación
     * @param h Altura de la ventana
     * @param w Ancho de la ventana
     */
    public Board(int h, int w) {
        super("UCNEC");
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setSize(h, w);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        content = getContentPane();
        content.setLayout(new BorderLayout());
        Splash splash = new Splash();
        content.add(splash);
        setLocationRelativeTo(null);
        setVisible(true);      
              
    }

/**
 * Genera un retardo de 4 segundos
 * @return  true luego de finalizar el retardo
 */
    public boolean hold(){
        
        try {
            Thread.sleep(4000);
            return true;
        } catch (InterruptedException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
}
