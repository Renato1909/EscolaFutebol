package controle;

import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Classe auxiliar simples para carregar os ícones PNG usados nos botões.
 * Os arquivos ficam em src/controle/icones.
 */
public final class Icones {
    private Icones() {
    }

    public static ImageIcon carregar(String nome) {
        URL url = Icones.class.getResource("/controle/icones/" + nome + ".png");
        return url == null ? null : new ImageIcon(url);
    }

    public static JButton botao(String texto, String nomeIcone) {
        JButton botao = new JButton(texto, carregar(nomeIcone));
        botao.setIconTextGap(6);
        return botao;
    }

    // Botões simples usados para navegar entre os registros da tabela.
    public static JButton navegacao(String texto, boolean azul) {
        JButton botao = new JButton(texto);
        botao.setBackground(azul ? new java.awt.Color(0, 102, 255)
                                  : new java.awt.Color(0, 153, 153));
        botao.setForeground(java.awt.Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setPreferredSize(new java.awt.Dimension(55, 31));
        return botao;
    }
}
