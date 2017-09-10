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
import ucnecgui.models.SWR;

/**
 *
 * @author Leoncio Gómez
 */

public class SWRPanel extends javax.swing.JPanel {

    private Global global;
    private JFrame frame;
    private double swrZ0;
    private int plotType;    

    /**
     *Constructor de la clase SWRPanel
     * @param global Objeto de la clase Global
     * @param frame Ventana que invoca al panel
     */
    public SWRPanel(Global global, JFrame frame) {
        this.global = global;
        this.frame = frame;
        initComponents();
        initializePanel();
    }

    /**
     *Inicializa los componentes del panel
     */
    public void initializePanel() {
        
        title_lbl.setText(Global.getMessages().getString("SWR.label.label"));
        initFreq_lbl.setText(Global.getMessages().getString("SWR.label1.label"));
        finalFreq_lbl.setText(Global.getMessages().getString("SWR.label2.label"));
        stepFreq_lbl.setText(Global.getMessages().getString("SWR.label3.label"));
        startSWR_btn.setText(Global.getMessages().getString("SWR.label4.label"));
        selectSrcLbl.setText(Global.getMessages().getString("SWR.src"));
        alterz0lbl.setText(Global.getMessages().getString("SWR.altz0"));
        alterz0.setText(Global.getMessages().getString("SWR.altz0Title"));
        serielbl.setText(Global.getMessages().getString("SWR.showin"));
        db.setText(Global.getMessages().getString("SWR.db"));
        times.setText(Global.getMessages().getString("SWR.times"));
        setPlotType(Global.PLOTTIMES);        

        //Comportamiento del botón Obtener ROE
        startSWR_btn.addActionListener((ActionEvent e) -> {
            if (validateInput()) {
                global.setgSWR(addSWR());
                global.executeSWR(global, getPlotType());
                frame.dispose();
            }else{
                global.errorValidateInput();
            }
        });

        selectSrc.removeAllItems();
        for (int i = 0; i < global.getgSource().size(); i++) {
            selectSrc.addItem(i + 1 + "");
        }
        selectSrc.revalidate();
        
        //Comportamiento del JCheckBox Z0 alternativa
        alterz0.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (alterz0.isSelected()) {
                    if (global.getAlterZ0() != 0) {
                        swrZ0 = global.getAlterZ0();
                    } else {
                        String msg = Global.getMessages().getString("Messages.alterZ0");
                        String input = global.inputMessage(msg).replace(",", ".");
                        double alterZ0 = 0;
                        if (Global.isNumeric(input)) {
                            alterZ0 = Double.valueOf(input);
                            if (alterZ0 > 0) {
                                global.setAlterZ0(alterZ0);
                                swrZ0 = alterZ0;
                            } else {
                                global.errorValidateInput();
                            }
                        } else {
                            global.errorValidateInput();
                        }
                    }
                }

            }
        });
    }

     /**
     * Verifica si todos los campos del panel contienen información válida
     *
     * @return true si todos los campos del panel contienen
     * información válida, de lo contrario, devuelve false
     */
    public boolean validateInput() {
        boolean condition1, condition2, condition3, condition4;

        condition1 = false;
        condition2 = false;
        condition3 = false;
        condition4 = false;
        
        if (times.isSelected()){
             setPlotType(Global.PLOTTIMES);
        }else{
            setPlotType(Global.PLOTDB);
        }

        condition1 = (!initFreq.getText().isEmpty())
                && (!stepFreq.getText().isEmpty())
                && (!finalFreq.getText().isEmpty());
        if (condition1) {
            condition2 = (Double.valueOf(initFreq.getText().replace(",", ".")) > 0
                    && (Double.valueOf(finalFreq.getText().replace(",", ".")) > 0)
                    && (Double.valueOf(stepFreq.getText().replace(",",".")) > 0));
            condition3 = (Double.valueOf(initFreq.getText().replace(",", "."))) < (Double.valueOf(finalFreq.getText().replace(",", ".")));
            condition4 = Double.valueOf(stepFreq.getText().replace(",", ".")) >= 0  
                    
                    ;
        } else {
            global.errorValidateInput();
            return false;
        }

        return condition1 && condition2 && condition3 && condition4;
    }

    /**
     * Genera un objeto SWR a partir de los parámetros introducidos en el panel
     * @return Objeto SWR
     */
    public SWR addSWR() {
        SWR swr = new SWR();
        swr.setUseAltZ0(alterz0.isSelected());
        swr.setAltZ0(global.getAlterZ0());
        swr.setSrcIndex(Integer.valueOf(selectSrc.getSelectedItem()+""));
        swr.setInitFreq(Double.valueOf((initFreq.getText()).replace(",", ".")));
        swr.setFinalFreq(Double.valueOf((finalFreq.getText()).replace(",", ".")));
        int step = 0;
        double fin = Double.valueOf(finalFreq.getText().replace(",", "."));
        double initial = Double.valueOf(initFreq.getText().replace(",", "."));
        double st = Double.valueOf(stepFreq.getText().replace(",", "."));
        step = Math.round((float) ((fin - initial)/st)); //Paso de ángulos de iteración
        swr.setStepFreq(step);
        return swr;
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
        title_lbl = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        startSWR_btn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        initFreq_lbl = new javax.swing.JLabel();
        initFreq = new javax.swing.JFormattedTextField();
        finalFreq_lbl = new javax.swing.JLabel();
        finalFreq = new javax.swing.JFormattedTextField();
        stepFreq_lbl = new javax.swing.JLabel();
        stepFreq = new javax.swing.JFormattedTextField();
        selectSrcLbl = new javax.swing.JLabel();
        selectSrc = new javax.swing.JComboBox<>();
        alterz0lbl = new javax.swing.JLabel();
        alterz0 = new javax.swing.JCheckBox();
        serielbl = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        times = new javax.swing.JRadioButton();
        db = new javax.swing.JRadioButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(36, 113, 163), 5));
        setPreferredSize(new java.awt.Dimension(400, 300));
        setLayout(new java.awt.BorderLayout());

        title_lbl.setBackground(new java.awt.Color(36, 113, 163));
        title_lbl.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        title_lbl.setForeground(new java.awt.Color(255, 255, 255));
        title_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        title_lbl.setText("jLabel1");
        title_lbl.setOpaque(true);
        add(title_lbl, java.awt.BorderLayout.NORTH);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridLayout(1, 1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.BorderLayout());

        startSWR_btn.setText("jButton2");
        jPanel2.add(startSWR_btn, java.awt.BorderLayout.PAGE_END);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(6, 2));

        initFreq_lbl.setBackground(new java.awt.Color(255, 255, 255));
        initFreq_lbl.setForeground(new java.awt.Color(0, 0, 0));
        initFreq_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        initFreq_lbl.setText("jLabel1");
        jPanel5.add(initFreq_lbl);

        initFreq.setBackground(new java.awt.Color(255, 255, 255));
        initFreq.setForeground(new java.awt.Color(0, 0, 0));
        initFreq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        initFreq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(initFreq);

        finalFreq_lbl.setForeground(new java.awt.Color(0, 0, 0));
        finalFreq_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        finalFreq_lbl.setText("jLabel1");
        jPanel5.add(finalFreq_lbl);

        finalFreq.setBackground(new java.awt.Color(255, 255, 255));
        finalFreq.setForeground(new java.awt.Color(0, 0, 0));
        finalFreq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        finalFreq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(finalFreq);

        stepFreq_lbl.setBackground(new java.awt.Color(255, 255, 255));
        stepFreq_lbl.setForeground(new java.awt.Color(0, 0, 0));
        stepFreq_lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        stepFreq_lbl.setText("jLabel2");
        jPanel5.add(stepFreq_lbl);

        stepFreq.setBackground(new java.awt.Color(255, 255, 255));
        stepFreq.setForeground(new java.awt.Color(0, 0, 0));
        stepFreq.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        stepFreq.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel5.add(stepFreq);

        selectSrcLbl.setBackground(new java.awt.Color(255, 255, 255));
        selectSrcLbl.setForeground(new java.awt.Color(0, 0, 0));
        selectSrcLbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectSrcLbl.setText("jLabel2");
        jPanel5.add(selectSrcLbl);

        selectSrc.setBackground(new java.awt.Color(255, 255, 255));
        selectSrc.setForeground(new java.awt.Color(0, 0, 0));
        selectSrc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        selectSrc.setBorder(null);
        jPanel5.add(selectSrc);

        alterz0lbl.setBackground(new java.awt.Color(255, 255, 255));
        alterz0lbl.setForeground(new java.awt.Color(0, 0, 0));
        alterz0lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        alterz0lbl.setText("jLabel2");
        jPanel5.add(alterz0lbl);

        alterz0.setBackground(new java.awt.Color(255, 255, 255));
        alterz0.setForeground(new java.awt.Color(0, 0, 0));
        alterz0.setText("jCheckBox1");
        jPanel5.add(alterz0);

        serielbl.setBackground(new java.awt.Color(255, 255, 255));
        serielbl.setForeground(new java.awt.Color(0, 0, 0));
        serielbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        serielbl.setText("jLabel2");
        jPanel5.add(serielbl);

        jPanel3.setLayout(new java.awt.GridLayout(1, 2));

        times.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(times);
        times.setForeground(new java.awt.Color(0, 0, 0));
        times.setSelected(true);
        times.setText("jRadioButton1");
        jPanel3.add(times);

        db.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(db);
        db.setForeground(new java.awt.Color(0, 0, 0));
        db.setText("jRadioButton2");
        jPanel3.add(db);

        jPanel5.add(jPanel3);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox alterz0;
    private javax.swing.JLabel alterz0lbl;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton db;
    private javax.swing.JFormattedTextField finalFreq;
    private javax.swing.JLabel finalFreq_lbl;
    private javax.swing.JFormattedTextField initFreq;
    private javax.swing.JLabel initFreq_lbl;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JComboBox<String> selectSrc;
    private javax.swing.JLabel selectSrcLbl;
    private javax.swing.JLabel serielbl;
    private javax.swing.JButton startSWR_btn;
    private javax.swing.JFormattedTextField stepFreq;
    private javax.swing.JLabel stepFreq_lbl;
    private javax.swing.JRadioButton times;
    private javax.swing.JLabel title_lbl;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the plotType
     */
    public int getPlotType() {
        return plotType;
    }

    /**
     * @param plotType the plotType to set
     */
    public void setPlotType(int plotType) {
        this.plotType = plotType;
    }
}
