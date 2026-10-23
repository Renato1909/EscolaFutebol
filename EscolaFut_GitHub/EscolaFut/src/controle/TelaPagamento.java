package controle;

import conexao.Conexao;
import dao.PagamentoDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
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
import modelo.Pagamento;

/** Cadastro e manutenção dos pagamentos. */
public class TelaPagamento extends JFrame {
    private final Conexao conexao = new Conexao();
    private PagamentoDAO dao;

    private final JTextField txtCodigo = new JTextField();
    private final JTextField txtVencimento = new JTextField();
    private final JTextField txtDataPagamento = new JTextField();
    private final JTextField txtValor = new JTextField();
    private final JTextField txtSituacao = new JTextField();
    private final JTextField txtIdMatricula = new JTextField();

    private final DefaultTableModel modeloTabela = new DefaultTableModel(
            new String[]{"Código", "Vencimento", "Data pagamento", "Valor", "Situação", "ID Matrícula"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable tabela = new JTable(modeloTabela);

    public TelaPagamento() {
        setTitle("Cadastro de Pagamentos");
        setSize(850, 510);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        montarTela();
        if (conexao.conecta()) {
            dao = new PagamentoDAO(conexao);
            carregarTabela();
        }
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosed(java.awt.event.WindowEvent e) { conexao.desconecta(); }
        });
    }

    private void montarTela() {
        setLayout(new BorderLayout(8, 8));
        JPanel campos = new JPanel(new GridLayout(6, 2, 8, 8));
        campos.setBorder(BorderFactory.createTitledBorder("Dados do pagamento"));
        adicionarCampo(campos, "Código:", txtCodigo);
        adicionarCampo(campos, "Vencimento (DD-MM-AAAA ou AAAA-MM-DD):", txtVencimento);
        adicionarCampo(campos, "Data pagamento (opcional, DD-MM-AAAA ou AAAA-MM-DD):", txtDataPagamento);
        adicionarCampo(campos, "Valor:", txtValor);
        adicionarCampo(campos, "Situação:", txtSituacao);
        adicionarCampo(campos, "Código da matrícula:", txtIdMatricula);
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
        scroll.setBorder(BorderFactory.createTitledBorder("Pagamentos cadastrados"));
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
            for (Pagamento p : dao.listar()) {
                modeloTabela.addRow(new Object[]{p.getIdPagamento(), p.getVencimento(), p.getDataPagamento(), p.getValor(), p.getSituacao(), p.getIdMatricula()});
            }
        } catch (Exception ex) { erro(ex); }
    }

    private void preencherCampos() {
        int r = tabela.getSelectedRow();
        if (r < 0) return;
        txtCodigo.setText(String.valueOf(modeloTabela.getValueAt(r, 0)));
        txtVencimento.setText(String.valueOf(modeloTabela.getValueAt(r, 1)));
        Object dataPagamento = modeloTabela.getValueAt(r, 2);
        txtDataPagamento.setText(dataPagamento == null ? "" : String.valueOf(dataPagamento));
        txtValor.setText(String.valueOf(modeloTabela.getValueAt(r, 3)));
        txtSituacao.setText(String.valueOf(modeloTabela.getValueAt(r, 4)));
        txtIdMatricula.setText(String.valueOf(modeloTabela.getValueAt(r, 5)));
    }

    private Pagamento lerCampos() {
        Pagamento p = new Pagamento();
        p.setVencimento(DataInput.parse(txtVencimento.getText()));
        p.setDataPagamento(txtDataPagamento.getText().isBlank() ? null : DataInput.parse(txtDataPagamento.getText()));
        p.setValor(new BigDecimal(txtValor.getText().trim().replace(',', '.')));
        p.setSituacao(txtSituacao.getText().trim());
        p.setIdMatricula(Integer.parseInt(txtIdMatricula.getText().trim()));
        return p;
    }

    private void salvar() {
        try {
            if (!txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Clique em Novo antes de cadastrar outro pagamento."); return; }
            dao.inserir(lerCampos());
            JOptionPane.showMessageDialog(this, "Pagamento cadastrado com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void editar() {
        try {
            if (txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Selecione um pagamento na tabela para editar."); return; }
            Pagamento p = lerCampos();
            p.setIdPagamento(Integer.parseInt(txtCodigo.getText()));
            dao.atualizar(p);
            JOptionPane.showMessageDialog(this, "Pagamento atualizado com sucesso.");
            carregarTabela();
        } catch (Exception ex) { erro(ex); }
    }

    private void excluir() {
        if (txtCodigo.getText().isBlank()) { JOptionPane.showMessageDialog(this, "Selecione um pagamento na tabela para excluir."); return; }
        if (JOptionPane.showConfirmDialog(this, "Excluir este pagamento?", "Confirmação", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            dao.excluir(Integer.parseInt(txtCodigo.getText()));
            JOptionPane.showMessageDialog(this, "Pagamento excluído com sucesso.");
            carregarTabela(); limpar();
        } catch (Exception ex) { erro(ex); }
    }

    private void limpar() {
        txtCodigo.setText(""); txtVencimento.setText(""); txtDataPagamento.setText(""); txtValor.setText(""); txtSituacao.setText(""); txtIdMatricula.setText("");
        tabela.clearSelection(); txtVencimento.requestFocus();
    }

    private void erro(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
}
