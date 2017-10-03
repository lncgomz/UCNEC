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

package ucnecgui.jpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JFrame;
import ucnecgui.Global;
import ucnecgui.models.Unit;

/**
 *
 * @author Leoncio Gómez
 */

public class UnitsPanel extends javax.swing.JPanel {

    private Global global;
    private JFrame frame;
    private int oldUnit;
    
    /**
     *Constructor de la clase UnitsPanel
     * @param global Objeto de la clase Global
     * @param frame Ventana que invoca a este panel
     */
    public UnitsPanel(Global global, JFrame frame) {
        initComponents();
        this.global = global;
        this.frame = frame;
        this.oldUnit = global.getCurrentUnit();
        initializePanel();
        loadGSCard();
    }

    /**
     *Inicializa los componentes del panel
     */
    public void initializePanel() {
        jRadioButton10.setText(Global.getMessages().
                getString("SimulationPanel.param.units.jradiobutton10"));
        jRadioButton11.setText(Global.getMessages().
                getString("SimulationPanel.param.units.jradiobutton11"));
        jRadioButton12.setText(Global.getMessages().
                getString("SimulationPanel.param.units.jradiobutton12"));
        jRadioButton13.setText(Global.getMessages().
                getString("SimulationPanel.param.units.jradiobutton13"));
        jRadioButton14.setText(Global.getMessages().
                getString("SimulationPanel.param.units.jradiobutton14"));
        okbtn.setText(Global.getMessages().
                getString("GT.ok"));
        cancelbtn.setText(Global.getMessages().
                getString("GT.cancel"));

       //Comportamiento del botón Metros
        jRadioButton10.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    global.changeUnit(0, global.getCurrentUnit());
                    global.setCurrentUnit(0);
                }
            }
        });

        //Comportamiento del botón Milímetros
        jRadioButton11.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    global.changeUnit(1, global.getCurrentUnit());
                    global.setCurrentUnit(1);
                }
            }
        });
         //Comportamiento del botón Pies
        jRadioButton12.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    global.changeUnit(2, global.getCurrentUnit());
                    global.setCurrentUnit(2);
                }
            }
        });
        
         //Comportamiento del botón Pulgadas
        jRadioButton13.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    global.changeUnit(3, global.getCurrentUnit());
                    global.setCurrentUnit(3);
                }
            }
        });

         //Comportamiento del botón Longitud de Onda
        jRadioButton14.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent event) {
                int state = event.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    global.changeUnit(4, global.getCurrentUnit());
                    global.setCurrentUnit(4);
                }
            }
        });
        
         //Comportamiento del botón Aceptar
        okbtn.addActionListener((ActionEvent e) -> {
            global.setgUnit(generateGSCard());
            frame.dispose();
        });

         //Comportamiento del botón Cancelar
        cancelbtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });

    }
    
    /**
     * Genera el comando NEC2 de tipo unidades a partir de los parámetros
     * introducidos en el panel
     *
     * @return Comando de unidades de NEC2
     */
    public Unit generateGSCard() {
        Unit unit = new Unit();
        double c = 299792458;
        if (jRadioButton10.isSelected()) {
            global.setCurrentUnit(0);
            unit.setUnit(0);
            return null;
        } else if (jRadioButton11.isSelected()) {
            global.setCurrentUnit(1);
            unit.setScale(0.001);
            unit.setUnit(1);
        } else if (jRadioButton12.isSelected()) {
            global.setCurrentUnit(2);
            unit.setScale(0.3048);
            unit.setUnit(2);
        } else if (jRadioButton13.isSelected()) {
            global.setCurrentUnit(3);
            unit.setScale(0.0254);
            unit.setUnit(3);
        } else {
            unit.setScale(global.getWavelength());
            global.setCurrentUnit(4);
            unit.setUnit(4);
        }        
        return unit;
    }

    /**
     * Convierte la información obtenida a partir de una descripción de tipo de
     * unidades en NEC2 a un objeto tipo Unit para
     * desplegar los valores correspondientes en los controles del panel
     */
    public void loadGSCard() {
        int currentUnit = global.getgUnit().getUnit();
        switch (currentUnit) {
            case 0:
                jRadioButton10.setSelected(true);
                break;
            case 1:
                jRadioButton11.setSelected(true);
            case 2:
                jRadioButton12.setSelected(true);
                break;
            case 3:
                jRadioButton13.setSelected(true);
                break;
            case 4:
                jRadioButton14.setSelected(true);
                break;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        jRadioButton13 = new javax.swing.JRadioButton();
        jRadioButton14 = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        okbtn = new javax.swing.JButton();
        cancelbtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(3, 2));

        jRadioButton10.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton10);
        jRadioButton10.setSelected(true);
        jRadioButton10.setText("jRadioButton10");
        jPanel5.add(jRadioButton10);

        jRadioButton11.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton11);
        jRadioButton11.setText("jRadioButton11");
        jPanel5.add(jRadioButton11);

        jRadioButton12.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton12);
        jRadioButton12.setText("jRadioButton12");
        jPanel5.add(jRadioButton12);

        jRadioButton13.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton13);
        jRadioButton13.setText("jRadioButton13");
        jPanel5.add(jRadioButton13);

        jRadioButton14.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(jRadioButton14);
        jRadioButton14.setText("jRadioButton14");
        jPanel5.add(jRadioButton14);

        add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 50));
        jPanel1.setLayout(new java.awt.GridLayout(1, 2));

        okbtn.setBackground(new java.awt.Color(255, 255, 255));
        okbtn.setForeground(new java.awt.Color(0, 0, 50));
        okbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        jPanel1.add(okbtn);

        cancelbtn.setBackground(new java.awt.Color(255, 255, 255));
        cancelbtn.setForeground(new java.awt.Color(0, 0, 50));
        cancelbtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102), 4));
        jPanel1.add(cancelbtn);

        add(jPanel1, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton cancelbtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton10;
    private javax.swing.JRadioButton jRadioButton11;
    private javax.swing.JRadioButton jRadioButton12;
    private javax.swing.JRadioButton jRadioButton13;
    private javax.swing.JRadioButton jRadioButton14;
    private javax.swing.JButton okbtn;
    // End of variables declaration//GEN-END:variables
}
