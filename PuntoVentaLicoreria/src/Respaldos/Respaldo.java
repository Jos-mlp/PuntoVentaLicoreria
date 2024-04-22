
package Respaldos;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Respaldo {

    
    public static void main(String[] args) {
     
        try {
           Process p = Runtime.getRuntime().exec("mysqldump -u root -p12345 libreria1");
           new HiloLector(p.getErrorStream()).start();
            InputStream is = p.getInputStream();
            FileOutputStream fos = new FileOutputStream("backup_libreria1.sql");
            byte[] buffer = new byte[1000];
            int leido = is.read(buffer);
            while(leido>0)
            {
                fos.write(buffer,0,leido);
                leido = is.read(buffer);
            }
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Respaldo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
        
        
    }
    
}
