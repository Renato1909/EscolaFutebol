package controle;

import conexao.Conexao;
import dao.TreinadorDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import modelo.Treinador;

/** Cadastro e manutenção dos treinadores. */
public class TelaTreinador extends JFrame {
    private final Conexao conexao = new Conexao();
    private TreinadorDAO dao;

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNome = new JTextField();
    private final JTextField txtTelefone = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtEspecialidade = new JTextField();
    private final JTextField txtPesquisa = new JTextField();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"Código", "Nome", "Telefone", "E-mail", "Especialidade"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public TelaTreinador() {
        setTitle("Cadastro de Treinadores");
        setSize(850, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();
        if (conexao.conecta()) {
            dao = new TreinadorDAO(conexao);
            carregarTabela();
        }
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { conexao.desconecta(); }
        });
    }

    private void montarTela() {
        setLayout(new BorderLayout(8, 8));
        JPanel campos = new JPanel(new GridLayout(6, 2, 8, 8));
        campos.setBorder(BorderFactory.createTitledBorder("Dados do treinador"));
        adicionarCampo(campos, "Código:", txtCodigo);
        adicionarCampo(campos, "Nome:", txtNome);
        adicionarCampo(campos, "Telefone:", txtTelefone);
        adicionarCampo(campos, "E-mail:", txtEmail);
        adicionarCampo(campos, "Especialidade:", txtEspecialidade);
        adicionarCampo(campos, "Pesquisar por nome:", txtPesquisa);
        txtCodigo.setEditable(false);

        JPanel superior = new JPanel(new BorderLayout(8, 8));
        superior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        superior.add(campos, BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton novo = Icones.botao("Novo", "novo");
        JButton salvar = Icones.botao("Salvar", "salvar");
        JButton editar = Icones.botao("Editar", "editar");
        JButton excluir = Icones.botao("Excluir", "excluir");
        JButton fechar = Icones.botao("Fechar", "fechar");
        novo.addActionListener(e -> limpar());
        salvar.addActionListener(e -> salvar());
        editar.addActionListener(e -> editar());
        excluir.addActionListener(e -> excluir());
        fechar.addActionListener(e -> dispose());
        botoes.add(novo); botoes.add(salvar); botoes.add(editar); botoes.add(excluir); botoes.add(fechar);
        JPanel painelNavegacao = criarPainelNavegacao();
        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.add(botoes, BorderLayout.NORTH);
        painelRodape.add(painelNavegacao, BorderLayout.SOUTH);
        superior.add(painelRodape, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);

        tabela.setRowHeight(22);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder("Treinadores cadastrados"));
        add(scroll, BorderLayout.CENTER);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCampos();
        });
        txtPesquisa.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { carregarTabela(); }
            @Override public void removeUpdate(DocumentEvent e) { carregarTabela(); }
            @Override public void changedUpdate(DocumentEvent e) { carregarTabela(); }
        });
    }

    private JPanel criarPainelNavegacao() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));

        JButton primeiro = Icones.navegacao("|◀", true);
        JButton anterior = Icones.navegacao("◀", false);
        JButton proximo = Icones.navegacao("▶", false);
        JButton ultimo = Icones.navegacao("▶|", true);

        primeiro.addActionListener(e -> primeiroRegistro());
        anterior.addActionListener(e -> anteriorRegistro());
        proximo.addActionListener(e -> proximoRegistro());
        ultimo.addActionListener(e -> ultimoRegistro());

        painel.add(primeiro);
        painel.add(anterior);
        painel.add(proximo);
        painel.add(ultimo);
        return painel;
    }

    private void primeiroRegistro() {
        if (tabela.getRowCount() > 0) {
            tabela.setRowSelectionInterval(0, 0);
            preencherCampos();
        }
    }

    private void ultimoRegistro() {
        int linha = tabela.getRowCount() - 1;
        if (linha >= 0) {
            tabela.setRowSelectionInterval(linha, linha);
            preencherCampos();
        }
    }

    private void anteriorRegistro() {
        int linha = tabela.getSelectedRow();
        if (linha > 0) {
            tabela.setRowSelectionInterval(linha - 1, linha - 1);
            preencherCampos();
        }
    }

    private void proximoRegistro() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0 && linha < tabela.getRowCount() - 1) {
            tabela.setRowSelectionInterval(linha + 1, linha + 1);
            preencherCampos();
        }
    }

    private void adicionarCampo(JPanel painel, String texto, JTextField campo) {
        painel.add(new JLabel(texto));
        painel.add(campo);
    }

    private void carregarTabela() {
        if (dao == null) return;
        try {
            modeloTabela.setRowCount(0);
            for (Treinador t : dao.listar(txtPesquisa.getText().trim())) {
                modeloTabela.addRow(new Object[]{t.getId(), t.getNome(), t.getTelefone(), t.getEmail(), t.getEspecialidade()});
            }
        } catch (Exception ex) { erro(ex); }
    }

    private void preencherCampos() {
        int r = tabela.getSelectedRow();
        if (r < 0) return;
        txtCodigo.setText(String.valueOf(modeloTabela.getValueAt(r, 0)));
        txtNome.setText(String.valueOf(modeloTabela.getValueAt(r, 1)));
        txtTelefone.setText(String.valueOf(modeloTabela.getValueAt(r, 2)));
        txtEmail.setText(String.valueOf(modeloTabela.getValueAt(r, 3)));
        txtEspecialidade.setText(String.valueOf(modeloTabela.getValueAt(r, 4)));
    }

    private Treinador lerCampos() {
        if (txtNome.getText().isBlank()) throw new IllegalArgumentException("Nome é obrigatório.");
        Treinador t = new Treinador();
        t.setNome(txtNome.getText().trim());
        t.setTelefone(txtTelefone.getText().trim());
        t.setEmail(txtEmail.getText().trim());
        t.setEspecialidade(txtEspecialidade.getText().trim());
        return t;
    }

    private void salvar() {
        try {
            if (!txtCodigo.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Clique em Novo antes de cadastrar outro treinador.");
                return;
            }
            dao.inserir(lerCampos());
            JOptionPane.showMessageDialog(this, "Treinador cadastrado com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void editar() {
        try {
            if (txtCodigo.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Selecione um treinador na tabela para editar.");
                return;
            }
            Treinador t = lerCampos();
            t.setId(Integer.parseInt(txtCodigo.getText()));
            dao.atualizar(t);
            JOptionPane.showMessageDialog(this, "Treinador atualizado com sucesso.");
            carregarTabela();
        } catch (Exception ex) { erro(ex); }
    }

    private void excluir() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um treinador na tabela para excluir.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Excluir este treinador?", "Confirmação", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            JOptionPane.showMessageDialog(this, "Treinador excluído com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void limpar() {
        txtCodigo.setText(""); txtNome.setText(""); txtTelefone.setText(""); txtEmail.setText(""); txtEspecialidade.setText("");
        tabela.clearSelection(); txtNome.requestFocus();
    }

    private void erro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
