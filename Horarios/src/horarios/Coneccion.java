/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package horarios;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
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
    String ip="";
    String userBD="";
    String passBD="";
    String nomBD="";
    String CI="false";
    
    public Coneccion() {
        datos();
        conectar();
        
    }

    public Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            conecct = DriverManager.getConnection("jdbc:postgresql://" + ip + ":5432/"+nomBD, userBD, passBD);

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
                    if(datosL.length==5){
                    ip = datosL[0];
                    userBD = datosL[1];
                    passBD = datosL[2];
                    nomBD= datosL[3];
                    CI=datosL[4];    
                    }else{
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
    
    public boolean datosTemporales(){
         
        
        return true;
    }
    
    
}
