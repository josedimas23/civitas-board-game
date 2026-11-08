package GUI;

import civitas.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * Tablero dibujado con Java2D: casillas en anillo, propietarios, casas y hoteles,
 * fichas animadas y un dado en el centro.
 *
 * @author Jose Dimas Sánchez Casas y Álvaro R.
 */
class TableroPanel extends JPanel {

    private static final long MS_DADO = 750, MS_PAUSA_DADO = 250, MS_POR_CASILLA = 210;

    private CivitasJuego juego;
    private final Map<Jugador, Integer> colorDe = new IdentityHashMap<>();
    private final Map<Casilla, Color> grupoDe = new IdentityHashMap<>();

    /** Posición dibujada de cada ficha (puede ser fraccionaria mientras se mueve). */
    private final Map<Jugador, Double> posVisual = new IdentityHashMap<>();

    private int caraDado = 0;              // 0 = aún no se ha tirado
    private double giroDado = 0;
    private Jugador moviendo = null;
    private List<Jugador> ranking = null;
    private double fase = 0;               // para el brillo pulsante

    private javax.swing.Timer animacion;

    TableroPanel() {
        setBackground(Tema.FONDO);
        setPreferredSize(new Dimension(720, 720));
        new javax.swing.Timer(40, e -> { fase += 0.08; repaint(); }).start();
    }

    void setJuego(CivitasJuego juego) {
        this.juego = juego;
        List<Jugador> js = juego.getJugadores();
        for (int i = 0; i < js.size(); i++) colorDe.put(js.get(i), i);
        int calle = 0;
        for (int i = 0; i < juego.getTablero().getNumCasillas(); i++) {
            Casilla c = juego.getTablero().getCasilla(i);
            if (c instanceof CasillaCalle)
                grupoDe.put(c, Tema.GRUPOS[Math.min(calle++ / 2, Tema.GRUPOS.length - 1)]);
        }
        sincronizar();
    }

    Color colorDe(Jugador j) {
        Integer i = colorDe.get(j);
        return Tema.colorJugador(i != null ? i : juego.getJugadores().indexOf(j));
    }

    /** Coloca cada ficha en su casilla real (sin animación). */
    void sincronizar() {
        for (Jugador j : juego.getJugadores())
            if (j != moviendo) posVisual.put(j, (double) j.getCasillaActual());
    }

    void mostrarRanking(List<Jugador> r) {
        ranking = r;
        repaint();
    }

    /**
     * Anima la tirada del dado y el avance de la ficha casilla a casilla.
     * Se llama desde el EDT; {@code alTerminar} se ejecuta al acabar.
     */
    void animarTirada(Jugador j, int desde, int hasta, Runnable alTerminar) {
        int n = juego.getTablero().getNumCasillas();
        int pasos = Math.floorMod(hasta - desde, n);
        int tirada = pasos == 0 ? n : pasos;
        Random rnd = new Random();
        moviendo = j;
        posVisual.put(j, (double) desde);
        long t0 = System.nanoTime();
        if (animacion != null) animacion.stop();
        animacion = new javax.swing.Timer(16, null);
        animacion.addActionListener(e -> {
            long ms = (System.nanoTime() - t0) / 1_000_000;
            if (ms < MS_DADO) {
                if (rnd.nextInt(4) == 0) caraDado = 1 + rnd.nextInt(6);
                giroDado = Math.sin(ms / 60.0) * 0.35 * (1 - ms / (double) MS_DADO);
            } else {
                caraDado = tirada;
                giroDado = 0;
                long mov = ms - MS_DADO - MS_PAUSA_DADO;
                if (mov >= 0) {
                    double avance = Math.min(pasos, mov / (double) MS_POR_CASILLA);
                    posVisual.put(j, desde + avance);
                    if (avance >= pasos) {
                        animacion.stop();
                        moviendo = null;
                        posVisual.put(j, (double) hasta);
                        repaint();
                        alTerminar.run();
                        return;
                    }
                }
            }
            repaint();
        });
        animacion.start();
    }

