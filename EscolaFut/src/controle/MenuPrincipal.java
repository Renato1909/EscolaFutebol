package controle;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/** Menu inicial da Parte 2. */
public class MenuPrincipal extends JFrame {
    public MenuPrincipal() {
        setTitle("Craques do Futuro - Sistema de Gerenciamento");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 390);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12,12));

        JLabel titulo = new JLabel("Escola de Futebol Craques do Futuro", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(22f));
        titulo.setBorder(BorderFactory.createEmptyBorder(24,10,10,10));
        add(titulo, BorderLayout.NORTH);

        JPanel painel = new JPanel(new GridLayout(3,2,12,12));
        painel.setBorder(BorderFactory.createEmptyBorder(10,45,35,45));
        JButton aluno = new JButton("Alunos");
        JButton treinador = new JButton("Treinadores");
        JButton turma = new JButton("Turmas");
        JButton matricula = new JButton("Matrículas");
        JButton pagamento = new JButton("Pagamentos");
        JButton sair = new JButton("Sair");
        aluno.addActionListener(e -> new TelaAluno().setVisible(true));
        treinador.addActionListener(e -> new TelaTreinador().setVisible(true));
        turma.addActionListener(e -> new TelaTurma().setVisible(true));
        matricula.addActionListener(e -> new TelaMatricula().setVisible(true));
        pagamento.addActionListener(e -> new TelaPagamento().setVisible(true));
        sair.addActionListener(e -> dispose());
        painel.add(aluno); painel.add(treinador); painel.add(turma); painel.add(matricula); painel.add(pagamento); painel.add(sair);
        add(painel, BorderLayout.CENTER);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true)); }
}
