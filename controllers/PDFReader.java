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

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;

/**
 *
 * @author Leoncio Gómez
 */

public class PDFReader {

    /**
     * Método para cargar un archivo pdf desde la carpeta docs en el directorio raíz de la aplicación
     * @param name Nombre del archivo pdf a abrir
     */
    public static void loadPdf(String name) {
        String path = PDFReader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String dir = new File(path).getParentFile().getPath() + File.separator + "docs" + File.separator + name;        
        String filePath = dir;

        SwingController controller = new SwingController();
        SwingViewBuilder factory = new SwingViewBuilder(controller);
        JPanel viewerComponentPanel = factory.buildViewerPanel();
        
        // agregar soporte para anotaciones interactivas con el ratón, vía callback
        controller.getDocumentViewController().setAnnotationCallback(
                new org.icepdf.ri.common.MyAnnotationCallback(
                        controller.getDocumentViewController()));
        
        //Configuración de la ventana donde aparecerá el documento pdf
        JFrame applicationFrame = new JFrame();        
        applicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        applicationFrame.getContentPane().add(viewerComponentPanel);

        //Abrir documento pdf cargado
        controller.openDocument(filePath);

        // Mostrar componente
        applicationFrame.pack();
        applicationFrame.setVisible(true);
    }
}
