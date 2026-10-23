package controle;

import javax.swing.SwingUtilities;

/** Compatibilidade com a tela de entrada antiga; abre o cadastro atual de alunos. */
public class Tela extends TelaAluno {
    public Tela() {
        super();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Tela().setVisible(true));
    }
}
