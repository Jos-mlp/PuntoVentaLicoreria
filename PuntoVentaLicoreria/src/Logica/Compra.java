package Logica;

import java.sql.Date;
import javax.swing.JOptionPane;


public class Compra {
    private int id_compra;
    private Date fecha;
    private String usuario;
    private String proveedor;
    private float total;
    
    public Compra(){
        
    }
    
    public Compra(int id, Date fecha,String usuario,String proveedor,float total){
        this.id_compra=id;
        this.fecha=fecha;
        this.proveedor = proveedor;
        this.total = total;
        this.usuario=usuario;
    }

    public int getId_compra() {
        return id_compra;
    }

    public void setId_compra(int id_compra) {
        this.id_compra = id_compra;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
    
}
