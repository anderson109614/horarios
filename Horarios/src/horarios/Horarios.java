/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrador
 */
public class Horarios extends javax.swing.JFrame {

    /**
     * Creates new form Horarios
     */
    DefaultTableModel modeloTabla;
    DefaultComboBoxModel modeloComboBox;
    Coneccion cn;

    public Horarios(String cedula) {
        initComponents();
        cn = new Coneccion();
        modeloDeTabla();

        //cargarDatosDocente(cedula);
        //cargarComboBoxDias();
    }

//    public void activarJornada_o_Recordatorio() {
//        // TODO add your handling code here:
//        if (rdbJornada.isSelected()) {
//            fmtHoraFin.setEnabled(true);
//        } else {
//            fmtHoraFin.setText("");
//            fmtHoraFin.setEnabled(false);
//
//        }
//    }

    public boolean validarHoraFinMayorHoraIni() {
        try {
            SimpleDateFormat parseador = new SimpleDateFormat("H:mm");
            //Hora Inicio
            Calendar horaIni = Calendar.getInstance();
            Date hi = parseador.parse(fmtHoraIni.getText());
            horaIni.setTime(hi);
            //Hora Fin
            Calendar horaFin = Calendar.getInstance();
            Date hf = parseador.parse(fmtHoraFin.getText());
            horaFin.setTime(hf);
            //Comparando horas
            int comp = horaIni.compareTo(horaFin);
            //Si hora fin es mayor que inicio false
            if (comp >= 0) {
                JOptionPane.showMessageDialog(null, "Error: hora fin debe ser mayor a hora inicio");
                return false;
            }
            return true;
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error de fecha: " + ex);
            return false;
        }

    }

//    public boolean validarHoraRecordatorio() {
//        try {
//            SimpleDateFormat parseador = new SimpleDateFormat("H:mm");
//            SimpleDateFormat formateador = new SimpleDateFormat("H:mm");
//            //Hora Inicio
//            Calendar horaIni = Calendar.getInstance();
//            Date hi = parseador.parse(fmtHoraIni.getText());
//            horaIni.setTime(hi);
//            //Hora inicio recuperada base
//            Calendar horaIniBase = Calendar.getInstance();
//            Date hib = null;
//            Connection cc = cn.conectar();
//            String sql = "select hor_rec from recordatorios "
//                    + "where id_dia_per =" + (cmbDias.getSelectedIndex() + 1) + " "
//                    + "and ced_doc_per ='" + txtCedDoc.getText() + "'";
//            try {
//                Statement st = cc.createStatement();
//                ResultSet rs = st.executeQuery(sql);
//                while (rs.next()) {
//                    hib = parseador.parse(rs.getString("hor_rec"));
//                    horaIniBase.setTime(hib);
//                    if (horaIni.compareTo(horaIniBase) == 0) {
//                        JOptionPane.showMessageDialog(null, "Error: Hora " + formateador.format(horaIni) + " ya asignada");
//                        return false;
//                    }
//                }
//                return true;
//            } catch (SQLException ex) {
//                JOptionPane.showMessageDialog(null, "Error agregando recordatorio");
//                return false;
//            }
//
//
//
//        } catch (ParseException ex) {
//            JOptionPane.showMessageDialog(null, "Error en las fechas: " + ex);
//            return false;
//        }
//
//    }

    public boolean validarHorasJornada() {
        try {
            SimpleDateFormat parseador = new SimpleDateFormat("H:mm");
            //SimpleDateFormat formateador = new SimpleDateFormat("H:mm");
            //Hora Inicio
            Calendar horaIni = Calendar.getInstance();
            Date hi = parseador.parse(fmtHoraIni.getText());
            horaIni.setTime(hi);
            //Hora fin
            Calendar horafin = Calendar.getInstance();
            Date hf = parseador.parse(fmtHoraFin.getText());
            horafin.setTime(hf);
            //Hora inicio recuperada base
            Calendar horaIniBase = Calendar.getInstance();
            Date hib = null;

            //Hora fin recuperada base
            Calendar horaFinBase = Calendar.getInstance();
            Date hfb = null;

            int inicio, inicioB, finB, numH;
            inicio = 0;
            inicioB = 0;
            finB = 0;
            numH = 0;

            Connection cc = cn.conectar();
            String sql = "select hor_emp,hor_ter from recordatorios "
                    + "where id_dia_per =" + (cmbDias.getSelectedIndex() + 1) + " ";
//                    + "and ced_doc_per ='" + txtCedDoc.getText() + "'";
            try {
                Statement st = cc.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    hib = parseador.parse(rs.getString("hor_emp"));
                    horaIniBase.setTime(hib);
                    inicioB = horaIniBase.get(Calendar.HOUR_OF_DAY);

                    hfb = parseador.parse(rs.getString("hor_ter"));
                    horaFinBase.setTime(hfb);
                    finB = horaFinBase.get(Calendar.HOUR_OF_DAY);

                    numH = finB - inicioB;

                    inicio = horaIni.get(Calendar.HOUR_OF_DAY);
                    for (int i = 0; i < numH; i++) {
                        if (inicioB == inicio) {
                            JOptionPane.showMessageDialog(null, "Error: jornada se cruza con otra asignada");
                            return false;
                        }
                        inicioB++;
                    }
                }
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error agregando recordatorio");
                return false;
            }



        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Error en las fechas: " + ex);
            return false;
        }
    }

