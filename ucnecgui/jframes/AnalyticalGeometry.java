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
import java.awt.MenuBar;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import ucnecgui.Global;
import ucnecgui.jpanels.GeneratorPanel;
import ucnecgui.jpanels.TransformationPanel;

/**
 *
 * @author Leoncio Gómez
 */
public class AnalyticalGeometry extends JFrame {

    public final static int TRANSFORMATION = 0;
    public final static int GENERATION = 1;
    public Container content;
    
    /**
     * Constructor de la clase AnalyticalGeometry, gestiona las herramientas de Transformación de Coordenadas y Generador de Figuras
     * @param h Altura de la ventana
     * @param w Ancho de la ventana
     * @param global Objeto de la clase Global
     * @param selectedWires Índice del alambre seleccionado
     * @param type Selector de Herramienta (Transformación de Coordenadas o Generador de Figuras)
     * @param selected Objeto de la clase JComboBox correspondiente al selector de alambres
     */
    public AnalyticalGeometry(int h, int w, Global global, int[] selectedWires, int type, JComboBox selected) {
        super("UCNEC");
        setMenuBar(new MenuBar());
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Images/logo.png")));
        setSize(h, w);
        setResizable(false);
        content = getContentPane();
        setLocationRelativeTo(null);
        content.setLayout(new BorderLayout());
        switch (type) {
            case AnalyticalGeometry.TRANSFORMATION:
                TransformationPanel transformation = new TransformationPanel(global, selectedWires, this, selected);
                content.add(transformation);
                break;
            case AnalyticalGeometry.GENERATION:
                GeneratorPanel generation = new GeneratorPanel(global, selectedWires, this, selected);
                content.add(generation);
                break;
            default:
                throw new AssertionError();
        }
    }
}
