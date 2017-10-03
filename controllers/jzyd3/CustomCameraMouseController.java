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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.camera.AbstractCameraController;
import org.jzy3d.chart.controllers.mouse.AWTMouseUtilities;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.maths.Coord2d;

/**
 *
 * @author Leoncio Gómez
 */
public class CustomCameraMouseController extends AbstractCameraController implements
        MouseListener, MouseWheelListener, MouseMotionListener {

    public CustomCameraMouseController() {
    }

    public CustomCameraMouseController(Chart chart) {
        register(chart);
        addSlaveThreadController(new CameraThreadController(chart));
    }

    @Override
    public void register(Chart chart) {
        super.register(chart);
        chart.getCanvas().addMouseController(this);
    }

    @Override
    public void dispose() {
        for (Chart chart : targets) {
            chart.getCanvas().removeMouseController(this);
        }
        super.dispose();
    }

    /**
     * Handles toggle between mouse rotation/auto rotation: double-click starts
     * the animated rotation, while simple click stops it.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //
        if (handleSlaveThread(e)) {
            return;
        }
        prevMouse.x = x(e);
        prevMouse.y = y(e);
    }

    /**
     * Compute shift or rotate
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Coord2d mouse = xy(e);

        // Rotate
        if (AWTMouseUtilities.isLeftDown(e)) {
            Coord2d move = mouse.sub(prevMouse).div(100);
            rotate(move);
        } // Shift
        else if (AWTMouseUtilities.isRightDown(e)) {
            Coord2d move = mouse.sub(prevMouse);
            if (move.y != 0) {
                shift(move.y / 500);
            }
        }
        prevMouse = mouse;
    }

    /**
     * Compute zoom
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        stopThreadController();
        float factor = 1 + (e.getWheelRotation() / 10.0f);
        zoomZ(factor);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

//        Coord2d prev = prevMouse;
//       Coord2d mouse = xy(e);
//       Coord2d newPos = new Coord2d(prev.x - mouse.x, mouse.y);
////         float offset = 0.1f;
////         mouse.x = mouse.x - offset; rotate( mouse );
//          //Coord2d move = (mouse.x).sub(e.);
//            rotate(newPos);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println(x(e) + "," + y(e));
        Coord2d move = new Coord2d();
        Coord2d released = new Coord2d(x(e), y(e));
        float offsetx;
        float offsety;

        float xdiff =Math.abs(released.x - prevMouse.x);
        float ydiff = Math.abs(released.y - prevMouse.y);

        if (released.x > prevMouse.x) {
            offsetx = 0.1f;
        } else {
            offsetx = -0.1f;
        }
        if (released.y > prevMouse.y) {
            offsety = 0.1f;
        } else {
            offsety = -0.1f;
        }

        if (xdiff > ydiff) {
            move.x = move.x + offsetx;
            move.y = move.y;
        } else {
            move.y = move.y + offsety;
            move.x = move.x;
        }
        rotate(move);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public boolean handleSlaveThread(MouseEvent e) {
        if (AWTMouseUtilities.isDoubleClick(e)) {
            if (threadController != null) {
                threadController.start();
                return true;
            }
        }
        if (threadController != null) {
            threadController.stop();
        }
        return false;
    }

    /* */
    public Coord2d xy(MouseEvent e) {
        return new Coord2d(x(e), y(e));
    }

    public int y(MouseEvent e) {
        return e.getY();
    }

    public int x(MouseEvent e) {
        return e.getX();
    }
}
