package GUI;

import civitas.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

/**
 * Tarjetas de los jugadores: saldo (con animación al cambiar), propiedades y turno.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
class JugadoresPanel extends JPanel {

    private static final int ALTO = 70, SEP = 10;

    private CivitasJuego juego;
    private TableroPanel tablero;
    private final java.util.List<Jugador> orden = new ArrayList<>();
    private final Map<Jugador, Double> saldoVisible = new IdentityHashMap<>();
    private final Map<Jugador, Double> destello = new IdentityHashMap<>();

    JugadoresPanel() {
        setOpaque(false);
        new javax.swing.Timer(30, e -> anima()).start();
    }

    void setJuego(CivitasJuego juego, TableroPanel tablero) {
        this.juego = juego;
        this.tablero = tablero;
        orden.clear();
        orden.addAll(juego.getJugadores());   // orden fijo aunque el modelo reordene la lista
        for (Jugador j : orden) saldoVisible.put(j, (double) j.getSaldo());
        revalidate();
    }

    private void anima() {
        boolean cambio = false;
        for (Jugador j : orden) {
            double v = saldoVisible.getOrDefault(j, (double) j.getSaldo());
            double objetivo = j.getSaldo();
            if (Math.abs(objetivo - v) > 0.5) {
                if (!destello.containsKey(j)) destello.put(j, objetivo > v ? 1.0 : -1.0);
                saldoVisible.put(j, v + (objetivo - v) * 0.14);
                cambio = true;
            } else if (v != objetivo) {
                saldoVisible.put(j, objetivo);
                cambio = true;
            }
        }
        for (Iterator<Map.Entry<Jugador, Double>> it = destello.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Jugador, Double> e = it.next();
            double d = e.getValue() * 0.95;
            if (Math.abs(d) < 0.03) it.remove(); else e.setValue(d);
            cambio = true;
        }
        if (cambio) repaint();
    }

    private static String plural(int n, String s) {
        return n + " " + (n == 1 ? s : s.endsWith("l") ? s + "es" : s + "s");
    }

    @Override
    public Dimension getPreferredSize() {
        int n = Math.max(1, orden.size());
        return new Dimension(340, n * ALTO + (n - 1) * SEP);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (juego == null) return;
        Graphics2D g2 = Tema.suave(g);
        Jugador actual = juego.getJugadorActual();
        int w = getWidth();
        for (int i = 0; i < orden.size(); i++) {
            Jugador j = orden.get(i);
            int y = i * (ALTO + SEP);
            boolean turno = j == actual;
            Color col = tablero.colorDe(j);

            RoundRectangle2D caja = new RoundRectangle2D.Double(0, y, w - 1, ALTO, 18, 18);
            g2.setColor(turno ? Tema.TARJETA_ALT : Tema.TARJETA);
            g2.fill(caja);
            Double d = destello.get(j);
            if (d != null) {
                g2.setColor(Tema.alfa(d > 0 ? Tema.VERDE : Tema.ROJO, (int) (90 * Math.abs(d))));
                g2.fill(caja);
            }
            if (turno) {
                g2.setColor(col);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(caja);
            }

            // Ficha
            double r = 17;
            g2.setColor(col);
            g2.fill(new Ellipse2D.Double(16, y + ALTO / 2.0 - r, r * 2, r * 2));
            g2.setColor(Color.WHITE);
            g2.setFont(Tema.fuente(Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            String ini = j.getNombre().isBlank() ? "?" : j.getNombre().trim().substring(0, 1).toUpperCase();
            Tema.centrado(g2, ini, 16 + r, y + ALTO / 2.0 + (fm.getAscent() - fm.getDescent()) / 2.0);

            // Nombre y detalle
            g2.setColor(Tema.TEXTO);
            g2.setFont(Tema.fuente(Font.BOLD, 15));
            g2.drawString(j.getNombre(), 62, y + 30);
            int casas = 0, hoteles = 0;
            for (CasillaCalle c : j.getPropiedades()) { casas += c.getNumCasas(); hoteles += c.getNumHoteles(); }
            String det = plural(j.getPropiedades().size(), "calle") + " · " + plural(casas, "casa")
                    + " · " + plural(hoteles, "hotel");
            g2.setColor(Tema.TEXTO_SUAVE);
            g2.setFont(Tema.fuente(Font.PLAIN, 12));
            g2.drawString(det, 62, y + 50);

            if (j.Especulador()) {
                g2.setFont(Tema.fuente(Font.BOLD, 10));
                int bw = g2.getFontMetrics().stringWidth("ESPECULADOR") + 12;
                int bx = 62 + g2.getFontMetrics(Tema.fuente(Font.BOLD, 15)).stringWidth(j.getNombre()) + 8;
                g2.setColor(Tema.alfa(Tema.ORO, 60));
                g2.fill(new RoundRectangle2D.Double(bx, y + 17, bw, 17, 9, 9));
                g2.setColor(Tema.ORO);
                g2.drawString("ESPECULADOR", bx + 6, y + 30);
            }

            // Saldo
            String saldo = Tema.dinero((float) (double) saldoVisible.getOrDefault(j, (double) j.getSaldo()));
            g2.setFont(Tema.fuente(Font.BOLD, 17));
            g2.setColor(j.getSaldo() <= 0 ? Tema.ROJO : turno ? Tema.ORO : Tema.TEXTO);
            g2.drawString(saldo, w - 16 - g2.getFontMetrics().stringWidth(saldo), y + 41);
        }
        g2.dispose();
    }
}
