package Logica;

import com.mysql.jdbc.Statement;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CompraDao {
    Connection con;
    private final String SQL_INSERT = "INSERT INTO libreria1.compra(Fecha,Total,Usuario_ID,Proveedor_ID)" + " VALUES(?,?,?,?)";
    private final String SQL_INSERT2 = "INSERT INTO libreria1.detallecompra(Cantidad,Subtotal,Compra_ID,Inventario_ID)" + " VALUES(?,?,?,?)";
    private PreparedStatement ps;
    private ResultSet rs;;
    private static ResultSet Resultado;
    private final ConexionBD cn = new ConexionBD();;
    
    public CompraDao(){
        
    }
    
    public Producto BuscarProducto(String code){
        Producto l = new Producto();
        String sql = "SELECT * FROM libreria1.inventario WHERE Codigo = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, code);
            rs= ps.executeQuery();
            if (rs.next()) {
            l.setCodigo(rs.getString("Codigo"));
            l.setExistencia(rs.getInt("Existencia"));
            l.setNombre(rs.getString("Nombre"));
            l.setPrecio(rs.getFloat("Precio"));
            l.setMarca(rs.getString("Marca"));
            l.setDescripcion(rs.getString("Descripcion"));
            }
        } catch (SQLException e) {
            System.err.println("Error al bucar en la BD: " + e.getMessage());
        }finally {
            ps = null;
            cn.close(); 
        }
        
        return l;
    }
    
    // Metodo Insercion
    //Este metodo esta hecho para que regrese la llave primaria del dato insertado
       /* public int InsertarCompra(float total,int proveedor, int usuario) throws Exception {
        int id = 0,res=0;
        try {
            ps = cn.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, null);
            ps.setFloat(2, total);
            ps.setInt(3, usuario);
            ps.setInt(4, proveedor);
            res = ps.executeUpdate();
            Resultado = (ResultSet) ps.getGeneratedKeys();
             if (Resultado.next()) {
                id = Resultado.getInt(1);
             }
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro Guardado..........");
            }
            
        } catch (HeadlessException | SQLException e) {
            System.err.println("Error al guardar los datos en la base de datos: " + e.getMessage());
            return -1;
        } finally {
            ps = null;
            cn.close();
        }

        return id;
    }
        
        public void InsertarDatosCompra(int cantidad, float subtotal, int compraID, int inventarioID) throws SQLException{
        try {
            ps = cn.getConnection().prepareStatement(SQL_INSERT2);
            ps.setInt(1, cantidad);
            ps.setFloat(2,subtotal);
            ps.setInt(3, compraID);
            ps.setInt(4, inventarioID);
            int res = ps.executeUpdate();
            
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro Guardado..........");
            }
            
            } catch (HeadlessException | SQLException e) {
            System.err.println("Error al guardar en la BD: " + e.getMessage());
            
            } finally {
                ps = null;
                cn.close();
            }
        }*/
        
      
        
        public List Listarcompras(){
       List<Compra> ListaCompra = new ArrayList();
       String sql = "SELECT  c.ID, c.Fecha, c.Total, u.Nombre, p.Nombre FROM libreria1.compra c INNER JOIN libreria1.usuario u ON u.ID = c.Usuario_ID INNER JOIN libreria1.proveedor p ON p.id = c.Proveedor_ID";
       try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           while (rs.next()) {               
               Compra com = new Compra();
               com.setId_compra(rs.getInt("c.ID"));
               com.setFecha(rs.getDate("c.Fecha"));
               com.setTotal(rs.getFloat("c.Total"));
               com.setProveedor(rs.getString("p.Nombre"));
               com.setUsuario(rs.getString("u.Nombre"));
               
               ListaCompra.add(com);
           }
       } catch (SQLException e) {
           System.out.println(e.toString());
       }
       return ListaCompra;
       }
    //fin
}
