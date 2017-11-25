/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.*;
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

    public void activarJornada_o_Recordatorio() {
        // TODO add your handling code here:
        if (rdbJornada.isSelected()) {
            fmtHoraFin.setEnabled(true);
        } else {
            fmtHoraFin.setText("");
            fmtHoraFin.setEnabled(false);

        }
    }

    public void agregarRecordatorio() {
        Connection cc = cn.conectar();
        int Id_Dia_Per;
        String hor_rec, Ced_Doc_Per, Des_rec;
        Id_Dia_Per = cmbDias.getSelectedIndex() + 1;
        hor_rec = fmtHoraIni.getText();
        Ced_Doc_Per = txtCedDoc.getText();
        if (!txtDescripcion.getText().isEmpty()) {
            Des_rec = txtDescripcion.getText();
        } else {
            Des_rec = "Sin descripción";
        }
        String sql = "insert into recordatorios(Id_Dia_Per, hor_rec, Ced_Doc_Per, Des_rec)"
                + " values (?,?,?,?)";
        try {
            PreparedStatement psd = cc.prepareStatement(sql);
            psd.setInt(1, Id_Dia_Per);
            psd.setString(2, hor_rec);
            psd.setString(3, Ced_Doc_Per);
            psd.setString(4, Des_rec);

            int n = psd.executeUpdate();

            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Recordatorio agregado correctamente");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la insercción: " + ex);
        }
    }

    public void agregarJornada() {
        // TODO add your handling code here:
        Connection cc = cn.conectar();
        try {
            CallableStatement cst = cc.prepareCall("{call Contar_Horas_Pro (?,?,?,?,?)}");
            cst.setInt(1, cmbDias.getSelectedIndex() + 1);
            cst.setString(2, txtCedDoc.getText());
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

    public void cargarDatosDocente(String cedula) {
        Connection cc = cn.conectar();
        String sql = "Select * from docentes where ced_doc='" + cedula + "'";
        try {
            Statement st = cc.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                txtNombre.setText(rs.getString("nom_doc"));
                txtApellido.setText(rs.getString("ape_doc"));
            }
            st.close();
            rs.close();
            cc.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error en la consulta " + ex);
        }
    }

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
        String titulos[] = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
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
        rdbJornada.setSelected(true);
        fmtHoraFin.setText("");
        fmtHoraIni.setText("");
        txtDescripcion.setText("");
        activarJornada_o_Recordatorio();

    }

    public boolean validarCampos() {
        if (rdbJornada.isSelected()) {
            if (fmtHoraIni.getText().charAt(0) == ' ') {
                JOptionPane.showMessageDialog(null, "Ingrese la hora de inicio");
                return false;
            } else if (fmtHoraFin.getText().charAt(0) == ' ') {
                JOptionPane.showMessageDialog(null, "Ingrese la hora de fin");
                return false;
            } else {
                return true;
            }
        } else {
            if (fmtHoraIni.getText().charAt(0) == ' ') {
                JOptionPane.showMessageDialog(null, "Ingrese la hora de inicio");
                return false;
            } else {
                return true;
            }
        }
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

    public void validarSoloLetras(KeyEvent evt) {
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
        jLabel6 = new javax.swing.JLabel();
        fmtHoraIni = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        fmtHoraFin = new javax.swing.JFormattedTextField();
        txtNombre = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHorario = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtCedDoc = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbDias = new javax.swing.JComboBox();
        txtAgregar = new javax.swing.JButton();
        rdbJornada = new javax.swing.JRadioButton();
        rdbRecordatorio = new javax.swing.JRadioButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HORARIOS");

        jLabel6.setText("Hora Fin:");

        try {
            fmtHoraIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:00")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        fmtHoraIni.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fmtHoraIniKeyTyped(evt);
            }
        });

        jLabel4.setText("Día:");

        try {
            fmtHoraFin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:00")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        txtNombre.setEnabled(false);

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

        jLabel1.setText("Docente:");

        txtCedDoc.setEnabled(false);

        txtApellido.setEnabled(false);

        jLabel5.setText("Hora Inicio:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido:");

        txtAgregar.setText("Agregar >>");
        txtAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAgregarActionPerformed(evt);
            }
        });

        GrupoJornadaRecordatorio.add(rdbJornada);
        rdbJornada.setSelected(true);
        rdbJornada.setText("Jornada");
        rdbJornada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbJornadaActionPerformed(evt);
            }
        });

        GrupoJornadaRecordatorio.add(rdbRecordatorio);
        rdbRecordatorio.setText("Recordatorio");
        rdbRecordatorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbRecordatorioActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jLabel7.setText("Descripción:");

        txtDescripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDescripcionKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCedDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(txtApellido))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(rdbRecordatorio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                            .addComponent(rdbJornada, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtAgregar, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fmtHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fmtHoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtCedDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(cmbDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(rdbJornada))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(fmtHoraIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rdbRecordatorio))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(btnLimpiar)
                                .addGap(18, 18, 18)
                                .addComponent(txtAgregar))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(fmtHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAgregarActionPerformed
        if (validarCampos()) {
            if (rdbJornada.isSelected()) {
                agregarJornada();
            } else {
                agregarRecordatorio();
            }
        }


    }//GEN-LAST:event_txtAgregarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:

        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtDescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDescripcionKeyTyped
        validarSoloLetras(evt);

    }//GEN-LAST:event_txtDescripcionKeyTyped

    private void rdbJornadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbJornadaActionPerformed
        activarJornada_o_Recordatorio();
    }//GEN-LAST:event_rdbJornadaActionPerformed

    private void rdbRecordatorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbRecordatorioActionPerformed
        // TODO add your handling code here:
        activarJornada_o_Recordatorio();

    }//GEN-LAST:event_rdbRecordatorioActionPerformed

    private void fmtHoraIniKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fmtHoraIniKeyTyped
        validarEscrituraHoras(evt, fmtHoraIni);
    }//GEN-LAST:event_fmtHoraIniKeyTyped

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdbJornada;
    private javax.swing.JRadioButton rdbRecordatorio;
    private javax.swing.JTable tblHorario;
    private javax.swing.JButton txtAgregar;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtCedDoc;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
