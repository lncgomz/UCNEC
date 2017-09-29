/*
 * Copyright (C) 2017 Mago
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import javax.imageio.ImageIO;
import ucnecgui.Global;

/**
 *
 * @author Leoncio Gómez
 */
public class QRGenerator {

    /**
     * Método para generar un código QR a partir de un arreglo de objetos String pasado como parámetro data.
     * @param path Ruta donde se guardará el código QR  como un archivo de imagen .png
     * @param data Texto a ser convertido a código QR
     * @param global Objeto de la clase Global
     * @throws IOException IOException
     */
    public static void generate(String path, ArrayList<String> data, Global global) throws IOException {
        
        String qrData = "";
        for (String string : data) {
            qrData = qrData + string + "\n";
        }
        String filePath = path;
        int size = 500;
        String fileType = "png";
        File myFile = new File(filePath);
        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            hintMap.put(EncodeHintType.MARGIN, 1);
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, size,
                    size, hintMap);
            int qrWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(qrWidth, qrWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, qrWidth, qrWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < qrWidth; i++) {
                for (int j = 0; j < qrWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, myFile);
            global.InfoMessages("Messages.generateQR.title", "Messages.generateQR.OK");
        } catch (WriterException e) {
            e.printStackTrace();
            global.InfoMessages("Messages.generateQR.title", "Messages.generateQR.FAULT");
        } catch (IOException e) {
            e.printStackTrace();
            global.InfoMessages("Messages.generateQR.title", "Messages.generateQR.FAULT");
        }
    }

}
