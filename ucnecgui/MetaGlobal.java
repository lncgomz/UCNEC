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

import ucnecgui.jframes.LabNEC;
import ucnecgui.jframes.NECFrame;
import ucnecgui.jpanels.NECModulePanel;

/**
 *
 * @author Leoncio Gómez
 */


public class MetaGlobal {
    private static NECFrame nf;
    private static LabNEC ln;
    private static NECModulePanel nmp;

    /**
     * @return the nf
     */
    public static NECFrame getNf() {
        return nf;
    }

    /**
     * @param aNf the nf to set
     */
    public static void setNf(NECFrame aNf) {
        nf = aNf;
    }

    /**
     * @return the ln
     */
    public static LabNEC getLn() {
        return ln;
    }

    /**
     * @param aLn the ln to set
     */
    public static void setLn(LabNEC aLn) {
        ln = aLn;
    }

    /**
     * @return the nmp
     */
    public static NECModulePanel getNmp() {
        return nmp;
    }

    /**
     * @param aNmp the nmp to set
     */
    public static void setNmp(NECModulePanel aNmp) {
        nmp = aNmp;
    }
}
