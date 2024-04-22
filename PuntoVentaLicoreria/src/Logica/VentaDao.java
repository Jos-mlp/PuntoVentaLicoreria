package Logica;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mysql.jdbc.Statement;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;



public class VentaDao {
    Connection con;
    private final String SQL_INSERT = "INSERT INTO libreria1.venta(Fecha,Total,Usuario_id,Anulada)" + "VALUES(?,?,?,?)";
    private final String SQL_INSERT2 = "INSERT INTO libreria1.detalleventa(Cantidad,Subtotal,Venta_ID,Inventario_ID)" + "VALUES(?,?,?,?)";
    private PreparedStatement ps;
    private ResultSet rs;;
    private static ResultSet Resultado;
    private final ConexionBD cn = new ConexionBD();
    
    public VentaDao(){
        
    }
    //Buscar
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
        /*public int InsertarVenta(float total, int usuario) throws Exception {
        int id = 0,res=0;
        try {
            ps = cn.getConnection().prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, null);
            ps.setFloat(2, total);
            ps.setInt(3, usuario);
            ps.setInt(4, 0);
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
    }*/
    
    /*public void InsertarDatosVenta(int cantidad, float subtotal, int ventaID, int inventarioID) throws SQLException{
        try {
            ps = cn.getConnection().prepareStatement(SQL_INSERT2);
            ps.setInt(1, cantidad);
            ps.setFloat(2,subtotal);
            ps.setInt(3, ventaID);
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
    
        public List Listarventas(){
       List<Venta> ListaVenta = new ArrayList();
       String sql = "SELECT  v.ID, v.Fecha, v.Total, u.Nombre FROM libreria1.venta v INNER JOIN libreria1.usuario u ON u.ID = v.Usuario_ID";
       try {
           con = cn.getConnection();
           ps = con.prepareStatement(sql);
           rs = ps.executeQuery();
           while (rs.next()) {               
               Venta vent = new Venta();
               vent.setId_venta(rs.getInt("ID"));
               vent.setFecha(rs.getDate("Fecha"));
               vent.setTotal(rs.getFloat("Total"));
               vent.setUsuario(rs.getString("Nombre"));
               ListaVenta.add(vent);
           }
       } catch (SQLException e) {
           System.out.println(e.toString());
       }
       return ListaVenta;
   }
     
            public Venta BuscarVenta(int id){
        Venta cl = new Venta();
        String sql = "SELECT  v.ID, v.Fecha, v.Total, u.Nombre FROM libreria1.venta v INNER JOIN libreria1.usuario u ON u.ID = v.Usuario_ID WHERE v.ID = ?";
        //SELECT * FROM libreria1.venta WHERE id = ?
        //SELECT  v.ID, v.Fecha, v.Total, u.Nombre FROM venta v INNER JOIN usuario u ON u.ID = v.Usuario_ID WHERE v.ID = 1
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cl.setId_venta(rs.getInt("v.ID"));
                cl.setFecha(rs.getDate("v.Fecha"));
                cl.setTotal(rs.getFloat("v.Total"));
                cl.setUsuario(rs.getString("u.Nombre"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return cl;
    }
        
        public void pdfV(int idventa, int Cliente, double total, String usuario) {
        try {
            Date date = new Date();
            FileOutputStream archivo;
            String url = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            File salida = new File(url + "venta.pdf");
            archivo = new FileOutputStream(salida);
            Document doc = new Document();
            PdfWriter.getInstance(doc, archivo);
            doc.open();
            
            //Fecha
            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            fecha.add("Vendedor: " + usuario + "\nFolio: " + idventa + "\nFecha: "
                    + new SimpleDateFormat("dd/MM/yyyy").format(date) + "\n\n");
            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] columnWidthsEncabezado = new float[]{20f, 30f, 70f, 40f};
            Encabezado.setWidths(columnWidthsEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            Encabezado.addCell("");
            //info empresa
                String mensaje = "";
                Encabezado.addCell("Ruc:    " + "0001" + "\nNombre: " + "Libreria Chinita" + "\nTeléfono: " + "77638917" + "\nDirección: " + "2da. Avenida" + "\n\n");
                
            //
            Encabezado.addCell(fecha);
            doc.add(Encabezado);
            
            //DatosUsuario
            Paragraph cli = new Paragraph();
            cli.add(Chunk.NEWLINE);
            cli.add("DATOS DEL USUARIO" + "\n\n");
            doc.add(cli);

            PdfPTable proveedor = new PdfPTable(3);
            proveedor.setWidthPercentage(100);
            proveedor.getDefaultCell().setBorder(0);
            float[] columnWidthsCliente = new float[]{50f, 25f, 25f};
            proveedor.setWidths(columnWidthsCliente);
            proveedor.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell cliNom = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell cliTel = new PdfPCell(new Phrase("Télefono", negrita));
            PdfPCell cliDir = new PdfPCell(new Phrase("Dirección", negrita));
            cliNom.setBorder(Rectangle.NO_BORDER);
            cliTel.setBorder(Rectangle.NO_BORDER);
            cliDir.setBorder(Rectangle.NO_BORDER);
            proveedor.addCell(cliNom);
            proveedor.addCell(cliTel);
            proveedor.addCell(cliDir);
            String prove = "SELECT * FROM libreria1.usuario WHERE id = ?";
            try {
                ps = con.prepareStatement(prove);
                ps.setInt(1, Cliente);
                rs = ps.executeQuery();
                if (rs.next()) {
                    proveedor.addCell(rs.getString("Nombre"));
                    proveedor.addCell(rs.getString("Puesto") + "\n\n");
                } else {
                    proveedor.addCell("Publico en General");
                    proveedor.addCell("S/N");
                    proveedor.addCell("S/N" + "\n\n");
                }

            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            doc.add(proveedor);

            //DetallesVenta
PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.getDefaultCell().setBorder(0);
            float[] columnWidths = new float[]{10f, 50f, 15f, 15f};
            tabla.setWidths(columnWidths);
            tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell c1 = new PdfPCell(new Phrase("Cant.", negrita));
            PdfPCell c2 = new PdfPCell(new Phrase("Descripción.", negrita));
            PdfPCell c3 = new PdfPCell(new Phrase("P. unt.", negrita));
            PdfPCell c4 = new PdfPCell(new Phrase("P. Total", negrita));
            c1.setBorder(Rectangle.NO_BORDER);
            c2.setBorder(Rectangle.NO_BORDER);
            c3.setBorder(Rectangle.NO_BORDER);
            c4.setBorder(Rectangle.NO_BORDER);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tabla.addCell(c1);
            tabla.addCell(c2);
            tabla.addCell(c3);
            tabla.addCell(c4);
            String product = "SELECT d.id, d.Inventario_ID, d.Venta_ID, d.Subtotal, d.cantidad, i.id, i.nombre FROM libreria1.detalleventa d INNER JOIN libreria1.inventario i ON d.Inventario_ID = i.ID WHERE d.Venta_ID = ?";
            try {
                ps = con.prepareStatement(product);
                ps.setInt(1, idventa);
                rs = ps.executeQuery();
                while (rs.next()) {
                    float subTotal = rs.getFloat("d.Subtotal") ;
                    tabla.addCell(rs.getString("d.cantidad"));
                    tabla.addCell(rs.getString("i.nombre"));
                    float precio = (subTotal/rs.getInt("d.cantidad"));
                    tabla.addCell(String.valueOf(precio));
                    tabla.addCell(String.valueOf(subTotal));
                }

            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            
            ////
            doc.add(tabla);
            Paragraph info = new Paragraph();
            info.add(Chunk.NEWLINE);
            info.add("Total Q.: " + total);
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);
            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelacion \n\n");
            firma.add("------------------------------------\n");
            firma.add("Firma \n");
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);
            Paragraph gr = new Paragraph();
            gr.add(Chunk.NEWLINE);
            gr.add(mensaje);
            gr.setAlignment(Element.ALIGN_CENTER);
            doc.add(gr);
            doc.close();
            archivo.close();
            Desktop.getDesktop().open(salida);
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }
    //fin
}
