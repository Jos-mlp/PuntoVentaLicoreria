package Logica;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginDao {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    ConexionBD cn = new ConexionBD();
    
    public LoginDao(){
    
    }
    
    //Busca al usuario mediante usuario y contraseña
    //Obtiene todos los datos de la ttabla
    public login log(String usuario, String pass){
        login l = new login();
        String sql = "SELECT * FROM libreria1.usuario WHERE Usuario = ? AND Pass = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, pass);
            rs= ps.executeQuery();
            if (rs.next()) {
                l.setId(rs.getInt("ID"));
                l.setNombre(rs.getString("Nombre"));
                l.setPuesto(rs.getString("Puesto"));
                l.setUsuario(rs.getString("Usuario"));
                l.setPass(rs.getString("Pass"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return l;
    }
    
    //registra un usuario mediante un metodo construccion de clase login
    public boolean Registrar(login reg){
        String sql = "INSERT INTO libreria1.usuario (Nombre,Apellido,Puesto,Fecha_Nac,Edad,Salario,Pasword,Usuario) VALUES (?,?,?,?,?,?,?,?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, reg.getNombre());
            ps.setString(2, reg.getPuesto());
            ps.setString(3,reg.getPass());
            ps.setString(4, reg.getUsuario());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        }
    }
    
    //Consulta que lista los usuarios
    public List ListarUsuarios(){
       List<login> Lista = new ArrayList();
       String sql = "SELECT * FROM libreria1.usuario";
       try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           while (rs.next()) {               
               login l = new login();
               l.setId(rs.getInt("id"));
                l.setNombre(rs.getString("Nombre"));
                l.setPuesto(rs.getString("Puesto"));
                l.setUsuario(rs.getString("Usuario"));
                l.setPass(rs.getString("Pasword"));
               Lista.add(l);
           }
       } catch (SQLException e) {
           System.out.println(e.toString());
       }
       return Lista;
   }
}