//    public void agregarRecordatorio() {
//        Connection cc = cn.conectar();
//        int Id_Dia_Per;
//        String hor_rec, Ced_Doc_Per, Des_rec;
//        Id_Dia_Per = cmbDias.getSelectedIndex() + 1;
//        hor_rec = fmtHoraIni.getText();
//        Ced_Doc_Per = txtCedDoc.getText();
//        if (!txtDescripcion.getText().isEmpty()) {
//            Des_rec = txtDescripcion.getText();
//        } else {
//            Des_rec = "Sin descripción";
//        }
//        String sql = "insert into recordatorios(Id_Dia_Per, hor_rec, Ced_Doc_Per, Des_rec)"
//                + " values (?,?,?,?)";
//        try {
//            PreparedStatement psd = cc.prepareStatement(sql);
//            psd.setInt(1, Id_Dia_Per);
//            psd.setString(2, hor_rec);
//            psd.setString(3, Ced_Doc_Per);
//            psd.setString(4, Des_rec);
//
//            int n = psd.executeUpdate();
//
//            if (n > 0) {
//                JOptionPane.showMessageDialog(null, "Recordatorio agregado correctamente");
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Error en la insercción: " + ex);
//        }
//    }

    public void agregarJornada() {
        // TODO add your handling code here:
        Connection cc = cn.conectar();
        try {
            CallableStatement cst = cc.prepareCall("{call Contar_Horas_Pro (?,?,?,?,?)}");
            cst.setInt(1, cmbDias.getSelectedIndex() + 1);
//            cst.setString(2, txtCedDoc.getText());
            cst.setString(3, fmtHoraIni.getText());
            cst.setString(4, fmtHoraFin.getText());
            cst.setString(5, txtDescripcion.getText());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex);
        } finally {
            try {
                cc.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error " + ex);
            } catch (Exception ex) {
            }
        }
    }

