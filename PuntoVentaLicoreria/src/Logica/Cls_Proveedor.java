package Logica;

import Logica.ConexionBD;
import java.awt.HeadlessException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Cls_Proveedor {

    private final String SQL_INSERT = "INSERT INTO libreria1.proveedor(Nombre,Direccion,Telefono)" + "VALUES(?,?,?)";
    private final String SQL_SELECT = "SELECT * FROM libreria1.proveedor";
    private PreparedStatement PS;
    private DefaultTableModel DT;
    private ResultSet RS;
    private final ConexionBD CN;

    public Cls_Proveedor() {
        PS = null;
        CN = new ConexionBD();
    }

    private DefaultTableModel setTitulos() {
        DT = new DefaultTableModel();
        DT.addColumn("ID");
        DT.addColumn("Nombre");
        DT.addColumn("Direccion");
        DT.addColumn("Telefono");
        return DT;
    }

    public int InsertDatosP(String nombre, String direccion, String telefono) throws Exception {
        int res = 0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT);
            PS.setString(1, nombre);
            PS.setString(2, direccion);
            PS.setString(3, telefono);
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

    public int ActualizarDatosP(int id, String nombre, String direccion, String telefono) throws Exception {
        String SQL = "UPDATE libreria1.proveedor SET Nombre='" + nombre + "', Direccion='" + direccion + "',Telefono='" + telefono + "' WHERE ID=" + id;
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

    public int EliminarDatosP(int id) {
        String SQL = "DELETE FROM libreria1.proveedor WHERE ID=" + id;
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

    public DefaultTableModel getDatosP() {
        try {
            setTitulos();
            PS = CN.getConnection().prepareStatement(SQL_SELECT);
            RS = PS.executeQuery();
            Object[] fila = new Object[4];
            while (RS.next()) {
                fila[0] = RS.getInt(1);
                fila[1] = RS.getString(2);
                fila[2] = RS.getString(3);
                fila[3] = RS.getString(4);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los datos..." + e.getMessage());
        } finally {
            PS = null;
            RS = null;
            CN.close();
        }
        return DT;
    }

    public DefaultTableModel getDatoP(int ctr, String prm) {
        String SQL;
        if (ctr == 0) {
            SQL = "SELECT * FROM libreria1.proveedor WHERE ID=" + prm;
        } else {
            SQL = "SELECT * FROM libreria1.proveedor WHERE Nombre like'" + prm + "%'";
        }
        try {
            setTitulos();
            PS = CN.getConnection().prepareStatement(SQL);
            RS = PS.executeQuery();
            Object[] fila = new Object[4];
            while (RS.next()) {
                fila[0] = RS.getInt(1);
                fila[1] = RS.getString(2);
                fila[2] = RS.getString(3);
                fila[3] = RS.getString(4);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar los datos..." + e.getMessage());
        } finally {
            PS = null;
            RS = null;
            CN.close();
        }
        return DT;
    }
}
