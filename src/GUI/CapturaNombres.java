package GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Pantalla de inicio: nombres de 2 a 4 jugadores, cada uno con su color de ficha.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
public class CapturaNombres extends JDialog {

    private static final int MAX = 4;

    private final ArrayList<String> nombres = new ArrayList<>();
    private final JTextField[] campos = new JTextField[MAX];
    private final Boton empezar = new Boton("Empezar partida", Tema.ORO);

    public CapturaNombres(Frame parent, boolean modal) {
        super(parent, "Civitas", modal);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { System.exit(0); }
        });
        getRootPane().putClientProperty("apple.awt.fullWindowContent", true);
        getRootPane().putClientProperty("apple.awt.transparentTitleBar", true);
        getRootPane().putClientProperty("apple.awt.windowTitleVisible", false);

        JPanel raiz = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = Tema.suave(g);
                g2.setPaint(new GradientPaint(0, 0, Tema.MESA, 0, getHeight(), Tema.FONDO));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        raiz.setLayout(new BoxLayout(raiz, BoxLayout.Y_AXIS));
        raiz.setBorder(new EmptyBorder(44, 40, 32, 40));

        JLabel titulo = new JLabel("CIVITAS");
        titulo.setFont(Tema.fuente(Font.BOLD, 44));
        titulo.setForeground(Tema.ORO);
        JLabel sub = new JLabel("Compra calles, construye casas y hoteles, y no te arruines.");
        sub.setFont(Tema.fuente(Font.PLAIN, 13));
        sub.setForeground(Tema.TEXTO_SUAVE);
        raiz.add(centrar(titulo));
        raiz.add(Box.createVerticalStrut(4));
        raiz.add(centrar(sub));
        raiz.add(Box.createVerticalStrut(28));

        DocumentListener alCambiar = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { valida(); }
            public void removeUpdate(DocumentEvent e) { valida(); }
            public void changedUpdate(DocumentEvent e) { valida(); }
        };
        for (int i = 0; i < MAX; i++) {
            campos[i] = new Campo("Jugador " + (i + 1) + (i >= 2 ? " (opcional)" : ""), Tema.colorJugador(i));
            campos[i].getDocument().addDocumentListener(alCambiar);
            campos[i].addActionListener(e -> empezar.doClick());
            raiz.add(campos[i]);
            raiz.add(Box.createVerticalStrut(10));
        }

        raiz.add(Box.createVerticalStrut(16));
        JPanel botones = new JPanel(new GridLayout(1, 2, 10, 0));
        botones.setOpaque(false);
        Boton salir = new Boton("Salir", Tema.TARJETA_ALT);
        salir.addActionListener(e -> System.exit(0));
        empezar.addActionListener(e -> empezar());
        botones.add(salir);
        botones.add(empezar);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        raiz.add(botones);

        setContentPane(raiz);
        setSize(460, 560);
        setResizable(false);
        setLocationRelativeTo(null);
        valida();
    }

    private static JComponent centrar(JComponent c) {
        c.setAlignmentX(CENTER_ALIGNMENT);
        return c;
    }

    private void valida() {
        int n = 0;
        for (JTextField c : campos) if (!c.getText().isBlank()) n++;
        empezar.setEnabled(n >= 2);
    }

    private void empezar() {
        if (!empezar.isEnabled()) return;
        nombres.clear();
        for (JTextField c : campos) if (!c.getText().isBlank()) nombres.add(c.getText().trim());
        dispose();
    }

    /** Muestra el diálogo (modal) y devuelve los nombres introducidos. */
    public ArrayList<String> getNombres() {
        setVisible(true);
        return nombres;
    }

    /** Campo de texto redondeado con placeholder y la ficha del jugador a la izquierda. */
    private static class Campo extends JTextField {
        private final String placeholder;
        private final Color color;

        Campo(String placeholder, Color color) {
            this.placeholder = placeholder;
            this.color = color;
            setOpaque(false);
            setFont(Tema.fuente(Font.PLAIN, 15));
            setForeground(Tema.TEXTO);
            setCaretColor(Tema.ORO);
            setBorder(new EmptyBorder(12, 48, 12, 14));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
            setAlignmentX(CENTER_ALIGNMENT);
            addFocusListener(new FocusAdapter() {
                @Override public void focusGained(FocusEvent e) { repaint(); }
                @Override public void focusLost(FocusEvent e) { repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Tema.suave(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(Tema.TARJETA);
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 14, 14));
            if (hasFocus()) {
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new RoundRectangle2D.Double(1, 1, w - 2, h - 2, 14, 14));
            }
            g2.setColor(color);
            g2.fill(new Ellipse2D.Double(16, h / 2.0 - 9, 18, 18));
            if (getText().isEmpty()) {
                g2.setColor(Tema.TEXTO_SUAVE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, 48, (h + fm.getAscent() - fm.getDescent()) / 2f);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
