package Logica;

import Logica.ConexionBD;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Cls_Inventario {

    private final String SQL_INSERT = "INSERT INTO libreria1.inventario(Codigo,Existencia,Nombre,Precio,Marca,Descripcion)" + "VALUES(?,?,?,?,?,?)";
    private final String SQL_SELECT = "SELECT * FROM libreria1.inventario";
    private PreparedStatement PS;
    private DefaultTableModel DT;
    private ResultSet RS;
    private final ConexionBD CN;

    public Cls_Inventario() {
        PS = null;
        CN = new ConexionBD();
    }

    private DefaultTableModel setTitulos() {
        DT = new DefaultTableModel();
        DT.addColumn("ID");
        DT.addColumn("Codigo");
        DT.addColumn("Existencia");
        DT.addColumn("Nombre");
        DT.addColumn("Precio");
        DT.addColumn("Marca");
        DT.addColumn("Descripcion");
        return DT;
    }

    public int InsertDatos(String codigo, int existencia, String nombre, float precio, String marca, String descripcion) throws Exception {
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT);
            PS.setString(1, codigo);
            PS.setInt(2, existencia);
            PS.setString(3, nombre);
            PS.setFloat(4, precio);
            PS.setString(5, marca);
            PS.setString(6, descripcion);
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
    public int ActualizarDatos(int id,String codigo, int existencia, String nombre, float precio, String marca, String descripcion) throws Exception {
       String SQL="UPDATE libreria1.inventario SET Codigo='"+codigo+"', Existencia='"+existencia+"',Nombre='"+nombre+"', Precio='"+precio+"',Marca='"+marca+"',"
       + "Descripcion='"+descripcion+"' WHERE ID="+id;
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
    public int EliminarDatos(int id)
    {
        String SQL="DELETE FROM libreria1.inventario WHERE ID="+id;
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

    public DefaultTableModel getDatos() {
        try {
            setTitulos();
            PS= CN.getConnection().prepareStatement(SQL_SELECT);
            RS = PS.executeQuery();
            Object[] fila = new Object[7];
            while(RS.next())
            {
                fila[0]=RS.getInt(1);
                fila[1]=RS.getString(2);
                fila[2]=RS.getInt(3);
                fila[3]=RS.getString(4);
                fila[4]=RS.getFloat(5);
                fila[5]=RS.getString(6);
                fila[6]=RS.getString(7);
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
    public DefaultTableModel getDato(int ctr, String prm) {
        String SQL;
        if(ctr==0)
        {
            SQL="SELECT * FROM libreria1.inventario WHERE ID="+prm;
        }
        else
        {
            SQL="SELECT * FROM libreria1.inventario WHERE Codigo like'"+prm+"%'";
        }
        try {
            setTitulos();
            PS= CN.getConnection().prepareStatement(SQL);
            RS = PS.executeQuery();
            Object[] fila = new Object[7];
            while(RS.next())
            {
                fila[0]=RS.getInt(1);
                fila[1]=RS.getString(2);
                fila[2]=RS.getInt(3);
                fila[3]=RS.getString(4);
                fila[4]=RS.getFloat(5);
                fila[5]=RS.getString(6);
                fila[6]=RS.getString(7);
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
