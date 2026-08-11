/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package controle;

import conexao.Conexao;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class Tela extends javax.swing.JFrame {
    private final Conexao conCliente = new Conexao();
    private boolean novoRegistro = false;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Tela.class.getName());

    /**
     * Creates new form Tela
     */
    public Tela() {
        initComponents();
        setTitle("Cadastro de Clientes - Escola de Futebol");
        setLocationRelativeTo(null);
        configurarEventos();
        if (conCliente.conecta()) {
            JOptionPane.showMessageDialog(this, "Conexão estabelecida.", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            carregarTabela("");
            primeiroRegistro();
        }
    }

    private void configurarEventos() {
        btnAdiciona.addActionListener(e -> novo());
        btnSalvar.addActionListener(e -> salvar());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnSair.addActionListener(e -> { conCliente.desconecta(); dispose(); });
        btnVolta.addActionListener(e -> anteriorRegistro());
        btnPassa.addActionListener(e -> proximoRegistro());
        txtPesqCliente.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { pesquisar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { pesquisar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { pesquisar(); }
        });
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() >= 0) preencherDaTabela(jTable1.getSelectedRow());
        });
        txtCod.setEditable(false);
    }

    private void pesquisar() { carregarTabela(txtPesqCliente.getText().trim()); }

    private void carregarTabela(String filtro) {
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        modelo.setRowCount(0);
        String sql = "SELECT cod,nome,dt_nasc,telefone,email FROM tbclientes WHERE nome LIKE ? ORDER BY nome";
        try (PreparedStatement ps = conCliente.preparaSQL(sql)) {
            ps.setString(1, "%" + filtro + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) modelo.addRow(new Object[]{rs.getInt("cod"), rs.getString("nome"), rs.getString("dt_nasc"), rs.getString("telefone"), rs.getString("email")});
            }
        } catch (SQLException ex) { mensagemErro("Erro ao carregar clientes", ex); }
    }

    private void preencherDaTabela(int linha) {
        txtCod.setText(String.valueOf(jTable1.getValueAt(linha, 0)));
        txtNome.setText(String.valueOf(jTable1.getValueAt(linha, 1)));
        txtDataNasc.setText(String.valueOf(jTable1.getValueAt(linha, 2)));
        txtTel.setText(String.valueOf(jTable1.getValueAt(linha, 3)));
        txtEmail.setText(String.valueOf(jTable1.getValueAt(linha, 4)));
        novoRegistro = false;
    }

    private void novo() {
        txtCod.setText(""); txtNome.setText(""); txtDataNasc.setText(""); txtTel.setText(""); txtEmail.setText("");
        novoRegistro = true; txtNome.requestFocus();
    }

    private boolean validar() {
        if (txtNome.getText().isBlank() || txtDataNasc.getText().isBlank() || txtTel.getText().isBlank() || txtEmail.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Preencha nome, data de nascimento, telefone e e-mail."); return false;
        }
        return true;
    }

    private void salvar() {
        if (!validar()) return;
        if (!novoRegistro && !txtCod.getText().isBlank()) { editar(); return; }
        String sql = "INSERT INTO tbclientes (nome,dt_nasc,telefone,email) VALUES (?,?,?,?)";
        try (PreparedStatement ps = conCliente.preparaSQL(sql)) {
            ps.setString(1, txtNome.getText().trim()); ps.setString(2, txtDataNasc.getText().trim()); ps.setString(3, txtTel.getText().trim()); ps.setString(4, txtEmail.getText().trim());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso.");
            novoRegistro = false; carregarTabela(""); ultimoRegistro();
        } catch (SQLException ex) { mensagemErro("Erro ao cadastrar", ex); }
    }

    private void editar() {
        if (txtCod.getText().isBlank() || !validar()) return;
        String sql = "UPDATE tbclientes SET nome=?,dt_nasc=?,telefone=?,email=? WHERE cod=?";
        try (PreparedStatement ps = conCliente.preparaSQL(sql)) {
            ps.setString(1, txtNome.getText().trim()); ps.setString(2, txtDataNasc.getText().trim()); ps.setString(3, txtTel.getText().trim()); ps.setString(4, txtEmail.getText().trim()); ps.setInt(5, Integer.parseInt(txtCod.getText()));
            ps.executeUpdate(); JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso."); carregarTabela(""); selecionarPorCodigo(Integer.parseInt(txtCod.getText()));
        } catch (SQLException ex) { mensagemErro("Erro ao atualizar", ex); }
    }

    private void excluir() {
        if (txtCod.getText().isBlank()) return;
        if (JOptionPane.showConfirmDialog(this, "Excluir este cliente?", "Confirmação", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try (PreparedStatement ps = conCliente.preparaSQL("DELETE FROM tbclientes WHERE cod=?")) {
            ps.setInt(1, Integer.parseInt(txtCod.getText())); ps.executeUpdate(); carregarTabela(""); primeiroRegistro();
        } catch (SQLException ex) { mensagemErro("Erro ao excluir", ex); }
    }

    private void primeiroRegistro() { if (jTable1.getRowCount() > 0) { jTable1.setRowSelectionInterval(0,0); preencherDaTabela(0); } }
    private void ultimoRegistro() { int i=jTable1.getRowCount()-1; if(i>=0){ jTable1.setRowSelectionInterval(i,i); preencherDaTabela(i); } }
    private void anteriorRegistro() { int i=jTable1.getSelectedRow(); if(i>0){ jTable1.setRowSelectionInterval(i-1,i-1); preencherDaTabela(i-1); } }
    private void proximoRegistro() { int i=jTable1.getSelectedRow(); if(i>=0 && i<jTable1.getRowCount()-1){ jTable1.setRowSelectionInterval(i+1,i+1); preencherDaTabela(i+1); } }
    private void selecionarPorCodigo(int cod) { for(int i=0;i<jTable1.getRowCount();i++) if(Integer.parseInt(jTable1.getValueAt(i,0).toString())==cod){ jTable1.setRowSelectionInterval(i,i); preencherDaTabela(i); break; } }
    private void mensagemErro(String titulo, SQLException ex) { JOptionPane.showMessageDialog(this, titulo + ": " + ex.getMessage(), "Mensagem do Programa", JOptionPane.ERROR_MESSAGE); }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCodigo = new javax.swing.JLabel();
        lblNome = new javax.swing.JLabel();
        lblDataNasc = new javax.swing.JLabel();
        lblTel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblPesqCliente = new javax.swing.JLabel();
        txtPesqCliente = new javax.swing.JTextField();
        txtCod = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        txtDataNasc = new javax.swing.JTextField();
        txtTel = new javax.swing.JTextField();
        btnVoltaMais = new javax.swing.JButton();
        btnPassa = new javax.swing.JButton();
        btnVolta = new javax.swing.JButton();
        btnPassaMais = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnAdiciona = new javax.swing.JButton();
        lblEmail = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 0));

        lblCodigo.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        lblCodigo.setText("Código:");

        lblNome.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        lblNome.setText("Nome:");

        lblDataNasc.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        lblDataNasc.setText("Data Nascimento:");

        lblTel.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        lblTel.setText("Telefone:");

        jTable1.setBackground(new java.awt.Color(153, 204, 255));
        jTable1.setForeground(new java.awt.Color(204, 255, 0));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Nome", "Data Nascimento", "Telefone", "Email"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jScrollPane1.setViewportView(jTable1);

        lblPesqCliente.setText("Pesquisa por nome de Cliente:");

        txtPesqCliente.setBackground(new java.awt.Color(204, 255, 255));
        txtPesqCliente.addActionListener(this::txtPesqClienteActionPerformed);

        txtCod.setBackground(new java.awt.Color(204, 255, 255));
        txtCod.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCod.addActionListener(this::txtCodActionPerformed);

        txtNome.setBackground(new java.awt.Color(204, 255, 255));
        txtNome.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtDataNasc.setBackground(new java.awt.Color(204, 255, 255));
        txtDataNasc.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtTel.setBackground(new java.awt.Color(204, 255, 255));
        txtTel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnVoltaMais.setBackground(new java.awt.Color(0, 102, 255));
        btnVoltaMais.setText("|◀ ");
        btnVoltaMais.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnVoltaMais.setBorderPainted(false);
        btnVoltaMais.setPreferredSize(new java.awt.Dimension(15, 18));
        btnVoltaMais.addActionListener(this::btnVoltaMaisActionPerformed);

        btnPassa.setBackground(new java.awt.Color(0, 153, 153));
        btnPassa.setText("▶");
        btnPassa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnPassa.setBorderPainted(false);
        btnPassa.setPreferredSize(new java.awt.Dimension(15, 18));

        btnVolta.setBackground(new java.awt.Color(0, 153, 153));
        btnVolta.setText("◀ ");
        btnVolta.setAlignmentX(0.5F);
        btnVolta.setAlignmentY(0.0F);
        btnVolta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnVolta.setBorderPainted(false);

        btnPassaMais.setBackground(new java.awt.Color(0, 102, 255));
        btnPassaMais.setText("▶|");
        btnPassaMais.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnPassaMais.setBorderPainted(false);
        btnPassaMais.addActionListener(this::btnPassaMaisActionPerformed);

        btnExcluir.setBackground(new java.awt.Color(0, 102, 255));
        btnExcluir.setText("🗙");
        btnExcluir.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnExcluir.setBorderPainted(false);

        btnSair.setBackground(new java.awt.Color(0, 204, 204));
        btnSair.setText("🚪🡺");
        btnSair.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnSair.setBorderPainted(false);

        btnEditar.setBackground(new java.awt.Color(0, 153, 153));
        btnEditar.setText("✎");
        btnEditar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnEditar.setBorderPainted(false);

        btnSalvar.setBackground(new java.awt.Color(0, 153, 153));
        btnSalvar.setText("📄");
        btnSalvar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnSalvar.setBorderPainted(false);

        btnAdiciona.setBackground(new java.awt.Color(0, 102, 255));
        btnAdiciona.setText("➕");
        btnAdiciona.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnAdiciona.setBorderPainted(false);

        lblEmail.setFont(new java.awt.Font("Calibri Light", 1, 12)); // NOI18N
        lblEmail.setText("Email:");

        txtEmail.setBackground(new java.awt.Color(204, 255, 255));
        txtEmail.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtEmail.addActionListener(this::txtEmailActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnVoltaMais, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnVolta, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(btnPassa, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPassaMais, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(120, 120, 120)
                                .addComponent(btnAdiciona, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 822, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTel)
                            .addComponent(lblEmail)
                            .addComponent(lblCodigo)
                            .addComponent(lblDataNasc)
                            .addComponent(lblNome))
                        .addGap(61, 61, 61)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmail)
                            .addComponent(txtDataNasc)
                            .addComponent(txtTel)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 634, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(245, 245, 245)
                .addComponent(lblPesqCliente)
                .addGap(26, 26, 26)
                .addComponent(txtPesqCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo)
                    .addComponent(txtCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNome))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDataNasc))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTel)
                    .addComponent(txtDataNasc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmail)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVoltaMais, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPassa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPassaMais, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdiciona, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVolta, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPesqCliente)
                    .addComponent(txtPesqCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodActionPerformed

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void btnVoltaMaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVoltaMaisActionPerformed
        primeiroRegistro();
    }//GEN-LAST:event_btnVoltaMaisActionPerformed

    private void btnPassaMaisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPassaMaisActionPerformed
        ultimoRegistro();
    }//GEN-LAST:event_btnPassaMaisActionPerformed

    private void txtPesqClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesqClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesqClienteActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Tela().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdiciona;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnPassa;
    private javax.swing.JButton btnPassaMais;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnVolta;
    private javax.swing.JButton btnVoltaMais;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDataNasc;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPesqCliente;
    private javax.swing.JLabel lblTel;
    private javax.swing.JTextField txtCod;
    private javax.swing.JTextField txtDataNasc;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtPesqCliente;
    private javax.swing.JTextField txtTel;
    // End of variables declaration//GEN-END:variables
}
