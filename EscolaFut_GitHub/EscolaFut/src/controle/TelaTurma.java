package controle;

import conexao.Conexao;
import dao.TurmaDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalTime;
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
import modelo.Turma;

/** Cadastro e manutenção das turmas. */
public class TelaTurma extends JFrame {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new TelaTurma().setVisible(true));
    }

    private final Conexao conexao = new Conexao();
    private TurmaDAO dao;

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtNome = new JTextField();
    private final JTextField txtFaixaEtaria = new JTextField();
    private final JTextField txtDiaSemana = new JTextField();
    private final JTextField txtHorario = new JTextField();
    private final JTextField txtCapacidade = new JTextField();
    private final JTextField txtIdTreinador = new JTextField();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"Código", "Nome", "Faixa etária", "Dia", "Horário", "Capacidade", "ID Treinador"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public TelaTurma() {
        setTitle("Cadastro de Turmas");
        setSize(900, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();
        if (conexao.conecta()) {
            dao = new TurmaDAO(conexao);
            carregarTabela();
        }
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { conexao.desconecta(); }
        });
    }

    private void montarTela() {
        setLayout(new BorderLayout(8, 8));
        JPanel campos = new JPanel(new GridLayout(7, 2, 8, 8));
        campos.setBorder(BorderFactory.createTitledBorder("Dados da turma"));
        adicionarCampo(campos, "Código:", txtCodigo);
        adicionarCampo(campos, "Nome:", txtNome);
        adicionarCampo(campos, "Faixa etária:", txtFaixaEtaria);
        adicionarCampo(campos, "Dia da semana:", txtDiaSemana);
        adicionarCampo(campos, "Horário (HH:MM:SS):", txtHorario);
        adicionarCampo(campos, "Capacidade:", txtCapacidade);
        adicionarCampo(campos, "Código do treinador:", txtIdTreinador);
        txtCodigo.setEditable(false);

        JPanel superior = new JPanel(new BorderLayout(8, 8));
        superior.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        superior.add(campos, BorderLayout.CENTER);
        JPanel botoes = criarPainelBotoes();
        JPanel painelNavegacao = criarPainelNavegacao();
        JPanel painelRodape = new JPanel(new BorderLayout());
        painelRodape.add(botoes, BorderLayout.NORTH);
        painelRodape.add(painelNavegacao, BorderLayout.SOUTH);
        superior.add(painelRodape, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);

        tabela.setRowHeight(22);
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder("Turmas cadastradas"));
        add(scroll, BorderLayout.CENTER);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherCampos();
        });
    }

    private JPanel criarPainelBotoes() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
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
        p.add(novo); p.add(salvar); p.add(editar); p.add(excluir); p.add(fechar);
        return p;
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
            for (Turma t : dao.listar()) {
                modeloTabela.addRow(new Object[]{t.getIdTurma(), t.getNome(), t.getFaixaEtaria(), t.getDiaSemana(), t.getHorario(), t.getCapacidade(), t.getIdTreinador()});
            }
        } catch (Exception ex) { erro(ex); }
    }

    private void preencherCampos() {
        int r = tabela.getSelectedRow();
        if (r < 0) return;
        txtCodigo.setText(String.valueOf(modeloTabela.getValueAt(r, 0)));
        txtNome.setText(String.valueOf(modeloTabela.getValueAt(r, 1)));
        txtFaixaEtaria.setText(String.valueOf(modeloTabela.getValueAt(r, 2)));
        txtDiaSemana.setText(String.valueOf(modeloTabela.getValueAt(r, 3)));
        txtHorario.setText(String.valueOf(modeloTabela.getValueAt(r, 4)));
        txtCapacidade.setText(String.valueOf(modeloTabela.getValueAt(r, 5)));
        txtIdTreinador.setText(String.valueOf(modeloTabela.getValueAt(r, 6)));
    }

    private Turma lerCampos() {
        if (txtNome.getText().isBlank()) throw new IllegalArgumentException("Nome da turma é obrigatório.");
        Turma t = new Turma();
        t.setNome(txtNome.getText().trim());
        t.setFaixaEtaria(txtFaixaEtaria.getText().trim());
        t.setDiaSemana(txtDiaSemana.getText().trim());
        t.setHorario(LocalTime.parse(txtHorario.getText().trim()));
        t.setCapacidade(Integer.parseInt(txtCapacidade.getText().trim()));
        t.setIdTreinador(Integer.parseInt(txtIdTreinador.getText().trim()));
        return t;
    }

    private void salvar() {
        try {
            if (!txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Clique em Novo antes de cadastrar outra turma."); return; }
            dao.inserir(lerCampos());
            JOptionPane.showMessageDialog(this, "Turma cadastrada com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void editar() {
        try {
            if (txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Selecione uma turma na tabela para editar."); return; }
            Turma t = lerCampos();
            t.setIdTurma(Integer.parseInt(txtCodigo.getText()));
            dao.atualizar(t);
            JOptionPane.showMessageDialog(this, "Turma atualizada com sucesso.");
            carregarTabela();
        } catch (Exception ex) { erro(ex); }
    }

    private void excluir() {
        if (txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Selecione uma turma na tabela para excluir."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir esta turma?", "Confirmação", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            JOptionPane.showMessageDialog(this, "Turma excluída com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void limpar() {
        txtCodigo.setText(""); txtNome.setText(""); txtFaixaEtaria.setText(""); txtDiaSemana.setText(""); txtHorario.setText(""); txtCapacidade.setText(""); txtIdTreinador.setText("");
        tabela.clearSelection(); txtNome.requestFocus();
    }

    private void erro(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
}
