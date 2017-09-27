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
package controllers.jzyd3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.camera.AbstractCameraController;
import org.jzy3d.chart.controllers.keyboard.camera.ICameraKeyController;
import org.jzy3d.maths.Coord2d;

/**
 *
 * @author Leoncio Gómez
 */
public class CustomCameraKeyController extends AbstractCameraController implements KeyListener, ICameraKeyController {

    public CustomCameraKeyController() {
    }

    public CustomCameraKeyController(Chart chart) {
        register(chart);
    }

    @Override
    public void register(Chart chart) {
        super.register(chart);
        chart.getCanvas().addKeyController(this);
    }

    @Override
    public void dispose() {
        for (Chart c : targets) {
            c.getCanvas().removeKeyController(this);
        }

        super.dispose(); // i.e. target=null
    }

    /**
     * ******************************************************
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // rotation
        if (!e.isShiftDown()) {
            Coord2d move = new Coord2d();
            float offset = 0.1f;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    move.y = move.y + offset;
                    rotate(move);
                    break;
                case KeyEvent.VK_UP:
                    move.y = move.y - offset;
                    rotate(move);
                    break;
                case KeyEvent.VK_LEFT:
                    move.x = move.x - offset;
                    rotate(move);
                    break;
                case KeyEvent.VK_RIGHT:
                    move.x = move.x + offset;
                    rotate(move);
                    break;
                default:
                    break;
            }
        } // zoom
        else {
            switch (e.getKeyCode()) {
                // shift
                case KeyEvent.VK_DOWN:
                    shift(0.1f);
                    break;
                case KeyEvent.VK_UP:
                    shift(-0.1f);
                    break;
                // zoom
                case KeyEvent.VK_LEFT:
                    zoomX(0.9f);
                    zoomY(0.9f);
                    zoomZ(0.9f);
                    break;
                case KeyEvent.VK_RIGHT:
                    zoomX(1.1f);
                    zoomY(1.1f);
                    zoomZ(1.1f);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
