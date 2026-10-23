package controle;

import conexao.Conexao;
import dao.AlunoDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
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
import modelo.Aluno;

/** Cadastro e manutenção dos alunos. */
public class TelaAluno extends JFrame {
    private final Conexao conexao = new Conexao();
    private AlunoDAO dao;

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNome = new JTextField();
    private final JTextField txtDataNascimento = new JTextField();
    private final JTextField txtTelefone = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtResponsavel = new JTextField();
    private final JTextField txtPesquisa = new JTextField();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"Código", "Nome", "Nascimento", "Telefone", "E-mail", "Responsável"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public TelaAluno() {
        setTitle("Cadastro de Alunos");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();

        if (conexao.conecta()) {
            dao = new AlunoDAO(conexao);
            carregarTabela();
        }

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                conexao.desconecta();
            }
        });
    }

    private void montarTela() {
        setLayout(new BorderLayout(8, 8));

        JPanel painelCampos = new JPanel(new GridLayout(7, 2, 8, 8));
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do aluno"));
        adicionarCampo(painelCampos, "Código:", txtCodigo);
        adicionarCampo(painelCampos, "Nome:", txtNome);
        adicionarCampo(painelCampos, "Data nascimento (DD-MM-AAAA ou AAAA-MM-DD):", txtDataNascimento);
        adicionarCampo(painelCampos, "Telefone:", txtTelefone);
        adicionarCampo(painelCampos, "E-mail:", txtEmail);
        adicionarCampo(painelCampos, "Responsável:", txtResponsavel);
        adicionarCampo(painelCampos, "Pesquisar por nome:", txtPesquisa);
        txtCodigo.setEditable(false);

        JPanel painelSuperior = new JPanel(new BorderLayout(8, 8));
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        painelSuperior.add(painelCampos, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        JButton btnNovo = Icones.botao("Novo", "novo");
        JButton btnSalvar = Icones.botao("Salvar", "salvar");
        JButton btnEditar = Icones.botao("Editar", "editar");
        JButton btnExcluir = Icones.botao("Excluir", "excluir");
        JButton btnFechar = Icones.botao("Fechar", "fechar");

        btnNovo.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> salvar());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnFechar.addActionListener(e -> dispose());

        painelBotoes.add(btnNovo);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnFechar);
        JPanel painelNavegacao = criarPainelNavegacao();
        painelSuperior.add(painelBotoes, BorderLayout.NORTH);
        painelSuperior.add(painelNavegacao, BorderLayout.SOUTH);
        add(painelSuperior, BorderLayout.NORTH);

        tabela.setRowHeight(22);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder("Alunos cadastrados"));
        add(scroll, BorderLayout.CENTER);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                preencherCamposDaTabela();
            }
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
            preencherCamposDaTabela();
        }
    }

    private void ultimoRegistro() {
        int linha = tabela.getRowCount() - 1;
        if (linha >= 0) {
            tabela.setRowSelectionInterval(linha, linha);
            preencherCamposDaTabela();
        }
    }

    private void anteriorRegistro() {
        int linha = tabela.getSelectedRow();
        if (linha > 0) {
            tabela.setRowSelectionInterval(linha - 1, linha - 1);
            preencherCamposDaTabela();
        }
    }

    private void proximoRegistro() {
        int linha = tabela.getSelectedRow();
        if (linha >= 0 && linha < tabela.getRowCount() - 1) {
            tabela.setRowSelectionInterval(linha + 1, linha + 1);
            preencherCamposDaTabela();
        }
    }

    private void adicionarCampo(JPanel painel, String rotulo, JTextField campo) {
        painel.add(new JLabel(rotulo));
        painel.add(campo);
    }

    private void carregarTabela() {
        if (dao == null) return;
        try {
            modeloTabela.setRowCount(0);
            for (Aluno aluno : dao.listar(txtPesquisa.getText().trim())) {
                modeloTabela.addRow(new Object[]{
                    aluno.getId(), aluno.getNome(), aluno.getDataNascimento(),
                    aluno.getTelefone(), aluno.getEmail(), aluno.getResponsavel()
                });
            }
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void preencherCamposDaTabela() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) return;
        txtCodigo.setText(String.valueOf(modeloTabela.getValueAt(linha, 0)));
        txtNome.setText(String.valueOf(modeloTabela.getValueAt(linha, 1)));
        txtDataNascimento.setText(String.valueOf(modeloTabela.getValueAt(linha, 2)));
        txtTelefone.setText(String.valueOf(modeloTabela.getValueAt(linha, 3)));
        txtEmail.setText(String.valueOf(modeloTabela.getValueAt(linha, 4)));
        txtResponsavel.setText(String.valueOf(modeloTabela.getValueAt(linha, 5)));
    }

    private Aluno lerAlunoDosCampos() {
        if (txtNome.getText().isBlank() || txtDataNascimento.getText().isBlank()) {
            throw new IllegalArgumentException("Nome e data de nascimento são obrigatórios.");
        }
        Aluno aluno = new Aluno();
        aluno.setNome(txtNome.getText().trim());
        aluno.setDataNascimento(DataInput.parse(txtDataNascimento.getText()));
        aluno.setTelefone(txtTelefone.getText().trim());
        aluno.setEmail(txtEmail.getText().trim());
        aluno.setResponsavel(txtResponsavel.getText().trim());
        return aluno;
    }

    private void salvar() {
        try {
            if (!txtCodigo.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Clique em Novo antes de cadastrar outro aluno.");
                return;
            }
            dao.inserir(lerAlunoDosCampos());
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso.");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void editar() {
        try {
            if (txtCodigo.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela para editar.");
                return;
            }
            Aluno aluno = lerAlunoDosCampos();
            aluno.setId(Integer.parseInt(txtCodigo.getText()));
            dao.atualizar(aluno);
            JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso.");
            carregarTabela();
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void excluir() {
        if (txtCodigo.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela para excluir.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Excluir este aluno?", "Confirmação",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso.");
            carregarTabela();
            limparCampos();
        } catch (Exception ex) {
            mostrarErro(ex);
        }
    }

    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtDataNascimento.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtResponsavel.setText("");
        tabela.clearSelection();
        txtNome.requestFocus();
    }

    private void mostrarErro(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
