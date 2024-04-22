
package Logica;

public class Producto {
    private int id;
    private String codigo;
    private int existencia;
    private String nombre;
    private float precio;
    private String marca;
    private String descripcion;


    public Producto(){
        
    }

    public Producto(int id, String codigo, int existencia, String nombre, int precio, String marca, String descripcion){
        this.id = id;
        this.codigo=codigo;
        this.existencia=existencia;
        this.nombre = nombre;
        this.precio=precio;
        this.marca = marca;
        this.descripcion = descripcion;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}