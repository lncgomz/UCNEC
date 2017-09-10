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
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import ucnecgui.Global;

/**
 *
 * @author Leoncio Gómez
 */
public class MultiLabFrame extends JFrame {

    public Container content;
    public Window parent;

    /**
     * Constructor de la clase MultiLabFrame.  Se encarga de mostrar las ventanas correspondientes a cada uno de los laboratorios de LabNEC.
     * @param h Altura de la ventana
     * @param w Ancho de la ventana
     * @param title Título de la ventana
     * @param global Objeto de la clase Global
     * @param parent Objeto NECModulePanel que invoca a esta ventana
     */
    public MultiLabFrame(int h, int w, String title, Global global, Window parent) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setSize(h, w);
        setResizable(false);
        content = getContentPane();
        setLocationRelativeTo(null);
        content.setLayout(new BorderLayout());
        this.parent = parent;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                parent.setVisible(true);
            }

            @Override
            public void windowClosed(WindowEvent we) {
                parent.setVisible(true);
            }
        });
    }
}
