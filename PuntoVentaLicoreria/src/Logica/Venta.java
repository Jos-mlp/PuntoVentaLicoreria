package Logica;

import java.sql.Date;

public class Venta {
    private int id_venta;
    private Date fecha;
    private String usuario;
    private float total;
    
    public Venta(){
        
    }

    public Venta(int id, String usuario, float total, Date fecha) {
        this.id_venta = id;
        this.usuario = usuario;
        this.total = total;
        this.fecha = fecha;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }   

    
}

