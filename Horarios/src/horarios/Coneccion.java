/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ander
 */
public class Coneccion {

    Connection conecct;
    String ip = "";
    String userBD = "";
    String passBD = "";
    String nomBD = "";
    String CI = "false";

    public Coneccion() {
        datos();
        conectar();

    }

    public Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            conecct = DriverManager.getConnection("jdbc:postgresql://" + ip + ":5432/" + nomBD, userBD, passBD);

//JOptionPane.showMessageDialog(null, "Conexxion Exitosa");
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, ex);
            JOptionPane.showMessageDialog(null, "Error en la coneccion");
        }
        return conecct;

    }

    public void datos() {
        File f = new File("conf.txt");
        String[] datosL = null;
        boolean aux = false;
        if (f.exists()) {
            try {
                Scanner arch = new Scanner(f);
                String linea;

                while (arch.hasNext()) {

                    linea = arch.nextLine();
                    datosL = linea.split("::");
                    if (datosL.length == 5) {
                        ip = datosL[0];
                        userBD = datosL[1];
                        passBD = datosL[2];
                        nomBD = datosL[3];
                        CI = datosL[4];
                    } else {
                        JOptionPane.showMessageDialog(null, "Problemas en los parametros del archivo conf.txt");
                    }

                   // System.out.println(ip + " " + user + " " + pass);
                }
                arch.close();
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Problemas en la lectura del archivo conf.txt");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Archivo conf.txt no existente");
        }

    }

    public void generarArchivos(String usu) throws FileNotFoundException {
        File f = new File("horario.txt");
        PrintWriter salida = new PrintWriter(f);
        File f2 = new File("recordatorios.txt");
        PrintWriter salida2 = new PrintWriter(f);
        try {
            Connection con = conectar();
            String sql = "SELECT d.Ced_Doc,d.Nom_Doc,d.Ape_Doc,di.Id_Dia,di.Nom_Dia,j.Id_Jor,j.Hor_Emp,j.Hor_Ter,j.Des_Jor"
                    + "from docentes d, dias di, jornadas j"
                    + "WHERE j.Id_Dia_Per=di.Id_Dia"
                    + "AND j.Ced_Doc_Per=d.Ced_Doc"
                    + "AND j.Ced_Doc_Per='" + usu + "'";
            String sql2 = "SELECT d.Ced_Doc,d.Nom_Doc,d.Ape_Doc,di.Id_Dia,di.Nom_Dia,r.Id_Rec,r.hor_rec,r.Des_rec"
                    + "from docentes d, dias di, recordatorios r"
                    + "WHERE r.Id_Dia_Per=di.Id_Dia"
                    + "AND r.Ced_Doc_Per=d.Ced_Doc"
                    + "AND r.Ced_Doc_Per='" + usu + "'";

            PreparedStatement stm = con.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            PreparedStatement stm2 = con.prepareStatement(sql);
            ResultSet rs2 = stm.executeQuery();

            while (rs.next()) {
                salida.println(rs.getString(1) + "-"
                        + rs.getString(2) + "-"
                        + rs.getString(3) + "-"
                        + rs.getString(4) + "-"
                        + rs.getString(5) + "-"
                        + rs.getString(6) + "-"
                        + rs.getString(7) + "-"
                        + rs.getString(8) + "-"
                        + rs.getString(9));
            }
            salida.close();
            while (rs2.next()) {
                salida2.println(rs.getString(1) + "-"
                        + rs.getString(2) + "-"
                        + rs.getString(3) + "-"
                        + rs.getString(4) + "-"
                        + rs.getString(5) + "-"
                        + rs.getString(6) + "-"
                        + rs.getString(7) + "-"
                        + rs.getString(8));
            }
            salida2.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void CargarArchivos() throws FileNotFoundException {
        File f = new File("horarios.txt");
        File f2 = new File("horarios.txt");
        if (f.exists()) {
            Scanner entrada = new Scanner(f);
            while (entrada.hasNext()) {
                String linea = entrada.nextLine();
                String[] datos = linea.split("-");
                /*
                 datos[0];//cedula docente
                 datos[1];//nombre dcente
                 datos[2];//apellido docente
                 datos[3];//id dia
                 datos[4];//nombre dia
                 datos[5];//id jornada
                 datos[6];//hora empieza jornada
                 datos[7];//hora termina jornada
                 datos[8];//descricion jornada
                 */
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo local de horarios");
        }
        if (f2.exists()) {
            Scanner entrada2 = new Scanner(f2);
            while (entrada2.hasNext()) {
                String linea = entrada2.nextLine();
                String[] datos = linea.split("-");
                /*
                 datos[0];//cedula docente
                 datos[1];//nombre dcente
                 datos[2];//apellido docente
                 datos[3];//id dia
                 datos[4];//nombre dia
                 datos[5];//id recordatorio
                 datos[6];//hora recordatorio
                 datos[7];//descripcion recordatorio
                 */
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el archivo local de recordatorios");
        }
    }

    public boolean datosTemporales() {

        return true;
    }

}
