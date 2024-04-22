
package Forms;

import Logica.login;
import Logica.LoginDao;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;


public class Login extends javax.swing.JFrame {
    //Variables para guardar posicion mouse
    int xMouse,yMouse;
    boolean primera=false;
    //crea objeto tipo login 
    public login lg = new login();
    
    //crea objeto tipo LoginDao
    LoginDao loginEmpleado = new LoginDao();
    
    
    public Login() {
        initComponents();
        //Se utiliza para centrar pantalla
        this.setLocationRelativeTo(null);
    }
    
    //Valida que el correo y la contraseña existan
    public void validar(){
        String correo = Usuariotxt.getText();
        String pass = String.valueOf(PassTxt.getPassword());
        if (!"".equals(correo) || !"".equals(pass)) {
            
            //ejecuta el metodo log en donde se busca el usuario y contraseña de clase LoginDao
            lg = loginEmpleado.log(correo, pass);
            
            if (lg.getUsuario()!= null && lg.getPass() != null) {
                Interfaz sis = new Interfaz();
                sis.EmpleadoIngreso(lg.getId(),lg.getNombre());
                sis.setVisible(true);
                dispose();
            }else{
                JOptionPane.showMessageDialog(null, "Correo o la Contraseña incorrecta");
            }
        }
    }
    
    public void PresionarUsuario(){
        if (primera == false){
            primera = true;
        }else{
            if(Usuariotxt.getText().equals("INGRESE SU NOMBRE DE USUARIO")){
                Usuariotxt.setText("");
                Usuariotxt.setForeground(Color.black);
            }
            if(String.valueOf(PassTxt.getPassword()).isEmpty()){
                PassTxt.setText("**********");
                PassTxt.setForeground(Color.gray);
            }
        }
    }
    