    // ------------------------------------------------------------------ geometría

    private int lado() {
        int n = juego.getTablero().getNumCasillas();
        return (int) Math.ceil(n / 4.0) + 1;
    }

    /** Fila y columna de la casilla i, recorriendo el anillo en sentido horario desde abajo a la derecha. */
    private Point celda(int i) {
        int L = lado() - 1;
        int lado = i / L, k = i % L;
        switch (lado) {
            case 0:  return new Point(L - k, L);   // abajo, de derecha a izquierda
            case 1:  return new Point(0, L - k);   // izquierda, de abajo a arriba
            case 2:  return new Point(k, 0);       // arriba, de izquierda a derecha
            default: return new Point(L, k);       // derecha, de arriba a abajo
        }
    }

    private Rectangle2D rect(int i, double x0, double y0, double c) {
        Point p = celda(i);
        return new Rectangle2D.Double(x0 + p.x * c, y0 + p.y * c, c, c);
    }

    // ------------------------------------------------------------------ dibujo

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (juego == null) return;
        Graphics2D g2 = Tema.suave(g);

        int n = juego.getTablero().getNumCasillas();
        int L = lado();
        double S = Math.min(getWidth(), getHeight()) - 36;
        double c = S / L;
        double x0 = (getWidth() - S) / 2, y0 = (getHeight() - S) / 2;

        // Mesa
        g2.setColor(Tema.alfa(Color.BLACK, 90));
        g2.fill(new RoundRectangle2D.Double(x0 - 8, y0 - 2, S + 16, S + 16, 34, 34));
        g2.setPaint(new RadialGradientPaint(new Point2D.Double(x0 + S / 2, y0 + S / 2), (float) (S * 0.75),
                new float[]{0f, 1f}, new Color[]{Tema.MESA_LUZ, Tema.MESA}));
        g2.fill(new RoundRectangle2D.Double(x0 - 8, y0 - 8, S + 16, S + 16, 34, 34));

        Jugador actual = juego.getJugadorActual();
        for (int i = 0; i < n; i++)
            dibujaCasilla(g2, i, rect(i, x0, y0, c), c, actual);

        dibujaCentro(g2, x0 + c, y0 + c, S - 2 * c, c);

        // Fichas: primero las que no son del turno, encima la del jugador actual
        List<Jugador> js = new ArrayList<>(juego.getJugadores());
        js.sort(Comparator.comparingInt(j -> j == actual ? 1 : 0));
        for (Jugador j : js) dibujaFicha(g2, j, x0, y0, c, n, j == actual);

