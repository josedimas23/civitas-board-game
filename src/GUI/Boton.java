package GUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * Botón redondeado dibujado a mano, con estado hover y pulsado.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
class Boton extends JButton {

    private final Color base;
    private final Color textoColor;
    private boolean encima, pulsado;

    Boton(String texto, Color base) {
        this(texto, base, base.getRed() * 0.299 + base.getGreen() * 0.587 + base.getBlue() * 0.114 > 160
                ? Tema.TEXTO_OSCURO : Color.WHITE);
    }

    Boton(String texto, Color base, Color textoColor) {
        super(texto);
        this.base = base;
        this.textoColor = textoColor;
        setFont(Tema.fuente(Font.BOLD, 14));
        setForeground(textoColor);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
            @Override public void mouseExited(MouseEvent e)  { encima = false; pulsado = false; repaint(); }
            @Override public void mousePressed(MouseEvent e) { pulsado = true; repaint(); }
            @Override public void mouseReleased(MouseEvent e){ pulsado = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Tema.suave(g);
        int w = getWidth(), h = getHeight();
        Color c = !isEnabled() ? Tema.TARJETA_ALT
                : pulsado ? Tema.mezcla(base, Color.BLACK, 0.18)
                : encima ? Tema.mezcla(base, Color.WHITE, 0.12) : base;
        int dy = pulsado ? 1 : 0;
        g2.setColor(Tema.alfa(Color.BLACK, 70));
        g2.fillRoundRect(0, 3, w, h - 3, 14, 14);
        g2.setColor(c);
        g2.fillRoundRect(0, dy, w, h - 3, 14, 14);
        g2.setFont(getFont());
        g2.setColor(isEnabled() ? textoColor : Tema.TEXTO_SUAVE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(getText(), (w - fm.stringWidth(getText())) / 2f,
                (h - 3 - fm.getHeight()) / 2f + fm.getAscent() + dy);
        g2.dispose();
    }
}
