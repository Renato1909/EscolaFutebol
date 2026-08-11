package controle;

import conexao.Conexao;
import dao.AlunoDAO;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelo.Aluno;

public class TelaAluno extends JFrame {
    private final Conexao con=new Conexao(); private AlunoDAO dao;
    private final JTextField id=new JTextField(),nome=new JTextField(),data=new JTextField(),telefone=new JTextField(),email=new JTextField(),responsavel=new JTextField(),pesquisa=new JTextField();
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"Código","Nome","Nascimento","Telefone","E-mail","Responsável"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable tabela=new JTable(model);
    public TelaAluno(){setTitle("Cadastro de Aluno");setSize(850,520);setLocationRelativeTo(null);setDefaultCloseOperation(DISPOSE_ON_CLOSE);montar();if(con.conecta()){dao=new AlunoDAO(con);carregar();}addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosed(java.awt.event.WindowEvent e){con.desconecta();}});}
    private void montar(){JPanel campos=new JPanel(new GridLayout(7,2,6,6));id.setEditable(false);campos.add(new JLabel("Código:"));campos.add(id);campos.add(new JLabel("Nome:"));campos.add(nome);campos.add(new JLabel("Data nascimento (AAAA-MM-DD):"));campos.add(data);campos.add(new JLabel("Telefone:"));campos.add(telefone);campos.add(new JLabel("E-mail:"));campos.add(email);campos.add(new JLabel("Responsável:"));campos.add(responsavel);campos.add(new JLabel("Pesquisar por nome:"));campos.add(pesquisa);JPanel topo=new JPanel(new BorderLayout());topo.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));topo.add(campos,BorderLayout.CENTER);JPanel botoes=new JPanel();for(String s:new String[]{"Novo","Salvar","Excluir","Fechar"}){JButton b=new JButton(s);botoes.add(b);if(s.equals("Novo"))b.addActionListener(e->limpar());if(s.equals("Salvar"))b.addActionListener(e->salvar());if(s.equals("Excluir"))b.addActionListener(e->excluir());if(s.equals("Fechar"))b.addActionListener(e->dispose());}topo.add(botoes,BorderLayout.SOUTH);add(topo,BorderLayout.NORTH);add(new JScrollPane(tabela),BorderLayout.CENTER);tabela.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())selecionar();});pesquisa.getDocument().addDocumentListener(new DocumentListener(){public void insertUpdate(DocumentEvent e){carregar();}public void removeUpdate(DocumentEvent e){carregar();}public void changedUpdate(DocumentEvent e){carregar();}});}
    private void carregar(){if(dao==null)return;try{model.setRowCount(0);for(Aluno a:dao.listar(pesquisa.getText()))model.addRow(new Object[]{a.getId(),a.getNome(),a.getDataNascimento(),a.getTelefone(),a.getEmail(),a.getResponsavel()});}catch(Exception e){erro(e);}}
    private void selecionar(){int r=tabela.getSelectedRow();if(r<0)return;id.setText(model.getValueAt(r,0).toString());nome.setText(model.getValueAt(r,1).toString());data.setText(model.getValueAt(r,2).toString());telefone.setText(model.getValueAt(r,3).toString());email.setText(model.getValueAt(r,4).toString());responsavel.setText(String.valueOf(model.getValueAt(r,5)));}
    private void limpar(){id.setText("");nome.setText("");data.setText("");telefone.setText("");email.setText("");responsavel.setText("");nome.requestFocus();}
    private void salvar(){try{if(nome.getText().isBlank()||data.getText().isBlank())throw new IllegalArgumentException("Nome e data de nascimento são obrigatórios.");Aluno a=new Aluno();a.setNome(nome.getText().trim());a.setDataNascimento(LocalDate.parse(data.getText().trim()));a.setTelefone(telefone.getText().trim());a.setEmail(email.getText().trim());a.setResponsavel(responsavel.getText().trim());if(id.getText().isBlank())dao.inserir(a);else{a.setId(Integer.parseInt(id.getText()));dao.atualizar(a);}carregar();limpar();}catch(Exception e){erro(e);}}
    private void excluir(){if(id.getText().isBlank())return;if(JOptionPane.showConfirmDialog(this,"Excluir este aluno?","Confirmação",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;try{dao.excluir(Integer.parseInt(id.getText()));carregar();limpar();}catch(Exception e){erro(e);}}
    private void erro(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Erro",JOptionPane.ERROR_MESSAGE);}
}
