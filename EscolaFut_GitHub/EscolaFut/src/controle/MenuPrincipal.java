package controle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Tela inicial do sistema.
 * Utiliza apenas componentes Swing comuns: JFrame, JPanel, JLabel e JButton.
 */
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        setTitle("Escola de Futebol Craques do Futuro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 420);
        setLocationRelativeTo(null);
        montarTela();
    }

    private void montarTela() {
        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel("Sistema de Gerenciamento - Escola de Futebol", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 12, 12));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(20, 60, 35, 60));

        JButton btnAlunos = criarBotaoMenu("Alunos", "aluno");
        JButton btnTreinadores = criarBotaoMenu("Treinadores", "treinador");
        JButton btnTurmas = criarBotaoMenu("Turmas", "turma");
        JButton btnMatriculas = criarBotaoMenu("Matrículas", "matricula");
        JButton btnPagamentos = criarBotaoMenu("Pagamentos", "pagamento");
        JButton btnSair = criarBotaoMenu("Sair", "sair");

        btnAlunos.addActionListener(e -> new TelaAluno().setVisible(true));
        btnTreinadores.addActionListener(e -> new TelaTreinador().setVisible(true));
        btnTurmas.addActionListener(e -> new TelaTurma().setVisible(true));
        btnMatriculas.addActionListener(e -> new TelaMatricula().setVisible(true));
        btnPagamentos.addActionListener(e -> new TelaPagamento().setVisible(true));
        btnSair.addActionListener(e -> dispose());

        painelBotoes.add(btnAlunos);
        painelBotoes.add(btnTreinadores);
        painelBotoes.add(btnTurmas);
        painelBotoes.add(btnMatriculas);
        painelBotoes.add(btnPagamentos);
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.CENTER);
    }

    private JButton criarBotaoMenu(String texto, String icone) {
        JButton botao = Icones.botao(texto, icone);
        botao.setFont(new Font("Arial", Font.PLAIN, 15));
        botao.setPreferredSize(new Dimension(180, 65));
        botao.setFocusPainted(false);
        return botao;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