//    public void cargarDatosDocente(String cedula) {
//        Connection cc = cn.conectar();
//        String sql = "Select * from docentes where ced_doc='" + cedula + "'";
//        try {
//            Statement st = cc.createStatement();
//            ResultSet rs = st.executeQuery(sql);
//            while (rs.next()) {
//                txtNombre.setText(rs.getString("nom_doc"));
//                txtApellido.setText(rs.getString("ape_doc"));
//            }
//            st.close();
//            rs.close();
//            cc.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, "Ocurrió un error en la consulta " + ex);
//        }
//    }

    public void cargarComboBoxDias() {
        modeloComboBox = new DefaultComboBoxModel();
        Connection cc = cn.conectar();
        String sql = "select * from dias";
        try {
            Statement st = cc.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("Id_Dia");
                String dia = rs.getString("Nom_Dia");
                modeloComboBox.addElement(id + " " + dia);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Horarios.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void modeloDeTabla() {
        String titulos[] = {"Horas","Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modeloTabla = new DefaultTableModel(null, titulos) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHorario.setModel(modeloTabla);
        tblHorario.getTableHeader().setReorderingAllowed(false);
    }

    public void limpiar() {

        //cmbDias.setSelectedIndex(0);
//        rdbJornada.setSelected(true);
        fmtHoraFin.setText("");
        fmtHoraIni.setText("");
        txtDescripcion.setText("");
//        activarJornada_o_Recordatorio();

    }

    public boolean validarCampos() {
//        if (rdbJornada.isSelected()) {
            if (fmtHoraIni.getText().charAt(0) == ' ') {
                JOptionPane.showMessageDialog(null, "Ingrese la hora de inicio");
                return false;
            } else if (fmtHoraFin.getText().charAt(0) == ' ') {
                JOptionPane.showMessageDialog(null, "Ingrese la hora de fin");
                return false;
            } else {
                return true;
            }
//        } else {
//            if (fmtHoraIni.getText().charAt(0) == ' ') {
//                JOptionPane.showMessageDialog(null, "Ingrese la hora de inicio");
//                return false;
//            } else {
//                return true;
//            }
        }
     

    public void validarEscrituraHoras(KeyEvent evt, JFormattedTextField texto) {
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        int cont = 0;
        boolean verif = false;
        if (c > '2' && texto.getText().charAt(0) == ' ') {
            evt.consume();
        } else if (texto.getText().charAt(0) == '2') {
            cont++;
        }
        if (texto.getText().charAt(0) == ' ') {
            cont = 0;
            verif = false;
        }
        if (cont > 0 && c > '0') {
            evt.consume();
        }
        if (texto.getText().charAt(0) == '0') {
            verif = true;
        }
        if (verif && c < '7') {
            evt.consume();
        }
    }

    public void validarCamposSoloLetras(KeyEvent evt) {
        // TODO add your handling code here:
        char Alfab[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'ñ', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'Ñ', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'á', 'é', 'í', 'ó', 'ú', 'Á', 'É', 'Í', 'Ó', 'Ú', 'ü', 'Ü', ' '};
        char c = evt.getKeyChar();
        int x = 0;
        int longitud = Alfab.length;

        for (int i = 0; i < longitud; i++) {
            if (c != (Alfab[i])) {
                x += 1;
            }
        }

        if (x == longitud || txtDescripcion.getText().length() >= 20) {
            evt.consume();
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

        GrupoJornadaRecordatorio = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHorario = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtAgregar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbDias = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        fmtHoraIni = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        fmtHoraFin = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HORARIOS");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tblHorario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblHorario);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        txtAgregar.setText("Guardar");
        txtAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgregarActionPerformed(evt);
            }
        });

        btnLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        btnLimpiar.setText("Cancelar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        jButton1.setText("Salir");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAgregar)
                .addGap(18, 18, 18)
                .addComponent(btnLimpiar)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setText("Día:");

        cmbDias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDiasActionPerformed(evt);
            }
        });

        jLabel5.setText("Hora Inicio:");

        try {
            fmtHoraIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fmtHoraIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fmtHoraIniKeyTyped(evt);
            }
        });

        jLabel6.setText("Hora Fin:");

        try {
            fmtHoraFin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fmtHoraFin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fmtHoraFinKeyTyped(evt);
            }
        });

        jLabel7.setText("Descripción:");

        txtDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(48, 48, 48)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fmtHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fmtHoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(54, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(269, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fmtHoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(fmtHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel4)
                    .addContainerGap(136, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgregarActionPerformed
        if (validarCampos() && validarHoraFinMayorHoraIni()) {
//            if (rdbJornada.isSelected()) {
                if (validarHorasJornada()) {
                    agregarJornada();
                }
//            } else {
//                if (validarHoraRecordatorio()) {
//                    agregarRecordatorio();
//                }
//            }
        
        }

    }//GEN-LAST:event_txtAgregarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:

        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionKeyTyped
        validarCamposSoloLetras(evt);

    }//GEN-LAST:event_txtDescripcionKeyTyped

    private void fmtHoraIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fmtHoraIniKeyTyped
        validarEscrituraHoras(evt, fmtHoraIni);
    }//GEN-LAST:event_fmtHoraIniKeyTyped

    private void fmtHoraFinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fmtHoraFinKeyTyped
        // TODO add your handling code here:
        validarEscrituraHoras(evt, fmtHoraFin);
    }//GEN-LAST:event_fmtHoraFinKeyTyped

    private void cmbDiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbDiasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Horarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Horarios("").setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup GrupoJornadaRecordatorio;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox cmbDias;
    private javax.swing.JFormattedTextField fmtHoraFin;
    private javax.swing.JFormattedTextField fmtHoraIni;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHorario;
    private javax.swing.JButton txtAgregar;
    private javax.swing.JTextField txtDescripcion;
    // End of variables declaration//GEN-END:variables
}
