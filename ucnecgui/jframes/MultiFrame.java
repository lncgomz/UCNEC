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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import ucnecgui.Global;
import ucnecgui.jpanels.NECModulePanel;

/**
 *
 * @author Leoncio Gómez
 */
public class MultiFrame extends JFrame {

    public Container content;
    public NECModulePanel parent;

    /**
     * Constructor de la clase MultiFrame. Se encarga de mostrar la mayoría de
     * subventanas que utiliza el programa.
     *
     * @param h Altura de la ventana
     * @param w Ancho de la ventana
     * @param title Título de la ventana
     */
    public MultiFrame(int h, int w, String title) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setSize(h, w);
        setResizable(false);
        content = getContentPane();
        setLocationRelativeTo(null);
        content.setLayout(new BorderLayout());
    }

    /**
     * Constructor alternativo de la clase MultiFrame. Se encarga de mostrar las
     * subventanas del módulo UCNEC (Fuentes, Tierra, Pérdida de Alambres,
     * Patrón de Radiación, Geometría, Líneas de Transmisión, Unidades, etc)
     *
     * @param h Altura de la ventana
     * @param w Ancho de la ventana
     * @param title Título de la ventana
     * @param global Objeto de la clase Global
     * @param parent Objeto NECModulePanel que invoca a esta ventana
     */
    public MultiFrame(int h, int w, String title, Global global, NECModulePanel parent) {
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
                if (parent != null) {
                    parent.initializeInfo(global);
                }
            }

            @Override
            public void windowClosed(WindowEvent we) {
                if (parent != null) {
                    parent.initializeInfo(global);
                }
            }
        });
    }
}
