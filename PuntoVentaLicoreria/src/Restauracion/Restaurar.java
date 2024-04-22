/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Restauracion;

import Respaldos.Respaldo;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Restaurar {

  
    public static void main(String[] args) {
        try {
           Process p = Runtime.getRuntime().exec("mysql -u root -12345 libreria1");
           new HiloLector(p.getErrorStream()).start();
            OutputStream os = p.getOutputStream();
            FileInputStream fis = new  FileInputStream("backup_libreria1.sql");
            byte[] buffer = new byte[1000];
            int leido = fis.read(buffer);
            while(leido>0)
            {
                os.write(buffer,0,leido);
                leido = fis.read(buffer);
            }
            os.flush();
            os.close();
            fis.close();
        } catch (IOException ex) {
            Logger.getLogger(Respaldo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
