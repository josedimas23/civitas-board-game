package GUI;

import java.awt.*;

/**
 * Paleta, tipografías y utilidades de dibujo compartidas por toda la interfaz.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
final class Tema {

    private Tema() {}

    // Superficies
    static final Color FONDO       = new Color(0x12141C);
    static final Color PANEL       = new Color(0x1A1E29);
    static final Color TARJETA     = new Color(0x232837);
    static final Color TARJETA_ALT = new Color(0x2C3244);
    static final Color BORDE       = new Color(0x343B50);

    // Texto
    static final Color TEXTO       = new Color(0xE9EBF2);
    static final Color TEXTO_SUAVE = new Color(0x8B92A6);
    static final Color TEXTO_OSCURO = new Color(0x22252E);

    // Acentos
    static final Color ORO     = new Color(0xF5B83D);
    static final Color VERDE   = new Color(0x34C281);
    static final Color ROJO    = new Color(0xEF5B5B);
    static final Color AZUL    = new Color(0x5B8DEF);

    // Tablero
    static final Color MESA    = new Color(0x173B2F);
    static final Color MESA_LUZ = new Color(0x22553F);
    static final Color LOSA    = new Color(0xFAF6EC);
    static final Color LOSA_ESPECIAL = new Color(0xEFE7D3);

    /** Colores de los grupos de calles, ordenados de más barato a más caro. */
    static final Color[] GRUPOS = {
        new Color(0x8D5A3B), new Color(0x4FB3E8), new Color(0xE05297),
        new Color(0xF28C28), new Color(0xE2453C), new Color(0xE8B923),
        new Color(0x2FA36B), new Color(0x2F5DD8)
    };

    /** Color de ficha de cada jugador. */
    static final Color[] JUGADORES = {
        new Color(0xFF6B6B), new Color(0x2EC4B6), new Color(0x9B5DE5), new Color(0xFFB703)
    };

    static Color colorJugador(int i) {
        return JUGADORES[Math.floorMod(i, JUGADORES.length)];
    }

    static Font fuente(int estilo, double tamaño) {
        return new Font(Font.SANS_SERIF, estilo, 12).deriveFont((float) tamaño);
    }

    static Graphics2D suave(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }

    static Color mezcla(Color a, Color b, double t) {
        t = Math.max(0, Math.min(1, t));
        return new Color(
            (int) Math.round(a.getRed()   + (b.getRed()   - a.getRed())   * t),
            (int) Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t),
            (int) Math.round(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t),
            (int) Math.round(a.getAlpha() + (b.getAlpha() - a.getAlpha()) * t));
    }

    static Color alfa(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    /** Dibuja un texto centrado horizontalmente en x, con la línea base en y. */
    static void centrado(Graphics2D g, String s, double x, double y) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (float) (x - fm.stringWidth(s) / 2.0), (float) y);
    }

    /** Limpia los nombres del modelo (" madrid " -> "Madrid"). */
    static String nombreBonito(String s) {
        String t = s.trim().replaceAll("\\s*\\.$", "");
        return t.isEmpty() ? t : Character.toUpperCase(t.charAt(0)) + t.substring(1);
    }

    static String dinero(float v) {
        return String.format("%,d €", Math.round(v)).replace(',', '.');
    }
}
