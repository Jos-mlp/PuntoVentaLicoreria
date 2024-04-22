package Forms;
import Logica.Cls_Inventario;
import Logica.Cls_Proveedor;
import Logica.Cls_Usuarios;
import Logica.Compra;
import Logica.CompraDao;
import Logica.ConexionBD;
import Logica.Producto;
import Logica.Venta;
import Logica.VentaDao;
import com.mysql.jdbc.Statement;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public class Interfaz extends javax.swing.JFrame {
    
    
    //Josue
    //Aca empiezan las lineas de codigo que yo coloque
    int xMouse,yMouse;
    int empleado=0;
    String nombreEmpleado="";
    Venta v = new Venta();
    ConexionBD cnx = new ConexionBD();
    
    
        //Variable que controla el total de la venta
           float totalVenta = 0;
    
        //Variable que controla el total de la Compra
            float totalCompra = 0;
        //Crea un JFrame para poder lanzar los inputbox
        JFrame frame = new JFrame("CancelarVenta1");
        DefaultTableModel dtm = new DefaultTableModel();
        DefaultTableModel dtmC = new DefaultTableModel();
        DefaultTableModel modelo = new DefaultTableModel();
        
    public Interfaz() {
        initComponents();
        CP = new Cls_Inventario();
        CPU = new Cls_Usuarios();
        CPP = new Cls_Proveedor();
        Listar();
        ListarU();
        ListarP();
        this.setLocationRelativeTo(null);
        //TablaVenta
        String[] titulo = new String[]{"Codigo","Nombre","Cantidad","Precio","Sub Total"};
        String[] tituloProveedor = new String[]{"Codigo","Nombre","Proveedor","Cantidad","Precio","Sub Total"};
        dtm.setColumnIdentifiers(titulo);
        dtmC.setColumnIdentifiers(tituloProveedor);
        VentaJTable.setModel(dtm); 
         CompraJTable.setModel(dtmC); 
    }
    
    //Jesus
    //Jesus
    private final Cls_Inventario CP;
    private final Cls_Usuarios CPU;
    private final Cls_Proveedor CPP;
    int num = 0;
    int num1=0;
    private void Listar() {
        jtbDatos.setModel(CP.getDatos());
    }
    private void ListarU() {
        jtb_Datos.setModel(CPU.getDatosU());
    }
     private void ListarP() {
        jtb_DatosP.setModel(CPP.getDatosP());
    }
    private void Limpiar() {
        txtID.setText("");
        txtCodigo.setText("");
        txtExistencia.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtMarca.setText("");
        txtDescripcion.setText("");
    }
    private void LimpiarU() {
        txtID.setText("");
        txtNombre1.setText("");
        txtPuesto.setText("");
        txtUsuario.setText("");
        txtPass.setText("");
    }
    private void LimpiarP() {
        txtIDP.setText("");
        txtNombreP.setText("");
        txtDireccionP.setText("");
        txtTelefonoP.setText("");
    }
    Cls_Inventario nuevo=new Cls_Inventario();
    Cls_Usuarios nuevoU =new Cls_Usuarios();
    Cls_Proveedor nuevop = new Cls_Proveedor();
    
    
    
//Josue
    
    VentaDao nuevaVenta = new VentaDao();   
    //TablaVenta
    void AgregarVenta(){
        //Estas lineas sirve para calcular el subtotal = precio * cantidad
        float n1 = Float.parseFloat(PrecioTxt.getText());
        int n2= (Integer) CantidadTxt.getValue();
        float n = n1 * n2;
        //
        dtm.addRow(new Object[]{
        CodigoTxt.getText(),NombreTxt.getText(),CantidadTxt.getValue(),PrecioTxt.getText(),n
        });
        totalVenta += n;
        TotalVentaLabel.setText("Total: " + totalVenta);
    }
    void EliminarVenta(){
        int fila = VentaJTable.getSelectedRow();
        totalVenta = totalVenta - Float.parseFloat(String.valueOf(dtm.getValueAt(fila,4)));
        if(totalVenta<0){
            totalVenta=0;
        }
        dtm.removeRow(fila);
    }
    void ActualizarVenta(){
        int fila=VentaJTable.getSelectedRow();
        totalVenta -= Float.parseFloat(String.valueOf(dtm.getValueAt(fila,4)));
        dtm.setValueAt((String) CodigoTxt.getText(),fila,0);
        dtm.setValueAt(NombreTxt.getText(),fila,1);
        dtm.setValueAt((int) CantidadTxt.getValue(),fila,2);
        dtm.setValueAt(PrecioTxt.getText(),fila,3);
        //Estas lineas sirve para calcular el subtotal = precio * cantidad
        float n1 = (float) Double.parseDouble(PrecioTxt.getText());
        int n2= (Integer) CantidadTxt.getValue();
        float n = (float) (n1 * n2);
        totalVenta += n;
        //
        dtm.setValueAt(n,fila,4);
        TotalVentaLabel.setText("Total: " + totalVenta);
    }
    private void CancelarVenta(){
        //PRegunta si estamos seguros de cancelar la venta
        int result = JOptionPane.showConfirmDialog(frame,"¿Seguro? ¿Quieres cancelar esta venta?", "Cancelar Venta",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
       if(result == JOptionPane.YES_OPTION){
          LimpiarVenta();
          int filas=dtm.getRowCount();
          for(int i=0;i<filas;i++){
            dtm.removeRow(0);
          }
          totalVenta=0;
          TotalVentaLabel.setText("Total: " + totalVenta);
       }
    }
    public void EmpleadoIngreso(int e, String n){
        this.empleado=e;
        this.nombreEmpleado=n;
    }
    private void ConfirmarVenta() throws SQLException, Exception{
        //Aca debemos de colocar las transacciones
        //
        //
        //
        Connection cn = null;
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        int id = 0,res=0;
        ResultSet Resultado;
        String SQL_INSERT = "INSERT INTO libreria1.venta(Fecha,Total,Usuario_id,Anulada)" + "VALUES(?,?,?,?)";
        String SQL_INSERT2 = "INSERT INTO libreria1.detalleventa(Cantidad,Subtotal,Venta_ID,Inventario_ID)" + "VALUES(?,?,?,?)";
        
        try {
            cn = cnx.getConnection();
            cn.setAutoCommit(false);
                //Inserta una nueva venta
                //aca va la parte 1
            ps = cn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, null);
            ps.setFloat(2, totalVenta);
            ps.setInt(3, empleado);
            ps.setInt(4, 0);
            res = ps.executeUpdate();
            Resultado = (ResultSet) ps.getGeneratedKeys();
             if (Resultado.next()) {
                id = Resultado.getInt(1);
             }
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "Registro Guardado..........");
            }else{
                cn.rollback();
                JOptionPane.showMessageDialog(null, "Fallo la transaccion");
            }
                //termina
                //id = nuevaVenta.InsertarVenta(totalVenta,empleado);;
       
            
            
            //Insertar los datos de la venta
            int filas=dtm.getRowCount();
            for(int i=0;i<filas;i++){
              
              //Inserta en la base de datos
                    int cantidad = (int) (dtm.getValueAt(i, 2));
                    float subTotal = (float) (dtm.getValueAt(i, 4));
                    String codVenta = (String) (dtm.getValueAt(i, 0));
                    int codigo = Integer.parseInt(codVenta);
                    //aca va la parte 1
                    ps = cn.prepareStatement(SQL_INSERT2);
                    ps.setInt(1, cantidad);
                    ps.setFloat(2,subTotal);
                    ps.setInt(3, id);
                    ps.setInt(4, codigo);
                    int res2 = ps.executeUpdate();
            
            if (res2 > 0) {
                JOptionPane.showMessageDialog(null, "Registro Guardado..........");
            }else{
                cn.rollback();
                JOptionPane.showMessageDialog(null, "Fallo la transaccion");
            }
                      //termina
                      //nuevaVenta.InsertarDatosVenta(cantidad, subTotal, id, codigo);
               
            }
            int result = JOptionPane.showConfirmDialog(frame,"¿Seguro? ¿Quieres confirmar esta venta?", "Confirmar Venta",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            if(result == JOptionPane.YES_OPTION){
              //Borra toda la linea
                cn.commit();
                filas=dtm.getRowCount();
                for(int i=0;i<filas;i++){
                dtm.removeRow(0);
                }
                LimpiarVenta();

            }else{
                cn.rollback();
                JOptionPane.showMessageDialog(null, "Transaccion Cancelada");
            }
            //Por ultimo tiene que limpiar todo 
       } catch (SQLException ex) {
            cn.rollback();
            JOptionPane.showMessageDialog(null, "Transaccion Cancelada");
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ps = null;
            cn.close();
        }
    }
    private void LimpiarVenta(){
        CodigoTxt.setText("");
        NombreTxt.setText("");
        CantidadTxt.setValue(1);
        PrecioTxt.setText("");
    }

    public void ListarVentas() {
        List<Venta> ListarVenta = nuevaVenta.Listarventas();
        modelo = (DefaultTableModel) HIstorialVentasjTable.getModel();
        Object[] ob = new Object[4];
        //Borra toda la linea
        int filas=modelo.getRowCount();
        for(int i=0;i<filas;i++){
          modelo.removeRow(0);
        }
        for (int i = 0; i < ListarVenta.size(); i++) {
            ob[0] = ListarVenta.get(i).getId_venta();
            ob[1] = ListarVenta.get(i).getUsuario();
            ob[2] = ListarVenta.get(i).getFecha();
            ob[3] = ListarVenta.get(i).getTotal();
            modelo.addRow(ob);
        }
        HIstorialVentasjTable.setModel(modelo);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    CompraDao nuevaCompra = new CompraDao();
   //TablaCompra
    void AgregarCompra(){
        //Estas lineas sirve para calcular el subtotal = precio * cantidad
        float n1 = Float.parseFloat(PrecioCompraTxt.getText());
        int n2= (Integer) CantidadCompraTxt.getValue();
        float n = n1 * n2;
        //
        dtmC.addRow(new Object[]{
        CodigoCompraTxt.getText(),NombreCompraTxt.getText(),CodigoProveedor.getText(),CantidadCompraTxt.getValue(),PrecioCompraTxt.getText(),n
        });
        totalCompra += n;
        TotalCompraLabel.setText("Total: " + totalCompra);
    }
    void EliminarCompra(){
        int fila = CompraJTable.getSelectedRow();
        totalCompra = totalCompra - Float.parseFloat(String.valueOf(dtmC.getValueAt(fila,5)));
        if(totalCompra<0){
            totalCompra=0;
        }
        dtmC.removeRow(fila);
        TotalCompraLabel.setText("Total: " + totalCompra);
    }
    
    void ActualizarCompra(){
        int fila=CompraJTable.getSelectedRow();
        totalCompra -= Float.parseFloat(String.valueOf(dtmC.getValueAt(fila,4)));
        dtmC.setValueAt((String) CodigoCompraTxt.getText(),fila,0);
        dtmC.setValueAt(NombreCompraTxt.getText(),fila,1);
        dtmC.setValueAt(CodigoProveedor.getText(),fila,2);
        dtmC.setValueAt((int) CantidadCompraTxt.getValue(),fila,3);
        dtmC.setValueAt(PrecioCompraTxt.getText(),fila,4);
        //Estas lineas sirve para calcular el subtotal = precio * cantidad
        float n1 = (float) Double.parseDouble(PrecioCompraTxt.getText());
        int n2= (Integer) CantidadCompraTxt.getValue();
        float n = (float) (n1 * n2);
        totalCompra += n;
        //
        dtmC.setValueAt(n,fila,5);
        TotalCompraLabel.setText("Total: " + totalCompra);
    }
    
    private void CancelarCompra(){
        //PRegunta si estamos seguros de cancelar la venta
        int result = JOptionPane.showConfirmDialog(frame,"¿Seguro? ¿Quieres cancelar esta compra?", "Cancelar Compra",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE);
       if(result == JOptionPane.YES_OPTION){
          LimpiarCompra();
          int filas=dtmC.getRowCount();
          for(int i=0;i<filas;i++){
            dtmC.removeRow(0);
          }
          totalCompra=0;
          TotalCompraLabel.setText("Total: " + totalCompra);
       }
    }
    
    private void confirmarCompra() throws SQLException, Exception {
    Connection cn = null;
    PreparedStatement ps = null;
    PreparedStatement ps2 = null;
    int id = 0;
    
    try {
        cn = cnx.getConnection();
        cn.setAutoCommit(false);
        
        // Insertar una nueva compra
        String SQL_INSERT = "INSERT INTO libreria1.compra(Fecha,Total,Usuario_ID,Proveedor_ID) VALUES(?,?,?,?)";
        ps = cn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
        ps.setDate(1, new java.sql.Date(System.currentTimeMillis())); // Establecer la fecha actual
        ps.setFloat(2, totalCompra);
        ps.setInt(3, empleado);
        ps.setInt(4, Integer.parseInt(CodigoProveedor.getText()));
        int res = ps.executeUpdate();
        if (res > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            JOptionPane.showMessageDialog(null, "Compra registrada correctamente.");
        } else {
            throw new SQLException("Fallo al insertar la compra.");
        }
        
        // Insertar los detalles de la compra
        String SQL_INSERT2 = "INSERT INTO libreria1.detallecompra(Cantidad,Subtotal,Compra_ID,Inventario_ID) VALUES(?,?,?,?)";
        ps2 = cn.prepareStatement(SQL_INSERT2);
        int filas = dtmC.getRowCount();
        for (int i = 0; i < filas; i++) {
            int cantidad = (int) dtmC.getValueAt(i, 3);
            float subTotal = (float) dtmC.getValueAt(i, 5);
            int codigo = Integer.parseInt((String) dtmC.getValueAt(i, 0));
            ps2.setInt(1, cantidad);
            ps2.setFloat(2, subTotal);
            ps2.setInt(3, id);
            ps2.setInt(4, codigo);
            ps2.addBatch(); // Agregar a lote para ejecución eficiente
        }
        int[] res2 = ps2.executeBatch();
        for (int result : res2) {
            if (result <= 0) {
                throw new SQLException("Fallo al insertar un detalle de la compra.");
            }
        }
        
        // Confirmar la compra
        int result = JOptionPane.showConfirmDialog(frame, "¿Seguro que quieres confirmar esta compra?", "Confirmar Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            cn.commit();
            CancelarCompra(); // Supongamos que tienes un método para limpiar la tabla de compras
            
        } else {
            cn.rollback();
            JOptionPane.showMessageDialog(null, "Transacción cancelada.");
        }
    } catch (SQLException e) {
        if (cn != null) {
            cn.rollback();
        }
        JOptionPane.showMessageDialog(null, "Error al confirmar la compra: " + e.getMessage());
    } finally {
        if (ps != null) {
            ps.close();
        }
        if (ps2 != null) {
            ps2.close();
        }
        if (cn != null) {
            cn.setAutoCommit(true); // Restaurar el comportamiento predeterminado
            cn.close();
        }
    }
    }

    
    private void LimpiarCompra(){
        CodigoCompraTxt.setText("");
        NombreCompraTxt.setText("");
        CantidadCompraTxt.setValue(1);
        PrecioCompraTxt.setText("");
        totalCompra=0;
    }
    
    public void ListarCompras() {
        List<Compra> ListarCompra = nuevaCompra.Listarcompras();
        modelo = (DefaultTableModel) HIstorialComprasjTable.getModel();
        Object[] ob = new Object[5];
        //Borra toda la linea
        int filas=modelo.getRowCount();
        for(int i=0;i<filas;i++){
          modelo.removeRow(0);
        }
        for (int i = 0; i < ListarCompra.size(); i++) {
            ob[0] = ListarCompra.get(i).getId_compra();
            ob[1] = ListarCompra.get(i).getUsuario();
            ob[2] = ListarCompra.get(i).getProveedor();
            ob[3] = ListarCompra.get(i).getFecha();
            ob[4] = ListarCompra.get(i).getTotal();
            modelo.addRow(ob);
        }
        HIstorialComprasjTable.setModel(modelo);
    }
       
   
    //Cuando entra el mouse se cambia el color del boton panel
    private void EnteredMouse(JPanel panel){
        panel.setBackground(new Color(0,102,204));
        
    }
    
    //Cambie el color cuando el mouse sale del boton panel
    private void ExitMouse(JPanel panel){
        panel.setBackground(new Color(0,153,204));
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        SalirPanel = new javax.swing.JPanel();
        SalirTxt = new javax.swing.JLabel();
        Barra = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        CodigoTxt = new javax.swing.JTextField();
        BuscarButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        NombreTxt = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        PrecioTxt = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        CantidadTxt = new javax.swing.JSpinner();
        AgregarBtn = new javax.swing.JButton();
        EliminarBtn = new javax.swing.JButton();
        ActualizarBtn = new javax.swing.JButton();
        CancelarVentaBtn = new javax.swing.JButton();
        ConfirmarVentaBtn = new javax.swing.JButton();
        TotalVentaLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        VentaJTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtExistencia = new javax.swing.JTextField();
        label1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtPrecio = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtbDatos = new javax.swing.JTable();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jbc_Buscar = new javax.swing.JComboBox<>();
        txtBuscar = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtIDP = new javax.swing.JTextField();
        txtNombreP = new javax.swing.JTextField();
        txtDireccionP = new javax.swing.JTextField();
        txtTelefonoP = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtb_DatosP = new javax.swing.JTable();
        btnAgregarP = new javax.swing.JButton();
        btnModificarP = new javax.swing.JButton();
        btnEliminarP = new javax.swing.JButton();
        btnSalirP = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        CodigoCompraTxt = new javax.swing.JTextField();
        BuscarButton1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        PrecioCompraTxt = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        CantidadCompraTxt = new javax.swing.JSpinner();
        jButton9 = new javax.swing.JButton();
        EliminarBtn4 = new javax.swing.JButton();
        EliminarBtn5 = new javax.swing.JButton();
        EliminarBtn6 = new javax.swing.JButton();
        EliminarBtn7 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        NombreCompraTxt = new javax.swing.JTextField();
        TotalCompraLabel = new javax.swing.JLabel();
        CodigoProveedor = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        CompraJTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtNombre1 = new javax.swing.JTextField();
        txtPuesto = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        txtPass = new javax.swing.JPasswordField();
        btnAgregar1 = new javax.swing.JButton();
        btnModificar1 = new javax.swing.JButton();
        btnEliminar1 = new javax.swing.JButton();
        btnSalir1 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jtb_Datos = new javax.swing.JTable();
        jPanel16 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        OpcionComboBox = new javax.swing.JComboBox<>();
        btnPdfVentas = new javax.swing.JButton();
        txtIdVenta = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        HIstorialVentasjTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        HIstorialComprasjTable = new javax.swing.JTable();
        NuevaVentaBtn = new javax.swing.JPanel();
        NuevaVentaTxt = new javax.swing.JLabel();
        InventarioBtn = new javax.swing.JPanel();
        InventarioTxt = new javax.swing.JLabel();
        ProveedoresBtn = new javax.swing.JPanel();
        ProveedoresTxt = new javax.swing.JLabel();
        ComprasBtn = new javax.swing.JPanel();
        ComprasTxt = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ConfiBtn = new javax.swing.JPanel();
        Configuracion = new javax.swing.JLabel();
        UsuariosBtn = new javax.swing.JPanel();
        UsuariosTxt = new javax.swing.JLabel();
        EmpleadoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(51, 51, 255));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel1MouseEntered(evt);
            }
        });
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentShown(evt);
            }
        });
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SalirPanel.setBackground(new java.awt.Color(255, 255, 255));
        SalirPanel.setName(""); // NOI18N

        SalirTxt.setFont(new java.awt.Font("Roboto Light", 0, 24)); // NOI18N
        SalirTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SalirTxt.setText("X");
        SalirTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        SalirTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SalirTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SalirTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                SalirTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout SalirPanelLayout = new javax.swing.GroupLayout(SalirPanel);
        SalirPanel.setLayout(SalirPanelLayout);
        SalirPanelLayout.setHorizontalGroup(
            SalirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SalirTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );
        SalirPanelLayout.setVerticalGroup(
            SalirPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SalirTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jPanel1.add(SalirPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 30, 30));

        Barra.setBackground(new java.awt.Color(255, 255, 255));
        Barra.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                BarraMouseDragged(evt);
            }
        });
        Barra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                BarraMousePressed(evt);
            }
        });

        javax.swing.GroupLayout BarraLayout = new javax.swing.GroupLayout(Barra);
        Barra.setLayout(BarraLayout);
        BarraLayout.setHorizontalGroup(
            BarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        BarraLayout.setVerticalGroup(
            BarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel1.add(Barra, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, -1));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CodigoTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CodigoTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CodigoTxtMouseClicked(evt);
            }
        });
        CodigoTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CodigoTxtActionPerformed(evt);
            }
        });
        CodigoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoTxtKeyPressed(evt);
            }
        });
        jPanel2.add(CodigoTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 161, -1));

        BuscarButton.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        BuscarButton.setText("Buscar");
        BuscarButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarButtonMouseClicked(evt);
            }
        });
        BuscarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarButtonActionPerformed(evt);
            }
        });
        jPanel2.add(BuscarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 20, -1, -1));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Codigo Producto:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, -1));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Nombre Producto:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, -1, -1));

        NombreTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        NombreTxt.setEnabled(false);
        NombreTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NombreTxtActionPerformed(evt);
            }
        });
        jPanel2.add(NombreTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 161, -1));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Precio:");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));

        PrecioTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        PrecioTxt.setEnabled(false);
        PrecioTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrecioTxtActionPerformed(evt);
            }
        });
        jPanel2.add(PrecioTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 161, -1));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Cantidad:");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, -1, -1));

        CantidadTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CantidadTxt.setValue(1);
        CantidadTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CantidadTxtMouseClicked(evt);
            }
        });
        jPanel2.add(CantidadTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 50, -1));

        AgregarBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        AgregarBtn.setText("Agregar");
        AgregarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AgregarBtnMouseClicked(evt);
            }
        });
        AgregarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarBtnActionPerformed(evt);
            }
        });
        jPanel2.add(AgregarBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, -1, -1));

        EliminarBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EliminarBtn.setText("Eliminar");
        EliminarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EliminarBtnMouseClicked(evt);
            }
        });
        EliminarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBtnActionPerformed(evt);
            }
        });
        jPanel2.add(EliminarBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 110, 100, -1));

        ActualizarBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ActualizarBtn.setText("Actualizar");
        ActualizarBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ActualizarBtnMouseClicked(evt);
            }
        });
        ActualizarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarBtnActionPerformed(evt);
            }
        });
        jPanel2.add(ActualizarBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, -1, -1));

        CancelarVentaBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CancelarVentaBtn.setText("Cancelar Venta");
        CancelarVentaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CancelarVentaBtnMouseClicked(evt);
            }
        });
        CancelarVentaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarVentaBtnActionPerformed(evt);
            }
        });
        jPanel2.add(CancelarVentaBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 120, 150, -1));

        ConfirmarVentaBtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ConfirmarVentaBtn.setText("Confirmar Venta");
        ConfirmarVentaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ConfirmarVentaBtnMouseClicked(evt);
            }
        });
        ConfirmarVentaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmarVentaBtnActionPerformed(evt);
            }
        });
        jPanel2.add(ConfirmarVentaBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 90, -1, -1));

        TotalVentaLabel.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        TotalVentaLabel.setText("Total: 0");
        jPanel2.add(TotalVentaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, -1, -1));

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 160));

        VentaJTable.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        VentaJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", "", null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Cantidad", "Precio", "Sub Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(VentaJTable);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 1060, 430));

        jTabbedPane1.addTab("tab1", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("INVENTARIO");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(509, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addGap(404, 404, 404))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 40));

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel21.setText("ID");

        txtID.setEditable(false);
        txtID.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel22.setText("Codigo");

        txtCodigo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel23.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel23.setText("Existencia");

        txtExistencia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        label1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        label1.setText("Nombre");

        txtNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel24.setText("Precio");

        txtPrecio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel25.setText("Marca");

        txtMarca.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel26.setText("Descripcion");

        txtDescripcion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(label1)
                            .addComponent(jLabel24)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtMarca, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtDescripcion)))
                .addContainerGap(245, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel25)
                    .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 680, 280));

        jtbDatos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtbDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtbDatosMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jtbDatos);

        jPanel10.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 1010, 240));

        btnAgregar.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_save_80px_2.png"))); // NOI18N
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel10.add(btnAgregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 160, 80, 80));

        btnEliminar.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_delete_document_80px.png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        jPanel10.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 250, 90, 80));

        btnModificar.setBackground(new java.awt.Color(0, 0, 0));
        btnModificar.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_edit_property_80px.png"))); // NOI18N
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel10.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 160, 90, 80));

        btnSalir.setBackground(new java.awt.Color(0, 0, 0));
        btnSalir.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_close_window_96px.png"))); // NOI18N
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel10.add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 250, 80, 80));

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Buscar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        jbc_Buscar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Codigo", " " }));

        txtBuscar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbc_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbc_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, 330, 100));

        jPanel4.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTabbedPane1.addTab("tab2", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(0, 0, 0));

        jLabel27.setBackground(new java.awt.Color(255, 255, 255));
        jLabel27.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("PROVEEDORES");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(473, Short.MAX_VALUE)
                .addComponent(jLabel27)
                .addGap(404, 404, 404))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 40));

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 2, 12))); // NOI18N

        jLabel28.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel28.setText("Nombre");

        jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel29.setText("Dirección");

        jLabel30.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel30.setText("Telefono");

        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setText("ID");

        txtIDP.setEditable(false);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTelefonoP, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(txtDireccionP))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDP, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtIDP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtNombreP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtDireccionP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtTelefonoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(45, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 350, 220));

        jtb_DatosP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtb_DatosP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtb_DatosPMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jtb_DatosP);

        jPanel14.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 1000, 280));

        btnAgregarP.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregarP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAgregarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_save_80px_2.png"))); // NOI18N
        btnAgregarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPActionPerformed(evt);
            }
        });
        jPanel14.add(btnAgregarP, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, 80, 80));

        btnModificarP.setBackground(new java.awt.Color(0, 0, 0));
        btnModificarP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnModificarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_edit_property_80px.png"))); // NOI18N
        btnModificarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarPActionPerformed(evt);
            }
        });
        jPanel14.add(btnModificarP, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 70, 90, 80));

        btnEliminarP.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminarP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEliminarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_delete_document_80px.png"))); // NOI18N
        btnEliminarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPActionPerformed(evt);
            }
        });
        jPanel14.add(btnEliminarP, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 170, 90, 80));

        btnSalirP.setBackground(new java.awt.Color(0, 0, 0));
        btnSalirP.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSalirP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_close_window_96px.png"))); // NOI18N
        btnSalirP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirPActionPerformed(evt);
            }
        });
        jPanel14.add(btnSalirP, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 80, 80));

        jPanel5.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTabbedPane1.addTab("tab3", jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanel8MouseEntered(evt);
            }
        });
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CodigoCompraTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CodigoCompraTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CodigoCompraTxtActionPerformed(evt);
            }
        });
        CodigoCompraTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoCompraTxtKeyPressed(evt);
            }
        });
        jPanel8.add(CodigoCompraTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 161, -1));

        BuscarButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        BuscarButton1.setText("Buscar");
        BuscarButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BuscarButton1MouseClicked(evt);
            }
        });
        BuscarButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarButton1ActionPerformed(evt);
            }
        });
        jPanel8.add(BuscarButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Codigo Producto:");
        jPanel8.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, -1, -1));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Precio:");
        jPanel8.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 80, -1, -1));

        PrecioCompraTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        PrecioCompraTxt.setEnabled(false);
        PrecioCompraTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrecioCompraTxtActionPerformed(evt);
            }
        });
        jPanel8.add(PrecioCompraTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 161, -1));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Codigo Proveedor:");
        jLabel15.setToolTipText("");
        jPanel8.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, -1, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Cantidad:");
        jPanel8.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, -1, -1));

        CantidadCompraTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CantidadCompraTxt.setValue(1);
        CantidadCompraTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CantidadCompraTxtMouseClicked(evt);
            }
        });
        jPanel8.add(CantidadCompraTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, 50, -1));

        jButton9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton9.setText("Agregar");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel8.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, -1, -1));

        EliminarBtn4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EliminarBtn4.setText("Eliminar");
        EliminarBtn4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EliminarBtn4MouseClicked(evt);
            }
        });
        EliminarBtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBtn4ActionPerformed(evt);
            }
        });
        jPanel8.add(EliminarBtn4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 110, 100, -1));

        EliminarBtn5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EliminarBtn5.setText("Actualizar");
        EliminarBtn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EliminarBtn5MouseClicked(evt);
            }
        });
        EliminarBtn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBtn5ActionPerformed(evt);
            }
        });
        jPanel8.add(EliminarBtn5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, -1, -1));

        EliminarBtn6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EliminarBtn6.setText("Cancelar Compra");
        EliminarBtn6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EliminarBtn6MouseClicked(evt);
            }
        });
        EliminarBtn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBtn6ActionPerformed(evt);
            }
        });
        jPanel8.add(EliminarBtn6, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 120, 170, -1));

        EliminarBtn7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EliminarBtn7.setText("Confirmar Compra");
        EliminarBtn7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EliminarBtn7MouseClicked(evt);
            }
        });
        EliminarBtn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarBtn7ActionPerformed(evt);
            }
        });
        jPanel8.add(EliminarBtn7, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 90, 170, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Nombre Producto:");
        jPanel8.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, -1, -1));

        NombreCompraTxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        NombreCompraTxt.setEnabled(false);
        NombreCompraTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NombreCompraTxtActionPerformed(evt);
            }
        });
        jPanel8.add(NombreCompraTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 50, 161, -1));

        TotalCompraLabel.setFont(new java.awt.Font("Roboto", 0, 18)); // NOI18N
        TotalCompraLabel.setText("Total: 0");
        jPanel8.add(TotalCompraLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 20, 150, 30));

        CodigoProveedor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CodigoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CodigoProveedorActionPerformed(evt);
            }
        });
        CodigoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CodigoProveedorKeyPressed(evt);
            }
        });
        jPanel8.add(CodigoProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 30, 161, -1));

        jPanel6.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 160));

        CompraJTable.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        CompraJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", "", null, "", null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Proveedor", "Cantidad", "Precio", "Sub Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CompraJTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CompraJTableMouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(CompraJTable);

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 1060, 430));

        jTabbedPane1.addTab("tab4", jPanel6);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBackground(new java.awt.Color(51, 51, 51));

        jLabel32.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("USUARIOS");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(497, Short.MAX_VALUE)
                .addComponent(jLabel32)
                .addGap(458, 458, 458))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1050, 50));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel18.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel18.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, -1));

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 0, 12))); // NOI18N

        jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel33.setText("ID");

        jLabel34.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel34.setText("Nombre");

        jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel35.setText("Puesto");

        jLabel36.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel36.setText("Usuario");

        jLabel37.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel37.setText("Password");

        txtId.setEditable(false);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel37)
                        .addGap(18, 18, 18)
                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(txtNombre1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(txtPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jPanel18.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 480, 270));

        btnAgregar1.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregar1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAgregar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_save_80px_2.png"))); // NOI18N
        btnAgregar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregar1ActionPerformed(evt);
            }
        });
        jPanel18.add(btnAgregar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 70, 80, 80));

        btnModificar1.setBackground(new java.awt.Color(0, 0, 0));
        btnModificar1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnModificar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_edit_property_80px.png"))); // NOI18N
        btnModificar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificar1ActionPerformed(evt);
            }
        });
        jPanel18.add(btnModificar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 70, 90, 80));

        btnEliminar1.setBackground(new java.awt.Color(0, 0, 0));
        btnEliminar1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEliminar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_delete_document_80px.png"))); // NOI18N
        btnEliminar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminar1ActionPerformed(evt);
            }
        });
        jPanel18.add(btnEliminar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 90, 80));

        btnSalir1.setBackground(new java.awt.Color(0, 0, 0));
        btnSalir1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSalir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_close_window_96px.png"))); // NOI18N
        btnSalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalir1ActionPerformed(evt);
            }
        });
        jPanel18.add(btnSalir1, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 170, 80, 80));

        jtb_Datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtb_Datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtb_DatosMouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jtb_Datos);

        jPanel18.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, 1010, 220));

        jPanel7.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jTabbedPane1.addTab("tab5", jPanel7);

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel18.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel18.setText("Factura Seleccionada:");
        jPanel9.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 40, -1, -1));

        OpcionComboBox.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        OpcionComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ventas", "Compras" }));
        OpcionComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                OpcionComboBoxItemStateChanged(evt);
            }
        });
        OpcionComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpcionComboBoxMouseClicked(evt);
            }
        });
        jPanel9.add(OpcionComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 40, -1, 30));

        btnPdfVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Print.png"))); // NOI18N
        btnPdfVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfVentasActionPerformed(evt);
            }
        });
        jPanel9.add(btnPdfVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 20, 70, 70));
        jPanel9.add(txtIdVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 40, 50, 30));

        jLabel38.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel38.setText("Opcion:");
        jPanel9.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, -1, -1));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 60, 50));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 60, 50));

        jPanel16.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 100));

        HIstorialVentasjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Usuario", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        HIstorialVentasjTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HIstorialVentasjTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(HIstorialVentasjTable);

        jPanel16.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1060, 500));

        HIstorialComprasjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Usuario", "Proveedor", "Fecha", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(HIstorialComprasjTable);

        jPanel16.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 1060, 490));

        jTabbedPane1.addTab("tab6", jPanel16);

        jPanel1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, 1060, 620));

        NuevaVentaBtn.setBackground(new java.awt.Color(0, 153, 204));

        NuevaVentaTxt.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        NuevaVentaTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NuevaVentaTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/NuevaVenta.png"))); // NOI18N
        NuevaVentaTxt.setText("NUEVA VENTA");
        NuevaVentaTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        NuevaVentaTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NuevaVentaTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                NuevaVentaTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                NuevaVentaTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout NuevaVentaBtnLayout = new javax.swing.GroupLayout(NuevaVentaBtn);
        NuevaVentaBtn.setLayout(NuevaVentaBtnLayout);
        NuevaVentaBtnLayout.setHorizontalGroup(
            NuevaVentaBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NuevaVentaBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(NuevaVentaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        NuevaVentaBtnLayout.setVerticalGroup(
            NuevaVentaBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, NuevaVentaBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(NuevaVentaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(NuevaVentaBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 220, 70));

        InventarioBtn.setBackground(new java.awt.Color(0, 153, 204));

        InventarioTxt.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        InventarioTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        InventarioTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Inventario.png"))); // NOI18N
        InventarioTxt.setText("INVENTARIO");
        InventarioTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        InventarioTxt.setMaximumSize(new java.awt.Dimension(136, 64));
        InventarioTxt.setMinimumSize(new java.awt.Dimension(136, 64));
        InventarioTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InventarioTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                InventarioTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                InventarioTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout InventarioBtnLayout = new javax.swing.GroupLayout(InventarioBtn);
        InventarioBtn.setLayout(InventarioBtnLayout);
        InventarioBtnLayout.setHorizontalGroup(
            InventarioBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InventarioBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(InventarioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        InventarioBtnLayout.setVerticalGroup(
            InventarioBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InventarioBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(InventarioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(InventarioBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 360, -1, -1));

        ProveedoresBtn.setBackground(new java.awt.Color(0, 153, 204));

        ProveedoresTxt.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ProveedoresTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ProveedoresTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Proveedores.png"))); // NOI18N
        ProveedoresTxt.setText("PROVEEDORES");
        ProveedoresTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ProveedoresTxt.setMaximumSize(new java.awt.Dimension(136, 64));
        ProveedoresTxt.setMinimumSize(new java.awt.Dimension(136, 64));
        ProveedoresTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ProveedoresTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ProveedoresTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ProveedoresTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout ProveedoresBtnLayout = new javax.swing.GroupLayout(ProveedoresBtn);
        ProveedoresBtn.setLayout(ProveedoresBtnLayout);
        ProveedoresBtnLayout.setHorizontalGroup(
            ProveedoresBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProveedoresBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ProveedoresTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ProveedoresBtnLayout.setVerticalGroup(
            ProveedoresBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ProveedoresBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ProveedoresTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(ProveedoresBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, -1, -1));

        ComprasBtn.setBackground(new java.awt.Color(0, 153, 204));

        ComprasTxt.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ComprasTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ComprasTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Compra.png"))); // NOI18N
        ComprasTxt.setText("COMPRAS");
        ComprasTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ComprasTxt.setMaximumSize(new java.awt.Dimension(136, 64));
        ComprasTxt.setMinimumSize(new java.awt.Dimension(136, 64));
        ComprasTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ComprasTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ComprasTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ComprasTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout ComprasBtnLayout = new javax.swing.GroupLayout(ComprasBtn);
        ComprasBtn.setLayout(ComprasBtnLayout);
        ComprasBtnLayout.setHorizontalGroup(
            ComprasBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ComprasBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ComprasTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ComprasBtnLayout.setVerticalGroup(
            ComprasBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ComprasBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ComprasTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(ComprasBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, -1, -1));

        jLabel2.setFont(new java.awt.Font("Roboto Black", 0, 72)); // NOI18N
        jLabel2.setText("LICORERIA");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, -1, -1));

        ConfiBtn.setBackground(new java.awt.Color(0, 153, 204));

        Configuracion.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        Configuracion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Configuracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/Configuracion.png"))); // NOI18N
        Configuracion.setText("COMPRAS Y VENTAS");
        Configuracion.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Configuracion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ConfiguracionMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ConfiguracionMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ConfiguracionMouseExited(evt);
            }
        });

        javax.swing.GroupLayout ConfiBtnLayout = new javax.swing.GroupLayout(ConfiBtn);
        ConfiBtn.setLayout(ConfiBtnLayout);
        ConfiBtnLayout.setHorizontalGroup(
            ConfiBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConfiBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Configuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        ConfiBtnLayout.setVerticalGroup(
            ConfiBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ConfiBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Configuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(ConfiBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 720, -1, -1));

        UsuariosBtn.setBackground(new java.awt.Color(0, 153, 204));

        UsuariosTxt.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        UsuariosTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UsuariosTxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/User.png"))); // NOI18N
        UsuariosTxt.setText("USUARIOS");
        UsuariosTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        UsuariosTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UsuariosTxtMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                UsuariosTxtMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                UsuariosTxtMouseExited(evt);
            }
        });

        javax.swing.GroupLayout UsuariosBtnLayout = new javax.swing.GroupLayout(UsuariosBtn);
        UsuariosBtn.setLayout(UsuariosBtnLayout);
        UsuariosBtnLayout.setHorizontalGroup(
            UsuariosBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsuariosBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(UsuariosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        UsuariosBtnLayout.setVerticalGroup(
            UsuariosBtnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, UsuariosBtnLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(UsuariosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(UsuariosBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 630, -1, -1));

        EmpleadoLabel.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        EmpleadoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        EmpleadoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/userInterfaz.png"))); // NOI18N
        EmpleadoLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        EmpleadoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmpleadoLabelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                EmpleadoLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                EmpleadoLabelMouseExited(evt);
            }
        });
        jPanel1.add(EmpleadoLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 30, 270, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SalirTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalirTxtMouseClicked
        System.exit(0);
    }//GEN-LAST:event_SalirTxtMouseClicked

    private void SalirTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalirTxtMouseEntered
        SalirPanel.setBackground(Color.red);
        SalirTxt.setForeground(Color.white);
    }//GEN-LAST:event_SalirTxtMouseEntered

    private void SalirTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalirTxtMouseExited
        SalirPanel.setBackground(Color.white);
        SalirTxt.setForeground(Color.black);
    }//GEN-LAST:event_SalirTxtMouseExited

    private void BarraMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BarraMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x-xMouse,y-yMouse);

    }//GEN-LAST:event_BarraMouseDragged

    private void BarraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BarraMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_BarraMousePressed

    private void NuevaVentaTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NuevaVentaTxtMouseClicked
       jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_NuevaVentaTxtMouseClicked

    private void InventarioTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventarioTxtMouseClicked
         jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_InventarioTxtMouseClicked

    private void ProveedoresTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProveedoresTxtMouseClicked
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_ProveedoresTxtMouseClicked

    private void ComprasTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComprasTxtMouseClicked
        jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_ComprasTxtMouseClicked

    private void ConfiguracionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ConfiguracionMouseClicked
        jTabbedPane1.setSelectedIndex(5);
        jScrollPane4.setVisible(false);
        ListarVentas();

    }//GEN-LAST:event_ConfiguracionMouseClicked

    private void NuevaVentaTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NuevaVentaTxtMouseEntered
        EnteredMouse(NuevaVentaBtn);
    }//GEN-LAST:event_NuevaVentaTxtMouseEntered

    private void NuevaVentaTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NuevaVentaTxtMouseExited
        ExitMouse(NuevaVentaBtn);
    }//GEN-LAST:event_NuevaVentaTxtMouseExited

    private void InventarioTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventarioTxtMouseEntered
        EnteredMouse(InventarioBtn);
    }//GEN-LAST:event_InventarioTxtMouseEntered

    private void ProveedoresTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProveedoresTxtMouseEntered
        EnteredMouse(ProveedoresBtn);
    }//GEN-LAST:event_ProveedoresTxtMouseEntered

    private void ComprasTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComprasTxtMouseEntered
        EnteredMouse(ComprasBtn);
    }//GEN-LAST:event_ComprasTxtMouseEntered

    private void ConfiguracionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ConfiguracionMouseEntered
        EnteredMouse(ConfiBtn);
    }//GEN-LAST:event_ConfiguracionMouseEntered

    private void InventarioTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventarioTxtMouseExited
        ExitMouse(InventarioBtn);
    }//GEN-LAST:event_InventarioTxtMouseExited

    private void ProveedoresTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ProveedoresTxtMouseExited
        ExitMouse(ProveedoresBtn);
    }//GEN-LAST:event_ProveedoresTxtMouseExited

    private void ComprasTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ComprasTxtMouseExited
        ExitMouse(ComprasBtn);
    }//GEN-LAST:event_ComprasTxtMouseExited

    private void ConfiguracionMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ConfiguracionMouseExited
        ExitMouse(ConfiBtn);
    }//GEN-LAST:event_ConfiguracionMouseExited

    private void UsuariosTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsuariosTxtMouseClicked
        jTabbedPane1.setSelectedIndex(4);
    }//GEN-LAST:event_UsuariosTxtMouseClicked

    private void UsuariosTxtMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsuariosTxtMouseEntered
        EnteredMouse(UsuariosBtn);
    }//GEN-LAST:event_UsuariosTxtMouseEntered

    private void UsuariosTxtMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsuariosTxtMouseExited
        ExitMouse(UsuariosBtn);
    }//GEN-LAST:event_UsuariosTxtMouseExited

    private void EliminarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBtnActionPerformed
       
    }//GEN-LAST:event_EliminarBtnActionPerformed

    private void PrecioTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrecioTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrecioTxtActionPerformed

    private void NombreTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NombreTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NombreTxtActionPerformed

    private void BuscarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuscarButtonActionPerformed

    private void BuscarButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarButtonMouseClicked
        String nombreI=null;
        float precioI=0;
        if(!"".equals(CodigoTxt.getText())){
            String code = CodigoTxt.getText();
            Producto encon = nuevaVenta.BuscarProducto(code);
            nombreI = encon.getNombre();
            precioI = encon.getPrecio();
            if(nombreI != null){  
                NombreTxt.setText(nombreI);
                PrecioTxt.setText(""+precioI);
            }
        }
    }//GEN-LAST:event_BuscarButtonMouseClicked

    private void CodigoTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoTxtKeyPressed
        String nombreI=null;
        float precioI=0;
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            if(!"".equals(CodigoTxt.getText())){
                String code = CodigoTxt.getText();
                Producto encon = nuevaVenta.BuscarProducto(code);
                nombreI = encon.getNombre();
                precioI = encon.getPrecio();
                if(nombreI != null){
                    NombreTxt.setText(nombreI);
                    PrecioTxt.setText(""+precioI);
                }
            }
        }
    }//GEN-LAST:event_CodigoTxtKeyPressed

    private void CodigoTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodigoTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodigoTxtActionPerformed

    private void CantidadTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CantidadTxtMouseClicked

    }//GEN-LAST:event_CantidadTxtMouseClicked

    private void EliminarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EliminarBtnMouseClicked
       EliminarVenta();
       
    }//GEN-LAST:event_EliminarBtnMouseClicked

    private void AgregarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AgregarBtnMouseClicked
         AgregarVenta();
         LimpiarVenta();
    }//GEN-LAST:event_AgregarBtnMouseClicked

    private void AgregarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarBtnActionPerformed
  
    }//GEN-LAST:event_AgregarBtnActionPerformed

    private void ActualizarBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ActualizarBtnMouseClicked
        ActualizarVenta();
         LimpiarVenta();
    }//GEN-LAST:event_ActualizarBtnMouseClicked

    private void ActualizarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarBtnActionPerformed
       
    }//GEN-LAST:event_ActualizarBtnActionPerformed

    private void CancelarVentaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarVentaBtnMouseClicked
        CancelarVenta();
    }//GEN-LAST:event_CancelarVentaBtnMouseClicked

    private void CancelarVentaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarVentaBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CancelarVentaBtnActionPerformed

    private void ConfirmarVentaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ConfirmarVentaBtnMouseClicked
        
    }//GEN-LAST:event_ConfirmarVentaBtnMouseClicked

    private void ConfirmarVentaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmarVentaBtnActionPerformed
 
        try {
            ConfirmarVenta();
        } catch (Exception ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }//GEN-LAST:event_ConfirmarVentaBtnActionPerformed

    private void CodigoCompraTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodigoCompraTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodigoCompraTxtActionPerformed

    private void CodigoCompraTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoCompraTxtKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodigoCompraTxtKeyPressed

    private void BuscarButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BuscarButton1MouseClicked
        String nombreI=null;
        float precioI=0;
        if(!"".equals(CodigoCompraTxt.getText())){
            String code = CodigoCompraTxt.getText();
            Producto encon = nuevaCompra.BuscarProducto(code);
            nombreI = encon.getNombre();
            precioI = encon.getPrecio();
            if(nombreI != null){  
                NombreCompraTxt.setText(nombreI);
                PrecioCompraTxt.setText(""+precioI);
            }
        }
    }//GEN-LAST:event_BuscarButton1MouseClicked

    private void BuscarButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuscarButton1ActionPerformed

    private void PrecioCompraTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrecioCompraTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PrecioCompraTxtActionPerformed

    private void CantidadCompraTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CantidadCompraTxtMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_CantidadCompraTxtMouseClicked

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        AgregarCompra();
    }//GEN-LAST:event_jButton9MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void EliminarBtn4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EliminarBtn4MouseClicked
        EliminarCompra();
    }//GEN-LAST:event_EliminarBtn4MouseClicked

    private void EliminarBtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBtn4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EliminarBtn4ActionPerformed

    private void EliminarBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EliminarBtn5MouseClicked
        ActualizarCompra();
    }//GEN-LAST:event_EliminarBtn5MouseClicked

    private void EliminarBtn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBtn5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EliminarBtn5ActionPerformed

    private void EliminarBtn6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EliminarBtn6MouseClicked
        CancelarCompra();
    }//GEN-LAST:event_EliminarBtn6MouseClicked

    private void EliminarBtn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBtn6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EliminarBtn6ActionPerformed

    private void EliminarBtn7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EliminarBtn7MouseClicked
        
        try {
            confirmarCompra();
        } catch (Exception ex) {
            Logger.getLogger(Interfaz.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_EliminarBtn7MouseClicked

    private void EliminarBtn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarBtn7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EliminarBtn7ActionPerformed

    private void CodigoTxtMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CodigoTxtMouseClicked
        LimpiarVenta();    
    }//GEN-LAST:event_CodigoTxtMouseClicked

    private void EmpleadoLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmpleadoLabelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_EmpleadoLabelMouseClicked

    private void EmpleadoLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmpleadoLabelMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_EmpleadoLabelMouseEntered

    private void EmpleadoLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmpleadoLabelMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_EmpleadoLabelMouseExited

    private void jPanel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseEntered
        EmpleadoLabel.setText(nombreEmpleado);
    }//GEN-LAST:event_jPanel1MouseEntered

    private void NombreCompraTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NombreCompraTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NombreCompraTxtActionPerformed

    private void jPanel1ComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentHidden
       
    }//GEN-LAST:event_jPanel1ComponentHidden

    private void jPanel1ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentShown
       
    }//GEN-LAST:event_jPanel1ComponentShown

    private void jPanel8MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel8MouseEntered
        
    }//GEN-LAST:event_jPanel8MouseEntered

    private void CompraJTableMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CompraJTableMouseEntered
       
    }//GEN-LAST:event_CompraJTableMouseEntered

    private void OpcionComboBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpcionComboBoxMouseClicked
        
    }//GEN-LAST:event_OpcionComboBoxMouseClicked

    private void OpcionComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_OpcionComboBoxItemStateChanged
       if (OpcionComboBox.getSelectedItem() == "Ventas"){
            jScrollPane4.setVisible(false);
            jScrollPane3.setVisible(true);
            ListarVentas();
        }else if(OpcionComboBox.getSelectedItem() == "Compras"){
            jScrollPane3.setVisible(false);
            jScrollPane4.setVisible(true);
            ListarCompras();
        }
    }//GEN-LAST:event_OpcionComboBoxItemStateChanged

    private void CodigoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CodigoProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CodigoProveedorActionPerformed

    private void CodigoProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CodigoProveedorKeyPressed
        
    }//GEN-LAST:event_CodigoProveedorKeyPressed

    private void jtbDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtbDatosMouseClicked
        // TODO add your handling code here:
        //Evento Click Inventario
        int row = jtbDatos.getSelectedRow();
        txtID.setText(jtbDatos.getValueAt(row, 0).toString());
        txtCodigo.setText(jtbDatos.getValueAt(row, 1).toString());
        txtExistencia.setText(jtbDatos.getValueAt(row, 2).toString());
        txtNombre.setText(jtbDatos.getValueAt(row, 3).toString());
        txtPrecio.setText(jtbDatos.getValueAt(row, 4).toString());
        txtMarca.setText(jtbDatos.getValueAt(row, 5).toString());
        txtDescripcion.setText(jtbDatos.getValueAt(row, 6).toString());
        num = 1;
    }//GEN-LAST:event_jtbDatosMouseClicked

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        //Boton Agregar Inventario
        String codigo = txtCodigo.getText();
        int existencia = Integer.parseInt(txtExistencia.getText());
        String nombre = txtNombre.getText();
        float precio = Float.parseFloat(txtPrecio.getText());
        String marca = txtMarca.getText();
        String descripcion = txtDescripcion.getText();
        try {
            int respuesta = CP.InsertDatos(codigo, existencia, nombre, precio, marca, descripcion);
            Listar();
            Limpiar();
        } catch (Exception ex) {
            Logger.getLogger(Cls_Inventario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        //Boton Eliminar Inventario
        int fila = jtbDatos.getSelectedRow();
        if (fila < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
        } else {
            if (CP.EliminarDatos((int) jtbDatos.getValueAt(jtbDatos.getSelectedRow(), 0)) > 0) {
                Limpiar();
                Listar();
            }

        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        //Boton Modificar Inventario
        int ID = Integer.parseInt(txtID.getText());
        String codigo = txtCodigo.getText();
        int existencia = Integer.parseInt(txtExistencia.getText());
        String nombre = txtNombre.getText();
        float precio = Float.parseFloat(txtPrecio.getText());
        String marca = txtMarca.getText();
        String descripcion = txtDescripcion.getText();
        if (num == 0) {

            int respuesta = 0;
            try {
                respuesta = CP.InsertDatos(codigo, existencia, nombre, precio, marca, descripcion);
            } catch (Exception ex) {
                Logger.getLogger(Cls_Inventario.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (respuesta > 0) {
                Listar();
                Limpiar();
            }
        } else {
            int respuesta = 0;
            try {
                respuesta = CP.ActualizarDatos(ID, codigo, existencia, nombre, precio, marca, descripcion);
            } catch (Exception ex) {
                Logger.getLogger(Cls_Inventario.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (respuesta > 0) {
                Listar();
                Limpiar();
                num = 0;
            }
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        //Boton Salir Inventario
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        //Eento key par ainventario
        nuevo.getDatos();
        char tecla=evt.getKeyChar();
        if(tecla == KeyEvent.VK_ENTER)
        {
            jtbDatos.setModel(nuevo.getDato(jbc_Buscar.getSelectedIndex(), txtBuscar.getText()));
        }
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void jtb_DatosPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtb_DatosPMouseClicked
        // TODO add your handling code here:
        int row = jtb_DatosP.getSelectedRow();
        txtIDP.setText(jtb_DatosP.getValueAt(row, 0).toString());
        txtNombreP.setText(jtb_DatosP.getValueAt(row, 1).toString());
        txtDireccionP.setText(jtb_DatosP.getValueAt(row, 2).toString());
        txtTelefonoP.setText(jtb_DatosP.getValueAt(row, 3).toString());
        num = 1;
    }//GEN-LAST:event_jtb_DatosPMouseClicked

    private void btnAgregarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPActionPerformed
        // TODO add your handling code here:
        String nombre = txtNombreP.getText();
        String direccion = txtDireccionP.getText();
        String telefono = txtTelefonoP.getText();
        try {
            int respuesta = CPP.InsertDatosP(nombre, direccion, telefono);
            ListarP();
            LimpiarP();
        } catch (Exception ex) {
            Logger.getLogger(Cls_Proveedor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnAgregarPActionPerformed

    private void btnModificarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarPActionPerformed
        // TODO add your handling code here:
        int ID = Integer.parseInt(txtIDP.getText());
        String nombre = txtNombreP.getText();
        String direccion = txtDireccionP.getText();
        String telefono = txtTelefonoP.getText();
        if (num == 0) {

            int respuesta = 0;
            try {
                respuesta = CPP.InsertDatosP(nombre, direccion, telefono);
            } catch (Exception ex) {
                Logger.getLogger(Cls_Proveedor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (respuesta > 0) {
                ListarP();
                LimpiarP();
            }
        } else {
            int respuesta = 0;
            try {
                respuesta = CPP.ActualizarDatosP(ID, nombre, direccion, telefono);
            } catch (Exception ex) {
                Logger.getLogger(Cls_Proveedor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (respuesta > 0) {
                ListarP();
                LimpiarP();
                num = 0;
            }
        }
    }//GEN-LAST:event_btnModificarPActionPerformed

    private void btnEliminarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPActionPerformed
        // TODO add your handling code here:
        int fila = jtb_DatosP.getSelectedRow();
        if (fila < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
        } else {
            if (CPP.EliminarDatosP((int) jtb_DatosP.getValueAt(jtb_DatosP.getSelectedRow(), 0)) > 0) {
                LimpiarP();
                ListarP();
            }

        }
    }//GEN-LAST:event_btnEliminarPActionPerformed

    private void btnSalirPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirPActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnSalirPActionPerformed

    private void btnAgregar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregar1ActionPerformed
        // TODO add your handling code here:
        String nombre = txtNombre1.getText();
        String puesto = txtPuesto.getText();
        String Usuario = txtUsuario.getText();
        String pass = txtPass.getText();
        try {
            int respuesta = CPU.InsertDatosU(nombre, puesto, Usuario, pass);
            ListarU();
            LimpiarU();
        } catch (Exception ex) {
            Logger.getLogger(Cls_Usuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAgregar1ActionPerformed

    private void btnModificar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificar1ActionPerformed
        // TODO add your handling code here:
        int ID = Integer.parseInt(txtID.getText());
        String nombre = txtNombre1.getText();
        String puesto = txtPuesto.getText();
        String usuario = txtUsuario.getText();
        String pass = txtPass.getText();
        if (num == 0) {

            int respuesta = 0;
            try {
                respuesta = CPU.InsertDatosU(nombre, puesto, usuario, pass);
            } catch (Exception ex) {
                Logger.getLogger(Cls_Usuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (respuesta > 0) {
                ListarU();
                LimpiarU();
            }
        } else {
            int respuesta = 0;
            try {
                respuesta = CPU.ActualizarDatosU(ID, nombre, puesto, usuario, pass);
            } catch (Exception ex) {
                Logger.getLogger(Cls_Usuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (respuesta > 0) {
                ListarU();
                LimpiarU();
                num = 0;
            }
        }
    }//GEN-LAST:event_btnModificar1ActionPerformed

    private void btnEliminar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminar1ActionPerformed
        // TODO add your handling code here:
        int fila = jtb_Datos.getSelectedRow();
        if (fila < 1) {
            JOptionPane.showMessageDialog(null, "Seleccione un registro de la tabla");
        } else {
            if (CPU.EliminarDatosU((int) jtb_Datos.getValueAt(jtb_Datos.getSelectedRow(), 0)) > 0) {
                LimpiarU();
                ListarU();
            }

        }

    }//GEN-LAST:event_btnEliminar1ActionPerformed

    private void btnSalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalir1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnSalir1ActionPerformed

    private void jtb_DatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtb_DatosMouseClicked
        // TODO add your handling code here:
        int row = jtb_Datos.getSelectedRow();
        txtId.setText(jtb_Datos.getValueAt(row, 0).toString());
        txtNombre1.setText(jtb_Datos.getValueAt(row, 1).toString());
        txtPuesto.setText(jtb_Datos.getValueAt(row, 2).toString());
        txtUsuario.setText(jtb_Datos.getValueAt(row, 3).toString());
        txtPass.setText(jtb_Datos.getValueAt(row, 4).toString());
        num = 1;
    }//GEN-LAST:event_jtb_DatosMouseClicked

    private void btnPdfVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfVentasActionPerformed

        if(txtIdVenta.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Selecciona una fila");
        }else{
            v = nuevaVenta.BuscarVenta(Integer.parseInt(txtIdVenta.getText()));
            JOptionPane.showMessageDialog(null, "id"+v.getId_venta()+"total"+v.getTotal()+"User"+v.getUsuario());//pueden volverlo comentario
            nuevaVenta.pdfV(v.getId_venta(), 1, v.getTotal(), v.getUsuario());
        }
    }//GEN-LAST:event_btnPdfVentasActionPerformed

    private void HIstorialVentasjTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HIstorialVentasjTableMouseClicked
        int fila = HIstorialVentasjTable.rowAtPoint(evt.getPoint());
        txtIdVenta.setText(HIstorialVentasjTable.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_HIstorialVentasjTableMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interfaz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Interfaz().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActualizarBtn;
    private javax.swing.JButton AgregarBtn;
    private javax.swing.JPanel Barra;
    private javax.swing.JButton BuscarButton;
    private javax.swing.JButton BuscarButton1;
    private javax.swing.JButton CancelarVentaBtn;
    private javax.swing.JSpinner CantidadCompraTxt;
    private javax.swing.JSpinner CantidadTxt;
    private javax.swing.JTextField CodigoCompraTxt;
    private javax.swing.JTextField CodigoProveedor;
    private javax.swing.JTextField CodigoTxt;
    private javax.swing.JTable CompraJTable;
    private javax.swing.JPanel ComprasBtn;
    private javax.swing.JLabel ComprasTxt;
    private javax.swing.JPanel ConfiBtn;
    private javax.swing.JLabel Configuracion;
    private javax.swing.JButton ConfirmarVentaBtn;
    private javax.swing.JButton EliminarBtn;
    private javax.swing.JButton EliminarBtn4;
    private javax.swing.JButton EliminarBtn5;
    private javax.swing.JButton EliminarBtn6;
    private javax.swing.JButton EliminarBtn7;
    private javax.swing.JLabel EmpleadoLabel;
    private javax.swing.JTable HIstorialComprasjTable;
    private javax.swing.JTable HIstorialVentasjTable;
    private javax.swing.JPanel InventarioBtn;
    private javax.swing.JLabel InventarioTxt;
    private javax.swing.JTextField NombreCompraTxt;
    private javax.swing.JTextField NombreTxt;
    private javax.swing.JPanel NuevaVentaBtn;
    private javax.swing.JLabel NuevaVentaTxt;
    private javax.swing.JComboBox<String> OpcionComboBox;
    private javax.swing.JTextField PrecioCompraTxt;
    private javax.swing.JTextField PrecioTxt;
    private javax.swing.JPanel ProveedoresBtn;
    private javax.swing.JLabel ProveedoresTxt;
    private javax.swing.JPanel SalirPanel;
    private javax.swing.JLabel SalirTxt;
    private javax.swing.JLabel TotalCompraLabel;
    private javax.swing.JLabel TotalVentaLabel;
    private javax.swing.JPanel UsuariosBtn;
    private javax.swing.JLabel UsuariosTxt;
    private javax.swing.JTable VentaJTable;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnAgregar1;
    private javax.swing.JButton btnAgregarP;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEliminar1;
    private javax.swing.JButton btnEliminarP;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnModificar1;
    private javax.swing.JButton btnModificarP;
    private javax.swing.JButton btnPdfVentas;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnSalir1;
    private javax.swing.JButton btnSalirP;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> jbc_Buscar;
    private javax.swing.JTable jtbDatos;
    private javax.swing.JTable jtb_Datos;
    private javax.swing.JTable jtb_DatosP;
    private javax.swing.JLabel label1;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtDireccionP;
    private javax.swing.JTextField txtExistencia;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtIDP;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtIdVenta;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombre1;
    private javax.swing.JTextField txtNombreP;
    private javax.swing.JPasswordField txtPass;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtPuesto;
    private javax.swing.JTextField txtTelefonoP;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
