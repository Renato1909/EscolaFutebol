package controle;

import conexao.Conexao;
import dao.MatriculaDAO;
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
import javax.swing.table.DefaultTableModel;
import modelo.Matricula;

/** Cadastro e manutenção das matrículas. */
public class TelaMatricula extends JFrame {
    private final Conexao conexao = new Conexao();
    private MatriculaDAO dao;

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtDataMatricula = new JTextField();
    private final JTextField txtSituacao = new JTextField();
    private final JTextField txtIdAluno = new JTextField();
    private final JTextField txtIdTurma = new JTextField();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"Código", "Data", "Situação", "ID Aluno", "ID Turma"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public TelaMatricula() {
        setTitle("Cadastro de Matrículas");
        setSize(760, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();
        if (conexao.conecta()) {
            dao = new MatriculaDAO(conexao);
            carregarTabela();
        }
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { conexao.desconecta(); }
        });
    }

    private void montarTela() {
        setLayout(new BorderLayout(8, 8));
        JPanel campos = new JPanel(new GridLayout(5, 2, 8, 8));
        campos.setBorder(BorderFactory.createTitledBorder("Dados da matrícula"));
        adicionarCampo(campos, "Código:", txtCodigo);
        adicionarCampo(campos, "Data da matrícula (DD-MM-AAAA ou AAAA-MM-DD):", txtDataMatricula);
        adicionarCampo(campos, "Situação:", txtSituacao);
        adicionarCampo(campos, "Código do aluno:", txtIdAluno);
        adicionarCampo(campos, "Código da turma:", txtIdTurma);
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
        scroll.setBorder(BorderFactory.createTitledBorder("Matrículas cadastradas"));
        add(scroll, BorderLayout.CENTER);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCampos();
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
        painel.add(new JLabel(texto)); painel.add(campo);
    }

    private void carregarTabela() {
        if (dao == null) return;
        try {
            modeloTabela.setRowCount(0);
            for (Matricula m : dao.listar()) {
                modeloTabela.addRow(new Object[]{m.getIdMatricula(), m.getDataMatricula(), m.getSituacao(), m.getIdAluno(), m.getIdTurma()});
            }
        } catch (Exception ex) { erro(ex); }
    }

    private void preencherCampos() {
        int r = tabela.getSelectedRow();
        if (r < 0) return;
        txtCodigo.setText(String.valueOf(modeloTabela.getValueAt(r, 0)));
        txtDataMatricula.setText(String.valueOf(modeloTabela.getValueAt(r, 1)));
        txtSituacao.setText(String.valueOf(modeloTabela.getValueAt(r, 2)));
        txtIdAluno.setText(String.valueOf(modeloTabela.getValueAt(r, 3)));
        txtIdTurma.setText(String.valueOf(modeloTabela.getValueAt(r, 4)));
    }

    private Matricula lerCampos() {
        Matricula m = new Matricula();
        m.setDataMatricula(DataInput.parse(txtDataMatricula.getText()));
        m.setSituacao(txtSituacao.getText().trim());
        m.setIdAluno(Integer.parseInt(txtIdAluno.getText().trim()));
        m.setIdTurma(Integer.parseInt(txtIdTurma.getText().trim()));
        return m;
    }

    private void salvar() {
        try {
            if (!txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Clique em Novo antes de cadastrar outra matrícula."); return; }
            dao.inserir(lerCampos());
            JOptionPane.showMessageDialog(this, "Matrícula cadastrada com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void editar() {
        try {
            if (txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Selecione uma matrícula na tabela para editar."); return; }
            Matricula m = lerCampos();
            m.setIdMatricula(Integer.parseInt(txtCodigo.getText()));
            dao.atualizar(m);
            JOptionPane.showMessageDialog(this, "Matrícula atualizada com sucesso.");
            carregarTabela();
        } catch (Exception ex) { erro(ex); }
    }

    private void excluir() {
        if (txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Selecione uma matrícula na tabela para excluir."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir esta matrícula?", "Confirmação", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            JOptionPane.showMessageDialog(this, "Matrícula excluída com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void limpar() {
        txtCodigo.setText(""); txtDataMatricula.setText(""); txtSituacao.setText(""); txtIdAluno.setText(""); txtIdTurma.setText("");
        tabela.clearSelection(); txtDataMatricula.requestFocus();
    }

    private void erro(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
}