    public void PresionarPass(){
        if(String.valueOf(PassTxt.getPassword()).equals("**********")){
            PassTxt.setText("");
            PassTxt.setForeground(Color.black);
        }
        if(Usuariotxt.getText().isEmpty()){
            Usuariotxt.setText("INGRESE SU NOMBRE DE USUARIO");
            Usuariotxt.setForeground(Color.gray);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        SalirPanel = new javax.swing.JPanel();
        SalirTxt = new javax.swing.JLabel();
        Barra = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        NombreEmpresa = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        TituloTxt = new javax.swing.JLabel();
        Usuariotxt = new javax.swing.JTextField();
        PassTxt = new javax.swing.JPasswordField();
        PassLabel = new javax.swing.JLabel();
        UserLabel = new javax.swing.JLabel();
        IngresarPanel = new javax.swing.JPanel();
        BIngresar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        SalirPanel.setBackground(new java.awt.Color(255, 255, 255));

        SalirTxt.setFont(new java.awt.Font("Roboto Light", 0, 24)); // NOI18N
        SalirTxt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        SalirTxt.setText("X");
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
            .addGap(0, 880, Short.MAX_VALUE)
        );
        BarraLayout.setVerticalGroup(
            BarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel1.add(Barra, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 430, 10));
        jPanel1.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 290, 430, 10));

        jPanel2.setBackground(new java.awt.Color(51, 51, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NombreEmpresa.setFont(new java.awt.Font("Roboto Medium", 0, 24)); // NOI18N
        NombreEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NombreEmpresa.setText("Sistema Licoreria");
        jPanel2.add(NombreEmpresa, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 260, 20));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/botellalogin.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 260, 430));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 0, 260, 460));

        TituloTxt.setFont(new java.awt.Font("Roboto Black", 0, 24)); // NOI18N
        TituloTxt.setText("INICIAR SESION");
        jPanel1.add(TituloTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        Usuariotxt.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        Usuariotxt.setForeground(new java.awt.Color(204, 204, 204));
        Usuariotxt.setText("INGRESE SU NOMBRE DE USUARIO");
        Usuariotxt.setBorder(null);
        Usuariotxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UsuariotxtFocusGained(evt);
            }
        });
        Usuariotxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                UsuariotxtMousePressed(evt);
            }
        });
        Usuariotxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UsuariotxtActionPerformed(evt);
            }
        });
        Usuariotxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                UsuariotxtKeyPressed(evt);
            }
        });
        jPanel1.add(Usuariotxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 160, 430, 40));

        PassTxt.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        PassTxt.setForeground(new java.awt.Color(204, 204, 204));
        PassTxt.setText("**********");
        PassTxt.setBorder(null);
        PassTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PassTxtFocusGained(evt);
            }
        });
        PassTxt.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PassTxtMousePressed(evt);
            }
        });
        PassTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PassTxtKeyPressed(evt);
            }
        });
        jPanel1.add(PassTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 430, 40));

        PassLabel.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        PassLabel.setText("CONTRASEÑA");
        jPanel1.add(PassLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 230, 100, -1));

        UserLabel.setFont(new java.awt.Font("Roboto Light", 1, 14)); // NOI18N
        UserLabel.setText("USUARIO");
        jPanel1.add(UserLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 140, 70, -1));

        IngresarPanel.setBackground(new java.awt.Color(51, 51, 255));

        BIngresar.setFont(new java.awt.Font("Roboto Light", 1, 12)); // NOI18N
        BIngresar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        BIngresar.setText("INGRESAR");
        BIngresar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        BIngresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BIngresarMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BIngresarMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BIngresarMouseExited(evt);
            }
        });

        javax.swing.GroupLayout IngresarPanelLayout = new javax.swing.GroupLayout(IngresarPanel);
        IngresarPanel.setLayout(IngresarPanelLayout);
        IngresarPanelLayout.setHorizontalGroup(
            IngresarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IngresarPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        IngresarPanelLayout.setVerticalGroup(
            IngresarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, IngresarPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(BIngresar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(IngresarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, 160, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 881, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void UsuariotxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UsuariotxtActionPerformed
        
    }//GEN-LAST:event_UsuariotxtActionPerformed

    private void BarraMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BarraMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_BarraMousePressed

    private void BarraMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BarraMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        this.setLocation(x-xMouse,y-yMouse);
        
    }//GEN-LAST:event_BarraMouseDragged

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

    private void BIngresarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BIngresarMouseEntered
        IngresarPanel.setBackground(new Color(0,153,204));
        
    }//GEN-LAST:event_BIngresarMouseEntered

    private void BIngresarMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BIngresarMouseExited
        IngresarPanel.setBackground(new Color(51,51,255));
        
    }//GEN-LAST:event_BIngresarMouseExited

    private void UsuariotxtMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsuariotxtMousePressed
        PresionarUsuario();
    }//GEN-LAST:event_UsuariotxtMousePressed

    private void PassTxtMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PassTxtMousePressed
       PresionarPass();
    }//GEN-LAST:event_PassTxtMousePressed

    private void BIngresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BIngresarMouseClicked
        validar();
    }//GEN-LAST:event_BIngresarMouseClicked

    private void PassTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PassTxtFocusGained
       PresionarPass();
    }//GEN-LAST:event_PassTxtFocusGained

    private void UsuariotxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UsuariotxtFocusGained
        PresionarUsuario();
    }//GEN-LAST:event_UsuariotxtFocusGained

    private void PassTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PassTxtKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            validar();
        }
    }//GEN-LAST:event_PassTxtKeyPressed

    private void UsuariotxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_UsuariotxtKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            PassTxt.requestFocus();
        }
        PresionarUsuario();
        
        if(evt.getKeyCode() == KeyEvent.VK_DELETE){
            PresionarUsuario();
        }
    }//GEN-LAST:event_UsuariotxtKeyPressed

  
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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BIngresar;
    private javax.swing.JPanel Barra;
    private javax.swing.JPanel IngresarPanel;
    private javax.swing.JLabel NombreEmpresa;
    private javax.swing.JLabel PassLabel;
    private javax.swing.JPasswordField PassTxt;
    private javax.swing.JPanel SalirPanel;
    private javax.swing.JLabel SalirTxt;
    private javax.swing.JLabel TituloTxt;
    private javax.swing.JLabel UserLabel;
    private javax.swing.JTextField Usuariotxt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables

    private void If(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
