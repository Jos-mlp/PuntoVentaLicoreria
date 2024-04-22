package Logica;
import Logica.ConexionBD;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
public class Cls_Usuarios {
    private final String SQL_INSERT = "INSERT INTO libreria1.usuario(Nombre,Puesto,Usuario,Pass)" + "VALUES(?,?,?,?)";
    private final String SQL_SELECT = "SELECT * FROM libreria1.usuario";
    private PreparedStatement PS;
    private DefaultTableModel DT;
    private ResultSet RS;
    private final ConexionBD CN;
    
     public Cls_Usuarios(){
         PS = null;
        CN = new ConexionBD();
     }
     private DefaultTableModel setTitulos() {
        DT = new DefaultTableModel();
        DT.addColumn("ID");
        DT.addColumn("Nombre");
        DT.addColumn("Puesto");
        DT.addColumn("Uusuario");
        DT.addColumn("Pasword");
        return DT;
    }
     public int InsertDatosU(String nombre, String puesto,  String usuario, String password) throws Exception {
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT);
            PS.setString(1, nombre);
            PS.setString(2, puesto);
            PS.setString(3, usuario);
            PS.setString(4, password);
            res = PS.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro Guardado..........");
            }
        } catch (HeadlessException | SQLException e) {
            System.err.println("Error al guardar los datos en la base de datos: " + e.getMessage());
        } finally {
            PS = null;
            CN.close();
        }

        return res;
    }
     public int ActualizarDatosU(int id,String nombre, String puesto,String usuario, String pass) throws Exception {
       String SQL="UPDATE libreria1.usuario SET Nombre='"+nombre+"', Puesto='"+puesto+"',Usuario='"+usuario+"', Pass='"+pass+"' WHERE ID="+id;
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro Modificado..........");
            }
        } catch (HeadlessException | SQLException e) {
            System.err.println("Error al modificar los datos en la base de datos: " + e.getMessage());
        } finally {
            PS = null;
            CN.close();
        }

        return res;
    }
     public int EliminarDatosU(int id)
    {
        String SQL="DELETE FROM libreria1.usuario WHERE ID="+id;
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro Eliminado..........");
            }
        } catch (HeadlessException | SQLException e) {
            System.err.println("Error al eliminar los datos en la base de datos: " + e.getMessage());
        } finally {
            PS = null;
            CN.close();
        }
        return res;
    }
     public DefaultTableModel getDatosU() {
        try {
            setTitulos();
            PS= CN.getConnection().prepareStatement(SQL_SELECT);
            RS = PS.executeQuery();
            Object[] fila = new Object[5];
            while(RS.next())
            {
                fila[0]=RS.getInt(1);
                fila[1]=RS.getString(2);
                fila[2]=RS.getString(3);
                fila[3]=RS.getString(4);
                fila[4]=RS.getString(5);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
           System.out.println("Error al listar los datos..." +e.getMessage()); 
        } finally {
            PS=null;
            RS= null;
            CN.close();
        }
        return DT;
    }
     public DefaultTableModel getDatoU(int ctr, String prm) {
        String SQL;
        if(ctr==0)
        {
            SQL="SELECT * FROM libreria1.usuario WHERE idUsuario="+prm;
        }
        else
        {
            SQL="SELECT * FROM libreria1.usuario WHERE Nombre like'"+prm+"%'";
        }
        try {
            setTitulos();
            PS= CN.getConnection().prepareStatement(SQL);
            RS = PS.executeQuery();
            Object[] fila = new Object[5];
            while(RS.next())
            {
                fila[0]=RS.getInt(1);
                fila[1]=RS.getString(2);
                fila[2]=RS.getString(3);
                fila[3]=RS.getString(4);
                fila[4]=RS.getString(5);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
           System.out.println("Error al listar los datos..." +e.getMessage()); 
        } finally {
            PS=null;
            RS= null;
            CN.close();
        }
        return DT;
    }
}