        if (ranking != null) dibujaRanking(g2, x0 + c, y0 + c, S - 2 * c);
        g2.dispose();
    }

    private void dibujaCasilla(Graphics2D g, int i, Rectangle2D r, double c, Jugador actual) {
        Casilla cas = juego.getTablero().getCasilla(i);
        double m = Math.max(2, c * 0.035);
        RoundRectangle2D losa = new RoundRectangle2D.Double(r.getX() + m, r.getY() + m,
                r.getWidth() - 2 * m, r.getHeight() - 2 * m, c * 0.16, c * 0.16);

        // Brillo de la casilla del jugador en turno
        if (moviendo == null && actual.getCasillaActual() == i && ranking == null) {
            Color col = colorDe(actual);
            double a = 0.35 + 0.3 * Math.sin(fase * 2);
            for (int k = 3; k >= 1; k--) {
                double o = k * c * 0.035;
                g.setColor(Tema.alfa(col, (int) (255 * a / k)));
                g.fill(new RoundRectangle2D.Double(losa.getX() - o, losa.getY() - o,
                        losa.getWidth() + 2 * o, losa.getHeight() + 2 * o, c * 0.16 + o, c * 0.16 + o));
            }
        }

        g.setColor(Tema.alfa(Color.BLACK, 60));
        g.fill(new RoundRectangle2D.Double(losa.getX(), losa.getY() + 3, losa.getWidth(), losa.getHeight(), c * 0.16, c * 0.16));

        boolean calle = cas instanceof CasillaCalle;
        g.setColor(calle ? Tema.LOSA : Tema.LOSA_ESPECIAL);
        g.fill(losa);

        double cx = losa.getCenterX();
        double top = losa.getY(), w = losa.getWidth();
        String nombre = Tema.nombreBonito(cas.getNombre());

        if (calle) {
            CasillaCalle cc = (CasillaCalle) cas;
            Color grupo = grupoDe.getOrDefault(cas, Tema.GRUPOS[0]);
            Shape banda = new Area(losa);
            ((Area) banda).intersect(new Area(new Rectangle2D.Double(losa.getX(), top, w, c * 0.24)));
            g.setColor(grupo);
            g.fill(banda);

            // Casas y hoteles dentro de la banda
            int casas = cc.getNumCasas(), hoteles = cc.getNumHoteles();
            double s = c * 0.11, gap = s * 0.35;
            int total = Math.min(casas + hoteles, 6);
            double bx = cx - (total * s + (total - 1) * gap) / 2;
            for (int k = 0; k < total; k++) {
                boolean hotel = k < hoteles;
                g.setColor(hotel ? Tema.ROJO : Tema.VERDE);
                double hw = hotel ? s * 1.2 : s;
                g.fill(new RoundRectangle2D.Double(bx + k * (s + gap), top + c * 0.065, hw, s, 3, 3));
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(1f));
                g.draw(new RoundRectangle2D.Double(bx + k * (s + gap), top + c * 0.065, hw, s, 3, 3));
            }

            g.setColor(Tema.TEXTO_OSCURO);
            g.setFont(ajusta(g, nombre, Font.BOLD, c * 0.15, w - 8));
            Tema.centrado(g, nombre, cx, top + c * 0.43);
            g.setFont(Tema.fuente(Font.PLAIN, c * 0.12));
            g.setColor(new Color(0x6B6F7A));
            Tema.centrado(g, Tema.dinero(cc.getPrecioCompra()), cx, top + c * 0.58);

            Jugador dueño = propietario(cc);
            if (dueño != null) {
                Color col = colorDe(dueño);
                g.setColor(col);
                g.setStroke(new BasicStroke((float) Math.max(2.5, c * 0.04)));
                g.draw(losa);
                g.fill(new Ellipse2D.Double(losa.getMaxX() - c * 0.17, losa.getMaxY() - c * 0.17, c * 0.1, c * 0.1));
            }
        } else {
            String lower = nombre.toLowerCase();
            Color icono; String simbolo;
            if (lower.contains("salida"))      { icono = Tema.VERDE; simbolo = "➜"; }
            else if (lower.contains("parking")) { icono = Tema.AZUL;  simbolo = "P"; }
            else if (lower.contains("descanso")) { icono = new Color(0x9B5DE5); simbolo = "z"; }
            else                               { icono = Tema.ORO;   simbolo = "?"; }
            double d = c * 0.36;
            g.setColor(icono);
            g.fill(new Ellipse2D.Double(cx - d / 2, top + c * 0.1, d, d));
            g.setColor(Color.WHITE);
            g.setFont(Tema.fuente(Font.BOLD, d * 0.62));
            FontMetrics fm = g.getFontMetrics();
            Tema.centrado(g, simbolo, cx, top + c * 0.1 + (d + fm.getAscent() - fm.getDescent()) / 2);
            g.setColor(Tema.TEXTO_OSCURO);
            g.setFont(ajusta(g, nombre.toUpperCase(), Font.BOLD, c * 0.12, w - 8));
            Tema.centrado(g, nombre.toUpperCase(), cx, top + c * 0.62);
        }
    }

    private Jugador propietario(CasillaCalle cc) {
        if (!cc.tienePropietario()) return null;
        for (Jugador j : juego.getJugadores()) if (cc.esEsteElPropietario(j)) return j;
        return null;
    }

    private Font ajusta(Graphics2D g, String s, int estilo, double tam, double ancho) {
        Font f = Tema.fuente(estilo, tam);
        while (g.getFontMetrics(f).stringWidth(s) > ancho && f.getSize() > 7)
            f = Tema.fuente(estilo, f.getSize() - 1);
        return f;
    }

    private void dibujaFicha(Graphics2D g, Jugador j, double x0, double y0, double c, int n, boolean enTurno) {
        double p = posVisual.getOrDefault(j, (double) j.getCasillaActual());
        int a = (int) Math.floor(p);
        double t = p - a;
        Rectangle2D ra = rect(Math.floorMod(a, n), x0, y0, c);
        Rectangle2D rb = rect(Math.floorMod(a + 1, n), x0, y0, c);
        double cx = ra.getCenterX() + (rb.getCenterX() - ra.getCenterX()) * t;
        double cy = ra.getCenterY() + (rb.getCenterY() - ra.getCenterY()) * t;

        int idx = colorDe.getOrDefault(j, 0);
        double r = c * 0.105;
        double fx = cx + (idx - 1.5) * c * 0.205;
        double fy = cy + c * 0.29 - (j == moviendo ? Math.sin(t * Math.PI) * c * 0.28 : 0);

        Color col = colorDe(j);
        g.setColor(Tema.alfa(Color.BLACK, 80));
        g.fill(new Ellipse2D.Double(fx - r, cy + c * 0.29 - r + 3, r * 2, r * 2));
        g.setColor(col);
        g.fill(new Ellipse2D.Double(fx - r, fy - r, r * 2, r * 2));
        g.setColor(enTurno ? Color.WHITE : Tema.mezcla(col, Color.BLACK, 0.35));
        g.setStroke(new BasicStroke(enTurno ? 2.5f : 1.5f));
        g.draw(new Ellipse2D.Double(fx - r, fy - r, r * 2, r * 2));
        String ini = j.getNombre().isBlank() ? "?" : j.getNombre().trim().substring(0, 1).toUpperCase();
        g.setColor(Color.WHITE);
        g.setFont(Tema.fuente(Font.BOLD, r * 1.15));
        FontMetrics fm = g.getFontMetrics();
        Tema.centrado(g, ini, fx, fy + (fm.getAscent() - fm.getDescent()) / 2.0);
    }

    private void dibujaCentro(Graphics2D g, double x, double y, double s, double c) {
        double cx = x + s / 2;

        g.setColor(Tema.ORO);
        g.setFont(Tema.fuente(Font.BOLD, s * 0.13));
        Tema.centrado(g, "CIVITAS", cx, y + s * 0.24);
        g.setColor(Tema.alfa(Color.WHITE, 150));
        g.setFont(Tema.fuente(Font.PLAIN, s * 0.038));
        Tema.centrado(g, "juego de gestión inmobiliaria", cx, y + s * 0.31);

        // Dado
        double d = c * 0.95, dy = y + s * 0.52;
        AffineTransform viejo = g.getTransform();
        g.rotate(giroDado, cx, dy);
        g.setColor(Tema.alfa(Color.BLACK, 90));
        g.fill(new RoundRectangle2D.Double(cx - d / 2, dy - d / 2 + 5, d, d, d * 0.24, d * 0.24));
        g.setColor(Color.WHITE);
        g.fill(new RoundRectangle2D.Double(cx - d / 2, dy - d / 2, d, d, d * 0.24, d * 0.24));
        dibujaPuntos(g, caraDado, cx, dy, d);
        g.setTransform(viejo);

        if (ranking == null) {
            Jugador a = juego.getJugadorActual();
            String txt = "Turno de " + a.getNombre();
            g.setFont(Tema.fuente(Font.BOLD, s * 0.05));
            FontMetrics fm = g.getFontMetrics();
            double pw = fm.stringWidth(txt) + s * 0.1, ph = s * 0.085, py = y + s * 0.74;
            g.setColor(Tema.alfa(Color.BLACK, 110));
            g.fill(new RoundRectangle2D.Double(cx - pw / 2, py, pw, ph, ph, ph));
            g.setColor(colorDe(a));
            g.fill(new Ellipse2D.Double(cx - pw / 2 + ph * 0.3, py + ph * 0.3, ph * 0.4, ph * 0.4));
            g.setColor(Color.WHITE);
            Tema.centrado(g, txt, cx + ph * 0.2, py + (ph + fm.getAscent() - fm.getDescent()) / 2);
        }
    }

    private void dibujaPuntos(Graphics2D g, int cara, double cx, double cy, double d) {
        g.setColor(Tema.TEXTO_OSCURO);
        if (cara == 0) {
            g.setFont(Tema.fuente(Font.BOLD, d * 0.5));
            FontMetrics fm = g.getFontMetrics();
            Tema.centrado(g, "?", cx, cy + (fm.getAscent() - fm.getDescent()) / 2.0);
            return;
        }
        double o = d * 0.27, r = d * 0.085;
        int[][] mapa = {
            {}, {0, 0}, {-1, -1, 1, 1}, {-1, -1, 0, 0, 1, 1}, {-1, -1, 1, -1, -1, 1, 1, 1},
            {-1, -1, 1, -1, 0, 0, -1, 1, 1, 1}, {-1, -1, 1, -1, -1, 0, 1, 0, -1, 1, 1, 1}
        };
        int[] pts = mapa[Math.max(1, Math.min(6, cara))];
        for (int k = 0; k < pts.length; k += 2)
            g.fill(new Ellipse2D.Double(cx + pts[k] * o - r, cy + pts[k + 1] * o - r, r * 2, r * 2));
    }

    private void dibujaRanking(Graphics2D g, double x, double y, double s) {
        double m = s * 0.06;
        RoundRectangle2D caja = new RoundRectangle2D.Double(x + m, y + m, s - 2 * m, s - 2 * m, 28, 28);
        g.setColor(Tema.alfa(Tema.FONDO, 235));
        g.fill(caja);
        g.setColor(Tema.ORO);
        g.setStroke(new BasicStroke(2f));
        g.draw(caja);

        double cx = x + s / 2;
        g.setFont(Tema.fuente(Font.BOLD, s * 0.075));
        Tema.centrado(g, "Fin de la partida", cx, y + s * 0.2);
        String[] medallas = {"1º", "2º", "3º", "4º"};
        double fila = s * 0.11;
        for (int i = 0; i < ranking.size(); i++) {
            Jugador j = ranking.get(i);
            double fy = y + s * 0.3 + i * fila;
            g.setColor(i == 0 ? Tema.alfa(Tema.ORO, 50) : Tema.alfa(Color.WHITE, 12));
            g.fill(new RoundRectangle2D.Double(x + s * 0.14, fy, s * 0.72, fila * 0.82, 16, 16));
            g.setFont(Tema.fuente(Font.BOLD, s * 0.045));
            FontMetrics fm = g.getFontMetrics();
            double base = fy + (fila * 0.82 + fm.getAscent() - fm.getDescent()) / 2;
            g.setColor(i == 0 ? Tema.ORO : Tema.TEXTO_SUAVE);
            g.drawString(i < medallas.length ? medallas[i] : (i + 1) + "º", (float) (x + s * 0.18), (float) base);
            g.setColor(colorDe(j));
            g.fill(new Ellipse2D.Double(x + s * 0.27, base - fm.getAscent() * 0.75, fm.getAscent() * 0.7, fm.getAscent() * 0.7));
            g.setColor(Tema.TEXTO);
            g.drawString(j.getNombre(), (float) (x + s * 0.33), (float) base);
            String saldo = Tema.dinero(j.getSaldo());
            g.drawString(saldo, (float) (x + s * 0.82 - fm.stringWidth(saldo)), (float) base);
        }
    }
}
